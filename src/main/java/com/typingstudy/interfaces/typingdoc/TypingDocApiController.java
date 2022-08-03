package com.typingstudy.interfaces.typingdoc;

import com.typingstudy.application.typingdoc.TypingDocFacade;
import com.typingstudy.common.exception.InvalidAccessException;
import com.typingstudy.common.response.CommonResponse;
import com.typingstudy.domain.typingdoc.DocCommand;
import com.typingstudy.domain.typingdoc.TypingDoc;
import com.typingstudy.domain.typingdoc.TypingDocInfo;
import com.typingstudy.domain.typingdoc.comment.DocCommentInfo;
import com.typingstudy.domain.typingdoc.history.DocReviewHistoryInfo;
import com.typingstudy.domain.typingdoc.object.DocObjectInfo;
import com.typingstudy.interfaces.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequestMapping("/api/v1/docs")
@RestController
@RequiredArgsConstructor
public class TypingDocApiController {
    private final TypingDocFacade docFacade;
    private final TypingDocDtoMapper dtoMapper;

    @PostMapping
    public CommonResponse createDoc(@Valid @RequestBody TypingDocDto.CreateDoc createRequest) {
        createRequest.setAuthorId(SecurityUtils.getUserId());
        TypingDocInfo.Main doc = docFacade.createDoc(dtoMapper.of(createRequest));
        return CommonResponse.success(dtoMapper.of(doc));
    }

    @GetMapping
    public CommonResponse docs(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "sort", defaultValue = "createdAt") String sort,
            @RequestParam(name = "direction", defaultValue = "desc") String direction) {
        List<TypingDocInfo.PageItem> pageResult =
                docFacade.retrieveDocs(SecurityUtils.getUserId(), page, sort, direction);
        return CommonResponse.success(
                pageResult.stream().map(dtoMapper::of).toList()
        );
    }

    @GetMapping
    public CommonResponse docsByToken(@RequestParam(name = "token") List<String> tokenList) {
        List<TypingDocInfo.PageItem> pageResult = docFacade.retrieveDocs(tokenList);
        Long userId = SecurityUtils.getUserId();
        pageResult.forEach(item -> {
                    if (item.getAccess() == TypingDoc.Access.PRIVATE && !item.getAuthorId().equals(userId))
                        throw new InvalidAccessException("권한이 없습니다.");
                });
        return CommonResponse.success(
                pageResult.stream().map(dtoMapper::of).toList()
        );
    }

    @GetMapping("/{docToken}")
    public CommonResponse findOne(@PathVariable String docToken) {
        TypingDocInfo.Main doc = docFacade.viewDoc(docToken);
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

    // 유저의 docReviewHistory를 볼 수 있다.
    @GetMapping("/history")
    public CommonResponse history(@RequestParam(name = "page", defaultValue = "0") int page) {
        Long userId = SecurityUtils.getUserId();
        long historyCount = docFacade.reviewCountByUserId(userId);
        List<DocReviewHistoryInfo> historyList = docFacade.reviewHistoryByUserId(userId);
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
        List<DocCommentInfo.Main> comments = docFacade.retrieveComments(docToken);
        return CommonResponse.success(
                comments.stream()
                        .map(dtoMapper::of)
                        .toList()
        );
    }

    @PostMapping("/{docToken}/comment")
    public CommonResponse addDocComment(@PathVariable String docToken,
                                        @Valid @RequestBody TypingDocDto.AddDocComment request) {
        boolean valid = docFacade.validatePrivate(docToken, request.getUserId());
        if (!valid) throw new InvalidAccessException("권한이 없습니다.");
        request.setDocToken(docToken);
        request.setUserId(SecurityUtils.getUserId());
        return CommonResponse.success(
                docFacade.addComment(dtoMapper.of(request)) // private 문서 검증 포함
        );
    }

    @PatchMapping("/{docToken}/comment/{commentId}")
    public CommonResponse editDocComment(@PathVariable String docToken,
                                         @PathVariable Long commentId,
                                         @Valid @RequestBody TypingDocDto.EditDocComment request) {
        request.setCommentId(commentId);
        request.setUserId(SecurityUtils.getUserId());
        return CommonResponse.success(
                docFacade.editComment(dtoMapper.of(request))
        );
    }

    @DeleteMapping("/{docToken}/comment/{commentId}")
    public CommonResponse deleteDocComment(@PathVariable String docToken,
                                         @PathVariable Long commentId,
                                         @Valid @RequestBody TypingDocDto.RemoveDocComment request) {
        request.setCommentId(commentId);
        request.setUserId(SecurityUtils.getUserId());
        docFacade.removeComment(dtoMapper.of(request));
        return CommonResponse.ok();
    }

    @GetMapping("/{docToken}/obj/{fileName}")
    public ResponseEntity<Resource> docObject(@PathVariable String docToken, @PathVariable String fileName) {
        TypingDocInfo.Main doc = docFacade.retrieveDoc(docToken);
        if (doc.getAccess() == TypingDoc.Access.PRIVATE
                && !doc.getAuthorId().equals(SecurityUtils.getUserId())) {
            throw new InvalidAccessException("권한이 없습니다");
        }
        DocObjectInfo docObject = docFacade.retrieveDocObject(
                DocCommand.RetrieveDocObjectRequest.builder()
                        .docToken(docToken)
                        .fileName(fileName)
                        .build());
        ByteArrayResource resource = new ByteArrayResource(docObject.getData());
        return ResponseEntity.ok()
                .contentLength(resource.contentLength())
                .header(HttpHeaders.CONTENT_TYPE,
                        ContentDisposition.attachment().filename(docObject.getFileName()).build().toString())
                .body(resource);
    }

    @PostMapping("/{docToken}/obj")
    public CommonResponse addDocObject(@PathVariable String docToken,
                                       @Valid @RequestBody TypingDocDto.AddObject addObject) {
        addObject.setAuthorId(SecurityUtils.getUserId());
        addObject.setDocToken(docToken);
        docFacade.addDocObject(dtoMapper.of(addObject));
        return CommonResponse.ok();
    }

    @PatchMapping("/{docToken}")
    public CommonResponse editDoc(@PathVariable String docToken, @Valid @RequestBody TypingDocDto.EditDoc editRequest) {
        editRequest.setDocToken(docToken);
        editRequest.setAuthorId(SecurityUtils.getUserId());
        TypingDocInfo.Main docInfo = docFacade.editDoc(dtoMapper.of(editRequest));
        return CommonResponse.success(dtoMapper.of(docInfo));
    }

    @DeleteMapping("/{docToken}")
    public CommonResponse removeDoc(@PathVariable String docToken) {
        Long userId = SecurityUtils.getUserId();
        docFacade.removeDoc(DocCommand.RemoveDocRequest.builder()
                .docToken(docToken)
                .authorId(userId)
                .build());
        return CommonResponse.ok();
    }
}
