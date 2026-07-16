package com.acme.email;

public class ODEmailSender {

    private String smtpHost;
    private int smtpPort;
    private String fromAddress;

    public ODEmailSender() {
        this.smtpHost = "localhost";
        this.smtpPort = 25;
        this.fromAddress = "noreply@acme.com";
    }

    public void sendEmail(String to, String subject, String body) {
        // Stub: logs email sending intent
        System.out.println("ODEmail: Sending to=" + to + " subject=" + subject);
    }

    public void sendHtmlEmail(String to, String subject, String htmlBody) {
        System.out.println("ODEmail: Sending HTML to=" + to + " subject=" + subject);
    }

    public String getSmtpHost() { return smtpHost; }
    public void setSmtpHost(String smtpHost) { this.smtpHost = smtpHost; }
    public int getSmtpPort() { return smtpPort; }
    public void setSmtpPort(int smtpPort) { this.smtpPort = smtpPort; }
    public String getFromAddress() { return fromAddress; }
    public void setFromAddress(String fromAddress) { this.fromAddress = fromAddress; }
}
