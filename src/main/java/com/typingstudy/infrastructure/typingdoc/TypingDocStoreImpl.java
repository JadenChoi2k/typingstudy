package com.typingstudy.infrastructure.typingdoc;

import com.typingstudy.common.exception.AlreadyExistException;
import com.typingstudy.domain.typingdoc.TypingDoc;
import com.typingstudy.domain.typingdoc.TypingDocStore;
import com.typingstudy.domain.typingdoc.history.DocReviewHistory;
import com.typingstudy.domain.typingdoc.object.DocObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;

@Slf4j
@Component
@RequiredArgsConstructor
public class TypingDocStoreImpl implements TypingDocStore {
    private final TypingDocRepository docRepository;
    private final EntityManager em;

    @Override
    public TypingDoc store(TypingDoc doc) {
        return docRepository.save(doc);
    }

    @Override
    public DocReviewHistory store(DocReviewHistory docReviewHistory) {
        em.persist(docReviewHistory);
        return docReviewHistory;
    }

    @Override
    public DocObject store(DocObject docObject) {
        em.createQuery("select o from DocObject o" +
                        " where o.doc.id = :docId and o.fileName = :fileName", DocObject.class)
                .setParameter("docId", docObject.getDoc().getId())
                .setParameter("fileName", docObject.getFileName())
                .setMaxResults(1)
                .getResultList().stream()
                .findFirst()
                .ifPresent(o -> {
                    throw new AlreadyExistException("이미 존재하는 파일 이름입니다");
                });
        em.persist(docObject);
        return docObject;
    }

    @Override
    public void remove(TypingDoc doc) {
        em.remove(doc);
    }

    @Override
    public void remove(DocObject docObject) {
        em.remove(docObject);
    }
}
