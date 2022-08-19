package com.typingstudy.domain.user.favorite;

import com.typingstudy.domain.typingdoc.TypingDoc;
import com.typingstudy.domain.typingdoc.TypingDocInfo;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

public class FavoriteGroupInfo {

    // 요청한 docToken에 대하여 해당 그룹이 해당 doc을 아이템으로 가지고 있는지 여부를 알려주는 객체
    @Data
    @Builder
    public static class ContainsDoc {
        private Long groupId;
        private String groupName;
        private Long userId;
        private String docToken;
        private boolean result;
    }

    @Data
    public static class Main {
        private Long groupId;
        private String groupName;
        private Long userId;
        private List<ItemInfo> itemList;
    }

    @Data
    public static class GroupInfo {
        private Long groupId;
        private String groupName;
        private Long userId;
        private LocalDateTime lastEdited;
        private LocalDateTime created;

        public GroupInfo(FavoriteGroup favoriteGroup) {
            this.groupId = favoriteGroup.getId();
            this.groupName = favoriteGroup.getGroupName();
            this.userId = favoriteGroup.getUser().getId();
            this.lastEdited = favoriteGroup.getEditedAt();
            this.created = favoriteGroup.getCreatedAt();
        }
    }

    @Data
    public static class GroupWithItemInfo {
        private Long groupId;
        private String groupName;
        private Long userId;
        private List<ItemInfo> items;

        public GroupWithItemInfo(FavoriteGroup favoriteGroup) {
            this.groupId = favoriteGroup.getId();
            this.groupName = favoriteGroup.getGroupName();
            this.userId = favoriteGroup.getUser().getId();
        }
    }

    @Data
    public static class ItemInfo {
        private Long itemId;
        private Long groupId;
        private String docToken;
        private Long authorId;
        private String title;
        private TypingDoc.Access access;
        private Integer views;
        private LocalDateTime lastStudyDate;
        private LocalDateTime createDate;
        private LocalDateTime editDate;

        public ItemInfo(FavoriteItem item) {
            this.groupId = item.getGroup().getId();
            this.itemId = item.getId();
            this.docToken = item.getDocToken();
        }

        public void setDocInfo(TypingDoc doc) {
            this.authorId = doc.getAuthorId();
            this.title = doc.getTitle();
            this.access = doc.getAccess();
            this.views = doc.getViews();
            this.lastStudyDate = doc.getLastStudyDate();
            this.createDate = doc.getCreatedAt();
            this.editDate = doc.getEditedAt();
        }
    }
}
