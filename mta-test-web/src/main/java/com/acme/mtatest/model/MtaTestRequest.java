package com.acme.mtatest.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
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
@Schema(description = "MtaTest scheduling request")
public class MtaTestRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "Customer account number", required = true)
    @JsonProperty("accountNumber")
    private String accountNumber;

    @Schema(description = "Requester name", required = true)
    @JsonProperty("requesterName")
    private String requesterName;

    @Schema(description = "Requester email address")
    @JsonProperty("requesterEmail")
    private String requesterEmail;

    @Schema(description = "Requester phone number")
    @JsonProperty("requesterPhone")
    private String requesterPhone;

    @Schema(description = "MtaTest date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonProperty("mtaTestDate")
    private Date mtaTestDate;

    @Schema(description = "Earliest available mtatest time (HH:mm)")
    @JsonProperty("readyTime")
    private String readyTime;

    @Schema(description = "Latest available mtatest time (HH:mm)")
    @JsonProperty("closeTime")
    private String closeTime;

    @Schema(description = "Shipper name")
    @JsonProperty("shipperName")
    private String shipperName;

    @Schema(description = "Shipper address")
    @JsonProperty("shipperAddress")
    private ShipperAddress shipperAddress;

    @Schema(description = "Shipment items for mtatest")
    @JsonProperty("items")
    private List<MtaTestItem> items;

    @Schema(description = "Special instructions for the driver")
    @JsonProperty("specialInstructions")
    private String specialInstructions;

    @Schema(description = "reCAPTCHA response token")
    @JsonProperty("recaptchaToken")
    private String recaptchaToken;

    @Schema(description = "Destination ZIP code for transit time estimation")
    @JsonProperty("destinationZip")
    private String destinationZip;
}
