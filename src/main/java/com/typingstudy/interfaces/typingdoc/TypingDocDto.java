package com.typingstudy.interfaces.typingdoc;

import com.typingstudy.domain.typingdoc.TypingDoc;
import com.typingstudy.domain.typingdoc.comment.DocCommentInfo;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;

public class TypingDocDto {

    /* 반환 데이터 클래스 */
    @Data
    public static class DocInfo {
        private String docToken;
        private Long authorId;
        private String title;
        private String content;
        private TypingDoc.Access access;
        private List<DocCommentInfo.Main> comments;
        private Integer views;
        private LocalDateTime lastStudyDate;
        private LocalDateTime createDate;
        private LocalDateTime editDate;
    }

    @Data
    public static class DocPageItem {
        private String docToken;
        private Long authorId;
        private String title;
        private TypingDoc.Access access;
        private Integer views;
        private LocalDateTime lastStudyDate;
        private LocalDateTime createDate;
        private LocalDateTime editDate;
    }

    @Data
    public static class History {
        private LocalDateTime reviewAt;
        private String docToken;
    }

    @Data
    public static class Comment {
        private Long id;
        private String docToken;
        private String content;
        private Long userId;
        private LocalDateTime editedAt;
    }

    /* 입력(request) 데이터 클래스 */
    @Slf4j
    @Data
    public static class CreateDoc {
        private Long authorId;
        @Size(min = 1, max = 50)
        private String title;
        @Size(min = 1, max = 4095)
        private String content;
        @NotNull
        private TypingDoc.Access access;
    }

    @Data
    public static class AddDocComment {
        private String docToken;
        private Long userId;
        @NotEmpty(message = "1자 이상 입력해주세요")
        @Max(value = 1023)
        private String content;
    }

    @Data
    public static class EditDocComment {
        private Long commentId;
        private Long userId;
        @NotEmpty(message = "1자 이상 입력해주세요")
        @Max(value = 1023)
        private String content;
    }

    @Data
    public static class RemoveDocComment {
        private Long commentId;
        private Long userId;
    }

    @Data
    public static class EditDoc {
        private Long authorId;
        @NotNull
        private String docToken;
        @Max(50)
        private String title;
        @Max(4095)
        private String content;
        private TypingDoc.Access access;
    }

    @Data
    public static class AddObject {
        private Long authorId;
        private String docToken;
        @NotEmpty
        private String fileName;
        @NotNull
        private byte[] data;
    }
}
