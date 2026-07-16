package com.acme.customer.api;

public class AccountValidationClient {

    private String baseUrl;

    public AccountValidationClient() {}

    public AccountValidationClient(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public AccountValidationResult validateAccount(String accountNumber) {
        return validateAccount(accountNumber, null);
    }

    public AccountValidationResult validateAccount(String accountNumber, String zipCode) {
        AccountValidationResult result = new AccountValidationResult();
        result.setValid(accountNumber != null && accountNumber.matches("\\d{7,10}"));
        if (result.isValid()) {
            result.setAccountName("Account " + accountNumber);
            result.setAccountType("SHIPPER");
            result.setAccountStatus("ACTIVE");
        }
        return result;
    }

    public String getBaseUrl() { return baseUrl; }
    public void setBaseUrl(String baseUrl) { this.baseUrl = baseUrl; }
}
