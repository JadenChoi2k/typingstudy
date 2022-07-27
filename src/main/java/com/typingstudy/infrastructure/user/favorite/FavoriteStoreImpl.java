package com.typingstudy.infrastructure.user.favorite;

import com.typingstudy.domain.user.favorite.FavoriteGroup;
import com.typingstudy.domain.user.favorite.FavoriteItem;
import com.typingstudy.domain.user.favorite.FavoriteStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;

@Slf4j
@Component
@RequiredArgsConstructor
public class FavoriteStoreImpl implements FavoriteStore {
    private final EntityManager em;

    @Override
    public FavoriteGroup store(FavoriteGroup group) {
        em.persist(group);
        return group;
    }

    @Override
    public FavoriteItem store(FavoriteItem item) {
        em.persist(item);
        return item;
    }
}
