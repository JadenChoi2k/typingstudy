package com.typingstudy.domain.typingdoc.comment;

import com.typingstudy.domain.BaseTimeEntity;
import com.typingstudy.domain.typingdoc.TypingDoc;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

/**
 * 수정 사항이나 알게된 것 등을 적는다.
 * public 문서에는 작성자 외에도 접근할 수 있다.
 */
@Entity
@Getter
@NoArgsConstructor
public class DocComment extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "doc_content_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doc_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private TypingDoc doc;

    @Column(length = 1023)
    @Setter
    private String content;

    @Column(nullable = false)
    private Long userId;

    public DocComment(TypingDoc doc, String content, Long userId) {
        this.doc = doc;
        this.content = content;
        this.userId = userId;
    }
}
