package com.typingstudy.domain.typingdoc;

import com.typingstudy.domain.typingdoc.comment.DocCommentInfo;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

public class TypingDocInfo {

    @Data
    @Builder
    public static class Main {
        private String docToken;
        private Long authorId;
        private String title;
        private String content;
        private TypingDoc.Access access;
        private List<DocCommentInfo.Main> comments;
        private Integer views;
        private LocalDateTime lastStudyDate;
        private LocalDateTime createDate;

        public static Main of(TypingDoc doc) {
            return Main.builder()
                    .docToken(doc.getDocToken())
                    .authorId(doc.getAuthorId())
                    .title(doc.getTitle())
                    .content(doc.getContent())
                    .access(doc.getAccess())
                    .views(doc.getViews())
                    .lastStudyDate(doc.getLastStudyDate())
                    .createDate(doc.getCreatedAt())
                    .build();
        }
    }

    @Data
    public static class PageItem {
        private String docToken;
        private Long authorId;
        private String title;
        private TypingDoc.Access access;
        private Integer views;
        private LocalDateTime lastStudyDate;
        private LocalDateTime createDate;

        public PageItem(TypingDoc doc) {
            this.docToken = doc.getDocToken();
            this.authorId = doc.getAuthorId();
            this.title = doc.getTitle();
            this.access = doc.getAccess();
            this.views = doc.getViews();
            this.lastStudyDate = doc.getLastStudyDate();
            this.createDate = doc.getCreatedAt();
        }
    }
}
