package com.acme.mtatest.service;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

import com.acme.common.recaptcha.jboss.JBossRecaptchaValidator;
import com.acme.mtatest.exception.MtaTestException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@ApplicationScoped
public class RecaptchaService {

    private static final Logger logger = LogManager.getLogger(RecaptchaService.class);

    private JBossRecaptchaValidator recaptchaValidator;

    @PostConstruct
    public void init() {
        recaptchaValidator = new JBossRecaptchaValidator();
        recaptchaValidator.setMinimumScore(0.5);
        logger.info("JBossRecaptchaValidator initialized with minimum score {}", recaptchaValidator.getMinimumScore());
    }

    public void validateToken(String recaptchaToken) {
        if (recaptchaToken == null || recaptchaToken.trim().isEmpty()) {
            logger.warn("reCAPTCHA token is missing");
            throw new MtaTestException("RECAPTCHA_REQUIRED", "reCAPTCHA verification is required");
        }

        boolean isValid = recaptchaValidator.validate(recaptchaToken);

        if (!isValid) {
            logger.warn("reCAPTCHA verification failed");
            throw new MtaTestException("RECAPTCHA_FAILED", "reCAPTCHA verification failed");
        }

        logger.debug("reCAPTCHA verification passed");
    }
}
