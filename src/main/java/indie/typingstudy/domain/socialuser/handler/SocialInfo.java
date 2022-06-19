package indie.typingstudy.domain.socialuser.handler;

import indie.typingstudy.domain.socialuser.SocialPlatform;
import lombok.Data;

@Data
public class SocialInfo {
    private String username;
    private String profileUrl;
    private String email;
    private SocialPlatform platform;
}
