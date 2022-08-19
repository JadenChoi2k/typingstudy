package com.typingstudy.domain.typingdoc.history;

import java.util.List;

public interface DocReviewHistoryReader {
    List<DocReviewHistory> findAllByToken(String token, Integer page);

    List<DocReviewHistory> findAllByUserId(Long userId, int page);

    long countsByToken(String token);

    long countsByUserId(Long userId);
}
