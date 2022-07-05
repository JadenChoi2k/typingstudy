package com.typingstudy.domain.typingdoc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TypingDocServiceImpl implements TypingDocService {
    private final TypingDocReader typingDocReader;

    @Override
    public List<TypingDocInfo.PageItem> retrieveDocs(Long userId, int page, String sort, String direction) {
        return typingDocReader.findAllWithUser(userId, page, sort, direction)
                .stream()
                .map(TypingDocInfo.PageItem::new)
                .collect(Collectors.toList());
    }
}
