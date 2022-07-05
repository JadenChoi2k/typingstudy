package com.typingstudy.infrastructure.user.favorite;

import com.typingstudy.domain.user.favorite.FavoriteGroup;
import com.typingstudy.domain.user.favorite.FavoriteItem;

import java.util.List;

public interface FavoriteRepository {
    List<FavoriteGroup> findAllFavoriteGroups(Long userId, int page, int size);

    List<FavoriteItem> findAllFavoriteItems(Long groupId, int page, int size);
}
