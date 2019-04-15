package com.biao.service.impl;

import com.biao.constant.Constants;
import com.biao.entity.PlatUser;
import com.biao.entity.mkcommon.MkCommonCoinRate;
import com.biao.entity.mkcommon.MkCommonUserCoinFee;
import com.biao.exception.PlatException;
import com.biao.mapper.PlatUserDao;
import com.biao.mapper.mkcommon.MkCommonCoinRateDao;
import com.biao.mapper.mkcommon.MkCommonUserCoinFeeDao;
import com.biao.reactive.data.mongo.service.TradeDetailService;
import com.biao.service.MkCommonTaskService;
import com.biao.util.DateUtils;
import com.biao.vo.TradeExPairDayAvgVO;
import com.biao.vo.TradeUserCoinFeeVO;
import com.biao.vo.UserTradeVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MkCommonTaskServiceImpl implements MkCommonTaskService {

    private Logger logger = LoggerFactory.getLogger(MkCommonTaskServiceImpl.class);

    @Autowired
    private TradeDetailService tradeDetailService;

    @Autowired
    private MkCommonCoinRateDao mkCommonCoinRateDao;

    @Autowired
    private MkCommonUserCoinFeeDao mkCommonUserCoinFeeDao;

    @Autowired
    private PlatUserDao platUserDao;

    @Override
    @Transactional
    public void calcUserMainCoinFee() {

        LocalDate curDateTime = LocalDate.now();
//        curDateTime = curDateTime.minusDays(-1);
        long count = mkCommonUserCoinFeeDao.findUserCoinFeeByDate(DateUtils.formaterLocalDateTime(DateUtils.getDayStart(curDateTime.minusDays(1))), DateUtils.formaterLocalDateTime(DateUtils.getDayEnd(curDateTime.minusDays(1))));

        if (count > 0) {
            logger.info("已存在记录！");
            return;
        }

        List<TradeUserCoinFeeVO> tradeUserCoinFeeList = this.calcUserTradeFee(curDateTime.minusDays(1));
        if (CollectionUtils.isEmpty(tradeUserCoinFeeList)) {
            return;
        }

        List<MkCommonUserCoinFee> mkCommonUserCoinFeeList = new ArrayList<>();
        this.transferTradeFeeToMainCoin(mkCommonUserCoinFeeList, tradeUserCoinFeeList, curDateTime);

        mkCommonUserCoinFeeDao.insertBatch(mkCommonUserCoinFeeList);
    }

    private List<TradeUserCoinFeeVO> calcUserTradeFee(LocalDate curDateTime) {

        logger.info(String.format("start [%s]计算前天交易手续费,并把手续费转成主区币种（ETH,BTC,USDT）", DateUtils.formaterDate(curDateTime)));

        //查询交易列表
        List<UserTradeVO> userTradeVOList = tradeDetailService.findByDate(DateUtils.formaterLocalDateTime(DateUtils.getDayStart(curDateTime)), DateUtils.formaterLocalDateTime(DateUtils.getDayEnd(curDateTime)));
        if (CollectionUtils.isEmpty(userTradeVOList)) {
            logger.info(String.format("无[%s]成交记录！", curDateTime));
            return null;
        }

        //计算每一笔交易的手续费，并转换成主区币种
        List<TradeUserCoinFeeVO> userCoinFeeList = new ArrayList<>();
        userTradeVOList.forEach(userTradeVO -> {
            TradeUserCoinFeeVO tradeUserCoinFee = new TradeUserCoinFeeVO();
            tradeUserCoinFee.setCoinMain(userTradeVO.getCoinMain());
            tradeUserCoinFee.setCoinOther(userTradeVO.getCoinOther());
            tradeUserCoinFee.setUserId(userTradeVO.getUserId());
            tradeUserCoinFee.setOrderNo(userTradeVO.getOrderNo());

            if (userTradeVO.getExType() == 0) { //买,手续费装换成主区币(ETH|BTC|USDT)
                tradeUserCoinFee.setExFee(userTradeVO.getExFee().multiply(userTradeVO.getPrice()));
            } else if (userTradeVO.getExType() == 1) { //卖，手续费就主区币（ETH|BTC|USDT）
                tradeUserCoinFee.setExFee(userTradeVO.getExFee());
            }
            userCoinFeeList.add(tradeUserCoinFee);
        });

        logger.info(String.format("end [%s]计算前天交易手续费,并把手续费转成主区币种（ETH, BTC,USDT）", curDateTime));
        return userCoinFeeList;
    }

    private void transferTradeFeeToMainCoin(List<MkCommonUserCoinFee> mkCommonUserCoinFeeList, List<TradeUserCoinFeeVO> userCoinFeeList, LocalDate curDateTime) {
        Map<String, Double> btcMap = userCoinFeeList.stream().filter(f -> f.getCoinMain().equals("BTC")).collect(Collectors.groupingBy(f -> f.getUserId(), Collectors.summingDouble(f -> f.getExFee().doubleValue())));
        Map<String, Double> ethMap = userCoinFeeList.stream().filter(f -> f.getCoinMain().equals("ETH")).collect(Collectors.groupingBy(f -> f.getUserId(), Collectors.summingDouble(f -> f.getExFee().doubleValue())));
        Map<String, Double> usdtMap = userCoinFeeList.stream().filter(f -> f.getCoinMain().equals("USDT")).collect(Collectors.groupingBy(f -> f.getUserId(), Collectors.summingDouble(f -> f.getExFee().doubleValue())));
        this.buildUserCoinFee(mkCommonUserCoinFeeList, btcMap, "BTC", curDateTime);
        this.buildUserCoinFee(mkCommonUserCoinFeeList, ethMap, "ETH", curDateTime);
        this.buildUserCoinFee(mkCommonUserCoinFeeList, usdtMap, "USDT", curDateTime);
    }

    private TradeExPairDayAvgVO getExPairDayAvg(String curDate, String coinMain, String coinOther) {
        //获取前天均价
        TradeExPairDayAvgVO avgVO = tradeDetailService.findDayAverageByExPair(curDate, coinMain, coinOther);

        //不存在均价
        if (null == avgVO || BigDecimal.ZERO.compareTo(avgVO.getAvgPrice()) >= 0) {
            logger.info(String.format("执行失败，无[%s][%s/%s]均价!", curDate, coinOther, coinMain));
            throw new PlatException(Constants.DISTRIBUTE_TASK_ERROR, String.format("执行失败，无[%s][%s/%s]均价!", curDate, coinOther, coinMain));
        }

        avgVO.setAvgPrice(avgVO.getAvgPrice().divide(BigDecimal.ONE, 8, BigDecimal.ROUND_DOWN));
        logger.info(String.format("[%s][%s/%s]的均价为：[%s]", curDate, coinOther, coinMain, String.valueOf(avgVO.getAvgPrice())));
        return avgVO;
    }

    private void buildUserCoinFee(List<MkCommonUserCoinFee> mkCommonUserCoinFeeList, Map<String, Double> userFeeMap, String coinSymbol, LocalDate curDateTime) {

        TradeExPairDayAvgVO dayAvgVO = null;
        if (!"USDT".equals(coinSymbol)) {
            dayAvgVO = this.getExPairDayAvg(DateUtils.formaterDate(curDateTime.minusDays(1)), "USDT", coinSymbol);
            MkCommonCoinRate mkCommonCoinRate = new MkCommonCoinRate();
            mkCommonCoinRate.setId(UUID.randomUUID().toString().replace("-", ""));
            mkCommonCoinRate.setMainCoinSymbol("USDT");
            mkCommonCoinRate.setOtherCoinSymbol(coinSymbol);
            mkCommonCoinRate.setRate(dayAvgVO.getAvgPrice());
            mkCommonCoinRate.setBeginDate(DateUtils.getDayStart(curDateTime));
            mkCommonCoinRate.setEndDate(DateUtils.getDayEnd(curDateTime));
            mkCommonCoinRateDao.insert(mkCommonCoinRate);
        }

        final BigDecimal avgPrice = null == dayAvgVO ? BigDecimal.ONE : dayAvgVO.getAvgPrice();
        userFeeMap.forEach((userId, coinFee) -> {
            //过滤费率为0
            if (BigDecimal.valueOf(coinFee).compareTo(new BigDecimal("0.00000001")) < 0) return;

            //1, 把所有的费率都转换成USDT
            BigDecimal curUSDTFee = BigDecimal.ZERO;
            if ("USDT".equals(coinSymbol)) {
                curUSDTFee = BigDecimal.valueOf(coinFee);
            } else if ("BTC".equals(coinSymbol)) {
                curUSDTFee = BigDecimal.valueOf(coinFee).multiply(avgPrice);
            } else if ("ETH".equals(coinSymbol)) {
                curUSDTFee = BigDecimal.valueOf(coinFee).multiply(avgPrice);
            }

            MkCommonUserCoinFee mkCommonUserCoinFee = new MkCommonUserCoinFee();
            PlatUser platUser = platUserDao.findById(userId);
            mkCommonUserCoinFee.setId(UUID.randomUUID().toString().replace("-", ""));
            mkCommonUserCoinFee.setUserId(userId);
            mkCommonUserCoinFee.setMail(platUser.getMail());
            mkCommonUserCoinFee.setMobile(platUser.getMobile());
            mkCommonUserCoinFee.setIdCard(platUser.getIdCard());
            mkCommonUserCoinFee.setRealName(platUser.getRealName());
            mkCommonUserCoinFee.setCoinSymbol(coinSymbol);
            mkCommonUserCoinFee.setExUsdtVol(curUSDTFee);
            mkCommonUserCoinFee.setVolume(BigDecimal.valueOf(coinFee));
            mkCommonUserCoinFee.setBeginDate(DateUtils.getDayStart(curDateTime.minusDays(1)));
            mkCommonUserCoinFee.setEndDate(DateUtils.getDayEnd(curDateTime.minusDays(1)));
            mkCommonUserCoinFeeList.add(mkCommonUserCoinFee);
        });
    }

}
