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

package com.biao.netty.initializer;

import com.biao.config.NettyConfig;
import com.biao.netty.handler.WebSocketServerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * NettyServerHandlerInitializer.
 *
 *  ""
 */
@Component
public class NettyServerHandlerInitializer extends ChannelInitializer<SocketChannel> {

    private final NettyConfig nettyConfig;

    private final WebSocketServerHandler webSocketServerHandler;


    private DefaultEventExecutorGroup servletExecutor;

    @Autowired(required = false)
    public NettyServerHandlerInitializer(final NettyConfig nettyConfig,
                                         final WebSocketServerHandler webSocketServerHandler) {
        this.nettyConfig = nettyConfig;
        this.webSocketServerHandler = webSocketServerHandler;
    }

    public void setServletExecutor(final DefaultEventExecutorGroup servletExecutor) {
        this.servletExecutor = servletExecutor;
    }

    @Override
    protected void initChannel(final SocketChannel ch) {
        final ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new HttpServerCodec());
        pipeline.addLast(new HttpObjectAggregator(65536));
        pipeline.addLast(new WebSocketServerCompressionHandler());
        pipeline.addLast("http-chunked", new ChunkedWriteHandler());
        pipeline.addLast(webSocketServerHandler);
    }
}
