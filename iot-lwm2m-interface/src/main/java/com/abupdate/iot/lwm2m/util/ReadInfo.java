package com.abupdate.iot.lwm2m.util;

/**
 * @author wangxiaojing
 * @company adups
 * @date 2017年12月21日
 **/
public class ReadInfo {
    private ReadContent content;
    private String status;

    public ReadContent getContent() {
        return content;
    }

    public void setContent(ReadContent content) {
        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "{\"content\":\"" + content + "\",\"status\":\"" + status + "\"}";
    }

}
