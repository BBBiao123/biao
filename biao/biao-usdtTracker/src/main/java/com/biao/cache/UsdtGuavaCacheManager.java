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
import com.biao.mapper.CoinBlockDao;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.Weigher;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

/**
 * use google guava cache.
 *
 *  ""
 */
@Component
public class UsdtGuavaCacheManager {

    private static final int MAX_COUNT = 10000;

    private final CoinBlockDao coinBlockDao;

    private final LoadingCache<String, Integer> COIN_BLOCK_CACHE =
            CacheBuilder.newBuilder().maximumWeight(MAX_COUNT)
                    .weigher((Weigher<String, Integer>) (string, integer) -> getSize())
                    .build(new CacheLoader<String, Integer>() {
                        @Override
                        public Integer load(final String key) {
                            return cacheCoinBlockHeight(key);
                        }
                    });

    @Autowired(required = false)
    public UsdtGuavaCacheManager(CoinBlockDao coinBlockDao) {
        this.coinBlockDao = coinBlockDao;
    }

    private int getSize() {
        return (int) COIN_BLOCK_CACHE.size();
    }

    private Integer cacheCoinBlockHeight(final String symbol) {
        return Optional.ofNullable(coinBlockDao.findBySmybol(symbol).getBlockHeight()).orElse(500);
    }


    public void cacheBlockHeight(final String symbol, final Integer height) {
        COIN_BLOCK_CACHE.put(symbol, height);
    }

    /**
     * acquire getBlockHeight.
     *
     * @param symbol this guava key.
     * @return {@linkplain Coin}
     */
    public Integer getBlockHeight(final String symbol) {
        try {
            return COIN_BLOCK_CACHE.get(symbol);
        } catch (ExecutionException e) {
            return 500;
        }
    }

    /**
     * remove guava cache by key.
     *
     * @param symbol guava cache key.
     */
    public void removeByKey(final String symbol) {
        if (StringUtils.isNotEmpty(symbol)) {
            COIN_BLOCK_CACHE.invalidate(symbol);
        }
    }

}
