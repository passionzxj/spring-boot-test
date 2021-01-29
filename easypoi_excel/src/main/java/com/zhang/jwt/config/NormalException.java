package com.zhang.jwt.config;

class NormalException extends RuntimeException {
    NormalException(String message) {
        super(message);
    }
    NormalException(){};
}
