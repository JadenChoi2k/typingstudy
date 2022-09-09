package com.typingstudy.common.utils;

import com.typingstudy.common.config.auth.PrincipalDetails;
import com.typingstudy.common.config.jwt.JwtProperty;
import com.typingstudy.common.config.jwt.JwtUtils;
import com.typingstudy.domain.user.jwt.RefreshToken;
import com.typingstudy.domain.user.jwt.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class RefreshTokenGenerator {
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public String generateRefreshToken(PrincipalDetails principalDetails) {
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
}
