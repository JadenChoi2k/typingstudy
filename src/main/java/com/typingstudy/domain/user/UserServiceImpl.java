package com.typingstudy.domain.user;

import com.typingstudy.common.exception.EntityNotFoundException;
import com.typingstudy.domain.typingdoc.TypingDoc;
import com.typingstudy.domain.typingdoc.TypingDocInfo;
import com.typingstudy.domain.typingdoc.TypingDocReader;
import com.typingstudy.domain.user.favorite.FavoriteGroupInfo;
import com.typingstudy.domain.user.favorite.FavoriteItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.typingstudy.domain.user.UserCommand.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserReader userReader;
    private final TypingDocReader docReader;
    private final UserStore userStore;
    private final BCryptPasswordEncoder passwordEncoder;

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
                passwordEncoder.encode(request.getPassword()),
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

    @Override
    public List<FavoriteGroupInfo.GroupInfo> retrieveFavoriteGroups(Long userId, int page) {
        return userReader.findAllFavoriteGroups(userId, page, 20).stream()
                .map(FavoriteGroupInfo.GroupInfo::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<FavoriteGroupInfo.ItemInfo> retrieveFavoriteGroup(Long userId, Long groupId, int page) {
        List<FavoriteItem> items = userReader.findAllFavoriteItems(groupId, page, 20);
        Map<Long, TypingDoc> idToDocMap = docReader
                        .findAllByIdList(items.stream().map(FavoriteItem::getDocId).collect(Collectors.toList()))
                        .stream().collect(Collectors.toMap(TypingDoc::getId, Function.identity()));
        return userReader.findAllFavoriteItems(groupId, page, 20).stream()
                .map(FavoriteGroupInfo.ItemInfo::new)
                .peek(item -> {
                    if (!idToDocMap.containsKey(item.getDocId())) throw new EntityNotFoundException("문서를 찾을 수 없습니다.");
                    item.setDocInfo(new TypingDocInfo.PageItem(idToDocMap.get(item.getDocId())));
                }).collect(Collectors.toList());
    }


}
