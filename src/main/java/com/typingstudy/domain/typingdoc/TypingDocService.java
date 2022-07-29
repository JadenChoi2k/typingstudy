package com.typingstudy.domain.typingdoc;

import com.typingstudy.domain.typingdoc.comment.DocCommentInfo;
import com.typingstudy.domain.typingdoc.history.DocReviewHistoryInfo;
import com.typingstudy.domain.typingdoc.object.DocObjectInfo;

import java.util.List;

public interface TypingDocService {

    TypingDocInfo.Main createDoc(DocCommand.CreateRequest request);

    TypingDocInfo.Main editDoc(DocCommand.EditDocRequest request);

    void removeDoc(DocCommand.RemoveDocRequest request);

    void addDocObject(DocCommand.AddObjectRequest request);

    DocObjectInfo retrieveDocObject(DocCommand.RetrieveDocObjectRequest request);

    void removeDocObject(DocCommand.RemoveDocRequest request);

    // 유저가 가진 문서목록 조회
    List<TypingDocInfo.PageItem> retrieveDocs(Long userId, int page, String sort, String direction);

    // 요청된 문서목록 조회
    List<TypingDocInfo.PageItem> retrieveDocs(List<String> docTokenList);

    // 문서 단건 조회
    TypingDocInfo.Main retrieveDoc(String docToken);

    // 문서 복습
    void reviewDoc(DocCommand.ReviewRequest request);

    // 문서 복습 기록 조회
    List<DocReviewHistoryInfo> reviewHistoryByToken(String docToken, Integer page);

    // 유저의 복습 기록 조회
    List<DocReviewHistoryInfo> reviewHistoryByUserId(Long userId);

    long reviewCountByToken(String docToken);

    long reviewCountByUserId(Long userId);

    List<DocCommentInfo.Main> retrieveComments(String docToken);

    DocCommentInfo.Main addComment(DocCommand.AddCommentRequest request);

    DocCommentInfo.Main editComment(DocCommand.EditCommentRequest request);

    void removeComment(DocCommand.RemoveCommentRequest request);
}
