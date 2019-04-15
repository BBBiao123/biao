package com.biao.config;

import com.biao.client.MyOmniClient;
import com.googlecode.jsonrpc4j.JsonRpcHttpClient;
import org.apache.commons.codec.binary.Base64;
import org.bitcoinj.params.MainNetParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * UsdtTrackerConfiguration.
 *
 *  ""
 */
@Configuration
public class OmniConfiguration {

    private final OmniConfig omniConfig;

    /**
     * Instantiates a new Omni configuration.
     *
     * @param omniConfig the omni config
     */
    @Autowired(required = false)
    public OmniConfiguration(OmniConfig omniConfig) {
        this.omniConfig = omniConfig;
    }

    /**
     * Omni client my omni client.
     *
     * @return the my omni client
     * @throws Exception the exception
     */
    @Bean
    public MyOmniClient omniClient() throws Exception {
        return new MyOmniClient(MainNetParams.get(),
                new URI(omniConfig.getUri()), omniConfig.getUsername(), omniConfig.getPassword());
    }

    /**
     * Json rpc http client json rpc http client.
     *
     * @return the json rpc http client
     */
    @Bean
    public JsonRpcHttpClient jsonRpcHttpClient() {
        String creb = Base64.encodeBase64String((omniConfig.getUsername() + ":" + omniConfig.getPassword()).getBytes());
        Map<String, String> headers = new HashMap<>(2);
        headers.put("Authorization", "Basic " + creb);
        headers.put("server", "1");
        try {
            return new JsonRpcHttpClient(new URL(omniConfig.getUri()), headers);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }


}
