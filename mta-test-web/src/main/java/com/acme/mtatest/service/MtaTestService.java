package com.acme.mtatest.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.acme.mtatest.model.MtaTestRequest;
import com.acme.mtatest.model.MtaTestResponse;
import com.acme.mtatest.model.TransitTimeResponse;
import com.acme.mtatest.exception.MtaTestException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@ApplicationScoped
public class MtaTestService {

    private static final Logger logger = LogManager.getLogger(MtaTestService.class);

    private final Map<String, MtaTestResponse> mtaTestStore = new ConcurrentHashMap<>();
    private final Map<String, List<MtaTestResponse>> accountMtaTests = new ConcurrentHashMap<>();

    @Inject
    private EmailService emailService;

    @Inject
    private TransitTimeService transitTimeService;

    public MtaTestResponse scheduleMtaTest(MtaTestRequest request) {
        validateMtaTestRequest(request);

        String confirmationNumber = generateConfirmationNumber();
        String proNumber = generateProNumber();

        Integer transitDays = null;
        if (request.getDestinationZip() != null && request.getShipperAddress() != null) {
            try {
                TransitTimeResponse transitResponse = transitTimeService.estimateTransitTime(
                        request.getShipperAddress().getZipCode(),
                        request.getDestinationZip(),
                        request.getMtaTestDate(),
                        null);
                transitDays = transitResponse.getTransitDays();
            } catch (Exception e) {
                logger.warn("Could not estimate transit time: {}", e.getMessage());
            }
        }

        MtaTestResponse response = MtaTestResponse.builder()
                .confirmationNumber(confirmationNumber)
                .status("SCHEDULED")
                .scheduledDate(request.getMtaTestDate())
                .estimatedTimeWindow(request.getReadyTime() + " - " + request.getCloseTime())
                .proNumber(proNumber)
                .serviceCenterId(resolveServiceCenter(request.getShipperAddress().getZipCode()))
                .message("MtaTest has been scheduled successfully")
                .estimatedTransitDays(transitDays)
                .build();

        mtaTestStore.put(confirmationNumber, response);
        accountMtaTests.computeIfAbsent(request.getAccountNumber(), k -> new ArrayList<>()).add(response);

        sendConfirmationEmail(request, response);

        return response;
    }

    public MtaTestResponse getMtaTestStatus(String confirmationNumber) {
        return mtaTestStore.get(confirmationNumber);
    }

    public MtaTestResponse cancelMtaTest(String confirmationNumber) {
        MtaTestResponse existing = mtaTestStore.get(confirmationNumber);
        if (existing == null) {
            throw new MtaTestException("MTA_TEST_NOT_FOUND",
                    "No mtatest found with confirmation number: " + confirmationNumber);
        }

        MtaTestResponse cancelled = MtaTestResponse.builder()
                .confirmationNumber(confirmationNumber)
                .status("CANCELLED")
                .scheduledDate(existing.getScheduledDate())
                .estimatedTimeWindow(existing.getEstimatedTimeWindow())
                .proNumber(existing.getProNumber())
                .serviceCenterId(existing.getServiceCenterId())
                .message("MtaTest has been cancelled")
                .build();

        mtaTestStore.put(confirmationNumber, cancelled);
        return cancelled;
    }

    public List<MtaTestResponse> getMtaTestsByAccount(String accountNumber) {
        return accountMtaTests.getOrDefault(accountNumber, new ArrayList<>());
    }

    private void validateMtaTestRequest(MtaTestRequest request) {
        if (request.getAccountNumber() == null || request.getAccountNumber().trim().isEmpty()) {
            throw new MtaTestException("INVALID_ACCOUNT", "Account number is required");
        }
        if (request.getMtaTestDate() == null) {
            throw new MtaTestException("INVALID_DATE", "MtaTest date is required");
        }
        if (request.getMtaTestDate().before(new Date())) {
            throw new MtaTestException("INVALID_DATE", "MtaTest date must be in the future");
        }
        if (request.getShipperAddress() == null) {
            throw new MtaTestException("INVALID_ADDRESS", "Shipper address is required");
        }
        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new MtaTestException("INVALID_ITEMS", "At least one shipment item is required");
        }
    }

    private String generateConfirmationNumber() {
        return "PU" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private String generateProNumber() {
        return "PRO" + System.currentTimeMillis() % 1000000000L;
    }

    private String resolveServiceCenter(String zipCode) {
        if (zipCode == null) return "UNKNOWN";
        return "SC-" + zipCode.substring(0, Math.min(3, zipCode.length()));
    }

    private void sendConfirmationEmail(MtaTestRequest request, MtaTestResponse response) {
        if (request.getRequesterEmail() != null && !request.getRequesterEmail().isEmpty()) {
            try {
                String subject = "ACME MtaTest Confirmation - " + response.getConfirmationNumber();
                String body = String.format(
                        "Your mtatest has been scheduled.\n\n" +
                        "Confirmation Number: %s\n" +
                        "PRO Number: %s\n" +
                        "Scheduled Date: %s\n" +
                        "Time Window: %s\n" +
                        "Service Center: %s\n",
                        response.getConfirmationNumber(),
                        response.getProNumber(),
                        response.getScheduledDate(),
                        response.getEstimatedTimeWindow(),
                        response.getServiceCenterId());

                emailService.sendEmail(request.getRequesterEmail(), subject, body);
            } catch (Exception e) {
                logger.error("Failed to send confirmation email: {}", e.getMessage(), e);
            }
        }
    }
}
