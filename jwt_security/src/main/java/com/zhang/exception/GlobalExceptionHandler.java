package com.zhang.exception;

import com.zhang.conf.AjaxResult;
import com.zhang.conf.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler({BadRequestException.class, IllegalArgumentException.class})
    public AjaxResult bizExceptionHandler(Exception e) {
        return AjaxResult.fail(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public AjaxResult exceptionHandler(Exception e) {
        e.printStackTrace();
        System.err.println(e.getMessage());
        System.err.println(e.toString());
        log.error(e.getMessage(), e);
        return AjaxResult.fail("系统异常请稍后重试:" + e.getMessage());
    }
}
