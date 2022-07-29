package com.typingstudy.common.exception;

import com.typingstudy.common.response.ErrorCode;

public class AlreadyExistException extends BaseException {

    public AlreadyExistException() {
        super(ErrorCode.COMMON_ALREADY_EXIST);
    }

    public AlreadyExistException(String message) {
        super(message, ErrorCode.COMMON_ALREADY_EXIST);
    }
}
