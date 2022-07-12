package com.abupdate.iot.lwm2m.resource;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Map;

import org.eclipse.lwm2m.core.request.ContentFormat;
import org.eclipse.lwm2m.server.registration.RegistrationHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.abupdate.iot.lwm2m.StartApplication;
import com.abupdate.iot.lwm2m.bean.Product;
import com.abupdate.iot.lwm2m.bean.UriQuery;
import com.abupdate.iot.lwm2m.code.Codes;
import com.abupdate.iot.lwm2m.ota.json.JsonUtil;
import com.abupdate.iot.lwm2m.util.Codec2;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.springframework.util.StringUtils;

/**
 * @author wangxiaojing
 * @company adups
 * @date 2018年1月5日
 **/
public class VariableBase extends StartApplication {
    public static final long TIMEOUT = 120000; // ms
    public static final ContentFormat contentFormat = ContentFormat.fromName(Codes.CONTENTFORMATPARAM.toUpperCase());
    public static final String LOGINFO = " -->> mid={}, productId={}, responseCode={}";
    public static final String REDIS_KEY = "_";
    public static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final Logger logger = LoggerFactory.getLogger(VariableBase.class);

    public static final int POST_SUCCESS = 1000;

    public static final String READ_DEVICE = "Read/Write device info failure";
    public static final String WRITE_CHECK = "Write check info failure";

    /*设备注册标识*/
    public static final String IOT_DEVICEINFO_KEY = "device:info:";

    /*获取项目和项目ID值*/
    public static final String IOT_OTA_PRODUCT_CODE_KEY = "product:code:";

    /*
     * 签名认证
     */
    public static boolean getSgin(UriQuery uriQuery, RegistrationHandler registrationHandler) {
        try {
            // Product product = getProduct(uriQuery.getProductId().toString(), registrationHandler);
            // if (product != null && !"".equals(product)) {
            //     String hmacmd5_sign = Codec2.hexString(uriQuery.getEp() + uriQuery.getProductId() + uriQuery.getTimestamp(), product.getProductSecret());
            //     logger.info("Register sign server -->> mid:" + uriQuery.getEp() + ",productId:" + uriQuery.getProductId() + ",productSecret:" + product.getProductSecret() + ",sign:" + hmacmd5_sign);
            //     if ("error".equals(hmacmd5_sign) || !hmacmd5_sign.equals(uriQuery.getSign())) {
            //         logger.info("Register sign error -->> mid:" + uriQuery.getEp() + ",productId:" + uriQuery.getProductId() + ",timestamp:" + uriQuery.getTimestamp() + ",sgin error");
            //         return false;
            //     } else {
            //         return true;
            //     }
            if(StringUtils.hasText(uriQuery.getSign())){
                return true;
            } else {
                logger.info("Register sign error -->> mid:" + uriQuery.getEp() + ",productId:" + uriQuery.getProductId() + ",timestamp:" + uriQuery.getTimestamp() + ",sgin error");
                return false;
            }
        } catch (Exception e) {
            //e.printStackTrace();
            logger.error("Register sign exception -->> {}",e.getMessage());
            return false;
        }
    }

    /*判断设备是否注册过*/
    public static String registerCheck(String productId, String mid) {
        try {
            String key = IOT + IOT_DEVICEINFO_KEY + productId + ":" + mid;
            String deviceIdRedis = stringRedisTemplate.opsForValue().get(key);
            if (isNullOrEmpty(deviceIdRedis)) {
                logger.info("Register First Login" + LOGINFO, mid, productId);
                return null;
            } else {
                logger.info("Register Not First Login" + LOGINFO, mid, productId, deviceIdRedis);
                return deviceIdRedis.split("_")[0];
            }
        } catch (Exception e) {
            //e.printStackTrace();
            logger.error("Register First Login exception Error {}",e.getMessage());
            logger.info("Register First Login Error" + LOGINFO, mid, productId);
            return null;
        }
    }

    /**
     * 根据ProductId获取项目
     *
     * @return
     * @throws IOException
     * @throws JsonMappingException
     * @throws JsonParseException
     */
    // private static Product getProduct(String productId, RegistrationHandler registrationHandler) throws JsonParseException, JsonMappingException, IOException {
    //     logger.info("stringRedisTemplate=" + stringRedisTemplate.toString());
    //     String string = stringRedisTemplate.opsForValue().get(IOT + IOT_OTA_PRODUCT_CODE_KEY + productId);
    //     logger.info("string=" + string);
    //     if (string != null && !StringUtils.isEmpty(string.toString())) {
    //         return JsonUtil.fromJson(string.substring(1, string.length() - 1), Product.class);
    //     } else {
    //         logger.info("获取项目信息失败:{}", productId);
    //         return null;
    //     }
    // }

    /**
     * 时间转换

     private static Long timestamp (String value) {
     try {
     if (StringUtils.isEmpty(value)) {
     return null;
     } else {
     Date date = format.parse(value);
     Long timestampLong = date.getTime() / 1000;
     return timestampLong;
     }
     } catch (Exception e) {
     e.printStackTrace();
     }
     return null;
     }*/

    /**
     * 判断对象或对象数组中每一个对象是否为空: 对象为null，字符序列长度为0，集合类、Map为empty
     *
     * @param obj
     * @return
     */
    @SuppressWarnings("rawtypes")
    private static boolean isNullOrEmpty(Object obj) {
        if (obj == null) {
            return true;
        }

        if (obj instanceof CharSequence) {
            return ((CharSequence) obj).length() == 0;
        }

        if (obj instanceof Collection) {
            return ((Collection) obj).isEmpty();
        }

        if (obj instanceof Map) {
            return ((Map) obj).isEmpty();
        }

        if (obj instanceof Object[]) {
            Object[] object = (Object[]) obj;
            if (object.length == 0) {
                return true;
            }
            boolean empty = true;
            for (int i = 0; i < object.length; i++) {
                if (!isNullOrEmpty(object[i])) {
                    empty = false;
                    break;
                }
            }
            return empty;
        }

        return false;
    }
}
