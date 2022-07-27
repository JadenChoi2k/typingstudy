package com.typingstudy.infrastructure.user.favorite;

import com.typingstudy.domain.user.favorite.FavoriteGroup;
import com.typingstudy.domain.user.favorite.FavoriteItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class FavoriteRepositoryImpl implements FavoriteRepository {
    private final EntityManager em;

    @Override
    @Transactional(readOnly = true)
    public List<FavoriteGroup> findAllFavoriteGroups(Long userId, int page, int size) {
        return em.createQuery("select fg from FavoriteGroup fg " +
                        "where fg.user.id = :userId", FavoriteGroup.class)
                .setParameter("userId", userId)
                .setMaxResults(size)
                .setFirstResult(page * size)
                .getResultList();
    }


    @Override
    @Transactional(readOnly = true)
    public List<FavoriteItem> findAllFavoriteItems(Long groupId, int page, int size) {
        return em.createQuery("select fi from FavoriteItem fi " +
                        "where fi.group.id = :groupId", FavoriteItem.class)
                .setParameter("groupId", groupId)
                .setMaxResults(size)
                .setFirstResult(page * size)
                .getResultList();
    }

    @Override
    public FavoriteGroup findGroupById(Long groupId) {
        return em.find(FavoriteGroup.class, groupId);
    }

    @Override
    public List<FavoriteGroup> findAllGroups(Long userId) {
        return em.createQuery("select g from FavoriteGroup g" +
                " where g.user.id = :userId", FavoriteGroup.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    @Override
    public List<FavoriteItem> findAllItems(Long groupId) {
        return em.createQuery("select item from FavoriteItem item" +
                " where item.group.id = :groupId", FavoriteItem.class)
                .setParameter("groupId", groupId)
                .getResultList();
    }
}
