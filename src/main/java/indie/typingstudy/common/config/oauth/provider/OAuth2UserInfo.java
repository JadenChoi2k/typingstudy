package indie.typingstudy.common.config.oauth.provider;

import indie.typingstudy.domain.user.User;

public interface OAuth2UserInfo {
    String getProviderId();

    String getProvider();

    String getUsername();

    String getEmail();

    String getProfileUrl();

    User.SocialPlatform getPlatform();
}
