package indie.typingstudy.interfaces.user;

import indie.typingstudy.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserApiController {
    private final UserService userService;

    @PostMapping("/join")
    public Map<String, Object> join(@RequestBody UserDto.JoinRequest request) {
        return null;
    }
}
