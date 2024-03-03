package org.kharitonov.ms_jwt_auth.service;

import org.kharitonov.ms_jwt_auth.mapper.UserMapper;
import org.kharitonov.ms_jwt_auth.model.User;
import org.kharitonov.ms_jwt_auth.model.dto.SignUpRequest;
import org.kharitonov.ms_jwt_auth.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * @author Kharitonov Pave
 * l on 03.03.2024.
 */
@Service
public class UserService {

    private final UserRepository repository;
    private final UserMapper mapper;

    public UserService(UserRepository repository, UserMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public ResponseEntity<HttpStatus> signUp(SignUpRequest request) {
        User user = mapper.dtoToUser(request);
        if (repository.existsUserByEmail(user.getEmail())) {
            throw new RuntimeException("User email already exist");
        }
        if (repository.existsUserByUsername(user.getEmail())) {
            throw new RuntimeException("Username already taken");
        }
        repository.save(user);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }
}
