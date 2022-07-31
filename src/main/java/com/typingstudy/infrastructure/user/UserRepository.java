package com.typingstudy.infrastructure.user;

import com.typingstudy.domain.typingdoc.TypingDoc;
import com.typingstudy.domain.user.User;
import com.typingstudy.domain.user.favorite.FavoriteGroup;
import com.typingstudy.domain.user.favorite.FavoriteItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Optional<User> findById(Long userId);

    Optional<User> findByEmail(String email);

    User save(User user);

    void remove(User user);

    boolean exists(String email);
}
