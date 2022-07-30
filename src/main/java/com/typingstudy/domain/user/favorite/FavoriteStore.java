package com.typingstudy.domain.user.favorite;

public interface FavoriteStore {
    FavoriteGroup store(FavoriteGroup group);

    FavoriteItem store(FavoriteItem item);

    void remove(FavoriteGroup group);

    void removeFavoriteItem(Long userId, Long itemId);
}
