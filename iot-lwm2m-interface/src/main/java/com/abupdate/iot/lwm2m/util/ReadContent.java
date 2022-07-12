package com.abupdate.iot.lwm2m.util;

import java.util.List;

/**
 * @author wangxiaojing
 * @company adups
 * @date 2017年12月21日
 **/
public class ReadContent {
    private int id;
    private List<ReadResources> resources;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<ReadResources> getResources() {
        return resources;
    }

    public void setResources(List<ReadResources> resources) {
        this.resources = resources;
    }

    @Override
    public String toString() {
        return "{\"id\":\"" + id + "\",\"resources\":\"" + resources + "\"}";
    }
}
