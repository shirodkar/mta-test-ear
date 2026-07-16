package com.acme.mtatest.webservice;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.acme.mtatest.model.MtaTestRequest;
import com.acme.mtatest.model.MtaTestResponse;
import com.acme.mtatest.service.MtaTestService;
import com.acme.mtatest.service.RecaptchaService;
import com.acme.mtatest.exception.MtaTestException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Path("/mtaTests")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "MtaTest", description = "MtaTest scheduling operations")
public class MtaTestResource {

    private static final Logger logger = LogManager.getLogger(MtaTestResource.class);

    @Inject
    private MtaTestService mtaTestService;

    @Inject
    private RecaptchaService recaptchaService;

    @POST
    @Operation(summary = "Schedule a new mtatest",
               description = "Creates a new mtatest request for freight collection")
    @ApiResponse(responseCode = "201", description = "MtaTest scheduled successfully")
    @ApiResponse(responseCode = "400", description = "Invalid request data")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    public Response scheduleMtaTest(@Valid MtaTestRequest request) {
        logger.info("Received mtatest request for account: {}", request.getAccountNumber());

        recaptchaService.validateToken(request.getRecaptchaToken());

        MtaTestResponse response = mtaTestService.scheduleMtaTest(request);
        logger.info("MtaTest scheduled successfully. Confirmation: {}", response.getConfirmationNumber());

        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    @GET
    @Path("/{confirmationNumber}")
    @Operation(summary = "Get mtatest status",
               description = "Retrieves the status of an existing mtatest request")
    @ApiResponse(responseCode = "200", description = "MtaTest details retrieved")
    @ApiResponse(responseCode = "404", description = "MtaTest not found")
    public Response getMtaTestStatus(
            @Parameter(description = "MtaTest confirmation number")
            @PathParam("confirmationNumber") String confirmationNumber) {
        logger.info("Retrieving mtatest status for confirmation: {}", confirmationNumber);

        MtaTestResponse response = mtaTestService.getMtaTestStatus(confirmationNumber);
        if (response == null) {
            throw new MtaTestException("MTA_TEST_NOT_FOUND",
                    "No mtatest found with confirmation number: " + confirmationNumber);
        }

        return Response.ok(response).build();
    }

    @PUT
    @Path("/{confirmationNumber}/cancel")
    @Operation(summary = "Cancel a mtatest",
               description = "Cancels an existing scheduled mtatest")
    @ApiResponse(responseCode = "200", description = "MtaTest cancelled successfully")
    @ApiResponse(responseCode = "404", description = "MtaTest not found")
    public Response cancelMtaTest(
            @Parameter(description = "MtaTest confirmation number")
            @PathParam("confirmationNumber") String confirmationNumber) {
        logger.info("Cancelling mtatest: {}", confirmationNumber);

        MtaTestResponse response = mtaTestService.cancelMtaTest(confirmationNumber);
        return Response.ok(response).build();
    }

    @GET
    @Operation(summary = "List mtaTests for account",
               description = "Retrieves all mtaTests for a given account number")
    @ApiResponse(responseCode = "200", description = "MtaTests retrieved")
    public Response getMtaTestsByAccount(
            @Parameter(description = "Customer account number")
            @QueryParam("accountNumber") String accountNumber) {
        logger.info("Retrieving mtaTests for account: {}", accountNumber);

        return Response.ok(mtaTestService.getMtaTestsByAccount(accountNumber)).build();
    }
}
