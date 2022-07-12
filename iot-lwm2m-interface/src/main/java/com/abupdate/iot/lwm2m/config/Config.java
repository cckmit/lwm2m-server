package com.abupdate.iot.lwm2m.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@ConfigurationProperties(prefix = "url")
@Component
public class Config {
    @Value("${url.downloadUurl}")
    private String downloadUurl;
    @Value("${url.redisUrl}")
    private String redisUrl;

    public String getDownloadUurl() {
        return downloadUurl;
    }

    public void setDownloadUurl(String downloadUurl) {
        this.downloadUurl = downloadUurl;
    }

    public String getRedisUrl() {
        return redisUrl;
    }

    public void setRedisUrl(String redisUrl) {
        this.redisUrl = redisUrl;
    }
}

