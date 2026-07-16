package com.acme.enterprise.email.wsdl;

import java.io.Serializable;

public class EmailResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private boolean success;
    private String messageId;
    private String errorMessage;

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    public String getMessageId() { return messageId; }
    public void setMessageId(String messageId) { this.messageId = messageId; }
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
}
