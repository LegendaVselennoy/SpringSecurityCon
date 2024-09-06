package org.example.exception;

public class NotFoundEmployeeException extends RuntimeException {

    public NotFoundEmployeeException(String message) {
        super(message);
    }

    public NotFoundEmployeeException(String message, Throwable cause) {
        super(message, cause);
    }
}
