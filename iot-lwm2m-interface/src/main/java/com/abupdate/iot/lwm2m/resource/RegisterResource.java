package com.abupdate.iot.lwm2m.resource;

import static org.eclipse.lwm2m.core.californium.ExchangeUtil.extractIdentity;
import static org.eclipse.lwm2m.core.californium.ResponseCodeUtil.fromLwM2mCode;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.abupdate.iot.lwm2m.server.LwM2mServer;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.coap.Request;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.eclipse.lwm2m.Link;
import org.eclipse.lwm2m.core.request.BindingMode;
import org.eclipse.lwm2m.core.request.Identity;
import org.eclipse.lwm2m.core.request.RegisterRequest;
import org.eclipse.lwm2m.core.request.UpdateRequest;
import org.eclipse.lwm2m.core.response.RegisterResponse;
import org.eclipse.lwm2m.core.response.UpdateResponse;
import org.eclipse.lwm2m.server.impl.SendableResponse;
import org.eclipse.lwm2m.server.registration.RegistrationHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.abupdate.iot.lwm2m.bean.UriQuery;

/**
 * @author wangxiaojing
 * @company adups
 * @date 2018年1月5日
 **/
public class RegisterResource extends VariableBase {
    private static final Logger logger = LoggerFactory.getLogger(RegisterResource.class);

    public static final String RESOURCE_NAME = "rd";

    public static void registerResource(UriQuery uriQuery, CoapExchange exchange, LwM2mServer lwM2mServer, RegistrationHandler registrationHandler) {
        Request request = exchange.advanced().getRequest();
        List<String> uri = exchange.getRequestOptions().getUriPath();

        if (uri.size() == 1) {
            //回复消息-->2，处理注册信息
            handleRegister(uriQuery, exchange, request, registrationHandler);
        } else if (uri.size() == 2) {
            handleUpdate(uriQuery, exchange, request, uri.get(1), registrationHandler);
            return;
        } else {
            exchange.respond(ResponseCode.BAD_REQUEST);
            return;
        }
    }

    private static void handleRegister(UriQuery uriQuery, CoapExchange exchange, Request request, RegistrationHandler registrationHandler) {
        // Get identity
        Identity sender = extractIdentity(exchange);

        // Get object Links
        Link[] objectLinks = Link.parse(request.getPayload());
        String endpoint = uriQuery.getEp();
        Long lifetime = uriQuery.getLt();
        String smsNumber = uriQuery.getSms();
        String lwVersion = uriQuery.getLwm2m();

        BindingMode binding = BindingMode.valueOf(uriQuery.getB());
        Long productId = uriQuery.getProductId();

        Map<String, String> additionalParams = new HashMap<>();
        // Get parameters
        for (String param : request.getOptions().getUriQuery()) {
            String[] tokens = param.split("\\=");
            if (tokens != null && tokens.length == 2) {
                additionalParams.put(tokens[0], tokens[1]);
            }
        }

        //create key
        String key = REDIS_KEY + productId;

        // Create request
        RegisterRequest registerRequest = new RegisterRequest(endpoint, lifetime, lwVersion, binding, smsNumber, objectLinks, additionalParams);
        // Handle request

        InetSocketAddress serverEndpoint = exchange.advanced().getEndpoint().getAddress();
        final SendableResponse<RegisterResponse> sendableResponse = registrationHandler.register(sender, registerRequest, serverEndpoint, key);

        RegisterResponse response = sendableResponse.getResponse();

        // Create CoAP Response from LwM2m request
        // -------------------------------
        if (response.getCode() == org.eclipse.lwm2m.ResponseCode.CREATED) {
            exchange.setLocationPath(RESOURCE_NAME + "/" + response.getRegistrationID());
            //回复消息-->3，将消息交给CoapExchange处理
            exchange.respond(ResponseCode.CREATED);
            logger.info("Register LwM2m success" + LOGINFO, endpoint, productId, ResponseCode.CONTENT);
        } else {
            exchange.respond(fromLwM2mCode(response.getCode()), response.getErrorMessage());
        }
        sendableResponse.sent();
    }

    private static void handleUpdate(UriQuery uriQuery, CoapExchange exchange, Request request, String registrationId, RegistrationHandler registrationHandler) {
        // Get identity
        Identity sender = extractIdentity(exchange);
        // Get object Links
        Link[] objectLinks = Link.parse(request.getPayload());

        // Create LwM2m request from CoAP request
        Long lifetime = uriQuery.getLt();
        String smsNumber = uriQuery.getSms();
        BindingMode binding = BindingMode.valueOf(uriQuery.getB());

        if (request.getPayload() != null && request.getPayload().length > 0) {
            objectLinks = Link.parse(request.getPayload());
        }
        UpdateRequest updateRequest = new UpdateRequest(registrationId, lifetime, smsNumber, binding, objectLinks);

        // Handle request
        final SendableResponse<UpdateResponse> sendableResponse = registrationHandler.update(sender, updateRequest);
        UpdateResponse updateResponse = sendableResponse.getResponse();

        // Create CoAP Response from LwM2m request
        exchange.respond(fromLwM2mCode(updateResponse.getCode()), updateResponse.getErrorMessage());
        sendableResponse.sent();
    }

}
