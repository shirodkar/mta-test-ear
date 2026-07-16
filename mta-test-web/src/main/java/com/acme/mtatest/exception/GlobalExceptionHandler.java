package com.acme.mtatest.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.acme.mtatest.model.ErrorResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Provider
public class GlobalExceptionHandler implements ExceptionMapper<Exception> {

    private static final Logger logger = LogManager.getLogger(GlobalExceptionHandler.class);

    @Override
    public Response toResponse(Exception exception) {
        if (exception instanceof MtaTestException) {
            MtaTestException pe = (MtaTestException) exception;
            logger.warn("MtaTest error [{}]: {}", pe.getErrorCode(), pe.getMessage());

            ErrorResponse error = ErrorResponse.builder()
                    .errorCode(pe.getErrorCode())
                    .message(pe.getMessage())
                    .timestamp(System.currentTimeMillis())
                    .build();

            Response.Status status = mapErrorCodeToStatus(pe.getErrorCode());
            return Response.status(status)
                    .entity(error)
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }

        logger.error("Unexpected error: {}", exception.getMessage(), exception);

        ErrorResponse error = ErrorResponse.builder()
                .errorCode("INTERNAL_ERROR")
                .message("An unexpected error occurred")
                .timestamp(System.currentTimeMillis())
                .build();

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(error)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    private Response.Status mapErrorCodeToStatus(String errorCode) {
        switch (errorCode) {
            case "INVALID_ACCOUNT":
            case "INVALID_DATE":
            case "INVALID_ADDRESS":
            case "INVALID_ITEMS":
            case "INVALID_ZIP":
                return Response.Status.BAD_REQUEST;
            case "RECAPTCHA_REQUIRED":
            case "RECAPTCHA_FAILED":
                return Response.Status.FORBIDDEN;
            case "MTA_TEST_NOT_FOUND":
                return Response.Status.NOT_FOUND;
            default:
                return Response.Status.INTERNAL_SERVER_ERROR;
        }
    }
}
