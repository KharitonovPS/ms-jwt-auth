package org.kharitonov.ms_jwt_auth.controller;

import org.kharitonov.ms_jwt_auth.model.dto.JwtAuthenticationResponse;
import org.kharitonov.ms_jwt_auth.model.dto.SignUpRequest;
import org.kharitonov.ms_jwt_auth.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Kharitonov Pavel on 03.03.2024.
 */
@RestController
@RequestMapping("/api/v1/registration")
public class RegistrationController {

    private final UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping()
    public JwtAuthenticationResponse signUp(@RequestBody SignUpRequest request){
        return userService.signUp(request);
    }
}
