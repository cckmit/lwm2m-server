package com.abupdate.iot.lwm2m.bean;

/**
 * @author wangxiaojing
 * @company adups
 * @date 2017年12月29日
 **/
public class UriQuery {
    private String lwm2m;
    private String ep;
    private String b;
    private Long lt;
    private Long productId;
    private String sms;
    private String deviceId;
    private String op;
    private Long timestamp;
    private String sign;
    private Integer v;

    public String getLwm2m() {
        return lwm2m;
    }

    public void setLwm2m(String lwm2m) {
        this.lwm2m = lwm2m;
    }

    public String getEp() {
        return ep;
    }

    public void setEp(String ep) {
        this.ep = ep;
    }

    public String getB() {
        return b;
    }

    public void setB(String b) {
        this.b = b;
    }

    public Long getLt() {
        return lt;
    }

    public void setLt(Long lt) {
        this.lt = lt;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getSms() {
        return sms;
    }

    public void setSms(String sms) {
        this.sms = sms;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getOp() {
        return op;
    }

    public void setOp(String op) {
        this.op = op;
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

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"lwm2m\":\"")
                .append(lwm2m).append('\"');
        sb.append(",\"ep\":\"")
                .append(ep).append('\"');
        sb.append(",\"b\":\"")
                .append(b).append('\"');
        sb.append(",\"lt\":")
                .append(lt);
        sb.append(",\"productId\":")
                .append(productId);
        sb.append(",\"sms\":\"")
                .append(sms).append('\"');
        sb.append(",\"deviceId\":\"")
                .append(deviceId).append('\"');
        sb.append(",\"op\":\"")
                .append(op).append('\"');
        sb.append(",\"timestamp\":")
                .append(timestamp);
        sb.append(",\"sign\":\"")
                .append(sign).append('\"');
        sb.append(",\"v\":")
                .append(v);
        sb.append('}');
        return sb.toString();
    }
}
