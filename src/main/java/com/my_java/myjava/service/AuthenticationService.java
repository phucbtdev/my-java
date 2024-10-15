package com.my_java.myjava.service;

import com.my_java.myjava.dto.request.AuthenticationRequest;
import com.my_java.myjava.dto.request.IntrospectRequest;
import com.my_java.myjava.dto.response.AuthenticationResponse;
import com.my_java.myjava.dto.response.IntrospectResponse;
import com.my_java.myjava.entity.User;
import com.my_java.myjava.exception.AppException;
import com.my_java.myjava.exception.ErrorCode;
import com.my_java.myjava.repository.UserRepository;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nimbusds.jose.*;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {

    UserRepository userRepository;

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        //Get user by username
        var user =  userRepository.findByUsername(authenticationRequest.getUsername())
                .orElseThrow(()-> new AppException(ErrorCode.USER_NOT_EXISTED));

        //Check password
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean authenticate =  passwordEncoder.matches(authenticationRequest.getPassword(), user.getPassword());

        if(!authenticate) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        //Generate a token form user
        var token = generateToken(user);

        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .build();
    }

    //Generate token
    public String generateToken(User user) {
        //Generate header
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        //Generate claimSet
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("phucdev.com")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()
                ))
                .claim("scope",buildScope(user))
                .build();


        //Generate payload
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        //Generate jwt token
        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }

    }

    //Check token
    public IntrospectResponse introspect(IntrospectRequest introspectRequest) throws JOSEException, ParseException {

        var token = introspectRequest.getToken(); //Lấy token từ request
        SignedJWT signedJWT = SignedJWT.parse(token); //Parse token
        Date expireTime = signedJWT.getJWTClaimsSet().getExpirationTime(); //Lấy thời gian của token

        JWSVerifier jwsVerifier = new MACVerifier(SIGNER_KEY.getBytes()); // Mã hóa sign_key
        var verified = signedJWT.verify(jwsVerifier); //Xác thực 2 signer_key

        return IntrospectResponse.builder()
                .valid(verified && expireTime.after(new Date()))
                .build();
    }

    //Generate scope vd: ADMIN, USER, MANAGER
    private String buildScope(User user){
        StringJoiner stringJoiner = new StringJoiner(" ");
//        if (!CollectionUtils.isEmpty(user.getRoles()))
//            user.getRoles().forEach(stringJoiner::add);

        return stringJoiner.toString();
    }
}
