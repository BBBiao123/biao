package com.biao.netty.handler;

import com.biao.constant.WebsocketConstant;
import com.biao.netty.NettyWebsocketHandler;
import com.biao.netty.utils.UrlUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The type Message websocket handler.
 *
 *  ""(Myth)
 */
@Component
public class MessageWebsocketHandler extends AbstractHandshakeHandler implements NettyWebsocketHandler {

    private static final Map<String, Channel> MESSAGE_MAP = new ConcurrentHashMap<>();

    @Override
    public String path() {
        return WebsocketConstant.WS_MESSAGE;
    }

    @Override
    public void remove(final Channel channel) {
        final String key = channel.attr(WebsocketConstant.MESSAGE_KEY).get();
        MESSAGE_MAP.remove(key);
    }

    @Override
    public ChannelFuture handshake(final ChannelHandlerContext ctx, final FullHttpRequest req) {
        final ChannelFuture handshake = super.handshake(ctx, req, path());
        if (Objects.nonNull(handshake)) {
            final String param = UrlUtils.urlParam(req.uri());
            ctx.channel().attr(WebsocketConstant.MESSAGE_KEY).set(param);
            MESSAGE_MAP.put(param, ctx.channel());
        }
        return handshake;
    }

    @Override
    public void handleWebSocketFrame(final ChannelHandlerContext ctx, final WebSocketFrame frame) {
        //ctx.writeAndFlush(new TextWebSocketFrame("userOrder"));
    }

    public void sendMessage(final String userId, final String data) {
        if (StringUtils.isNoneBlank(data) && StringUtils.isNoneBlank(userId)) {
            Optional.ofNullable(MESSAGE_MAP.get(userId)).ifPresent(channel ->
                    channel.writeAndFlush(new TextWebSocketFrame(data)));
        }
    }


}
