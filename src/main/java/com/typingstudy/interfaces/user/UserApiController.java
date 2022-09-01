package com.typingstudy.interfaces.user;

import com.typingstudy.application.user.UserFacade;
import com.typingstudy.common.response.CommonResponse;
import com.typingstudy.domain.user.UserCommand;
import com.typingstudy.domain.user.UserInfo;
import com.typingstudy.domain.user.email.EmailVerificationEntity;
import com.typingstudy.domain.user.favorite.FavoriteGroupInfo;
import com.typingstudy.domain.user.profile.ProfileImage;
import com.typingstudy.domain.user.profile.ProfileImageInfo;
import com.typingstudy.interfaces.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public CommonResponse join(@Valid @RequestBody UserDto.JoinRequest requestDto) {
        UserCommand.DomainUserRegisterRequest registerRequest = dtoMapper.of(requestDto);
        // save profile image
        if (registerRequest.hasProfileImage()) {
            userFacade.saveProfileImage(UserCommand.AddProfileImageRequest.builder()
                    .userId(SecurityUtils.getUserId())
                    .extension(registerRequest.getExtension())
                    .data(registerRequest.getProfileImage())
                    .build());
        }
        UserInfo joinedUser = userFacade.join(registerRequest);
        UserDto.Main success = dtoMapper.of(joinedUser);
        return CommonResponse.success(success);
    }

    // TODO: add test
    @GetMapping("/email/verify")
    public CommonResponse sendVerifyCode(@RequestParam String email, HttpSession session) {
        // send mail
        EmailVerificationEntity emailValidationEntity = new EmailVerificationEntity(email);
        userFacade.sendVerifyCode(emailValidationEntity);
        if (emailValidationEntity.getState() == EmailVerificationEntity.State.FAILED) {
            return CommonResponse.fail("email send failed", "EMAIL_SEND_FAIL");
        }
        if (emailValidationEntity.getState() == EmailVerificationEntity.State.OVERLAPPED) {
            return CommonResponse.fail("email overlapped", "EMAIL_OVERLAPPED");
        }
        // session
        session.setMaxInactiveInterval(3000); // 5min
        session.setAttribute("EMAIL_ENTITY", emailValidationEntity);
        // construct response
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("timeout", session.getMaxInactiveInterval());
        responseBody.put("state", emailValidationEntity.getState());
        return CommonResponse.success(responseBody);
    }

    @PostMapping("/email/verify")
    public CommonResponse verifyEmail(@RequestParam String code, HttpSession session) {
        var verifyEntity = (EmailVerificationEntity) session.getAttribute("EMAIL_ENTITY");
        if (verifyEntity == null) {
            return CommonResponse.fail("there is no verifying entity", "NO_ENTITY");
        }
        if (verifyEntity.getState() == EmailVerificationEntity.State.WAITING) {
            verifyEntity.verify(code);
        }
        return CommonResponse.success(verifyEntity.getState());
    }

    // TODO: add test
    @GetMapping("/me/profile")
    public ResponseEntity<Resource> profile() {
        ProfileImageInfo profileImageInfo = userFacade.findProfileImage(SecurityUtils.getUserId());
        ByteArrayResource resource = new ByteArrayResource(profileImageInfo.getData());
        return ResponseEntity.ok()
                .contentLength(resource.contentLength())
                .header(HttpHeaders.CONTENT_TYPE, ContentDisposition.attachment().filename(profileImageInfo.getExtension()).build().toString())
                .body(resource);
    }


    @GetMapping("/me")
    public CommonResponse me() {
        return CommonResponse.success(
                dtoMapper.of(userFacade.retrieve(getUserId()))
        );
    }

    @GetMapping("/me/detail")
    public CommonResponse meDetail() {
        return CommonResponse.success(
                dtoMapper.of(userFacade.retrieveDetail(getUserId()))
        );
    }

    @GetMapping("/info/{userId}")
    public CommonResponse userInfo(@PathVariable Long userId) {
        return CommonResponse.success(
                dtoMapper.of(userFacade.retrieve(userId))
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

    @GetMapping("/favorites")
    public CommonResponse favorites(@RequestParam(name = "page", defaultValue = "0") Integer page) {
        return CommonResponse.success(
                userFacade.retrieveFavoriteGroups(getUserId(), page).stream()
                        .map(dtoMapper::of)
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/favorites/{groupId}")
    public CommonResponse favoriteGroup(
            @PathVariable Long groupId,
            @RequestParam(name = "page", defaultValue = "0") Integer page) {
        FavoriteGroupInfo.GroupWithItemInfo groupWithItemInfo = userFacade.retrieveFavoriteGroup(groupId);
        List<FavoriteGroupInfo.ItemInfo> items = userFacade.retrieveFavoriteGroupItems(getUserId(), groupId, page);
        groupWithItemInfo.setItems(items);
        return CommonResponse.success(
                dtoMapper.of(groupWithItemInfo)
        );
    }

    // todo: add test
    @GetMapping("/favorites/contains")
    public CommonResponse favoriteGroupContainsDoc(@RequestParam(name = "docToken") String docToken) {
        List<FavoriteGroupInfo.ContainsDoc> containsDocs = userFacade.retrieveContainsDoc(SecurityUtils.getUserId(), docToken);
        return CommonResponse.success(
                containsDocs.stream()
                        .map(dtoMapper::of)
                        .toList()
        );
    }

    @PatchMapping("/favorites/{groupId}")
    public CommonResponse editFavoriteGroup(@PathVariable Long groupId,
                                            @Valid @RequestBody UserDto.EditFavoriteGroupRequest requestDto) {
        requestDto.setGroupId(groupId);
        requestDto.setUserId(getUserId());
        FavoriteGroupInfo.GroupInfo groupInfo = userFacade.editFavoriteGroup(dtoMapper.of(requestDto));
        return CommonResponse.success(
                dtoMapper.of(groupInfo)
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

    @DeleteMapping("/favorites/{groupId}/bydoc/{docToken}")
    public CommonResponse removeFavoriteItemByDocToken(@PathVariable Long groupId, @PathVariable String docToken) {
        userFacade.removeFavoriteItemByDocToken(SecurityUtils.getUserId(), groupId, docToken);
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
