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
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 *  ""(Myth)
 */
@Component
public class HomeWebsocketHandler extends AbstractHandshakeHandler implements NettyWebsocketHandler {

    private final ChannelGroup HOME_CHANNEL_MAP = new DefaultChannelGroup("home",
            new DefaultEventExecutor());

    @Override
    public String path() {
        return WebsocketConstant.WS_HOME;
    }

    @Override
    public void remove(Channel channel) {
        HOME_CHANNEL_MAP.remove(channel);
    }

    @Override
    public ChannelFuture handshake(ChannelHandlerContext ctx, FullHttpRequest req) {
        final ChannelFuture handshake = super.handshake(ctx, req, path());
        if (Objects.nonNull(handshake)) {
            HOME_CHANNEL_MAP.add(ctx.channel());
        }
        return handshake;
    }

    @Override
    public void handleWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {
        ctx.writeAndFlush(new TextWebSocketFrame("home"));
    }

    public void sendHomeData(String data) {
        HOME_CHANNEL_MAP.writeAndFlush(new TextWebSocketFrame(data));
    }


}
