package com.abupdate.iot.lwm2m.ota.server;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.abupdate.iot.lwm2m.StartApplication;
import com.abupdate.iot.lwm2m.bean.UpgradePost;
import com.abupdate.iot.lwm2m.error.ErrorCode;
import com.abupdate.iot.lwm2m.ota.json.JsonUtil;
import com.abupdate.iot.lwm2m.ota.json.ResultData;
import com.google.gson.Gson;

/**
 * @author wangxiaojing
 * @company adups
 * @date 2018年1月4日
 * @Upgrade
 * @String JSON
 * @return {"id": 200,"value": 1000}
 **/
public class UpgradeServer extends StartApplication {
    public static final String LOGINFO = " -->> mid={}, productId={}, responseCode={}";
    private static final Logger logger = LoggerFactory.getLogger(UpgradeServer.class);

    public String upgradeReport(UpgradePost upgradePost) {
        upgradePost.setSign("64a569acbebc1fe16fdab7c95a0db850");

        String result = null;
        Map<String, Object> resultLwM2m = new HashMap<>();
        try {
            /*调用OTA接口*/
            ResultData rd = new ResultData(1000,"1000","1000");
            resultLwM2m.put("id", 200);
            resultLwM2m.put("value", rd.getStatus());
            result = JsonUtil.toJson(resultLwM2m);

        } catch (Exception e) {
            logger.info("OTA Upgrade Server error" + LOGINFO, upgradePost.getMid(), upgradePost.getProductId(), ErrorCode.ERROR_UPGRADE);
            e.printStackTrace();
        }
        return result;
    }
}
