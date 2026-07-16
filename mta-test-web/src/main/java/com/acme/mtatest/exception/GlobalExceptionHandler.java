package com.acme.mtatest.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.acme.jee.rest.error.ErrorResponseBuilder;
import com.acme.jee.rest.error.ServiceException;
import com.acme.jee.rest.validation.JsonValidator;
import com.acme.mtatest.model.ErrorResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Provider
public class GlobalExceptionHandler implements ExceptionMapper<Exception> {

    private static final Logger logger = LogManager.getLogger(GlobalExceptionHandler.class);
    private final JsonValidator jsonValidator = new JsonValidator();

    @Override
    public Response toResponse(Exception exception) {
        if (exception instanceof ServiceException) {
            ServiceException se = (ServiceException) exception;
            logger.warn("Service error [{}]: {}", se.getErrorCode(), se.getMessage());

            ErrorResponseBuilder builder = new ErrorResponseBuilder()
                    .withErrorCode(se.getErrorCode())
                    .withMessage(se.getMessage())
                    .withHttpStatus(se.getHttpStatus());

            ErrorResponse error = ErrorResponse.builder()
                    .errorCode(builder.getErrorCode())
                    .message(builder.getMessage())
                    .details(builder.getDetails())
                    .timestamp(System.currentTimeMillis())
                    .build();

            return Response.status(se.getHttpStatus())
                    .entity(error)
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }

        if (exception instanceof MtaTestException) {
            MtaTestException pe = (MtaTestException) exception;
            logger.warn("MtaTest error [{}]: {}", pe.getErrorCode(), pe.getMessage());

            ErrorResponseBuilder builder = new ErrorResponseBuilder()
                    .withErrorCode(pe.getErrorCode())
                    .withMessage(pe.getMessage())
                    .withHttpStatus(mapErrorCodeToStatus(pe.getErrorCode()).getStatusCode());

            ErrorResponse error = ErrorResponse.builder()
                    .errorCode(builder.getErrorCode())
                    .message(builder.getMessage())
                    .timestamp(System.currentTimeMillis())
                    .build();

            return Response.status(builder.getHttpStatus())
                    .entity(error)
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }

        logger.error("Unexpected error: {}", exception.getMessage(), exception);

        ErrorResponseBuilder builder = new ErrorResponseBuilder()
                .withErrorCode("INTERNAL_ERROR")
                .withMessage("An unexpected error occurred")
                .withHttpStatus(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());

        ErrorResponse error = ErrorResponse.builder()
                .errorCode(builder.getErrorCode())
                .message(builder.getMessage())
                .timestamp(System.currentTimeMillis())
                .build();

        return Response.status(builder.getHttpStatus())
                .entity(error)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    public JsonValidator.ValidationResult validateRequestPayload(String json) {
        return jsonValidator.validatePayload(json);
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
