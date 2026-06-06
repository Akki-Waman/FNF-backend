package com.ensf.fnf.core.exception;

import java.time.OffsetDateTime;

public class ApiErrorResponse {

    private String message;
    private String path;
    private OffsetDateTime timestamp;

    public ApiErrorResponse(String message, String path, OffsetDateTime timestamp) {
        this.message = message;
        this.path = path;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public String getPath() {
        return path;
    }

    public OffsetDateTime getTimestamp() {
        return timestamp;
    }
}
