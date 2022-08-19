package com.typingstudy.domain.typingdoc.comment;

import java.util.List;

public interface DocCommentReader {
    List<DocComment> findAll(String docToken);

    DocComment findById(Long id);

    List<DocComment> findAllRelated(Long userId, int page);
}
