package com.typingstudy.domain.typingdoc;

import java.util.List;

public interface TypingDocReader {
    List<TypingDoc> findAllWithUser(Long userId, int page, String sortBy, String direction);

    List<TypingDoc> findAllByIdList(List<Long> idList);

    List<TypingDoc> findAllByTokenList(List<String> tokenList);

    TypingDoc findByToken(String token);
}
