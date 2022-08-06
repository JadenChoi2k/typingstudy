package com.typingstudy.domain.user;

import com.typingstudy.domain.typingdoc.TypingDoc;
import com.typingstudy.domain.user.favorite.FavoriteGroup;
import com.typingstudy.domain.user.favorite.FavoriteItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface UserReader {
    User findById(Long userId);

    User findByEmail(String email);

    List<FavoriteGroup> findAllFavoriteGroups(Long userId, int page, int size);

    FavoriteGroup findFavoriteGroup(Long groupId);

    List<FavoriteItem> findAllFavoriteItems(Long groupId, int page, int size);

    boolean exists(String email);
}
