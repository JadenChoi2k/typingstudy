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
        private List<DocCommentInfo.Main> comments; // set manually
        private Integer views;
        private Integer reviewCount;
        private LocalDateTime lastStudyDate;
        private LocalDateTime createDate;
        private LocalDateTime editDate;

        public static Main of(TypingDoc doc) {
            return Main.builder()
                    .docToken(doc.getDocToken())
                    .authorId(doc.getAuthorId())
                    .title(doc.getTitle())
                    .content(doc.getContent())
                    .access(doc.getAccess())
                    .views(doc.getViews())
                    .reviewCount(doc.getReviewCount())
                    .lastStudyDate(doc.getLastStudyDate())
                    .createDate(doc.getCreatedAt())
                    .editDate(doc.getEditedAt())
                    .build();
        }
    }

    @Data
    public static class PageItem {
        private String docToken;
        private Long authorId;
        private String title;
        private String content;
        private TypingDoc.Access access;
        private Integer views;
        private Integer reviewCount;
        private LocalDateTime lastStudyDate;
        private LocalDateTime editDate;
        private LocalDateTime createDate;

        public PageItem(TypingDoc doc) {
            this.docToken = doc.getDocToken();
            this.authorId = doc.getAuthorId();
            this.title = doc.getTitle();
            this.content = doc.getShortContent();
            this.access = doc.getAccess();
            this.views = doc.getViews();
            this.reviewCount = doc.getReviewCount();
            this.lastStudyDate = doc.getLastStudyDate();
            this.editDate = doc.getEditedAt();
            this.createDate = doc.getCreatedAt();
        }
    }
}
