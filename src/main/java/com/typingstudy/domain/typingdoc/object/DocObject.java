package com.typingstudy.domain.typingdoc.object;

import com.typingstudy.domain.BaseTimeEntity;
import com.typingstudy.domain.typingdoc.TypingDoc;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.nio.file.Files;

/**
 * image, 동영상 등 문서에 넣는 오브젝트 파일
 * .../{docId}/obj/{objectId} 로 접근한다.
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DocObject extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "doc_object_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "doc_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private TypingDoc doc;

    @Column(nullable = false)
    private String fileName;

    private byte[] data;

    public DocObject(TypingDoc doc, String fileName, byte[] data) {
        this.doc = doc;
        this.fileName = fileName;
        this.data = data;
    }
}
