package com.acme.mtatest.webservice;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import com.acme.mtatest.model.TransitTimeRequest;
import com.acme.mtatest.model.TransitTimeResponse;
import com.acme.mtatest.service.TransitTimeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Path("/transit-time")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Transit Time", description = "Transit time estimation operations")
public class TransitTimeResource {

    private static final Logger logger = LogManager.getLogger(TransitTimeResource.class);

    @Inject
    private TransitTimeService transitTimeService;

    @POST
    @Path("/estimate")
    @Operation(summary = "Estimate transit time",
               description = "Calculates estimated transit time between origin and destination")
    @ApiResponse(responseCode = "200", description = "Transit time calculated")
    @ApiResponse(responseCode = "400", description = "Invalid ZIP codes")
    public Response estimateTransitTime(@Valid TransitTimeRequest request) {
        logger.info("Estimating transit time from {} to {}",
                request.getOriginZip(), request.getDestinationZip());

        TransitTimeResponse response = transitTimeService.estimateTransitTime(
                request.getOriginZip(),
                request.getDestinationZip(),
                request.getShipDate(),
                request.getWeight());

        return Response.ok(response).build();
    }
}