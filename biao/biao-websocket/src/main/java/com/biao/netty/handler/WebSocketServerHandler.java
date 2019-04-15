package com.biao.netty.handler;

import com.biao.constant.WebsocketConstant;
import com.biao.netty.NettyWebsocketHandler;
import com.biao.netty.NettyWebsocketHandlerFactory;
import com.biao.netty.utils.HttpHeaderUtil;
import com.biao.netty.utils.UrlUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.util.CharsetUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import static io.netty.handler.codec.http.HttpMethod.GET;
import static io.netty.handler.codec.http.HttpResponseStatus.*;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * The type Web socket server handler.
 *
 *  ""
 */
@ChannelHandler.Sharable
@Component
public class WebSocketServerHandler extends SimpleChannelInboundHandler<Object> {

    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketServerHandler.class);

    private final NettyWebsocketHandlerFactory nettyWebsocketHandlerFactory;

    private static final Map<String, String> CHANNEL_PATH = new ConcurrentHashMap<>();

    /**
     * Instantiates a new Web socket server handler.
     *
     * @param nettyWebsocketHandlerFactory the netty websocket handler factory
     */
    @Autowired
    public WebSocketServerHandler(NettyWebsocketHandlerFactory nettyWebsocketHandlerFactory) {
        this.nettyWebsocketHandlerFactory = nettyWebsocketHandlerFactory;
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof FullHttpRequest) {
            handleHttpRequest(ctx, (FullHttpRequest) msg);
        } else if (msg instanceof WebSocketFrame) {
            handleWebSocketFrame(ctx, (WebSocketFrame) msg);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        CHANNEL_PATH.remove(ctx.channel().id().asLongText());
        final NettyWebsocketHandler nettyWebsocketHandler = buildHandler(ctx);
        if (Objects.nonNull(nettyWebsocketHandler)) {
            nettyWebsocketHandler.remove(ctx.channel());
        }

    }

    private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req) {
        // Handle a bad request.
        if (!req.decoderResult().isSuccess()) {
            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HTTP_1_1, BAD_REQUEST));
            return;
        }
        // Allow only GET methods.
        if (req.method() != GET) {
            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HTTP_1_1, FORBIDDEN));
            return;
        }
        if ("/favicon.ico".equals(req.uri()) || ("/".equals(req.uri()))) {
            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HTTP_1_1, NOT_FOUND));
            return;
        }
        final NettyWebsocketHandler nettyWebsocketHandler =
                nettyWebsocketHandlerFactory.factoryOf(UrlUtils.realPath(req.uri()));
        if (Objects.isNull(nettyWebsocketHandler)) {
            LOGGER.error("此websocket url 暂时不支持！{}", req.uri());
            return;
        }
        final ChannelFuture handshake = nettyWebsocketHandler.handshake(ctx, req);
        if (Objects.nonNull(handshake) && handshake.isDone()) {
            CHANNEL_PATH.put(ctx.channel().id().asLongText(), UrlUtils.realPath(req.uri()));
        }

    }

    private void handleWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {
        if (frame instanceof CloseWebSocketFrame) {
            ctx.channel().close();
            return;
        }
        if (!(frame instanceof TextWebSocketFrame)) {
            throw new UnsupportedOperationException(String.format("%s frame types not supported", frame.getClass().getName()));
        }
        final String text = ((TextWebSocketFrame) frame).text();
        if (WebsocketConstant.PING.equals(text)) {
            ctx.writeAndFlush(new TextWebSocketFrame(WebsocketConstant.PONG));
            return;
        }
        final NettyWebsocketHandler nettyWebsocketHandler = buildHandler(ctx);
        if (Objects.nonNull(nettyWebsocketHandler)) {
            nettyWebsocketHandler.handleWebSocketFrame(ctx, frame);
        }

    }


    private NettyWebsocketHandler buildHandler(ChannelHandlerContext ctx) {
        final String path = CHANNEL_PATH.get(ctx.channel().id().asLongText());
        if (StringUtils.isNoneBlank(path)) {
            return nettyWebsocketHandlerFactory.factoryOf(path);
        }
        return null;
    }

    private static void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest req, FullHttpResponse res) {
        if (res.status().code() != WebsocketConstant.HTTP_SUCCESS_CODE) {
            ByteBuf buf = Unpooled.copiedBuffer(res.status().toString(), CharsetUtil.UTF_8);
            res.content().writeBytes(buf);
            buf.release();
        }
        ChannelFuture f = ctx.channel().writeAndFlush(res);
        if (!HttpHeaderUtil.isKeepAlive(req) || res.status().code() != WebsocketConstant.HTTP_SUCCESS_CODE) {
            f.addListener(ChannelFutureListener.CLOSE);
        }
    }


}
