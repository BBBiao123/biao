package com.biao.netty.handler;

import com.biao.constant.WebsocketConstant;
import com.biao.netty.NettyWebsocketHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.util.concurrent.DefaultEventExecutor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The type Kline websocket handler.
 *
 *  ""(Myth)
 */
@Component
public class KlineWebsocketHandler extends AbstractHandshakeHandler implements NettyWebsocketHandler {

    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(KlineWebsocketHandler.class);

    private static final Map<String, ChannelGroup> KLINE_CHANNEL_MAP = new ConcurrentHashMap<>();

    private static final int LENGTH = 2;

    @Override
    public String path() {
        return WebsocketConstant.WS_KLINE;
    }

    @Override
    public void remove(Channel channel) {
        final String key = channel.attr(WebsocketConstant.KLINE_KEY).get();
        if (StringUtils.isNoneBlank(key)) {
            Optional.ofNullable(KLINE_CHANNEL_MAP.get(key))
                    .ifPresent(channels -> channels.remove(channel));
        }
    }

    @Override
    public ChannelFuture handshake(ChannelHandlerContext ctx, FullHttpRequest req) {
        return super.handshake(ctx, req, path());
    }

    @Override
    public void handleWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {
        try {
            final String text = ((TextWebSocketFrame) frame).text();
            if (StringUtils.isNoneBlank(text)) {
                String[] split = StringUtils.split(text, WebsocketConstant.JOIN);
                if (split.length == LENGTH) {
                    final String symbol = ctx.channel().attr(WebsocketConstant.KLINE_KEY).get();
                    if (StringUtils.isNoneBlank(symbol)) {
                        //把之前的删除
                        KLINE_CHANNEL_MAP.get(symbol).remove(ctx.channel());
                    }
                    if (KLINE_CHANNEL_MAP.containsKey(text)) {
                        KLINE_CHANNEL_MAP.get(text).add(ctx.channel());
                    } else {
                        ChannelGroup channelGroup = new DefaultChannelGroup("kline",
                                new DefaultEventExecutor());
                        channelGroup.add(ctx.channel());
                        KLINE_CHANNEL_MAP.put(text, channelGroup);
                    }
                    ctx.channel().attr(WebsocketConstant.KLINE_KEY).set(text);
                } else {
                    LOGGER.error("前端连接websocket，发送数据格式错误,数据为：{}", text);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendKlineData(String expair, String data) {
        if (StringUtils.isNoneBlank(expair) && StringUtils.isNoneBlank(data)) {
            Optional.ofNullable(KLINE_CHANNEL_MAP.get(expair))
                    .ifPresent(channels -> channels.writeAndFlush(new TextWebSocketFrame(data)));
        }
    }

}
