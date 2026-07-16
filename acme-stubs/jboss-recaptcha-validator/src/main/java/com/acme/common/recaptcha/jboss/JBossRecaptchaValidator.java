package com.acme.common.recaptcha.jboss;

import com.acme.common.recaptcha.RecaptchaVerifier;
import com.acme.common.recaptcha.RecaptchaResult;
import com.acme.core.vault.VaultService;

public class JBossRecaptchaValidator {

    private RecaptchaVerifier verifier;
    private VaultService vaultService;
    private double minimumScore = 0.5;

    public JBossRecaptchaValidator() {
        this.verifier = new RecaptchaVerifier();
        this.vaultService = new VaultService();
    }

    public JBossRecaptchaValidator(String vaultSecretPath) {
        this.vaultService = new VaultService();
        String siteSecret = vaultService.readSecret(vaultSecretPath, "recaptcha-secret");
        this.verifier = new RecaptchaVerifier(siteSecret);
    }

    public boolean validate(String responseToken) {
        if (responseToken == null || responseToken.isEmpty()) {
            return false;
        }
        RecaptchaResult result = verifier.verify(responseToken);
        return result.isSuccess() && result.getScore() >= minimumScore;
    }

    public double getMinimumScore() { return minimumScore; }
    public void setMinimumScore(double minimumScore) { this.minimumScore = minimumScore; }
}
