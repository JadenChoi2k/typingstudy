package com.typingstudy.common.config.oauth;

import com.typingstudy.common.config.auth.PrincipalDetails;
import com.typingstudy.common.config.jwt.JwtProperty;
import com.typingstudy.common.config.jwt.JwtUtils;
import com.typingstudy.common.utils.RefreshTokenGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    private final RefreshTokenGenerator refreshTokenGenerator;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        log.info("CustomAuthenticationSuccessHandler");
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        log.info("request url : {}", request.getRequestURL());
        log.info("소셜 로그인 성공: {}", principal.getAttributes());
        // jwt 주입
        String jwt = JwtUtils.createSocialJwt(principal);
        log.info("jwt 주입: {}", jwt);
        String fullJwt = JwtProperty.JWT_PREFIX + jwt;
        response.addHeader(JwtProperty.JWT_HEADER, fullJwt);
        sendCookie(response, new Cookie(
                JwtProperty.JWT_HEADER,
                URLEncoder.encode(fullJwt, StandardCharsets.UTF_8)));
        response.sendRedirect("http://localhost:8088/oauth2/success?access_token=" + fullJwt +
                "&refresh_token=" + refreshTokenGenerator.generateRefreshToken(principal));
        super.onAuthenticationSuccess(request, response, authentication);
    }

    private void sendCookie(HttpServletResponse response, Cookie cookie) {
        cookie.setMaxAge(JwtProperty.TIMEOUT / 1000);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
