package com.typingstudy.interfaces.typingdoc;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class TypingDocApiController {

    @GetMapping("/{docId}")
    public Map<String, Object> fetchOne() {
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

    @GetMapping("/")
    public Map<String, Object> docs() {
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
