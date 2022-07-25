package com.typingstudy.domain.typingdoc;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class TypingDocTest {

    @Test
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
}