package com.typingstudy.interfaces;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class AuthController {

    @GetMapping("/unauthorized")
    public ResponseEntity<?> unauthorized() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("unauthorized");
    }

    @GetMapping(value = "/oauth2/error", produces = "text/plain; charset=utf-8")
    public ResponseEntity<String> oauth2Error(@RequestParam(defaultValue = "오류가 발생했습니다...") String msg) {
        log.info("OAuth2.0 error - msg: {}", msg);
        return ResponseEntity
                .ok(msg); // TODO: utf8 깨짐 문제 해결하기
    }
}
