package com.abupdate.iot.lwm2m.bean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

/**
 * @author wangxiaojing
 * @company adups
 * @date 2018年1月4日
 **/
public class DeviceNode {
    public static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static final int OEM = 0;//oem
    public static final int MODELS = 1;//models
    public static final int SERIALNUMBER = 2;//serialNumber
    public static final int VERSION = 3;//version
    public static final int REBOOT = 4;//reboot
    public static final int FACTORYRESET = 5;//factoryReset
    public static final int AVAILABLEPOWERSOURCES = 6;//availablePowerSources
    public static final int POWERSOURCEVOLTAGE = 7;//powerSourceVoltage
    public static final int POWERSOURCECURRENT = 8;//powerSourceCurrent
    public static final int BATTERYLEVEL = 9;//batteryLevel
    public static final int MEMORYFREE = 10;//memoryFree
    public static final int ERRORCODE = 11;//errorCode
    public static final int RESETERRORCODE = 12;//resetErrorCode
    public static final int CURRENTTIME = 13;//currentTime
    public static final int UTCOFFSET = 14;//UtcOffset
    public static final int TIMEZONE = 15;//timezone
    public static final int SUPPORTEDBINDINGANDMODES = 16;//supportedBindingandModes
    public static final int DEVICETYPE = 17;//deviceType
    public static final int HARDWAREVERSION = 18;//hardwareVersion
    public static final int SOFTWAREVERSION = 19;//softwareVersion
    public static final int BATTERYSTATUS = 20;//batteryStatus
    public static final int MEMORYTOTAL = 21;//memoryTotal
    public static final int EXTDEVINFO = 22;//extDevInfo
    public static final int PRODUCTID = 100;//productId
    public static final int DEVICEID = 101;//deviceId
    public static final int PLATFORM = 102;//platform
    public static final int TIMESTAMP = 103;//timestamp
    public static final int SIGN = 104;//sign
    public static final int SDKVERSION = 105;//sdkversion
    public static final int APPVERSION = 106;//appversion
    public static final int NETWORKTYPE = 107;//networkType
    public static final int MAC = 108;//mac
    public static final int LAC = 109;//lac
    public static final int CID = 110;//cid
    public static final int MCC = 111;//mcc
    public static final int MNC = 112;//mnc
    public static final int RXLEV = 113;//rxlev
    public static final int STATUS = 200;//status

    public static final int MID = 1001;//mid
    public static final int IP = 1002;//ip

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

    public static Long timestamp(String value) throws ParseException {
        if (StringUtils.isEmpty(value)) {
            return null;
        } else {
            Date date = format.parse(value);
            Long timestampLong = date.getTime() / 1000;
            return timestampLong;
        }
    }
}
