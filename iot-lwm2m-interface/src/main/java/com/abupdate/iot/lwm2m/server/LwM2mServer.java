package com.abupdate.iot.lwm2m.server;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.CoapServer;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.network.CoapEndpoint;
import org.eclipse.californium.core.network.Endpoint;
import org.eclipse.californium.core.network.config.NetworkConfig;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.eclipse.californium.core.server.resources.Resource;
import org.eclipse.lwm2m.core.node.codec.CodecException;
import org.eclipse.lwm2m.core.node.codec.LwM2mNodeDecoder;
import org.eclipse.lwm2m.core.node.codec.LwM2mNodeEncoder;
import org.eclipse.lwm2m.core.observation.Observation;
import org.eclipse.lwm2m.core.request.DownlinkRequest;
import org.eclipse.lwm2m.core.request.exception.InvalidResponseException;
import org.eclipse.lwm2m.core.request.exception.RequestCanceledException;
import org.eclipse.lwm2m.core.request.exception.RequestRejectedException;
import org.eclipse.lwm2m.core.response.ErrorCallback;
import org.eclipse.lwm2m.core.response.LwM2mResponse;
import org.eclipse.lwm2m.core.response.ResponseCallback;
import org.eclipse.lwm2m.server.Destroyable;
import org.eclipse.lwm2m.server.Startable;
import org.eclipse.lwm2m.server.Stoppable;
import org.eclipse.lwm2m.server.impl.RegistrationServiceImpl;
import org.eclipse.lwm2m.server.model.LwM2mModelProvider;
import org.eclipse.lwm2m.server.observation.ObservationService;
import org.eclipse.lwm2m.server.registration.Registration;
import org.eclipse.lwm2m.server.registration.RegistrationHandler;
import org.eclipse.lwm2m.server.registration.RegistrationListener;
import org.eclipse.lwm2m.server.registration.RegistrationService;
import org.eclipse.lwm2m.server.registration.RegistrationUpdate;
import org.eclipse.lwm2m.server.request.LwM2mRequestSender;
import org.eclipse.lwm2m.server.security.Authorizer;
import org.eclipse.lwm2m.server.security.SecurityInfo;
import org.eclipse.lwm2m.server.security.SecurityStore;
import org.eclipse.lwm2m.util.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.abupdate.iot.lwm2m.impl.CaliforniumLwM2mRequestSender;
import com.abupdate.iot.lwm2m.impl.CaliforniumRegistrationStore;
import com.abupdate.iot.lwm2m.impl.ObservationServiceImpl;
import com.abupdate.iot.lwm2m.resource.EntranceResource;

/**
 * @author wangxiaojing
 * @company adups
 * @date 2018年1月4日
 **/
public class LwM2mServer {
    private static final Logger LOG = LoggerFactory.getLogger(LwM2mServer.class);

    private final CoapServer coapServer;

    private final LwM2mRequestSender requestSender;

    private final RegistrationServiceImpl registrationService;

    private final ObservationServiceImpl observationService;

    private final SecurityStore securityStore;

    private final LwM2mModelProvider modelProvider;

    private final CoapEndpoint unsecuredEndpoint;

    private final CoapEndpoint securedEndpoint;

    private final CaliforniumRegistrationStore registrationStore;

    /**
     * Initialize a server which will bind to the specified address and port.
     *
     * @param unsecuredEndpoint the unsecure coap endpoint.
     * @param securedEndpoint   the secure coap endpoint.
     * @param registrationStore the {@link Registration} store.
     * @param securityStore     the {@link SecurityInfo} store.
     * @param authorizer        define which devices is allow to register on this server.
     * @param modelProvider     provides the objects description for each client.
     * @param decoder           decoder used to decode response payload.
     * @param encoder           encode used to encode request payload.
     * @param coapConfig        the CoAP {@link NetworkConfig}.
     */
    public LwM2mServer(CoapEndpoint unsecuredEndpoint, CoapEndpoint securedEndpoint, CaliforniumRegistrationStore registrationStore,
                       SecurityStore securityStore, Authorizer authorizer, LwM2mModelProvider modelProvider, LwM2mNodeEncoder encoder,
                       LwM2mNodeDecoder decoder, NetworkConfig coapConfig) {

        Validate.notNull(registrationStore, "registration store cannot be null");
        Validate.notNull(authorizer, "authorizer cannot be null");
        Validate.notNull(modelProvider, "modelProvider cannot be null");
        Validate.notNull(encoder, "encoder cannot be null");
        Validate.notNull(decoder, "decoder cannot be null");
        Validate.notNull(coapConfig, "coapConfig cannot be null");

        // Init services and stores
        this.registrationStore = registrationStore;
        this.registrationService = new RegistrationServiceImpl(registrationStore);
        this.securityStore = securityStore;
        this.observationService = new ObservationServiceImpl(registrationStore, modelProvider, decoder);
        this.modelProvider = modelProvider;

        // Cancel observations on client unregistering
        this.registrationService.addListener(new RegistrationListener() {
            @Override
            public void updated(RegistrationUpdate update, Registration updatedRegistration,
                                Registration previousRegistration) {
            }

            @Override
            public void unregistered(Registration registration, Collection<Observation> observations, boolean expired,
                                     Registration newReg) {
                requestSender.cancelPendingRequests(registration);
            }

            @Override
            public void registered(Registration registration, Registration previousReg,
                                   Collection<Observation> previousObsersations) {
            }
        });

        // define a set of endpoints
        Set<Endpoint> endpoints = new HashSet<>();
        coapServer = new CoapServer(coapConfig) {
            @Override
            protected Resource createRoot() {
                return new RootResource();
            }
        };

        // default endpoint coap
        this.unsecuredEndpoint = unsecuredEndpoint;
        if (unsecuredEndpoint != null) {
            unsecuredEndpoint.addNotificationListener(observationService);
            observationService.setNonSecureEndpoint(unsecuredEndpoint);
            coapServer.addEndpoint(unsecuredEndpoint);
            endpoints.add(unsecuredEndpoint);
        }

        // secure endpoint coaps
        this.securedEndpoint = securedEndpoint;
        if (securedEndpoint != null) {
            securedEndpoint.addNotificationListener(observationService);
            observationService.setSecureEndpoint(securedEndpoint);
            coapServer.addEndpoint(securedEndpoint);
            endpoints.add(securedEndpoint);
        }

        // define /rd resource
        EntranceResource rdResource = new EntranceResource(new RegistrationHandler(this.registrationService, authorizer), this);
        coapServer.add(rdResource);

        // create sender
        requestSender = new CaliforniumLwM2mRequestSender(endpoints, this.observationService, modelProvider, encoder, decoder);
    }

    public void start() {
        // Start stores
        if (registrationStore instanceof Startable) {
            ((Startable) registrationStore).start();
        }
        if (securityStore instanceof Startable) {
            ((Startable) securityStore).start();
        }

        // Start server
        coapServer.start();
        if (LOG.isInfoEnabled()) {
            LOG.info("LWM2M server started at {} {}", getUnsecuredAddress() == null ? "" : "coap://" + getUnsecuredAddress(),
                    getSecuredAddress() == null ? "" : "coaps://" + getSecuredAddress());
        }
    }

    public void stop() {
        // Stop server
        coapServer.stop();

        // Stop stores
        if (registrationStore instanceof Stoppable) {
            ((Stoppable) registrationStore).stop();
        }
        if (securityStore instanceof Stoppable) {
            ((Stoppable) securityStore).stop();
        }

        LOG.info("LWM2M server stopped.");
    }

    public void destroy() {
        // Destroy server
        coapServer.destroy();

        // Destroy stores
        if (registrationStore instanceof Destroyable) {
            ((Destroyable) registrationStore).destroy();
        } else if (registrationStore instanceof Stoppable) {
            ((Stoppable) registrationStore).stop();
        }

        if (securityStore instanceof Destroyable) {
            ((Destroyable) securityStore).destroy();
        } else if (securityStore instanceof Stoppable) {
            ((Stoppable) securityStore).stop();
        }

        LOG.info("LWM2M server destroyed.");
    }

    public <T extends LwM2mResponse> T send(Registration destination, DownlinkRequest<T> request)
            throws InterruptedException, CodecException, InvalidResponseException, RequestCanceledException,
            RequestRejectedException {
        return requestSender.send(destination, request, null);
    }

    public <T extends LwM2mResponse> T send(Registration destination, DownlinkRequest<T> request, long timeout)
            throws InterruptedException, CodecException, InvalidResponseException, RequestCanceledException,
            RequestRejectedException {
        LOG.info("LwM2mResponse==========>"+request+"destination====>"+destination);
        return requestSender.send(destination, request, timeout);
    }

    public <T extends LwM2mResponse> void send(Registration destination, DownlinkRequest<T> request,
                                               ResponseCallback<T> responseCallback, ErrorCallback errorCallback) throws CodecException {
        requestSender.send(destination, request, responseCallback, errorCallback);
    }

    public RegistrationService getRegistrationService() {
        return this.registrationService;
    }

    public ObservationService getObservationService() {
        return this.observationService;
    }

    public SecurityStore getSecurityStore() {
        return this.securityStore;
    }

    public LwM2mModelProvider getModelProvider() {
        return this.modelProvider;
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

    public InetSocketAddress getSecuredAddress() {
        if (securedEndpoint != null) {
            return securedEndpoint.getAddress();
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
