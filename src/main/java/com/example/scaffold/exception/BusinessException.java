package com.example.scaffold.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BusinessException extends RuntimeException {

    private int code;
    private HttpStatus httpStatus;

    public BusinessException(final ErrorCodes errorCodes) {
        super(errorCodes.getMessage());
        this.code = errorCodes.getCode();
        this.httpStatus = errorCodes.getHttpStatus();
    }

    public BusinessException(final String message) {
        super(message);
    }
}
