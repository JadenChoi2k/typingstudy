package com.typingstudy.interfaces.user;

import com.typingstudy.domain.user.UserCommand;
import com.typingstudy.domain.user.UserInfo;
import com.typingstudy.domain.user.favorite.FavoriteGroupInfo;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface UserDtoMapper {

    UserCommand.DomainUserRegisterRequest of(UserDto.JoinRequest joinRequest);

    UserDto.Main of(UserInfo userInfo);

    UserDto.FavoriteItemDto of(FavoriteGroupInfo.ItemInfo itemInfo);

    UserDto.FavoriteGroupDto of(FavoriteGroupInfo.GroupInfo groupInfo);
}
