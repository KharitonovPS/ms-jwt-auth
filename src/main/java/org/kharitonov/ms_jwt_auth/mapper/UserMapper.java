package org.kharitonov.ms_jwt_auth.mapper;

import org.kharitonov.ms_jwt_auth.model.User;
import org.kharitonov.ms_jwt_auth.model.UserRole;
import org.kharitonov.ms_jwt_auth.model.dto.SignUpRequest;
import org.kharitonov.ms_jwt_auth.security.BasePasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;

/**
 * @author Kharitonov Pavel on 03.03.2024.
 */
@Component
public class UserMapper {
    private final BasePasswordEncoder encoder;

    public UserMapper(BasePasswordEncoder encoder) {
        this.encoder = encoder;
    }

    public User dtoToUser(SignUpRequest request) {
        User user = new User();
        user.setUsername(request.username());
        user.setPassword(encoder.encode(request.password()));
        user.setEmail(request.email());
        user.setRoles(Collections.singleton(UserRole.ROLE_USER));
        return user;
    }
}
