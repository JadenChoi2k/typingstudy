package com.typingstudy.interfaces.user;

import com.typingstudy.domain.user.UserCommand;
import com.typingstudy.domain.user.UserInfo;
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

    UserDto.JoinSuccess of(UserInfo userInfo);
}
