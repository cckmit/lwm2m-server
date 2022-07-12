package com.abupdate.iot.lwm2m.util;

/**
 * @author wangxiaojing
 * @company adups
 * @date 2017年12月21日
 **/
public class ReadResources {
    private int id;
    private String value;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "{\"id\":\"" + id + "\",\"value\":\"" + value + "\"}";
    }
}
