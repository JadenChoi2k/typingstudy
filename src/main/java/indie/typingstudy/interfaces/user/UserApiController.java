package indie.typingstudy.interfaces.user;

import indie.typingstudy.application.user.UserFacade;
import indie.typingstudy.domain.user.UserCommand;
import indie.typingstudy.domain.user.UserInfo;
import indie.typingstudy.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserApiController {
    private final UserFacade userFacade;
    private final UserDtoMapper userDtoMapper;

    @PostMapping("/join")
    public Map<String, Object> join(@RequestBody UserDto.JoinRequest request) {
        UserCommand.DomainUserRegisterRequest registerRequest = userDtoMapper.of(request);
        UserInfo joinedUser = userFacade.join(registerRequest);
        UserDto.JoinSuccess success = userDtoMapper.of(joinedUser);
        Map<String, Object> result = new HashMap<>();
        result.put("result", "success");
        result.put("data", success);
        return result;
    }

    @GetMapping("/test")
    public String test() {
        return "test";
    }
}
