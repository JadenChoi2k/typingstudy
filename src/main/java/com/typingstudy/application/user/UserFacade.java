package com.typingstudy.application.user;

import com.typingstudy.domain.user.UserCommand;
import com.typingstudy.domain.user.UserInfo;
import com.typingstudy.domain.user.UserService;
import com.typingstudy.domain.user.email.EmailService;
import com.typingstudy.domain.user.email.EmailVerificationEntity;
import com.typingstudy.domain.user.favorite.FavoriteGroupInfo;
import com.typingstudy.domain.user.profile.ProfileImage;
import com.typingstudy.domain.user.profile.ProfileImageInfo;
import com.typingstudy.domain.user.profile.ProfileImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.typingstudy.domain.user.UserCommand.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserFacade {
    private final UserService userService;
    private final EmailService emailService;
    private final ProfileImageService profileImageService;

    public boolean login(LoginRequest request) {
        return userService.login(request);
    }

    public UserInfo join(DomainUserRegisterRequest request) {
        return userService.join(request);
    }

//    public UserInfo join(SocialUserRegisterRequest request) {
//        return userService.join(request);
//    }

    public UserInfo retrieve(Long userId) {
        return userService.retrieve(userId);
    }

    public void resign(UserCommand.ResignUserRequest request) {
        userService.resign(request);
    }

    public FavoriteGroupInfo.GroupInfo createFavoriteGroup(UserCommand.CreateFavoriteGroupRequest request) {
        return userService.createFavoriteGroup(request);
    }

    public List<FavoriteGroupInfo.GroupInfo> retrieveFavoriteGroups(Long userId, int page) {
        return userService.retrieveFavoriteGroups(userId, page);
    }

    public FavoriteGroupInfo.GroupWithItemInfo retrieveFavoriteGroup(Long groupId) {
        return userService.retrieveFavoriteGroup(groupId);
    }

    public List<FavoriteGroupInfo.ItemInfo> retrieveFavoriteGroupItems(Long userId, Long groupId, int page) {
        return userService.retrieveFavoriteGroupItems(userId, groupId, page);
    }

    public void removeFavoriteGroup(Long userId, Long groupId) {
        userService.removeFavoriteGroup(userId, groupId);
    }

    public FavoriteGroupInfo.GroupInfo editFavoriteGroup(UserCommand.EditFavoriteGroupRequest request) {
        return userService.editFavoriteGroup(request);
    }

    public FavoriteGroupInfo.ItemInfo addFavoriteItem(UserCommand.AddFavoriteItemRequest request) {
        return userService.addFavoriteItem(request);
    }

    public void removeFavoriteItem(RemoveFavoriteItemRequest request) {
        userService.removeFavoriteItem(request);
    }

    /* EmailService */
    public void sendVerifyCode(EmailVerificationEntity verificationEntity) {
        emailService.sendVerifyCode(verificationEntity);
    }

    /* ProfileImageService */
    public void saveProfileImage(UserCommand.AddProfileImageRequest request) {
        profileImageService.save(request);
    }

    public ProfileImageInfo findProfileImage(Long userId) {
        return profileImageService.findByUserId(userId);
    }
}
