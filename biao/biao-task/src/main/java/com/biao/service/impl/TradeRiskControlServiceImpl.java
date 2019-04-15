package com.biao.service.impl;

import com.biao.entity.TradeRiskControl;
import com.biao.entity.UserCoinVolume;
import com.biao.mapper.TradeRiskControlDao;
import com.biao.mapper.UserCoinVolumeDao;
import com.biao.reactive.data.mongo.domain.MatchStream;
import com.biao.reactive.data.mongo.service.MatchStreamService;
import com.biao.redis.RedisCacheManager;
import com.biao.service.TradeRiskControlService;
import com.biao.vo.risk.TotalRiskVolume;
import com.biao.vo.risk.TradeRiskVolume;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * tradeRiskControlService.
 *
 *  ""(Myth)
 */
@Service("tradeRiskControlService")
public class TradeRiskControlServiceImpl implements TradeRiskControlService {

    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(TradeRiskControlServiceImpl.class);

    private final TradeRiskControlDao tradeRiskControlDao;

    private final MatchStreamService matchStreamService;

    private final UserCoinVolumeDao userCoinVolumeDao;

    private final RedisCacheManager redisCacheManager;


    @Autowired(required = false)
    public TradeRiskControlServiceImpl(TradeRiskControlDao tradeRiskControlDao, MatchStreamService matchStreamService, UserCoinVolumeDao userCoinVolumeDao, RedisCacheManager redisCacheManager) {
        this.tradeRiskControlDao = tradeRiskControlDao;
        this.matchStreamService = matchStreamService;
        this.userCoinVolumeDao = userCoinVolumeDao;
        this.redisCacheManager = redisCacheManager;
    }


    /**
     *
     */
    @Override
    public void execute() {
        final List<TradeRiskControl> tradeRiskControls = tradeRiskControlDao.findAll();
        if (CollectionUtils.isNotEmpty(tradeRiskControls)) {
            for (TradeRiskControl tradeRiskControl : tradeRiskControls) {
                final TotalRiskVolume totalRiskVolume =
                        tradeRiskControlDao.findTotalRiskVolumeBySymbol(tradeRiskControl.getCoinOther());
                if (Objects.isNull(totalRiskVolume)) {
                    LOGGER.error("当前风险配置规则未查询到币种账面总量：{}", tradeRiskControl.toString());
                    continue;
                }
                final TradeRiskVolume coinOtherVolume =
                        tradeRiskControlDao.findByUserIdsAndCoinSymbol(tradeRiskControl.getUserIds(),
                                tradeRiskControl.getCoinOther());

                final TradeRiskVolume coinMainVolume = tradeRiskControlDao
                        .findByUserIdsAndCoinSymbol(tradeRiskControl.getUserIds(),
                                tradeRiskControl.getCoinMain());

                if (Objects.isNull(coinOtherVolume) || Objects.isNull(coinMainVolume)) {
                    LOGGER.error("当前风险配置规则未查询到配置用户个人资产总量：{}", tradeRiskControl.toString());
                    continue;
                }
                final String userIds = tradeRiskControl.getUserIds();
                final List<String> userIdList = Splitter.on(",").splitToList(userIds);

                List<UserCoinVolume> allUserCoinVolume = Lists.newArrayList();
                userIdList.forEach(userId ->
                        allUserCoinVolume.addAll(userCoinVolumeDao.findByUserId(userId)));

                final List<UserCoinVolume> flagList =
                        allUserCoinVolume.stream().filter(v -> Objects.isNull(v.getFlag()) || v.getFlag() == 0)
                                .collect(Collectors.toList());

                if (coinMainVolume.getTotalVolume().compareTo(tradeRiskControl.getFixedVolume()) <= 0) {
                    LOGGER.info("当前护盘资金小于等于风险控制阀值,进行锁定。护盘资金为:{},阀值为:{},风险配置为:{}",
                            coinMainVolume.getTotalVolume(), tradeRiskControl.getFixedVolume(),
                            tradeRiskControl.toString());

                    flagList.forEach(coinVolume -> {
                        userCoinVolumeDao.updateFlag(1,
                                "护盘资金小于等于风险控制阀值,请充值!", coinVolume.getUserId());
                        redisCacheManager.cleanUserCoinVolume(coinVolume.getUserId(), coinVolume.getCoinSymbol());
                    });
                    continue;
                }

                final BigDecimal volume =
                        totalRiskVolume.getTotalVolume().subtract(coinOtherVolume.getTotalVolume());

                if (volume.compareTo(BigDecimal.ZERO) <= 0) {
                    LOGGER.error("总盘量小于操盘手总资产:{}", tradeRiskControl.toString());
                    continue;
                }

                LOGGER.info("当前交易币种：{},流通数量为:{}", tradeRiskControl.getCoinOther(), volume);

                final MatchStream matchStream = matchStreamService.
                        findTopByCoinMainAndCoinOther(tradeRiskControl.getCoinMain(),
                                tradeRiskControl.getCoinOther(), 1).get(0);
                if (Objects.isNull(matchStream)) {
                    LOGGER.error("未找到最新流水=========：{},{}", tradeRiskControl.getCoinOther(),
                            tradeRiskControl.getCoinMain());
                    continue;
                }
                final BigDecimal currencyVolume = volume.multiply(matchStream.getPrice())
                        .setScale(8, RoundingMode.HALF_DOWN);

                final BigDecimal ratioVolume = currencyVolume
                        .multiply(new BigDecimal(tradeRiskControl.getRiskRatio()))
                        .setScale(8, RoundingMode.HALF_DOWN);


                LOGGER.info("当前交易对:{},{},当前盘面资量为:{},比例资金为:{}",
                        tradeRiskControl.getCoinOther(), tradeRiskControl.getCoinMain(),
                        currencyVolume, ratioVolume);

                if (ratioVolume.compareTo(coinMainVolume.getTotalVolume()) >= 0) {

                    flagList.forEach(coinVolume -> {
                        userCoinVolumeDao.updateFlag(1,
                                "护盘资金小于盘面资金比例,请充值!", coinVolume.getUserId());
                        redisCacheManager.cleanUserCoinVolume(coinVolume.getUserId(), coinVolume.getCoinSymbol());
                    });
                    continue;
                }

                final List<UserCoinVolume> updateList =
                        allUserCoinVolume.stream().filter(v -> Objects.nonNull(v.getFlag()) && v.getFlag() == 1)
                                .collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(updateList)) {
                    allUserCoinVolume.forEach(coinVolume -> {
                        userCoinVolumeDao.updateFlag(0,
                                "", coinVolume.getUserId());
                        redisCacheManager.cleanUserCoinVolume(coinVolume.getUserId(), coinVolume.getCoinSymbol());
                    });

                }

            }
        }

    }
}
