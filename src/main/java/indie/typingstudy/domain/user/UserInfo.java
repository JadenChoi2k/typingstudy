package indie.typingstudy.domain.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserInfo {
    private String id;
    private String name;
    private String profileUrl;

    public static UserInfo of(User user) {
        return UserInfo.builder()
                .id(user.getId().toString())
                .name(user.getUsername())
                .profileUrl(user.getProfileUrl())
                .build();
    }
}
