package com.abupdate.iot.lwm2m.ota.server;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.abupdate.iot.lwm2m.StartApplication;
import com.abupdate.iot.lwm2m.bean.RegisterCheckPost;
import com.abupdate.iot.lwm2m.error.ErrorCode;
import com.abupdate.iot.lwm2m.ota.json.JsonUtil;
import com.abupdate.iot.lwm2m.ota.json.ResultData;
import com.abupdate.iot.lwm2m.ota.model.OtaResultRegister;
import com.abupdate.iot.lwm2m.util.ResultCode;
import com.google.gson.Gson;

import net.sf.json.JSONObject;

import static com.abupdate.iot.lwm2m.resource.VariableBase.IOT_DEVICEINFO_KEY;

/**
 * @author wangxiaojing
 * @company adups
 * @date 2018年1月4日
 * @Register
 * @String JSON
 * @return {"id": 0,"resources": [{"id": 101,"value": 243},{"id": 200,"value": 1000}]}
 **/
@SuppressWarnings("unchecked")
public class RegisterServer extends StartApplication {
    private static final Logger logger = LoggerFactory.getLogger(RegisterServer.class);
    public static final String LOGINFO = " -->> mid={}, productId={}, responseCode={}";

    public static final String WRITE_SUCCESS = "/3/0";
    public static final String WRITE_FAILURE = "/3/0/200";

    public ResultCode register(RegisterCheckPost registerCheck) {
        String result = null;
        Map<String, Object> resultLwM2m = new HashMap<>();
        ResultCode rc = new ResultCode();

        try {
            /*调用OTA接口*/
            ResultData rd = new ResultData(1000,"1000","1000");
            Gson gson = new Gson();
            if (rd.getData() != null && !rd.getData().equals("")) {
                OtaResultRegister or = gson.fromJson(JSONObject.fromObject(rd.getData()).toString(), OtaResultRegister.class);
                Map<String, Object>[] resultMap = new Map[2];
                resultMap[1] = new HashMap<String, Object>();
                resultMap[0] = new HashMap<String, Object>();

                resultMap[0].put("id", 200);
                resultMap[0].put("value", rd.getStatus());
                resultMap[1].put("id", 101);
                resultMap[1].put("value", or.getDeviceId());

                stringRedisTemplate.opsForValue().set(IOT + IOT_DEVICEINFO_KEY + registerCheck.getProductId() + ":" + registerCheck.getMid(),or.getDeviceId()+"_"+or.getDeviceSecret());

                resultLwM2m.put("id", 0);
                resultLwM2m.put("resources", resultMap);

                rc.setId(WRITE_SUCCESS);
            } else {
                resultLwM2m.put("id", 200);
                resultLwM2m.put("value", rd.getStatus());
                rc.setId(WRITE_FAILURE);
            }

            result = JsonUtil.toJson(resultLwM2m);
        } catch (Exception e) {
            logger.info("OTA Register Server error" + LOGINFO, registerCheck.getMid(), registerCheck.getProductId(), ErrorCode.ERROR_REGISTER);
            e.printStackTrace();
        }

        rc.setJson(result);
        return rc;
    }
}
