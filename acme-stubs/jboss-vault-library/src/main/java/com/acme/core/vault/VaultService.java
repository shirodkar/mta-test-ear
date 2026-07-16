package com.acme.core.vault;

public class VaultService {

    private String vaultAddress;
    private String vaultToken;

    public VaultService() {}

    public VaultService(String vaultAddress, String vaultToken) {
        this.vaultAddress = vaultAddress;
        this.vaultToken = vaultToken;
    }

    public String readSecret(String path) {
        return "stub-secret-value";
    }

    public String readSecret(String path, String key) {
        return "stub-secret-for-" + key;
    }

    public void writeSecret(String path, String key, String value) {
        // Stub implementation
    }

    public boolean isAvailable() {
        return vaultAddress != null && vaultToken != null;
    }

    public String getVaultAddress() { return vaultAddress; }
    public void setVaultAddress(String vaultAddress) { this.vaultAddress = vaultAddress; }
    public String getVaultToken() { return vaultToken; }
    public void setVaultToken(String vaultToken) { this.vaultToken = vaultToken; }
}
