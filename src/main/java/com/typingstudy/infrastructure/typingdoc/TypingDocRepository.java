package com.typingstudy.infrastructure.typingdoc;

import com.typingstudy.domain.typingdoc.TypingDoc;
import com.typingstudy.domain.typingdoc.object.DocObject;

import java.util.List;

public interface TypingDocRepository {
    List<TypingDoc> findAllWithAuthor(Long authorId, int page, String sortBy, String direction);

    List<TypingDoc> findAllByIdList(List<Long> idList);

    List<TypingDoc> findAllByTokenList(List<String> tokenList);

    DocObject findDocObject(String docToken, String fileName);

    TypingDoc findByToken(String token);

    TypingDoc save(TypingDoc doc);

    boolean validatePrivate(String docToken, Long userId);
}
