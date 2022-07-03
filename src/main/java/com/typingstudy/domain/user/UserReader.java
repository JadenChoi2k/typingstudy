package com.typingstudy.domain.user;

public interface UserReader {
    User findById(Long userId);

    User findByEmail(String email);

    boolean exists(String email);
}
