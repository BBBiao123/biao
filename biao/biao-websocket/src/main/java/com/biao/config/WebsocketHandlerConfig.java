package com.biao.config;

import com.biao.netty.NettyWebsocketHandler;
import com.biao.netty.NettyWebsocketHandlerFactory;
import com.biao.netty.handler.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * it websocket handler config.
 *
 *  ""(Myth)
 */
@Configuration
public class WebsocketHandlerConfig {

    private final C2cUserOrderWebsocketHandler c2cUserOrderWebsocketHandler;

    private final FlowingWaterWebsocketHandler flowingWaterWebsocketHandler;

    private final HomeWebsocketHandler homeWebsocketHandler;

    private final KlineWebsocketHandler klineWebsocketHandler;

    private final PlatWebsocketHandler platWebsocketHandler;

    private final SellBuyWebsocketHandler sellBuyWebsocketHandler;

    private final UserOrderWebsocketHandler userOrderWebsocketHandler;

    private final UserVolumeWebsocketHandler userVolumeWebsocketHandler;

    private final ReplayWebsocketHandler replayWebsocketHandler;

    private final LuckyDrawWebsocketHandler luckyDrawWebsocketHandler;

    private final MessageWebsocketHandler messageWebsocketHandler;

    /**
     * Instantiates a new Websocket handler config.
     *
     * @param klineWebsocketHandler          the kline websocket handler
     * @param userOrderWebsocketHandler      the user order websocket handler
     * @param platWebsocketHandler           the plat websocket handler
     * @param flowingWaterWebsocketHandler   the flowing water websocket handler
     * @param homeWebsocketHandler           the home websocket handler
     * @param sellBuyWebsocketHandler        the sell buy websocket handler
     * @param userVolumeWebsocketHandler     the user volume websocket handler
     * @param c2cUserOrderWebsocketHandler   the c 2 c user order websocket handler
     * @param replayWebsocketHandler         the replay websocket handler
     * @param luckyDrawWebsocketHandler      the lucky draw websocket handler
     * @param messageWebsocketHandler the offline message websocket handler
     */
    @Autowired
    public WebsocketHandlerConfig(final KlineWebsocketHandler klineWebsocketHandler,
                                  final UserOrderWebsocketHandler userOrderWebsocketHandler,
                                  final PlatWebsocketHandler platWebsocketHandler,
                                  final FlowingWaterWebsocketHandler flowingWaterWebsocketHandler,
                                  final HomeWebsocketHandler homeWebsocketHandler,
                                  final SellBuyWebsocketHandler sellBuyWebsocketHandler,
                                  final UserVolumeWebsocketHandler userVolumeWebsocketHandler,
                                  final C2cUserOrderWebsocketHandler c2cUserOrderWebsocketHandler,
                                  final ReplayWebsocketHandler replayWebsocketHandler,
                                  final LuckyDrawWebsocketHandler luckyDrawWebsocketHandler,
                                  final MessageWebsocketHandler messageWebsocketHandler) {
        this.klineWebsocketHandler = klineWebsocketHandler;
        this.userOrderWebsocketHandler = userOrderWebsocketHandler;
        this.platWebsocketHandler = platWebsocketHandler;
        this.flowingWaterWebsocketHandler = flowingWaterWebsocketHandler;
        this.homeWebsocketHandler = homeWebsocketHandler;
        this.sellBuyWebsocketHandler = sellBuyWebsocketHandler;
        this.userVolumeWebsocketHandler = userVolumeWebsocketHandler;
        this.c2cUserOrderWebsocketHandler = c2cUserOrderWebsocketHandler;
        this.replayWebsocketHandler = replayWebsocketHandler;
        this.luckyDrawWebsocketHandler = luckyDrawWebsocketHandler;
        this.messageWebsocketHandler = messageWebsocketHandler;
    }

    /**
     * Factory netty websocket handler factory.
     *
     * @return the netty websocket handler factory
     */
    @Bean
    public NettyWebsocketHandlerFactory factory() {
        NettyWebsocketHandlerFactory factory = new NettyWebsocketHandlerFactory();
        Map<String, NettyWebsocketHandler> handlerMap = new HashMap<>(11);
        handlerMap.put(c2cUserOrderWebsocketHandler.path(), c2cUserOrderWebsocketHandler);
        handlerMap.put(flowingWaterWebsocketHandler.path(), flowingWaterWebsocketHandler);
        handlerMap.put(homeWebsocketHandler.path(), homeWebsocketHandler);
        handlerMap.put(klineWebsocketHandler.path(), klineWebsocketHandler);
        handlerMap.put(platWebsocketHandler.path(), platWebsocketHandler);
        handlerMap.put(sellBuyWebsocketHandler.path(), sellBuyWebsocketHandler);
        handlerMap.put(userOrderWebsocketHandler.path(), userOrderWebsocketHandler);
        handlerMap.put(userVolumeWebsocketHandler.path(), userVolumeWebsocketHandler);
        handlerMap.put(replayWebsocketHandler.path(), replayWebsocketHandler);
        handlerMap.put(luckyDrawWebsocketHandler.path(), luckyDrawWebsocketHandler);
        handlerMap.put(messageWebsocketHandler.path(), messageWebsocketHandler);
        factory.setHandlerMap(handlerMap);
        return factory;
    }
}
