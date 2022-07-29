package com.typingstudy.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonResponse<T> {
    private Result result;
    private T data;
    private String message;
    private String errorCode;

    public static CommonResponse ok() {
        return CommonResponse.builder()
                .result(Result.SUCCESS)
                .message("ok")
                .build();
    }

    public static <T> CommonResponse<T> success(T data, String message) {
        return (CommonResponse<T>) CommonResponse.builder()
                .result(Result.SUCCESS)
                .data(data)
                .message(message)
                .build();
    }

    public static <T> CommonResponse<T> success(T data) {
        return success(data, null);
    }

    public static CommonResponse fail(String message, String errorCode) {
        return CommonResponse.builder()
                .result(Result.FAIL)
                .message(message)
                .errorCode(errorCode)
                .build();
    }

    public static CommonResponse fail(Exception e) {
        return CommonResponse.builder()
                .result(Result.FAIL)
                .message(e.getMessage())
                .errorCode(e.getClass().getName())
                .build();
    }

    public enum Result {
        SUCCESS, FAIL
    }
}
