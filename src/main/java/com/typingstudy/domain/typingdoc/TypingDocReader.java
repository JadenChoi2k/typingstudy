package com.typingstudy.domain.typingdoc;

import com.typingstudy.domain.typingdoc.object.DocObject;

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

    DocObject findDocObject(String docToken, String fileName);

    long countsByUserId(Long userId);

    boolean validatePrivate(String docToken, Long userId);
}
