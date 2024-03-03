package org.kharitonov.ms_jwt_auth.service;

import lombok.RequiredArgsConstructor;
import org.kharitonov.ms_jwt_auth.exceptions.UsernameNotFoundException;
import org.kharitonov.ms_jwt_auth.model.User;
import org.kharitonov.ms_jwt_auth.repository.UserRepository;
import org.springframework.stereotype.Service;

/**
 * @author Kharitonov Pavel on 03.03.2024.
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    public void createUser(User user) {
        if (repository.existsUserByEmail(user.getEmail())) {
            throw new RuntimeException("User email already exist");
        }
        if (repository.existsUserByUsername(user.getEmail())) {
            throw new RuntimeException("Username already taken");
        }
        repository.save(user);
    }

    public User findByUsername(String username) {
        return repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException
                        ("Пользователь не найден"));
    }
}
