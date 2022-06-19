package indie.typingstudy.domain.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static indie.typingstudy.domain.user.UserCommand.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserReader userReader;
    private final UserStore userStore;

    @Override
    public boolean login(LoginRequest request) {
        log.info("login tried - email: {}, password: {}", request.getEmail(), request.getPassword());
        return userReader.findByEmail(request.getEmail()).getPassword().equals(request.getPassword());
    }

    @Override
    public UserInfo join(DomainUserRegisterRequest request) {
        log.info("domain join request: {}", request);
        User user = User.createDomainUser(
                request.getEmail(),
                request.getPassword(),
                request.getUsername(),
                request.getProfileUrl()
        );
        return UserInfo.of(userStore.store(user));
    }

    @Override
    public UserInfo join(SocialUserRegisterRequest request) {
        log.info("social join request: {}", request);
        User user = User.createSocialLoginUser(
                request.getPlatform(),
                request.getEmail(),
                request.getUsername(),
                request.getProfileUrl()
        );
        return UserInfo.of(userStore.store(user));
    }

    @Override
    public UserInfo retrieve(Long userId) {
        return UserInfo.of(userReader.findById(userId));
    }
}
