package com.typingstudy.common.config.jwt;

import com.typingstudy.common.config.auth.PrincipalDetails;
import com.typingstudy.common.config.jwt.refresh.RefreshToken;
import com.typingstudy.common.config.jwt.refresh.RefreshTokenRepository;
import com.typingstudy.domain.user.User;
import com.typingstudy.infrastructure.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Optional;

@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository, RefreshTokenRepository refreshTokenRepository) {
        super(authenticationManager);
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String jwt = request.getHeader(JwtProperty.JWT_HEADER);
        log.info("request uri : {}", request.getRequestURI());
        log.info("request url : {}", request.getRequestURL());
        log.info("request code : {}", request.getParameter("code"));
        String cookieJwt = getJwtFromCookies(request);
        jwt = cookieJwt != null ? URLDecoder.decode(cookieJwt, StandardCharsets.UTF_8) : jwt;
        log.info("jwt authorization = {}", jwt);
        if (jwt == null || !jwt.startsWith(JwtProperty.JWT_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }
        jwt = jwt.replace(JwtProperty.JWT_PREFIX, "");
        String email = JwtUtils.getEmailFromJwt(jwt);
        if (email != null) {
            Optional<User> userEntity = userRepository.findByEmail(email);
            if (userEntity.isPresent()) {
                PrincipalDetails principalDetails = new PrincipalDetails(userEntity.get());
                Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());
                log.info("jwt 검증 완료. security session에 주입");
                executeRefreshCheck(jwt, principalDetails, response);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        chain.doFilter(request, response);
    }

    private String getJwtFromCookies(HttpServletRequest request) {
        if (request.getCookies() == null) return null;
//        Arrays.stream(request.getCookies())
//                .forEach(c -> log.info("cookie {}:{}", c.getName(), c.getValue()));
        return Arrays.stream(request.getCookies())
                .filter(c -> c.getName().equals(JwtProperty.JWT_HEADER))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);
    }

    @Transactional
    protected void executeRefreshCheck(String jwt, PrincipalDetails principalDetails, HttpServletResponse response) {
        Long userId = principalDetails.getUser().getId();
        Optional<RefreshToken> entity = refreshTokenRepository.findById(userId);
        // inject refresh token to header
        entity.ifPresent((ref) -> {
            if (!ref.getToken().replace(JwtProperty.JWT_PREFIX, "").equals(jwt)) {
                log.info("refresh 토큰 발급 x, jwt={}, processedJwt={}", jwt, ref.getToken().replace(JwtProperty.JWT_PREFIX, ""));
                return;
            }
            String accessToken = JwtUtils.createDomainJwt(principalDetails);
            String refreshToken = JwtUtils.createDomainJwt(principalDetails, 60000 * 60);
            ref.setToken(refreshToken);
            refreshTokenRepository.save(ref);
            // inject
            response.addHeader(JwtProperty.JWT_HEADER, accessToken);
            response.addHeader(JwtProperty.REFRESH_HEADER, refreshToken);
            log.info("jwt refresh 토큰 요청, 생성 완료.");
        });
    }
}
