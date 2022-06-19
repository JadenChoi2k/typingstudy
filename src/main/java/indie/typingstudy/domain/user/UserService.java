package indie.typingstudy.domain.user;

public interface UserService {
    boolean login(UserCommand.LoginRequest loginRequest);

    UserInfo join(UserCommand.DomainUserRegisterRequest domainUserRegisterRequest);

    UserInfo join(UserCommand.SocialUserRegisterRequest socialUserRegisterRequest);

    UserInfo retrieve(Long userId);
}
