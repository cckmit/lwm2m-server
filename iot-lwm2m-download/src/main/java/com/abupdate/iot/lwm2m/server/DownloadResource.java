package com.abupdate.iot.lwm2m.server;

import com.abupdate.iot.lwm2m.config.ActivitiConfig;
import com.abupdate.iot.lwm2m.config.CoapConfiguration;
import com.abupdate.iot.lwm2m.impl.LwM2mCoapServer;
import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.eclipse.californium.core.server.resources.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author wangxiaojing
 * @company adups
 * @date 2018年1月3日
 **/

public class DownloadResource extends CoapResource {

    private static final Logger logger = LoggerFactory.getLogger(DownloadResource.class);

    private static String os = System.getProperty("os.name").toLowerCase();

    public static final String RESOURCE_NAME = "rd";

    private static final String DOWNLOADS = "downloads";

    private static final String PATH = "/";

    public final LwM2mCoapServer lwM2mServer;

    // private static final String DOWNLOAD_PATH = "/data/iot_ota/upload/";

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");

    public DownloadResource(LwM2mCoapServer lwM2mServer) {
        super(RESOURCE_NAME);
        this.lwM2mServer = lwM2mServer;
    }

    @Override
    public void handleGET(CoapExchange exchange) {

        logger.info("GET download request -->> {}", exchange.advanced().getRequest());

        String downUrl = "";
        try {
            List<String> uri_path = exchange.advanced().getRequest().getOptions().getUriPath();

            for (int i = 0; i < uri_path.size(); i++) {
                logger.info("uri > {}", uri_path.get(i).toString());
            }
            String downloads = "", productId = "", deltaId = "", filename = "";
            if (uri_path != null) {
                for (int i = 0; i < uri_path.size(); i++) {
                    if (i == 1) {
                        downloads = uri_path.get(i).toString();
                    } else if (i == 2) {
                        productId = uri_path.get(i).toString();
                    } else if (i == 3) {
                        deltaId = uri_path.get(i).toString();
                    } else if (i == 4) {
                        filename = uri_path.get(i).toString();
                    }
                }
                downUrl = productId + PATH + deltaId + PATH + filename;

                logger.info("url:" + downUrl);

            } else {
                exchange.respond(ResponseCode.PRECONDITION_FAILED, "URL is null");
                logger.warn("[INFO ] Download URL is null -->> URL is null,request -->> {}", exchange.advanced().getRequest());
                return;
            }
            if (!downloads.equals(DOWNLOADS)) {
                exchange.respond(ResponseCode.NOT_FOUND, "URL is error");
                logger.warn("Download URL is error -->> URL is error,request -->> {}", exchange.advanced().getRequest());
                return;
            }

            // 改为https，不需要文件真实路径
            // if (os.contains("win")) {
            //     downUrl = "E:/" + downUrl;
            // } else {
            //     downUrl = DOWNLOAD_PATH + downUrl;
            // }

            logger.info("Download URL -->> {}, request -->> {}", downUrl, exchange.advanced().getRequest());
            // File file = new File(downUrl);
            // if (file.exists()) {
            //     FileInputStream inputStream = new FileInputStream(file);
            //     // get length of file for buffer
            //     int fileLength = (int) file.length();
            //     byte[] fileData = new byte[fileLength];
            //     inputStream.read(fileData);
            //     inputStream.close();
            //     exchange.respond(ResponseCode.CONTENT, fileData);
            //     logger.info("Download success -->> {},request -->> {}", ResponseCode.CONTENT, exchange.advanced().getRequest());
            // } else {
            //     exchange.respond(ResponseCode.NOT_FOUND);
            //     logger.warn("Download file is not found -->> file is not found,request -->> {}", exchange.advanced().getRequest());
            //     return;
            // }

            CoapConfiguration coapConfiguration = ActivitiConfig.getBean(CoapConfiguration.class);
            String httpUrl = coapConfiguration.getUrl();
            //真正的下载地址
            String completeUrl = httpUrl + downUrl;
            logger.info("completeUrl==============>:{}",completeUrl);

            HttpURLConnection connection = null;
            ByteArrayOutputStream baos = null;
            InputStream inputStream = null;
            try {
                URL url = new URL(completeUrl);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                inputStream = connection.getInputStream();
                // 获取远程资源长度
                int fileSize = connection.getContentLength();
                byte[] fileData = new byte[fileSize];
                baos = new ByteArrayOutputStream();
                for ( int len = 0; (len = inputStream.read(fileData)) > 0;) {
                    baos.write(fileData, 0, len);
                }
                byte[] baosBytes = baos.toByteArray();

                exchange.respond(ResponseCode.CONTENT, baosBytes);

                baos.flush();
                logger.info(" [" + sdf.format(new Date()) + "] [INFO ] Download success -->> {},request -->> {}", ResponseCode.CONTENT, exchange.advanced().getRequest());

            } catch (FileNotFoundException e) {
                exchange.respond(ResponseCode.NOT_FOUND);
                logger.info(" [" + sdf.format(new Date()) + "] [INFO ] Download file is not found -->> file is not found,request -->> {}", exchange.advanced().getRequest());
            }finally {
                if(baos != null){
                    baos.close();
                }

                if(inputStream!=null){
                    inputStream.close();
                }

                if(connection!=null){
                    connection.disconnect();
                }
            }

        } catch (Exception e) {
            exchange.respond(ResponseCode.INTERNAL_SERVER_ERROR, "I/O error");
            logger.warn("Download Exception -->> I/O error,request -->> {}", exchange.advanced().getRequest());
            logger.error("download error", e);
        }
    }

    @Override
    public Resource getChild(String name) {
        return this;
    }
}
