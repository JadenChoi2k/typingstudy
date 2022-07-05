package com.typingstudy.common.exception;

import com.typingstudy.common.response.ErrorCode;

public class InvalidAccessException extends BaseException {
    public InvalidAccessException() {
        super(ErrorCode.COMMON_INVALID_ACCESS);
    }

    public InvalidAccessException(String message) {
        super(message, ErrorCode.COMMON_INVALID_ACCESS);
    }
}
