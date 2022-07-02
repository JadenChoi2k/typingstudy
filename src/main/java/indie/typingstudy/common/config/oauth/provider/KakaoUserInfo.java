package indie.typingstudy.common.config.oauth.provider;

import indie.typingstudy.domain.user.User;

import java.util.Map;

public class KakaoUserInfo implements OAuth2UserInfo {

    private final Map<String, Object> attributes;

    public KakaoUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getProviderId() {
        return null;
    }

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getUsername() {
        return "카카카오오유저";
    }

    @Override
    public String getEmail() {
        return null;
    }

    @Override
    public String getProfileUrl() {
        return null;
    }

    @Override
    public User.SocialPlatform getPlatform() {
        return User.SocialPlatform.KAKAO;
    }
}
