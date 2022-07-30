package com.typingstudy.domain.user;

import com.typingstudy.common.exception.EntityNotFoundException;
import com.typingstudy.common.exception.InvalidAccessException;
import com.typingstudy.domain.typingdoc.TypingDoc;
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
        log.info("login tried - email: {}, password: xxxx", request.getEmail());
        return passwordEncoder.matches(
                request.getPassword(),
                userReader.findByEmail(request.getEmail()).getPassword()
        );
    }

    @Override
    public UserInfo join(DomainUserRegisterRequest request) {
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        log.info("domain join request: {}", request);
        User user = User.createDomainUser(
                request.getEmail(),
                request.getPassword(),
                request.getUsername(),
                request.getProfileUrl()
        );
        return UserInfo.of(userStore.store(user));
    }

//    @Override
//    public UserInfo join(SocialUserRegisterRequest request) {
//        log.info("social join request: {}", request);
//        User user = User.createSocialLoginUser(
//                request.getPlatform(),
//                request.getEmail(),
//                request.getUsername(),
//                request.getProfileUrl()
//        );
//        return UserInfo.of(userStore.store(user));
//    }

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

    @Override
    public void removeFavoriteGroup(Long userId, Long groupId) {
        FavoriteGroup group = favoriteReader.findGroupById(groupId);
        if (group == null) throw new EntityNotFoundException("존재하지 않는 즐겨찾기 그룹입니다.");
        if (!group.getUser().getId().equals(userId)) throw new InvalidAccessException("권한이 없습니다.");
        favoriteStore.remove(group);
    }

    @Override
    public FavoriteGroupInfo.GroupInfo editFavoriteGroup(EditFavoriteGroupRequest request) {
        FavoriteGroup group = favoriteReader.findGroupById(request.getUserId());
        if (!group.getUser().getId().equals(request.getUserId()))
            throw new InvalidAccessException("권한이 없습니다.");
        group.setGroupName(request.getGroupName());
        return new FavoriteGroupInfo.GroupInfo(group);
    }

    @Override
    public FavoriteGroupInfo.ItemInfo addFavoriteItem(AddFavoriteItemRequest request) {
        log.info("request.getGroupId()={}", request.getGroupId());
        FavoriteGroup group = favoriteReader.findGroupById(request.getGroupId());
        if (group == null) throw new EntityNotFoundException("즐겨찾기 그룹을 찾을 수 없습니다.");
        if (!request.getUserId().equals(group.getUser().getId())) throw new InvalidAccessException("권한이 없습니다.");
        FavoriteItem item = group.createItem(request.getDocToken());
        // 다른 도메인 계층 호출.
        TypingDoc doc = docReader.findByToken(item.getDocToken());
        if (doc == null) throw new EntityNotFoundException("문서를 찾을 수 없습니다.");
        item = favoriteStore.store(item);
        FavoriteGroupInfo.ItemInfo itemInfo = new FavoriteGroupInfo.ItemInfo(item);
        itemInfo.setDocInfo(doc);
        return itemInfo;
    }

    @Override
    public void removeFavoriteItem(RemoveFavoriteItemRequest request) {
        favoriteStore.removeFavoriteItem(request.getUserId(), request.getItemId());
    }
}
