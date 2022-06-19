package indie.typingstudy.domain.socialuser;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SocialPlatform {
    DOMAIN("도메인"), KAKAO("카카오톡"), GOOGLE("구글");

    private final String description;
}
