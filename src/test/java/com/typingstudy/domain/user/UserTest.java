package com.typingstudy.domain.user;

import com.typingstudy.domain.user.User;
import com.typingstudy.domain.user.favorite.FavoriteGroup;
import com.typingstudy.domain.user.favorite.FavoriteItem;
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

    @Test
    @DisplayName("이름 유효화")
    void processUsername() {
        // given
        String email = "test@example.com";
        String password = "password1234";
        String username = "testuser with space";
        String postUsername = "testuserwithspace";
        String profileUrl = "profile";
        // when
        User user = User.createDomainUser(email, password, username, profileUrl);
        // then
        assertThat(user.getEmail()).isEqualTo(email);
        assertThat(user.getPassword()).isEqualTo(password);
        assertThat(user.getUsername()).isEqualTo(postUsername);
        assertThat(user.getProfileUrl()).isEqualTo(profileUrl);
        assertThat(user.isSocial()).isFalse();
    }

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

    @Test
    @DisplayName("소셜 유저 확인")
    void isSocial() {
        // given
        User domainUser = new User(User.SocialPlatform.DOMAIN, "user@domain.com", "12345", "domain_user", null);
        User socialUser = new User(User.SocialPlatform.GOOGLE, "user@gmail.com", "12345", "social_user", null);
        // then
        assertThat(domainUser.isSocial()).isFalse();
        assertThat(socialUser.isSocial()).isTrue();
    }

    @Test
    @DisplayName("즐겨찾기 그룹 생성")
    void create_favorite_group() {
        // given
        User user = new User(User.SocialPlatform.DOMAIN, "user@domain.com", "12345", "domain_user", null);
        String groupName = "my favorite";
        // when
        FavoriteGroup favoriteGroup = user.createFavoriteGroup(groupName);
        // then
        assertThat(favoriteGroup.getUser()).isEqualTo(user);
        assertThat(favoriteGroup.getGroupName()).isEqualTo(groupName);
    }

    @Test
    @DisplayName("즐겨찾기 그룹 생성 후 아이템 생성")
    void create_favorite_group_and_item() {
        // given
        User user = new User(User.SocialPlatform.DOMAIN, "user@domain.com", "12345", "domain_user", null);
        String groupName = "my favorite";
        String docToken = "abcdefghijklmnopqrstuvwxyz";
        FavoriteGroup favoriteGroup = user.createFavoriteGroup(groupName);
        // when
        FavoriteItem item = favoriteGroup.createItem(docToken);
        // then
        assertThat(item.getGroup()).isEqualTo(favoriteGroup);
        assertThat(item.getDocToken()).isEqualTo(docToken);
    }
}
