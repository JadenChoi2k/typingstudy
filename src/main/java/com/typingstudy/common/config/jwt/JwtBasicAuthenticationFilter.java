package com.typingstudy.common.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.typingstudy.common.config.auth.PrincipalDetails;
import com.typingstudy.common.config.jwt.refresh.RefreshToken;
import com.typingstudy.common.config.jwt.refresh.RefreshTokenRepository;
import com.typingstudy.interfaces.user.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class JwtBasicAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenRepository refreshTokenRepository;

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
        Map<String, String> body = new HashMap<>();
        String accessToken = JwtProperty.JWT_PREFIX + JwtUtils.createDomainJwt(principalDetails);
        String refreshToken = generateRefreshToken(principalDetails);
        body.put("accessToken", accessToken);
        body.put("refreshToken", refreshToken);
        // send json
        response.addHeader("Content-Type", "application/json");
        response.getWriter().print(new ObjectMapper().writeValueAsString(body));
//        response.addHeader(JwtProperty.JWT_HEADER, accessToken);
        log.info("user({})에게 jwt 발급 완료", principalDetails.getUsername());
    }

    // returns refresh token
    @Transactional
    protected String generateRefreshToken(PrincipalDetails principalDetails) {
        Long userId = principalDetails.getUser().getId();
        Optional<RefreshToken> refreshTokenEntity = refreshTokenRepository.findById(userId);
        String refreshToken = JwtProperty.JWT_PREFIX + JwtUtils.createDomainJwt(principalDetails, 60000 * 60);
        if (refreshTokenEntity.isEmpty()) {
            log.info("refresh token 신규 생성, userId={}", userId);
            var entity = new RefreshToken(userId, refreshToken);
            refreshTokenRepository.save(entity);
        } else {
            refreshTokenEntity.get().setToken(refreshToken);
            refreshTokenRepository.save(refreshTokenEntity.get());
        }
        return refreshToken;
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        log.info("[{}] 로그인 실패", request.getAttribute("JwtAuth"));
        super.unsuccessfulAuthentication(request, response, failed);
    }
}
