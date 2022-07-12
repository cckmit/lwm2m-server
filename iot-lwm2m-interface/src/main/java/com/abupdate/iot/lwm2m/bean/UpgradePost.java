package com.abupdate.iot.lwm2m.bean;

/**
 * @author wangxiaojing
 * @company adups
 * @date 2018年4月12日
 **/
public class UpgradePost {
    private Long deltaID;
    private int updateStatus;
    private Long timestamp;
    private String sign;
    private String extStr;
    private Long productId;
    private String deviceId;
    private String ip;
    private String mid;

    public Long getDeltaID() {
        return deltaID;
    }

    public void setDeltaID(Long deltaID) {
        this.deltaID = deltaID;
    }

    public int getUpdateStatus() {
        return updateStatus;
    }

    public void setUpdateStatus(int updateStatus) {
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

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    @Override
    public String toString() {
        return "{\"deltaID\":\"" + deltaID + "\",\"updateStatus\":\"" + updateStatus + "\",\"timestamp\":\"" + timestamp
                + "\",\"sign\":\"" + sign + "\",\"extStr\":\"" + extStr + "\",\"productId\":\"" + productId
                + "\",\"deviceId\":\"" + deviceId + "\",\"ip\":\"" + ip + "\",\"mid\":\"" + mid + "\"}";
    }
}
