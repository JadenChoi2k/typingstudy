package indie.typingstudy.interfaces.user;

import lombok.Data;

public class UserDto {

    @Data
    static class JoinRequest {
        private String email;
        private String password;
        private String username;
    }

}
