package com.abupdate.iot.lwm2m.ota.model;

/**
 * @author wangxiaojing
 * @company adups
 * @date 2018年1月8日
 **/
public class OtaResultRegister {
    private String deviceId;
    private String deviceSecret;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceSecret() {
        return deviceSecret;
    }

    public void setDeviceSecret(String deviceSecret) {
        this.deviceSecret = deviceSecret;
    }

}
