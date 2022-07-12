package com.abupdate.iot.lwm2m.resource;

import java.util.List;

import com.abupdate.iot.lwm2m.server.LwM2mServer;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.eclipse.lwm2m.core.node.LwM2mNode;
import org.eclipse.lwm2m.core.request.WriteRequest;
import org.eclipse.lwm2m.core.request.WriteRequest.Mode;
import org.eclipse.lwm2m.core.response.WriteResponse;
import org.eclipse.lwm2m.server.registration.Registration;
import org.eclipse.lwm2m.server.registration.RegistrationHandler;
import org.eclipse.lwm2m.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.abupdate.iot.lwm2m.bean.RegisterCheckPost;
import com.abupdate.iot.lwm2m.code.Codes;
import com.abupdate.iot.lwm2m.ota.server.CheckServer;
import com.abupdate.iot.lwm2m.ota.server.RegisterServer;
import com.abupdate.iot.lwm2m.util.ReadContent;
import com.abupdate.iot.lwm2m.util.ReadResources;
import com.abupdate.iot.lwm2m.util.ResultCode;
import com.google.gson.Gson;

/**
 * @author wangxiaojing
 * @company adups
 * @date 2018年1月5日
 **/
public class CheckResource extends VariableBase {
    private static final Logger logger = LoggerFactory.getLogger(CheckResource.class);
    public static final String WRITE_FAILURE = "/3/0/200";

    public static void checkResource(RegisterCheckPost registerCheck, CoapExchange exchange, LwM2mServer lwM2mServer, RegistrationHandler registrationHandler, Gson gson) {
        String key = REDIS_KEY + registerCheck.getProductId();
        String mid = registerCheck.getMid();

        String productId = registerCheck.getProductId();

        String ip = exchange.getSourceAddress().toString().replace("/", "");
        String deviceId = null;
        registerCheck.setIp(ip);

        Registration registration = registrationHandler.getByEndpoint(mid, key);
        //logger.info("exists registration"+registrationHandler.getValue(key).getClass());
        logger.info("Registration -->> {}", registration);
        ResultCode resultRegister = new ResultCode();
        if (registration != null) {
            try {
                // create & process request  **************  TODO read
                /**ReadRequest readRequest = new ReadRequest(contentFormat, Codes.READ_TARGET);
                 ReadResponse readResponse = lwM2mServer.send(registration, readRequest, TIMEOUT);

                 if (readResponse == null) {
                 logger.info("Read device failure" + LOGINFO, mid, productId, ResponseCode.CONTENT);
                 exchange.respond(ResponseCode.NOT_FOUND, "Read device failure");
                 return;
                 }
                 logger.info("Read Device -->> {}", gson.toJson(readResponse));
                 ReadInfo deviceInfo = gson.fromJson(gson.toJson(readResponse), ReadInfo.class);
                 list = deviceInfo.getContent().getResources();

                 if (list.size() == 0) {
                 logger.info("Read device failure" + LOGINFO, mid, productId, ResponseCode.CONTENT);
                 exchange.respond(ResponseCode.NOT_FOUND, "Read device failure");
                 return;
                 }

                 list = addList(list, mid, ip, productId.toString());
                 **/
                logger.info("register check11");
                        /*TODO Register*/
                deviceId = registerCheck(productId.toString(), mid);
                if (StringUtils.isEmpty(deviceId)) {
                    RegisterServer registerServer = new RegisterServer();
                    resultRegister = registerServer.register(registerCheck);

                    LwM2mNode node = gson.fromJson(resultRegister.getJson(), LwM2mNode.class);
                    WriteRequest writeRequest = new WriteRequest(Mode.REPLACE, contentFormat, resultRegister.getId(), node);

                    WriteResponse writeResponse = lwM2mServer.send(registration, writeRequest, TIMEOUT);
                    if (writeResponse != null && Codes.WRITE_RESULT.equals(writeResponse.getCode().toString())) {
                        logger.info("Write Register success" + LOGINFO, mid, productId, resultRegister.getJson());
                    } else {
                        logger.info("Write Register failure" + LOGINFO, mid, productId, resultRegister.getJson());
                    }
                }
            } catch (Exception e) {
                //e.printStackTrace();
                logger.info("Read/Write device failure" + LOGINFO, mid, productId, READ_DEVICE+e.getMessage());
                return;
            }

            try {
                logger.info("register check22");
                /*TODO Check*/
                if (StringUtils.isEmpty(deviceId)) {
                    ReadContent readContent = gson.fromJson(resultRegister.getJson(), ReadContent.class);
                    List<ReadResources> listResources = readContent.getResources();
                    if (listResources.size() > 0) {
                        for (ReadResources readResources : listResources) {
                            if (readResources.getId() == 101) {
                                deviceId = readResources.getValue();
                            } else if (readResources.getId() == 200 && Integer.parseInt(readResources.getValue()) != POST_SUCCESS) {
                                logger.info("Register error" + LOGINFO, mid, productId, readResources.getValue());
                                return;
                            }
                        }
                    } else {
                        logger.info("Register error" + LOGINFO, mid, productId, resultRegister);
                        return;
                    }
                }
                registerCheck.setDeviceId(deviceId);
                CheckServer checkServer = new CheckServer();

                ResultCode resultCheck = checkServer.checkVersion(registerCheck);

                LwM2mNode checkNode = gson.fromJson(resultCheck.getJson(), LwM2mNode.class);
                WriteRequest checkRequest = new WriteRequest(Mode.REPLACE, contentFormat, resultCheck.getId(), checkNode);
                //exchange.respond(CoAP.ResponseCode.CREATED,resultCheck.getJson());


                WriteResponse checkResponse = lwM2mServer.send(registration, checkRequest, TIMEOUT);
                //logger.info("check status:"+checkResponse.getCode());
                if (checkResponse != null && Codes.WRITE_RESULT.equals(checkResponse.getCode().toString())) {
                    logger.info("Write Check url success" + LOGINFO, mid, productId, resultCheck.getJson());
                } else {
                    logger.info("Write Check url failure" + LOGINFO, mid, productId, resultCheck.getJson());
                }
            } catch (Exception e) {
                logger.info("exception message:" + e.getMessage());
                //e.printStackTrace();
                logger.info("Write Check failure" + LOGINFO, mid, productId, WRITE_CHECK);
                return;
            }
        }
    }

    /**
     private static List<ReadResources> addList (List<ReadResources> list, String mid, String ip, String productId) {
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
     rr.setId(100);
     rr.setValue(productId);
     list.add(rr);
     return list;
     }**/
}
