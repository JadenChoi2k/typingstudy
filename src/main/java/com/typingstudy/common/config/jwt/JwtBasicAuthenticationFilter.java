package com.typingstudy.common.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.typingstudy.common.config.auth.PrincipalDetails;
import com.typingstudy.interfaces.user.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class JwtBasicAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            ObjectMapper om = new ObjectMapper();
            var loginRequest = om.readValue(request.getInputStream(), UserDto.LoginRequest.class);
            request.setAttribute("JwtAuth", UUID.randomUUID().toString());
            log.info("[{}] 로그인 시도: {}", request.getAttribute("JwtAuth"), loginRequest.getEmail());
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());
            return authenticationManager.authenticate(authenticationToken);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        var principalDetails = (PrincipalDetails) authResult.getPrincipal();
        log.info("[{}] 로그인 완료: {}", request.getAttribute("JwtAuth"), principalDetails.getUsername());
        String jwt = JwtUtils.createDomainJwt(principalDetails);
        response.addHeader(JwtProperty.JWT_HEADER, JwtProperty.JWT_PREFIX + jwt);
        log.info("user({})에게 jwt 발급 완료", principalDetails.getUsername());
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        log.info("[{}] 로그인 실패", request.getAttribute("JwtAuth"));
        super.unsuccessfulAuthentication(request, response, failed);
    }
}
