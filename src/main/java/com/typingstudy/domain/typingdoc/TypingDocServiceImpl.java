package com.typingstudy.domain.typingdoc;

import com.typingstudy.common.exception.InvalidAccessException;
import com.typingstudy.domain.typingdoc.comment.DocComment;
import com.typingstudy.domain.typingdoc.comment.DocCommentInfo;
import com.typingstudy.domain.typingdoc.comment.DocCommentReader;
import com.typingstudy.domain.typingdoc.comment.DocCommentStore;
import com.typingstudy.domain.typingdoc.history.DocReviewHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class TypingDocServiceImpl implements TypingDocService {
    private final TypingDocReader typingDocReader;
    private final DocCommentReader docCommentReader;
    private final TypingDocStore typingDocStore;
    private final DocCommentStore docCommentStore;

    @Override
    @Transactional(readOnly = true)
    public List<TypingDocInfo.PageItem> retrieveDocs(Long userId, int page, String sort, String direction) {
        return typingDocReader.findAllWithUser(userId, page, sort, direction)
                .stream()
                .map(TypingDocInfo.PageItem::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<TypingDocInfo.PageItem> retrieveDocs(List<String> docTokenList) {
        return typingDocReader.findAllByTokenList(docTokenList).stream()
                .map(TypingDocInfo.PageItem::new)
                .collect(Collectors.toList());
    }

    @Override
    public TypingDocInfo.Main retrieveDoc(String docToken) {
        TypingDocInfo.Main docInfo = TypingDocInfo.Main.of(typingDocReader.findByToken(docToken));
        docInfo.setComments(
                docCommentReader.findAll(docToken).stream()
                        .map(comment -> DocCommentInfo.Main.of(comment, docToken))
                        .collect(Collectors.toList())
        );
        return docInfo;
    }

    @Override
    public void reviewDoc(DocCommand.ReviewRequest request) {
        TypingDoc doc = typingDocReader.findByToken(request.getDocToken());
        if (!doc.getAuthorId().equals(request.getUserId())) {
            throw new InvalidAccessException("잘못된 접근입니다.");
        }
        DocReviewHistory review = doc.review();
        typingDocStore.store(review);
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
    public DocCommentInfo.Main addComment(DocCommand.AddCommentRequest request) {
        TypingDoc doc = typingDocReader.findByToken(request.getDocToken());
        DocComment comment = docCommentStore.store(doc.createComment(request.getUserId(), request.getContent()));
        return DocCommentInfo.Main.builder()
                .id(comment.getId())
                .docToken(doc.getDocToken())
                .content(comment.getContent())
                .userId(comment.getUserId())
                .build();
    }

    @Override
    public DocCommentInfo.Main editComment(DocCommand.EditCommentRequest request) {
        DocComment comment = docCommentReader.findById(request.getCommentId());
        comment.setContent(request.getContent());
        return DocCommentInfo.Main.builder()
                .id(comment.getId())
                .docToken(null)
                .content(comment.getContent())
                .userId(comment.getUserId())
                .build();
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
