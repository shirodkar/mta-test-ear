package com.acme.mtatest.service;

import java.util.Collections;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

import com.acme.email.ODEmailSender;
import com.acme.enterprise.email.client.SystemEmailClient;
import com.acme.enterprise.email.wsdl.EmailRequest;
import com.acme.enterprise.email.wsdl.EmailResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@ApplicationScoped
public class EmailService {

    private static final Logger logger = LogManager.getLogger(EmailService.class);

    private SystemEmailClient systemEmailClient;
    private ODEmailSender odEmailSender;

    @PostConstruct
    public void init() {
        systemEmailClient = new SystemEmailClient();
        odEmailSender = new ODEmailSender();
        odEmailSender.setFromAddress("noreply@acme.com");
        logger.info("Email service initialized with SystemEmailClient and ODEmailSender");
    }

    public void sendEmail(String to, String subject, String body) {
        logger.info("Sending email to: {}, subject: {}", to, subject);

        try {
            EmailRequest request = new EmailRequest();
            request.setTo(Collections.singletonList(to));
            request.setFrom("noreply@acme.com");
            request.setSubject(subject);
            request.setBody(body);
            request.setContentType("text/plain");

            EmailResponse response = systemEmailClient.sendEmail(request);
            if (response.isSuccess()) {
                logger.info("Email sent via SystemEmailClient, messageId: {}", response.getMessageId());
                return;
            }
            logger.warn("SystemEmailClient failed: {}", response.getErrorMessage());
        } catch (Exception e) {
            logger.warn("SystemEmailClient unavailable: {}", e.getMessage());
        }

        odEmailSender.sendEmail(to, subject, body);
        logger.info("Email sent via ODEmailSender fallback");
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
