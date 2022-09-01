package com.typingstudy.infrastructure.typingdoc;

import com.typingstudy.common.exception.EntityNotFoundException;
import com.typingstudy.domain.typingdoc.TypingDoc;
import com.typingstudy.domain.typingdoc.object.DocObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.NoResultException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Repository
@RequiredArgsConstructor
public class TypingDocRepositoryImpl implements TypingDocRepository {
    private final TypingDocJpaRepository docRepository;
    private final EntityManager em;

    @Override
    public List<TypingDoc> findAllWithAuthor(Long authorId, int page, String sortBy, String direction) {
        return docRepository.findAllByAuthorId(
                authorId,
                PageRequest.of(
                        page,
                        20,
                        direction.equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC,
                        sortBy)).stream().toList();
    }

    @Override
    public List<TypingDoc> findAllByIdList(List<Long> idList) {
        return em.createQuery("select d from TypingDoc d " +
                        "where d.id in (:ids)", TypingDoc.class)
                .setParameter("ids",
                        idList.stream().map(id -> Long.toString(id)).collect(Collectors.joining(", ")))
                .getResultList();
    }

    @Override
    public List<TypingDoc> findAllByTokenList(List<String> tokenList) {
        return em.createQuery("select d from TypingDoc d " +
                        "where d.docToken in (:tokens)", TypingDoc.class)
                .setParameter("tokens", tokenList)
                .getResultList();
    }

    @Override
    public DocObject findDocObject(String docToken, String fileName) {
        try {
            return em.createQuery("select o from DocObject o" +
                            " where o.doc.docToken = :docToken and o.fileName = :fileName", DocObject.class)
                    .setParameter("docToken", docToken)
                    .setParameter("fileName", fileName)
                    .getSingleResult();
        } catch (NoResultException e) {
            throw new EntityNotFoundException("객체를 찾을 수 없습니다");
        }
    }

    @Override
    public TypingDoc findByToken(String token) {
        return docRepository.findByDocToken(token)
                .orElseThrow(() -> new EntityNotFoundException("문서를 찾을 수 없습니다"));
    }

    @Override
    public TypingDoc save(TypingDoc doc) {
        return docRepository.save(doc);
    }

    @Override
    public long countsByUserId(Long userId) {
        return em.createQuery("select count(doc) from TypingDoc doc where doc.authorId=:userId", Long.class)
                .setParameter("userId", userId)
                .getSingleResult();
    }

    @Override
    public boolean validatePrivate(String docToken, Long userId) {
        log.info("validate docToken={} userId={}", docToken, userId);
        boolean result = em.createQuery("select true from TypingDoc doc" +
                        " where doc.docToken = :docToken and (" +
                        "    doc.authorId = :userId" +
                        "    or doc.access = 'PUBLIC'" +
                        "    or doc.access = 'PROTECTED'" + // enum not work.
                        " )", Boolean.class)
                .setParameter("docToken", docToken)
                .setParameter("userId", userId)
                .getResultList().stream()
                .findFirst() // 만약 결과값이 없으면 private 문서에 접근하는 다른 유저
                .orElse(false);
        return result;
    }
}
