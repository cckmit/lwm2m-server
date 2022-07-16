package com.abupdate.iot.lwm2m.resource;

import com.abupdate.iot.lwm2m.bean.Auth;
import com.abupdate.iot.lwm2m.code.Codes;
import com.abupdate.iot.lwm2m.ota.server.AuthServer;
import com.abupdate.iot.lwm2m.server.LwM2mServer;
import com.abupdate.iot.lwm2m.util.ResultCode;
import com.google.gson.Gson;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.eclipse.lwm2m.core.node.LwM2mNode;
import org.eclipse.lwm2m.core.request.WriteRequest;
import org.eclipse.lwm2m.core.response.WriteResponse;
import org.eclipse.lwm2m.server.registration.Registration;
import org.eclipse.lwm2m.server.registration.RegistrationHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author abupdate
 * @date 2019/7/8.
 * @title
 */
public class AuthResource extends VariableBase {
    private static final Logger logger = LoggerFactory.getLogger(AuthResource.class);

    public static void getKeyResource(Auth auth, CoapExchange exchange, LwM2mServer lwM2mServer, RegistrationHandler registrationHandler, Gson gson) {
        String mid = auth.getMid();
        Integer productId = auth.getProductId();
        String key = REDIS_KEY + productId;
        String ip = exchange.getSourceAddress().toString().replace("/", "");
        auth.setIp(ip);

        Registration registration = registrationHandler.getByEndpoint(mid, key);
        logger.info("Registration -->> {}", registration);
        if (registration != null) {
            try {
                AuthServer authServer = new AuthServer();
                // call api
                ResultCode resultCode = authServer.getKey(auth);
                LwM2mNode uaNode = gson.fromJson(resultCode.getJson(), LwM2mNode.class);
                WriteRequest uaRequest = new WriteRequest(WriteRequest.Mode.REPLACE, contentFormat, resultCode.getId(), uaNode);
                WriteResponse uaResponse = lwM2mServer.send(registration, uaRequest, TIMEOUT);
                if (uaResponse != null && Codes.WRITE_RESULT.equals(uaResponse.getCode().toString())) {
                    logger.info("Write ua url success" + LOGINFO, mid, productId, resultCode.getJson());
                } else {
                    logger.info("Write ua url failure" + LOGINFO, mid, productId, resultCode.getJson());
                    System.out.println("1111111111111");
                }
            } catch (Exception e) {
                //e.printStackTrace();
                logger.info("Read FirmwareUpdate failure" + LOGINFO, mid, productId+":"+e.getMessage());
                return;
            }
        }
    }
}
