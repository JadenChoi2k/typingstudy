package com.typingstudy.infrastructure.typingdoc.history;

import com.typingstudy.domain.typingdoc.TypingDoc;
import com.typingstudy.domain.typingdoc.history.DocReviewRecommender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class DocReviewRecommenderImpl implements DocReviewRecommender {
    private final EntityManager em;

    /**
     * user가 가진 문서 중에서 다음을 충족하는 문서를 작성날짜에 대해 내림차순으로 제시
     * 복습 횟수 0회 -> 작성날로부터 10분 ~ 1일
     * 복습 횟수 1회 -> 작성날로부터 1일 ~ 3일
     * 복습 횟수 2회 -> 작성날로부터 7일 ~ 10일
     * 복습 횟수 3회 -> 작성날로부터 30일 ~ 33일
     * 제약조건: db가 datediff 함수를 지원해야 함.
     *
     * @param userId
     * @param page
     * @return
     */
    @Override
    public List<TypingDoc> recommend(Long userId, int page) {
        return em.createQuery("select" +
                        " doc from TypingDoc doc" +
                        " where doc.authorId = :userId and (" +
                        " (doc.reviewCount = 0 and FUNCTION('datediff', 'day', doc.createdAt, current_timestamp) < 2)" +
                        " or (doc.reviewCount = 1 and FUNCTION('datediff', 'day', doc.createdAt, current_timestamp) < 4)" +
                        " or (doc.reviewCount = 2 and FUNCTION('datediff', 'day', doc.createdAt, current_timestamp) < 11)" +
                        " or (doc.reviewCount = 3 and FUNCTION('datediff', 'day', doc.createdAt, current_timestamp) < 34)" +
                        ") order by doc.createdAt asc", TypingDoc.class)
                .setParameter("userId", userId)
                .setMaxResults(20)
                .setFirstResult(page * 20)
                .getResultList();
    }
}
