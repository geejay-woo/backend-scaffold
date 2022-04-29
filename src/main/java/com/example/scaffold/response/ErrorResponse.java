package com.example.scaffold.response;

import lombok.Getter;

@Getter
public class ErrorResponse {
    private Integer code;
    private String field;
    private String message;

    public ErrorResponse(String field, String message) {
        this.field = field;
        this.message = message;
    }

    public ErrorResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
