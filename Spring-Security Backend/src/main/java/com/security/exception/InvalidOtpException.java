package com.security.exception;


import org.springframework.http.HttpStatus;

public class InvalidOtpException extends RuntimeException {
    public InvalidOtpException(HttpStatus badRequest, String message) {
        super(message);
    }
    public HttpStatus getStatus(){
        return HttpStatus.BAD_REQUEST;
    }
}
