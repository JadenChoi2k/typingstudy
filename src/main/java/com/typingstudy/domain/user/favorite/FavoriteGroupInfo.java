package com.typingstudy.domain.user.favorite;

import com.typingstudy.domain.typingdoc.TypingDoc;
import com.typingstudy.domain.typingdoc.TypingDocInfo;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

public class FavoriteGroupInfo {

    @Data
    public static class Main {
        private Long groupId;
        private String groupName;
        private List<ItemInfo> itemList;
    }

    @Data
    public static class GroupInfo {
        private Long groupId;
        private String groupName;

        public GroupInfo(FavoriteGroup favoriteGroup) {
            this.groupId = favoriteGroup.getId();
            this.groupName = favoriteGroup.getGroupName();
        }
    }

    @Data
    public static class ItemInfo {
        private Long itemId;
        private String docToken;
        private Long authorId;
        private String title;
        private TypingDoc.Access access;
        private Integer views;
        private LocalDateTime lastStudyDate;
        private LocalDateTime createDate;

        public ItemInfo(FavoriteItem item) {
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
        }
    }
}
