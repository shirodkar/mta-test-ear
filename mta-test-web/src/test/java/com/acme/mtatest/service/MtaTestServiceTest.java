package com.acme.mtatest.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.acme.mtatest.exception.MtaTestException;
import com.acme.mtatest.model.MtaTestItem;
import com.acme.mtatest.model.MtaTestRequest;
import com.acme.mtatest.model.MtaTestResponse;
import com.acme.mtatest.model.ShipperAddress;
import com.acme.mtatest.model.TransitTimeResponse;

@ExtendWith(MockitoExtension.class)
class MtaTestServiceTest {

    @Mock
    private EmailService emailService;

    @Mock
    private TransitTimeService transitTimeService;

    @InjectMocks
    private MtaTestService mtaTestService;

    private MtaTestRequest validRequest;

    @BeforeEach
    void setUp() {
        mtaTestService.init();

        validRequest = MtaTestRequest.builder()
                .accountNumber("1234567")
                .requesterName("Jane Smith")
                .requesterEmail("jane@example.com")
                .mtaTestDate(new Date(System.currentTimeMillis() + 86400000))
                .readyTime("09:00")
                .closeTime("16:00")
                .shipperName("Smith Logistics")
                .shipperAddress(ShipperAddress.builder()
                        .addressLine1("456 Oak Ave")
                        .city("Charlotte")
                        .state("NC")
                        .zipCode("28202")
                        .country("US")
                        .build())
                .items(Collections.singletonList(MtaTestItem.builder()
                        .handlingUnits(1)
                        .packagingType("Cartons")
                        .weight(200.0)
                        .description("Electronics")
                        .build()))
                .destinationZip("30301")
                .build();
    }

    @Test
    void scheduleMtaTest_validRequest_returnsConfirmation() {
        TransitTimeResponse transitResponse = TransitTimeResponse.builder()
                .transitDays(2)
                .build();
        when(transitTimeService.estimateTransitTime(anyString(), anyString(), any(), any()))
                .thenReturn(transitResponse);
        doNothing().when(emailService).sendEmail(anyString(), anyString(), anyString());

        MtaTestResponse response = mtaTestService.scheduleMtaTest(validRequest);

        assertThat(response).isNotNull();
        assertThat(response.getConfirmationNumber()).startsWith("PU");
        assertThat(response.getStatus()).isEqualTo("SCHEDULED");
        assertThat(response.getProNumber()).startsWith("PRO");
        assertThat(response.getEstimatedTransitDays()).isEqualTo(2);
        assertThat(response.getEstimatedTimeWindow()).isEqualTo("09:00 - 16:00");
    }

    @Test
    void scheduleMtaTest_nullAccountNumber_throwsException() {
        validRequest.setAccountNumber(null);

        assertThatThrownBy(() -> mtaTestService.scheduleMtaTest(validRequest))
                .isInstanceOf(MtaTestException.class)
                .hasMessageContaining("Account number is required");
    }

    @Test
    void scheduleMtaTest_pastDate_throwsException() {
        validRequest.setMtaTestDate(new Date(System.currentTimeMillis() - 86400000));

        assertThatThrownBy(() -> mtaTestService.scheduleMtaTest(validRequest))
                .isInstanceOf(MtaTestException.class)
                .hasMessageContaining("MtaTest date must be in the future");
    }

    @Test
    void scheduleMtaTest_noItems_throwsException() {
        validRequest.setItems(Collections.emptyList());

        assertThatThrownBy(() -> mtaTestService.scheduleMtaTest(validRequest))
                .isInstanceOf(MtaTestException.class)
                .hasMessageContaining("At least one shipment item is required");
    }

    @Test
    void scheduleMtaTest_noAddress_throwsException() {
        validRequest.setShipperAddress(null);

        assertThatThrownBy(() -> mtaTestService.scheduleMtaTest(validRequest))
                .isInstanceOf(MtaTestException.class)
                .hasMessageContaining("Shipper address is required");
    }

    @Test
    void getMtaTestStatus_afterSchedule_returnsMtaTest() {
        when(transitTimeService.estimateTransitTime(anyString(), anyString(), any(), any()))
                .thenReturn(TransitTimeResponse.builder().transitDays(2).build());
        doNothing().when(emailService).sendEmail(anyString(), anyString(), anyString());

        MtaTestResponse scheduled = mtaTestService.scheduleMtaTest(validRequest);
        MtaTestResponse retrieved = mtaTestService.getMtaTestStatus(scheduled.getConfirmationNumber());

        assertThat(retrieved).isNotNull();
        assertThat(retrieved.getConfirmationNumber()).isEqualTo(scheduled.getConfirmationNumber());
    }

    @Test
    void cancelMtaTest_existingMtaTest_returnsCancelled() {
        when(transitTimeService.estimateTransitTime(anyString(), anyString(), any(), any()))
                .thenReturn(TransitTimeResponse.builder().transitDays(2).build());
        doNothing().when(emailService).sendEmail(anyString(), anyString(), anyString());

        MtaTestResponse scheduled = mtaTestService.scheduleMtaTest(validRequest);
        MtaTestResponse cancelled = mtaTestService.cancelMtaTest(scheduled.getConfirmationNumber());

        assertThat(cancelled.getStatus()).isEqualTo("CANCELLED");
        assertThat(cancelled.getMessage()).isEqualTo("MtaTest has been cancelled");
    }

    @Test
    void cancelMtaTest_nonExistent_throwsException() {
        assertThatThrownBy(() -> mtaTestService.cancelMtaTest("INVALID"))
                .isInstanceOf(MtaTestException.class)
                .hasMessageContaining("No mtatest found");
    }

    @Test
    void getMtaTestsByAccount_afterSchedule_returnsList() {
        when(transitTimeService.estimateTransitTime(anyString(), anyString(), any(), any()))
                .thenReturn(TransitTimeResponse.builder().transitDays(2).build());
        doNothing().when(emailService).sendEmail(anyString(), anyString(), anyString());

        mtaTestService.scheduleMtaTest(validRequest);

        List<MtaTestResponse> mtaTests = mtaTestService.getMtaTestsByAccount("1234567");
        assertThat(mtaTests).hasSize(1);
    }

    @Test
    void getMtaTestsByAccount_noMtaTests_returnsEmpty() {
        List<MtaTestResponse> mtaTests = mtaTestService.getMtaTestsByAccount("9999999");
        assertThat(mtaTests).isEmpty();
    }
}
