package com.typingstudy.infrastructure.typingdoc;

import com.typingstudy.domain.typingdoc.TypingDoc;

import java.util.List;

public interface TypingDocRepository {
    List<TypingDoc> findAllWithAuthor(Long authorId, int page, String sortBy, String direction);

    List<TypingDoc> findAllByIdList(List<Long> idList);
}
