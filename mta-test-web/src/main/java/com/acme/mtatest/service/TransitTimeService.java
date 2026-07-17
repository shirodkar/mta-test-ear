package com.acme.mtatest.service;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import com.acme.ecommerce.transittime.TransitTimeClient;
import com.acme.ecommerce.transittime.TransitTimeResult;
import com.acme.mtatest.model.TransitTimeResponse;
import com.acme.mtatest.exception.MtaTestException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@ApplicationScoped
public class TransitTimeService {

    private static final Logger logger = LogManager.getLogger(TransitTimeService.class);
    private static final String TRANSIT_TIME_API_BASE = System.getProperty(
            "transittime.api.url", "http://localhost:8180");

    private ResteasyClient resteasyClient;
    private TransitTimeClient transitTimeClient;

    @PostConstruct
    public void init() {
        resteasyClient = ((ResteasyClientBuilder) ResteasyClientBuilder.newBuilder())
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .connectionPoolSize(10)
                .build();
        transitTimeClient = new TransitTimeClient();
        logger.info("RESTEasy client initialized for transit time API at {}", TRANSIT_TIME_API_BASE);
        logger.info("TransitTimeClient initialized at {}", transitTimeClient.getServiceUrl());
    }

    @PreDestroy
    public void destroy() {
        if (resteasyClient != null) {
            resteasyClient.close();
            logger.info("RESTEasy client closed");
        }
    }

    public TransitTimeResponse estimateTransitTime(String originZip, String destinationZip,
                                                    Date shipDate, Double weight) {
        if (originZip == null || originZip.trim().isEmpty()) {
            throw new MtaTestException("INVALID_ZIP", "Origin ZIP code is required");
        }
        if (destinationZip == null || destinationZip.trim().isEmpty()) {
            throw new MtaTestException("INVALID_ZIP", "Destination ZIP code is required");
        }

        logger.info("Calculating transit time from {} to {}", originZip, destinationZip);

        TransitTimeResponse remoteResult = fetchTransitTimeFromApi(originZip, destinationZip, weight);
        if (remoteResult != null) {
            return remoteResult;
        }

        logger.info("Remote transit time API unavailable, falling back to TransitTimeClient");

        try {
            TransitTimeResult clientResult = transitTimeClient.getTransitTime(
                    originZip, destinationZip, shipDate != null ? shipDate : new Date());
            return TransitTimeResponse.builder()
                    .transitDays(clientResult.getTransitDays())
                    .estimatedDeliveryDate(clientResult.getEstimatedDeliveryDate())
                    .originServiceCenter(clientResult.getOriginServiceCenter())
                    .destinationServiceCenter(clientResult.getDestinationServiceCenter())
                    .serviceType(clientResult.getServiceType())
                    .build();
        } catch (Exception e) {
            logger.info("TransitTimeClient unavailable: {}, using local calculation", e.getMessage());
        }

        int transitDays = calculateTransitDays(originZip, destinationZip);

        Date estimatedDelivery = calculateDeliveryDate(
                shipDate != null ? shipDate : new Date(), transitDays);

        return TransitTimeResponse.builder()
                .transitDays(transitDays)
                .estimatedDeliveryDate(estimatedDelivery)
                .originServiceCenter(resolveServiceCenter(originZip))
                .destinationServiceCenter(resolveServiceCenter(destinationZip))
                .serviceType(transitDays <= 2 ? "PRIORITY" : "STANDARD")
                .build();
    }

    private TransitTimeResponse fetchTransitTimeFromApi(String originZip, String destinationZip, Double weight) {
        try {
            ResteasyWebTarget target = resteasyClient
                    .target(TRANSIT_TIME_API_BASE)
                    .path("/api/transit-time/calculate");

            Map<String, Object> payload = new HashMap<>();
            payload.put("originZip", originZip);
            payload.put("destinationZip", destinationZip);
            if (weight != null) {
                payload.put("weight", weight);
            }

            Response response = target.request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(payload));

            try {
                if (response.getStatus() == 200) {
                    TransitTimeResponse result = response.readEntity(TransitTimeResponse.class);
                    logger.info("Received transit time from remote API: {} days", result.getTransitDays());
                    return result;
                }
                logger.warn("Transit time API returned status {}", response.getStatus());
            } finally {
                response.close();
            }
        } catch (Exception e) {
            logger.debug("Could not reach transit time API: {}", e.getMessage());
        }
        return null;
    }

    private int calculateTransitDays(String originZip, String destinationZip) {
        // Sample logic based on ZIP code distance heuristic
        // In production, the transittime-client provides actual service days
        int originRegion = Integer.parseInt(originZip.substring(0, 1));
        int destRegion = Integer.parseInt(destinationZip.substring(0, 1));
        int regionDiff = Math.abs(originRegion - destRegion);

        if (regionDiff == 0) return 1;
        if (regionDiff <= 2) return 2;
        if (regionDiff <= 4) return 3;
        return 4;
    }

    private Date calculateDeliveryDate(Date shipDate, int transitDays) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(shipDate);

        int daysAdded = 0;
        while (daysAdded < transitDays) {
            cal.add(Calendar.DAY_OF_MONTH, 1);
            int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
            if (dayOfWeek != Calendar.SATURDAY && dayOfWeek != Calendar.SUNDAY) {
                daysAdded++;
            }
        }

        return cal.getTime();
    }

    private String resolveServiceCenter(String zipCode) {
        return "SC-" + zipCode.substring(0, 3);
    }
}
