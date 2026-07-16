package com.acme.ecommerce.transittime;

import java.io.Serializable;
import java.util.Date;

public class TransitTimeResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private int transitDays;
    private Date estimatedDeliveryDate;
    private String originServiceCenter;
    private String destinationServiceCenter;
    private String serviceType;

    public int getTransitDays() { return transitDays; }
    public void setTransitDays(int transitDays) { this.transitDays = transitDays; }
    public Date getEstimatedDeliveryDate() { return estimatedDeliveryDate; }
    public void setEstimatedDeliveryDate(Date estimatedDeliveryDate) { this.estimatedDeliveryDate = estimatedDeliveryDate; }
    public String getOriginServiceCenter() { return originServiceCenter; }
    public void setOriginServiceCenter(String originServiceCenter) { this.originServiceCenter = originServiceCenter; }
    public String getDestinationServiceCenter() { return destinationServiceCenter; }
    public void setDestinationServiceCenter(String destinationServiceCenter) { this.destinationServiceCenter = destinationServiceCenter; }
    public String getServiceType() { return serviceType; }
    public void setServiceType(String serviceType) { this.serviceType = serviceType; }
}
