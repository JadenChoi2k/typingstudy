package com.typingstudy.infrastructure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Optional;

@Slf4j
@Controller
public class IndexController {

    @GetMapping("/")
    public ResponseEntity home() {
        return ResponseEntity.ok("home");
    }
}
