package com.sipl.ticket.core.exception.custom;

public class ProductUnitNotFoundException extends RuntimeException {

    public ProductUnitNotFoundException(String message) {
        super(message);
    }

    @Override
    public String toString() {
        return "ProductUnitExceptions{" + ", message='" + getMessage() + '\'' + '}';
    }
}

