package com.typingstudy.interfaces.user;

import com.typingstudy.domain.typingdoc.history.DocReviewHistoryInfo;
import com.typingstudy.domain.user.UserCommand;
import com.typingstudy.domain.user.UserInfo;
import com.typingstudy.domain.user.favorite.FavoriteGroupInfo;
import com.typingstudy.interfaces.typingdoc.TypingDocDto;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedTargetPolicy = ReportingPolicy.IGNORE // some logic needs post-processing
)
public interface UserDtoMapper {

    UserCommand.DomainUserRegisterRequest of(UserDto.JoinRequest joinRequest);

    UserCommand.CreateFavoriteGroupRequest of(UserDto.CreateFavoriteGroupRequest createFavoriteGroupRequest);

    UserCommand.EditFavoriteGroupRequest of(UserDto.EditFavoriteGroupRequest editFavoriteGroupRequest);

    UserCommand.AddFavoriteItemRequest of(UserDto.AddFavoriteItemRequest addFavoriteItemRequest);

    UserCommand.RemoveFavoriteItemRequest of(UserDto.RemoveFavoriteItemRequest removeFavoriteItemRequest);

    UserCommand.ResignUserRequest of(UserDto.ResignUserRequest resignUserRequest);

    UserDto.Main of(UserInfo userInfo);

    UserDto.FavoriteItemDto of(FavoriteGroupInfo.ItemInfo itemInfo);

    UserDto.FavoriteGroupWithItemDto of(FavoriteGroupInfo.GroupWithItemInfo groupWithItemInfo);

    UserDto.FavoriteGroupDto of(FavoriteGroupInfo.GroupInfo groupInfo);

    TypingDocDto.History of(DocReviewHistoryInfo historyInfo);
}
