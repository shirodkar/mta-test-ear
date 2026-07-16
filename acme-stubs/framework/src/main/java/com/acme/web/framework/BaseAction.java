package com.acme.web.framework;

import java.io.Serializable;

public abstract class BaseAction implements Serializable {

    private static final long serialVersionUID = 1L;

    private String actionName;
    private boolean authenticated;

    public String getActionName() { return actionName; }
    public void setActionName(String actionName) { this.actionName = actionName; }
    public boolean isAuthenticated() { return authenticated; }
    public void setAuthenticated(boolean authenticated) { this.authenticated = authenticated; }
}
