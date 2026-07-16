package com.acme.mtatest.exception;

public class MtaTestException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final String errorCode;

    public MtaTestException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public MtaTestException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
