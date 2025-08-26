package com.banking.backend.customer.exception;

import java.time.Instant;

public class ErrorResponse {

    private Instant timestamp;
    private String message;
    private String details;

    public ErrorResponse(Instant timestamp, String message, String details) {
        this.timestamp = timestamp;
        this.message = message;
        this.details = details;
    }


    public Instant getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public String getDetails() {
        return details;
    }
}