package com.typingstudy.domain.typingdoc.comment;

import com.typingstudy.domain.typingdoc.TypingDoc;

public interface DocCommentStore {
    DocComment store(DocComment comment);

    void removeComment(Long commentId);
}
