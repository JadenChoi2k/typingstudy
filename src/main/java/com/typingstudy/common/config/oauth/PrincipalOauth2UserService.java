package com.typingstudy.common.config.oauth;

import com.typingstudy.common.config.auth.PrincipalDetails;
import com.typingstudy.common.config.oauth.provider.GoogleUserInfo;
import com.typingstudy.common.config.oauth.provider.KakaoUserInfo;
import com.typingstudy.common.config.oauth.provider.OAuth2UserInfo;
import com.typingstudy.common.exception.AlreadyExistException;
import com.typingstudy.common.exception.InvalidAccessException;
import com.typingstudy.domain.user.User;
import com.typingstudy.infrastructure.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;

    // OAuth 인증 후 db에 갱신
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("load user");
        OAuth2User oAuth2User = super.loadUser(userRequest);
        return process(userRequest, oAuth2User);
    }

    @Transactional
    public OAuth2User process(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {
        log.info("OAuth2User process");
        OAuth2UserInfo oAuth2UserInfo;
        if (userRequest.getClientRegistration().getRegistrationId().equals("google")) {
            oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
            log.info("attributes: {}", oAuth2User.getAttributes());
        } else if (userRequest.getClientRegistration().getRegistrationId().equals("kakao")) {
            log.info("for kakao");
            oAuth2UserInfo = new KakaoUserInfo(oAuth2User.getAttributes());
        } else {
            throw new DisabledException("지원하지 않는 플랫폼입니다");
        }
        Optional<User> userOpt = userRepository.findByEmail(oAuth2UserInfo.getEmail());
        User user;
        if (userOpt.isPresent()) {
            user = userOpt.get();
            // 만약 플랫폼이 다르면 오류 발생
            if (user.getPlatform() != oAuth2UserInfo.getPlatform()) {
                throw new DisabledException("이미 가입된 회원입니다.");
            }
            // 유저 정보 갱신
//            log.info("user({}) 정보 갱신", user.getEmail());
        } else {
            user = new User(
                    oAuth2UserInfo.getPlatform(),
                    oAuth2UserInfo.getEmail(),
                    null,
                    oAuth2UserInfo.getUsername(),
                    oAuth2UserInfo.getProfileUrl(),
                    oAuth2UserInfo.getProviderId()
            );
            userRepository.save(user);
        }
        return new PrincipalDetails(user, oAuth2User.getAttributes());
    }
}
