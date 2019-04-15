package com.biao.netty.handler;

import com.biao.constant.WebsocketConstant;
import com.biao.current.threadpool.BbexThreadFactory;
import com.biao.current.threadpool.BbexThreadPool;
import com.biao.handler.PlatDataHandler;
import com.biao.netty.NettyWebsocketHandler;
import com.biao.util.JsonUtils;
import com.biao.vo.PlatUserPushVO;
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

import java.util.Objects;
import java.util.concurrent.ExecutorService;

/**
 *  ""(Myth)
 */
@Component
public class UserVolumeWebsocketHandler extends AbstractHandshakeHandler implements NettyWebsocketHandler {

    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(UserVolumeWebsocketHandler.class);

    private static final int LENGTH = 3;

    private static final ExecutorService EXECUTOR_SERVICE =
            BbexThreadPool.newCustomFixedThreadPool(BbexThreadPool.THREAD_COUNT,
                    BbexThreadFactory.create("user-volume-websocket", false));

    @Autowired
    private PlatDataHandler platDataHandler;

    @Override
    public String path() {
        return WebsocketConstant.WS_USER_VOLUME;
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
            final String coinOther = split[0];
            final String coinMain = split[1];
            final String userId = split[2];
            EXECUTOR_SERVICE.execute(() -> {
                PlatUserPushVO platUserPushVO = platDataHandler.buildUserCoinVolume(userId, coinMain, coinOther);
                if (Objects.nonNull(platUserPushVO)) {
                    ctx.writeAndFlush(new TextWebSocketFrame(JsonUtils.toJson(platUserPushVO)));
                }
            });


        }

    }


}
