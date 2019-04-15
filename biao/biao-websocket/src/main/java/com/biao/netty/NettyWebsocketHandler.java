package com.biao.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

/**
 * The interface Netty websocket handler.
 *
 *  ""(Myth)
 */
public interface NettyWebsocketHandler {


    /**
     * websocket path.
     *
     * @return websocket path
     */
    String path();


    /**
     * clean cache channel.
     *
     * @param channel channel
     */
    void remove(Channel channel);

    /**
     * websocket  handshake.
     *
     * @param ctx ctx
     * @param req req
     * @return ChannelFuture channel future
     */
    ChannelFuture handshake(ChannelHandlerContext ctx, FullHttpRequest req);


    /**
     * handleWebSocketFrame.
     *
     * @param ctx   handleWebSocketFrame
     * @param frame frame
     */
    void handleWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame);


}
