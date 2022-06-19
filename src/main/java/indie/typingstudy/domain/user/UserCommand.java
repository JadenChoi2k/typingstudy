package indie.typingstudy.domain.user;

import indie.typingstudy.domain.socialuser.SocialPlatform;
import lombok.*;

public class UserCommand {

    @Data
    @Builder
    static class DomainUserRegisterRequest {
        private String email;
        private String password;
        private String username;
        private String profileUrl;
    }

    @Data
    @Builder
    static class SocialUserRegisterRequest {
        private SocialPlatform platform;
        private String email;
        private String username;
        private String profileUrl;
    }
}
