/*
 *
 * Copyright 2017-2018 ""611@qq.com(ttt)
 *
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU
 * Lesser General Public License, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this distribution; if not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.biao.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * this is netty config.
 *
 *  ""
 */
@Data
@ConfigurationProperties(prefix = "biao.netty.websocket")
@Configuration
public class NettyConfig {

    /**
     * 启动服务端口.
     */
    private int port;

    /**
     * 最大线程数.
     */
    private int maxThreads = Runtime.getRuntime().availableProcessors() << 2;

    /**
     * 是否使用wss SSL
     */
    private boolean isSSL = false;

}

