package com.typingstudy.domain.user.favorite;

import com.typingstudy.domain.BaseEntity;
import com.typingstudy.domain.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name = "user_favorite_group")
@Entity
@Getter
@NoArgsConstructor
public class FavoriteGroup extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "user_favorite_group_id")
    private Long id;

    @Column(length = 50, nullable = false)
    @Setter
    private String groupName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    public FavoriteGroup(String groupName, User user) {
        this.groupName = groupName;
        this.user = user;
    }

    public FavoriteItem createItem(String docToken) {
        this.setEditedAt(LocalDateTime.now());
        return new FavoriteItem(this, docToken);
    }
}
