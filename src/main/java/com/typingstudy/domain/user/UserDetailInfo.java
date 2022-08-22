package com.typingstudy.domain.user;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UserDetailInfo {
    private Long id;
    private String username;
    private String profileUrl;
    // detailed fields
    private String platform;
    private LocalDateTime joinDateTime;
    // set manually
    private Integer docCount;
    private Integer reviewCount;

    public static UserDetailInfo of(User user) {
        return UserDetailInfo.builder()
                .id(user.getId())
                .username(user.getUsername())
                .profileUrl(user.getProfileUrl())
                .platform(user.getPlatform().name())
                .joinDateTime(user.getCreatedAt())
                .build();
    }

    public void setFields(Integer docCount, Integer reviewCount) {
        this.docCount = docCount;
        this.reviewCount = reviewCount;
    }
}
