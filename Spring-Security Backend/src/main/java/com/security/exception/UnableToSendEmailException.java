package com.security.exception;

import org.springframework.http.HttpStatus;

public class UnableToSendEmailException extends RuntimeException {
    public UnableToSendEmailException(String message) {
        super(message);
    }
    public HttpStatus getStatus() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
