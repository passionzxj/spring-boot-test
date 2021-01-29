package com.zhang.jwt.config.exception;

public class BizException extends RuntimeException {
    public BizException(String msg){
        super(msg);
    }
    public BizException(){}
}
