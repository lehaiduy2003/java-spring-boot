package com.example.onlinecourses.exceptions;

// This exception is thrown when a resource is not found (unchecked exception)
public class RoleNotFoundException extends RuntimeException {
    public RoleNotFoundException(String message) {
        super(message);
    }
}
