package org.kharitonov.ms_jwt_auth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Kharitonov Pavel on 03.03.2024.
 */
@RestController
@RequestMapping("/api/v1/test")
public class TestAuthController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello world";
    }

    @GetMapping("/hello/admin")
    public String helloAdmin() {
        return "Hello Admin";
    }

}
