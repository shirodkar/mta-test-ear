package com.acme.enterprise.email.wsdl;

import java.io.Serializable;
import java.util.List;

public class EmailRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private String from;
    private List<String> to;
    private List<String> cc;
    private String subject;
    private String body;
    private String contentType;

    public String getFrom() { return from; }
    public void setFrom(String from) { this.from = from; }
    public List<String> getTo() { return to; }
    public void setTo(List<String> to) { this.to = to; }
    public List<String> getCc() { return cc; }
    public void setCc(List<String> cc) { this.cc = cc; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }
    public String getContentType() { return contentType; }
    public void setContentType(String contentType) { this.contentType = contentType; }
}
