package com.acme.mtatest.service;

import javax.enterprise.context.ApplicationScoped;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@ApplicationScoped
public class EmailService {

    private static final Logger logger = LogManager.getLogger(EmailService.class);

    public void sendEmail(String to, String subject, String body) {
        // In production, this delegates to the ODEmail / system-email-client libraries
        // which handle SMTP configuration and email delivery through the ACME mail system
        logger.info("Sending email to: {}, subject: {}", to, subject);
        logger.debug("Email body: {}", body);
    }

    public void sendMtaTestConfirmation(String to, String confirmationNumber, String proNumber) {
        String subject = "ACME MtaTest Confirmation - " + confirmationNumber;
        String body = String.format(
                "Thank you for scheduling a mtatest with ACME.\n\n" +
                "Your mtatest confirmation number is: %s\n" +
                "PRO Number: %s\n\n" +
                "You can track your shipment at acme.com using your PRO number.\n\n" +
                "Thank you for choosing ACME.",
                confirmationNumber, proNumber);
        sendEmail(to, subject, body);
    }

    public void sendMtaTestCancellation(String to, String confirmationNumber) {
        String subject = "ACME MtaTest Cancellation - " + confirmationNumber;
        String body = String.format(
                "Your mtatest with confirmation number %s has been cancelled.\n\n" +
                "If you did not request this cancellation, please contact us immediately.\n\n" +
                "Thank you for choosing ACME.",
                confirmationNumber);
        sendEmail(to, subject, body);
    }
}
