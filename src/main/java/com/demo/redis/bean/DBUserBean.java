package com.demo.redis.bean;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class DBUserBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private long userID;
    private String name;
    private String pwd;
    private Byte sex;
    private String area;
    private String birthday;
    private byte status;
    private String portraitURL;
    private String sign;
    private byte timeZone;
    private String oriPortraitURL;
    private String extend;
    private long lockDeadline;
    private long remainLockDuration;
    private long SDKID;
    private String entExtend;
    private long regTime;
    private byte pwdStrength;
    private String orgID;
    private long roleID;
    private byte bizStatus;/*业务状态，1：初始默认，2：在高管层*/
    private List<DBUserContact> accounts;
    private byte uploadFlag;/*上传标识, 1:未上传,2:已上传*/
    //用以保存冻结之前用户的status字段的值
    private byte oldStatus;
    // 密码更新时间
    private long pwdUpdateTime;
    /**
     * 工作密级
     */
    private Integer securityLevel;

    public Integer getSecurityLevel() {
        return securityLevel;
    }

    public void setSecurityLevel(Integer securityLevel) {
        this.securityLevel = securityLevel;
    }

    /**
     * 真实姓名
     */
    private String realname;

    /**
     * 修订版本号
     */
    private long changeVersion;
    /**
     * 第三方导入唯一标识
     */
    private String ImportFlag;

    /**
     * 用户标签IDS
     */
    private String tagIds;

    /**
     * 第三方用户唯一标识
     */
    private String externalId;

    /**
     * 是否发送弱提示 默认为true，即默认发送若弱提示
     */
    private Boolean isSendTip;

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public byte getOldStatus() {
        return oldStatus;
    }

    public void setOldStatus(byte oldStatus) {
        this.oldStatus = oldStatus;
    }

    public List<DBUserContact> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<DBUserContact> accounts) {
        this.accounts = accounts;
    }

    public long getRegTime() {
        return regTime;
    }

    public void setRegTime(long regTime) {
        this.regTime = regTime;
    }

    @JsonProperty("SDKID")
    public long getSDKID() {
        return SDKID;
    }

    @JsonProperty("SDKID")
    public void setSDKID(long sDKID) {
        SDKID = sDKID;
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getPortraitURL() {
        return portraitURL;
    }

    public void setPortraitURL(String portraitURL) {
        this.portraitURL = portraitURL;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public Byte getSex() {
        return sex;
    }

    public void setSex(Byte sex) {
        this.sex = sex;
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(byte timeZone) {
        this.timeZone = timeZone;
    }

    public String getOriPortraitURL() {
        return oriPortraitURL;
    }

    public void setOriPortraitURL(String oriPortraitURL) {
        this.oriPortraitURL = oriPortraitURL;
    }

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }

    public long getLockDeadline() {
        return lockDeadline;
    }

    public void setLockDeadline(long lockDeadline) {
        this.lockDeadline = lockDeadline;
    }

    public long getRemainLockDuration() {
        return remainLockDuration;
    }

    public void setRemainLockDuration(long remainLockDuration) {
        this.remainLockDuration = remainLockDuration;
    }

    public String getEntExtend() {
        return entExtend;
    }

    public void setEntExtend(String entExtend) {
        this.entExtend = entExtend;
    }

    public byte getPwdStrength() {
        return pwdStrength;
    }

    public void setPwdStrength(byte pwdStrength) {
        this.pwdStrength = pwdStrength;
    }

    public String getOrgID() {
        return orgID;
    }

    public void setOrgID(String orgID) {
        this.orgID = orgID;
    }

    public long getRoleID() {
        return roleID;
    }

    public void setRoleID(long roleID) {
        this.roleID = roleID;
    }

    public byte getBizStatus() {
        return bizStatus;
    }

    public void setBizStatus(byte bizStatus) {
        this.bizStatus = bizStatus;
    }

    public byte getUploadFlag() {
        return uploadFlag;
    }

    public void setUploadFlag(byte uploadFlag) {
        this.uploadFlag = uploadFlag;
    }

    public long getChangeVersion() {
        return changeVersion;
    }

    public void setChangeVersion(long changeVersion) {
        this.changeVersion = changeVersion;
    }

    public String getImportFlag() {
        return ImportFlag;
    }

    public void setImportFlag(String importFlag) {
        ImportFlag = importFlag;
    }

    public String getTagIds() {
        return tagIds;
    }

    public void setTagIds(String tagIds) {
        this.tagIds = tagIds;
    }


    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public long getPwdUpdateTime() {
        return pwdUpdateTime;
    }

    public void setPwdUpdateTime(long pwdUpdateTime) {
        this.pwdUpdateTime = pwdUpdateTime;
    }

    public boolean isSendTip() {
        if (isSendTip == null) {
            return true;
        } else {
            return isSendTip;
        }
    }

    public DBUserBean setSendTip(boolean sendTip) {
        isSendTip = sendTip;
        return this;
    }

    @Override
    public String toString() {
        return "DBUserBean{" +
                "userID=" + userID +
                ", name='" + name + '\'' +
                ", pwd='" + pwd + '\'' +
                ", sex=" + sex +
                ", area='" + area + '\'' +
                ", birthday='" + birthday + '\'' +
                ", status=" + status +
                ", portraitURL='" + portraitURL + '\'' +
                ", sign='" + sign + '\'' +
                ", timeZone=" + timeZone +
                ", oriPortraitURL='" + oriPortraitURL + '\'' +
                ", extend='" + extend + '\'' +
                ", lockDeadline=" + lockDeadline +
                ", remainLockDuration=" + remainLockDuration +
                ", SDKID=" + SDKID +
                ", entExtend='" + entExtend + '\'' +
                ", regTime=" + regTime +
                ", pwdStrength=" + pwdStrength +
                ", orgID='" + orgID + '\'' +
                ", roleID=" + roleID +
                ", bizStatus=" + bizStatus +
                ", accounts=" + accounts +
                ", uploadFlag=" + uploadFlag +
                ", oldStatus=" + oldStatus +
                ", securityLevel=" + securityLevel +
                ", realname='" + realname + '\'' +
                ", changeVersion=" + changeVersion +
                ", ImportFlag='" + ImportFlag + '\'' +
                ", tagIds='" + tagIds + '\'' +
                ", externalId='" + externalId + '\'' +
                ", isSendTip=" + isSendTip + '\'' +
                ", pwdUpdateTime=" + pwdUpdateTime +
                '}';
    }
}
