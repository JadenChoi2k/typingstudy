package com.typingstudy.infrastructure.typingdoc;

import com.typingstudy.domain.typingdoc.TypingDoc;
import com.typingstudy.domain.typingdoc.TypingDocReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TypingDocReaderImpl implements TypingDocReader {
    private final TypingDocRepository docRepository;

    @Override
    public List<TypingDoc> findAllWithUser(Long userId, int page, String sortBy, String direction) {
        return docRepository.findAllWithAuthor(userId, page, sortBy, direction);
    }

    @Override
    public List<TypingDoc> findAllByIdList(List<Long> idList) {
        return docRepository.findAllByIdList(idList);
    }

    @Override
    public List<TypingDoc> findAllByTokenList(List<String> tokenList) {
        return docRepository.findAllByTokenList(tokenList);
    }

    @Override
    public TypingDoc findByToken(String token) {
        return docRepository.findByToken(token);
    }
}
