package com.typingstudy.infrastructure.typingdoc.comment;

import com.typingstudy.common.exception.EntityNotFoundException;
import com.typingstudy.domain.typingdoc.comment.DocComment;
import com.typingstudy.domain.typingdoc.comment.DocCommentReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class DocCommentReaderImpl implements DocCommentReader {
    private final DocCommentJpaRepository commentRepository;
    private final EntityManager em;

    @Override
    public List<DocComment> findAll(String docToken) {
        return commentRepository.findAllByDocToken(docToken);
    }

    @Override
    public DocComment findById(Long id) {
        return commentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("객체를 찾을 수 없습니다."));
    }

    @Override
    public List<DocComment> findAllRelated(Long userId, int page) {
        return em.createQuery("select c from DocComment c" +
                " where c.doc.authorId = :userId or c.userId = :userId" +
                        " order by c.editedAt desc", DocComment.class)
                .setParameter("userId", userId)
                .setMaxResults(20)
                .setFirstResult(page * 20)
                .getResultList();
//        return commentRepository.findAllRelated(userId, page);
    }
}
