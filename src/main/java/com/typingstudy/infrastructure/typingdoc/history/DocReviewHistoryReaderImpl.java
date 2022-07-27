package com.typingstudy.infrastructure.typingdoc.history;

import com.typingstudy.domain.typingdoc.history.DocReviewHistory;
import com.typingstudy.domain.typingdoc.history.DocReviewHistoryReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DocReviewHistoryReaderImpl implements DocReviewHistoryReader {
    private final EntityManager em;

    @Override
    public List<DocReviewHistory> findAllByToken(String token) {
        return em.createQuery("select hist from DocReviewHistory hist" +
                        " where hist.doc.docToken = :docToken", DocReviewHistory.class)
                .setParameter("docToken", token)
                .getResultList();
    }

    @Override
    public List<DocReviewHistory> findAllByUserId(Long userId) {
        return em.createQuery("select hist from DocReviewHistory hist" +
                        " where hist.doc.authorId = :userId", DocReviewHistory.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    @Override
    public long countsByToken(String token) {
        return em.createQuery("select count(hist) from DocReviewHistory hist" +
                        " where hist.doc.docToken = :docToken", Long.class)
                .setParameter("docToken", token)
                .getSingleResult();
    }

    @Override
    public long countsByUserId(Long userId) {
        return em.createQuery("select count(hist) from DocReviewHistory hist" +
                        " where hist.doc.authorId = :userId", Long.class)
                .setParameter("userId", userId)
                .getSingleResult();
    }
}
