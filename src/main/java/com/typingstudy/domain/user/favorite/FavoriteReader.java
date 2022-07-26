package com.typingstudy.domain.user.favorite;

import java.util.List;

public interface FavoriteReader {
    FavoriteGroup findGroupById(Long groupId);

    List<FavoriteGroup> findAllGroups(Long userId);

    List<FavoriteItem> findAllItems(Long groupId);
}
