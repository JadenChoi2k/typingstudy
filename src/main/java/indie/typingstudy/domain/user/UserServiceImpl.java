package indie.typingstudy.domain.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserReader userReader;
    private final UserStore userStore;

    @Override
    public boolean login(String email, String password) {
        log.info("login tried - email: {}, password: {}", email, password);
        return userReader.findByEmail(email).getPassword().equals(password);
    }

    @Override
    public UserInfo join(UserCommand.DomainUserRegisterRequest domainUserRegisterRequest) {
        log.info("domain join request: {}", domainUserRegisterRequest);
        User user = User.createDomainUser(
                domainUserRegisterRequest.getEmail(),
                domainUserRegisterRequest.getPassword(),
                domainUserRegisterRequest.getUsername(),
                domainUserRegisterRequest.getProfileUrl()
        );
        return UserInfo.of(userStore.store(user));
    }

    @Override
    public UserInfo join(UserCommand.SocialUserRegisterRequest socialUserRegisterRequest) {
        log.info("social join request: {}", socialUserRegisterRequest);
        User user = User.createSocialLoginUser(
                socialUserRegisterRequest.getPlatform(),
                socialUserRegisterRequest.getEmail(),
                socialUserRegisterRequest.getUsername(),
                socialUserRegisterRequest.getProfileUrl()
        );
        return UserInfo.of(userStore.store(user));
    }

    @Override
    public UserInfo retrieve(Long userId) {
        return UserInfo.of(userReader.findById(userId));
    }
}
