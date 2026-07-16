package com.acme.core.runtime;

import java.util.HashMap;
import java.util.Map;

public class RuntimeContext {

    private static final RuntimeContext INSTANCE = new RuntimeContext();

    private final Map<String, Object> attributes = new HashMap<>();
    private String applicationName;
    private String serverName;

    private RuntimeContext() {}

    public static RuntimeContext getInstance() {
        return INSTANCE;
    }

    public void setAttribute(String key, Object value) {
        attributes.put(key, value);
    }

    @SuppressWarnings("unchecked")
    public <T> T getAttribute(String key) {
        return (T) attributes.get(key);
    }

    public String getApplicationName() { return applicationName; }
    public void setApplicationName(String applicationName) { this.applicationName = applicationName; }
    public String getServerName() { return serverName; }
    public void setServerName(String serverName) { this.serverName = serverName; }
}
