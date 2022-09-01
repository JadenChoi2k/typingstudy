package com.typingstudy.domain.user;

import com.typingstudy.common.exception.EntityNotFoundException;
import com.typingstudy.common.exception.InvalidAccessException;
import com.typingstudy.common.exception.InvalidParameterException;
import com.typingstudy.domain.typingdoc.DocCommand;
import com.typingstudy.domain.typingdoc.TypingDoc;
import com.typingstudy.domain.typingdoc.TypingDocInfo;
import com.typingstudy.domain.typingdoc.TypingDocService;
import com.typingstudy.domain.user.favorite.FavoriteGroupInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
@Transactional
@SpringBootTest
@ActiveProfiles({"test", "oauth", "mail"})
class UserServiceTest {
    @Autowired
    private UserService userService;
    @Autowired
    private TypingDocService docService;

    UserInfo userJoin() {
        String email = "user@gmail.com";
        String password = "mypassword1234";
        String username = "testuser";
        String profileUrl = ".../testuser.png";
        return userService.join(UserCommand.DomainUserRegisterRequest.builder()
                .email(email)
                .password(password)
                .username(username)
                .profileImage(null)
                .build());
    }

    TypingDocInfo.Main createDoc(Long userId) {
        return docService.createDoc(DocCommand.CreateRequest.builder()
                .authorId(userId)
                .access(TypingDoc.Access.PUBLIC)
                .content("doccontent")
                .title("title")
                .build());
    }

    @Test
    @DisplayName("도메인 회원가입")
    void domain_join() {
        // given
        String email = "user@gmail.com";
        String password = "mypassword1234";
        String username = "testuser";
        String profileUrl = "/default_profile_image.png";
        // when
        UserInfo userInfo = userService.join(UserCommand.DomainUserRegisterRequest.builder()
                .email(email)
                .password(password)
                .username(username)
                .profileImage(null)
                .build());
        // then
        assertThat(userInfo.getId()).isNotNull();
        assertThat(userInfo.getUsername()).isEqualTo(username);
        assertThat(userInfo.getProfileUrl()).isEqualTo(profileUrl);
    }

    @Test
    @DisplayName("로그인")
    void login() {
        // given
        UserInfo userInfo = userJoin();
        String email = "user@gmail.com";
        String password = "mypassword1234";
        // when
        boolean isLogin = userService.login(UserCommand.LoginRequest.builder()
                .email(email)
                .password(password)
                .build());
        // then
        assertThat(isLogin).isTrue();
    }

    @Test
    @DisplayName("로그인 실패 - 존재하지 않는 이메일")
    void login_fail1() {
        // given
        UserInfo userInfo = userJoin();
        String email = "notfound@gmail.com";
        String password = "mypassword1234";
        // when, then
        assertThatThrownBy(() -> userService.login(
                UserCommand.LoginRequest.builder()
                        .email(email)
                        .password(password)
                        .build())
        ).isInstanceOf(EntityNotFoundException.class);
    }

    // 5회 이상 틀릴 시 차단되는 기능(도메인 유저)은 인터페이스 계층에서 구현
    @Test
    @DisplayName("로그인 실패 - 일치하지 않는 비밀번호")
    void login_fail2() {
        // given
        UserInfo userInfo = userJoin();
        String email = "user@gmail.com";
        String password = "not-mypassword1234";
        // when
        boolean isLogin = userService.login(UserCommand.LoginRequest.builder()
                .email(email)
                .password(password)
                .build());
        // then
        assertThat(isLogin).isFalse();
    }

    @Test
    @DisplayName("유저 정보 가져오기")
    void retrieve_user_info() {
        // given
        UserInfo userInfo = userJoin();
        // when
        UserInfo retrieveUser = userService.retrieve(Long.parseLong(userInfo.getId()));
        // then
        assertThat(retrieveUser.getId()).isEqualTo(userInfo.getId());
        assertThat(retrieveUser.getUsername()).isEqualTo(userInfo.getUsername());
        assertThat(retrieveUser.getProfileUrl()).isEqualTo(userInfo.getProfileUrl());
    }

    @Test
    @DisplayName("유저 정보 가져오기 실패 - 존재하지 않는 유저")
    void retrieve_user_info_fail() {
        // given
        userJoin();
        // when, then
        assertThatThrownBy(() -> userService.retrieve(11280312L))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("유저 탈퇴")
    void resign() {
        // given
        UserInfo userInfo = userJoin();
        // when
        userService.resign(UserCommand.ResignUserRequest.builder()
                .userId(userInfo.getIdLong())
                .username(userInfo.getUsername())
                .build());
        // then
        assertThatThrownBy(() -> userService.retrieve(userInfo.getIdLong()))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("유저 탈퇴 실패 - 유저 이름 불일치")
    void resign_fail() {
        // given
        UserInfo userInfo = userJoin();
        // when, then
        assertThatThrownBy(() -> userService.resign(UserCommand.ResignUserRequest.builder()
                .userId(userInfo.getIdLong())
                .username(userInfo.getUsername() + "nono")
                .build())
        ).isInstanceOf(InvalidParameterException.class);
    }

    @Test
    @DisplayName("즐겨찾기 그룹 만들기")
    void create_favorite_group() {
        // given
        UserInfo userInfo = userJoin();
        String groupName = "my favorite";
        // when
        FavoriteGroupInfo.GroupInfo groupInfo = userService.createFavoriteGroup(UserCommand.CreateFavoriteGroupRequest.builder()
                .userId(Long.parseLong(userInfo.getId()))
                .groupName(groupName)
                .build());
        // then
        assertThat(groupInfo.getGroupId()).isNotNull();
        assertThat(groupInfo.getUserId()).isEqualTo(userInfo.getIdLong());
        assertThat(groupInfo.getGroupName()).isEqualTo(groupName);
    }

    @Test
    @DisplayName("즐겨찾기 그룹 만들기 실패 - 존재하지 않는 유저 아이디")
    void create_favorite_group_fail() {
        // given
        UserInfo userInfo = userJoin();
        String groupName = "my favorite";
        // when, then
        assertThatThrownBy(() -> userService.createFavoriteGroup(
                        UserCommand.CreateFavoriteGroupRequest.builder()
                                .userId(Long.parseLong(userInfo.getId() + 123123L))
                                .groupName(groupName)
                                .build()
                )
        ).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("즐겨찾기 그룹 제거")
    void remove_favorite_group() {
        // given
        UserInfo userInfo = userJoin();
        String groupName = "my favorite";
        FavoriteGroupInfo.GroupInfo groupInfo = userService.createFavoriteGroup(UserCommand.CreateFavoriteGroupRequest.builder()
                .userId(Long.parseLong(userInfo.getId()))
                .groupName(groupName)
                .build());
        // when
        userService.removeFavoriteGroup(userInfo.getIdLong(), groupInfo.getGroupId());
        // then
        assertThatThrownBy(() -> userService.retrieveFavoriteGroupItems(userInfo.getIdLong(), groupInfo.getGroupId(), 0))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("즐겨찾기 그룹 수정")
    void edit_favorite_group() {
        // given
        UserInfo userInfo = userJoin();
        String groupName = "my favorite";
        String editName = "new favorite";
        FavoriteGroupInfo.GroupInfo groupInfo = userService.createFavoriteGroup(UserCommand.CreateFavoriteGroupRequest.builder()
                .userId(Long.parseLong(userInfo.getId()))
                .groupName(groupName)
                .build());
        // when
        FavoriteGroupInfo.GroupInfo editGroupInfo = userService.editFavoriteGroup(UserCommand.EditFavoriteGroupRequest.builder()
                .userId(userInfo.getIdLong())
                .groupId(groupInfo.getGroupId())
                .groupName(editName)
                .build());
        // then
        assertThat(editGroupInfo.getGroupName()).isEqualTo(editName);
    }

    @Test
    @DisplayName("즐겨찾기 그룹 수정 실패 - 권한 없음")
    void edit_favorite_group_fail() {
        // given
        UserInfo userInfo = userJoin();
        String groupName = "my favorite";
        String editName = "new favorite";
        FavoriteGroupInfo.GroupInfo groupInfo = userService.createFavoriteGroup(UserCommand.CreateFavoriteGroupRequest.builder()
                .userId(Long.parseLong(userInfo.getId()))
                .groupName(groupName)
                .build());
        // when, then
        assertThatThrownBy(() -> userService.editFavoriteGroup(UserCommand.EditFavoriteGroupRequest.builder()
                .userId(userInfo.getIdLong() + 123L)
                .groupId(groupInfo.getGroupId())
                .groupName(editName)
                .build())
        ).isInstanceOf(InvalidAccessException.class);
    }

    @Test
    @DisplayName("즐겨찾기 아이템 추가")
    void add_favorite_item() {
        // given
        UserInfo userInfo = userJoin();
        String groupName = "my favorite";
        TypingDocInfo.Main doc = createDoc(userInfo.getIdLong());
        FavoriteGroupInfo.GroupInfo groupInfo = userService.createFavoriteGroup(UserCommand.CreateFavoriteGroupRequest.builder()
                .userId(Long.parseLong(userInfo.getId()))
                .groupName(groupName)
                .build());
        // when
        log.info("groupId={}", groupInfo.getGroupId());
        FavoriteGroupInfo.ItemInfo itemInfo = userService.addFavoriteItem(UserCommand.AddFavoriteItemRequest.builder()
                .userId(userInfo.getIdLong())
                .groupId(groupInfo.getGroupId())
                .docToken(doc.getDocToken())
                .build());
        // then
        assertThat(itemInfo.getGroupId()).isEqualTo(groupInfo.getGroupId());
        assertThat(itemInfo.getItemId()).isNotNull();
        assertThat(itemInfo.getDocToken()).isEqualTo(doc.getDocToken());
    }

    @Test
    @DisplayName("즐겨찾기 아이템 추가 실패 - 존재하지 않는 그룹 아이디")
    void add_favorite_item_fail1() {
        // given
        UserInfo userInfo = userJoin();
        String groupName = "my favorite";
        TypingDocInfo.Main doc = createDoc(userInfo.getIdLong());
        FavoriteGroupInfo.GroupInfo groupInfo = userService.createFavoriteGroup(UserCommand.CreateFavoriteGroupRequest.builder()
                .userId(Long.parseLong(userInfo.getId()))
                .groupName(groupName)
                .build());
        // when, then
        assertThatThrownBy(() -> userService.addFavoriteItem(
                        UserCommand.AddFavoriteItemRequest.builder()
                                .userId(userInfo.getIdLong())
                                .groupId(groupInfo.getGroupId() + 123L)
                                .docToken(doc.getDocToken())
                                .build()
                )
        ).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("즐겨찾기 아이템 추가 실패 - 일치하지 않는 소유자")
    void add_favorite_item_fail2() {
        // given
        UserInfo userInfo = userJoin();
        String groupName = "my favorite";
        TypingDocInfo.Main doc = createDoc(userInfo.getIdLong());
        FavoriteGroupInfo.GroupInfo groupInfo = userService.createFavoriteGroup(UserCommand.CreateFavoriteGroupRequest.builder()
                .userId(Long.parseLong(userInfo.getId()))
                .groupName(groupName)
                .build());
        // when, then
        assertThatThrownBy(() -> userService.addFavoriteItem(
                        UserCommand.AddFavoriteItemRequest.builder()
                                .userId(userInfo.getIdLong() + 123L)
                                .groupId(groupInfo.getGroupId())
                                .docToken(doc.getDocToken())
                                .build()
                )
        ).isInstanceOf(InvalidAccessException.class);
    }

    @Test
    @DisplayName("즐겨찾기 아이템 추가 실패 - 존재하지 않는 문서")
    void add_favorite_item_fail3() {
        // given
        UserInfo userInfo = userJoin();
        String groupName = "my favorite";
        TypingDocInfo.Main doc = createDoc(userInfo.getIdLong());
        FavoriteGroupInfo.GroupInfo groupInfo = userService.createFavoriteGroup(UserCommand.CreateFavoriteGroupRequest.builder()
                .userId(Long.parseLong(userInfo.getId()))
                .groupName(groupName)
                .build());
        // when, then
        assertThatThrownBy(() -> userService.addFavoriteItem(
                        UserCommand.AddFavoriteItemRequest.builder()
                                .userId(userInfo.getIdLong())
                                .groupId(groupInfo.getGroupId())
                                .docToken(doc.getDocToken() + "notfound")
                                .build()
                )
        ).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("유저의 즐겨찾기 그룹 페이징")
    void retrieve_favorite_groups() {
        // given
        UserInfo userInfo = userJoin();
        for (int i = 0; i < 100; i++) {
            userService.createFavoriteGroup(UserCommand.CreateFavoriteGroupRequest.builder()
                    .userId(Long.parseLong(userInfo.getId()))
                    .groupName("groupName " + i)
                    .build());
        }
        // when
        List<FavoriteGroupInfo.GroupInfo> groups = userService.retrieveFavoriteGroups(userInfo.getIdLong(), 0);
        // then
        assertThat(groups)
                .hasSize(20) // 20개씩 받아온다
                .allMatch(group -> group.getGroupId() != null) // 모든 원소의 아이디 값은 존재한다
                .allMatch(group -> group.getUserId().equals(userInfo.getIdLong())) // 모든 원소의 소유자는 같다
                .isSortedAccordingTo(Comparator.comparing(FavoriteGroupInfo.GroupInfo::getCreated).reversed()); // 데이터는 생성일로 내림차순 정렬되어 있다
    }

    @Test
    @DisplayName("즐겨찾기 그룹 내 아이템 페이징")
    void retrieve_favorite_items() {
        // given
        UserInfo userInfo = userJoin();
        FavoriteGroupInfo.GroupInfo groupInfo = userService.createFavoriteGroup(UserCommand.CreateFavoriteGroupRequest.builder()
                .userId(Long.parseLong(userInfo.getId()))
                .groupName("groupName")
                .build());
        for (int i = 0; i < 100; i++) {
            userService.addFavoriteItem(UserCommand.AddFavoriteItemRequest.builder()
                    .userId(userInfo.getIdLong())
                    .docToken(createDoc(userInfo.getIdLong()).getDocToken())
                    .groupId(groupInfo.getGroupId())
                    .build());
        }
        // when
        List<FavoriteGroupInfo.ItemInfo> favoriteItems =
                userService.retrieveFavoriteGroupItems(userInfo.getIdLong(), groupInfo.getGroupId(), 0);
        // then
        assertThat(favoriteItems)
                .hasSize(20)
                .allMatch(item -> item.getGroupId().equals(groupInfo.getGroupId()))
                .allMatch(item -> item.getDocToken() != null)
                .isSortedAccordingTo(Comparator.comparing(FavoriteGroupInfo.ItemInfo::getItemId));
    }

    @Test
    @DisplayName("즐겨찾기 아이템 삭제")
    void remove_favorite_item() {
        // given
        UserInfo userInfo = userJoin();
        String groupName = "my favorite";
        TypingDocInfo.Main doc = createDoc(userInfo.getIdLong());
        FavoriteGroupInfo.GroupInfo groupInfo = userService.createFavoriteGroup(UserCommand.CreateFavoriteGroupRequest.builder()
                .userId(Long.parseLong(userInfo.getId()))
                .groupName(groupName)
                .build());
        FavoriteGroupInfo.ItemInfo itemInfo = userService.addFavoriteItem(UserCommand.AddFavoriteItemRequest.builder()
                .userId(userInfo.getIdLong())
                .groupId(groupInfo.getGroupId())
                .docToken(doc.getDocToken())
                .build());
        // when
        userService.removeFavoriteItem(UserCommand.RemoveFavoriteItemRequest.builder()
                .itemId(itemInfo.getItemId())
                .userId(userInfo.getIdLong())
                .build());
        // then
        List<FavoriteGroupInfo.ItemInfo> items = userService.retrieveFavoriteGroupItems(userInfo.getIdLong(), groupInfo.getGroupId(), 0);
        assertThat(items).isEmpty();
    }
}