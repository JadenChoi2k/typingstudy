package indie.typingstudy.common.config.oauth.provider;

import indie.typingstudy.domain.user.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@Getter
@RequiredArgsConstructor
public class GoogleUserInfo implements OAuth2UserInfo {

    private final Map<String, Object> attributes;

    @Override
    public String getProviderId() {
        return (String) attributes.get("sub");
    }

    @Override
    public String getProvider() {
        return "google";
    }

    @Override
    public String getUsername() {
        return (String) attributes.get("name");
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getProfileUrl() {
        return (String) attributes.get("picture");
    }

    @Override
    public User.SocialPlatform getPlatform() {
        return User.SocialPlatform.GOOGLE;
    }
}
