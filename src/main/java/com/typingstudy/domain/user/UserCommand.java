package com.typingstudy.domain.user;

import lombok.Builder;
import lombok.Data;

public class UserCommand {
    @Data
    @Builder
    public static class LoginRequest {
        private String email;
        private String password;
    }

    @Data
    @Builder
    public static class DomainUserRegisterRequest {
        private String email;
        private String password;
        private String username;
        private String profileUrl;
    }

    @Data
    @Builder
    public static class SocialUserRegisterRequest {
        private User.SocialPlatform platform;
        private String email;
        private String username;
        private String profileUrl;
    }

    @Data
    @Builder
    public static class CreateFavoriteGroupRequest {
        private String groupName;
        private Long userId;
    }

    @Data
    @Builder
    public static class EditFavoriteGroupRequest {
        private Long groupId;
        private Long userId;
        private String groupName;
    }

    // userId는 security에서 주입
    @Data
    @Builder
    public static class AddFavoriteItemRequest {
        private Long userId;
        private Long groupId;
        private String docToken;
    }

    @Data
    @Builder
    public static class RemoveFavoriteItemRequest {
        private Long userId;
        private Long itemId;
    }
}
