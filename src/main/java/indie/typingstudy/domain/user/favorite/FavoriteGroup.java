package indie.typingstudy.domain.user.favorite;

import indie.typingstudy.domain.user.User;
import lombok.Getter;

import javax.persistence.*;

@Table(name = "user_favorite_group")
@Entity
@Getter
public class FavoriteGroup {
    @Id
    @GeneratedValue
    @Column(name = "user_favorite_group_id")
    private Long id;

    @Column(length = 50, nullable = false)
    private String groupName;

    @ManyToOne
    @Column(name = "user_id")
    private User user;
}
