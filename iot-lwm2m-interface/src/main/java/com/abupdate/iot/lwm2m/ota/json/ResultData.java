package com.abupdate.iot.lwm2m.ota.json;

public class ResultData {
    private int status;// 状态编码code
    private String msg;// 状态对应值
    private Object data;// 返回数据

    public ResultData(int status, String msg, Object data) {
        super();
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
