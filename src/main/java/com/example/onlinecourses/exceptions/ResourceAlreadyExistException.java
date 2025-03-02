package com.example.onlinecourses.exceptions;

// This exception is thrown when a resource already exists (unchecked exception)
public class ResourceAlreadyExistException extends RuntimeException {
    public ResourceAlreadyExistException(String message) {
        super(message);
    }
}
