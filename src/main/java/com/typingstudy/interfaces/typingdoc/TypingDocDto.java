package com.typingstudy.interfaces.typingdoc;

import com.typingstudy.domain.typingdoc.TypingDoc;
import com.typingstudy.domain.typingdoc.comment.DocCommentInfo;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

public class TypingDocDto {

    /* 반환 데이터 클래스 */
    @Data
    @Builder
    public static class DocInfo {
        private String docToken;
        private Long authorId;
        private String title;
        private String content;
        private TypingDoc.Access access;
        private List<DocCommentInfo.Main> comments;
        private Integer views;
        private LocalDateTime lastStudyDate;
        private LocalDateTime editDate;
        private LocalDateTime createDate;
    }

    @Data
    @Builder
    public static class DocPageItem {
        private String docToken;
        private Long authorId;
        private String title;
        private TypingDoc.Access access;
        private Integer views;
        private LocalDateTime lastStudyDate;
        private LocalDateTime createDate;
    }

    @Data
    @Builder
    public static class History {
        private LocalDateTime reviewAt;
        private String docToken;
    }

    @Data
    @Builder
    public static class Comment {
        private Long id;
        private String docToken;
        private String content;
        private Long userId;
        private LocalDateTime editedAt;
    }

    /* 입력 데이터 클래스 */
    @Data
    @Builder
    public static class CreateDoc {
        private Long authorId;
        private String title;
        private String content;
        private TypingDoc.Access access;
    }

    @Data
    @Builder
    public static class EditDoc {

    }
}
