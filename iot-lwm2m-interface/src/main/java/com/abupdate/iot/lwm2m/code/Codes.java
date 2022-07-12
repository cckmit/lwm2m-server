package com.abupdate.iot.lwm2m.code;

/**
 * @author wangxiaojing
 * @company adups
 * @date 2017年12月22日
 **/
public class Codes {
    public static final String CONTENTFORMATPARAM = "TLV";

    public static final String UP_TARGET = "/5/0";
    public static final String EXECUTE_TARGET = "/5/0/2";
    public static final String UP_TARGET_URL = "/5/0/1";
    public static final String UP_TARGET_SUCCESS = "/5/0/200";

    public static final String READ_TARGET = "/3/0";
    public static final String DEVICE_WRITE_TARGET_SUCCESS = "/3/0/200";

    /**
     * WriteRequest result success
     */
    public static final String WRITE_RESULT = "CHANGED";

    public static final int DOWN_SUCCESS_VALUE = 1;

    public static final String UP_RESOURCEID = "5";
    public static final String UP_SUCCESS_VALUE = "1";
}
