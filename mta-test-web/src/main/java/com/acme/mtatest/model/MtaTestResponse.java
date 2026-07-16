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
@Schema(description = "MtaTest scheduling response")
public class MtaTestResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "MtaTest confirmation number")
    @JsonProperty("confirmationNumber")
    private String confirmationNumber;

    @Schema(description = "Status of the mtatest request")
    @JsonProperty("status")
    private String status;

    @Schema(description = "Scheduled mtatest date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonProperty("scheduledDate")
    private Date scheduledDate;

    @Schema(description = "Estimated mtatest time window")
    @JsonProperty("estimatedTimeWindow")
    private String estimatedTimeWindow;

    @Schema(description = "PRO number assigned to the shipment")
    @JsonProperty("proNumber")
    private String proNumber;

    @Schema(description = "Service center handling the mtatest")
    @JsonProperty("serviceCenterId")
    private String serviceCenterId;

    @Schema(description = "Response message")
    @JsonProperty("message")
    private String message;

    @Schema(description = "Estimated transit days to destination")
    @JsonProperty("estimatedTransitDays")
    private Integer estimatedTransitDays;
}
