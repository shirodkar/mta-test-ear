package com.acme.core.persistence;

public class PersistenceConfig {

    private String dataSourceJndi;
    private String persistenceUnit;

    public String getDataSourceJndi() { return dataSourceJndi; }
    public void setDataSourceJndi(String dataSourceJndi) { this.dataSourceJndi = dataSourceJndi; }
    public String getPersistenceUnit() { return persistenceUnit; }
    public void setPersistenceUnit(String persistenceUnit) { this.persistenceUnit = persistenceUnit; }
}
