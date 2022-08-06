package com.typingstudy.infrastructure.user;

import com.typingstudy.common.exception.EntityNotFoundException;
import com.typingstudy.domain.typingdoc.TypingDoc;
import com.typingstudy.domain.user.User;
import com.typingstudy.domain.user.UserReader;
import com.typingstudy.domain.user.favorite.FavoriteGroup;
import com.typingstudy.domain.user.favorite.FavoriteItem;
import com.typingstudy.infrastructure.user.favorite.FavoriteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserReaderImpl implements UserReader {
    private final UserRepository userRepository;
    private final FavoriteRepository favoriteRepository;

    @Override
    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("user not found. userId: " + userId));
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("user not found. email: " + email));
    }

    @Override
    public List<FavoriteGroup> findAllFavoriteGroups(Long userId, int page, int size) {
        return favoriteRepository.findAllFavoriteGroups(userId, page, size);
    }

    @Override
    public FavoriteGroup findFavoriteGroup(Long groupId) {
        return favoriteRepository.findGroupById(groupId);
    }

    @Override
    public List<FavoriteItem> findAllFavoriteItems(Long groupId, int page, int size) {
        return favoriteRepository.findAllFavoriteItems(groupId, page, size);
    }

    @Override
    public boolean exists(String email) {
        return userRepository.exists(email);
    }
}
