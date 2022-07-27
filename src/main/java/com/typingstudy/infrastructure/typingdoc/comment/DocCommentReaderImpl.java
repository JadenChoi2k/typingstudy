package com.typingstudy.infrastructure.typingdoc.comment;

import com.typingstudy.common.exception.EntityNotFoundException;
import com.typingstudy.domain.typingdoc.comment.DocComment;
import com.typingstudy.domain.typingdoc.comment.DocCommentReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class DocCommentReaderImpl implements DocCommentReader {
    private final DocCommentJpaRepository commentRepository;

    @Override
    public List<DocComment> findAll(String docToken) {
        return commentRepository.findAllByDocToken(docToken);
    }

    @Override
    public DocComment findById(Long id) {
        return commentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("객체를 찾을 수 없습니다."));
    }
}
