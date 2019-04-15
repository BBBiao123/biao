package com.biao.handler;

import com.biao.constant.Constants;
import com.biao.current.threadpool.BbexThreadFactory;
import com.biao.current.threadpool.BbexThreadPool;
import com.biao.enums.OrderEnum;
import com.biao.enums.TradePairEnum;
import com.biao.kafka.interceptor.MessageDTO;
import com.biao.netty.handler.*;
import com.biao.pojo.MatchStreamDto;
import com.biao.pojo.TradeDto;
import com.biao.pojo.UserOrderDTO;
import com.biao.util.DateUtils;
import com.biao.util.JsonUtils;
import com.biao.vo.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

/**
 * 推送数据Handler.
 *
 *  ""
 */
@Component
@SuppressWarnings("all")
public class PlatPushDataHandler {

    @Autowired
    private PlatDataHandler platDataHandler;

    @Autowired
    private UserOrderWebsocketHandler userOrderWebsocketHandler;

    @Autowired
    private FlowingWaterWebsocketHandler flowingWaterWebsocketHandler;

    @Autowired
    private KlineWebsocketHandler klineWebsocketHandler;

    @Autowired
    private HomeWebsocketHandler homeWebsocketHandler;

    @Autowired
    private PlatWebsocketHandler platWebsocketHandler;

    @Autowired
    private C2cUserOrderWebsocketHandler c2cUserOrderWebsocketHandler;

    @Autowired
    private MessageWebsocketHandler messageWebsocketHandler;


    private final Executor executor =
            BbexThreadPool.newCustomFixedThreadPool(BbexThreadPool.THREAD_COUNT,
                    BbexThreadFactory.create("kline-threads", false));


    /**
     * 推送挂单信息到前端.
     *
     * @param tradeDto 挂单实体
     */
    public void pushTradeOrder(final TradeDto tradeDto) {
        PlatUserPushVO platUserPushVO = new PlatUserPushVO();
        platUserPushVO.setOrderVo(buildUserOrderVo(tradeDto));
        //推送挂单信息给指定用户
        userOrderWebsocketHandler.sendUserOrder(tradeDto.getUserId(), JsonUtils.toJson(platUserPushVO));
    }

    public void pushMessage(MessageDTO messageDTO) {
        messageWebsocketHandler.sendMessage(messageDTO.getUserId(), JsonUtils.toJson(messageDTO));
    }

    /**
     * 推送撮合完成后订单的状态信息等。
     *
     * @param userOrderDTO
     */
    public void pushUserPartSuccessVolume(UserOrderDTO userOrderDTO) {
        PlatUserPushVO userPushVO = new PlatUserPushVO();
        userPushVO.setOrderVo(platDataHandler.buildOrderVo(userOrderDTO));
        userOrderWebsocketHandler.sendUserOrder(userOrderDTO.getUserId(), JsonUtils.toJson(userPushVO));
    }


    /**
     * 推送交易流水结果到前端. 同时推送流水信息到K线
     *
     * @param matchStream 交易流水对象
     */
    public void pushMatchStream(final MatchStreamDto matchStream) {
        PlatTradePushVO platTradePushVO = new PlatTradePushVO();
        platTradePushVO.setMatchStreamVO(MatchStreamVO.transform(matchStream));
        String coinMain = matchStream.getCoinMain();
        String coinOther = matchStream.getCoinOther();
        if (TradePairEnum.ETH.getKey().equals(coinOther)) {
            if (TradePairEnum.USDT.getKey().equals(coinMain)) {
                platTradePushVO.setEthLastPrice(matchStream.getPrice());
                platDataHandler.setRedisLastPrice(coinMain, coinOther, matchStream.getPrice());
            } else if (TradePairEnum.CNB.getKey().equals(coinMain)) {
                platTradePushVO.setCnbEthLastPrice(matchStream.getPrice());
                platDataHandler.setRedisLastPrice(coinMain, coinOther, matchStream.getPrice());
            }
        } else if (TradePairEnum.BTC.getKey().equals(coinOther)) {
            if (TradePairEnum.USDT.getKey().equals(coinMain)) {
                platTradePushVO.setBtcLastPrice(matchStream.getPrice());
                platDataHandler.setRedisLastPrice(coinMain, coinOther, matchStream.getPrice());
            } else if (TradePairEnum.CNB.getKey().equals(coinMain)) {
                platTradePushVO.setCnbBtcLastPrice(matchStream.getPrice());
                platDataHandler.setRedisLastPrice(coinMain, coinOther, matchStream.getPrice());
            }
        }else if (TradePairEnum.EOS.getKey().equals(coinOther)) {
            if (TradePairEnum.USDT.getKey().equals(coinMain)) {
                platTradePushVO.setEosLastPrice(matchStream.getPrice());
                platDataHandler.setRedisLastPrice(coinMain, coinOther, matchStream.getPrice());
            } else if (TradePairEnum.CNB.getKey().equals(coinMain)) {
                platTradePushVO.setCnbEosLastPrice(matchStream.getPrice());
                platDataHandler.setRedisLastPrice(coinMain, coinOther, matchStream.getPrice());
            }
        }
        flowingWaterWebsocketHandler.sendFlowingWater(
                buildSocketKey(matchStream.getCoinMain(), matchStream.getCoinOther()),
                JsonUtils.toJson(platTradePushVO));

        pushKlineData(matchStream);
    }


    private void pushKlineData(final MatchStreamDto matchStream) {
        KlineResult klineResult = new KlineResult();
        klineResult.setType("realTime");
        klineResult.setCode(0);
        KlineVO klineVO = new KlineVO();
        klineVO.setS("ok");
        try {
            klineVO.setT(String.valueOf(DateUtils.parseLocalDateTime(matchStream.getMinuteTime())
                    .toInstant(ZoneOffset.of("+8")).toEpochMilli()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        klineVO.setV(matchStream.getVolume().toPlainString());
        klineVO.setC(matchStream.getPrice().toPlainString());
        klineResult.setData(Collections.singletonList(klineVO));
        klineWebsocketHandler.sendKlineData(buildSocketKey(matchStream.getCoinMain(),
                matchStream.getCoinOther()),
                JsonUtils.toJson(klineResult));
    }

    /**
     * 推送交易对信息到首页.
     *
     * @param coinMain 主区
     */
    public void pushTradeExPairData(final String coinMain) {
        final Map<String, List<TradePairVO>> stringListMap = platDataHandler.buildAllTradePair();
        final String result = JsonUtils.toJson(stringListMap);
        if (StringUtils.isNoneBlank(result)) {
            homeWebsocketHandler.sendHomeData(result);
            platWebsocketHandler.sendPlatData(result);
        }
    }

    /**
     * 推送数据到前端.
     *
     * @param key  websocket Map 保存的Key
     * @param data String
     */
    public void pushC2CData(final String userId, final String data) {
        c2cUserOrderWebsocketHandler.sendC2cUserOrder(userId, data);
    }

    private String buildSocketKey(final String coinMain, final String coinOther) {
        return String.join(Constants.JOIN, coinOther, coinMain);
    }

    private OrderVo buildUserOrderVo(final TradeDto tradeDto) {
        OrderVo orderVo = new OrderVo();
        orderVo.setTime(LocalDateTime.now());
        orderVo.setOrderNo(tradeDto.getOrderNo());
        orderVo.setPrice(tradeDto.getPrice());
        orderVo.setExType(tradeDto.getType().ordinal());
        orderVo.setVolume(tradeDto.getVolume());
        orderVo.setSuccessVolume(new BigDecimal(0));
        orderVo.setStatus(OrderEnum.OrderStatus.NOT_SUCCESS.getCode());
        return orderVo;
    }


}
