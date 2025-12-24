package com.sipl.ticket.core.exception.custom;

public class FieldAlreadyExistException extends RuntimeException {
    public FieldAlreadyExistException() {
        super("Field already exist");
    }

    public FieldAlreadyExistException(String message) {
        super(message);
    }

}
