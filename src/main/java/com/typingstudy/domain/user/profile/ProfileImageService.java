package com.typingstudy.domain.user.profile;

import com.typingstudy.common.exception.EntityNotFoundException;
import com.typingstudy.domain.user.User;
import com.typingstudy.domain.user.UserCommand;
import com.typingstudy.domain.user.UserReader;
import com.typingstudy.infrastructure.user.profile.ProfileImageJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileImageService {
    private final ProfileImageJpaRepository profileImageRepository;
    private final UserReader userReader;
    private final int UPPER_BOUND = 3 * 1024 * 1024;

    public void save(UserCommand.AddProfileImageRequest request) {
        User user = userReader.findById(request.getUserId());
        ProfileImage profileImage = new ProfileImage(user, request.getExtension(), request.getData());
        if (profileImage.getData().length > UPPER_BOUND) {
            throw new IllegalStateException("저장 용량을 초과하였습니다.");
        }
        profileImageRepository.save(profileImage);
    }

    public ProfileImageInfo findByUserId(Long userId) {
        return ProfileImageInfo.of(profileImageRepository.findByUserId(userId)
                .orElseThrow(() -> {
                    throw new EntityNotFoundException("프로필 사진을 찾을 수 없습니다.");
                })
        );
    }
}
