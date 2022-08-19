package com.typingstudy.domain.typingdoc.history;

import com.typingstudy.domain.BaseEntity;
import com.typingstudy.domain.typingdoc.TypingDoc;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

/**
 * doc을 복습한 정보를 얻기 위해 저장하는 테이블
 * 작성자만 접근할 수 있다.
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DocReviewHistory extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "doc_history_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doc_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private TypingDoc doc;

    public DocReviewHistory(TypingDoc doc) {
        this.doc = doc;
    }
}
