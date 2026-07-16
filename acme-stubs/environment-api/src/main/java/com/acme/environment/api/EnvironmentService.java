package com.acme.environment.api;

public interface EnvironmentService {

    String getEnvironmentName();

    String getProperty(String key);

    String getProperty(String key, String defaultValue);

    boolean isProduction();

    boolean isDevelopment();
}
