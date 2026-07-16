package com.acme.freight.mtatest.ws;

public class MtaTestWebServiceClient {

    private String wsdlUrl;
    private String endpointUrl;

    public MtaTestWebServiceClient() {}

    public MtaTestWebServiceClient(String endpointUrl) {
        this.endpointUrl = endpointUrl;
    }

    public String getWsdlUrl() { return wsdlUrl; }
    public void setWsdlUrl(String wsdlUrl) { this.wsdlUrl = wsdlUrl; }
    public String getEndpointUrl() { return endpointUrl; }
    public void setEndpointUrl(String endpointUrl) { this.endpointUrl = endpointUrl; }
}
