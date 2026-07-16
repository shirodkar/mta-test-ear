package com.acme.mtatest.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Calendar;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.acme.mtatest.exception.MtaTestException;
import com.acme.mtatest.model.TransitTimeResponse;

class TransitTimeServiceTest {

    private TransitTimeService transitTimeService;

    @BeforeEach
    void setUp() {
        transitTimeService = new TransitTimeService();
    }

    @Test
    void estimateTransitTime_sameRegion_returns1Day() {
        TransitTimeResponse response = transitTimeService.estimateTransitTime(
                "27360", "28202", new Date(), 500.0);

        assertThat(response.getTransitDays()).isEqualTo(1);
        assertThat(response.getServiceType()).isEqualTo("PRIORITY");
    }

    @Test
    void estimateTransitTime_adjacentRegion_returns2Days() {
        TransitTimeResponse response = transitTimeService.estimateTransitTime(
                "27360", "30301", new Date(), 500.0);

        assertThat(response.getTransitDays()).isEqualTo(2);
        assertThat(response.getServiceType()).isEqualTo("PRIORITY");
    }

    @Test
    void estimateTransitTime_distantRegion_returns3OrMoreDays() {
        TransitTimeResponse response = transitTimeService.estimateTransitTime(
                "10001", "90210", new Date(), 500.0);

        assertThat(response.getTransitDays()).isGreaterThanOrEqualTo(3);
        assertThat(response.getServiceType()).isEqualTo("STANDARD");
    }

    @Test
    void estimateTransitTime_nullOriginZip_throwsException() {
        assertThatThrownBy(() -> transitTimeService.estimateTransitTime(
                null, "27360", new Date(), 500.0))
                .isInstanceOf(MtaTestException.class)
                .hasMessageContaining("Origin ZIP code is required");
    }

    @Test
    void estimateTransitTime_nullDestZip_throwsException() {
        assertThatThrownBy(() -> transitTimeService.estimateTransitTime(
                "27360", null, new Date(), 500.0))
                .isInstanceOf(MtaTestException.class)
                .hasMessageContaining("Destination ZIP code is required");
    }

    @Test
    void estimateTransitTime_deliverySkipsWeekends() {
        Calendar friday = Calendar.getInstance();
        friday.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);

        TransitTimeResponse response = transitTimeService.estimateTransitTime(
                "27360", "30301", friday.getTime(), 500.0);

        assertThat(response.getEstimatedDeliveryDate()).isNotNull();
        Calendar deliveryDay = Calendar.getInstance();
        deliveryDay.setTime(response.getEstimatedDeliveryDate());
        int dayOfWeek = deliveryDay.get(Calendar.DAY_OF_WEEK);
        assertThat(dayOfWeek).isNotIn(Calendar.SATURDAY, Calendar.SUNDAY);
    }

    @Test
    void estimateTransitTime_setsServiceCenters() {
        TransitTimeResponse response = transitTimeService.estimateTransitTime(
                "27360", "30301", new Date(), null);

        assertThat(response.getOriginServiceCenter()).isEqualTo("SC-273");
        assertThat(response.getDestinationServiceCenter()).isEqualTo("SC-303");
    }
}
