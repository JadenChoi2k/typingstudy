package indie.typingstudy.domain.user;

import indie.typingstudy.domain.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
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

    @Column(unique = true, length = 30)
    private String email;

    @Column(length = 30)
    private String password;

    @Column(length = 20, nullable = false)
    private String username;
    @Column(nullable = false)
    private String profileUrl;

    @Enumerated(EnumType.STRING)
    private SocialPlatform provider;

    private String providerId;

    private LocalDateTime lastLogin;

    public enum SocialPlatform {
        DOMAIN, GOOGLE, KAKAO
    }

    private User(SocialPlatform platform, String email, String password, String username, String profileUrl) {
        this.platform = platform;
        this.email = email;
        this.password = password;
        this.username = username;
        this.profileUrl = profileUrl;
    }

    public static User createSocialLoginUser(SocialPlatform platform, String email, String username, String profileUrl) {
        return new User(platform, email, null, username, profileUrl);
    }

    public static User createDomainUser(String email, String password, String username, String profileUrl) {
        if (email == null || password == null) throw new IllegalArgumentException("이메일 또는 비밀번호가 존재하지 않습니다.");
        return new User(SocialPlatform.DOMAIN, email, password, username, profileUrl);
    }

    public boolean isSocial() {
        return this.platform != SocialPlatform.DOMAIN;
    }

    public void onLogin() {
        this.lastLogin = LocalDateTime.now();
    }
}
