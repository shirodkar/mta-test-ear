package com.acme.environment.client;

import com.acme.environment.api.EnvironmentService;

public class EnvironmentClientImpl implements EnvironmentService {

    private static final String DEFAULT_ENV = "DEVELOPMENT";

    @Override
    public String getEnvironmentName() {
        String env = System.getProperty("acme.environment", DEFAULT_ENV);
        return env;
    }

    @Override
    public String getProperty(String key) {
        return getProperty(key, null);
    }

    @Override
    public String getProperty(String key, String defaultValue) {
        String value = System.getProperty("acme." + key);
        return value != null ? value : defaultValue;
    }

    @Override
    public boolean isProduction() {
        return "PRODUCTION".equalsIgnoreCase(getEnvironmentName());
    }

    @Override
    public boolean isDevelopment() {
        return "DEVELOPMENT".equalsIgnoreCase(getEnvironmentName());
    }
}
