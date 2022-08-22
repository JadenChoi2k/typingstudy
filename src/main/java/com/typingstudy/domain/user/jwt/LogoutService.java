package com.typingstudy.domain.user.jwt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LogoutService {

    @Cacheable(cacheNames = "logoutStore", key = "#jwt")
    public boolean isLogout(final String jwt) {
        log.info("로그아웃 상태=false");
        return false;
    }

    @CachePut(cacheNames = "logoutStore", key = "#jwt")
    public boolean logout(final String jwt) {
        return true;
    }
}
