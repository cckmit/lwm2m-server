package com.abupdate.iot.lwm2m.bean;

import org.apache.commons.lang.StringUtils;

/**
 * @author wangxiaojing
 * @company adups
 * @date 2018年1月4日
 **/
public class UpdateNode {
    public static final int PACKAGE = 0;//package
    public static final int PACKAGEURI = 1;//packageURI
    public static final int UPDATE = 2;//update
    public static final int STATE = 3;//state
    public static final int UPDATERESULT = 5;//updateResult
    public static final int PKGNAME = 6;//pkgName
    public static final int PKGVERSION = 7;//pkgVersion
    public static final int FIRMWAREUPDATEPROTOCOLSUPPORT = 8;//firmwareUpdateProtocolSupport
    public static final int FIRMWAREUPDATEDELIVERYMETHOD = 9;//firmwareUpdateDeliveryMethod
    public static final int FILESIZE = 100;//fileSize
    public static final int DELTAID = 101;//deltaID
    public static final int MD5SUM = 102;//md5sum
    public static final int DOWNSTART = 103;//downStart
    public static final int DOWNEND = 104;//downEnd
    public static final int DOWNSIZE = 105;//downSize
    public static final int DOWNIP = 106;//downIp
    public static final int ERRORCODE = 107;//errorCode
    public static final int STATUS = 200;//status

    public static final int MID = 1001;//mid
    public static final int IP = 1002;//ip
    public static final int PRODUCTID = 1003;//productId
    public static final int DEVICEID = 1004;//deviceId


    public static String checkString(String value) {
        if (StringUtils.isEmpty(value)) {
            return null;
        } else {
            return value;
        }
    }

    public static Integer checkInteger(String value) {
        if (StringUtils.isEmpty(value)) {
            return null;
        } else {
            return Integer.parseInt(value);
        }
    }

    public static Long checkLong(String value) {
        if (StringUtils.isEmpty(value)) {
            return null;
        } else {
            return Long.parseLong(value);
        }
    }
}
