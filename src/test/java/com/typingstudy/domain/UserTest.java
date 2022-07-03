package com.typingstudy.domain;

import com.typingstudy.domain.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class UserTest {

    @Test
    @DisplayName("도메인 유저 생성")
    void createDomain() {
        // given
        String email = "test@example.com";
        String password = "password1234";
        String username = "testuser";
        String profileUrl = "profile";
        // when
        User user = User.createDomainUser(email, password, username, profileUrl);
        // then
        assertThat(user.getEmail()).isEqualTo(email);
        assertThat(user.getPassword()).isEqualTo(password);
        assertThat(user.getUsername()).isEqualTo(username);
        assertThat(user.getProfileUrl()).isEqualTo(profileUrl);
        assertThat(user.isSocial()).isFalse();
    }

//    @Test
//    @DisplayName("소셜 유저 생성")
//    void createSocial() {
//        // given
//        SocialPlatform platform = SocialPlatform.KAKAO;
//        String email = "test@kakao.com";
//        String username = "testuser";
//        String profileUrl = "kakaoprofile";
//        // when
//        User user = User.createSocialLoginUser(platform, email, username, profileUrl);
//        // then
//        assertThat(user.getPlatform()).isEqualTo(platform);
//        assertThat(user.getEmail()).isEqualTo(email);
//        assertThat(user.getPassword()).isNull();
//        assertThat(user.getUsername()).isEqualTo(username);
//        assertThat(user.getProfileUrl()).isEqualTo(profileUrl);
//        assertThat(user.isSocial()).isTrue();
//    }

    @Test
    @DisplayName("로그인 시 로그인 날짜 갱신")
    void onLogin() {
        // given
        User user = User.createDomainUser("test@example.com",
                "password1234", "testuser", "profile");
        // when
        user.onLogin();
        // then
        assertThat(user.getLastLogin())
                .isBetween(LocalDateTime.now().minusMinutes(1), LocalDateTime.now());
    }
}
