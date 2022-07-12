package com.abupdate.iot.lwm2m.bean;

/**
 * @author wangxiaojing
 * @company adups
 * @date 2018年4月11日
 **/
public class RegisterCheckPost {
    private String oem;
    private String models;
    private String platform;
    private String deviceType;
    private String timestamp;
    private String sign;
    private String sdkversion;
    private String appversion;
    private String version;
    private String networkType = "-";
    private String mac;
    private String ip;
    private String mid;
    private String productId;
    private String deviceId;

    public String getOem() {
        return oem;
    }

    public void setOem(String oem) {
        this.oem = oem;
    }

    public String getModels() {
        return models;
    }

    public void setModels(String models) {
        this.models = models;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getSdkversion() {
        return sdkversion;
    }

    public void setSdkversion(String sdkversion) {
        this.sdkversion = sdkversion;
    }

    public String getAppversion() {
        return appversion;
    }

    public void setAppversion(String appversion) {
        this.appversion = appversion;
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

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
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

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    @Override
    public String toString() {
        return "{\"oem\":\"" + oem + "\",\"models\":\"" + models + "\",\"platform\":\"" + platform
                + "\",\"deviceType\":\"" + deviceType + "\",\"timestamp\":\"" + timestamp + "\",\"sign\":\"" + sign
                + "\",\"sdkversion\":\"" + sdkversion + "\",\"appversion\":\"" + appversion + "\",\"version\":\""
                + version + "\",\"networkType\":\"" + networkType + "\",\"mac\":\"" + mac + "\",\"ip\":\"" + ip
                + "\",\"mid\":\"" + mid + "\",\"productId\":\"" + productId + "\",\"deviceId\":\"" + deviceId + "\"}";
    }

}
