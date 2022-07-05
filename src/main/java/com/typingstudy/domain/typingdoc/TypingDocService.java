package com.typingstudy.domain.typingdoc;

import java.util.List;

public interface TypingDocService {
    List<TypingDocInfo.PageItem> retrieveDocs(Long userId, int page, String sort, String direction);
}
