package com.typingstudy.domain.user;

import com.typingstudy.domain.typingdoc.TypingDocInfo;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface UserDocReader {
    List<TypingDocInfo.PageItem> paging(Long userId, PageRequest pageRequest);
}
