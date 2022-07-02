package indie.typingstudy.common.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import indie.typingstudy.common.config.auth.PrincipalDetails;
import indie.typingstudy.common.config.oauth.provider.OAuth2UserInfo;
import indie.typingstudy.infrastructure.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

@RequiredArgsConstructor
public class JwtUtils {
    private final UserRepository userRepository;

    public static String createDomainJwt(PrincipalDetails principalDetails) {
        return JWT.create()
                .withSubject(principalDetails.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperty.TIMEOUT))
                .withClaim("platform", "DOMAIN")
                .withClaim("id", principalDetails.getUser().getId())
                .withClaim("username", principalDetails.getUser().getUsername())
                .sign(Algorithm.HMAC512(JwtProperty.SECRET));
    }

    public static String createSocialJwt(PrincipalDetails principalDetails) {
        return JWT.create()
                .withSubject(principalDetails.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperty.TIMEOUT))
                .withClaim("platform", principalDetails.getUser().getPlatform().name())
                .withClaim("id", principalDetails.getUser().getId())
                .withClaim("username", principalDetails.getUser().getUsername())
                .sign(Algorithm.HMAC512(JwtProperty.SECRET));
    }

    public static String getEmailFromJwt(String jwt) {
        return JWT.require(Algorithm.HMAC512(JwtProperty.SECRET)).build()
                .verify(jwt).getSubject();
    }
}
