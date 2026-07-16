package com.acme.core.dto;

import java.io.Serializable;

public abstract class BaseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private long createdTimestamp;
    private long updatedTimestamp;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public long getCreatedTimestamp() { return createdTimestamp; }
    public void setCreatedTimestamp(long createdTimestamp) { this.createdTimestamp = createdTimestamp; }
    public long getUpdatedTimestamp() { return updatedTimestamp; }
    public void setUpdatedTimestamp(long updatedTimestamp) { this.updatedTimestamp = updatedTimestamp; }
}
