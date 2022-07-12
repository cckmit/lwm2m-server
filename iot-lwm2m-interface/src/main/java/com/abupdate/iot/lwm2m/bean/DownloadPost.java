package com.abupdate.iot.lwm2m.bean;

/**
 * @author wangxiaojing
 * @company adups
 * @date 2018年4月12日
 **/
public class DownloadPost {
    private Long deltaID;
    private Long downStart;
    private Long downEnd;
    private Long downSize;
    private String downIp;
    private Integer state;
    private String extStr;
    private Long timestamp;
    private String sign;
    private Long productId;
    private String deviceId;
    private String ip;
    private String mid;
    private String downloadStatus;

    public Long getDeltaID() {
        return deltaID;
    }

    public void setDeltaID(Long deltaID) {
        this.deltaID = deltaID;
    }

    public Long getDownStart() {
        return downStart;
    }

    public void setDownStart(Long downStart) {
        this.downStart = downStart;
    }

    public Long getDownEnd() {
        return downEnd;
    }

    public void setDownEnd(Long downEnd) {
        this.downEnd = downEnd;
    }

    public Long getDownSize() {
        return downSize;
    }

    public void setDownSize(Long downSize) {
        this.downSize = downSize;
    }

    public String getDownIp() {
        return downIp;
    }

    public void setDownIp(String downIp) {
        this.downIp = downIp;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getExtStr() {
        return extStr;
    }

    public void setExtStr(String extStr) {
        this.extStr = extStr;
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

    public String getDownloadStatus() {
        return downloadStatus;
    }

    public void setDownloadStatus(String downloadStatus) {
        this.downloadStatus = downloadStatus;
    }

    @Override
    public String toString() {
        return "{\"deltaID\":\"" + deltaID + "\",\"downStart\":\"" + downStart + "\",\"downEnd\":\"" + downEnd
                + "\",\"downSize\":\"" + downSize + "\",\"downIp\":\"" + downIp + "\",\"state\":\"" + state
                + "\",\"extStr\":\"" + extStr + "\",\"timestamp\":\"" + timestamp + "\",\"sign\":\"" + sign
                + "\",\"productId\":\"" + productId + "\",\"deviceId\":\"" + deviceId + "\",\"ip\":\"" + ip
                + "\",\"mid\":\"" + mid + "\",\"downloadStatus\":\"" + downloadStatus + "\"}";
    }
}
