package org.kharitonov.ms_jwt_auth.service;

import lombok.RequiredArgsConstructor;
import org.kharitonov.ms_jwt_auth.mapper.UserMapper;
import org.kharitonov.ms_jwt_auth.model.User;
import org.kharitonov.ms_jwt_auth.model.dto.JwtAuthenticationResponse;
import org.kharitonov.ms_jwt_auth.model.dto.SignInRequest;
import org.kharitonov.ms_jwt_auth.model.dto.SignUpRequest;
import org.kharitonov.ms_jwt_auth.security.JwtProvider;
import org.springframework.stereotype.Service;

/**
 * @author Kharitonov Pave
 * l on 03.03.2024.
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
        User dbUser = userService.findByUsername(user.getUsername());
        if (dbUser.getPassword().equals(user.getPassword())) {
            return jwtProvider.generateToken(user);
        }
        return null;
    }
}
