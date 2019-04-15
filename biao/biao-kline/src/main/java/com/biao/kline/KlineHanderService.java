package com.biao.kline;

import com.biao.enums.KlineTimeEnum;
import com.biao.kline.cache.GenericKlineCacheData;
import com.biao.kline.cache.KlineCacheDataList;
import com.biao.kline.vo.KlineHandlerVO;
import com.biao.spring.SpringBeanFactoryContext;
import com.biao.util.DateUtils;
import com.biao.vo.KlineVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

public class KlineHanderService implements Runnable {

    private static Logger logger = LoggerFactory.getLogger(KlineHanderService.class);

    private KlineHandlerVO klineHandlerVO;

    private KlineTimeEnum klineTimeEnum;

    private static LocalTime firstLocalDate = LocalTime.of(0, 0, 0);

    private LocalTime[] klineCycleTime;

    private static Map<String, LocalTime[]> klineCycleTimeMap = new HashMap<>(KlineTimeEnum.values().length);

    private RedisTemplate<String, Object> redisTemplate;

    @SuppressWarnings("unchecked")
    public KlineHanderService(KlineHandlerVO klineHandlerVO, KlineTimeEnum klineTimeEnum) {
        this.klineHandlerVO = klineHandlerVO;
        this.klineTimeEnum = klineTimeEnum;
        this.redisTemplate = (RedisTemplate<String, Object>) SpringBeanFactoryContext.findBean("redisTemplate");
        init();
    }

    private void buildMinuteAndHourCycle(int factor) {
        LocalTime[] klineCycleTimeCache = klineCycleTimeMap.get(String.valueOf(klineTimeEnum.getTime()));
        if (klineCycleTimeCache != null) {
            klineCycleTime = klineCycleTimeCache;
            return;
        }
        logger.info("初始化计算周期,klineTimeEnum:{}", klineTimeEnum.getMsg());
        klineCycleTimeCache = new LocalTime[factor * 24 * 60 / klineTimeEnum.getTime()];
        LocalTime currentLocalTime = firstLocalDate;
        for (int i = 0; i < klineCycleTimeCache.length; i++) {
            klineCycleTimeCache[i] = currentLocalTime;
            currentLocalTime = currentLocalTime.plus(klineTimeEnum.getTime(), ChronoUnit.MINUTES);
            logger.info("klineData:{}", DateUtils.formaterTime(klineCycleTimeCache[i], DateUtils.HMS_FORMAT));
        }
        logger.info("klineCycle的长度length:{}", klineCycleTimeCache.length);
        klineCycleTimeMap.put(String.valueOf(klineTimeEnum.getTime()), klineCycleTimeCache);
        klineCycleTime = klineCycleTimeCache;
        logger.info("初始化12小时的klineCycle完成");
    }


    private void init() {
        switch (klineTimeEnum) {
            case ONE_MINUTE:
            case FIVE_MINUTE:
            case FIFTEEN_MINUTE:
            case THIRTY_MINUTE:
            case ONE_HOUR:
            case TWO_HOUR:
            case FOUR_HOUR:
            case SIX_HOUR:
            case EIGHT_HOUR:
            case TWELVE_HOUR:
                buildMinuteAndHourCycle(1);
                break;
            case ONE_DAY:
                break;
            case ONE_WEEK:
                break;
            case ONE_MONTH:
                break;
            default:
                logger.error("不支持的kline计算单位msg:{}", klineTimeEnum.getMsg());
                break;
        }
    }

    @Override
    public void run() {
        try {
            logger.info("计算kline数据coinMain:{},otherCoin:{},周期msg:{}", klineHandlerVO.getCoinMain(),
                    klineHandlerVO.getCoinOther(), klineTimeEnum.getMsg());
            // 计算归集点
            LocalDateTime formatTradeTime = null;
            switch (klineTimeEnum) {
                case ONE_MINUTE:
                case FIVE_MINUTE:
                case FIFTEEN_MINUTE:
                case THIRTY_MINUTE:
                case ONE_HOUR:
                case TWO_HOUR:
                case FOUR_HOUR:
                case SIX_HOUR:
                case EIGHT_HOUR:
                case TWELVE_HOUR:
                    formatTradeTime = handerMinuteAndHourKlinePoint();
                    break;
                case ONE_DAY:
                    formatTradeTime = handerDayKlinePoint();
                    break;
                case ONE_WEEK:
                    formatTradeTime = handerWeekKlinePoint();
                    break;
                case ONE_MONTH:
                    formatTradeTime = handerMonthKlinePoint();
                    break;
                default:
                    logger.error("不支持的kline计算单位msg:{}", klineTimeEnum.getMsg());
                    break;
            }
            if (formatTradeTime == null) {
                logger.error("计算kline数据coinMain:{},otherCoin:{},周期msg:{},归集时间time为空,数据异常", klineHandlerVO.getCoinMain(),
                        klineHandlerVO.getCoinOther(), klineTimeEnum.getMsg());
                return;
            }
            logger.info("kline的单位:{},交易时间tradeTime:{},在kline中的时间格式为formatTradeTime:{}", klineTimeEnum.getMsg(), klineHandlerVO.getTradeTime(),
                    DateUtils.formaterLocalDateTime(formatTradeTime));
            final String klineKeyStr = GenericKlineCacheData.buildKlineCacheKey(klineHandlerVO.getCoinMain(), klineHandlerVO.getCoinOther(), klineTimeEnum.getMsg());
            KlineVO klineVo = KlineCacheDataList.putDataGetKlineVO(klineTimeEnum, klineKeyStr, formatTradeTime, klineHandlerVO);
            if(klineVo == null) {
            	logger.info("kline的单位:{},交易时间tradeTime:{},在kline中的时间格式为formatTradeTime:{},klineHandlerVO:{},返回数据klineVo为空,被丢弃.", klineTimeEnum.getMsg(), klineHandlerVO.getTradeTime(),
                        DateUtils.formaterLocalDateTime(formatTradeTime),klineHandlerVO);
            	return ;
            }
            logger.info("klineKeyStr:{},交易时间tradeTime:{},在kline中的时间格式为formatTradeTime:{},合并计算结果klineVO:{}", klineKeyStr, klineHandlerVO.getTradeTime(),
                    DateUtils.formaterLocalDateTime(formatTradeTime), klineVo);
            String klineDateKeyStr = formatRedisCacheMapKey(formatTradeTime);
            redisTemplate.boundHashOps(klineKeyStr).put(klineDateKeyStr, klineVo);
        } catch (Exception e) {
            logger.error("计算kline数据coinMain:{},otherCoin:{},周期msg:{},error:{}", klineHandlerVO.getCoinMain(),
                    klineHandlerVO.getCoinOther(), klineTimeEnum.getMsg(), e);
        }
    }

    public static String formatRedisCacheMapKey(LocalDateTime formatTradeTime) {
        return DateUtils.formaterLocalDateTime(formatTradeTime);
    }

    public static LocalDateTime formatRedisCacheMapKey(String formatTradeTimeStr) throws ParseException {
        return DateUtils.parseLocalDateTime(formatTradeTimeStr);
    }

    private LocalDateTime handerMinuteAndHourKlinePoint() throws ParseException {
        String tradeTime = klineHandlerVO.getTradeTime();
        LocalDateTime tradeTimeDate = DateUtils.parseLocalDateTime(tradeTime);
        LocalDate tradeLocalDate = tradeTimeDate.toLocalDate();
        long minCycle = DateUtils.getMinutesBetween(LocalDateTime.of(tradeLocalDate, firstLocalDate), tradeTimeDate);
        int mod = (int) minCycle / klineTimeEnum.getTime();
        logger.info("kline的单位:{},tradeTime:{},取余结果minCycle:{},归集点位置index:{},归集时间段time:{}", klineTimeEnum.getMsg(), tradeTime, minCycle, mod,
                DateUtils.formaterTime(klineCycleTime[mod], DateUtils.HMS_FORMAT));
        return LocalDateTime.of(tradeLocalDate, klineCycleTime[mod]);
    }

    private LocalDateTime handerDayKlinePoint() throws ParseException {
        String tradeTime = klineHandlerVO.getTradeTime();
        LocalDateTime tradeTimeDate = DateUtils.parseLocalDateTime(tradeTime);
        LocalDate tradeLocalDate = tradeTimeDate.toLocalDate();
        logger.info("day tradeTime:{},归集时间段time:{}", tradeTime, DateUtils.formaterDate(tradeLocalDate));
        return LocalDateTime.of(tradeLocalDate, firstLocalDate);
    }

    private LocalDateTime handerWeekKlinePoint() throws ParseException {
        String tradeTime = klineHandlerVO.getTradeTime();
        LocalDateTime tradeTimeDate = DateUtils.parseLocalDateTime(tradeTime);
        LocalDate tradeLocalDate = tradeTimeDate.toLocalDate();
        LocalDate klineWeek = DateUtils.getWeekStart(tradeLocalDate);
        logger.info("week tradeTime:{},归集时间段time:{}", tradeTime, DateUtils.formaterDate(klineWeek));
        return LocalDateTime.of(klineWeek, firstLocalDate);
    }

    private LocalDateTime handerMonthKlinePoint() throws ParseException {
        String tradeTime = klineHandlerVO.getTradeTime();
        LocalDateTime tradeTimeDate = DateUtils.parseLocalDateTime(tradeTime);
        LocalDate tradeLocalDate = tradeTimeDate.toLocalDate();
        LocalDate klineMonth = LocalDate.of(tradeLocalDate.getYear(), tradeLocalDate.getMonth(), 1);
        logger.info("month tradeTime:{},归集时间段time:{}", tradeTime, DateUtils.formaterDate(klineMonth));
        return LocalDateTime.of(klineMonth, firstLocalDate);
    }

}
