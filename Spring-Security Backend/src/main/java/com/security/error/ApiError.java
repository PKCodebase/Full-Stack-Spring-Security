package com.security.error;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ApiError {

    private boolean status;
    private int statusCode;
    private String message;
    private String path;
    private LocalDateTime timestamp;

    public ApiError(int statusCode, String message, String path) {
        this.status = false;
        this.statusCode = statusCode;
        this.message = message;
        this.path = path;
        this.timestamp = LocalDateTime.now();
    }
}

