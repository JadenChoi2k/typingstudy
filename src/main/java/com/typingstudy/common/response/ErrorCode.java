package com.typingstudy.common.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // COMMON
    COMMON_SYSTEM_ERROR("일시적인 오류가 발생했습니다. 잠시 후 다시 시도해주세요."),
    COMMON_INVALID_PARAMETER("요청한 값이 올바르지 않습니다."),
    COMMON_ENTITY_NOT_FOUND("존재하지 않는 엔티티입니다."),
    COMMON_INVALID_ACCESS("잘못된 접근입니다."),
    COMMON_INVALID_TOKEN("유효하지 않은 토큰값입니다."),
    COMMON_ALREADY_EXIST("이미 존재하는 객체입니다."),
    // (TYPING) DOC
    DOC_INVALID_ACCESS("보호된 문서입니다.");

    private final String errorMsg;

    public String getErrorMsg(Object... args) {
        return String.format(errorMsg, args);
    }
}
