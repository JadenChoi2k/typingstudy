package com.typingstudy.infrastructure.typingdoc.history;

import com.typingstudy.domain.typingdoc.TypingDoc;
import com.typingstudy.domain.typingdoc.history.DocReviewRecommender;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
     * @param userId
     * @param page
     * @return
     */
    @Override
    public List<Pair<TypingDoc, Integer>> recommend(Long userId, int page) {
        List<Object[]> resultList = em.createQuery("select" +
                        " doc, (select count(hist) from DocReviewHistory hist where hist.doc.docToken = doc.docToken) as _count" +
                        " from TypingDoc doc" +
                        " where doc.authorId = :userId and (" +
                        " (_count = 0 and FUNCTION('datediff', 'day', doc.editedAt, current_timestamp) < 1)" +
                        " or (_count = 1 and FUNCTION('datediff', 'day', doc.editedAt, current_timestamp) < 3)" +
                        " or (_count = 2 and FUNCTION('datediff', 'day', doc.editedAt, current_timestamp) < 10)" +
                        " or (_count = 3 and FUNCTION('datediff', 'day', doc.editedAt, current_timestamp) < 33)" +
                        ")")
                .setParameter("userId", userId)
                .setMaxResults(20)
                .setFirstResult(page * 20)
                .getResultList();
        List<Pair<TypingDoc, Integer>> returnList = new ArrayList<>();
        for (Object[] row: resultList) {
            TypingDoc doc = (TypingDoc) row[0];
            Integer count = (Integer) row[1];
            returnList.add(Pair.of(doc, count));
        }
        return returnList;
    }
}
