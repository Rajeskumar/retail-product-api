package com.retail.productapi.exception;

public class RestTemplateRetryException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public RestTemplateRetryException() {}

    public RestTemplateRetryException(String message) {
        super(message);
    }

    public RestTemplateRetryException(String message, Throwable cause) {
        super(message,cause);
    }
}
