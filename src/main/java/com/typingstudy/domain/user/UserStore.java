package com.typingstudy.domain.user;

public interface UserStore {
    User store(User user);

    void remove(User user);
}
