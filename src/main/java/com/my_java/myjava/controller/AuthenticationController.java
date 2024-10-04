package com.my_java.myjava.controller;

import com.my_java.myjava.dto.request.ApiResponse;
import com.my_java.myjava.dto.request.AuthenticationRequest;
import com.my_java.myjava.dto.request.IntrospectRequest;
import com.my_java.myjava.dto.response.AuthenticationResponse;
import com.my_java.myjava.dto.response.IntrospectResponse;
import com.my_java.myjava.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {

    AuthenticationService authenticationService;

    @PostMapping("/log-in")
    ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest authenticationRequest) {
        var res =  authenticationService.authenticate(authenticationRequest);

        return ApiResponse.<AuthenticationResponse>builder()
                .data(res)
                .build();
    }

    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> authenticate(@RequestBody IntrospectRequest introspectRequest) throws ParseException, JOSEException {
        var res =  authenticationService.introspect(introspectRequest);

        return ApiResponse.<IntrospectResponse>builder()
                .data(res)
                .build();
    }
}
