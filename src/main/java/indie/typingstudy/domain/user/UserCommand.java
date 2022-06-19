package indie.typingstudy.domain.user;

import indie.typingstudy.domain.socialuser.SocialPlatform;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class UserCommand {

    @Getter
    @Setter
    @Builder
    static class DomainUserRegisterRequest {
        private String email;
        private String password;
        private String username;
        private String profileUrl;
    }

    @Getter
    @Setter
    @Builder
    static class SocialUserRegisterRequest {
        private SocialPlatform platform;
        private String email;
        private String username;
        private String profileUrl;
    }
}
