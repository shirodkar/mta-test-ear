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
@Schema(description = "Transit time estimation request")
public class TransitTimeRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "Origin ZIP code", required = true)
    @JsonProperty("originZip")
    private String originZip;

    @Schema(description = "Destination ZIP code", required = true)
    @JsonProperty("destinationZip")
    private String destinationZip;

    @Schema(description = "Shipment date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonProperty("shipDate")
    private Date shipDate;

    @Schema(description = "Total shipment weight in pounds")
    @JsonProperty("weight")
    private Double weight;
}
