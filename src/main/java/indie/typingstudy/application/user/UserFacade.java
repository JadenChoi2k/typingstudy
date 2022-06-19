package indie.typingstudy.application.user;

import indie.typingstudy.domain.user.UserInfo;
import indie.typingstudy.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static indie.typingstudy.domain.user.UserCommand.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserFacade {
    private final UserService userService;

    public boolean login(LoginRequest request) {
        return userService.login(request);
    }

    public UserInfo join(DomainUserRegisterRequest request) {
        return userService.join(request);
    }

    public UserInfo join(SocialUserRegisterRequest request) {
        return userService.join(request);
    }

    public UserInfo retrieve(Long userId) {
        return userService.retrieve(userId);
    }
}
