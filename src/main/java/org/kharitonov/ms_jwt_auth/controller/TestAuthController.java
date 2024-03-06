package org.kharitonov.ms_jwt_auth.controller;

import lombok.RequiredArgsConstructor;
import org.kharitonov.ms_jwt_auth.service.AuthService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Kharitonov Pavel on 03.03.2024.
 */
@RestController
@RequestMapping("/api/v1/hello")
@RequiredArgsConstructor
public class TestAuthController {

    private final AuthService authService;

    @GetMapping("")
    public String hello() {
        return "Hello world";
    }

    @GetMapping("/admin")
    public String helloAdmin() {
        return "Hello Admin";
    }

    @GetMapping("/makeAdmin/{username}")
    public String makeAdmin(@PathVariable ("username") String username) {
        return authService.makeAdmin(username);
    }



}
