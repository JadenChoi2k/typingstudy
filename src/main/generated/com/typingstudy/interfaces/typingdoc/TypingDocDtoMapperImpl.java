package com.typingstudy.interfaces.typingdoc;

import com.typingstudy.domain.typingdoc.DocCommand;
import com.typingstudy.domain.typingdoc.TypingDocInfo;
import com.typingstudy.domain.typingdoc.comment.DocCommentInfo;
import com.typingstudy.domain.typingdoc.history.DocReviewHistoryInfo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-08-21T01:01:35+0900",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 17.0.3 (Oracle Corporation)"
)
@Component
public class TypingDocDtoMapperImpl implements TypingDocDtoMapper {

    @Override
    public TypingDocDto.DocInfo of(TypingDocInfo.Main docInfo) {
        if ( docInfo == null ) {
            return null;
        }

        TypingDocDto.DocInfo docInfo1 = new TypingDocDto.DocInfo();

        docInfo1.setDocToken( docInfo.getDocToken() );
        docInfo1.setAuthorId( docInfo.getAuthorId() );
        docInfo1.setTitle( docInfo.getTitle() );
        docInfo1.setContent( docInfo.getContent() );
        docInfo1.setAccess( docInfo.getAccess() );
        List<DocCommentInfo.Main> list = docInfo.getComments();
        if ( list != null ) {
            docInfo1.setComments( new ArrayList<DocCommentInfo.Main>( list ) );
        }
        docInfo1.setViews( docInfo.getViews() );
        docInfo1.setReviewCount( docInfo.getReviewCount() );
        docInfo1.setLastStudyDate( docInfo.getLastStudyDate() );
        docInfo1.setCreateDate( docInfo.getCreateDate() );
        docInfo1.setEditDate( docInfo.getEditDate() );

        return docInfo1;
    }

    @Override
    public TypingDocDto.DocPageItem of(TypingDocInfo.PageItem pageItem) {
        if ( pageItem == null ) {
            return null;
        }

        TypingDocDto.DocPageItem docPageItem = new TypingDocDto.DocPageItem();

        docPageItem.setDocToken( pageItem.getDocToken() );
        docPageItem.setAuthorId( pageItem.getAuthorId() );
        docPageItem.setTitle( pageItem.getTitle() );
        docPageItem.setContent( pageItem.getContent() );
        docPageItem.setAccess( pageItem.getAccess() );
        docPageItem.setViews( pageItem.getViews() );
        docPageItem.setReviewCount( pageItem.getReviewCount() );
        docPageItem.setLastStudyDate( pageItem.getLastStudyDate() );
        docPageItem.setCreateDate( pageItem.getCreateDate() );
        docPageItem.setEditDate( pageItem.getEditDate() );

        return docPageItem;
    }

    @Override
    public TypingDocDto.History of(DocReviewHistoryInfo historyInfo) {
        if ( historyInfo == null ) {
            return null;
        }

        TypingDocDto.History history = new TypingDocDto.History();

        history.setReviewAt( historyInfo.getReviewAt() );
        history.setDocToken( historyInfo.getDocToken() );

        return history;
    }

    @Override
    public TypingDocDto.Comment of(DocCommentInfo.Main commentInfo) {
        if ( commentInfo == null ) {
            return null;
        }

        TypingDocDto.Comment comment = new TypingDocDto.Comment();

        comment.setId( commentInfo.getId() );
        comment.setDocToken( commentInfo.getDocToken() );
        comment.setContent( commentInfo.getContent() );
        comment.setUserId( commentInfo.getUserId() );
        comment.setEditedAt( commentInfo.getEditedAt() );

        return comment;
    }

    @Override
    public DocCommand.CreateRequest of(TypingDocDto.CreateDoc createDocDto) {
        if ( createDocDto == null ) {
            return null;
        }

        DocCommand.CreateRequest.CreateRequestBuilder createRequest = DocCommand.CreateRequest.builder();

        createRequest.authorId( createDocDto.getAuthorId() );
        createRequest.title( createDocDto.getTitle() );
        createRequest.content( createDocDto.getContent() );
        createRequest.access( createDocDto.getAccess() );

        return createRequest.build();
    }

    @Override
    public DocCommand.AddCommentRequest of(TypingDocDto.AddDocComment addDocCommentDto) {
        if ( addDocCommentDto == null ) {
            return null;
        }

        DocCommand.AddCommentRequest.AddCommentRequestBuilder addCommentRequest = DocCommand.AddCommentRequest.builder();

        addCommentRequest.docToken( addDocCommentDto.getDocToken() );
        addCommentRequest.userId( addDocCommentDto.getUserId() );
        addCommentRequest.content( addDocCommentDto.getContent() );

        return addCommentRequest.build();
    }

    @Override
    public DocCommand.EditCommentRequest of(TypingDocDto.EditDocComment editDocCommentDto) {
        if ( editDocCommentDto == null ) {
            return null;
        }

        DocCommand.EditCommentRequest.EditCommentRequestBuilder editCommentRequest = DocCommand.EditCommentRequest.builder();

        editCommentRequest.commentId( editDocCommentDto.getCommentId() );
        editCommentRequest.userId( editDocCommentDto.getUserId() );
        editCommentRequest.content( editDocCommentDto.getContent() );

        return editCommentRequest.build();
    }

    @Override
    public DocCommand.RemoveCommentRequest of(TypingDocDto.RemoveDocComment removeDocCommentDto) {
        if ( removeDocCommentDto == null ) {
            return null;
        }

        DocCommand.RemoveCommentRequest.RemoveCommentRequestBuilder removeCommentRequest = DocCommand.RemoveCommentRequest.builder();

        removeCommentRequest.commentId( removeDocCommentDto.getCommentId() );
        removeCommentRequest.userId( removeDocCommentDto.getUserId() );

        return removeCommentRequest.build();
    }

    @Override
    public DocCommand.EditDocRequest of(TypingDocDto.EditDoc editDocDto) {
        if ( editDocDto == null ) {
            return null;
        }

        DocCommand.EditDocRequest.EditDocRequestBuilder editDocRequest = DocCommand.EditDocRequest.builder();

        editDocRequest.authorId( editDocDto.getAuthorId() );
        editDocRequest.docToken( editDocDto.getDocToken() );
        editDocRequest.title( editDocDto.getTitle() );
        editDocRequest.content( editDocDto.getContent() );
        editDocRequest.access( editDocDto.getAccess() );

        return editDocRequest.build();
    }

    @Override
    public DocCommand.AddObjectRequest of(TypingDocDto.AddObject addObjectDto) {
        if ( addObjectDto == null ) {
            return null;
        }

        DocCommand.AddObjectRequest.AddObjectRequestBuilder addObjectRequest = DocCommand.AddObjectRequest.builder();

        addObjectRequest.authorId( addObjectDto.getAuthorId() );
        addObjectRequest.docToken( addObjectDto.getDocToken() );
        addObjectRequest.fileName( addObjectDto.getFileName() );
        byte[] data = addObjectDto.getData();
        if ( data != null ) {
            addObjectRequest.data( Arrays.copyOf( data, data.length ) );
        }

        return addObjectRequest.build();
    }
}
