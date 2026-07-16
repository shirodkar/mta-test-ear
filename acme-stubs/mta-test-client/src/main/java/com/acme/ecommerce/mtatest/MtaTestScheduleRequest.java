package com.acme.ecommerce.mtatest;

import java.io.Serializable;
import java.util.Date;

public class MtaTestScheduleRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private String accountNumber;
    private String requesterName;
    private String originZip;
    private Date mtaTestDate;
    private String readyTime;
    private String closeTime;

    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    public String getRequesterName() { return requesterName; }
    public void setRequesterName(String requesterName) { this.requesterName = requesterName; }
    public String getOriginZip() { return originZip; }
    public void setOriginZip(String originZip) { this.originZip = originZip; }
    public Date getMtaTestDate() { return mtaTestDate; }
    public void setMtaTestDate(Date mtaTestDate) { this.mtaTestDate = mtaTestDate; }
    public String getReadyTime() { return readyTime; }
    public void setReadyTime(String readyTime) { this.readyTime = readyTime; }
    public String getCloseTime() { return closeTime; }
    public void setCloseTime(String closeTime) { this.closeTime = closeTime; }
}
