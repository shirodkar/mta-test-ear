package com.acme.jee.rest.client;

import java.util.HashMap;
import java.util.Map;

public class RestClientHelper {

    private String baseUrl;
    private int connectTimeoutMs = 5000;
    private int readTimeoutMs = 30000;
    private final Map<String, String> defaultHeaders = new HashMap<>();

    public RestClientHelper() {}

    public RestClientHelper(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public void addDefaultHeader(String name, String value) {
        defaultHeaders.put(name, value);
    }

    public String getBaseUrl() { return baseUrl; }
    public void setBaseUrl(String baseUrl) { this.baseUrl = baseUrl; }
    public int getConnectTimeoutMs() { return connectTimeoutMs; }
    public void setConnectTimeoutMs(int connectTimeoutMs) { this.connectTimeoutMs = connectTimeoutMs; }
    public int getReadTimeoutMs() { return readTimeoutMs; }
    public void setReadTimeoutMs(int readTimeoutMs) { this.readTimeoutMs = readTimeoutMs; }
    public Map<String, String> getDefaultHeaders() { return defaultHeaders; }
}
