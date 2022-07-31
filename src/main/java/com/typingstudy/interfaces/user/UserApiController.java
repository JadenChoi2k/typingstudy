package com.typingstudy.interfaces.user;

import com.typingstudy.application.user.UserFacade;
import com.typingstudy.common.response.CommonResponse;
import com.typingstudy.domain.user.UserCommand;
import com.typingstudy.domain.user.UserInfo;
import com.typingstudy.domain.user.favorite.FavoriteGroupInfo;
import com.typingstudy.interfaces.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserApiController {
    private final UserFacade userFacade;
    private final UserDtoMapper dtoMapper;

    // 도메인 유저의 가입 경로.
    // 소셜 유저는 oauth 패키지에서 자동 가입
    @PostMapping("/join")
    public CommonResponse join(@RequestBody UserDto.JoinRequest requestDto) {
        UserCommand.DomainUserRegisterRequest registerRequest = dtoMapper.of(requestDto);
        UserInfo joinedUser = userFacade.join(registerRequest);
        UserDto.Main success = dtoMapper.of(joinedUser);
        return CommonResponse.success(success);
    }

    @GetMapping("/me")
    public CommonResponse me() {
        return CommonResponse.success(
                dtoMapper.of(userFacade.retrieve(getUserId()))
        );
    }

    // favorites 구현 후 구현하기
    @GetMapping("/favorites")
    public CommonResponse favorites(@RequestParam(name = "page", defaultValue = "0") Integer page) {
        return CommonResponse.success(
                userFacade.retrieveFavoriteGroups(getUserId(), page).stream()
                        .map(dtoMapper::of)
                        .collect(Collectors.toList())
        );
    }

    @PostMapping("/favorites/create")
    public CommonResponse createFavoriteGroup(@RequestBody UserDto.CreateFavoriteGroupRequest requestDto) {
        requestDto.setUserId(getUserId());
        FavoriteGroupInfo.GroupInfo groupInfo = userFacade.createFavoriteGroup(dtoMapper.of(requestDto));
        return CommonResponse.success(
                dtoMapper.of(groupInfo)
        );
    }

    @GetMapping("/favorites/{groupId}")
    public CommonResponse favoriteGroup(
            @PathVariable Long groupId,
            @RequestParam(name = "page", defaultValue = "0") Integer page) {
        return CommonResponse.success(
                userFacade.retrieveFavoriteGroup(getUserId(), groupId, page).stream()
                        .map(dtoMapper::of)
                        .collect(Collectors.toList())
        );
    }

    @PatchMapping("/favorites/{groupId}")
    public CommonResponse editFavoriteGroup(@PathVariable Long groupId,
                                            @Valid @RequestBody UserDto.EditFavoriteGroupRequest requestDto) {
        requestDto.setGroupId(groupId);
        requestDto.setUserId(getUserId());
        return CommonResponse.success(
                userFacade.editFavoriteGroup(dtoMapper.of(requestDto))
        );
    }

    @DeleteMapping("/favorites/{groupId}")
    public CommonResponse removeFavoriteGroup(@PathVariable Long groupId) {
        userFacade.removeFavoriteGroup(getUserId(), groupId);
        return CommonResponse.ok();
    }

    @PostMapping("/favorites/{groupId}/add")
    public CommonResponse addFavoriteItem(@PathVariable Long groupId,
                                          @Valid @RequestBody UserDto.AddFavoriteItemRequest requestDto) {
        requestDto.setGroupId(groupId);
        requestDto.setUserId(getUserId());
        userFacade.addFavoriteItem(dtoMapper.of(requestDto));
        return CommonResponse.ok();
    }

    @DeleteMapping("/favorites/{groupId}/{itemId}")
    public CommonResponse removeFavoriteItem(@PathVariable Long groupId,
                                             @PathVariable Long itemId) {
        UserDto.RemoveFavoriteItemRequest requestDto = new UserDto.RemoveFavoriteItemRequest();
        requestDto.setUserId(getUserId());
        requestDto.setItemId(itemId);
        userFacade.removeFavoriteItem(dtoMapper.of(requestDto));
        return CommonResponse.ok();
    }

    // 로그아웃을 한다. LogoutFilter가 동작하지만 아직 미구현 (캐시 이용).
    @PostMapping("/logout")
    public CommonResponse logout() {
        return CommonResponse.ok();
    }

    // 서비스로부터 탈퇴.
    @PostMapping("/resign")
    public CommonResponse resignService(@Valid @RequestBody UserDto.ResignUserRequest request) {
        request.setUserId(getUserId());
        userFacade.resign(dtoMapper.of(request));
        return CommonResponse.ok();
    }

    private Long getUserId() {
        return SecurityUtils.getUserId();
    }
}
