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

package com.biao.netty.bootstrap;

import com.biao.config.NettyConfig;
import com.biao.netty.initializer.NettyServerHandlerInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * NettyServerServiceImpl.
 *
 *  ""
 */
@Component
public class NettyServerBootstrap implements DisposableBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(NettyServerBootstrap.class);

    private EventLoopGroup bossGroup;

    private EventLoopGroup workerGroup;

    private DefaultEventExecutorGroup servletExecutor;

    private final NettyConfig nettyConfig;

    private final NettyServerHandlerInitializer nettyServerHandlerInitializer;

    @Autowired(required = false)
    public NettyServerBootstrap(final NettyConfig nettyConfig,
                                final NettyServerHandlerInitializer nettyServerHandlerInitializer) {
        this.nettyConfig = nettyConfig;
        this.nettyServerHandlerInitializer = nettyServerHandlerInitializer;
    }

    public void start() throws InterruptedException {
        servletExecutor = new DefaultEventExecutorGroup(nettyConfig.getMaxThreads());
        nettyServerHandlerInitializer.setServletExecutor(servletExecutor);
        ServerBootstrap b = new ServerBootstrap();
        bossGroup = createEventLoopGroup();
        if (bossGroup instanceof EpollEventLoopGroup) {
            groupsEpoll(b, nettyConfig.getMaxThreads());
        } else {
            groupsNio(b, nettyConfig.getMaxThreads());
        }
        try {
            LOGGER.info("netty service started on port: " + nettyConfig.getPort());
            ChannelFuture future = b.bind(nettyConfig.getPort()).sync();
            future.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
            servletExecutor.shutdownGracefully();
        }

    }

    private EventLoopGroup createEventLoopGroup() {
        try {
            return new EpollEventLoopGroup(1);
        } catch (Throwable ex) {
            return new NioEventLoopGroup(1);
        }
    }

    private void groupsEpoll(final ServerBootstrap bootstrap, final int workThreads) {
        workerGroup = new EpollEventLoopGroup(workThreads);
        bootstrap.group(bossGroup, workerGroup)
                .channel(EpollServerSocketChannel.class)
                .option(EpollChannelOption.TCP_CORK, true)
                .option(EpollChannelOption.SO_KEEPALIVE, true)
                .option(EpollChannelOption.SO_BACKLOG, 100)
                .option(EpollChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .childOption(EpollChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(nettyServerHandlerInitializer);
    }

    private void groupsNio(final ServerBootstrap bootstrap, final int workThreads) {
        workerGroup = new NioEventLoopGroup(workThreads);
        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(EpollChannelOption.TCP_CORK, true)
                .option(EpollChannelOption.SO_KEEPALIVE, true)
                .option(EpollChannelOption.SO_BACKLOG, 100)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 100)
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(nettyServerHandlerInitializer);
    }

    private void stop() {
        try {
            if (null != bossGroup) {
                bossGroup.shutdownGracefully().await();
            }
            if (null != workerGroup) {
                workerGroup.shutdownGracefully().await();
            }
            if (null != servletExecutor) {
                servletExecutor.shutdownGracefully().await();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(" Netty  Container stop interrupted", e);
        }

    }

    @Override
    public void destroy() {
        stop();
    }

}
