package com.typingstudy.domain.user.profile;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProfileImageInfo {
    private String extension;
    private byte[] data;

    public static ProfileImageInfo of(ProfileImage profileImage) {
        return ProfileImageInfo.builder()
                .extension(profileImage.getExtension())
                .data(profileImage.getData())
                .build();
    }
}
