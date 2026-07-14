package com.ensf.fnf.core.exception;

/**
 * Thrown when a requested resource (e.g. the authenticated user) cannot be found.
 * If your project already has an equivalent exception class, delete this file
 * and use yours instead - just update the import in UserServiceImpl.
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}