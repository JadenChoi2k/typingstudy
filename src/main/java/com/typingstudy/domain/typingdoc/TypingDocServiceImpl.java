package com.typingstudy.domain.typingdoc;

import com.typingstudy.common.exception.EntityNotFoundException;
import com.typingstudy.common.exception.InvalidAccessException;
import com.typingstudy.domain.typingdoc.comment.DocComment;
import com.typingstudy.domain.typingdoc.comment.DocCommentInfo;
import com.typingstudy.domain.typingdoc.comment.DocCommentReader;
import com.typingstudy.domain.typingdoc.comment.DocCommentStore;
import com.typingstudy.domain.typingdoc.history.DocReviewHistory;
import com.typingstudy.domain.typingdoc.history.DocReviewHistoryInfo;
import com.typingstudy.domain.typingdoc.history.DocReviewHistoryReader;
import com.typingstudy.domain.typingdoc.history.DocReviewRecommender;
import com.typingstudy.domain.typingdoc.object.DocObjectInfo;
import com.typingstudy.domain.user.favorite.FavoriteStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.jni.Local;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class TypingDocServiceImpl implements TypingDocService {
    private final TypingDocReader typingDocReader;
    private final DocCommentReader docCommentReader;
    private final DocReviewHistoryReader historyReader;
    private final DocReviewRecommender reviewRecommender;
    private final TypingDocStore typingDocStore;
    private final DocCommentStore docCommentStore;
    private final FavoriteStore favoriteStore;

    void validateDoc(TypingDoc doc, Long authorId) {
        if (doc == null) {
            throw new EntityNotFoundException("문서를 찾을 수 없습니다");
        }
        if (!doc.getAuthorId().equals(authorId)) {
            throw new InvalidAccessException("잘못된 접근입니다");
        }
    }

    @Override
    public TypingDocInfo.Main createDoc(DocCommand.CreateRequest request) {
        TypingDoc doc = TypingDoc.builder()
                .authorId(request.getAuthorId())
                .title(request.getTitle())
                .content(request.getContent())
                .access(request.getAccess())
                .build();
        return TypingDocInfo.Main.of(typingDocStore.store(doc));
    }

    @Override
    public TypingDocInfo.Main editDoc(DocCommand.EditDocRequest request) {
        TypingDoc doc = typingDocReader.findByToken(request.getDocToken());
        validateDoc(doc, request.getAuthorId());
        doc.edit(
                request.getTitle(),
                request.getContent(),
                request.getAccess()
        );
        log.info("doc changed docToken={}, doc.editedAt={}", doc.getDocToken(), doc.getEditedAt());
        return TypingDocInfo.Main.of(doc);
    }

    @Override
    public void removeDoc(DocCommand.RemoveDocRequest request) {
        TypingDoc doc = typingDocReader.findByToken(request.getDocToken());
        validateDoc(doc, request.getAuthorId());
        typingDocStore.remove(doc);
        favoriteStore.removeAllItemsByDocToken(request.getDocToken());
    }

    @Override
    public boolean validatePrivate(String docToken, Long userId) {
        return typingDocReader.validatePrivate(docToken, userId);
    }

    @Override
    public void addDocObject(DocCommand.AddObjectRequest request) {
        TypingDoc doc = typingDocReader.findByToken(request.getDocToken());
        validateDoc(doc, request.getAuthorId());
        typingDocStore.store(doc.createObject(request.getFileName(), request.getData()));
    }

    @Override
    @Transactional(readOnly = true)
    public DocObjectInfo retrieveDocObject(DocCommand.RetrieveDocObjectRequest request) {
        return DocObjectInfo.of(
                typingDocReader.findDocObject(request.getDocToken(), request.getFileName())
        );
    }

    @Override
    public void removeDocObject(DocCommand.RemoveDocRequest request) {
        TypingDoc doc = typingDocReader.findByToken(request.getDocToken());
        validateDoc(doc, request.getAuthorId());
        typingDocStore.removeDocObject(request.getDocToken(), request.getFileName());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TypingDocInfo.PageItem> retrieveDocs(Long userId, int page, String sort, String direction) {
        return typingDocReader.findAllWithUser(userId, page, sort, direction)
                .stream()
                .map(TypingDocInfo.PageItem::new)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TypingDocInfo.PageItem> retrieveDocs(List<String> docTokenList) {
        return typingDocReader.findAllByTokenList(docTokenList).stream()
                .map(TypingDocInfo.PageItem::new)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public TypingDocInfo.Main retrieveDoc(String docToken) {
        TypingDocInfo.Main docInfo = TypingDocInfo.Main.of(typingDocReader.findByToken(docToken));
        long reviewCount = historyReader.countsByToken(docToken);
        docInfo.setReviewCount((int) reviewCount);
        log.info("[{}] review count: {}", docToken, reviewCount);
        docInfo.setComments(
                docCommentReader.findAll(docToken).stream()
                        .map(comment -> DocCommentInfo.Main.of(comment, docToken))
                        .collect(Collectors.toList())
        );
        return docInfo;
    }

    @Override
    public TypingDocInfo.Main viewDoc(String docToken) {
        TypingDoc doc = typingDocReader.findByToken(docToken);
        doc.onView();
        return retrieveDoc(docToken);
    }

    @Override
    public void reviewDoc(DocCommand.ReviewRequest request) {
        TypingDoc doc = typingDocReader.findByToken(request.getDocToken());
        validateDoc(doc, request.getUserId());
        DocReviewHistory review = doc.review();
        typingDocStore.store(review);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DocReviewHistoryInfo> reviewHistoryByToken(String docToken, Integer page) {
        return historyReader.findAllByToken(docToken, page).stream()
                .map(DocReviewHistoryInfo::of)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DocReviewHistoryInfo> reviewHistoryByUserId(Long userId, int page) {
        return historyReader.findAllByUserId(userId, page).stream()
                .map(DocReviewHistoryInfo::of)
                .collect(Collectors.toList());
    }

    @Override
    public List<TypingDocInfo.PageItem> recommendedReview(Long userId, int page) {
        return reviewRecommender.recommend(userId, page).stream()
                .map(TypingDocInfo.PageItem::new)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public long reviewCountByToken(String docToken) {
        return historyReader.countsByToken(docToken);
    }

    @Override
    @Transactional(readOnly = true)
    public long reviewCountByUserId(Long userId) {
        return historyReader.countsByUserId(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DocCommentInfo.Main> retrieveComments(String docToken) {
        return docCommentReader.findAll(docToken).stream()
                .map(comment -> DocCommentInfo.Main.of(comment, docToken))
                .toList();
    }

    @Override
    public List<DocCommentInfo.Main> retrieveRelatedComments(Long userId, int page) {
        return docCommentReader.findAllRelated(userId, page).stream()
                .map(comment -> DocCommentInfo.Main.of(comment, comment.getDoc().getDocToken()))
                .toList();
    }

    @Override
    public DocCommentInfo.Main addComment(DocCommand.AddCommentRequest request) {
        TypingDoc doc = typingDocReader.findByToken(request.getDocToken());
        if (doc.getAccess() == TypingDoc.Access.PRIVATE &&
                !doc.getAuthorId().equals(request.getUserId())) {
            throw new InvalidAccessException("권한이 없습니다.");
        }
        DocComment comment = docCommentStore.store(doc.createComment(request.getUserId(), request.getContent()));
        log.info("코멘트 등록: {}", comment.getId());
        return DocCommentInfo.Main.of(comment);
    }

    @Override
    public DocCommentInfo.Main editComment(DocCommand.EditCommentRequest request) {
        DocComment comment = docCommentReader.findById(request.getCommentId());
        comment.setContent(request.getContent());
        // 최적화를 고려. 만약 of 메서드를 통해서 초기화한다면 lazyInitialization 발생.
        return DocCommentInfo.Main.of(comment, null);
    }

    @Override
    public void removeComment(DocCommand.RemoveCommentRequest request) {
        DocComment comment = docCommentReader.findById(request.getCommentId());
        // 만약 요청한 쪽이 작성자가 아니라면 오류 발생
        if (!comment.getUserId().equals(request.getUserId())) {
            throw new InvalidAccessException("잘못된 접근입니다.");
        }
        docCommentStore.removeComment(request.getCommentId());
    }
}
