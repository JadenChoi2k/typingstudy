package com.typingstudy.infrastructure.user;

import com.typingstudy.domain.user.User;
import com.typingstudy.domain.user.UserStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserStoreImpl implements UserStore {
    private final UserRepository userRepository;

    @Override
    public User store(User user) {
        return userRepository.save(user);
    }

    @Override
    public void remove(User user) {
        userRepository.remove(user);
    }
}
