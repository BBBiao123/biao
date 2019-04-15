package com.biao.netty.handler;

import com.biao.constant.WebsocketConstant;
import com.biao.current.threadpool.BbexThreadFactory;
import com.biao.netty.NettyWebsocketHandler;
import com.biao.service.LuckyDrawService;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.util.concurrent.DefaultEventExecutor;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * LuckyDrawWebsocketHandler.
 *
 *  ""zj
 */
@Component
public class LuckyDrawWebsocketHandler extends AbstractHandshakeHandler
        implements NettyWebsocketHandler, InitializingBean {

    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(LuckyDrawWebsocketHandler.class);

    private final ChannelGroup LUCKY_CHANNEL_GROUP = new DefaultChannelGroup("lucky",
            new DefaultEventExecutor());

    private ScheduledExecutorService scheduledExecutorService =
            new ScheduledThreadPoolExecutor(1,
                    BbexThreadFactory.create("replay-task", false));


    @Override
    public String path() {
        return WebsocketConstant.WS_LUCKY_DRAW;
    }

    @Override
    public void remove(Channel channel) {
        LUCKY_CHANNEL_GROUP.remove(channel);
    }

    @Override
    public ChannelFuture handshake(ChannelHandlerContext ctx, FullHttpRequest req) {
        final ChannelFuture handshake = super.handshake(ctx, req, path());
        if (Objects.nonNull(handshake)) {
            LUCKY_CHANNEL_GROUP.add(ctx.channel());
        }
        return handshake;
    }

    @Override
    public void handleWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {
    }

    @Override
    public void afterPropertiesSet() {
       /* scheduledExecutorService
                .scheduleWithFixedDelay(() -> {
                    LOGGER.debug("定时执行推送中奖信息，同步间隔时间为：{} 秒", replayTime);
                    try {
                        String data = getData();
                        if (StringUtils.isNoneBlank(data)) {
                            LUCKY_CHANNEL_GROUP.writeAndFlush(new TextWebSocketFrame(data));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }, 30, replayTime, TimeUnit.SECONDS);*/
    }

}
