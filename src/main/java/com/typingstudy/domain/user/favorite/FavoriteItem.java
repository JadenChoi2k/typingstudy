package com.typingstudy.domain.user.favorite;

import com.typingstudy.domain.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table(name = "user_favorite")
@Entity
@Getter
@NoArgsConstructor
public class FavoriteItem extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "user_favorite_item_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private FavoriteGroup group;

    @Column(nullable = false)
    private Long docId;
}
