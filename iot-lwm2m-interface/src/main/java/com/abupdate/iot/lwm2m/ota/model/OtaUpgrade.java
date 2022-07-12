package com.abupdate.iot.lwm2m.ota.model;

/**
 * @author wangxiaojing
 * @company adups
 * @date 2018年1月9日
 **/
public class OtaUpgrade {
    private String mid;
    private String deltaID;
    private String updateStatus;
    private Long timestamp;
    private String sign;
    private String extStr;

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getDeltaID() {
        return deltaID;
    }

    public void setDeltaID(String deltaID) {
        this.deltaID = deltaID;
    }

    public String getUpdateStatus() {
        return updateStatus;
    }

    public void setUpdateStatus(String updateStatus) {
        this.updateStatus = updateStatus;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getExtStr() {
        return extStr;
    }

    public void setExtStr(String extStr) {
        this.extStr = extStr;
    }

    @Override
    public String toString() {
        return "{\"mid\":\"" + mid + "\",\"deltaID\":\"" + deltaID + "\",\"updateStatus\":\"" + updateStatus
                + "\",\"timestamp\":\"" + timestamp + "\",\"sign\":\"" + sign + "\",\"extStr\":\"" + extStr + "\"}";
    }
}
