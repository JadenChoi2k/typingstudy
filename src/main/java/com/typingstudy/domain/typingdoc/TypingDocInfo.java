package com.typingstudy.domain.typingdoc;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

public class TypingDocInfo {

    @Data
    public static class Main {
        private Long docId;
        private Long authorId;
        private String title;
        private String content;
        private TypingDoc.Access access;
        private Integer views;
        private LocalDateTime lastStudyDate;
        private LocalDateTime createDate;
    }

    @Data
    @Builder
    public static class PageItem {
        private Long docId;
        private Long authorId;
        private String title;
        private TypingDoc.Access access;
        private Integer views;
        private LocalDateTime lastStudyDate;
        private LocalDateTime createDate;

        public PageItem(TypingDoc doc) {
            this.docId = doc.getId();
            this.authorId = doc.getAuthorId();
            this.title = doc.getTitle();
            this.access = doc.getAccess();
            this.views = doc.getViews();
            this.lastStudyDate = doc.getLastStudyDate();
            this.createDate = doc.getCreatedAt();
        }
    }
}
