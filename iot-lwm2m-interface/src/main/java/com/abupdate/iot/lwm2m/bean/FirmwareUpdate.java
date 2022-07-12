package com.abupdate.iot.lwm2m.bean;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.abupdate.iot.lwm2m.util.ReadResources;

/**
 * @author wangxiaojing
 * @company adups
 * @date 2018年1月4日
 * @FirmwareUpdate /5/0
 **/
public class FirmwareUpdate extends UpdateNode {
    private Logger logger = LoggerFactory.getLogger(FirmwareUpdate.class);

    private String packageURI;// 1
    private String state;// 3
    private String updateResult;// 5
    private Long fileSize;// 100
    private Long deltaID;// 101
    private String md5sum;// 102
    private String downStart;// 103
    private String downEnd;// 104
    private String downSize;// 105
    private String downIp;// 106
    private String errorCode;// 107

    private String mid;//1001
    private String ip;//1002
    private String productId;//1003
    private String deviceId;//1004

    public FirmwareUpdate(List<ReadResources> list) {
        if (list != null && list.size() > 0) {
            for (ReadResources read : list) {
                if (read.getId() == PACKAGEURI) {
                    this.packageURI = checkString(read.getValue());
                } else if (read.getId() == STATE) {
                    this.state = checkString(read.getValue());
                } else if (read.getId() == UPDATERESULT) {
                    this.updateResult = checkString(read.getValue());
                } else if (read.getId() == FILESIZE) {
                    this.fileSize = checkLong(read.getValue());
                } else if (read.getId() == DELTAID) {
                    this.deltaID = checkLong(read.getValue());
                } else if (read.getId() == MD5SUM) {
                    this.md5sum = checkString(read.getValue());
                } else if (read.getId() == DOWNSTART) {
                    this.downStart = checkString(read.getValue());
                } else if (read.getId() == DOWNEND) {
                    this.downEnd = checkString(read.getValue());
                } else if (read.getId() == DOWNSIZE) {
                    this.downSize = checkString(read.getValue());
                } else if (read.getId() == DOWNIP) {
                    this.downIp = checkString(read.getValue());
                } else if (read.getId() == ERRORCODE) {
                    this.errorCode = checkString(read.getValue());
                } else if (read.getId() == MID) {
                    this.mid = checkString(read.getValue());
                } else if (read.getId() == IP) {
                    this.ip = checkString(read.getValue());
                } else if (read.getId() == PRODUCTID) {
                    this.productId = checkString(read.getValue());
                } else if (read.getId() == DEVICEID) {
                    this.deviceId = checkString(read.getValue());
                }
            }
        } else {
            logger.info("FirmwareUpdate list is null");
        }
    }

    public String getPackageURI() {
        return packageURI;
    }

    public void setPackageURI(String packageURI) {
        this.packageURI = packageURI;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getUpdateResult() {
        return updateResult;
    }

    public void setUpdateResult(String updateResult) {
        this.updateResult = updateResult;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public Long getDeltaID() {
        return deltaID;
    }

    public void setDeltaID(Long deltaID) {
        this.deltaID = deltaID;
    }

    public String getMd5sum() {
        return md5sum;
    }

    public void setMd5sum(String md5sum) {
        this.md5sum = md5sum;
    }

    public String getDownStart() {
        return downStart;
    }

    public void setDownStart(String downStart) {
        this.downStart = downStart;
    }

    public String getDownEnd() {
        return downEnd;
    }

    public void setDownEnd(String downEnd) {
        this.downEnd = downEnd;
    }

    public String getDownSize() {
        return downSize;
    }

    public void setDownSize(String downSize) {
        this.downSize = downSize;
    }

    public String getDownIp() {
        return downIp;
    }

    public void setDownIp(String downIp) {
        this.downIp = downIp;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
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

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    @Override
    public String toString() {
        return "{\"logger\":\"" + logger + "\",\"packageURI\":\"" + packageURI + "\",\"state\":\"" + state + "\",\"updateResult\":\""
                + updateResult + "\",\"fileSize\":\"" + fileSize + "\",\"deltaID\":\"" + deltaID
                + "\",\"md5sum\":\"" + md5sum + "\",\"downStart\":\"" + downStart + "\",\"downEnd\":\"" + downEnd
                + "\",\"downSize\":\"" + downSize + "\",\"downIp\":\"" + downIp + "\",\"errorCode\":\"" + errorCode
                + "\",\"mid\":\"" + mid + "\",\"ip\":\"" + ip + "\",\"productId\":\"" + productId + "\",\"deviceId\":\""
                + deviceId + "\"}";
    }

}
