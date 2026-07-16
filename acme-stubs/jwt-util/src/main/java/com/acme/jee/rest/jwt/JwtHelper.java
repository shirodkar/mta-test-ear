package com.acme.jee.rest.jwt;

public class JwtHelper {

    private String secretKey;
    private long expirationMs = 3600000;

    public JwtHelper() {}

    public JwtHelper(String secretKey) {
        this.secretKey = secretKey;
    }

    public String generateToken(String subject) {
        return "stub-jwt-token-for-" + subject;
    }

    public boolean validateToken(String token) {
        return token != null && !token.isEmpty();
    }

    public String getSubject(String token) {
        if (token != null && token.startsWith("stub-jwt-token-for-")) {
            return token.substring("stub-jwt-token-for-".length());
        }
        return null;
    }

    public String getSecretKey() { return secretKey; }
    public void setSecretKey(String secretKey) { this.secretKey = secretKey; }
    public long getExpirationMs() { return expirationMs; }
    public void setExpirationMs(long expirationMs) { this.expirationMs = expirationMs; }
}
