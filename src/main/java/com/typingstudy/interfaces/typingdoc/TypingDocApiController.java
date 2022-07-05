package com.typingstudy.interfaces.typingdoc;

import com.typingstudy.application.typingdoc.TypingDocFacade;
import com.typingstudy.common.response.CommonResponse;
import com.typingstudy.domain.typingdoc.TypingDocInfo;
import com.typingstudy.interfaces.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping("/api/v1/docs")
@RestController
@RequiredArgsConstructor
public class TypingDocApiController {
    private TypingDocFacade docFacade;

    @GetMapping("/")
    public CommonResponse docs(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "sort", defaultValue = "createDate") String sort,
            @RequestParam(name = "direction", defaultValue = "desc") String direction) {
        List<TypingDocInfo.PageItem> pageResult =
                docFacade.retrieveDocs(SecurityUtils.getUserId(), page, sort, direction);
        return CommonResponse.success(pageResult);
    }

    @GetMapping("/{docId}")
    public Map<String, Object> findOne() {
        return null;
    }

    @GetMapping("/{docId}/history")
    public Map<String, Object> docHistory() {
        return null;
    }

    @GetMapping("/{docId}/comment")
    public Map<String, Object> docComment() {
        return null;
    }

    @GetMapping("/{docId}/obj/{objId}")
    public Map<String, Object> docObject() {
        return null;
    }

    @PostMapping("/create")
    public Map<String, Object> createDoc() {
        return null;
    }

    @GetMapping("/edit")
    public Map<String, Object> editDoc() {
        return null;
    }

    @GetMapping("/delete")
    public Map<String, Object> deleteDoc() {
        return null;
    }
}
