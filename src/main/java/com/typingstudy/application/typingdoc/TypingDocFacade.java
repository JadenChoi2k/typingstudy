package com.typingstudy.application.typingdoc;

import com.typingstudy.domain.typingdoc.DocCommand;
import com.typingstudy.domain.typingdoc.TypingDoc;
import com.typingstudy.domain.typingdoc.TypingDocInfo;
import com.typingstudy.domain.typingdoc.TypingDocService;
import com.typingstudy.domain.typingdoc.comment.DocCommentInfo;
import com.typingstudy.domain.typingdoc.history.DocReviewHistoryInfo;
import com.typingstudy.domain.typingdoc.object.DocObjectInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TypingDocFacade {
    private final TypingDocService docService;

    public TypingDocInfo.Main createDoc(DocCommand.CreateRequest request) {
        return docService.createDoc(request);
    }

    public TypingDocInfo.Main editDoc(DocCommand.EditDocRequest request) {
        return docService.editDoc(request);
    }

    public void removeDoc(DocCommand.RemoveDocRequest request) {
        docService.removeDoc(request);
    }

    public boolean validatePrivate(String docToken, Long userId) {
        return docService.validatePrivate(docToken, userId);
    }

    public void addDocObject(DocCommand.AddObjectRequest request) {
        docService.addDocObject(request);
    }

    public DocObjectInfo retrieveDocObject(DocCommand.RetrieveDocObjectRequest request) {
        return docService.retrieveDocObject(request);
    }

    public void removeDocObject(DocCommand.RemoveDocRequest request) {
        docService.removeDocObject(request);
    }

    public List<TypingDocInfo.PageItem> retrieveDocs(Long userId, int page, String sort, String direction) {
        return docService.retrieveDocs(userId, page, sort, direction);
    }

    public List<TypingDocInfo.PageItem> retrieveDocs(List<String> docTokenList) {
        return docService.retrieveDocs(docTokenList);
    }

    public TypingDocInfo.Main retrieveDoc(String docToken) {
        return docService.retrieveDoc(docToken);
    }

    // 조회수 증가
    public TypingDocInfo.Main viewDoc(String docToken) {
        return docService.viewDoc(docToken);
    }

    public void reviewDoc(DocCommand.ReviewRequest request) {
        docService.reviewDoc(request);
    }

    public List<DocReviewHistoryInfo> reviewHistoryByToken(String docToken, Integer page) {
        return docService.reviewHistoryByToken(docToken, page);
    }

    public List<DocReviewHistoryInfo> reviewHistoryByUserId(Long userId) {
        return docService.reviewHistoryByUserId(userId);
    }

    public long reviewCountByToken(String docToken) {
        return docService.reviewCountByToken(docToken);
    }

    public long reviewCountByUserId(Long userId) {
        return docService.reviewCountByUserId(userId);
    }

    public List<DocCommentInfo.Main> retrieveComments(String docToken) {
        return docService.retrieveComments(docToken);
    }

    public DocCommentInfo.Main addComment(DocCommand.AddCommentRequest request) {
        return docService.addComment(request);
    }

    public DocCommentInfo.Main editComment(DocCommand.EditCommentRequest request) {
        return docService.editComment(request);
    }

    public void removeComment(DocCommand.RemoveCommentRequest request) {
        docService.removeComment(request);
    }
}
