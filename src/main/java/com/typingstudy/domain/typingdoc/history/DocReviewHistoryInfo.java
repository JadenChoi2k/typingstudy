package com.typingstudy.domain.typingdoc.history;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class DocReviewHistoryInfo {
    private LocalDateTime reviewAt;
    private String docToken;

    public static DocReviewHistoryInfo of(DocReviewHistory reviewHistory) {
        return DocReviewHistoryInfo.builder()
                .reviewAt(reviewHistory.getCreatedAt())
                .docToken(reviewHistory.getDoc().getDocToken())
                .build();
    }
}
