package com.typingstudy.infrastructure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Optional;

@Slf4j
@RestController
public class IndexController {

    @GetMapping("/")
    public ResponseEntity home() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("hello", "world");
        map.put("result", "success");
        return ResponseEntity.of(Optional.of(map));
    }
}
