package com.sipl.ticket.core.exception.custom;

public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(String message) {
        super(message);
    }

    @Override
    public String toString() {
        return "ProductExceptions{" + ", message='" + getMessage() + '\'' + '}';
    }
}
