package com.typingstudy.domain.user.favorite;

import com.typingstudy.domain.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Table(name = "user_favorite_group")
@Entity
@Getter
@NoArgsConstructor
public class FavoriteGroup {
    @Id
    @GeneratedValue
    @Column(name = "user_favorite_group_id")
    private Long id;

    @Column(length = 50, nullable = false)
    @Setter
    private String groupName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public FavoriteGroup(String groupName, User user) {
        this.groupName = groupName;
        this.user = user;
    }

    public FavoriteItem createItem(String docToken) {
        return new FavoriteItem(this, docToken);
    }
}
