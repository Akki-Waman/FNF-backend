package com.sipl.ticket.core.exception.custom;

public class InvalidEventException extends RuntimeException {
    public InvalidEventException(String message) {
        super(message);
    }
}