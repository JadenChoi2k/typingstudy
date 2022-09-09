package com.typingstudy.common.utils;

import com.typingstudy.domain.user.User;

public class OAuth2ResignHelper {
    public void resignOAuth2User(User user) {
        if (!user.isSocial()) {
            return;
        }
        
    }
}
