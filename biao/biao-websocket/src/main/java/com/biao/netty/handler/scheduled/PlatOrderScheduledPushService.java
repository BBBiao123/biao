package com.biao.netty.handler.scheduled;

import com.biao.current.threadpool.BbexThreadFactory;
import com.biao.handler.PlatDataHandler;
import com.biao.netty.handler.PlatWebsocketHandler;
import com.biao.util.JsonUtils;
import com.biao.vo.TradePairVO;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * The type Plat order scheduled push service.
 *
 *  ""(Myth)
 */
@Component
public class PlatOrderScheduledPushService implements InitializingBean {

    private final PlatDataHandler platDataHandler;

    private final PlatWebsocketHandler platWebsocketHandler;

    private final ScheduledExecutorService scheduledExecutorService =
            new ScheduledThreadPoolExecutor(1,
                    BbexThreadFactory.create("plat-task", false));

    @Value("${biao.order.delayTime:30}")
    private Integer delayTime;

    /**
     * Instantiates a new Plat order scheduled push service.
     *
     * @param platDataHandler      the plat data handler
     * @param platWebsocketHandler the plat websocket handler
     */
    @Autowired
    public PlatOrderScheduledPushService(final PlatDataHandler platDataHandler,
                                         final PlatWebsocketHandler platWebsocketHandler) {
        this.platDataHandler = platDataHandler;
        this.platWebsocketHandler = platWebsocketHandler;
    }

    @Override
    public void afterPropertiesSet() {
        scheduledExecutorService
                .scheduleAtFixedRate(() -> {
                    try {
                        final Map<String, List<TradePairVO>> resultMap =
                                platDataHandler.buildAllTradePair();
                        platWebsocketHandler.sendPlatData(JsonUtils.toJson(resultMap));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }, 10, delayTime, TimeUnit.SECONDS);
    }
}
