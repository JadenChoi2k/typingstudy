package com.typingstudy.infrastructure.user;

import com.typingstudy.common.exception.AlreadyExistException;
import com.typingstudy.domain.user.User;
import com.typingstudy.domain.user.favorite.FavoriteGroup;
import com.typingstudy.domain.user.favorite.FavoriteItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final UserJpaRepository userJpaRepository;

    @Override
    public Optional<User> findById(Long userId) {
        return userJpaRepository.findById(userId);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userJpaRepository.findByEmail(email);
    }

    @Override
    public User save(User user) {
        if (userJpaRepository.existsByEmail(user.getEmail()))
            throw new AlreadyExistException("이미 존재하는 유저입니다.");
        return userJpaRepository.save(user);
    }

    @Override
    public void remove(User user) {
        userJpaRepository.delete(user);
    }

    @Override
    public boolean exists(String email) {
        return userJpaRepository.existsByEmail(email);
    }
}
