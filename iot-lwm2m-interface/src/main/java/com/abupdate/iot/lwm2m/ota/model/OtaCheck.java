package com.abupdate.iot.lwm2m.ota.model;

/**
 * @author wangxiaojing
 * @company adups
 * @date 2018年1月8日
 **/
public class OtaCheck {
    private String mid;
    private String version;
    private String networkType;
    private Long timestamp;
    private String sign;
    private String lac;
    private String cid;
    private String mcc;
    private String mnc;
    private String rxlev;


    public String getMid() {
        return mid;
    }


    public void setMid(String mid) {
        this.mid = mid;
    }


    public String getVersion() {
        return version;
    }


    public void setVersion(String version) {
        this.version = version;
    }


    public String getNetworkType() {
        return networkType;
    }


    public void setNetworkType(String networkType) {
        this.networkType = networkType;
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


    public String getLac() {
        return lac;
    }


    public void setLac(String lac) {
        this.lac = lac;
    }


    public String getCid() {
        return cid;
    }


    public void setCid(String cid) {
        this.cid = cid;
    }


    public String getMcc() {
        return mcc;
    }


    public void setMcc(String mcc) {
        this.mcc = mcc;
    }


    public String getMnc() {
        return mnc;
    }


    public void setMnc(String mnc) {
        this.mnc = mnc;
    }


    public String getRxlev() {
        return rxlev;
    }


    public void setRxlev(String rxlev) {
        this.rxlev = rxlev;
    }


    @Override
    public String toString() {
        return "{\"cid\":\"" + cid + "\",\"lac\":\"" + lac + "\",\"mcc\":\"" + mcc + "\",\"mid\":\"" + mid
                + "\",\"mnc\":\"" + mnc + "\",\"networkType\":\"" + networkType + "\",\"rxlev\":\"" + rxlev
                + "\",\"sign\":\"" + sign + "\",\"timestamp\":\"" + timestamp + "\",\"version\":\"" + version + "\"}";
    }

}
