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
@Schema(description = "Account validation response")
public class AccountValidationResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "Whether the account is valid")
    @JsonProperty("valid")
    private boolean valid;

    @Schema(description = "Account holder name")
    @JsonProperty("accountName")
    private String accountName;

    @Schema(description = "Account type")
    @JsonProperty("accountType")
    private String accountType;

    @Schema(description = "Account status (ACTIVE, INACTIVE, SUSPENDED)")
    @JsonProperty("accountStatus")
    private String accountStatus;

    @Schema(description = "Validation message")
    @JsonProperty("message")
    private String message;
}
