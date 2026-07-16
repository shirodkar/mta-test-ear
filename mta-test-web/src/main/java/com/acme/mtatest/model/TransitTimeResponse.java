package com.acme.mtatest.model;

import java.io.Serializable;
import java.util.Date;

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
@Schema(description = "Transit time estimation response")
public class TransitTimeResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "Estimated transit days")
    @JsonProperty("transitDays")
    private int transitDays;

    @Schema(description = "Estimated delivery date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonProperty("estimatedDeliveryDate")
    private Date estimatedDeliveryDate;

    @Schema(description = "Origin service center ID")
    @JsonProperty("originServiceCenter")
    private String originServiceCenter;

    @Schema(description = "Destination service center ID")
    @JsonProperty("destinationServiceCenter")
    private String destinationServiceCenter;

    @Schema(description = "Service type")
    @JsonProperty("serviceType")
    private String serviceType;
}
