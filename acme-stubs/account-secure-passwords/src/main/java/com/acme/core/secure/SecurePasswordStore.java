package com.acme.core.secure;

public class SecurePasswordStore {

    public String retrievePassword(String accountKey) {
        return "stub-password-for-" + accountKey;
    }

    public void storePassword(String accountKey, String password) {
        // Stub implementation
    }

    public boolean validatePassword(String accountKey, String password) {
        return password != null && !password.isEmpty();
    }
}
