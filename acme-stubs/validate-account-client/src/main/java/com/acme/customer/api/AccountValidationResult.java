package com.acme.customer.api;

import java.io.Serializable;

public class AccountValidationResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private boolean valid;
    private String accountName;
    private String accountType;
    private String accountStatus;
    private String message;

    public boolean isValid() { return valid; }
    public void setValid(boolean valid) { this.valid = valid; }
    public String getAccountName() { return accountName; }
    public void setAccountName(String accountName) { this.accountName = accountName; }
    public String getAccountType() { return accountType; }
    public void setAccountType(String accountType) { this.accountType = accountType; }
    public String getAccountStatus() { return accountStatus; }
    public void setAccountStatus(String accountStatus) { this.accountStatus = accountStatus; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
