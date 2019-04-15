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
 * The type Plat websocket handler.
 *
 *  ""(Myth)
 */
@Component
public class PlatWebsocketHandler extends AbstractHandshakeHandler implements NettyWebsocketHandler {

    private final ChannelGroup CHANNEL_GROUP = new DefaultChannelGroup("plat",
            new DefaultEventExecutor());


    @Override
    public String path() {
        return WebsocketConstant.WS_PLAT;
    }

    @Override
    public void remove(Channel channel) {
        CHANNEL_GROUP.remove(channel);
    }

    @Override
    public ChannelFuture handshake(ChannelHandlerContext ctx, FullHttpRequest req) {
        final ChannelFuture handshake = super.handshake(ctx, req, path());
        if (Objects.nonNull(handshake)) {
            CHANNEL_GROUP.add(ctx.channel());
        }
        return handshake;
    }

    @Override
    public void handleWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {
        //ctx.writeAndFlush(new TextWebSocketFrame("plat"));
    }

    /**
     * Send plat data.
     *
     * @param data the data
     */
    public void sendPlatData(final String data) {
        CHANNEL_GROUP.writeAndFlush(new TextWebSocketFrame(data));
    }


}
