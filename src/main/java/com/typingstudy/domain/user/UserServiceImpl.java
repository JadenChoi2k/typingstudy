package com.typingstudy.domain.user;

import com.typingstudy.common.exception.EntityNotFoundException;
import com.typingstudy.domain.typingdoc.TypingDoc;
import com.typingstudy.domain.typingdoc.TypingDocInfo;
import com.typingstudy.domain.typingdoc.TypingDocReader;
import com.typingstudy.domain.user.favorite.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.typingstudy.domain.user.UserCommand.*;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserReader userReader;
    private final TypingDocReader docReader;
    private final UserStore userStore;
    private final FavoriteReader favoriteReader;
    private final FavoriteStore favoriteStore;
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
    @Transactional(readOnly = true)
    public UserInfo retrieve(Long userId) {
        return UserInfo.of(userReader.findById(userId));
    }

    @Override
    public FavoriteGroupInfo.GroupInfo createFavoriteGroup(CreateFavoriteGroupRequest request) {
        User user = userReader.findById(request.getUserId());
        FavoriteGroup favoriteGroup = new FavoriteGroup(request.getGroupName(), user);
        favoriteStore.store(favoriteGroup);
        return new FavoriteGroupInfo.GroupInfo(favoriteGroup);
    }

    @Override
    public FavoriteGroupInfo.ItemInfo addFavoriteItem(AddFavoriteItemRequest request) {
        FavoriteGroup group = favoriteReader.findGroupById(request.getGroupId());
        FavoriteItem item = group.createItem(request.getDocToken());
        TypingDoc doc = docReader.findByToken(item.getDocToken());
        if (doc == null) throw new EntityNotFoundException("문서를 찾을 수 없습니다.");
        item = favoriteStore.store(item);
        FavoriteGroupInfo.ItemInfo itemInfo = new FavoriteGroupInfo.ItemInfo(item);
        itemInfo.setDocInfo(doc);
        return itemInfo;
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
        Map<String, TypingDoc> docTokenToDocMap = docReader
                        .findAllByTokenList(items.stream().map(FavoriteItem::getDocToken).collect(Collectors.toList()))
                        .stream().collect(Collectors.toMap(TypingDoc::getDocToken, Function.identity()));
        return items.stream()
                .map(FavoriteGroupInfo.ItemInfo::new)
                .peek(item -> {
                    if (!docTokenToDocMap.containsKey(item.getDocToken()))
                        throw new EntityNotFoundException("문서를 찾을 수 없습니다.");
                    item.setDocInfo(docTokenToDocMap.get(item.getDocToken()));
                }).collect(Collectors.toList());
    }
}
