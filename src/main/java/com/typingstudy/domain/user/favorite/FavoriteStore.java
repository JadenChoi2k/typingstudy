package com.typingstudy.domain.user.favorite;

public interface FavoriteStore {
    FavoriteGroup store(FavoriteGroup group);

    FavoriteItem store(FavoriteItem item);

    void remove(FavoriteGroup group);

    void removeAllItemsByDocToken(String docToken);

    void removeFavoriteItem(Long userId, Long itemId);

    void removeFavoriteItemByDocToken(Long userId, Long groupId, String docToken);
}
