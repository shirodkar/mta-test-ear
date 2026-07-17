package com.acme.mtatest.service;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;

import com.acme.customer.api.AccountValidationClient;
import com.acme.customer.api.AccountValidationResult;
import com.acme.mtatest.model.AccountValidationResponse;
import com.acme.mtatest.exception.MtaTestException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@ApplicationScoped
public class AccountService {

    private static final Logger logger = LogManager.getLogger(AccountService.class);

    private AccountValidationClient validationClient;

    @PostConstruct
    public void init() {
        validationClient = new AccountValidationClient();
        logger.info("AccountValidationClient initialized at {}", validationClient.getBaseUrl());
    }

    public AccountValidationResponse validateAccount(String accountNumber, String zipCode) {
        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            throw new MtaTestException("INVALID_ACCOUNT", "Account number is required");
        }

        logger.info("Validating account {} with ZIP {}", accountNumber, zipCode);

        AccountValidationResult result = validationClient.validateAccount(accountNumber, zipCode);

        if (!result.isValid()) {
            return AccountValidationResponse.builder()
                    .valid(false)
                    .accountStatus("INVALID")
                    .message(result.getMessage())
                    .build();
        }

        return AccountValidationResponse.builder()
                .valid(true)
                .accountName(result.getAccountName())
                .accountType(result.getAccountType())
                .accountStatus(result.getAccountStatus())
                .message(result.getMessage())
                .build();
    }
}