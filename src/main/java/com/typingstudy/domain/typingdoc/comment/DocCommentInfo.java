package com.typingstudy.domain.typingdoc.comment;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

public class DocCommentInfo {

    @Data
    @Builder
    public static class Main {
        private Long id;
        private String docToken;
        private String content;
        private Long userId;
        private LocalDateTime editedAt;

        public static Main of(DocComment comment) {
            return of(comment, comment.getDoc().getDocToken());
        }

        public static Main of(DocComment comment, String docToken) {
            return Main.builder()
                    .id(comment.getId())
                    .docToken(docToken)
                    .content(comment.getContent())
                    .userId(comment.getUserId())
                    .editedAt(comment.getEditedAt())
                    .build();
        }
    }
}
