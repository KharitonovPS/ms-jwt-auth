package org.kharitonov.ms_jwt_auth.service;

import org.kharitonov.ms_jwt_auth.exceptions.UsernameNotFoundException;
import org.kharitonov.ms_jwt_auth.mapper.UserMapper;
import org.kharitonov.ms_jwt_auth.model.User;
import org.kharitonov.ms_jwt_auth.model.dto.JwtAuthenticationResponse;
import org.kharitonov.ms_jwt_auth.model.dto.SignInRequest;
import org.kharitonov.ms_jwt_auth.model.dto.SignUpRequest;
import org.kharitonov.ms_jwt_auth.repository.UserRepository;
import org.kharitonov.ms_jwt_auth.security.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 * @author Kharitonov Pave
 * l on 03.03.2024.
 */
@Service
public class UserService {

    private final UserRepository repository;
    private final UserMapper mapper;
    private final JwtProvider jwtProvider;

    @Autowired
    public UserService(UserRepository repository, UserMapper mapper, JwtProvider jwtProvider) {
        this.repository = repository;
        this.mapper = mapper;
        this.jwtProvider = jwtProvider;
    }

    public JwtAuthenticationResponse signUp(SignUpRequest request) {
        User user = mapper.dtoToUser(request);
        if (repository.existsUserByEmail(user.getEmail())) {
            throw new RuntimeException("User email already exist");
        }
        if (repository.existsUserByUsername(user.getEmail())) {
            throw new RuntimeException("Username already taken");
        }
        repository.save(user);
        return jwtProvider.generateToken(user);
    }

    public JwtAuthenticationResponse signIn(SignInRequest request) {
        User user = mapper.dtoToUser(request);
        User dbUser = repository.findByUsername(user.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));
        if (dbUser.getPassword().equals(user.getPassword())) {
            return jwtProvider.generateToken(user);
        }
        return null;
    }
}
