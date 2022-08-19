package com.typingstudy.domain.typingdoc.history;

import com.typingstudy.domain.typingdoc.TypingDoc;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.util.List;

public interface DocReviewRecommender {
    List<TypingDoc> recommend(Long userId, int page);
}
