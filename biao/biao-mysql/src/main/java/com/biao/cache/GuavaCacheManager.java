/*
 *
 *  * Licensed to the Apache Software Foundation (ASF) under one or more
 *  * contributor license agreements.  See the NOTICE file distributed with
 *  * this work for additional information regarding copyright ownership.
 *  * The ASF licenses this file to You under the Apache License, Version 2.0
 *  * (the "License"); you may not use this file except in compliance with
 *  * the License.  You may obtain a copy of the License at
 *  *
 *  *     http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package com.biao.cache;

import com.biao.entity.Coin;
import com.biao.service.CoinService;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.Weigher;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * use google guava cache.
 *
 *  ""
 */
@Component
public class GuavaCacheManager {

    private static final int MAX_COUNT = 10000;

    @Autowired
    private CoinService coinService;

    private final LoadingCache<String, Coin> COIN_CACHE =
            CacheBuilder.newBuilder().maximumWeight(MAX_COUNT)
                    .expireAfterWrite(100, TimeUnit.SECONDS)
                    .weigher((Weigher<String, Coin>) (string, tccTransaction) -> getSize())
                    .build(new CacheLoader<String, Coin>() {
                        @Override
                        public Coin load(final String key) {
                            return cacheCoin(key);
                        }
                    });


    private int getSize() {
        return (int) COIN_CACHE.size();
    }

    private Coin cacheCoin(final String name) {
        return Optional.ofNullable(coinService.findByName(name)).orElse(new Coin());
    }

    /**
     * cache coin.
     *
     * @param coin {@linkplain Coin}
     */
    public void cacheCoin(final Coin coin) {
        COIN_CACHE.put(coin.getName(), coin);
    }

    /**
     * acquire Coin.
     *
     * @param key this guava key.
     * @return {@linkplain Coin}
     */
    public Coin getTccTransaction(final String key) {
        try {
            return COIN_CACHE.get(key);
        } catch (ExecutionException e) {
            return new Coin();
        }
    }

    /**
     * remove guava cache by key.
     *
     * @param key guava cache key.
     */
    public void removeByKey(final String key) {
        if (StringUtils.isNotEmpty(key)) {
            COIN_CACHE.invalidate(key);
        }
    }

}
