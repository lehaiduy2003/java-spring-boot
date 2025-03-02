package com.example.onlinecourses.exceptions;

// This exception is thrown when the password does not match (unchecked exception)
public class PasswordNotMatchException extends RuntimeException {
    public PasswordNotMatchException(String message) {
        super(message);
    }
}
