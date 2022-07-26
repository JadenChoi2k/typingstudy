package com.typingstudy.domain.typingdoc;

import com.typingstudy.domain.typingdoc.comment.DocCommentInfo;

import java.util.List;

public interface TypingDocService {
    List<TypingDocInfo.PageItem> retrieveDocs(Long userId, int page, String sort, String direction);

    List<TypingDocInfo.PageItem> retrieveDocs(List<String> docTokenList);

    TypingDocInfo.Main retrieveDoc(String docToken);

    void reviewDoc(DocCommand.ReviewRequest request);

    TypingDocInfo.Main createDoc(DocCommand.CreateRequest request);

    DocCommentInfo.Main addComment(DocCommand.AddCommentRequest request);

    DocCommentInfo.Main editComment(DocCommand.EditCommentRequest request);

    void removeComment(DocCommand.RemoveCommentRequest request);
}
