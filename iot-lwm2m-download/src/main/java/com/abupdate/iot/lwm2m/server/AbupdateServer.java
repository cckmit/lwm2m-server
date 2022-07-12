package com.abupdate.iot.lwm2m.server;

import com.abupdate.iot.lwm2m.impl.LwM2mCoapServer;
import com.abupdate.iot.lwm2m.util.Validate;
import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.CoapServer;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.network.CoapEndpoint;
import org.eclipse.californium.core.network.Endpoint;
import org.eclipse.californium.core.network.config.NetworkConfig;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.eclipse.californium.core.server.resources.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * @author wangxiaojing
 * @company adups
 * @date 2018年1月3日
 **/
public class AbupdateServer implements LwM2mCoapServer {
    private Logger logger = LoggerFactory.getLogger(AbupdateServer.class);

    private final CoapServer coapServer;

    private final CoapEndpoint unsecuredEndpoint;

    public AbupdateServer(NetworkConfig coapConfig, CoapEndpoint unsecuredEndpoint) {
        Validate.notNull(coapConfig, "coapConfig cannot be null");

        coapServer = new CoapServer(coapConfig) {
            @Override
            protected Resource createRoot() {
                return new RootResource();
            }
        };

        this.unsecuredEndpoint = unsecuredEndpoint;
        if (unsecuredEndpoint != null) {
            coapServer.addEndpoint(unsecuredEndpoint);
        }

        DownloadResource downloadResource = new DownloadResource(this);
        coapServer.add(downloadResource);
    }


    public void start() {
        coapServer.start();

        if (logger.isInfoEnabled()) {
            logger.info("LWM2M server started at {}",
                    getUnsecuredAddress() == null ? "" : "coap://" + getUnsecuredAddress());
        }
    }


    public void stop() {
        coapServer.stop();
    }


    public void destroy() {
        coapServer.destroy();
    }

    /**
     * @return the underlying {@link CoapServer}
     */
    public CoapServer getCoapServer() {
        return coapServer;
    }

    public InetSocketAddress getUnsecuredAddress() {
        if (unsecuredEndpoint != null) {
            return unsecuredEndpoint.getAddress();
        } else {
            return null;
        }
    }

    /**
     * The Leshan Root Resource.
     */
    private class RootResource extends CoapResource {

        public RootResource() {
            super("");
        }

        @Override
        public void handleGET(CoapExchange exchange) {
            exchange.respond(ResponseCode.NOT_FOUND);
        }

        @Override
        public List<Endpoint> getEndpoints() {
            return coapServer.getEndpoints();
        }
    }
}
