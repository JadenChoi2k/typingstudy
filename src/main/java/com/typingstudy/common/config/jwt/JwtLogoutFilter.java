package com.typingstudy.common.config.jwt;

import com.typingstudy.domain.user.jwt.LogoutService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RequiredArgsConstructor
public class JwtLogoutFilter implements LogoutHandler {
    private final LogoutService logoutService;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        log.info("logout from {}", request.getRemoteAddr());
        String accessToken = JwtUtils.extractAccessTokenFromRequest(request);
        String refreshToken = JwtUtils.extractRefreshTokenFromRequest(request);
        if (accessToken != null) {
            logoutService.logout(accessToken);
        }
        if (refreshToken != null) {
            logoutService.logout(refreshToken);
        }
    }
}
