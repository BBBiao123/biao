package com.biao.configuration.coin;

import com.biao.coin.CoinMainService;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The type Coin main configuration.
 *
 *  ""(Myth)
 */
@Configuration
public class CoinMainConfiguration {

    /**
     * Coin main service coin main service.
     *
     * @return the coin main service
     */
    @Bean
    @ConfigurationProperties(prefix = "coin.main")
    CoinMainService coinMainService() {
        return new CoinMainService();
    }


}
