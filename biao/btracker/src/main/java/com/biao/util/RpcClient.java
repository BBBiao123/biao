package com.biao.util;

import com.azazar.bitcoin.jsonrpcclient.BitcoinJSONRPCClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;

public class RpcClient {
    private volatile static BitcoinJSONRPCClient client = null;
    private static final Logger logger = LoggerFactory.getLogger(RpcClient.class);

    private RpcClient() {
    }

    public static BitcoinJSONRPCClient getClient(String urlStr) {
        try {
            if (null == client) {
                synchronized (RpcClient.class) {
                    if (null == client) {
                        URL url = new URL(urlStr);
                        client = new BitcoinJSONRPCClient(url);
                    }
                }
            }
            return client;
        } catch (MalformedURLException e) {
            logger.error("get client faild:{}", e.getMessage(), e);
            return client;
        }
    }
}
