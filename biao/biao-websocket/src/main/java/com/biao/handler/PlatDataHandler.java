package com.biao.handler;

import com.biao.cache.PlatOrderCacheManager;
import com.biao.coin.CoinMainService;
import com.biao.constant.Constants;
import com.biao.constant.RedisConstants;
import com.biao.entity.UserCoinVolume;
import com.biao.enums.TradeEnum;
import com.biao.enums.TradePairEnum;
import com.biao.pojo.UserOrderDTO;
import com.biao.reactive.data.mongo.service.MatchStreamService;
import com.biao.redis.RedisCacheManager;
import com.biao.service.CoinService;
import com.biao.util.DateUtils;
import com.biao.vo.BuySellerOrderVO;
import com.biao.vo.KlineVO;
import com.biao.vo.OrderVo;
import com.biao.vo.PlatOrderVO;
import com.biao.vo.PlatUserPushVO;
import com.biao.vo.TradePairVO;
import com.biao.vo.UserCoinVolumeVO;
import com.biao.vo.redis.RedisExPairVO;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 处理平台数据.
 *
 *  ""
 */
@Component
@SuppressWarnings("all")
public class PlatDataHandler {

    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(PlatDataHandler.class);

    private static final int TOP = 30;

    private static final long REDIS_MAX_LENGTH = 1000L;

    private final MatchStreamService matchStreamService;

    private final RedisTemplate redisTemplate;

    private final RedisCacheManager redisCacheManager;

    private final PlatOrderCacheManager platOrderCacheManager;

    @Autowired
    private CoinMainService coinMainService;

    @Autowired
    public PlatDataHandler(final MatchStreamService matchStreamService,
                           final CoinService coinService,
                           final RedisTemplate redisTemplate,
                           final RedisCacheManager redisCacheManager, PlatOrderCacheManager platOrderCacheManager) {
        this.matchStreamService = matchStreamService;
        this.redisTemplate = redisTemplate;
        this.redisCacheManager = redisCacheManager;
        this.platOrderCacheManager = platOrderCacheManager;
    }

    /**
     * 初始化时候获取买单 卖单前7条数据.
     *
     * @param coinMain  主交易区
     * @param coinOther 被交易币种
     * @param scale     小数位
     * @return BuySellerOrderVO
     */
    public BuySellerOrderVO buyAndSellerOrder(final String coinMain, final String coinOther, final int scale) {
        BuySellerOrderVO vo = new BuySellerOrderVO();
        try {
            final String mapKey = buildMapKey(coinMain, coinOther);
            final List<PlatOrderVO> buyList = platOrderCacheManager.cacheBuyOrder(mapKey);
            List<PlatOrderVO> buyOrderVOS = buildResult(new ArrayList<>(buyList), TradeEnum.BUY.ordinal(), coinMain, coinOther, scale);
            if (buyOrderVOS.size() > TOP) {
                buyOrderVOS = buyOrderVOS.stream().limit(TOP).collect(Collectors.toList());
            }
            vo.setBuyOrderVOList(buyOrderVOS);
            final List<PlatOrderVO> sellList = platOrderCacheManager.cacheSellOrder(mapKey);
            List<PlatOrderVO> sellOrderVOS = buildResult(new ArrayList<>(sellList), TradeEnum.SELL.ordinal(), coinMain, coinOther, scale);
            if (sellOrderVOS.size() > TOP) {
                sellOrderVOS = sellOrderVOS.stream()
                        .skip(sellOrderVOS.size() - TOP)
                        .limit(TOP).collect(Collectors.toList());
            }
            vo.setSellOrderVOList(sellOrderVOS);
            return vo;
        } catch (Exception e) {
            e.printStackTrace();
            return vo;
        }
    }


    /**
     * 交易界面交易对信息.
     *
     * @return Map map
     */
    public Map<String, List<TradePairVO>> buildAllTradePair() {
        List<RedisExPairVO> allExpair = redisCacheManager.acquireAllExpair();
        if(allExpair ==null){
            return new HashMap<String, List<TradePairVO>>();
        }
        final List<RedisExPairVO> sortList = allExpair.stream()
                .sorted(Comparator.comparing(RedisExPairVO::getSort))
                .sorted(Comparator.comparing(RedisExPairVO::getType))
                .collect(Collectors.toList());

        Map<String, List<RedisExPairVO>> listMap = sortList
                .stream()
                .collect(Collectors.groupingBy(RedisExPairVO::getPairOne));

        Map<String, List<RedisExPairVO>> sortMap = Maps.newLinkedHashMap();

        for (String main : coinMainService.getList()) {
            if (CollectionUtils.isNotEmpty(listMap.get(main))) {
                sortMap.put(main, listMap.get(main));
            }
        }

        Map<String, List<TradePairVO>> resultMap = Maps.newLinkedHashMap();

        sortMap.forEach((k, v) -> {
            List<TradePairVO> vos = Lists.newArrayList();
            for (RedisExPairVO exPair : v) {
                final KlineVO klineVO = (KlineVO) redisTemplate.opsForHash().get(buildKey(exPair.getPairOne(), exPair.getPairOther()),
                        DateUtils.formaterLocalDateTime(LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0, 0))));
                TradePairVO vo = new TradePairVO();
                vo.setCoinMain(exPair.getPairOne());
                vo.setCoinOther(exPair.getPairOther());
                vo.setPricePrecision(exPair.getPricePrecision());
                vo.setVolumePrecision(exPair.getVolumePrecision());
                vo.setVolumePercent(exPair.getVolumePercent());
                vo.setType(exPair.getType());
                if (Objects.nonNull(klineVO)) {
                    vo.setHighestPrice(new BigDecimal(klineVO.getH()));
                    vo.setDayCount(new BigDecimal(klineVO.getV()));
                    vo.setLowerPrice(new BigDecimal(klineVO.getL()));
                    vo.setFirstPrice(new BigDecimal(klineVO.getO()));
                    vo.setLatestPrice(new BigDecimal(klineVO.getC()));
                    BigDecimal rise = new BigDecimal(0);
                    if (vo.getFirstPrice().compareTo(new BigDecimal(0)) != 0) {
                        rise = vo.getLatestPrice().subtract(vo.getFirstPrice())
                                .divide(vo.getFirstPrice(), 8, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
                    }
                    vo.setRise(rise.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "%");
                } else {
                    vo.setDayCount(new BigDecimal(0.00));
                    vo.setFirstPrice(new BigDecimal(0.00));
                    vo.setLowerPrice(new BigDecimal(0.00));
                    vo.setHighestPrice(new BigDecimal(0.00));
                    vo.setLatestPrice(new BigDecimal(0.00));
                    vo.setRise("0.00");
                }

                vos.add(vo);
            }
            resultMap.put(k, vos);
        });
        return resultMap;
    }

    private String buildKey(String coinMain, String coinOther) {
        return "kline:" + coinMain + "_" + coinOther + ":1d";
    }


    /**
     * 获取redis里面的交易对信息.
     *
     * @return Map
     */
    private Map<String, List<TradePairVO>> buildTradePairByRedis() {
        List<String> keys = TradePairEnum.buildKeys();
        Map<String, List<TradePairVO>> map =
                Maps.newHashMapWithExpectedSize(keys.size());
        keys.forEach(key -> map.put(key, handlerTradePair(key)));
        return map;
    }

    /**
     * 根据主区获取交易对数据.
     *
     * @param coinMain 主区币代号
     * @return List TradePairMarketVO
     */
    public List<TradePairVO> handlerTradePair(final String coinMain) {
        String key = TradePairEnum.buildTradeKey(coinMain);
        Map<String, TradePairVO> pairVOMap
                = redisTemplate.opsForHash().entries(key);
        return new ArrayList<>(pairVOMap.values());
    }

    /**
     * 将 eth btc 对应的usdt价格 存放到redis.
     *
     * @param coinOther eth btc
     * @param price     对应的 usdt 价格
     */
    public void setRedisLastPrice(final String coinMain, final String coinOther, final BigDecimal price) {
        redisTemplate.opsForHash().put(RedisConstants.TRADE_LAST_PRICE, coinMain + "_" + coinOther, price);
    }


    public PlatUserPushVO buildUserCoinVolume(String userId, String coinMain, String coinOther) {
        PlatUserPushVO platUserPushVO = new PlatUserPushVO();
        platUserPushVO.setCoinMainVolume(buildUserCoinVolumeVO(userId, coinMain));
        platUserPushVO.setCoinOtherVolume(buildUserCoinVolumeVO(userId, coinOther));
        return platUserPushVO;
    }


    private UserCoinVolumeVO buildUserCoinVolumeVO(final String userId, final String coinSymbol) {
        return UserCoinVolume.transform(redisCacheManager.acquireUserCoinVolume(userId, coinSymbol));
    }

    public OrderVo buildOrderVo(final UserOrderDTO orderDTO) {
        OrderVo vo = new OrderVo();
        vo.setExType(orderDTO.getExType());
        vo.setOrderNo(orderDTO.getOrderNo());
        vo.setStatus(orderDTO.getStatus());
        vo.setSuccessVolume(orderDTO.getSuccessVolume());
        return vo;
    }

    private List<PlatOrderVO> buildResult(final List<PlatOrderVO> platOrderVOS, final int type,
                                          final String coinMain, final String coinOther, final int scale) {

        List<PlatOrderVO> result = Lists.newArrayList();
        if (CollectionUtils.isEmpty(platOrderVOS)) {
            return result;
        }
        Map<BigDecimal, Double> maps = platOrderVOS.stream().filter(dto -> Objects.nonNull(dto.getPrice()))
                .map(dto -> {
                    PlatOrderVO vo = new PlatOrderVO();
                    if (TradeEnum.BUY.ordinal() == type) {
                        vo.setPrice(dto.getPrice().setScale(scale, BigDecimal.ROUND_DOWN));
                    } else {
                        vo.setPrice(dto.getPrice().setScale(scale, BigDecimal.ROUND_HALF_UP));
                    }
                    vo.setVolume(dto.getVolume());
                    return vo;
                })
                .collect(Collectors.groupingBy(PlatOrderVO::getPrice,
                        Collectors.summingDouble(e -> e.getVolume().doubleValue())));
        maps.forEach((k, v) -> {
            PlatOrderVO vo = new PlatOrderVO();
            BigDecimal price = k.setScale(scale, BigDecimal.ROUND_HALF_UP);
            vo.setPrice(price);
            vo.setCoinMain(coinMain);
            vo.setCoinOther(coinOther);
            BigDecimal volume = new BigDecimal(v).setScale(8, RoundingMode.HALF_UP);
            vo.setVolume(volume);
            vo.setType(type);
            result.add(vo);

        });
        return result.stream()
                .sorted(Comparator.comparing(PlatOrderVO::getPrice).reversed())
                .collect(Collectors.toList());
    }


    private List<KlineVO> buildKlineVO(final List<TradePairVO> tradePairVOS) {
        if (CollectionUtils.isNotEmpty(tradePairVOS)) {
            return tradePairVOS.stream().map(v -> {
                KlineVO klineVO = new KlineVO();
                klineVO.setS("ok");
                klineVO.setT(String.valueOf(v.getMinuteTime().toInstant(ZoneOffset.of("+8")).toEpochMilli()));
                klineVO.setH(v.getHighestPrice().toPlainString());
                klineVO.setL(v.getLowerPrice().toPlainString());
                klineVO.setV(v.getDayCount().toPlainString());
                klineVO.setO(v.getFirstPrice().toPlainString());
                klineVO.setC(v.getLatestPrice().toPlainString());
                return klineVO;
            }).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    private String buildMapKey(final String coinMain, final String coinOther) {
        return String.join(Constants.JOIN, coinOther, coinMain);
    }

}
