package com.typingstudy.domain.typingdoc;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.typingstudy.common.exception.InvalidParameterException;
import com.typingstudy.common.utils.TokenGenerator;
import com.typingstudy.domain.BaseEntity;
import com.typingstudy.domain.typingdoc.comment.DocComment;
import com.typingstudy.domain.typingdoc.history.DocReviewHistory;
import com.typingstudy.domain.typingdoc.object.DocObject;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class TypingDoc extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "doc_id")
    private Long id;

    @Column(unique = true, nullable = false)
    private String docToken;

    @Column(nullable = false)
    private Long authorId;

    @Column(length = 127, nullable = false)
    private String title;

    // 가장 처음 작성한 내용
    @Column(length = 4095, nullable = false)
    private String content;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Access access;

    @ColumnDefault(value = "0")
    private Integer views;

    @ColumnDefault(value = "0")
    private Integer reviewCount;

    private LocalDateTime lastStudyDate;

    private LocalDateTime editedAt;

    @Slf4j
    @Getter
    @RequiredArgsConstructor
    public enum Access {
        PUBLIC("공개"), PRIVATE("비공개"), PROTECTED("일부 공개");

        private final String description;

        @JsonCreator
        public static Access from(String s) {
            log.info("JsonCreator 동작함. s={}", s);
            return switch (s.toUpperCase()) {
                case "공개", "PUBLIC" -> Access.PUBLIC;
                case "비공개", "PRIVATE" -> Access.PRIVATE;
                case "일부 공개", "PROTECTED" -> Access.PROTECTED;
                default -> throw new InvalidParameterException("존재하지 않는 액세스 타입입니다: " + s);
            };
            //            return Access.valueOf(s.toUpperCase());
        }

//        @JsonValue
//        public String getValue() {
//            return this.description;
//        }
    }

    @Builder
    public TypingDoc(Long authorId, String title, String content, Access access) {
        this.authorId = authorId;
        this.docToken = TokenGenerator.generate();
        this.title = title;
        this.content = content;
        this.access = access;
        this.views = 0;
        this.reviewCount = 0;
    }

    // 조회수 증가.
    public void onView() {
        this.views++;
    }

    // 복습한 날짜 갱신. history 객체를 반환한다.
    public DocReviewHistory review() {
        this.lastStudyDate = LocalDateTime.now();
        this.reviewCount += 1;
        return new DocReviewHistory(this);
    }

    public boolean isEdited() {
        return this.getEditedAt().isAfter(this.getCreatedAt());
    }

    public void edit(String title, String content, Access access) {
        boolean changed = false;
        if (title != null) {
            this.title = title;
            changed = true;
        }
        if (content != null) {
            this.content = content;
            changed = true;
        }
        if (access != null) {
            this.access = access;
            changed = true;
        }
        if (changed) {
            this.editedAt = LocalDateTime.now();
        }
    }

    // 페이지 아이템 전용 내용 축약
    public String getShortContent() {
        return this.content.length() > 100 ? this.content.substring(0, 100) + "..." : this.content;
    }

    // 이 문서의 코멘트를 생성한다.
    public DocComment createComment(Long userId, String content) {
        return new DocComment(this, content, userId);
    }

    public DocObject createObject(String fileName, byte[] data) {
        return new DocObject(this, fileName, data);
    }
}
