package com.abupdate.iot.lwm2m.bean;

/**
 * @author abupdate
 * @date 2019/7/8.
 * @title
 */
public class Auth {
    private String mid;
    private Integer productId;
    private Integer version;
    private String ip;

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
