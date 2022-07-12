package com.abupdate.iot.lwm2m.bean;

import java.text.ParseException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.abupdate.iot.lwm2m.util.ReadResources;

/**
 * @author wangxiaojing
 * @company adups
 * @date 2018年1月4日
 * @Device /3/0
 **/
public class Device extends DeviceNode {
    private Logger logger = LoggerFactory.getLogger(Device.class);

    private String oem;// 0
    private String models;// 1
    private String serialNumber;// 2
    private String version;// 3
    private Integer availablePowerSources;// 6
    private Integer powerSourceVoltage;// 7
    private Integer powerSourceCurrent;// 8
    private Integer batteryLevel;// 9
    private Integer memoryFree;// 10
    private Integer errorCode;// 11
    private String resetErrorCode;// 12
    private String currentTime;// 13
    private String UtcOffset;// 14
    private String timezone;// 15
    private String supportedBindingandModes;// 16
    private String deviceType;// 17
    private String hardwareVersion;// 18
    private String softwareVersion;// 19
    private Integer batteryStatus;// 20
    private String memoryTotal;// 21
    private String extDevInfo;// 22
    private Long productId;// 100
    private String deviceId;// 101
    private String platform;// 102
    private Long timestamp;// 103
    private String sign;// 104
    private String sdkversion;// 105
    private String appversion;// 106
    private String networkType;// 107
    private String mac;// 108
    private String lac;// 109
    private String cid;// 110
    private String mcc;// 111
    private String mnc;// 112
    private String rxlev;// 113

    private String mid;//1001
    private String ip;//1002

    public Device(List<ReadResources> list) {
        if (list != null && list.size() > 0) {
            for (ReadResources read : list) {
                if (read.getId() == OEM) {
                    this.oem = checkString(read.getValue());
                } else if (read.getId() == MODELS) {
                    this.models = checkString(read.getValue());
                } else if (read.getId() == SERIALNUMBER) {
                    this.serialNumber = checkString(read.getValue());
                } else if (read.getId() == VERSION) {
                    this.version = checkString(read.getValue());
                } else if (read.getId() == AVAILABLEPOWERSOURCES) {
                    this.availablePowerSources = checkInteger(read.getValue());
                } else if (read.getId() == POWERSOURCEVOLTAGE) {
                    this.powerSourceVoltage = checkInteger(read.getValue());
                } else if (read.getId() == POWERSOURCECURRENT) {
                    this.powerSourceCurrent = checkInteger(read.getValue());
                } else if (read.getId() == BATTERYLEVEL) {
                    this.batteryLevel = checkInteger(read.getValue());
                } else if (read.getId() == MEMORYFREE) {
                    this.memoryFree = checkInteger(read.getValue());
                } else if (read.getId() == ERRORCODE) {
                    this.errorCode = checkInteger(read.getValue());
                } else if (read.getId() == RESETERRORCODE) {
                    this.resetErrorCode = checkString(read.getValue());
                } else if (read.getId() == CURRENTTIME) {
                    this.currentTime = checkString(read.getValue());
                } else if (read.getId() == UTCOFFSET) {
                    this.UtcOffset = checkString(read.getValue());
                } else if (read.getId() == TIMEZONE) {
                    this.timezone = checkString(read.getValue());
                } else if (read.getId() == SUPPORTEDBINDINGANDMODES) {
                    this.supportedBindingandModes = checkString(read.getValue());
                } else if (read.getId() == DEVICETYPE) {
                    this.deviceType = checkString(read.getValue());
                } else if (read.getId() == HARDWAREVERSION) {
                    this.hardwareVersion = checkString(read.getValue());
                } else if (read.getId() == SOFTWAREVERSION) {
                    this.softwareVersion = checkString(read.getValue());
                } else if (read.getId() == BATTERYSTATUS) {
                    this.batteryStatus = checkInteger(read.getValue());
                } else if (read.getId() == MEMORYTOTAL) {
                    this.memoryTotal = checkString(read.getValue());
                } else if (read.getId() == EXTDEVINFO) {
                    this.extDevInfo = checkString(read.getValue());
                } else if (read.getId() == PRODUCTID) {
                    this.productId = checkLong(read.getValue());
                } else if (read.getId() == DEVICEID) {
                    this.deviceId = checkString(read.getValue());
                } else if (read.getId() == PLATFORM) {
                    this.platform = checkString(read.getValue());
                } else if (read.getId() == TIMESTAMP) {
                    try {
                        this.timestamp = timestamp(read.getValue());
                    } catch (ParseException e) {
                        logger.info("Device Time Error time={}", read.getValue());
                        e.printStackTrace();
                    }
                } else if (read.getId() == PLATFORM) {
                    this.platform = checkString(read.getValue());
                } else if (read.getId() == SIGN) {
                    this.sign = checkString(read.getValue());
                } else if (read.getId() == SDKVERSION) {
                    this.sdkversion = checkString(read.getValue());
                } else if (read.getId() == APPVERSION) {
                    this.appversion = checkString(read.getValue());
                } else if (read.getId() == NETWORKTYPE) {
                    this.networkType = checkString(read.getValue());
                } else if (read.getId() == MAC) {
                    this.mac = checkString(read.getValue());
                } else if (read.getId() == LAC) {
                    this.lac = checkString(read.getValue());
                } else if (read.getId() == CID) {
                    this.cid = checkString(read.getValue());
                } else if (read.getId() == MCC) {
                    this.mcc = checkString(read.getValue());
                } else if (read.getId() == MNC) {
                    this.mnc = checkString(read.getValue());
                } else if (read.getId() == RXLEV) {
                    this.rxlev = checkString(read.getValue());
                } else if (read.getId() == MID) {
                    this.mid = checkString(read.getValue());
                } else if (read.getId() == IP) {
                    this.ip = checkString(read.getValue());
                }
            }
        } else {
            logger.info("Device list is null");
        }
    }

    public String getOem() {
        return oem;
    }

    public void setOem(String oem) {
        this.oem = oem;
    }

    public String getModels() {
        return models;
    }

    public void setModels(String models) {
        this.models = models;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Integer getAvailablePowerSources() {
        return availablePowerSources;
    }

    public void setAvailablePowerSources(Integer availablePowerSources) {
        this.availablePowerSources = availablePowerSources;
    }

    public Integer getPowerSourceVoltage() {
        return powerSourceVoltage;
    }

    public void setPowerSourceVoltage(Integer powerSourceVoltage) {
        this.powerSourceVoltage = powerSourceVoltage;
    }

    public Integer getPowerSourceCurrent() {
        return powerSourceCurrent;
    }

    public void setPowerSourceCurrent(Integer powerSourceCurrent) {
        this.powerSourceCurrent = powerSourceCurrent;
    }

    public Integer getBatteryLevel() {
        return batteryLevel;
    }

    public void setBatteryLevel(Integer batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    public Integer getMemoryFree() {
        return memoryFree;
    }

    public void setMemoryFree(Integer memoryFree) {
        this.memoryFree = memoryFree;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getResetErrorCode() {
        return resetErrorCode;
    }

    public void setResetErrorCode(String resetErrorCode) {
        this.resetErrorCode = resetErrorCode;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public String getUtcOffset() {
        return UtcOffset;
    }

    public void setUtcOffset(String utcOffset) {
        UtcOffset = utcOffset;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getSupportedBindingandModes() {
        return supportedBindingandModes;
    }

    public void setSupportedBindingandModes(String supportedBindingandModes) {
        this.supportedBindingandModes = supportedBindingandModes;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getHardwareVersion() {
        return hardwareVersion;
    }

    public void setHardwareVersion(String hardwareVersion) {
        this.hardwareVersion = hardwareVersion;
    }

    public String getSoftwareVersion() {
        return softwareVersion;
    }

    public void setSoftwareVersion(String softwareVersion) {
        this.softwareVersion = softwareVersion;
    }

    public Integer getBatteryStatus() {
        return batteryStatus;
    }

    public void setBatteryStatus(Integer batteryStatus) {
        this.batteryStatus = batteryStatus;
    }

    public String getMemoryTotal() {
        return memoryTotal;
    }

    public void setMemoryTotal(String memoryTotal) {
        this.memoryTotal = memoryTotal;
    }

    public String getExtDevInfo() {
        return extDevInfo;
    }

    public void setExtDevInfo(String extDevInfo) {
        this.extDevInfo = extDevInfo;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
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

    public String getSdkversion() {
        return sdkversion;
    }

    public void setSdkversion(String sdkversion) {
        this.sdkversion = sdkversion;
    }

    public String getAppversion() {
        return appversion;
    }

    public void setAppversion(String appversion) {
        this.appversion = appversion;
    }

    public String getNetworkType() {
        return networkType;
    }

    public void setNetworkType(String networkType) {
        this.networkType = networkType;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getLac() {
        return lac;
    }

    public void setLac(String lac) {
        this.lac = lac;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getMcc() {
        return mcc;
    }

    public void setMcc(String mcc) {
        this.mcc = mcc;
    }

    public String getMnc() {
        return mnc;
    }

    public void setMnc(String mnc) {
        this.mnc = mnc;
    }

    public String getRxlev() {
        return rxlev;
    }

    public void setRxlev(String rxlev) {
        this.rxlev = rxlev;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public String toString() {
        return "{\"oem\":\"" + oem + "\",\"models\":\"" + models + "\",\"serialNumber\":\"" + serialNumber
                + "\",\"version\":\"" + version + "\",\"availablePowerSources\":\"" + availablePowerSources
                + "\",\"powerSourceVoltage\":\"" + powerSourceVoltage + "\",\"powerSourceCurrent\":\""
                + powerSourceCurrent + "\",\"batteryLevel\":\"" + batteryLevel + "\",\"memoryFree\":\"" + memoryFree
                + "\",\"errorCode\":\"" + errorCode + "\",\"resetErrorCode\":\"" + resetErrorCode
                + "\",\"currentTime\":\"" + currentTime + "\",\"UtcOffset\":\"" + UtcOffset + "\",\"timezone\":\""
                + timezone + "\",\"supportedBindingandModes\":\"" + supportedBindingandModes + "\",\"deviceType\":\""
                + deviceType + "\",\"hardwareVersion\":\"" + hardwareVersion + "\",\"softwareVersion\":\""
                + softwareVersion + "\",\"batteryStatus\":\"" + batteryStatus + "\",\"memoryTotal\":\"" + memoryTotal
                + "\",\"extDevInfo\":\"" + extDevInfo + "\",\"productId\":\"" + productId + "\",\"deviceId\":\""
                + deviceId + "\",\"platform\":\"" + platform + "\",\"timestamp\":\"" + timestamp + "\",\"sign\":\""
                + sign + "\",\"sdkversion\":\"" + sdkversion + "\",\"appversion\":\"" + appversion
                + "\",\"networkType\":\"" + networkType + "\",\"mac\":\"" + mac + "\",\"lac\":\"" + lac
                + "\",\"cid\":\"" + cid + "\",\"mcc\":\"" + mcc + "\",\"mnc\":\"" + mnc + "\",\"rxlev\":\"" + rxlev
                + "\",\"mid\":\"" + mid + "\",\"ip\":\"" + ip + "\"}";
    }
}
