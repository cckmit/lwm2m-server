package com.abupdate.iot.lwm2m.resource;

import com.abupdate.iot.lwm2m.server.LwM2mServer;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.eclipse.lwm2m.core.node.LwM2mNode;
import org.eclipse.lwm2m.core.request.WriteRequest;
import org.eclipse.lwm2m.core.request.WriteRequest.Mode;
import org.eclipse.lwm2m.core.response.WriteResponse;
import org.eclipse.lwm2m.server.registration.Registration;
import org.eclipse.lwm2m.server.registration.RegistrationHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.abupdate.iot.lwm2m.bean.UpgradePost;
import com.abupdate.iot.lwm2m.code.Codes;
import com.abupdate.iot.lwm2m.ota.server.UpgradeServer;
import com.google.gson.Gson;

/**
 * @author wangxiaojing
 * @company adups
 * @date 2018年1月5日
 **/
public class UpgradeResource extends VariableBase {
    private static final Logger logger = LoggerFactory.getLogger(UpgradeResource.class);

    public static void upgradeResource(UpgradePost upgradePost, CoapExchange exchange, LwM2mServer lwM2mServer, RegistrationHandler registrationHandler, Gson gson) {
        String mid = upgradePost.getMid();
        Long productId = upgradePost.getProductId();
        String key = REDIS_KEY + productId;
        String ip = exchange.getSourceAddress().toString().replace("/", "");
        upgradePost.setIp(ip);

        Registration registration = registrationHandler.getByEndpoint(mid, key);
        logger.info("Registration -->> {}", registration);
        if (registration != null) {
            try {
                /** // create & process request ************** TODO read
                 ReadRequest readRequest = new ReadRequest(contentFormat, Codes.UP_TARGET);
                 ReadResponse readResponse = lwM2mServer.send(registration, readRequest, TIMEOUT);

                 System.err.println(readResponse);

                 if (readResponse == null) {
                 logger.info("Read FirmwareUpdate failure" + LOGINFO, mid, productId, ResponseCode.CONTENT);
                 exchange.respond(ResponseCode.NOT_FOUND, "Read device failure");
                 return;
                 }

                 ReadInfo deviceInfo = gson.fromJson(gson.toJson(readResponse), ReadInfo.class);
                 list = deviceInfo.getContent().getResources();

                 if (list.size() == 0) {
                 logger.info("Read FirmwareUpdate failure" + LOGINFO, mid, productId, ResponseCode.CONTENT);
                 exchange.respond(ResponseCode.NOT_FOUND, "Read device failure");
                 return;
                 }

                 list = addList(list, mid, ip, productId.toString(), uriQuery.getDeviceId());
                 if (list != null) {
                 **/
                UpgradeServer upgradeServer = new UpgradeServer();
                String resultUpgrade = upgradeServer.upgradeReport(upgradePost);

                if (resultUpgrade != null && !"".equals(resultUpgrade)) {
                    LwM2mNode node = gson.fromJson(resultUpgrade, LwM2mNode.class);
                    WriteRequest writeRequest = new WriteRequest(Mode.REPLACE, contentFormat, Codes.UP_TARGET_SUCCESS, node);
                    WriteResponse writeResponse = lwM2mServer.send(registration, writeRequest, TIMEOUT);
                    if (writeResponse != null && Codes.WRITE_RESULT.equals(writeResponse.getCode().toString())) {
                        logger.info("Write Upgrade success" + LOGINFO, mid, productId, ResponseCode.CONTENT);
                        return;
                    } else {
                        logger.info("Write Upgrade failure" + LOGINFO, mid, productId, ResponseCode.CONTENT);
                        return;
                    }
                } else {
                    return;
                }
            } catch (Exception e) {
                //e.printStackTrace();
                logger.error("Read FirmwareUpdate exception ={}",e.getMessage());
                logger.info("Read FirmwareUpdate failure" + LOGINFO, mid, productId);
                return;
            }
        }
    }

    /**
     private static List<ReadResources> addList(List<ReadResources> list, String mid, String ip, String productId, String deviceId) {
     String status = null;
     for (ReadResources read : list) {
     if (read.getId() == 5) {
     status = read.getValue();
     }
     }

     if (status == null && "0".equals(status) && "".equals(status)) {
     return null;
     }

     // MID
     ReadResources rr = new ReadResources();
     rr.setId(1001);
     rr.setValue(mid);
     list.add(rr);
     // IP
     rr = new ReadResources();
     rr.setId(1002);
     rr.setValue(ip);
     list.add(rr);
     // productId
     rr = new ReadResources();
     rr.setId(1003);
     rr.setValue(productId);
     list.add(rr);
     // deviceId
     rr = new ReadResources();
     rr.setId(1004);
     rr.setValue(deviceId);
     list.add(rr);
     return list;
     }**/
}
