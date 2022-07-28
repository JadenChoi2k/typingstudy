package com.typingstudy.domain.typingdoc;

import com.typingstudy.common.exception.EntityNotFoundException;
import com.typingstudy.common.exception.InvalidAccessException;
import com.typingstudy.domain.typingdoc.comment.DocCommentInfo;
import com.typingstudy.domain.typingdoc.history.DocReviewHistoryInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
@Transactional // 롤백을 위한 Transactional
@SpringBootTest
public class TypingDocServiceTest {
    @Autowired
    TypingDocService docService;

    @BeforeEach
    void before_each() {
        // authorId: 1
        // save 100 documents
        // replace createDoc test
        for (int i = 0; i < 100; i++) {
            docService.createDoc(
                    DocCommand.CreateRequest.builder()
                            .authorId(1L)
                            .title("doc " + i)
                            .content("doc " + i + " content")
                            .access(TypingDoc.Access.PUBLIC)
                            .build()
            );
        }
    }

    // doc 생성(저장)
    TypingDocInfo.Main createDoc() {
        Long userId = 123L;
        String title = "doc title";
        String content = "doc content";
        DocCommand.CreateRequest request = DocCommand.CreateRequest.builder()
                .authorId(userId)
                .title(title)
                .content(content)
                .access(TypingDoc.Access.PUBLIC)
                .build();
        return docService.createDoc(request);
    }

    @Test
    @DisplayName("문서 생성")
    void create_doc() {
        // given
        Long userId = 123L;
        String title = "doc title";
        String content = "doc content";
        DocCommand.CreateRequest request = DocCommand.CreateRequest.builder()
                .authorId(userId)
                .title(title)
                .content(content)
                .access(TypingDoc.Access.PUBLIC)
                .build();
        // when
        TypingDocInfo.Main doc = docService.createDoc(request);
        // then
        assertThat(doc.getAuthorId()).isEqualTo(userId);
        assertThat(doc.getTitle()).isEqualTo(title);
        assertThat(doc.getContent()).isEqualTo(content);
        assertThat(doc.getDocToken()).isNotNull();
        assertThat(doc.getAccess()).isEqualTo(TypingDoc.Access.PUBLIC);
    }

    @Test
    @DisplayName("문서 수정")
    void edit_doc() {
        // given
        String title = "edit title";
        String content = "edit content";
        TypingDoc.Access access = TypingDoc.Access.PRIVATE;
        TypingDocInfo.Main docInfo = createDoc();
        // when
        TypingDocInfo.Main editDocInfo = docService.editDoc(DocCommand.EditDocRequest.builder()
                .docToken(docInfo.getDocToken())
                .title(title)
                .content(content)
                .access(access)
                .authorId(docInfo.getAuthorId())
                .build());
        // then
        assertThat(editDocInfo.getDocToken()).isEqualTo(docInfo.getDocToken());
        assertThat(editDocInfo.getTitle()).isEqualTo(title);
        assertThat(editDocInfo.getContent()).isEqualTo(content);
        assertThat(editDocInfo.getAccess()).isEqualTo(access);
    }

    @Test
    @DisplayName("문서 삭제")
    void remove_doc() {
        // given
        TypingDocInfo.Main docInfo = createDoc();
        // when
        docService.removeDoc(DocCommand.RemoveDocRequest.builder()
                .docToken(docInfo.getDocToken())
                .authorId(docInfo.getAuthorId())
                .build());
        // then
        assertThatThrownBy(() -> docService.retrieveDoc(docInfo.getDocToken()))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("문서 페이징")
    void retrieve_docs() {
        // given
        Long userId = 1L;
        // when
        List<TypingDocInfo.PageItem> pageItems = docService.retrieveDocs(userId, 0, "createdAt", "desc");
        // then
        assertThat(pageItems).hasSize(20);
        assertThat(pageItems).allMatch(item -> item.getAuthorId().equals(userId));
    }

    @Test
    @DisplayName("토큰 리스트로 요청")
    void retrieve_docs2() {
        // given
        Long userId = 1L;
        List<TypingDocInfo.PageItem> pageItems = docService.retrieveDocs(userId, 0, "createdAt", "desc");
        // when
        List<TypingDocInfo.PageItem> pageItems2 = docService.retrieveDocs(
                pageItems.stream()
                        .map(TypingDocInfo.PageItem::getDocToken)
                        .collect(Collectors.toList())
        );
        // then
        assertThat(pageItems2).hasSize(pageItems.size());
        assertThat(pageItems2).allMatch(item -> item.getAuthorId().equals(userId));
    }

    @Test
    @DisplayName("토큰으로 요청(문서 컨텐츠 확인)")
    void retrieve_doc() {
        // given
        Long userId = 1L;
        List<TypingDocInfo.PageItem> pageItems = docService.retrieveDocs(userId, 0, "createdAt", "desc");
        // when
        Random random = new Random();
        String docToken = pageItems.get(random.nextInt(pageItems.size())).getDocToken();
        TypingDocInfo.Main docInfo = docService.retrieveDoc(docToken);
        // then
        assertThat(docInfo.getContent()).isNotNull();
        assertThat(docInfo.getDocToken()).isEqualTo(docToken);
        assertThat(docInfo.getAuthorId()).isEqualTo(userId);
    }

    @Test
    @DisplayName("토큰으로 요청: not found")
    void doc_not_found() {
        // given
        String docToken = "I am not a token";
        // when, then
        assertThatThrownBy(() -> docService.retrieveDoc(docToken))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("토큰으로 요청: 코멘트 확인")
    void doc_with_comments() {
        // given
        TypingDocInfo.Main doc = createDoc();
        // when
        String comment = "comment";
        DocCommentInfo.Main commentInfo = docService.addComment(DocCommand.AddCommentRequest.builder()
                .userId(doc.getAuthorId())
                .docToken(doc.getDocToken())
                .content(comment)
                .build());
        TypingDocInfo.Main docWithComment = docService.retrieveDoc(doc.getDocToken());
        // then
        assertThat(docWithComment.getComments()).hasSize(1);
        assertThat(commentInfo.getDocToken()).isEqualTo(doc.getDocToken());
        assertThat(docWithComment.getComments().get(0).getContent()).isEqualTo(comment);
    }

    @Test
    @DisplayName("문서 복습")
    void review_doc() {
        // given
        Long userId = 1L;
        List<TypingDocInfo.PageItem> pageItems = docService.retrieveDocs(userId, 0, "createdAt", "desc");
        // when
        Random random = new Random();
        String docToken = pageItems.get(random.nextInt(pageItems.size())).getDocToken();
        docService.reviewDoc(DocCommand.ReviewRequest.builder()
                .userId(userId)
                .docToken(docToken)
                .build()
        );
    }

    @Test
    @DisplayName("토큰으로 복습 기록 확인 (각 한번씩 복습)")
    void review_history_by_token() {
        // given
        Long userId = 1L;
        List<TypingDocInfo.PageItem> pageItems = docService.retrieveDocs(userId, 0, "createdAt", "desc");
        // when
        pageItems.forEach(item -> docService.reviewDoc(DocCommand.ReviewRequest.builder()
                .userId(userId)
                .docToken(item.getDocToken())
                .build()));
        List<List<DocReviewHistoryInfo>> historyInfoList = pageItems.stream()
                .map(item -> docService.reviewHistoryByToken(item.getDocToken(), 0)).toList();
        // then
        assertThat(historyInfoList).hasSize(pageItems.size());
        assertThat(historyInfoList).allMatch(list -> list.size() == 1);
        historyInfoList.forEach(list -> assertThat(list.get(0).getReviewAt()).isNotNull());
    }

    @Test
    @DisplayName("토큰으로 복습 기록 확인 (한 문서 5번 복습)")
    void review_history_by_token2() {
        // given
        Long userId = 1L;
        int count = 5;
        List<TypingDocInfo.PageItem> pageItems = docService.retrieveDocs(userId, 0, "createdAt", "desc");
        Random random = new Random();
        String docToken = pageItems.get(random.nextInt(pageItems.size())).getDocToken();
        for (int i = 0; i < count; i++)
            docService.reviewDoc(DocCommand.ReviewRequest.builder()
                    .userId(userId)
                    .docToken(docToken)
                    .build()
            );
        // when
        List<DocReviewHistoryInfo> reviewHistory = docService.reviewHistoryByToken(docToken, 0);
        long reviewCount = docService.reviewCountByToken(docToken);
        // then
        assertThat(reviewCount).isEqualTo(count);
        assertThat(reviewHistory).hasSize(count);
        assertThat(reviewHistory).allMatch(hist -> hist.getReviewAt() != null);
    }

    @Test
    @DisplayName("유저 아이디로 복습 기록 확인")
    void review_history_by_userid() {
        // given
        Long userId = 1L;
        List<TypingDocInfo.PageItem> pageItems = docService.retrieveDocs(userId, 0, "createdAt", "desc");
        pageItems.forEach(item -> docService.reviewDoc(DocCommand.ReviewRequest.builder()
                .userId(userId)
                .docToken(item.getDocToken())
                .build()));
        List<List<DocReviewHistoryInfo>> historyInfoList = pageItems.stream()
                .map(item -> docService.reviewHistoryByToken(item.getDocToken(), 0)).toList();
        // when
        List<DocReviewHistoryInfo> history = docService.reviewHistoryByUserId(userId);
        long reviewCount = docService.reviewCountByUserId(userId);
        // then
        assertThat(reviewCount).isEqualTo(pageItems.size());
        assertThat(history)
                .hasSize(pageItems.size())
                .allMatch(hist -> hist.getDocToken() != null)
                .allMatch(hist -> hist.getReviewAt() != null);
    }

    @Test
    @DisplayName("코멘트 작성")
    void add_comment() {
        // given
        TypingDocInfo.Main doc = createDoc();
        // when
        String comment = "comment";
        DocCommentInfo.Main commentInfo = docService.addComment(DocCommand.AddCommentRequest.builder()
                .userId(doc.getAuthorId())
                .docToken(doc.getDocToken())
                .content(comment)
                .build());
        // then
        assertThat(commentInfo.getDocToken()).isEqualTo(doc.getDocToken());
        assertThat(commentInfo.getContent()).isEqualTo(comment);
    }

    @Test
    @DisplayName("코멘트 작성 실패 - 문서 못 찾음")
    void add_comment_fail() {
        // given
        TypingDocInfo.Main doc = createDoc();
        // when, then
        String comment = "comment";
        assertThatThrownBy(() -> docService.addComment(DocCommand.AddCommentRequest.builder()
                .userId(doc.getAuthorId())
                .docToken(doc.getDocToken() + " will not be found")
                .content(comment)
                .build())
        ).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("코멘트 수정")
    void edit_comment() throws Exception {
        // given
        TypingDocInfo.Main doc = createDoc();
        String comment = "comment";
        DocCommentInfo.Main commentInfo = docService.addComment(DocCommand.AddCommentRequest.builder()
                .userId(doc.getAuthorId())
                .docToken(doc.getDocToken())
                .content(comment)
                .build());
        // when
        String editComment = "edit comment";
        DocCommentInfo.Main editedCommentInfo = docService.editComment(DocCommand.EditCommentRequest.builder()
                .commentId(commentInfo.getId())
                .userId(doc.getAuthorId())
                .content(editComment)
                .build());
        // then
        assertThat(editedCommentInfo.getContent()).isEqualTo(editComment);
        assertThat(editedCommentInfo.getId()).isEqualTo(commentInfo.getId());
        assertThat(editedCommentInfo.getDocToken()).isNull();
    }

    @Test
    @DisplayName("코멘트 수정 실패 - 코멘트 못 찾음")
    void edit_comment_fail() {
        // given
        TypingDocInfo.Main doc = createDoc();
        String comment = "comment";
        DocCommentInfo.Main commentInfo = docService.addComment(DocCommand.AddCommentRequest.builder()
                .userId(doc.getAuthorId())
                .docToken(doc.getDocToken())
                .content(comment)
                .build());
        // when, then
        String editComment = "edit comment";
        assertThatThrownBy(() -> docService.editComment(DocCommand.EditCommentRequest.builder()
                .commentId(commentInfo.getId() + 123123123L)
                .userId(doc.getAuthorId())
                .content(editComment)
                .build())
        ).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("코멘트 삭제")
    void remove_comment() {
        // given
        TypingDocInfo.Main doc = createDoc();
        String comment = "comment";
        DocCommentInfo.Main commentInfo = docService.addComment(DocCommand.AddCommentRequest.builder()
                .userId(doc.getAuthorId())
                .docToken(doc.getDocToken())
                .content(comment)
                .build());
        // when
        docService.removeComment(DocCommand.RemoveCommentRequest.builder()
                .userId(doc.getAuthorId())
                .commentId(commentInfo.getId())
                .build());
        // then
        List<DocCommentInfo.Main> comments = docService.retrieveComments(doc.getDocToken());
        assertThat(comments).isEmpty();
    }

    @Test
    @DisplayName("코멘트 삭제 실패 - 코멘트 못 찾음")
    void remove_comment_fail1() {
        // given
        TypingDocInfo.Main doc = createDoc();
        String comment = "comment";
        DocCommentInfo.Main commentInfo = docService.addComment(DocCommand.AddCommentRequest.builder()
                .userId(doc.getAuthorId())
                .docToken(doc.getDocToken())
                .content(comment)
                .build());
        // when, then
        assertThatThrownBy(() -> docService.removeComment(DocCommand.RemoveCommentRequest.builder()
                .userId(doc.getAuthorId())
                .commentId(commentInfo.getId() + 123123123L)
                .build())
        ).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("코멘트 삭제 실패 - 유저 아이디 불일치")
    void remove_comment_fail2() {
        // given
        TypingDocInfo.Main doc = createDoc();
        String comment = "comment";
        DocCommentInfo.Main commentInfo = docService.addComment(DocCommand.AddCommentRequest.builder()
                .userId(doc.getAuthorId())
                .docToken(doc.getDocToken())
                .content(comment)
                .build());
        // when, then
        assertThatThrownBy(() -> docService.removeComment(DocCommand.RemoveCommentRequest.builder()
                .userId(doc.getAuthorId() + 123123123L)
                .commentId(commentInfo.getId())
                .build())
        ).isInstanceOf(InvalidAccessException.class);
    }
}
