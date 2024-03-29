package com.typingstudy.infrastructure.typingdoc;

import com.typingstudy.domain.typingdoc.TypingDoc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TypingDocJpaRepository extends JpaRepository<TypingDoc, Long> {
    Page<TypingDoc> findAllByAuthorId(Long authorId, PageRequest pageRequest);

    Optional<TypingDoc> findByDocToken(String docToken);
}
