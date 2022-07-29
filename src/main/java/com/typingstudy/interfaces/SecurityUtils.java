package com.typingstudy.interfaces;

import com.typingstudy.common.config.auth.PrincipalDetails;
import com.typingstudy.common.exception.InvalidAccessException;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {
    public static Long getUserId() {
        var principal =
                (PrincipalDetails) SecurityContextHolder.getContext()
                        .getAuthentication().getPrincipal();
        if (principal == null) {
            throw new InvalidAccessException("unauthorized");
        }
        return principal.getUser().getId();
    }
}
