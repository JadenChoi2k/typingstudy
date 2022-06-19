package indie.typingstudy.domain.user;

import indie.typingstudy.domain.socialuser.SocialPlatform;
import lombok.*;

public class UserCommand {
    @Data
    @Builder
    public static class LoginRequest {
        private String email;
        private String password;
    }

    @Data
    @Builder
    public static class DomainUserRegisterRequest {
        private String email;
        private String password;
        private String username;
        private String profileUrl;
    }

    @Data
    @Builder
    public static class SocialUserRegisterRequest {
        private SocialPlatform platform;
        private String email;
        private String username;
        private String profileUrl;
    }
}
