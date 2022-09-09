package com.typingstudy.domain.user;

import com.typingstudy.domain.BaseTimeEntity;
import com.typingstudy.domain.user.favorite.FavoriteGroup;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SocialPlatform platform;

    @Column(unique = true, length = 50)
    private String email;

    @Column
    private String password;

    @Column(length = 20, nullable = false)
    private String username;

    private String profileUrl;

    // if domain user, be null.
    @Column(nullable = true)
    private String providerId;

    private LocalDateTime lastLogin;

    public enum SocialPlatform {
        DOMAIN, GOOGLE, KAKAO
    }

    public User(SocialPlatform platform, String email, String password, String username, String profileUrl) {
        new User(platform, email, password, username, profileUrl, null);
    }

    public User(SocialPlatform platform, String email, String password, String username, String profileUrl, String providerId) {
        this.platform = platform;
        this.email = email;
        this.password = password;
        this.username = username;
        this.profileUrl = profileUrl;
        this.providerId = providerId;
    }

    public static User createSocialLoginUser(SocialPlatform platform, String email, String username, String profileUrl) {
        return new User(platform, email, null, username, profileUrl);
    }

    public static User createDomainUser(String email, String password, String username, String profileUrl) {
        if (email == null || password == null) throw new IllegalArgumentException("이메일 또는 비밀번호가 존재하지 않습니다.");
        return new User(SocialPlatform.DOMAIN, email, password, username, profileUrl);
    }

    private static String processUsername(String username) {
        return username.replaceAll("[^가-힣A-Za-z\\d]+", "");
    }

    public boolean isSocial() {
        return this.platform != SocialPlatform.DOMAIN;
    }

    public void onLogin() {
        this.lastLogin = LocalDateTime.now();
    }

    public void updateAttributes(String username, String profileUrl) {
        this.username = username;
        this.profileUrl = profileUrl;
    }

    public FavoriteGroup createFavoriteGroup(String groupName) {
        return new FavoriteGroup(groupName, this);
    }
}
