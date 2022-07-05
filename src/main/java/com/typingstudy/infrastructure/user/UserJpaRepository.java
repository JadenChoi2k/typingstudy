package com.typingstudy.infrastructure.user;

import com.typingstudy.domain.typingdoc.TypingDoc;
import com.typingstudy.domain.user.User;
import com.typingstudy.domain.user.favorite.FavoriteGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}
