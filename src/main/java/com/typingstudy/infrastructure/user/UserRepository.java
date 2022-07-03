package com.typingstudy.infrastructure.user;

import com.typingstudy.domain.user.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findById(Long userId);

    Optional<User> findByEmail(String email);

    User save(User user);

    boolean exists(String email);


}
