package indie.typingstudy.common.config.oauth.provider;

public class KakaoUserInfo implements OAuth2UserInfo {
    @Override
    public String getProviderId() {
        return null;
    }

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getEmail() {
        return null;
    }

    @Override
    public String getProfileUrl() {
        return null;
    }
}
