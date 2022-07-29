package com.typingstudy.domain.typingdoc;

import com.typingstudy.domain.typingdoc.history.DocReviewHistory;
import com.typingstudy.domain.typingdoc.object.DocObject;

public interface TypingDocStore {
    TypingDoc store(TypingDoc doc);

    DocReviewHistory store(DocReviewHistory docReviewHistory);

    DocObject store(DocObject docObject);

    void remove(TypingDoc doc);

    void remove(DocObject docObject);
}
