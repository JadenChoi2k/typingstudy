package com.typingstudy.infrastructure.user;

import com.typingstudy.domain.typingdoc.TypingDocInfo;
import com.typingstudy.domain.user.UserDocReader;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserDocReaderImpl implements UserDocReader {
    @Override
    public List<TypingDocInfo.PageItem> paging(Long userId, PageRequest pageRequest) {
        return null;
    }
}
