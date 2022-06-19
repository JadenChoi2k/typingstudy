package indie.typingstudy.domain.socialuser;

import indie.typingstudy.domain.BaseTimeEntity;
import indie.typingstudy.domain.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SocialUser extends BaseTimeEntity {
    @EmbeddedId
    private SocialId socialId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public SocialUser(SocialId socialId, User user) {
        this.socialId = socialId;
        this.user = user;
    }
}
