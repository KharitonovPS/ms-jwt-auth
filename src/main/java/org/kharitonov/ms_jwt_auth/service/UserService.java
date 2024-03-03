package org.kharitonov.ms_jwt_auth.service;

import org.kharitonov.ms_jwt_auth.mapper.UserMapper;
import org.kharitonov.ms_jwt_auth.model.User;
import org.kharitonov.ms_jwt_auth.model.dto.JwtAuthenticationResponse;
import org.kharitonov.ms_jwt_auth.model.dto.SignUpRequest;
import org.kharitonov.ms_jwt_auth.repository.UserRepository;
import org.kharitonov.ms_jwt_auth.security.JwtProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Kharitonov Pave
 * l on 03.03.2024.
 */
@Service
public class UserService {

    private final UserRepository repository;
    private final UserMapper mapper;
    private final JwtProviderService jwtProviderService;

    @Autowired
    public UserService(UserRepository repository, UserMapper mapper, JwtProviderService jwtProviderService) {
        this.repository = repository;
        this.mapper = mapper;
        this.jwtProviderService = jwtProviderService;
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
        return jwtProviderService.generateToken(user);
    }
}
