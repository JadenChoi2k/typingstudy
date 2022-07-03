package com.typingstudy.common.config.oauth.provider;

import com.typingstudy.domain.user.User;

import java.util.Map;

public class KakaoUserInfo implements OAuth2UserInfo {

    private final Map<String, Object> attributes;
    private final Map<String, Object> properties;
    private final Map<String, Object> kakaoAccount;

    public KakaoUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
        this.properties = (Map<String, Object>) attributes.get("properties");
        this.kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        System.out.println("attributes = " + attributes);
        System.out.println("properties = " + properties);
        System.out.println("kakaoAccount = " + kakaoAccount);
    }

    @Override
    public String getProviderId() {
        return (String) attributes.get("id");
    }

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getUsername() {
        return (String) properties.get("nickname");
    }

    @Override
    public String getEmail() {
        return (String) kakaoAccount.get("email");
    }

    @Override
    public String getProfileUrl() {
        return (String) properties.get("profile_image");
    }

    @Override
    public User.SocialPlatform getPlatform() {
        return User.SocialPlatform.KAKAO;
    }
}
