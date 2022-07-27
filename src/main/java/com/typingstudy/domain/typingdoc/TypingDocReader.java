package com.typingstudy.domain.typingdoc;

import java.util.List;

public interface TypingDocReader {
    /**
     *
     * @param userId: requested user
     * @param page: starts from 0
     * @param sortBy: title, createdAt, editedAt
     * @param direction: asc, desc
     * @return typing doc list with length of 20
     */
    List<TypingDoc> findAllWithUser(Long userId, int page, String sortBy, String direction);

    List<TypingDoc> findAllByIdList(List<Long> idList);

    List<TypingDoc> findAllByTokenList(List<String> tokenList);

    TypingDoc findByToken(String token);
}
