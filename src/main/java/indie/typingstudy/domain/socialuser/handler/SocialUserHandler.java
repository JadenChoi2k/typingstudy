package indie.typingstudy.domain.socialuser.handler;

import indie.typingstudy.domain.socialuser.SocialPlatform;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface SocialUserHandler {
    SocialInfo handle(HttpServletRequest request, HttpServletResponse response);

    boolean support(SocialPlatform platform);
}
