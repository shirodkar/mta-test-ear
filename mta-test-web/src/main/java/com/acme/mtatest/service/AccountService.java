package com.acme.mtatest.service;

import javax.enterprise.context.ApplicationScoped;

import com.acme.mtatest.model.AccountValidationResponse;
import com.acme.mtatest.exception.MtaTestException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@ApplicationScoped
public class AccountService {

    private static final Logger logger = LogManager.getLogger(AccountService.class);

    public AccountValidationResponse validateAccount(String accountNumber, String zipCode) {
        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            throw new MtaTestException("INVALID_ACCOUNT", "Account number is required");
        }

        logger.info("Validating account {} with ZIP {}", accountNumber, zipCode);

        // Delegates to the validate-account-client library for actual validation
        // against the ACME account management system
        boolean isValid = performAccountValidation(accountNumber, zipCode);

        if (!isValid) {
            return AccountValidationResponse.builder()
                    .valid(false)
                    .accountStatus("INVALID")
                    .message("Account number could not be validated")
                    .build();
        }

        return AccountValidationResponse.builder()
                .valid(true)
                .accountName(getAccountName(accountNumber))
                .accountType("SHIPPER")
                .accountStatus("ACTIVE")
                .message("Account validated successfully")
                .build();
    }

    private boolean performAccountValidation(String accountNumber, String zipCode) {
        // In production, this calls the validate-account-client (OpenFeign-based)
        // to validate against the ACME backend account system.
        // Sample implementation returns true for well-formed account numbers.
        return accountNumber != null
                && accountNumber.matches("\\d{7,10}")
                && !accountNumber.startsWith("0");
    }

    private String getAccountName(String accountNumber) {
        // In production, this retrieves the account name from the account system.
        return "Account " + accountNumber;
    }
}
