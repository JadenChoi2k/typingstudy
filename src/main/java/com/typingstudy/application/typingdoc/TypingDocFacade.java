package com.typingstudy.application.typingdoc;

import com.typingstudy.domain.typingdoc.TypingDocInfo;
import com.typingstudy.domain.typingdoc.TypingDocService;
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
}
