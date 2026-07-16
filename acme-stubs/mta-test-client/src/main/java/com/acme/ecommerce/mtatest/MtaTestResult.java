package com.acme.ecommerce.mtatest;

import java.io.Serializable;

public class MtaTestResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private boolean success;
    private String confirmationNumber;
    private String proNumber;
    private String status;
    private String message;

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    public String getConfirmationNumber() { return confirmationNumber; }
    public void setConfirmationNumber(String confirmationNumber) { this.confirmationNumber = confirmationNumber; }
    public String getProNumber() { return proNumber; }
    public void setProNumber(String proNumber) { this.proNumber = proNumber; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
