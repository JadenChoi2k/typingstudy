package com.typingstudy.interfaces.user;

import com.typingstudy.application.user.UserFacade;
import com.typingstudy.common.config.auth.PrincipalDetails;
import com.typingstudy.common.exception.InvalidAccessException;
import com.typingstudy.common.response.CommonResponse;
import com.typingstudy.domain.typingdoc.TypingDocInfo;
import com.typingstudy.domain.user.UserCommand;
import com.typingstudy.domain.user.UserInfo;
import com.typingstudy.interfaces.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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
    private final UserDtoMapper userDtoMapper;

    @PostMapping("/join")
    public CommonResponse join(@RequestBody UserDto.JoinRequest request) {
        UserCommand.DomainUserRegisterRequest registerRequest = userDtoMapper.of(request);
        UserInfo joinedUser = userFacade.join(registerRequest);
        UserDto.Main success = userDtoMapper.of(joinedUser);
        return CommonResponse.success(success);
    }

    @GetMapping("/test")
    public String test() {
        return "test";
    }

    @GetMapping("/me")
    public CommonResponse me() {
        return CommonResponse.success(
                userDtoMapper.of(userFacade.retrieve(getUserId()))
        );
    }

    // 유저가 작성한 문서들을 페이징한다. size는 기본 20개.
    // sort: create_date, study_date, views
    // reverse: true, false
//    @GetMapping("/me/docs")
//    public CommonResponse docs(
//            @RequestParam(name = "page", defaultValue = "0") Integer page,
//            @RequestParam(name = "sort", defaultValue = "date_desc") String sort,
//            @RequestParam(name = "direction", defaultValue = "desc") String direction) {
//        Long userId = getUserId();
//        Map<String, Object> result = new HashMap<>();
//        List<TypingDocInfo.PageItem> pageItems = userFacade.retrieveDocs(userId, page, sort, direction);
//        result.put("page", page);
//        result.put("sort", sort);
//        result.put("reverse", direction);
//        result.put("data", pageItems);
//        return CommonResponse.success(result);
//    }

    // favorites 구현 후 구현하기
    @GetMapping("/favorites")
    public CommonResponse favorites(@RequestParam(name = "page", defaultValue = "0") Integer page) {
        return CommonResponse.success(
                userFacade.retrieveFavoriteGroups(getUserId(), page).stream()
                        .map(userDtoMapper::of)
                        .collect(Collectors.toList())
        );
    }

    @PostMapping("/favorites/create")
    public CommonResponse createFavoriteGroup(@RequestBody UserDto.CreateFavoriteGroupRequest createRequest) {
        return null;
    }

    @GetMapping("/favorites/{groupId}")
    public CommonResponse favoriteGroup(
            @PathVariable Long groupId,
            @RequestParam(name = "page", defaultValue = "0") Integer page) {
        return CommonResponse.success(
                userFacade.retrieveFavoriteGroup(getUserId(), groupId, page).stream()
                        .map(userDtoMapper::of)
                        .collect(Collectors.toList())
        );
    }

    @PatchMapping("/favorites/{groupId}")
    public CommonResponse editFavoriteGroup(@PathVariable Long groupId) {
        return null;
    }

    @DeleteMapping("/favorites/{groupId}")
    public CommonResponse deleteFavoriteGroup() {
        return null;
    }

    // 유저의 docReviewHistory를 볼 수 있다.
    @GetMapping("/history")
    public CommonResponse history() {
        return null;
    }

    // 로그아웃을 한다. LogoutFilter가 동작하지만 아직 미구현 (캐시 이용).
    @PostMapping("/logout")
    public CommonResponse logout() {
        return null;
    }

    @PostMapping("/resign")
    public CommonResponse resignService() {
        return null;
    }

    private Long getUserId() {
        return SecurityUtils.getUserId();
    }
}
