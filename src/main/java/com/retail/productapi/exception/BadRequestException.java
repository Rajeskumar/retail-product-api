package com.retail.productapi.exception;

/**
 * Custom Exception class to throw when bad request object is received.
 */
public class BadRequestException extends Exception {

    /**
     * Constructs a new {@link BadRequestException} with the specified detail message.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public BadRequestException(String message) {
        super(message);
    }
}
