package com.sipl.ticket.core.exception.custom;

public class OrderExpiryConfigNotFoundException extends RuntimeException {
    public OrderExpiryConfigNotFoundException(String message) {
        super(message);
    }
}