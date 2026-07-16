package com.acme.mtatest.webservice;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
