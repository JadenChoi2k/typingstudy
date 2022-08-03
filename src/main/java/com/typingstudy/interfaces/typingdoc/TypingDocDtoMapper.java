package com.typingstudy.interfaces.typingdoc;

import com.typingstudy.domain.typingdoc.DocCommand;
import com.typingstudy.domain.typingdoc.TypingDocInfo;
import com.typingstudy.domain.typingdoc.comment.DocCommentInfo;
import com.typingstudy.domain.typingdoc.history.DocReviewHistoryInfo;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface TypingDocDtoMapper {
    TypingDocDto.DocInfo of(TypingDocInfo.Main docInfo);

    TypingDocDto.DocPageItem of(TypingDocInfo.PageItem pageItem);

    TypingDocDto.History of(DocReviewHistoryInfo historyInfo);

    TypingDocDto.Comment of(DocCommentInfo.Main commentInfo);

    DocCommand.CreateRequest of(TypingDocDto.CreateDoc createDocDto);

    DocCommand.AddCommentRequest of(TypingDocDto.AddDocComment addDocCommentDto);

    DocCommand.EditCommentRequest of(TypingDocDto.EditDocComment editDocCommentDto);

    DocCommand.RemoveCommentRequest of(TypingDocDto.RemoveDocComment removeDocCommentDto);

    DocCommand.EditDocRequest of(TypingDocDto.EditDoc editDocDto);

    DocCommand.AddObjectRequest of(TypingDocDto.AddObject addObjectDto);
}
