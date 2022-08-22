package com.typingstudy.common.config;

import com.typingstudy.common.config.jwt.JwtBasicAuthenticationFilter;
import com.typingstudy.common.config.jwt.JwtAuthorizationFilter;
import com.typingstudy.common.config.jwt.JwtLogoutFilter;
import com.typingstudy.common.config.jwt.JwtLogoutSuccessHandler;
import com.typingstudy.domain.user.jwt.LogoutService;
import com.typingstudy.domain.user.jwt.RefreshTokenRepository;
import com.typingstudy.common.config.oauth.CustomAuthenticationSuccessHandler;
import com.typingstudy.common.config.oauth.PrincipalOauth2UserService;
import com.typingstudy.infrastructure.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity // 필터 체인 관리 시작 어노테이션
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    // PrincipalOauth2UserService가 di될 예정
    private final PrincipalOauth2UserService oAuth2UserService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final LogoutService logoutService;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .logout()
                    .addLogoutHandler(new JwtLogoutFilter(logoutService))
                    .logoutSuccessHandler(new JwtLogoutSuccessHandler())
                    .and()
                .addFilter(new JwtBasicAuthenticationFilter(authenticationManager(), refreshTokenRepository))
                .addFilter(new JwtAuthorizationFilter(authenticationManager(), userRepository, refreshTokenRepository, logoutService))
                .authorizeRequests()
                    .antMatchers(
                            "/api/v1/user/join",
                            "/api/v1/user/info/**",
                            "/api/v1/user/email/verify").permitAll()
                    .antMatchers("/api/v1/user/**", "/api/v1/docs/**").authenticated()
                    .anyRequest().permitAll()
                    .and()
                .oauth2Login()
                .loginPage("/unauthorized")
                .successHandler(new CustomAuthenticationSuccessHandler())
                .userInfoEndpoint()
                .userService(oAuth2UserService);
        http.httpBasic().disable();
    }
}
