package com.biao.netty.handler;

import com.biao.constant.WebsocketConstant;
import com.biao.current.threadpool.BbexThreadFactory;
import com.biao.netty.NettyWebsocketHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.util.concurrent.DefaultEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * ReplayWebsocketHandler.
 * <p>
 * ""(Myth)
 */
@Component
public class ReplayWebsocketHandler extends AbstractHandshakeHandler
        implements NettyWebsocketHandler, InitializingBean {

    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ReplayWebsocketHandler.class);

    @Value("${replayTime:5}")
    private int replayTime;

    private final ChannelGroup REPLAY_CHANNEL_GROUP = new DefaultChannelGroup("replay",
            new DefaultEventExecutor());

    private ScheduledExecutorService scheduledExecutorService =
            new ScheduledThreadPoolExecutor(1,
                    BbexThreadFactory.create("replay-task", false));

    @Override
    public String path() {
        return WebsocketConstant.WS_RELAY_PRIZE;
    }

    @Override
    public void remove(Channel channel) {
        REPLAY_CHANNEL_GROUP.remove(channel);
    }

    @Override
    public ChannelFuture handshake(ChannelHandlerContext ctx, FullHttpRequest req) {
        final ChannelFuture handshake = super.handshake(ctx, req, path());
        if (Objects.nonNull(handshake)) {
            REPLAY_CHANNEL_GROUP.add(ctx.channel());
        }
        return handshake;
    }

    @Override
    public void handleWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {
    }


    @Override
    public void afterPropertiesSet() {
    }

}
