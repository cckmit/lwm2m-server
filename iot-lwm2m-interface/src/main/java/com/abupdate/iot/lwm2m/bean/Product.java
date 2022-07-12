package com.abupdate.iot.lwm2m.bean;

public class Product {
    private String productId;
    private String productSecret;
    private String productCode;
    private Integer is_strategy;
    private Integer num;
    private String systemOs;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public Integer getIs_strategy() {
        return is_strategy;
    }

    public void setIs_strategy(Integer is_strategy) {
        this.is_strategy = is_strategy;
    }

    public String getProductSecret() {
        return productSecret;
    }

    public void setProductSecret(String productSecret) {
        this.productSecret = productSecret;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getSystemOs() {
        return systemOs;
    }

    public void setSystemOs(String systemOs) {
        this.systemOs = systemOs;
    }
}
