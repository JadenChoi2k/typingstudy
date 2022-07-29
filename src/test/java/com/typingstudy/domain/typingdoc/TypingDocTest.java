package com.typingstudy.domain.typingdoc;

import com.typingstudy.domain.typingdoc.comment.DocComment;
import com.typingstudy.domain.typingdoc.history.DocReviewHistory;
import com.typingstudy.domain.typingdoc.object.DocObject;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class TypingDocTest {
    @Test
    @DisplayName("생성")
    void create() {
        // given
        String content = "C".repeat(4000);
        // when
        TypingDoc typingDoc = TypingDoc.builder()
                .authorId(1L)
                .access(TypingDoc.Access.PUBLIC)
                .title("typing_doc")
                .content(content)
                .build();
        // then
        log.info("token={}", typingDoc.getDocToken());
        assertEquals(content, typingDoc.getContent());
    }

    // 정상작동한다. 검증은 인터페이스 계층에서 하기 때문.
    // 만약 이 코드가 실행되고 db에 접근하면, db 제약 조건때문에 저장되지 않는다.
    @Test
    @DisplayName("4096자 이상 생성")
    void create_over_4096() {
        // given
        String content = "C".repeat(5000);
        // when
        TypingDoc typingDoc = TypingDoc.builder()
                .authorId(1L)
                .access(TypingDoc.Access.PUBLIC)
                .title("typing_doc")
                .content(content)
                .build();
        // then
        log.info("token={}", typingDoc.getDocToken());
        assertEquals(content, typingDoc.getContent());
    }

    @Test
    @DisplayName("private 문서 생성")
    void create_private() {
        // given
        String content = "C".repeat(4000);
        // when
        TypingDoc typingDoc = TypingDoc.builder()
                .authorId(1L)
                .access(TypingDoc.Access.PRIVATE)
                .title("typing_doc")
                .content(content)
                .build();
        // then
        assertEquals(typingDoc.getAccess(), TypingDoc.Access.PRIVATE);
    }

    @Test
    @DisplayName("protected 문서 생성")
    void create_protected() {
        // given
        String content = "C".repeat(4000);
        // when
        TypingDoc typingDoc = TypingDoc.builder()
                .authorId(1L)
                .access(TypingDoc.Access.PROTECTED)
                .title("typing_doc")
                .content(content)
                .build();
        String givenToken = typingDoc.getDocToken();
        // then
        assertEquals(typingDoc.getAccess(), TypingDoc.Access.PROTECTED);
        assertEquals(givenToken, typingDoc.getDocToken());
    }

    @Test
    @DisplayName("복습")
    void review() throws InterruptedException {
        // given
        String content = "C".repeat(4000);
        TypingDoc typingDoc = TypingDoc.builder()
                .authorId(1L)
                .access(TypingDoc.Access.PROTECTED)
                .title("typing_doc")
                .content(content)
                .build();
        // when
        DocReviewHistory reviewHistory1 = typingDoc.review();
        LocalDateTime lastStudyDate1 = typingDoc.getLastStudyDate();
        Thread.sleep(1000);
        DocReviewHistory reviewHistory2 = typingDoc.review();
        // then
        assertEquals(reviewHistory1.getDoc(), typingDoc);
        assertEquals(reviewHistory2.getDoc(), typingDoc);
        assertNotEquals(lastStudyDate1, typingDoc.getLastStudyDate());
    }

    @Test
    @DisplayName("조회수 상승")
    void view() {
        // given
        String content = "C".repeat(4000);
        int viewCount = 5;
        TypingDoc typingDoc = TypingDoc.builder()
                .authorId(1L)
                .access(TypingDoc.Access.PROTECTED)
                .title("typing_doc")
                .content(content)
                .build();
        // when
        for (int i = 0; i < viewCount; i++) {
            typingDoc.onView();
        }
        // then
        assertEquals(typingDoc.getViews(), viewCount);
    }

    @Test
    @DisplayName("코멘트 생성")
    void create_comment() {
        // given
        String content = "C".repeat(4000);
        Long userId = 1L;
        String comment = "M".repeat(1000);
        TypingDoc typingDoc = TypingDoc.builder()
                .authorId(userId)
                .access(TypingDoc.Access.PROTECTED)
                .title("typing_doc")
                .content(content)
                .build();
        // when
        DocComment docComment = typingDoc.createComment(userId, comment);
        // then
        assertEquals(comment, docComment.getContent());
        assertEquals(typingDoc, docComment.getDoc());
        assertEquals(userId, docComment.getUserId());
    }

    @Test
    @DisplayName("1023자 이상 코멘트 생성")
    void create_comment_over1023() {
        // given
        String content = "C".repeat(4000);
        Long userId = 1L;
        String comment = "M".repeat(2000);
        TypingDoc typingDoc = TypingDoc.builder()
                .authorId(userId)
                .access(TypingDoc.Access.PROTECTED)
                .title("typing_doc")
                .content(content)
                .build();
        // when
        DocComment docComment = typingDoc.createComment(userId, comment);
        // then
        assertEquals(comment, docComment.getContent());
        assertEquals(typingDoc, docComment.getDoc());
        assertEquals(userId, docComment.getUserId());
    }

    @Test
    @DisplayName("문서 오브젝트 생성")
    void create_object() {
        // given
        String content = "C".repeat(4000);
        Long userId = 1L;
        TypingDoc typingDoc = TypingDoc.builder()
                .authorId(userId)
                .access(TypingDoc.Access.PROTECTED)
                .title("typing_doc")
                .content(content)
                .build();
        // when
        byte[] data = "my data".getBytes();
        DocObject docObject = typingDoc.createObject("file.obj", data);
        // then
        assertEquals(docObject.getDoc(), typingDoc);
        assertEquals(docObject.getData(), data);
    }
}