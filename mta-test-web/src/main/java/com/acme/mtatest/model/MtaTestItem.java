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
@Schema(description = "Individual shipment item details")
public class MtaTestItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "Number of handling units")
    @JsonProperty("handlingUnits")
    private int handlingUnits;

    @Schema(description = "Packaging type (e.g., Pallets, Cartons, Crates)")
    @JsonProperty("packagingType")
    private String packagingType;

    @Schema(description = "Total weight in pounds")
    @JsonProperty("weight")
    private double weight;

    @Schema(description = "Commodity description")
    @JsonProperty("description")
    private String description;

    @Schema(description = "Length in inches")
    @JsonProperty("length")
    private Double length;

    @Schema(description = "Width in inches")
    @JsonProperty("width")
    private Double width;

    @Schema(description = "Height in inches")
    @JsonProperty("height")
    private Double height;

    @Schema(description = "Whether the item is hazardous material")
    @JsonProperty("hazmat")
    private boolean hazmat;
}
