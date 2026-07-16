package com.acme.mtatest.webservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.Response;

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
import com.acme.mtatest.service.MtaTestService;
import com.acme.mtatest.service.RecaptchaService;

@ExtendWith(MockitoExtension.class)
class MtaTestResourceTest {

    @Mock
    private MtaTestService mtaTestService;

    @Mock
    private RecaptchaService recaptchaService;

    @InjectMocks
    private MtaTestResource mtaTestResource;

    private MtaTestRequest validRequest;

    @BeforeEach
    void setUp() {
        validRequest = MtaTestRequest.builder()
                .accountNumber("1234567")
                .requesterName("John Doe")
                .requesterEmail("john@example.com")
                .requesterPhone("555-0100")
                .mtaTestDate(new Date(System.currentTimeMillis() + 86400000))
                .readyTime("08:00")
                .closeTime("17:00")
                .shipperName("Test Shipper")
                .shipperAddress(ShipperAddress.builder()
                        .addressLine1("123 Main St")
                        .city("Thomasville")
                        .state("NC")
                        .zipCode("27360")
                        .country("US")
                        .build())
                .items(Collections.singletonList(MtaTestItem.builder()
                        .handlingUnits(2)
                        .packagingType("Pallets")
                        .weight(500.0)
                        .description("Machine parts")
                        .build()))
                .recaptchaToken("valid-token")
                .destinationZip("23320")
                .build();
    }

    @Test
    void scheduleMtaTest_returnsCreated() {
        MtaTestResponse mockResponse = MtaTestResponse.builder()
                .confirmationNumber("PU12345678")
                .status("SCHEDULED")
                .scheduledDate(validRequest.getMtaTestDate())
                .proNumber("PRO123456789")
                .build();

        doNothing().when(recaptchaService).validateToken(anyString());
        when(mtaTestService.scheduleMtaTest(any(MtaTestRequest.class))).thenReturn(mockResponse);

        Response response = mtaTestResource.scheduleMtaTest(validRequest);

        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        MtaTestResponse entity = (MtaTestResponse) response.getEntity();
        assertNotNull(entity.getConfirmationNumber());
        assertEquals("SCHEDULED", entity.getStatus());
        verify(recaptchaService).validateToken("valid-token");
    }

    @Test
    void scheduleMtaTest_recaptchaFails_throwsException() {
        doThrow(new MtaTestException("RECAPTCHA_FAILED", "reCAPTCHA verification failed"))
                .when(recaptchaService).validateToken(anyString());

        try {
            mtaTestResource.scheduleMtaTest(validRequest);
        } catch (MtaTestException e) {
            assertEquals("RECAPTCHA_FAILED", e.getErrorCode());
        }
    }

    @Test
    void getMtaTestStatus_found_returnsOk() {
        MtaTestResponse mockResponse = MtaTestResponse.builder()
                .confirmationNumber("PU12345678")
                .status("SCHEDULED")
                .build();

        when(mtaTestService.getMtaTestStatus("PU12345678")).thenReturn(mockResponse);

        Response response = mtaTestResource.getMtaTestStatus("PU12345678");

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    void getMtaTestStatus_notFound_throwsException() {
        when(mtaTestService.getMtaTestStatus("INVALID")).thenReturn(null);

        try {
            mtaTestResource.getMtaTestStatus("INVALID");
        } catch (MtaTestException e) {
            assertEquals("MTA_TEST_NOT_FOUND", e.getErrorCode());
        }
    }

    @Test
    void cancelMtaTest_returnsOk() {
        MtaTestResponse mockResponse = MtaTestResponse.builder()
                .confirmationNumber("PU12345678")
                .status("CANCELLED")
                .message("MtaTest has been cancelled")
                .build();

        when(mtaTestService.cancelMtaTest("PU12345678")).thenReturn(mockResponse);

        Response response = mtaTestResource.cancelMtaTest("PU12345678");

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        MtaTestResponse entity = (MtaTestResponse) response.getEntity();
        assertEquals("CANCELLED", entity.getStatus());
    }

    @Test
    void getMtaTestsByAccount_returnsOk() {
        List<MtaTestResponse> mockList = Arrays.asList(
                MtaTestResponse.builder().confirmationNumber("PU001").status("SCHEDULED").build(),
                MtaTestResponse.builder().confirmationNumber("PU002").status("COMPLETED").build());

        when(mtaTestService.getMtaTestsByAccount("1234567")).thenReturn(mockList);

        Response response = mtaTestResource.getMtaTestsByAccount("1234567");

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }
}
