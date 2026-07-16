package com.acme.mtatest.service;

import java.util.Calendar;
import java.util.Date;

import javax.enterprise.context.ApplicationScoped;

import com.acme.mtatest.model.TransitTimeResponse;
import com.acme.mtatest.exception.MtaTestException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@ApplicationScoped
public class TransitTimeService {

    private static final Logger logger = LogManager.getLogger(TransitTimeService.class);

    public TransitTimeResponse estimateTransitTime(String originZip, String destinationZip,
                                                    Date shipDate, Double weight) {
        if (originZip == null || originZip.trim().isEmpty()) {
            throw new MtaTestException("INVALID_ZIP", "Origin ZIP code is required");
        }
        if (destinationZip == null || destinationZip.trim().isEmpty()) {
            throw new MtaTestException("INVALID_ZIP", "Destination ZIP code is required");
        }

        logger.info("Calculating transit time from {} to {}", originZip, destinationZip);

        // In production, this delegates to the transittime-client library
        // which calls the ACME transit time API
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
