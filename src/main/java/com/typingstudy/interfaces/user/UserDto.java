package com.typingstudy.interfaces.user;

import lombok.Data;

import java.time.LocalDateTime;

public class UserDto {

    @Data
    public static class LoginRequest {
        private String email;
        private String password;
    }

    @Data
    public static class JoinRequest {
        private String email;
        private String password;
        private String username;
        private String profileUrl;
    }

    @Data
    public static class Main {
        private String id;
        private String name;
        private String profileUrl;
    }

    @Data
    public static class CreateFavoriteGroupRequest {
        private String groupName;
    }

    @Data
    public static class FavoriteGroupDto {
        private Long groupId;
        private String groupName;
    }

    @Data
    public static class FavoriteItemDto {
        private Long itemId;
        private Long docId;
        private Long authorId;
        private String title;
        private String access;
        private Integer views;
        private LocalDateTime lastStudyDate;
        private LocalDateTime createDate;
    }
}
