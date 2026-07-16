package com.acme.enterprise.email.client;

import com.acme.enterprise.email.wsdl.EmailRequest;
import com.acme.enterprise.email.wsdl.EmailResponse;

public class SystemEmailClient {

    private String endpointUrl;

    public SystemEmailClient() {}

    public SystemEmailClient(String endpointUrl) {
        this.endpointUrl = endpointUrl;
    }

    public EmailResponse sendEmail(EmailRequest request) {
        EmailResponse response = new EmailResponse();
        response.setSuccess(true);
        response.setMessageId("stub-msg-" + System.currentTimeMillis());
        return response;
    }

    public EmailResponse sendSimpleEmail(String to, String subject, String body) {
        EmailResponse response = new EmailResponse();
        response.setSuccess(true);
        response.setMessageId("stub-msg-" + System.currentTimeMillis());
        return response;
    }

    public String getEndpointUrl() { return endpointUrl; }
    public void setEndpointUrl(String endpointUrl) { this.endpointUrl = endpointUrl; }
}
