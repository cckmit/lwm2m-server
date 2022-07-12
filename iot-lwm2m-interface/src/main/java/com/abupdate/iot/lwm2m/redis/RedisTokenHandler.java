/*******************************************************************************
 * Copyright (c) 2016 Sierra Wireless and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v1.0 which accompany this distribution.
 *
 * The Eclipse Public License is available at
 *    http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 *    http://www.eclipse.org/org/documents/edl-v10.html.
 *
 * Contributors:
 *     Sierra Wireless - initial API and implementation
 *******************************************************************************/
package com.abupdate.iot.lwm2m.redis;

import java.util.Arrays;
import java.util.Collection;

import org.eclipse.lwm2m.core.observation.Observation;
import org.eclipse.lwm2m.server.registration.Registration;
import org.eclipse.lwm2m.server.registration.RegistrationListener;
import org.eclipse.lwm2m.server.registration.RegistrationUpdate;
import org.eclipse.lwm2m.util.Validate;

import redis.clients.jedis.Jedis;
//import redis.clients.jedis.util.Pool;
import redis.clients.util.Pool;

/**
 * Only one cluster instance can be responsible of a given LWM2M client at a given moment. (This restriction is mainly
 * due to the DTLS session)</br>
 * This class store the couple Cluster instance / LwM2M client in a Redis Store.</br>
 * Each Cluster instance is identified by a unique UI and each device by its endpoint.
 */
public class RedisTokenHandler implements RegistrationListener {

    private static final String EP_UID = "EP#UID#";
    private final Pool<Jedis> pool;
    private final String instanceUID;

    public RedisTokenHandler(Pool<Jedis> j, String instanceUID) {
        Validate.notNull(instanceUID);
        this.instanceUID = instanceUID;
        this.pool = j;
    }

    @Override
    public void registered(Registration registration, Registration previousReg,
                           Collection<Observation> previousObsersations) {
        try (Jedis j = pool.getResource()) {
            // create registration entry
            byte[] k = (EP_UID + registration.getEndpoint()).getBytes();
            j.set(k, instanceUID.getBytes());
            j.expire(k, registration.getLifeTimeInSec().intValue());
        }
    }

    @Override
    public void updated(RegistrationUpdate update, Registration updatedRegistration,
                        Registration previousRegistration) {
        try (Jedis j = pool.getResource()) {
            // create registration entry
            byte[] k = (EP_UID + updatedRegistration.getEndpoint()).getBytes();
            j.set(k, instanceUID.getBytes());
            j.expire(k, updatedRegistration.getLifeTimeInSec().intValue());
        }
    }

    @Override
    public void unregistered(Registration registration, Collection<Observation> observations, boolean expired,
                             Registration newReg) {
        try (Jedis j = pool.getResource()) {
            // create registration entry
            j.del((EP_UID + registration.getEndpoint()).getBytes());
        }
    }

    public boolean isResponsible(String endpoint) {
        try (Jedis j = pool.getResource()) {
            byte[] k = (EP_UID + endpoint).getBytes();
            byte[] data = j.get(k);
            return data != null && Arrays.equals(data, instanceUID.getBytes());
        }
    }
}
