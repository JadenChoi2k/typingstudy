package indie.typingstudy.domain.socialuser;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SocialId implements Serializable {
    @Column(name = "social_user_id")
    private String id;

    @Enumerated(EnumType.STRING)
    private SocialPlatform platform;

    public SocialId(String id, SocialPlatform platform) {
        this.id = id;
        this.platform = platform;
    }
}
