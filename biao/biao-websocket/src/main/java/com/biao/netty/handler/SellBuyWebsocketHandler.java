package com.biao.netty.handler;

import com.biao.constant.WebsocketConstant;
import com.biao.current.threadpool.BbexThreadFactory;
import com.biao.current.threadpool.BbexThreadPool;
import com.biao.handler.PlatDataHandler;
import com.biao.netty.NettyWebsocketHandler;
import com.biao.util.JsonUtils;
import com.biao.vo.BuySellerOrderVO;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;

/**
 *  ""(Myth)
 */
@Component
public class SellBuyWebsocketHandler extends AbstractHandshakeHandler implements NettyWebsocketHandler {

    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(SellBuyWebsocketHandler.class);

    private static final int LENGTH = 3;

    private static final ExecutorService EXECUTOR_SERVICE =
            BbexThreadPool.newCustomFixedThreadPool(BbexThreadPool.THREAD_COUNT,
                    BbexThreadFactory.create("buy-seller-websocket", false));


    @Autowired
    private PlatDataHandler platDataHandler;

    @Override
    public String path() {
        return WebsocketConstant.WS_BUY_SELL;
    }

    @Override
    public void remove(Channel channel) {
        channel.close();
    }

    @Override
    public ChannelFuture handshake(ChannelHandlerContext ctx, FullHttpRequest req) {
        return super.handshake(ctx, req, path());
    }

    @Override
    public void handleWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {
        final String text = ((TextWebSocketFrame) frame).text();
        if (StringUtils.isNoneBlank(text)) {
            String[] split = StringUtils.split(text, WebsocketConstant.JOIN);
            if (split.length < LENGTH) {
                LOGGER.error("前端传入参数非法异常！传入数据为:{}", text);
                return;
            }
            final BuySellerOrderVO buySellerOrderVO = platDataHandler.buyAndSellerOrder(split[1], split[0],
                    Integer.valueOf(split[2]));
            ctx.writeAndFlush(new TextWebSocketFrame(JsonUtils.toJson(buySellerOrderVO)));
        }
    }


}
