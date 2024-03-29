package com.typingstudy.infrastructure.typingdoc.comment;

import com.typingstudy.domain.typingdoc.comment.DocComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DocCommentJpaRepository extends JpaRepository<DocComment, Long> {
    @Query("select c from DocComment c where c.doc.docToken = :docToken")
    List<DocComment> findAllByDocToken(@Param("docToken") String docToken);

    @Query("select c from DocComment c where c.doc.authorId = :userId or c.userId = :userId")
    List<DocComment> findAllRelated(@Param("userId") Long userId);
}
