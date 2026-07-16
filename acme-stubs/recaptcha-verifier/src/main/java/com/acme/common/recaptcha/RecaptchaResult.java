package com.acme.common.recaptcha;

import java.io.Serializable;

public class RecaptchaResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private boolean success;
    private double score;
    private String action;
    private String hostname;

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    public double getScore() { return score; }
    public void setScore(double score) { this.score = score; }
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    public String getHostname() { return hostname; }
    public void setHostname(String hostname) { this.hostname = hostname; }
}
