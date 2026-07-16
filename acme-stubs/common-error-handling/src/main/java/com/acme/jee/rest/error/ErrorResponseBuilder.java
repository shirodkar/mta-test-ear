package com.acme.jee.rest.error;

import java.io.Serializable;

public class ErrorResponseBuilder implements Serializable {

    private static final long serialVersionUID = 1L;

    private String errorCode;
    private String message;
    private String details;
    private int httpStatus;

    public ErrorResponseBuilder withErrorCode(String errorCode) {
        this.errorCode = errorCode;
        return this;
    }

    public ErrorResponseBuilder withMessage(String message) {
        this.message = message;
        return this;
    }

    public ErrorResponseBuilder withDetails(String details) {
        this.details = details;
        return this;
    }

    public ErrorResponseBuilder withHttpStatus(int httpStatus) {
        this.httpStatus = httpStatus;
        return this;
    }

    public String getErrorCode() { return errorCode; }
    public String getMessage() { return message; }
    public String getDetails() { return details; }
    public int getHttpStatus() { return httpStatus; }
}
