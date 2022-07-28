package com.typingstudy.interfaces.typingdoc;

import com.typingstudy.application.typingdoc.TypingDocFacade;
import com.typingstudy.common.exception.InvalidAccessException;
import com.typingstudy.common.response.CommonResponse;
import com.typingstudy.domain.typingdoc.TypingDoc;
import com.typingstudy.domain.typingdoc.TypingDocInfo;
import com.typingstudy.domain.typingdoc.comment.DocCommentInfo;
import com.typingstudy.domain.typingdoc.history.DocReviewHistoryInfo;
import com.typingstudy.interfaces.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/api/v1/docs")
@RestController
@RequiredArgsConstructor
public class TypingDocApiController {
    private TypingDocFacade docFacade;
    private TypingDocDtoMapper dtoMapper;

    @GetMapping("/")
    public CommonResponse docs(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "sort", defaultValue = "createDate") String sort,
            @RequestParam(name = "direction", defaultValue = "desc") String direction) {
        List<TypingDocInfo.PageItem> pageResult =
                docFacade.retrieveDocs(SecurityUtils.getUserId(), page, sort, direction);
        return CommonResponse.success(
                pageResult.stream().map(dtoMapper::of).toList()
        );
    }

    @GetMapping("/{docToken}")
    public CommonResponse findOne(@PathVariable String docToken) {
        TypingDocInfo.Main doc = docFacade.retrieveDoc(docToken);
        if (doc.getAccess() == TypingDoc.Access.PRIVATE) {
            Long userId = SecurityUtils.getUserId();
            if (!doc.getAuthorId().equals(userId)) {
                throw new InvalidAccessException("권한이 없습니다");
            }
        }
        return CommonResponse.success(dtoMapper.of(doc));
    }

    @GetMapping("/{docToken}/history")
    public CommonResponse docHistory(
            @PathVariable String docToken, @RequestParam(name = "page", defaultValue = "0") Integer page) {
        List<DocReviewHistoryInfo> historyList = docFacade.reviewHistoryByToken(docToken, page);
        long historyCount = docFacade.reviewCountByToken(docToken);
        Map<String, Object> data = new HashMap<>();
        data.put("data", historyList.stream().map(dtoMapper::of).toList());
        data.put("size", historyCount);
        return CommonResponse.success(data);
    }

    @GetMapping("/{docToken}/comment")
    public CommonResponse docComment(@PathVariable String docToken) {
        TypingDocInfo.Main doc = docFacade.retrieveDoc(docToken);
        if (doc.getAccess() == TypingDoc.Access.PRIVATE) {
            if (!SecurityUtils.getUserId().equals(doc.getAuthorId())) {
                throw new InvalidAccessException("잘못된 접근입니다");
            }
        }
        return CommonResponse.success(
                doc.getComments().stream()
                        .map(dtoMapper::of)
                        .toList()
        );
    }

    @GetMapping("/{docToken}/obj/{fileName}")
    public CommonResponse docObject(@PathVariable String docToken, @PathVariable String fileName) {
        return null;
    }

    @PostMapping("/{docToken}/obj")
    public CommonResponse addDocObject(@PathVariable String docToken) {

        return null;
    }

    @PostMapping("/create")
    public CommonResponse createDoc(@RequestBody TypingDocDto.CreateDoc createRequest) {
        createRequest.setAuthorId(SecurityUtils.getUserId());
        TypingDocInfo.Main doc = docFacade.createDoc(dtoMapper.of(createRequest));
        return CommonResponse.success(dtoMapper.of(doc));
    }

    // 현재 doc의 edit에 대해 가능 여부를 고려 중.
    @GetMapping("/edit")
    public CommonResponse editDoc() {
        return null;
    }

    @DeleteMapping("/{docToken}")
    public CommonResponse deleteDoc(@PathVariable String docToken) {
        Long userId = SecurityUtils.getUserId();

        return null;
    }
}
