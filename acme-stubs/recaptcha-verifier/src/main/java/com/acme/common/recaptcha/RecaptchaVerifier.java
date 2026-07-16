package com.acme.common.recaptcha;

public class RecaptchaVerifier {

    private String siteSecret;
    private String verifyUrl = "https://www.google.com/recaptcha/api/siteverify";

    public RecaptchaVerifier() {}

    public RecaptchaVerifier(String siteSecret) {
        this.siteSecret = siteSecret;
    }

    public RecaptchaResult verify(String responseToken) {
        RecaptchaResult result = new RecaptchaResult();
        result.setSuccess(responseToken != null && !responseToken.isEmpty());
        result.setScore(0.9);
        return result;
    }

    public String getSiteSecret() { return siteSecret; }
    public void setSiteSecret(String siteSecret) { this.siteSecret = siteSecret; }
    public String getVerifyUrl() { return verifyUrl; }
    public void setVerifyUrl(String verifyUrl) { this.verifyUrl = verifyUrl; }
}
