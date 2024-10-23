package com.my_java.myjava.service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.my_java.myjava.dto.request.AuthenticationRequest;
import com.my_java.myjava.dto.request.IntrospectRequest;
import com.my_java.myjava.dto.request.LogoutRequest;
import com.my_java.myjava.dto.request.RefreshTokenRequest;
import com.my_java.myjava.dto.response.AuthenticationResponse;
import com.my_java.myjava.dto.response.IntrospectResponse;
import com.my_java.myjava.entity.InvalidatedToken;
import com.my_java.myjava.entity.User;
import com.my_java.myjava.exception.AppException;
import com.my_java.myjava.exception.ErrorCode;
import com.my_java.myjava.repository.InvalidatedTokenRepository;
import com.my_java.myjava.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthenticationService {

    UserRepository userRepository;
    InvalidatedTokenRepository invalidatedTokenRepository;

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    @NonFinal
    @Value("${jwt.valid-duration}")
    protected long VALID_DURATION;

    @NonFinal
    @Value("${jwt.refreshable-duration}")
    protected long REFRESHABLE_DURATION;

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        log.info(SIGNER_KEY);
        // Get user by username
        var user = userRepository
                .findByUsername(authenticationRequest.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // Check password
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean authenticate = passwordEncoder.matches(authenticationRequest.getPassword(), user.getPassword());

        if (!authenticate) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        // Generate a token form user
        var token = generateToken(user);

        return AuthenticationResponse.builder().token(token).authenticated(true).build();
    }

    public IntrospectResponse introspect(IntrospectRequest introspectRequest) throws JOSEException, ParseException {

        var token = introspectRequest.getToken(); // Lấy token từ request

        boolean isValid = true;

        try {
            verifyToken(token, false);
        } catch (AppException e) {
            isValid = false;
        }

        return IntrospectResponse.builder().valid(isValid).build();
    }

    public void logout(LogoutRequest logoutRequest) throws ParseException, JOSEException {
        try {
            var signToken = verifyToken(logoutRequest.getToken(), true);

            String jit = signToken.getJWTClaimsSet().getJWTID();
            Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();

            InvalidatedToken invalidatedToken =
                    InvalidatedToken.builder().id(jit).expiryTime(expiryTime).build();

            invalidatedTokenRepository.save(invalidatedToken);
        } catch (AppException exception) {
            log.info("Token already expired");
        }
    }

    public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest)
            throws ParseException, JOSEException {

        var signedJWT = verifyToken(refreshTokenRequest.getToken(), true);

        // Logout old token
        var jit = signedJWT.getJWTClaimsSet().getJWTID();
        var expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        InvalidatedToken invalidatedToken =
                InvalidatedToken.builder().id(jit).expiryTime(expiryTime).build();

        invalidatedTokenRepository.save(invalidatedToken);

        // Generate new token
        var username = signedJWT.getJWTClaimsSet().getSubject();
        var user =
                userRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));
        var token = generateToken(user);

        return AuthenticationResponse.builder().token(token).authenticated(true).build();
    }

    public String generateToken(User user) {
        // Generate header
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        // Generate claimSet
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("phucdev.com")
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()))
                .jwtID(UUID.randomUUID().toString()) // tạo id cho token to logout
                .claim("scope", buildScope(user))
                .build();

        // Generate payload
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        // Generate jwt token
        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }

    private String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");
        if (!CollectionUtils.isEmpty(user.getRoles()))
            user.getRoles().forEach(role -> {
                stringJoiner.add("ROLE_" + role.getName());
                if (!CollectionUtils.isEmpty(role.getPermissions()))
                    role.getPermissions().forEach(permission -> stringJoiner.add(permission.getName()));
            });

        return stringJoiner.toString();
    }

    private SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expiryTime = (isRefresh)
                ? new Date(signedJWT
                        .getJWTClaimsSet()
                        .getIssueTime()
                        .toInstant()
                        .plus(REFRESHABLE_DURATION, ChronoUnit.SECONDS)
                        .toEpochMilli())
                : signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified = signedJWT.verify(verifier); // Check

        if (!(verified && expiryTime.after(new Date()))) throw new AppException(ErrorCode.UNAUTHENTICATED);

        if (invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID()))
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        return signedJWT;
    }
}
