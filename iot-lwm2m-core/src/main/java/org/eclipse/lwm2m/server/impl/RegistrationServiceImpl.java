/*******************************************************************************
 * Copyright (c) 2013-2015 Sierra Wireless and others.
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
package org.eclipse.lwm2m.server.impl;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.eclipse.lwm2m.core.observation.Observation;
import org.eclipse.lwm2m.server.registration.ExpirationListener;
import org.eclipse.lwm2m.server.registration.Registration;
import org.eclipse.lwm2m.server.registration.RegistrationListener;
import org.eclipse.lwm2m.server.registration.RegistrationService;
import org.eclipse.lwm2m.server.registration.RegistrationStore;
import org.eclipse.lwm2m.server.registration.RegistrationUpdate;

/**
 * An implementation of {@link RegistrationService}
 */
public class RegistrationServiceImpl implements RegistrationService, ExpirationListener {

    private final List<RegistrationListener> listeners = new CopyOnWriteArrayList<>();

    private RegistrationStore store;

    public RegistrationServiceImpl(RegistrationStore store) {
        this.store = store;
        store.setExpirationListener(this);
    }

    @Override
    public void addListener(RegistrationListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(RegistrationListener listener) {
        listeners.remove(listener);
    }

    @Override
    public Iterator<Registration> getAllRegistrations() {
        return store.getAllRegistrations();
    }

    @Override
    public Registration getByEndpoint(String endpoint, Object key) {
        return store.getRegistrationByEndpoint(endpoint, key);
    }

    @Override
    public Registration getById(String id) {
        return store.getRegistration(id);
    }

    @Override
    public void registrationExpired(Registration registration, Collection<Observation> observations) {
        for (RegistrationListener l : listeners) {
            l.unregistered(registration, observations, true, null);
        }
    }

    public void fireRegistered(Registration registration, Registration previousReg,
                               Collection<Observation> previousObsersations) {
        for (RegistrationListener l : listeners) {
            l.registered(registration, previousReg, previousObsersations);
        }
    }

    public void fireUnregistered(Registration registration, Collection<Observation> observations, Registration newReg) {
        for (RegistrationListener l : listeners) {
            l.unregistered(registration, observations, false, newReg);
        }
    }

    public void fireUpdated(RegistrationUpdate update, Registration updatedRegistration,
                            Registration previousRegistration) {
        for (RegistrationListener l : listeners) {
            l.updated(update, updatedRegistration, previousRegistration);
        }
    }

    public RegistrationStore getStore() {
        return store;
    }
}
