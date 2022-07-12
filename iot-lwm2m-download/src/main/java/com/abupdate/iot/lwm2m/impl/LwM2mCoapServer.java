package com.abupdate.iot.lwm2m.impl;

/**
 * @author wangxiaojing
 * @company adups
 * @date 2018年1月3日
 **/
public interface LwM2mCoapServer {
    void start();

    void stop();

    void destroy();
}
