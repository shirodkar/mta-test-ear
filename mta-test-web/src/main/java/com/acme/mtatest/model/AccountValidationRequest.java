package com.acme.mtatest.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Account validation request")
public class AccountValidationRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "Customer account number to validate", required = true)
    @JsonProperty("accountNumber")
    private String accountNumber;

    @Schema(description = "Customer ZIP code for validation")
    @JsonProperty("zipCode")
    private String zipCode;

    @Schema(description = "reCAPTCHA response token")
    @JsonProperty("recaptchaToken")
    private String recaptchaToken;
}
