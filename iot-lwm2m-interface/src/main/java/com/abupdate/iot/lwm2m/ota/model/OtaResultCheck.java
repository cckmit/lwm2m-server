package com.abupdate.iot.lwm2m.ota.model;

/**
 * @author wangxiaojing
 * @company adups
 * @date 2018年1月9日
 **/
public class OtaResultCheck {
    private String bakUrl;
    private String deltaUrl;
    private String md5sum;
    private Long deltaID;
    private Long fileSize;
    private String versionAlias;
    private String versionName;
    private String segmentMd5;

    public String getBakUrl() {
        return bakUrl;
    }

    public void setBakUrl(String bakUrl) {
        this.bakUrl = bakUrl;
    }

    public String getDeltaUrl() {
        return deltaUrl;
    }

    public void setDeltaUrl(String deltaUrl) {
        this.deltaUrl = deltaUrl;
    }

    public String getMd5sum() {
        return md5sum;
    }

    public void setMd5sum(String md5sum) {
        this.md5sum = md5sum;
    }

    public Long getDeltaID() {
        return deltaID;
    }

    public void setDeltaID(Long deltaID) {
        this.deltaID = deltaID;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getVersionAlias() {
        return versionAlias;
    }

    public void setVersionAlias(String versionAlias) {
        this.versionAlias = versionAlias;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getSegmentMd5() {
        return segmentMd5;
    }

    public void setSegmentMd5(String segmentMd5) {
        this.segmentMd5 = segmentMd5;
    }
}
