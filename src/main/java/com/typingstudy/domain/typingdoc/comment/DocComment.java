package com.typingstudy.domain.typingdoc.comment;

import com.typingstudy.domain.BaseTimeEntity;
import com.typingstudy.domain.typingdoc.TypingDoc;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * 수정 사항이나 알게된 것 등을 적는다.
 * public 문서에는 작성자 외에도 접근할 수 있다.
 */
@Entity
@NoArgsConstructor
public class DocComment extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "doc_content_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "doc_id")
    private TypingDoc doc;

    @Column(length = 1023)
    private String content;

    @Column(nullable = false)
    private Long userId;

    public DocComment(TypingDoc doc, String content, Long userId) {
        this.doc = doc;
        this.content = content;
        this.userId = userId;
    }
}
