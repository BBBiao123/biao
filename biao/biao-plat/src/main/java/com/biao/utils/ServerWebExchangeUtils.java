package com.biao.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;

public class ServerWebExchangeUtils {

    private static Logger logger = LoggerFactory.getLogger(ServerWebExchangeUtils.class);

    /**
     * 获取客户端IP
     *
     * @param exchange
     * @return
     */
    public static String getRemoteAddr(ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();
        String remoteAddr = request.getHeaders().getFirst("X-Forwarded-For");
        if (StringUtils.isBlank(remoteAddr)) {
            remoteAddr = request.getHeaders().getFirst("X-Real-IP");
        }
        if (StringUtils.isBlank(remoteAddr)) {
            remoteAddr = request.getHeaders().getFirst("Proxy-Client-IP");
        }
        if (StringUtils.isBlank(remoteAddr)) {
            remoteAddr = request.getHeaders().getFirst("WL-Proxy-Client-IP");
        }
        return remoteAddr != null ? remoteAddr : request.getRemoteAddress().getAddress().getHostAddress();
    }
}