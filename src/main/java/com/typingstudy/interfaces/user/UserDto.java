package com.typingstudy.interfaces.user;

import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;

public class UserDto {

    @Data
    public static class LoginRequest {
        @NotNull(message = "이메일을 입력해주세요")
        private String email;
        @NotNull(message = "비밀번호를 입력해주세요")
        private String password;
    }

    @Data
    public static class JoinRequest {
        @Email(message = "이메일 형식으로 기재해주시기 바랍니다")
        private String email;
        @Max(value = 30, message = "비밀번호는 30자 이하입니다.")
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$",
                message = "비밀번호는 8자 이상의 영문자, 숫자 조합으로 이루어집니다.")
        private String password;
        @Max(value = 20, message = "유저 이름은 20자 이하입니다")
        @Pattern(regexp = "[가-힣A-Za-z][가-힣A-Za-z\\d]+",
                message = "유저 이름은 2자 이상의 영문자, 한글, 숫자로 이루어집니다.")
        private String username;
        private String profileUrl;
    }

    @Data
    public static class CreateFavoriteGroupRequest {
        private Long userId;
        @Size(min = 1, max = 50, message = "그룹 이름은 1에서 50자 이내입니다.")
        private String groupName;
    }

    @Data
    public static class EditFavoriteGroupRequest {
        private Long groupId;
        private Long userId;
        @Size(min = 1, max = 50, message = "그룹 이름은 1에서 50자 이내입니다.")
        private String groupName;
    }

    @Data
    public static class AddFavoriteItemRequest {
        private Long userId;
        private Long groupId;
        private String dcoToken;
    }

    @Data
    public static class RemoveFavoriteItemRequest {
        private Long userId;
        private Long itemId;
    }

    @Data
    public static class ResignUserRequest {
        private Long userId;
        @NotNull(message = "필수 파라미터: username")
        private String username;
    }


    @Data
    public static class Main {
        private String id;
        private String username;
        private String profileUrl;
    }

    @Data
    public static class FavoriteGroupDto {
        private Long groupId;
        private String groupName;
        private Long userId;
    }

    @Data
    public static class FavoriteGroupWithItemDto {
        private Long groupId;
        private String groupName;
        private Long userId;
        private List<FavoriteItemDto> items;
    }

    @Data
    public static class FavoriteItemDto {
        private Long itemId;
        private String docToken;
        private Long authorId;
        private String title;
        private String access;
        private Integer views;
        private LocalDateTime lastStudyDate;
        private LocalDateTime createDate;
    }
}
