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
package org.eclipse.lwm2m.core.californium;

import org.eclipse.californium.core.coap.MessageObserverAdapter;
import org.eclipse.californium.core.coap.Request;
import org.eclipse.californium.core.coap.Response;
import org.eclipse.lwm2m.core.response.LwM2mResponse;

/**
 * 消息观察类，当消息发生变化时调用相应的方法
 *
 onResponse() 当收到响应时回调
 onAcknowledgement() 当收到应答时回调
 onReject() 当请求消息被拒绝时回调
 onTimeout() 当客户端停止重传且仍然没有从对端收到任何消息时回调
 onCancel() 当消息被取消时回调
 *
 * @param <T>
 */
public abstract class AbstractRequestObserver<T extends LwM2mResponse> extends MessageObserverAdapter {
    Request coapRequest;

    public AbstractRequestObserver(Request coapRequest) {
        this.coapRequest = coapRequest;
    }

    public abstract T buildResponse(Response coapResponse);
}