package com.biao.handler;

import com.biao.BaseTest;
import com.biao.util.DateUtils;
import com.biao.util.JsonUtils;
import com.biao.vo.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

/**
 *  ""
 */
public class PlatDataHandlerTest extends BaseTest {

    @Autowired
    private PlatDataHandler platDataHandler;

    @Test
    public void handlerCoinToCoinHomeData() throws Exception {

    }

    @Test
    public void handlerMatchStream() throws Exception {

    }

    @Test
    public void handlerTradePair() throws Exception {

    }

    @Test
    public void buildAllTradePair() {
        final Map<String, List<TradePairVO>> stringListMap = platDataHandler.buildAllTradePair();
        System.err.println(JsonUtils.toJson(stringListMap));
        stringListMap.forEach((k, v) -> System.out.println("主交易区：" + k + "值为:" + v.toString()));
    }

    @Test
    public void merge() throws Exception {
        BigDecimal bigDecimal = new BigDecimal(1.2222434434343434);
        BigDecimal bigDecimal1 = bigDecimal.setScale(4, BigDecimal.ROUND_HALF_UP);

        BigDecimal bigDecimal2 = bigDecimal1.setScale(8, BigDecimal.ROUND_HALF_UP);
        double v = bigDecimal.doubleValue();
        // List<PlatOrderVO> merge = platDataHandler.merge("USDT", "HT", 0, 4);

    }


    @Test
    public void setRedisLastPrice() {
    }

    @Test
    public void statMinDateTime() throws ParseException {

        LocalDateTime nowTime = DateUtils.parseLocalDateTime("2018-06-20 00:24:00");
        LocalTime time = LocalTime.of(nowTime.getHour(), nowTime.getMinute(), 0);
        LocalDateTime minuteTime = LocalDateTime.of(nowTime.toLocalDate(), time);

        final TradePairVO tradePairVO = platDataHandler.statMinDateTime("BTC", "LOOM", minuteTime);
        System.out.println(tradePairVO);

    }


    @Test
    public void historyKlineData() {
        final KlineResult klineResult = platDataHandler.historyKlineData("BTC", "ETH");
        System.out.println(klineResult);
    }


    @Test
    public void findByTradeNoAndOrderNo() {
    }

    @Test
    public void buildOtherPlatUserPushVO() {
    }

    @Test
    public void buildPlatUserPushVO() {
    }

    @Test
    public void handlerTradePair1() {
    }

    @Test
    public void buildLastPrice() {
    }


    @Test
    public void testP() {
        PlatTradePushVO platTradePushVO = new PlatTradePushVO();
        platTradePushVO.setEthLastPrice(new BigDecimal(7000.00));
        platTradePushVO.setBtcLastPrice(new BigDecimal(5000));
        MatchStreamVO matchStreamVO = new MatchStreamVO();
        matchStreamVO.setDate(LocalDateTime.now());
        matchStreamVO.setPrice(new BigDecimal(7.6666).setScale(8, BigDecimal.ROUND_HALF_DOWN));
        matchStreamVO.setType(1);
        matchStreamVO.setVolume(new BigDecimal(1.23455).setScale(8, BigDecimal.ROUND_HALF_DOWN));
        matchStreamVO.setCoinMain("USDT");
        matchStreamVO.setCoinOther("LOOM");
        platTradePushVO.setMatchStreamVO(matchStreamVO);
        System.err.println(JsonUtils.toJson(platTradePushVO));
    }

    @Test
    public void testUserData() {
        PlatUserPushVO platUserPushVO = new PlatUserPushVO();
        OrderVo orderVo = new OrderVo();
        orderVo.setTime(LocalDateTime.now());
        orderVo.setExType(0);
        orderVo.setSuccessVolume(new BigDecimal(0.00));
        orderVo.setVolume(new BigDecimal(1));
        orderVo.setStatus(0);
        orderVo.setPrice(new BigDecimal(999.99));
        orderVo.setOrderNo("123445666");
        platUserPushVO.setOrderVo(orderVo);

        UserCoinVolumeVO userCoinVolumeVO = new UserCoinVolumeVO();
        userCoinVolumeVO.setLockVolume(new BigDecimal(1));
        userCoinVolumeVO.setUserId("1");
        userCoinVolumeVO.setCoinSymbol("USDT");
        userCoinVolumeVO.setVolume(new BigDecimal(999));

        platUserPushVO.setCoinMainVolume(userCoinVolumeVO);


        UserCoinVolumeVO other = new UserCoinVolumeVO();
        other.setLockVolume(new BigDecimal(1));
        other.setUserId("1");
        other.setCoinSymbol("LOOM");
        other.setVolume(new BigDecimal(999));
        platUserPushVO.setCoinOtherVolume(other);

        System.err.println(JsonUtils.toJson(platUserPushVO));

    }
}