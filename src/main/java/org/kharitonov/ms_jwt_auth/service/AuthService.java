package org.kharitonov.ms_jwt_auth.service;

import lombok.RequiredArgsConstructor;
import org.kharitonov.ms_jwt_auth.exceptions.UsernameNotFoundException;
import org.kharitonov.ms_jwt_auth.mapper.UserMapper;
import org.kharitonov.ms_jwt_auth.model.User;
import org.kharitonov.ms_jwt_auth.model.UserRole;
import org.kharitonov.ms_jwt_auth.model.dto.JwtAuthenticationResponse;
import org.kharitonov.ms_jwt_auth.model.dto.SignInRequest;
import org.kharitonov.ms_jwt_auth.model.dto.SignUpRequest;
import org.kharitonov.ms_jwt_auth.security.JwtProvider;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * @author Kharitonov Pavel on 03.03.2024.
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final UserMapper mapper;
    private final JwtProvider jwtProvider;

    public JwtAuthenticationResponse signUp(SignUpRequest request) {
        User user = mapper.dtoToUser(request);
        userService.createUser(user);
        return jwtProvider.generateToken(user);
    }

    public JwtAuthenticationResponse signIn(SignInRequest request) {
        User user = mapper.dtoToUser(request);
        User dbUser;
        try {
            dbUser = userService.findByUsername(user.getUsername());
        } catch (UsernameNotFoundException e) {
            return new JwtAuthenticationResponse(e.getMessage());
        }
        if (dbUser.getPassword().equals(user.getPassword())) {
            return jwtProvider.generateToken(dbUser);
        }
        return new JwtAuthenticationResponse("Пароль указан неверно!");
    }

    public String makeAdmin(String username) {
        User user = userService.findByUsername(username);
        user.setRoles(Collections.singleton(UserRole.ROLE_ADMIN));
        return username + "is now admin";
    }
}
