package com.typingstudy.interfaces.user;

import com.typingstudy.application.user.UserFacade;
import com.typingstudy.common.config.auth.PrincipalDetails;
import com.typingstudy.common.response.CommonResponse;
import com.typingstudy.domain.user.UserCommand;
import com.typingstudy.domain.user.UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
        UserDto.JoinSuccess success = userDtoMapper.of(joinedUser);
        return CommonResponse.success(success);
    }

    @GetMapping("/test")
    public String test() {
        return "test";
    }

    @GetMapping("/me")
    public CommonResponse me() {
        UserInfo userInfo = userFacade.retrieve(getUserId());
        return CommonResponse.success(userInfo);
    }

    @GetMapping("/docs")
    public CommonResponse docs() {
        return null;
    }

    @GetMapping("/favorites")
    public Map<String, Object> favorites() {
        return null;
    }

    @GetMapping("/history")
    public Map<String, Object> history() {
        return null;
    }

    @PostMapping("/logout")
    public Map<String, Object> logout() {
        return null;
    }

    @PostMapping("/resign")
    public Map<String, Object> resignService() {
        return null;
    }

    private Long getUserId() {
        var principal =
                (PrincipalDetails) SecurityContextHolder.getContext()
                        .getAuthentication().getPrincipal();
        return principal.getUser().getId();
    }
}
