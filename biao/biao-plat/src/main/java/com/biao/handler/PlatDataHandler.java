package com.biao.handler;

import com.biao.cache.FlowingWaterCacheManager;
import com.biao.coin.CoinMainService;
import com.biao.constant.Constants;
import com.biao.constant.RedisConstants;
import com.biao.constant.RedisKeyConstant;
import com.biao.constant.TradeConstant;
import com.biao.entity.UserCoinVolume;
import com.biao.enums.TradeEnum;
import com.biao.enums.TradePairEnum;
import com.biao.pojo.TradeDto;
import com.biao.pojo.UserOrderDTO;
import com.biao.reactive.data.mongo.service.MatchStreamService;
import com.biao.redis.RedisCacheManager;
import com.biao.service.CoinService;
import com.biao.util.DateUtils;
import com.biao.vo.BuySellerOrderVO;
import com.biao.vo.KlineResult;
import com.biao.vo.KlineVO;
import com.biao.vo.LastPriceVO;
import com.biao.vo.MatchStreamVO;
import com.biao.vo.OrderVo;
import com.biao.vo.PlatOrderVO;
import com.biao.vo.PlatUserPushVO;
import com.biao.vo.TradePairVO;
import com.biao.vo.UserCoinVolumeVO;
import com.biao.vo.redis.MainCnbVO;
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

    private static final long TRADE_LIMIT = 1000L;

    private static final long REDIS_MAX_LENGTH = 1000L;

    private final MatchStreamService matchStreamService;

    private final CoinService coinService;

    private final RedisTemplate redisTemplate;

    private final RedisCacheManager redisCacheManager;


    @Autowired
    private CoinMainService coinMainService;

    @Autowired
    private FlowingWaterCacheManager flowingWaterCacheManager;


    /**
     * Instantiates a new Plat data handler.
     *
     * @param matchStreamService    the match stream service
     * @param coinService           the coin service
     * @param redisTemplate         the redis template
     * @param redisCacheManager     the redis cache manager
     * @param platOrderCacheManager the plat order cache manager
     */
    @Autowired
    public PlatDataHandler(final MatchStreamService matchStreamService,
                           final CoinService coinService,
                           final RedisTemplate redisTemplate,
                           final RedisCacheManager redisCacheManager
    ) {
        this.matchStreamService = matchStreamService;
        this.coinService = coinService;
        this.redisTemplate = redisTemplate;
        this.redisCacheManager = redisCacheManager;
    }


    /**
     * 将 eth btc 对应的usdt价格 存放到redis.
     *
     * @param coinMain  eth btc
     * @param coinOther eth btc
     * @param price     对应的 usdt 价格
     */
    public void setRedisLastPrice(final String coinMain, final String coinOther, final BigDecimal price) {
        redisTemplate.opsForHash().put(RedisConstants.TRADE_LAST_PRICE, coinMain + "_" + coinOther, price);
    }

    /**
     * 获取交易流水.
     *
     * @param coinMain  主交易区
     * @param coinOther 被交易币种
     * @return List MatchStreamVO
     */
    public List<MatchStreamVO> buildMatchStream(final String coinMain, final String coinOther) {
        flowingWaterCacheManager.refreshMap();
        return flowingWaterCacheManager.find(coinMain, coinOther);
    }

    /**
     * Stat min date time trade pair vo.
     *
     * @param coinMain    the coin main
     * @param coinOther   the coin other
     * @param minDateTime the min date time
     * @return the trade pair vo
     */
    public TradePairVO statMinDateTime(final String coinMain, final String coinOther, final LocalDateTime minDateTime) {
        return matchStreamService.statByMinDate(coinMain, coinOther, minDateTime);
    }


    /**
     * 交易界面合并买单买单小数点位数.
     *
     * @param coinMain  主交易区
     * @param coinOther 被交易币种
     * @param type      类型
     * @param scale     小数点位数
     * @return BuySellerOrderVO buy seller order vo
     */
    public BuySellerOrderVO merge(final String coinMain, final String coinOther, final int type, final int scale) {

        BuySellerOrderVO vo = new BuySellerOrderVO();

        if (TradeEnum.BUY.ordinal() == type) {
            String buyKey = tradeKey(TradeEnum.BUY, coinMain, coinOther);
            final List<PlatOrderVO> buyList = buildPlatTradeVOList(buyKey, REDIS_MAX_LENGTH);
            vo.setBuyOrderVOList(buildResult(buyList, type, coinMain, coinOther, scale));
        } else if (TradeEnum.SELL.ordinal() == type) {
            String sellKey = tradeKey(TradeEnum.SELL, coinMain, coinOther);
            final List<PlatOrderVO> sellList = buildPlatTradeVOList(sellKey, REDIS_MAX_LENGTH);
            vo.setSellOrderVOList(buildResult(sellList, type, coinMain, coinOther, scale));
        } else {
            //获取全部的
            String buyKey = tradeKey(TradeEnum.BUY, coinMain, coinOther);
            final List<PlatOrderVO> buyList = buildPlatTradeVOList(buyKey, REDIS_MAX_LENGTH);
            vo.setBuyOrderVOList(buildResult(buyList, type, coinMain, coinOther, scale));
            String sellKey = tradeKey(TradeEnum.SELL, coinMain, coinOther);
            final List<PlatOrderVO> sellList = buildPlatTradeVOList(sellKey, REDIS_MAX_LENGTH);
            vo.setSellOrderVOList(buildResult(sellList, type, coinMain, coinOther, scale));
        }
        return vo;
    }


    /**
     * 获取K线历史数据.
     *
     * @param coinMain  主交易区
     * @param coinOther 被交易币种
     * @return KlineResult kline result
     */
    public KlineResult historyKlineData(final String coinMain, final String coinOther) {
        KlineResult klineResult = new KlineResult();
        klineResult.setCode(0);
        klineResult.setType("kline");
        List<TradePairVO> tradePairVOList
                = matchStreamService.acquireToDayStatTradeExpair(coinMain, coinOther, LocalDateTime.now());
        Map<String, List<TradePairVO>> pairVOMap =
                redisTemplate.opsForHash()
                        .entries(RedisKeyConstant
                                .buildTaskStatTradeToDay(coinMain, coinOther));
        List<TradePairVO> redisTradePairVOList = new ArrayList();

        pairVOMap.forEach((k, v) -> redisTradePairVOList.addAll(v));
        if (CollectionUtils.isNotEmpty(tradePairVOList)) {
            redisTradePairVOList.addAll(tradePairVOList);
        }
        klineResult.setData(buildKlineVO(redisTradePairVOList));
        return klineResult;
    }


    /**
     * 交易界面交易对信息.
     *
     * @return Map map
     */
    public Map<String, List<TradePairVO>> buildAllTradePair() {
        List<RedisExPairVO> allExpair = redisCacheManager.acquireAllExpair();

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
     * Build user coin volume plat user push vo.
     *
     * @param userId    the user id
     * @param coinMain  the coin main
     * @param coinOther the coin other
     * @return the plat user push vo
     */
    public PlatUserPushVO buildUserCoinVolume(final String userId, final String coinMain, final String coinOther) {
        PlatUserPushVO platUserPushVO = new PlatUserPushVO();
        platUserPushVO.setCoinMainVolume(buildUserCoinVolumeVO(userId, coinMain));
        platUserPushVO.setCoinOtherVolume(buildUserCoinVolumeVO(userId, coinOther));
        return platUserPushVO;
    }


    /**
     * 根据主区获取交易对数据.
     *
     * @param coinMain 主区币代号
     * @return List TradePairMarketVO
     */
    public List<TradePairVO> handlerTradePair(final String coinMain) {
        String key = coinMainService.buildTradeKey(coinMain);
        Map<String, TradePairVO> pairVOMap
                = redisTemplate.opsForHash().entries(key);
        return new ArrayList<>(pairVOMap.values());
    }


    /**
     * 获取 eth btc eos 对应的 USDT 价格.
     *
     * @return LastPriceVO last price vo
     */
    public LastPriceVO buildLastPrice() {
        LastPriceVO lastPriceVO = new LastPriceVO();
        BigDecimal btcLastPrice = (BigDecimal) redisTemplate.opsForHash()
                .get(RedisConstants.TRADE_LAST_PRICE,
                        TradePairEnum.USDT.getKey() + "_" + TradePairEnum.BTC.getKey());
        lastPriceVO.setBtcLastPrice(btcLastPrice);
        BigDecimal etcLastPrice = (BigDecimal) redisTemplate.opsForHash()
                .get(RedisConstants.TRADE_LAST_PRICE,
                        TradePairEnum.USDT.getKey() + "_" + TradePairEnum.ETH.getKey());
        lastPriceVO.setEthLastPrice(etcLastPrice);

        BigDecimal cnbBtcLastPrice = (BigDecimal) redisTemplate.opsForHash()
                .get(RedisConstants.TRADE_LAST_PRICE,
                        TradePairEnum.CNB.getKey() + "_" + TradePairEnum.BTC.getKey());
        lastPriceVO.setCnbBtcLastPrice(cnbBtcLastPrice);

        BigDecimal cnbEthLastPrice = (BigDecimal) redisTemplate.opsForHash()
                .get(RedisConstants.TRADE_LAST_PRICE,
                        TradePairEnum.CNB.getKey() + "_" + TradePairEnum.ETH.getKey());
        lastPriceVO.setCnbEthLastPrice(cnbEthLastPrice);

        BigDecimal cnbEosLastPrice = (BigDecimal) redisTemplate.opsForHash()
                .get(RedisConstants.TRADE_LAST_PRICE,
                        TradePairEnum.CNB.getKey() + "_" + TradePairEnum.EOS.getKey());
        lastPriceVO.setCnbEosLastPrice(cnbEosLastPrice);

        return lastPriceVO;
    }


    /**
     * 获取redis里面的交易对信息.
     *
     * @return Map
     */
    private Map<String, List<TradePairVO>> buildTradePairByRedis() {
        final List<String> keys = coinMainService.getList();
        Map<String, List<TradePairVO>> map =
                Maps.newHashMapWithExpectedSize(keys.size());
        keys.forEach(key -> map.put(key, handlerTradePair(key)));
        return map;
    }

    private UserCoinVolumeVO buildUserCoinVolumeVO(final String userId, final String coinSymbol) {
        return UserCoinVolume.transform(redisCacheManager.acquireUserCoinVolume(userId, coinSymbol));
    }

    /**
     * Build order vo order vo.
     *
     * @param orderDTO the order dto
     * @return the order vo
     */
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
            //LOGGER.error("platOrderVOS = [" + platOrderVOS + "], type = [" + type + "], coinMain = [" + coinMain + "], coinOther = [" + coinOther + "], scale = [" + scale + "]");
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
        return result.stream().sorted(Comparator.comparing(PlatOrderVO::getPrice).reversed()).collect(Collectors.toList());
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

    private List<PlatOrderVO> buildPlatTradeVOList(final String redisKey, final long limit) {
        List<TradeDto> tradeDtoList = buildTradeDto(redisKey, limit);
        return buildTradeOrder(tradeDtoList);
    }

    private List<TradeDto> buildTradeDto(final String redisKey, final long limit) {
        final Set<String> sets = redisTemplate.opsForZSet().range(redisKey, 0, limit);
        if (CollectionUtils.isEmpty(sets)) {
            return Collections.EMPTY_LIST;
        }
        return
                redisTemplate.opsForHash()
                        .multiGet(TradeConstant.TRADE_PREPOSITION_KEY, sets);
    }

    private List<PlatOrderVO> buildTradeOrder(final List<TradeDto> tradeDtoList) {
        if (CollectionUtils.isNotEmpty(tradeDtoList)) {
            return tradeDtoList.stream()
                    .filter(Objects::nonNull)
                    .map(this::convertPlatOrderVO)
                    .collect(Collectors.toList());
        }
        return Lists.newArrayList();

    }

    private PlatOrderVO convertPlatOrderVO(final TradeDto dto) {
        PlatOrderVO platOrderVO = new PlatOrderVO();
        platOrderVO.setCoinMain(dto.getCoinMain());
        platOrderVO.setCoinOther(dto.getCoinOther());
        Optional.ofNullable(dto.getType()).ifPresent(d -> platOrderVO.setType(d.ordinal()));
        platOrderVO.setPrice(dto.getPrice());
        platOrderVO.setVolume(dto.getComputerVolume());
        return platOrderVO;
    }

    private String tradeKey(final TradeEnum tradeEnum, final String coinMain, final String coinOther) {
        return tradeEnum.redisKey(coinMain, coinOther);
    }

    private String buildMapKey(final String coinMain, final String coinOther) {
        return String.join(Constants.JOIN, coinOther, coinMain);
    }

    private Boolean checkCoin(final String coinMain, final String coinOther) {
        /*Coin main = coinService.findByName(coinMain);
        Coin other = coinService.findByName(coinOther);
        return Objects.nonNull(main) && Objects.nonNull(other);*/
        return true;
    }

    /**
     * Acquire all main cnb list.
     *
     * @return the list
     */
    public List<MainCnbVO> acquireAllMainCnb() {
        return redisCacheManager.acquireAllMainCnb();
    }

    private List<PlatOrderVO> buildPlatOrderVO(final String redisKey) {
        final Set<String> sets = redisTemplate.opsForZSet().range(redisKey, 0, -1);
        if (CollectionUtils.isEmpty(sets)) {
            return Collections.synchronizedList(new LinkedList<>());
        }

        final List<TradeDto> tradeDtoList = redisTemplate.opsForHash()
                .multiGet(TradeConstant.TRADE_PREPOSITION_KEY, sets);
        if (CollectionUtils.isNotEmpty(tradeDtoList)) {
            return tradeDtoList.stream()
                    .filter(Objects::nonNull)
                    .map(PlatOrderVO::transform)
                    .collect(Collectors.toCollection(() -> Collections.synchronizedList(new LinkedList<>())));
        }
        return Collections.synchronizedList(new LinkedList<>());
    }



    /**
     * 交易界面交易对信息.
     *
     * @return Map map
     */
    public List<Map<String,Object>>  buildAllTrades() {
        List<RedisExPairVO> allExpair = redisCacheManager.acquireAllExpair();

        final List<RedisExPairVO> sortList = allExpair.stream()
                .sorted(Comparator.comparing(RedisExPairVO::getSort))
                .sorted(Comparator.comparing(RedisExPairVO::getType))
                .collect(Collectors.toList());

        List<Map<String,Object>> tradeList=new ArrayList<>();
            List<TradePairVO> vos = Lists.newArrayList();
            for (RedisExPairVO exPair : sortList) {
                final KlineVO klineVO = (KlineVO) redisTemplate.opsForHash().get(buildKey(exPair.getPairOne(), exPair.getPairOther()),
                        DateUtils.formaterLocalDateTime(LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0, 0))));

                Map<String,Object> map2=new HashMap<>();
                map2.put("symbol",exPair.getPairOther().toLowerCase()+"_"+exPair.getPairOne().toLowerCase());
                String buyRedisKey = tradeKey(TradeEnum.BUY, exPair.getPairOne(), exPair.getPairOther());

                 List<PlatOrderVO> buyList = buildPlatOrderVO(buyRedisKey);
                buyList= buyList.stream().sorted(Comparator.comparing(PlatOrderVO::getPrice).reversed()).collect(Collectors.toList());
                map2.put("buy", BigDecimal.ZERO);
                if(CollectionUtils.isNotEmpty(buyList)){

                    map2.put("buy", buyList.get(0).getPrice());
                }


                map2.put("sell",BigDecimal.ZERO);
                String sellRedisKey = tradeKey(TradeEnum.SELL, exPair.getPairOne(), exPair.getPairOther());
                 List<PlatOrderVO> sellList = buildPlatOrderVO(sellRedisKey);
                sellList= sellList.stream().sorted(Comparator.comparing(PlatOrderVO::getPrice)).collect(Collectors.toList());
                if(CollectionUtils.isNotEmpty(sellList)){

                    map2.put("sell", sellList.get(0).getPrice());
                }
                if (Objects.nonNull(klineVO)) {
                    map2.put("high",new BigDecimal(klineVO.getH()));
                    map2.put("vol",new BigDecimal(klineVO.getV()));
                    map2.put("low",new BigDecimal(klineVO.getL()));
                    map2.put("last",new BigDecimal(klineVO.getC()));
                    BigDecimal rise = new BigDecimal(0);
                    if ((new BigDecimal(klineVO.getO())).compareTo(new BigDecimal(0)) != 0) {
                        rise = (new BigDecimal(klineVO.getC())).subtract(new BigDecimal(klineVO.getO()))
                                .divide(new BigDecimal(klineVO.getO()), 8, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
                    }
                    map2.put("change",rise);
                } else {
                    map2.put("high",new BigDecimal(0.00));
                    map2.put("vol",new BigDecimal(0.00));
                    map2.put("low",new BigDecimal(0.00));
                    map2.put("last",new BigDecimal(0.00));
                    map2.put("change",new BigDecimal(0.00));
                }
                tradeList.add(map2);
            }
        return tradeList;
    }
}
