package com.abupdate.iot.lwm2m.resource;

import com.abupdate.iot.lwm2m.server.LwM2mServer;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.eclipse.lwm2m.core.node.LwM2mNode;
import org.eclipse.lwm2m.core.request.ExecuteRequest;
import org.eclipse.lwm2m.core.request.WriteRequest;
import org.eclipse.lwm2m.core.request.WriteRequest.Mode;
import org.eclipse.lwm2m.core.response.ExecuteResponse;
import org.eclipse.lwm2m.core.response.WriteResponse;
import org.eclipse.lwm2m.server.registration.Registration;
import org.eclipse.lwm2m.server.registration.RegistrationHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.abupdate.iot.lwm2m.bean.DownloadPost;
import com.abupdate.iot.lwm2m.code.Codes;
import com.abupdate.iot.lwm2m.ota.server.DownloadServer;
import com.google.gson.Gson;

/**
 * @author wangxiaojing
 * @company adups
 * @date 2018年1月5日
 **/
public class DownloadResource extends VariableBase {
    private static final Logger logger = LoggerFactory.getLogger(DownloadResource.class);

    public static void downloadResource(DownloadPost downloadPost, CoapExchange exchange, LwM2mServer lwM2mServer, RegistrationHandler registrationHandler, Gson gson) {
        String mid = downloadPost.getMid();
        Long productId = downloadPost.getProductId();
        String key = REDIS_KEY + productId;
        String ip = exchange.getSourceAddress().toString().replace("/", "");
        downloadPost.setIp(ip);

        exchange.respond(ResponseCode.CONTENT);

        /*Download*/
        DownloadServer downloadServer = new DownloadServer();
        String resultDownload = downloadServer.downloadReport(downloadPost);

        Registration registration = registrationHandler.getByEndpoint(mid, key);
        if (registration != null) {
            try {
                LwM2mNode node = gson.fromJson(resultDownload, LwM2mNode.class);
                WriteRequest writeRequest = new WriteRequest(Mode.REPLACE, contentFormat, Codes.UP_TARGET_SUCCESS, node);
                WriteResponse writeResponse = lwM2mServer.send(registration, writeRequest, TIMEOUT);
                if (writeResponse != null && Codes.WRITE_RESULT.equals(writeResponse.getCode().toString())) {
                    logger.info("Write Download success" + LOGINFO, mid, productId, resultDownload);
                } else {
                    logger.info("Write Download failure" + LOGINFO, mid, productId, resultDownload);
                }
            } catch (Exception e) {
                //e.printStackTrace();
                logger.info("Write Download failure" + LOGINFO, mid, productId, e.getMessage());
                return;
            }

            try {
                /*Exce*/
                if (downloadPost.getState() == Codes.DOWN_SUCCESS_VALUE) {
                    ExecuteRequest executeRequest = new ExecuteRequest(Codes.EXECUTE_TARGET, null);
                    ExecuteResponse cResponse = lwM2mServer.send(registration, executeRequest, TIMEOUT);
                    if (cResponse != null && Codes.WRITE_RESULT.equals(cResponse.getCode().toString())) {
                        logger.info("Execute Upgrade success" + LOGINFO, mid, productId, cResponse.getCode());
                        return;
                    } else {
                        logger.info("Execute Upgrade failure" + LOGINFO, mid, productId, cResponse.getCode());
                        return;
                    }
                }
            } catch (Exception e) {
                //e.printStackTrace();
                logger.info("Execute Upgrade failure" + LOGINFO, mid, productId, e.getMessage());
                return;
            }
            return;
        }
    }

    /**
     private static List<ReadResources> addList (Map<String, String> mapUriQuery, String mid, String ip, String productId, String deviceId) {
     List<ReadResources> list = new ArrayList<>();
     //MID
     ReadResources rr = new ReadResources();
     rr.setId(1001);
     rr.setValue(mid);
     list.add(rr);
     //IP
     rr = new ReadResources();
     rr.setId(1002);
     rr.setValue(ip);
     list.add(rr);
     //productId
     rr = new ReadResources();
     rr.setId(1003);
     rr.setValue(productId);
     list.add(rr);
     //deviceId
     rr = new ReadResources();
     rr.setId(1004);
     rr.setValue(deviceId);
     list.add(rr);
     //downStart
     rr = new ReadResources();
     rr.setId(101);
     rr.setValue(mapUriQuery.get("101"));
     list.add(rr);
     //downStart
     rr = new ReadResources();
     rr.setId(103);
     rr.setValue(mapUriQuery.get("103"));
     list.add(rr);
     //downEnd
     rr = new ReadResources();
     rr.setId(104);
     rr.setValue(mapUriQuery.get("104"));
     list.add(rr);
     //downSize
     rr = new ReadResources();
     rr.setId(105);
     rr.setValue(mapUriQuery.get("105"));
     list.add(rr);
     //downIp
     rr = new ReadResources();
     rr.setId(106);
     rr.setValue(mapUriQuery.get("106"));
     list.add(rr);

     String status = mapUriQuery.get("107");
     if (!StringUtils.isEmpty(status)) {
     if (Integer.parseInt(status) < 100) {
     //downStatus
     rr = new ReadResources();
     rr.setId(3);
     rr.setValue(status);
     list.add(rr);
     } else {
     //errorCode
     rr = new ReadResources();
     rr.setId(107);
     rr.setValue(status);
     list.add(rr);
     }
     }

     //errorCode
     rr = new ReadResources();
     rr.setId(107);
     rr.setValue(mapUriQuery.get("107"));
     list.add(rr);
     return list;
     }**/
}
