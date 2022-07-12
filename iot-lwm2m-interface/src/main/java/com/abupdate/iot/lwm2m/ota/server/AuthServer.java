package com.abupdate.iot.lwm2m.ota.server;

import com.abupdate.iot.lwm2m.StartApplication;
import com.abupdate.iot.lwm2m.bean.Auth;
import com.abupdate.iot.lwm2m.error.ErrorCode;
import com.abupdate.iot.lwm2m.ota.json.JsonUtil;
import com.abupdate.iot.lwm2m.ota.json.ResultData;
import com.abupdate.iot.lwm2m.resource.VariableBase;
import com.abupdate.iot.lwm2m.util.ResultCode;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author abupdate
 * @date 2019/7/8.
 * @title
 */
public class AuthServer extends StartApplication {
    private static final Logger logger = LoggerFactory.getLogger(CheckServer.class);
    private static final String LOGINFO = " -->> mid={}, productId={}, responseCode={}";

    public static final String WRITE_SUCCESS = "/5/0";
    public static final String WRITE_NOT_VERSION = "/3/0/200";

    public ResultCode getKey(Auth auth) {

        String result = null;
        Map<String, Object> resultLwM2m = new HashMap<>();
        ResultCode rc = new ResultCode();
        try {
            /*调用OTA接口*/
            //String ota = oc.getKey(auth.getProductId(), auth.getMid(), auth.getVersion(), auth.getIp());
            String ota = "111111111111111111111111111111";
            logger.info("OTA ua Server success" + LOGINFO + ", result={}", auth.getMid(), auth.getProductId(), 1000, ota);

            Gson gson = new Gson();
            ResultData rd = new ResultData(1000,"11111111","1111111111");
            if (rd.getStatus() == VariableBase.POST_SUCCESS) {
                Map<String, Object>[] resultMap = new Map[1];
                resultMap[0] = new HashMap<String, Object>();

                resultMap[0].put("id", 114);
                resultMap[0].put("value", rd.getData());

                resultLwM2m.put("id", 0);
                resultLwM2m.put("resources", resultMap);

                rc.setId(WRITE_SUCCESS);
            } else {
                resultLwM2m.put("id", 200);
                resultLwM2m.put("value", rd.getStatus());
                rc.setId(WRITE_NOT_VERSION);
            }
            result = JsonUtil.toJson(resultLwM2m);
        } catch (Exception e) {
            logger.info("OTA ua Server error" + LOGINFO, auth.getMid(), auth.getProductId(), ErrorCode.ERROR_CHECK);
            e.printStackTrace();
        }
        rc.setJson(result);
        return rc;
    }
}
