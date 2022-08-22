package com.typingstudy.interfaces.user;

import com.typingstudy.domain.typingdoc.history.DocReviewHistoryInfo;
import com.typingstudy.domain.user.UserCommand;
import com.typingstudy.domain.user.UserDetailInfo;
import com.typingstudy.domain.user.UserInfo;
import com.typingstudy.domain.user.favorite.FavoriteGroupInfo;
import com.typingstudy.interfaces.typingdoc.TypingDocDto;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-08-21T03:17:21+0900",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 17.0.3 (Oracle Corporation)"
)
@Component
public class UserDtoMapperImpl implements UserDtoMapper {

    @Override
    public UserCommand.DomainUserRegisterRequest of(UserDto.JoinRequest joinRequest) {
        if ( joinRequest == null ) {
            return null;
        }

        UserCommand.DomainUserRegisterRequest.DomainUserRegisterRequestBuilder domainUserRegisterRequest = UserCommand.DomainUserRegisterRequest.builder();

        domainUserRegisterRequest.email( joinRequest.getEmail() );
        domainUserRegisterRequest.password( joinRequest.getPassword() );
        domainUserRegisterRequest.username( joinRequest.getUsername() );
        domainUserRegisterRequest.extension( joinRequest.getExtension() );
        domainUserRegisterRequest.profileImage( byteArrayTobyteArray( joinRequest.getProfileImage() ) );

        return domainUserRegisterRequest.build();
    }

    @Override
    public UserCommand.CreateFavoriteGroupRequest of(UserDto.CreateFavoriteGroupRequest createFavoriteGroupRequest) {
        if ( createFavoriteGroupRequest == null ) {
            return null;
        }

        UserCommand.CreateFavoriteGroupRequest.CreateFavoriteGroupRequestBuilder createFavoriteGroupRequest1 = UserCommand.CreateFavoriteGroupRequest.builder();

        createFavoriteGroupRequest1.groupName( createFavoriteGroupRequest.getGroupName() );
        createFavoriteGroupRequest1.userId( createFavoriteGroupRequest.getUserId() );

        return createFavoriteGroupRequest1.build();
    }

    @Override
    public UserCommand.EditFavoriteGroupRequest of(UserDto.EditFavoriteGroupRequest editFavoriteGroupRequest) {
        if ( editFavoriteGroupRequest == null ) {
            return null;
        }

        UserCommand.EditFavoriteGroupRequest.EditFavoriteGroupRequestBuilder editFavoriteGroupRequest1 = UserCommand.EditFavoriteGroupRequest.builder();

        editFavoriteGroupRequest1.groupId( editFavoriteGroupRequest.getGroupId() );
        editFavoriteGroupRequest1.userId( editFavoriteGroupRequest.getUserId() );
        editFavoriteGroupRequest1.groupName( editFavoriteGroupRequest.getGroupName() );

        return editFavoriteGroupRequest1.build();
    }

    @Override
    public UserCommand.AddFavoriteItemRequest of(UserDto.AddFavoriteItemRequest addFavoriteItemRequest) {
        if ( addFavoriteItemRequest == null ) {
            return null;
        }

        UserCommand.AddFavoriteItemRequest.AddFavoriteItemRequestBuilder addFavoriteItemRequest1 = UserCommand.AddFavoriteItemRequest.builder();

        addFavoriteItemRequest1.userId( addFavoriteItemRequest.getUserId() );
        addFavoriteItemRequest1.groupId( addFavoriteItemRequest.getGroupId() );
        addFavoriteItemRequest1.docToken( addFavoriteItemRequest.getDocToken() );

        return addFavoriteItemRequest1.build();
    }

    @Override
    public UserCommand.RemoveFavoriteItemRequest of(UserDto.RemoveFavoriteItemRequest removeFavoriteItemRequest) {
        if ( removeFavoriteItemRequest == null ) {
            return null;
        }

        UserCommand.RemoveFavoriteItemRequest.RemoveFavoriteItemRequestBuilder removeFavoriteItemRequest1 = UserCommand.RemoveFavoriteItemRequest.builder();

        removeFavoriteItemRequest1.userId( removeFavoriteItemRequest.getUserId() );
        removeFavoriteItemRequest1.itemId( removeFavoriteItemRequest.getItemId() );

        return removeFavoriteItemRequest1.build();
    }

    @Override
    public UserCommand.ResignUserRequest of(UserDto.ResignUserRequest resignUserRequest) {
        if ( resignUserRequest == null ) {
            return null;
        }

        UserCommand.ResignUserRequest.ResignUserRequestBuilder resignUserRequest1 = UserCommand.ResignUserRequest.builder();

        resignUserRequest1.username( resignUserRequest.getUsername() );
        resignUserRequest1.userId( resignUserRequest.getUserId() );

        return resignUserRequest1.build();
    }

    @Override
    public UserDto.Main of(UserInfo userInfo) {
        if ( userInfo == null ) {
            return null;
        }

        UserDto.Main main = new UserDto.Main();

        main.setId( userInfo.getId() );
        main.setUsername( userInfo.getUsername() );
        main.setProfileUrl( userInfo.getProfileUrl() );

        return main;
    }

    @Override
    public UserDto.Detail of(UserDetailInfo userDetailInfo) {
        if ( userDetailInfo == null ) {
            return null;
        }

        UserDto.Detail detail = new UserDto.Detail();

        detail.setId( userDetailInfo.getId() );
        detail.setUsername( userDetailInfo.getUsername() );
        detail.setProfileUrl( userDetailInfo.getProfileUrl() );
        detail.setPlatform( userDetailInfo.getPlatform() );
        detail.setJoinDateTime( userDetailInfo.getJoinDateTime() );
        detail.setDocCount( userDetailInfo.getDocCount() );
        detail.setReviewCount( userDetailInfo.getReviewCount() );

        return detail;
    }

    @Override
    public UserDto.FavoriteItemDto of(FavoriteGroupInfo.ItemInfo itemInfo) {
        if ( itemInfo == null ) {
            return null;
        }

        UserDto.FavoriteItemDto favoriteItemDto = new UserDto.FavoriteItemDto();

        favoriteItemDto.setItemId( itemInfo.getItemId() );
        favoriteItemDto.setDocToken( itemInfo.getDocToken() );
        favoriteItemDto.setAuthorId( itemInfo.getAuthorId() );
        favoriteItemDto.setTitle( itemInfo.getTitle() );
        if ( itemInfo.getAccess() != null ) {
            favoriteItemDto.setAccess( itemInfo.getAccess().name() );
        }
        favoriteItemDto.setViews( itemInfo.getViews() );
        favoriteItemDto.setLastStudyDate( itemInfo.getLastStudyDate() );
        favoriteItemDto.setCreateDate( itemInfo.getCreateDate() );
        favoriteItemDto.setEditDate( itemInfo.getEditDate() );

        return favoriteItemDto;
    }

    @Override
    public UserDto.FavoriteGroupContainsDoc of(FavoriteGroupInfo.ContainsDoc containsDoc) {
        if ( containsDoc == null ) {
            return null;
        }

        UserDto.FavoriteGroupContainsDoc favoriteGroupContainsDoc = new UserDto.FavoriteGroupContainsDoc();

        favoriteGroupContainsDoc.setGroupId( containsDoc.getGroupId() );
        favoriteGroupContainsDoc.setGroupName( containsDoc.getGroupName() );
        favoriteGroupContainsDoc.setUserId( containsDoc.getUserId() );
        favoriteGroupContainsDoc.setDocToken( containsDoc.getDocToken() );
        favoriteGroupContainsDoc.setResult( containsDoc.isResult() );

        return favoriteGroupContainsDoc;
    }

    @Override
    public UserDto.FavoriteGroupWithItemDto of(FavoriteGroupInfo.GroupWithItemInfo groupWithItemInfo) {
        if ( groupWithItemInfo == null ) {
            return null;
        }

        UserDto.FavoriteGroupWithItemDto favoriteGroupWithItemDto = new UserDto.FavoriteGroupWithItemDto();

        favoriteGroupWithItemDto.setGroupId( groupWithItemInfo.getGroupId() );
        favoriteGroupWithItemDto.setGroupName( groupWithItemInfo.getGroupName() );
        favoriteGroupWithItemDto.setUserId( groupWithItemInfo.getUserId() );
        favoriteGroupWithItemDto.setItems( itemInfoListToFavoriteItemDtoList( groupWithItemInfo.getItems() ) );

        return favoriteGroupWithItemDto;
    }

    @Override
    public UserDto.FavoriteGroupDto of(FavoriteGroupInfo.GroupInfo groupInfo) {
        if ( groupInfo == null ) {
            return null;
        }

        UserDto.FavoriteGroupDto favoriteGroupDto = new UserDto.FavoriteGroupDto();

        favoriteGroupDto.setGroupId( groupInfo.getGroupId() );
        favoriteGroupDto.setGroupName( groupInfo.getGroupName() );
        favoriteGroupDto.setUserId( groupInfo.getUserId() );
        favoriteGroupDto.setLastEdited( groupInfo.getLastEdited() );
        favoriteGroupDto.setCreated( groupInfo.getCreated() );

        return favoriteGroupDto;
    }

    @Override
    public TypingDocDto.History of(DocReviewHistoryInfo historyInfo) {
        if ( historyInfo == null ) {
            return null;
        }

        TypingDocDto.History history = new TypingDocDto.History();

        history.setReviewAt( historyInfo.getReviewAt() );
        history.setDocToken( historyInfo.getDocToken() );

        return history;
    }

    protected byte[] byteArrayTobyteArray(Byte[] byteArray) {
        if ( byteArray == null ) {
            return null;
        }

        byte[] byteTmp = new byte[byteArray.length];
        int i = 0;
        for ( Byte byte1 : byteArray ) {
            byteTmp[i] = byte1;
            i++;
        }

        return byteTmp;
    }

    protected List<UserDto.FavoriteItemDto> itemInfoListToFavoriteItemDtoList(List<FavoriteGroupInfo.ItemInfo> list) {
        if ( list == null ) {
            return null;
        }

        List<UserDto.FavoriteItemDto> list1 = new ArrayList<UserDto.FavoriteItemDto>( list.size() );
        for ( FavoriteGroupInfo.ItemInfo itemInfo : list ) {
            list1.add( of( itemInfo ) );
        }

        return list1;
    }
}
