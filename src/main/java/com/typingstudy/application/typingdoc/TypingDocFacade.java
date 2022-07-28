package com.typingstudy.application.typingdoc;

import com.typingstudy.domain.typingdoc.DocCommand;
import com.typingstudy.domain.typingdoc.TypingDocInfo;
import com.typingstudy.domain.typingdoc.TypingDocService;
import com.typingstudy.domain.typingdoc.comment.DocCommentInfo;
import com.typingstudy.domain.typingdoc.history.DocReviewHistoryInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TypingDocFacade {
    private final TypingDocService docService;

    public List<TypingDocInfo.PageItem> retrieveDocs(Long userId, int page, String sort, String direction) {
        return docService.retrieveDocs(userId, page, sort, direction);
    }

    List<TypingDocInfo.PageItem> retrieveDocs(List<String> docTokenList) {
        return docService.retrieveDocs(docTokenList);
    }

    TypingDocInfo.Main retrieveDoc(String docToken) {
        return docService.retrieveDoc(docToken);
    }

    void reviewDoc(DocCommand.ReviewRequest request) {
        docService.reviewDoc(request);
    }

    List<DocReviewHistoryInfo> reviewHistoryByToken(String docToken) {
        return docService.reviewHistoryByToken(docToken);
    }

    List<DocReviewHistoryInfo> reviewHistoryByUserId(Long userId) {
        return docService.reviewHistoryByUserId(userId);
    }

    long reviewCountByToken(String docToken) {
        return docService.reviewCountByToken(docToken);
    }

    long reviewCountByUserId(Long userId) {
        return docService.reviewCountByUserId(userId);
    }

    TypingDocInfo.Main createDoc(DocCommand.CreateRequest request) {
        return docService.createDoc(request);
    }

    List<DocCommentInfo.Main> retrieveComments(String docToken) {
        return docService.retrieveComments(docToken);
    }

    DocCommentInfo.Main addComment(DocCommand.AddCommentRequest request) {
        return docService.addComment(request);
    }

    DocCommentInfo.Main editComment(DocCommand.EditCommentRequest request) {
        return docService.editComment(request);
    }

    void removeComment(DocCommand.RemoveCommentRequest request) {
        docService.removeComment(request);
    }
}
