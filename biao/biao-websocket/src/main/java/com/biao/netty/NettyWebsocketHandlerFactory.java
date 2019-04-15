package com.biao.netty;

import java.util.HashMap;
import java.util.Map;

/**
 *  ""(Myth)
 */
public class NettyWebsocketHandlerFactory {

    private final Map<String, NettyWebsocketHandler> handlerMap = new HashMap<>();

    public void setHandlerMap(Map<String, NettyWebsocketHandler> urlMap) {
        this.handlerMap.putAll(urlMap);
    }

    public NettyWebsocketHandler factoryOf(String path) {
        return handlerMap.get(path);
    }

}
