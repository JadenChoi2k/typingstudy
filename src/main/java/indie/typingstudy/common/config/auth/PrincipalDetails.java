package indie.typingstudy.common.config.auth;

import indie.typingstudy.domain.user.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Getter
public class PrincipalDetails implements UserDetails, OAuth2User {

    private final User user;
    private final Map<String, Object> attributes;

    public PrincipalDetails(User user) {
        this(user, null);
    }

    public PrincipalDetails(User user, Map<String, Object> attributes) {
        this.user = user;
        this.attributes = attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(() -> "ROLE_USER");
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    // email 로그인
    @Override
    public String getUsername() {
        return user.getEmail();
    }

    // TODO: 구현하기
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    // OAuth2User

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return user.getId().toString();
    }
}
