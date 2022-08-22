package com.typingstudy.interfaces;

import com.typingstudy.common.exception.BaseException;
import com.typingstudy.common.response.CommonResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ErrorControllerAdvice {

    @ExceptionHandler(BaseException.class)
    public CommonResponse baseException(BaseException e) {
        return CommonResponse.fail(e.getMessage(), e.getErrorCode().getErrorMsg());
    }
}
