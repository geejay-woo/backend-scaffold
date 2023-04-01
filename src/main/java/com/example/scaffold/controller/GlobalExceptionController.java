package com.example.scaffold.controller;

import com.example.scaffold.exception.BusinessException;
import com.example.scaffold.exception.ErrorCodes;
import com.example.scaffold.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Order(value = Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class GlobalExceptionController {

    // 已知场景异常：全局异常不做记录，抛出处记录上下文信息
    // 未知场景异常：外层直接打印堆栈，确定位置
    // feign调用异常：见BaseErrorDecoder

    @ExceptionHandler({BusinessException.class})
    public ResponseEntity<ErrorResponse> businessException(BusinessException e) {
        final ErrorResponse errorResponse = new ErrorResponse(e.getCode(), e.getMessage());
        return ResponseEntity.status(e.getHttpStatus()).body(errorResponse);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<List<ErrorResponse>> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        final List<ErrorResponse> errors = e.getBindingResult().getAllErrors().stream()
                .map(error -> {
                    final String fieldName = ((FieldError) error).getField();
                    final String errorMessage = error.getDefaultMessage();
                    return new ErrorResponse(fieldName, errorMessage);
                }).collect(Collectors.toList());
        return ResponseEntity.badRequest().body(errors);
    }

    // 自由添加异常处理器
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> unknownException(RuntimeException e) {
        log.error("unknown exception, stack: {}", e);
        final ErrorResponse errorResponse = new ErrorResponse(ErrorCodes.UNKNOWN_ERROR.getCode(), "unknown error");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
