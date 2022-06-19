package indie.typingstudy.domain.typingdoc;

import indie.typingstudy.domain.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

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

    private String docToken;

    @Column(nullable = false)
    private Long authorId;

    @Column(length = 50, nullable = false)
    private String title;

    @Column(length = 4095, nullable = false)
    private String content;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Access access;

    private Integer views;

    private LocalDateTime lastStudyDate;

    @RequiredArgsConstructor
    public enum Access {
        PUBLIC("공개"), PRIVATE("비공개"), PROTECTED("일부 공개");

        private final String description;
    }

    @Builder
    public TypingDoc(Long authorId, String title, String content, Access access) {
        this.authorId = authorId;
        this.title = title;
        this.content = content;
        this.access = access;
        this.views = 0;
    }

    // 조회수 증가.
    public void onView() {
        this.views++;
    }

    // 복습한 날짜 갱신
    public void onReview() {
        this.lastStudyDate = LocalDateTime.now();
    }
}
