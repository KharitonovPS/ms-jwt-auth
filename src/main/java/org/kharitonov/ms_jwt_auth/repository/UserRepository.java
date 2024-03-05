package org.kharitonov.ms_jwt_auth.repository;

import org.kharitonov.ms_jwt_auth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsUserByEmail(String email);

    boolean existsUserByUsername(String username);

    Optional<User> findByUsername(String userName);

    List<User> findAll();
}
