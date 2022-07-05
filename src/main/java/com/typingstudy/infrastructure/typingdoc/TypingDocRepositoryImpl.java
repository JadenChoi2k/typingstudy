package com.typingstudy.infrastructure.typingdoc;

import com.typingstudy.domain.typingdoc.TypingDoc;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

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
}
