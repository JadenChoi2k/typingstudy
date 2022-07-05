package com.typingstudy.domain.user.favorite;

import com.typingstudy.domain.typingdoc.TypingDocInfo;
import lombok.Data;

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
        private Long docId;
        private TypingDocInfo.PageItem docInfo;

        public ItemInfo(FavoriteItem item) {
            this.itemId = item.getId();
            this.docId = item.getDocId();
        }
    }
}
