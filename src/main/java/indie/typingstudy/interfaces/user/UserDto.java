package indie.typingstudy.interfaces.user;

import lombok.Data;

public class UserDto {

    @Data
    public static class LoginRequest {
        private String email;
        private String password;
    }

    @Data
    public static class JoinRequest {
        private String email;
        private String password;
        private String username;
        private String profileUrl;
    }

    @Data
    public static class JoinSuccess {
        private Long id;
        private String name;
    }
}
