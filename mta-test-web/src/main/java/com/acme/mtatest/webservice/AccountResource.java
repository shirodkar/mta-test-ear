package com.acme.mtatest.webservice;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.acme.mtatest.model.AccountValidationRequest;
import com.acme.mtatest.model.AccountValidationResponse;
import com.acme.mtatest.service.AccountService;
import com.acme.mtatest.service.RecaptchaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Path("/accounts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Account", description = "Account validation operations")
public class AccountResource {

    private static final Logger logger = LogManager.getLogger(AccountResource.class);

    @Inject
    private AccountService accountService;

    @Inject
    private RecaptchaService recaptchaService;

    @POST
    @Path("/validate")
    @Operation(summary = "Validate a customer account",
               description = "Validates the customer account number and returns account details")
    @ApiResponse(responseCode = "200", description = "Account validation completed")
    @ApiResponse(responseCode = "400", description = "Invalid request")
    public Response validateAccount(@Valid AccountValidationRequest request) {
        logger.info("Validating account: {}", request.getAccountNumber());

        recaptchaService.validateToken(request.getRecaptchaToken());

        AccountValidationResponse response = accountService.validateAccount(
                request.getAccountNumber(), request.getZipCode());

        return Response.ok(response).build();
    }
}
