package com.typingstudy.infrastructure.typingdoc.comment;

import com.typingstudy.domain.typingdoc.comment.DocComment;
import com.typingstudy.domain.typingdoc.comment.DocCommentStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityNotFoundException;

@Slf4j
@Repository
@RequiredArgsConstructor
public class DocCommentStoreImpl implements DocCommentStore {
    private final DocCommentJpaRepository commentRepository;

    @Override
    public DocComment store(DocComment comment) {
        return commentRepository.save(comment);
    }

    @Override
    public void removeComment(Long commentId) {
        commentRepository.delete(
                commentRepository.findById(commentId).orElseThrow(EntityNotFoundException::new)
        );
    }
}
