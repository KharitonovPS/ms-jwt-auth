package org.kharitonov.ms_jwt_auth.controller;

import org.kharitonov.ms_jwt_auth.model.dto.JwtAuthenticationResponse;
import org.kharitonov.ms_jwt_auth.model.dto.SignInRequest;
import org.kharitonov.ms_jwt_auth.model.dto.SignUpRequest;
import org.kharitonov.ms_jwt_auth.service.AuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Kharitonov Pavel on 03.03.2024.
 */
@RestController
@RequestMapping("/api/v1/auth")
public class RegistrationController {

    private final AuthService authService;

    public RegistrationController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/sign-up")
    public JwtAuthenticationResponse signUp(@RequestBody SignUpRequest request){
        return authService.signUp(request);
    }

    @PostMapping("/sign-in")
    public JwtAuthenticationResponse signIn(@RequestBody SignInRequest request){
        return authService.signIn(request);
    }
}
