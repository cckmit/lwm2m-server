package com.abupdate.iot.lwm2m.ota.model;

/**
 * @author wangxiaojing
 * @company adups
 * @date 2018年1月9日
 **/
public class OtaDownload {
    private String mid;
    private Long deltaID;
    private String downloadStatus;
    private Long downStart;
    private Long downEnd;
    private Integer downSize;
    private String downIp;
    private Long timestamp;
    private String sign;
    private String extStr;

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public Long getDeltaID() {
        return deltaID;
    }

    public void setDeltaID(Long deltaID) {
        this.deltaID = deltaID;
    }

    public String getDownloadStatus() {
        return downloadStatus;
    }

    public void setDownloadStatus(String downloadStatus) {
        this.downloadStatus = downloadStatus;
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

    public Integer getDownSize() {
        return downSize;
    }

    public void setDownSize(Integer downSize) {
        this.downSize = downSize;
    }

    public String getDownIp() {
        return downIp;
    }

    public void setDownIp(String downIp) {
        this.downIp = downIp;
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
        return "{\"mid\":\"" + mid + "\",\"deltaID\":\"" + deltaID + "\",\"downloadStatus\":\"" + downloadStatus
                + "\",\"downStart\":\"" + downStart + "\",\"downEnd\":\"" + downEnd + "\",\"downSize\":\"" + downSize
                + "\",\"downIp\":\"" + downIp + "\",\"timestamp\":\"" + timestamp + "\",\"sign\":\"" + sign
                + "\",\"extStr\":\"" + extStr + "\"}";
    }

}
