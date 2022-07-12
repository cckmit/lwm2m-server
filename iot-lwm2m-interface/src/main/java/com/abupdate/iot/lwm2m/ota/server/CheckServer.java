package com.abupdate.iot.lwm2m.ota.server;

import java.util.HashMap;
import java.util.Map;

import com.eclipsesource.json.Json;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.abupdate.iot.lwm2m.StartApplication;
import com.abupdate.iot.lwm2m.bean.RegisterCheckPost;
import com.abupdate.iot.lwm2m.error.ErrorCode;
import com.abupdate.iot.lwm2m.ota.json.JsonUtil;
import com.abupdate.iot.lwm2m.ota.json.ResultData;
import com.abupdate.iot.lwm2m.ota.model.OtaResultCheck;
import com.abupdate.iot.lwm2m.resource.VariableBase;
import com.abupdate.iot.lwm2m.util.ResultCode;
import com.google.gson.Gson;

import net.sf.json.JSONObject;

/**
 * @author wangxiaojing
 * @company adups
 * @date 2018年1月4日
 * @Check
 * @String JSON
 * @return {"id": 0,"resources": [{"id": 1,"value": "coap://172.17.1.215:5683/rd/downloads/1505963097/2041705/1514009305626.zip"},{"id": 100,"value": 38591},{"id": 101,"value": 243},{"id": 102,"value": "2f2c95ee4c8617316999e5050eda796a"}]}
 **/
@SuppressWarnings("unchecked")
public class CheckServer extends StartApplication {
    private static final Logger logger = LoggerFactory.getLogger(CheckServer.class);
    private static final String LOGINFO = " -->> mid={}, productId={}, responseCode={}";
    private static final String PATH = "/";

    public static final String WRITE_SUCCESS = "/5/0";
    public static final String WRITE_NOT_VERSION = "/3/0/200";

    public ResultCode checkVersion(RegisterCheckPost registerCheck) {
        registerCheck.setSign("64a569acbebc1fe16fdab7c95a0db850");

        String result = null;
        Map<String, Object> resultLwM2m = new HashMap<>();
        ResultCode rc = new ResultCode();
        try {
            logger.info("register json" + JsonUtil.toJson(registerCheck));
            /*调用OTA接口*/
            ResultData rd = new ResultData(1000,"1000","1000");
            Gson gson = new Gson();
            if (rd.getStatus() == VariableBase.POST_SUCCESS) {
                HashMap<String, Object> version = JsonUtil.fromJson(JSONObject.fromObject(rd.getData()).toString(), HashMap.class);
                OtaResultCheck orc = gson.fromJson(JSONObject.fromObject(version.get("version")).toString(), OtaResultCheck.class);

                Map<String, Object>[] resultMap = new Map[4];
                resultMap[0] = new HashMap<String, Object>();
                resultMap[1] = new HashMap<String, Object>();
                resultMap[2] = new HashMap<String, Object>();
                resultMap[3] = new HashMap<String, Object>();

                resultMap[0].put("id", 1);//1
                resultMap[0].put("value", getFileName(orc.getDeltaUrl(), registerCheck.getProductId(), orc.getDeltaID()));

                resultMap[1].put("id", 100);//100
                resultMap[1].put("value", orc.getFileSize());

                resultMap[2].put("id", 101);//101
                resultMap[2].put("value", orc.getDeltaID());

                resultMap[3].put("id", 102);//102
                resultMap[3].put("value", orc.getMd5sum());

                resultLwM2m.put("id", 0);
                resultLwM2m.put("resources", resultMap);

                rc.setId(WRITE_SUCCESS);
            } else {
                resultLwM2m.put("id", 200);//200
                resultLwM2m.put("value", rd.getStatus());

                rc.setId(WRITE_NOT_VERSION);
            }
            result = JsonUtil.toJson(resultLwM2m);
        } catch (Exception e) {
            logger.info("OTA Check Server error" + LOGINFO, registerCheck.getMid(), registerCheck.getProductId(), ErrorCode.ERROR_CHECK);
            e.printStackTrace();
        }
        rc.setJson(result);
        return rc;
    }

    public static String getFileName(String url, String productId, Long deltaId) {
        String[] urlSplit = url.split("/");
        String filename = urlSplit[urlSplit.length - 1];
//		String deltaUrl = DOWNLOAD_PATH + PATH + productId + PATH + deltaId + PATH + "1514009305626.zip";
        String deltaUrl = DOWNLOADURL + PATH + productId + PATH + deltaId + PATH + filename;
        return deltaUrl;
    }
}
