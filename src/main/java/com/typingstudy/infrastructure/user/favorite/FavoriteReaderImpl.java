package com.typingstudy.infrastructure.user.favorite;

import com.typingstudy.domain.user.favorite.FavoriteGroup;
import com.typingstudy.domain.user.favorite.FavoriteItem;
import com.typingstudy.domain.user.favorite.FavoriteReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class FavoriteReaderImpl implements FavoriteReader {
    private final FavoriteRepository favoriteRepository;

    @Override
    public FavoriteGroup findGroupById(Long groupId) {
        return null;
    }

    @Override
    public List<FavoriteGroup> findAllGroups(Long userId) {
        return null;
    }

    @Override
    public List<FavoriteItem> findAllItems(Long groupId) {
        return null;
    }
}
