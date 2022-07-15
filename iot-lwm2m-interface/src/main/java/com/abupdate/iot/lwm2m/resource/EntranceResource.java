package com.abupdate.iot.lwm2m.resource;

import com.abupdate.iot.lwm2m.bean.*;
import com.abupdate.iot.lwm2m.json.LwM2mNodeDeserializer;
import com.abupdate.iot.lwm2m.json.LwM2mNodeSerializer;
import com.abupdate.iot.lwm2m.json.ResponseSerializer;
import com.abupdate.iot.lwm2m.server.LwM2mServer;
import com.abupdate.iot.lwm2m.util.UriQueryUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.sf.json.JSONObject;
import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.coap.CoAP.Type;
import org.eclipse.californium.core.coap.Request;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.eclipse.californium.core.server.resources.Resource;
import org.eclipse.lwm2m.core.node.LwM2mNode;
import org.eclipse.lwm2m.core.response.LwM2mResponse;
import org.eclipse.lwm2m.server.registration.RegistrationHandler;
import org.eclipse.lwm2m.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

import static com.abupdate.iot.lwm2m.resource.AuthResource.getKeyResource;
import static com.abupdate.iot.lwm2m.resource.CheckResource.checkResource;
import static com.abupdate.iot.lwm2m.resource.DownloadResource.downloadResource;
import static com.abupdate.iot.lwm2m.resource.RegisterResource.registerResource;
import static com.abupdate.iot.lwm2m.resource.UpgradeResource.upgradeResource;
import static com.abupdate.iot.lwm2m.resource.VariableBase.getSgin;
import static com.abupdate.iot.lwm2m.resource.VariableBase.registerCheck;

/**
 * @author wangxiaojing
 * @company adups
 * @date 2018年1月5日
 **/
public class EntranceResource extends CoapResource {
    private static final Logger logger = LoggerFactory.getLogger(EntranceResource.class);

    private final RegistrationHandler registrationHandler;
    private final LwM2mServer lwM2mServer;

    private static final String LOGINFO = " -->> mid={}, productId={}, responseCode={}";
    public static final String RESOURCE_NAME = "rd";

    private static final String CHECK = "cv";
    private static final String DOWNLOAD = "rd";
    private static final String UPGRADE = "ru";
    private static final String UA = "ua";

    private final Gson gson;

    public EntranceResource(RegistrationHandler registrationHandler, LwM2mServer lwM2mServer) {
        super(RESOURCE_NAME);
        this.lwM2mServer = lwM2mServer;
        this.registrationHandler = registrationHandler;

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeHierarchyAdapter(LwM2mResponse.class, new ResponseSerializer());
        gsonBuilder.registerTypeHierarchyAdapter(LwM2mNode.class, new LwM2mNodeSerializer());
        gsonBuilder.registerTypeHierarchyAdapter(LwM2mNode.class, new LwM2mNodeDeserializer());
        gsonBuilder.setDateFormat("yyyy-MM-dd HH:mm:ss");
        this.gson = gsonBuilder.create();

        getAttributes().addResourceType("core.rd");
    }

    @Override
    public void handlePOST(CoapExchange exchange) {
        //接收消息-->12，将请求交给自定义的Resource处理
        Request request = exchange.advanced().getRequest();

        logger.info("Requset = {}, IP = {}", request, exchange.getSourceAddress());

        Map<String, String> mapUriQuery = new UriQueryUtil().getUriQueryMap(request.getOptions().getUriQuery());
        logger.info("uriQuery: {}", mapUriQuery.toString());

        JSONObject jsonUriQuery = JSONObject.fromObject(mapUriQuery);
        UriQuery uriQuery = this.gson.fromJson(jsonUriQuery.toString(), UriQuery.class);

        String result = checkUriQuery(uriQuery);
        if (!"true".equals(result)) {
            logger.info("UriQuery is null" + LOGINFO, result, result, ResponseCode.BAD_REQUEST);
            exchange.respond(ResponseCode.BAD_REQUEST, result);
            return;
        }

        uriQuery.setProductId(Long.parseLong(uriQuery.getSms()));

        String mid = uriQuery.getEp();
        Long productId = uriQuery.getProductId();

        if (!Type.CON.equals(request.getType())) {
            logger.info("Type" + LOGINFO, mid, productId, ResponseCode.BAD_REQUEST);
            exchange.respond(ResponseCode.BAD_REQUEST);
            return;
        }

        List<String> uri = exchange.getRequestOptions().getUriPath();
        if (uri == null || uri.size() == 0 || !RESOURCE_NAME.equals(uri.get(0))) {
            logger.info("UriPath is null" + LOGINFO, mid, productId, ResponseCode.BAD_REQUEST);
            exchange.respond(ResponseCode.BAD_REQUEST);
            return;
        }

        if (uriQuery.getEp() == null || "".equals(uriQuery.getEp())) {
            logger.info("Device name is null" + LOGINFO, mid, productId, ResponseCode.BAD_REQUEST);
            exchange.respond(ResponseCode.BAD_REQUEST, "Device name is null");
            return;
        }

        if (StringUtils.isEmpty(uriQuery.getOp())) {
            //回复消息-->1，处理注册信息
            registerResource(uriQuery, exchange, lwM2mServer, registrationHandler);
            exchange.respond(ResponseCode.CREATED);
            return;
        }

        if (!uriQuery.getOp().equals(UA) && !getSgin(uriQuery, registrationHandler)) {
            logger.info("Sign error " + LOGINFO, mid, productId, ResponseCode.BAD_REQUEST);
            exchange.respond(ResponseCode.BAD_REQUEST, "Sign error");
            return;
        }

        if (uriQuery.getOp().equals(UA)) {
            logger.info("ua -->> data={}", jsonUriQuery.toString());
            /*TODO UA*/
            Auth auth = this.gson.fromJson(jsonUriQuery.toString(), Auth.class);
            auth.setMid(uriQuery.getEp());
            auth.setProductId(Integer.parseInt(uriQuery.getSms()));
            auth.setVersion(uriQuery.getV());
            getKeyResource(auth, exchange, lwM2mServer, registrationHandler, gson);
        }

        if (uriQuery.getOp().equals(CHECK)) {
            /*TODO Check*/
            logger.info("check -->> mid={}, productId={}", mid, productId);
            RegisterCheckPost registerCheck = this.gson.fromJson(jsonUriQuery.toString(), RegisterCheckPost.class);
            registerCheck.setMid(uriQuery.getEp());
            registerCheck.setProductId(uriQuery.getSms());
            checkResource(registerCheck, exchange, lwM2mServer, registrationHandler, gson);
        }

        if (uriQuery.getOp().equals(DOWNLOAD)) {
            /*TODO Download*/
            logger.info("download -->> mid={}, productId={}", mid, productId);
            String deviceId = registerCheck(uriQuery.getProductId().toString(), uriQuery.getEp());
            DownloadPost downloadPost = this.gson.fromJson(jsonUriQuery.toString(), DownloadPost.class);
            downloadPost.setDeviceId(deviceId);
            downloadPost.setMid(uriQuery.getEp());
            downloadPost.setProductId(Long.parseLong(uriQuery.getSms()));
            downloadResource(downloadPost, exchange, lwM2mServer, registrationHandler, gson);
        }

        if (uriQuery.getOp().equals(UPGRADE)) {
            /*TODO Upgrade*/
            logger.info("upgrade -->> mid={}, productId={}", mid, productId);
            String deviceId = registerCheck(uriQuery.getProductId().toString(), uriQuery.getEp());
            uriQuery.setDeviceId(deviceId);
            UpgradePost upgradePost = this.gson.fromJson(jsonUriQuery.toString(), UpgradePost.class);
            upgradePost.setDeviceId(deviceId);
            upgradePost.setMid(uriQuery.getEp());
            upgradePost.setProductId(Long.parseLong(uriQuery.getSms()));
            upgradeResource(upgradePost, exchange, lwM2mServer, registrationHandler, gson);
        }
    }

    private String checkUriQuery(UriQuery uriQuery) {
        if (StringUtils.isEmpty(uriQuery.getEp())) {
            return "Mid is null";
        } else if (StringUtils.isEmpty(uriQuery.getSms())) {
            return "ProductId is null";
        }
        return "true";
    }

    @Override
    public Resource getChild(String name) {
        return this;
    }
}
