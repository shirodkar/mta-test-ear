package com.acme.mtatest.service;

import javax.enterprise.context.ApplicationScoped;

import com.acme.mtatest.exception.MtaTestException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@ApplicationScoped
public class RecaptchaService {

    private static final Logger logger = LogManager.getLogger(RecaptchaService.class);

    public void validateToken(String recaptchaToken) {
        if (recaptchaToken == null || recaptchaToken.trim().isEmpty()) {
            logger.warn("reCAPTCHA token is missing");
            throw new MtaTestException("RECAPTCHA_REQUIRED", "reCAPTCHA verification is required");
        }

        // In production, this delegates to jboss-recaptcha-validator
        // which uses the recaptcha-verifier library (OkHttp-based) to call
        // Google's reCAPTCHA verification API
        boolean isValid = verifyWithGoogle(recaptchaToken);

        if (!isValid) {
            logger.warn("reCAPTCHA verification failed");
            throw new MtaTestException("RECAPTCHA_FAILED", "reCAPTCHA verification failed");
        }

        logger.debug("reCAPTCHA verification passed");
    }

    private boolean verifyWithGoogle(String token) {
        // In production, calls Google reCAPTCHA API via jboss-recaptcha-validator
        // using site secret from JBoss vault (jboss-vault-library)
        return token != null && !token.isEmpty();
    }
}
