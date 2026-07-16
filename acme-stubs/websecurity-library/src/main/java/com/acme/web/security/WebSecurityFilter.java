package com.acme.web.security;

public class WebSecurityFilter {

    private boolean csrfEnabled = true;
    private boolean xssProtectionEnabled = true;

    public boolean validateCsrfToken(String token) {
        return token != null && !token.isEmpty();
    }

    public String sanitizeInput(String input) {
        if (input == null) return null;
        return input.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
    }

    public boolean isCsrfEnabled() { return csrfEnabled; }
    public void setCsrfEnabled(boolean csrfEnabled) { this.csrfEnabled = csrfEnabled; }
    public boolean isXssProtectionEnabled() { return xssProtectionEnabled; }
    public void setXssProtectionEnabled(boolean xssProtectionEnabled) { this.xssProtectionEnabled = xssProtectionEnabled; }
}
