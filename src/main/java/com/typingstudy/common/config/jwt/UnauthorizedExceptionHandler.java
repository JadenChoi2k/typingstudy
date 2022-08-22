package com.typingstudy.common.config.jwt;

import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

public class UnauthorizedExceptionHandler extends LoginUrlAuthenticationEntryPoint {

    public UnauthorizedExceptionHandler() {
        super("");
    }

    public UnauthorizedExceptionHandler(String loginFormUrl) {
        super(loginFormUrl);
    }
}
