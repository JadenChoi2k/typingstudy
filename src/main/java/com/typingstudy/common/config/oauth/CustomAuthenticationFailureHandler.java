package com.typingstudy.common.config.oauth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
//        response.sendError(HttpServletResponse.SC_BAD_REQUEST, exception.getMessage());
        log.info("OAuth2 failure - msg: {}", exception.getMessage());

        response.sendRedirect("/oauth2/error?msg=" + URLEncoder.encode(exception.getMessage(), StandardCharsets.UTF_8));
    }
}
