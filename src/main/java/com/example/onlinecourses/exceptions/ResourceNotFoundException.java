package com.example.onlinecourses.exceptions;

// This exception is thrown when a resource is not found (unchecked exception)
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
