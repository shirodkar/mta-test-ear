package com.acme.ecommerce.mtatest;

import java.util.Date;

public class MtaTestClient {

    private String serviceUrl;

    public MtaTestClient() {}

    public MtaTestClient(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    public MtaTestResult scheduleMtaTest(MtaTestScheduleRequest request) {
        MtaTestResult result = new MtaTestResult();
        result.setSuccess(true);
        result.setConfirmationNumber("PU-STUB-001");
        return result;
    }

    public MtaTestResult getMtaTestStatus(String confirmationNumber) {
        MtaTestResult result = new MtaTestResult();
        result.setSuccess(true);
        result.setConfirmationNumber(confirmationNumber);
        result.setStatus("SCHEDULED");
        return result;
    }

    public MtaTestResult cancelMtaTest(String confirmationNumber) {
        MtaTestResult result = new MtaTestResult();
        result.setSuccess(true);
        result.setConfirmationNumber(confirmationNumber);
        result.setStatus("CANCELLED");
        return result;
    }

    public String getServiceUrl() { return serviceUrl; }
    public void setServiceUrl(String serviceUrl) { this.serviceUrl = serviceUrl; }
}
