package com.typingstudy.domain.user;

import com.typingstudy.domain.user.favorite.FavoriteGroupInfo;

import java.util.List;

public interface UserService {
    boolean login(UserCommand.LoginRequest request);

    UserInfo join(UserCommand.DomainUserRegisterRequest request);

    // SpringBoot Security 계층에서 처리하므로 더이상 쓰이지 않는다.
//    @Deprecated
//    UserInfo join(UserCommand.SocialUserRegisterRequest socialUserRegisterRequest);
    UserInfo retrieve(Long userId);

    void resign(UserCommand.ResignUserRequest request);

    FavoriteGroupInfo.GroupInfo createFavoriteGroup(UserCommand.CreateFavoriteGroupRequest request);

    FavoriteGroupInfo.GroupWithItemInfo retrieveFavoriteGroup(Long groupId);

    List<FavoriteGroupInfo.ItemInfo> retrieveFavoriteGroupItems(Long userId, Long groupId, int page);

    // 만약 group의 소유자가 요청된 user가 아니면 InvalidAccessException을 발생시킨다.
    List<FavoriteGroupInfo.GroupInfo> retrieveFavoriteGroups(Long userId, int page);

    void removeFavoriteGroup(Long userId, Long groupId);

    FavoriteGroupInfo.GroupInfo editFavoriteGroup(UserCommand.EditFavoriteGroupRequest request);

    FavoriteGroupInfo.ItemInfo addFavoriteItem(UserCommand.AddFavoriteItemRequest request);

    void removeFavoriteItem(UserCommand.RemoveFavoriteItemRequest request);
}
