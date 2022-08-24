package com.demo.redis.bean;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DBUserContact {

    private long userID;
    private byte type;
    private String contactInfo;
    private byte status;
    private long SDKID;
    private String mark;
    private String contactMark;

    @JsonProperty("SDKID")
    public long getSDKID() {
        return SDKID;
    }

    public void setSDKID(long sDKID) {
        SDKID = sDKID;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }


    public String getContactMark() {
        return contactMark;
    }

    public void setContactMark(String contactMark) {
        this.contactMark = contactMark;
    }

    public DBUserContact() {
    }

    public DBUserContact(Long userID, byte type, String contactInfo, byte status, long sDKID, String mark) {
        super();
        this.userID = userID;
        this.type = type;
        this.contactInfo = contactInfo;
        this.status = status;
        SDKID = sDKID;
        this.mark = mark;
    }

    public DBUserContact(Long userID, byte type, String contactInfo, byte status, long sDKID, String mark, String contactMark) {
        super();
        this.userID = userID;
        this.type = type;
        this.contactInfo = contactInfo;
        this.status = status;
        SDKID = sDKID;
        this.mark = mark;
        this.contactMark = contactMark;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("DBUserContact [userID=");
        builder.append(userID);
        builder.append(", type=");
        builder.append(type);
        builder.append(", contactInfo=");
        builder.append(contactInfo);
        builder.append(", contactMark=");
        builder.append(contactMark);
        builder.append(", status=");
        builder.append(status);
        builder.append(", SDKID=");
        builder.append(SDKID);
        builder.append(", mark=");
        builder.append(mark);
        builder.append("]");
        return builder.toString();
    }
}
