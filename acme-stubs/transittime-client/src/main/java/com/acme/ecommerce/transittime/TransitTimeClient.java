package com.acme.ecommerce.transittime;

import java.util.Date;

public class TransitTimeClient {

    private String serviceUrl;

    public TransitTimeClient() {}

    public TransitTimeClient(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    public TransitTimeResult getTransitTime(String originZip, String destinationZip, Date shipDate) {
        TransitTimeResult result = new TransitTimeResult();
        result.setTransitDays(3);
        result.setOriginServiceCenter("SC-" + originZip.substring(0, 3));
        result.setDestinationServiceCenter("SC-" + destinationZip.substring(0, 3));
        return result;
    }

    public String getServiceUrl() { return serviceUrl; }
    public void setServiceUrl(String serviceUrl) { this.serviceUrl = serviceUrl; }
}
