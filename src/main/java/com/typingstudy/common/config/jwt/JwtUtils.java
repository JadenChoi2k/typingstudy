package com.typingstudy.common.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.typingstudy.common.config.auth.PrincipalDetails;
import com.typingstudy.common.exception.InvalidTokenException;
import com.typingstudy.infrastructure.user.UserRepository;
import lombok.RequiredArgsConstructor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
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

    public static String createDomainJwt(PrincipalDetails principalDetails, int plusTimeout) {
        return JWT.create()
                .withSubject(principalDetails.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperty.TIMEOUT + plusTimeout))
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

    public static String createSocialJwt(PrincipalDetails principalDetails, int plusTimeout) {
        return JWT.create()
                .withSubject(principalDetails.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperty.TIMEOUT + plusTimeout))
                .withClaim("platform", principalDetails.getUser().getPlatform().name())
                .withClaim("id", principalDetails.getUser().getId())
                .withClaim("username", principalDetails.getUser().getUsername())
                .sign(Algorithm.HMAC512(JwtProperty.SECRET));
    }

    public static String extractAccessTokenFromRequest(HttpServletRequest request) {
        String jwt = request.getHeader(JwtProperty.JWT_HEADER);
        String cookieJwt = getAccessTokenFromCookies(request);
        jwt = cookieJwt != null ? URLDecoder.decode(cookieJwt, StandardCharsets.UTF_8) : jwt;
        return jwt;
    }

    public static String extractRefreshTokenFromRequest(HttpServletRequest request) {
        String jwt = request.getHeader(JwtProperty.REFRESH_HEADER);
        String cookieJwt = getRefreshTokenFromCookies(request);
        jwt = cookieJwt != null ? URLDecoder.decode(cookieJwt, StandardCharsets.UTF_8) : jwt;
        return jwt;
    }

    public static String getEmailFromJwt(String jwt) {
        if (jwt == null) {
            return null;
        }
        try {
            return JWT.require(Algorithm.HMAC512(JwtProperty.SECRET)).build()
                    .verify(jwt).getSubject();
        } catch (SignatureVerificationException e) {
            throw new InvalidTokenException();
        }
    }

    private static String getAccessTokenFromCookies(HttpServletRequest request) {
        if (request.getCookies() == null) return null;
        return Arrays.stream(request.getCookies())
                .filter(c -> c.getName().equals(JwtProperty.JWT_HEADER))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);
    }

    private static String getRefreshTokenFromCookies(HttpServletRequest request) {
        if (request.getCookies() == null) return null;
        return Arrays.stream(request.getCookies())
                .filter(c -> c.getName().equals(JwtProperty.REFRESH_HEADER))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);
    }
}
