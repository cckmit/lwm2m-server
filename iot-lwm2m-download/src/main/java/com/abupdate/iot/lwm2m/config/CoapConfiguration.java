package com.abupdate.iot.lwm2m.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @description:
 * @author: wei xing
 * @create: 2021-07-16 14:30
 */
@Configuration
public class CoapConfiguration {
    @Value("${download.url}")
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "CoapConfiguration{" +
                "url='" + url + '\'' +
                '}';
    }
}
