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
        log.info("즐겨찾기 그룹 제거. group={}", group.getId());
        em.remove(group);
    }

    // 문서 삭제할 때 외에는 호출하지 않도록 주의
    @Override
    public void removeAllItemsByDocToken(String docToken) {
        log.info("토큰에 해당하는 즐겨찾기 아이템 제거 docToken={}", docToken);
        em.createQuery("delete from FavoriteItem item where item.docToken = :docToken")
                .setParameter("docToken", docToken)
                .executeUpdate();
    }

    @Override
    public void removeFavoriteItem(Long userId, Long itemId) {
        //group2.user.id = :userId and item2.id = :itemId
        int removeCount = em.createQuery("delete from FavoriteItem item" +
                        " where item.id = (" +
                        "   select item2.id from FavoriteItem item2" +
                        "   join FavoriteGroup group2 on group2.id = item2.group.id" +
                        "   where group2.user.id = :userId and item2.id = :itemId" +
                        ")")
                .setParameter("userId", userId)
                .setParameter("itemId", itemId)
                .executeUpdate();
        if (removeCount == 0) throw new IllegalStateException("즐겨찾기 아이템 삭제 실패");
    }

    @Override
    public void removeFavoriteItemByDocToken(Long userId, Long groupId, String docToken) {
        em.createQuery("select true from FavoriteGroup group" +
                        " where group.id = :groupId and group.user.id =:userId", Boolean.class)
                .setParameter("userId", userId)
                .setParameter("groupId", groupId)
                .getSingleResult();
        int removeCount = em.createQuery("delete from FavoriteItem item" +
                        " where item.group.id = :groupId and item.docToken = :docToken")
                .setParameter("groupId", groupId)
                .setParameter("docToken", docToken)
                .executeUpdate();
        if (removeCount == 0) throw new IllegalStateException("즐겨찾기 아이템 삭제 실패");
    }
}
