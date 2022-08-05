package com.typingstudy.interfaces;

import com.typingstudy.common.config.auth.PrincipalDetails;
import com.typingstudy.common.exception.InvalidAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {
    public static Long getUserId() {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        if (authentication == null) {
            throw new InvalidAccessException("unauthorized");
        }
        var principal =
                (PrincipalDetails) authentication.getPrincipal();
        if (principal == null) {
            throw new InvalidAccessException("unauthorized");
        }
        return principal.getUser().getId();
    }
}
