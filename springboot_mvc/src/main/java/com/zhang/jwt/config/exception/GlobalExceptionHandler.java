package com.zhang.jwt.config.exception;

import cn.hutool.core.exceptions.ValidateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler({BizException.class,IllegalArgumentException.class, ValidateException.class})
    public ResponseEntity bizExceptionHandler(Exception e) {
        return ResponseEntity.ok(e.getMessage());
    }
}
