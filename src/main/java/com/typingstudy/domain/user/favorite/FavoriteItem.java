package com.typingstudy.domain.user.favorite;

import com.typingstudy.domain.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Table(name = "user_favorite_item")
@Entity
@Getter
@NoArgsConstructor
public class FavoriteItem extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "user_favorite_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private FavoriteGroup group;

    @Column(nullable = false)
    private String docToken;

    public FavoriteItem(FavoriteGroup group, String docToken) {
        this.group = group;
        this.docToken = docToken;
    }
}
