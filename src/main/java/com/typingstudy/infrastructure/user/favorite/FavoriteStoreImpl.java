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

    @Override
    public void remove(FavoriteGroup group) {
        em.remove(group);
    }

    @Override
    public void removeFavoriteItem(Long userId, Long itemId) {
        int removeCount = em.createQuery("delete from FavoriteItem item" +
                        " where item.id = (" +
                        "   select item2.id from FavoriteItem item2" +
                        "   join FavoriteGroup group2" +
                        "   where group2.user.id = :userId and item2.id = :itemId" +
                        ")")
                .setParameter("userId", userId)
                .setParameter("itemId", itemId)
                .executeUpdate();
        if (removeCount == 0) throw new IllegalStateException("즐겨찾기 아이템 삭제 실패");
    }
}
