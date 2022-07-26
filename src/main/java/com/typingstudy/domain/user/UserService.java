package com.typingstudy.domain.user;

import com.typingstudy.domain.user.favorite.FavoriteGroupInfo;

import java.util.List;

public interface UserService {
    boolean login(UserCommand.LoginRequest loginRequest);

    UserInfo join(UserCommand.DomainUserRegisterRequest domainUserRegisterRequest);

    UserInfo join(UserCommand.SocialUserRegisterRequest socialUserRegisterRequest);

    UserInfo retrieve(Long userId);

    FavoriteGroupInfo.GroupInfo createFavoriteGroup(UserCommand.CreateFavoriteGroupRequest createFavoriteGroupRequest);

    FavoriteGroupInfo.ItemInfo addFavoriteItem(UserCommand.AddFavoriteItemRequest addFavoriteItemRequest);
    
    List<FavoriteGroupInfo.GroupInfo> retrieveFavoriteGroups(Long userId, int page);

    // 만약 group의 소유자가 요청된 user가 아니면 InvalidAccessException을 발생시킨다.
    List<FavoriteGroupInfo.ItemInfo> retrieveFavoriteGroup(Long userId, Long groupId, int page);
}
