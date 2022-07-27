package com.typingstudy.domain.typingdoc.history;

import java.util.List;

public interface DocReviewHistoryReader {
    List<DocReviewHistory> findAllByToken(String token);

    List<DocReviewHistory> findAllByUserId(Long userId);

    long countsByToken(String token);

    long countsByUserId(Long userId);
}
