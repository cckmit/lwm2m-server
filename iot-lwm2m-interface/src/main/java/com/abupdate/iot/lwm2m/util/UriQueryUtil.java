package com.abupdate.iot.lwm2m.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author wangxiaojing
 * @company adups
 * @date 2017年12月29日
 **/
public class UriQueryUtil {
    private static final Logger loggin = LoggerFactory.getLogger(UriQueryUtil.class);

    public static final String SQLIT_EQ = "=";

    public Map<String, String> getUriQueryMap(List<String> list) {
        Map<String, String> map = new HashMap<String, String>();
        for (String name : list) {
            String[] uri = name.split(SQLIT_EQ);
            if (uri.length == 2) {
                map.put(uri[0].toString(), uri[1].toString());
            } else if (uri.length == 1) {
                map.put(uri[0].toString(), null);
            } else {
                loggin.info("uriQuery error -->> {}", name);
            }
        }
        return map;
    }
}
