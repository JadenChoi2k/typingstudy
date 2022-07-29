package com.typingstudy.domain.typingdoc;

import lombok.Builder;
import lombok.Data;

public class DocCommand {

    @Data
    @Builder
    public static class CreateRequest {
        private Long authorId;
        private String title;
        private String content;
        private TypingDoc.Access access;
    }

    @Data
    @Builder
    public static class EditDocRequest {
        private Long authorId;
        private String docToken;
        private String title;
        private String content;
        private TypingDoc.Access access;
    }

    @Data
    @Builder
    public static class AddObjectRequest {
        private Long authorId;
        private String docToken;
        private String fileName;
        private byte[] data;
    }

    @Data
    @Builder
    public static class RemoveDocRequest {
        private Long authorId;
        private String docToken;
        private String fileName;
    }

    @Data
    @Builder
    public static class ReviewRequest {
        private Long userId;
        private String docToken;
    }

    // userId는 인터페이스 계층(Spring Security)에서 주입한다.
    @Data
    @Builder
    public static class AddCommentRequest {
        private String docToken;
        private Long userId;
        private String content;
    }

    // userId는 인터페이스 계층에서 주입하므로 유저 검증에 사용할 수 있다.
    @Data
    @Builder
    public static class EditCommentRequest {
        private Long commentId;
        private Long userId;
        private String content;
    }

    // userId는 인터페이스 계층에서 주입하므로 유저 검증에 사용할 수 있다.
    @Data
    @Builder
    public static class RemoveCommentRequest {
        private Long commentId;
        private Long userId;
    }

    @Data
    @Builder
    public class RetrieveDocObjectRequest {
        private String docToken;
        private String fileName;
    }
}
