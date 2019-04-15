package com.biao.kline.cache;

import com.biao.enums.KlineTimeEnum;
import com.biao.kline.KlineHanderService;
import com.biao.kline.impl.KlineCacheDataListImpl;
import com.biao.kline.vo.KlineHandlerVO;
import com.biao.util.DateUtils;
import com.biao.vo.KlineVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The type Kline cache data list.
 */
public class KlineCacheDataList {

    private static Logger logger = LoggerFactory.getLogger(KlineCacheDataList.class);

    private static volatile Map<String, KlineCacheDataListImpl> cacheMap = new ConcurrentHashMap<>();

    private static KlineCacheDataListImpl getKlineData(String klineKeyStr, KlineTimeEnum klineTimeEnum) {
        KlineCacheDataListImpl klineData = cacheMap.get(klineKeyStr);
        if (klineData == null) {
            klineData = new KlineCacheDataListImpl(klineTimeEnum);
            cacheMap.put(klineKeyStr, klineData);
        }
        return klineData;
    }

    /**
     * Read kline vo list.
     *
     * @param coinMain     the coin main
     * @param coinOhter    the coin ohter
     * @param klineTimeMsg the kline time msg
     * @return the list
     */
    public static List<KlineVO> readKlineVO(final String coinMain, final String coinOhter, final String klineTimeMsg) {
        return readKlineVO(GenericKlineCacheData.buildKlineCacheKey(coinMain, coinOhter, klineTimeMsg));
    }

    /**
     * Read kline vo list.
     *
     * @param klineKeyStr the kline key str
     * @return the list
     */
    public static List<KlineVO> readKlineVO(final String klineKeyStr) {
        KlineCacheDataListImpl klineData = cacheMap.get(klineKeyStr);
        return klineData.readKlineVO();
    }

    /**
     * Put data get kline vo kline vo.
     *
     * @param klineTimeEnum   the kline time enum
     * @param klineKeyStr     the kline key str
     * @param formatTradeTime the format trade time
     * @param klineHandlerVO  the kline handler vo
     * @return the kline vo
     */
    public static KlineVO putDataGetKlineVO(KlineTimeEnum klineTimeEnum, String klineKeyStr, LocalDateTime formatTradeTime, KlineHandlerVO klineHandlerVO) {
        MergeEx mergeEx = putData(klineTimeEnum, klineKeyStr, formatTradeTime, klineHandlerVO);
        if(mergeEx==null) {
        	return null ;
        }
        return MergeEx.convert(mergeEx);
    }

    /**
     * Put data merge ex.
     *
     * @param klineTimeEnum   the kline time enum
     * @param klineKeyStr     the kline key str
     * @param formatTradeTime the format trade time
     * @param klineHandlerVO  the kline handler vo
     * @return the merge ex
     */
    public static MergeEx putData(KlineTimeEnum klineTimeEnum, String klineKeyStr, LocalDateTime formatTradeTime, KlineHandlerVO klineHandlerVO) {
        KlineCacheDataListImpl cacheDataListImpl = getKlineData(klineKeyStr, klineTimeEnum);
        return cacheDataListImpl.add(formatTradeTime, klineHandlerVO);
    }

    /**
     * Init redis to cache.
     *
     * @param coinMain      the coin main
     * @param coinOther     the coin other
     * @param klineTimeEnum the kline time enum
     * @param redisMapData  the redis map data
     */
    public static void initRedisToCache(String coinMain, String coinOther, KlineTimeEnum klineTimeEnum,
                                        Map<String, KlineVO> redisMapData) {
        if (redisMapData == null) {
            return;
        }
        Map<String, KlineVO> copyRedisMapData = new HashMap<>(redisMapData);
        redisMapData.keySet().stream().sorted(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                try {
                    return DateUtils.parseLocalDateTime(o1).compareTo(DateUtils.parseLocalDateTime(o2));
                } catch (ParseException e) {
                }
                return 0;
            }
        }).limit(GenericKlineCacheData.DEF_CAP * 2).sorted(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                try {
                    return DateUtils.parseLocalDateTime(o2).compareTo(DateUtils.parseLocalDateTime(o1));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return 0;
            }
        }).forEach(key -> {
            try {
                String klineKeyStr = GenericKlineCacheData.buildKlineCacheKey(coinMain, coinOther, klineTimeEnum.getMsg());
                KlineCacheDataListImpl cacheDataListImpl = getKlineData(klineKeyStr, klineTimeEnum);
                LocalDateTime formatTradeTime = KlineHanderService.formatRedisCacheMapKey(key);
                KlineVO klineVO = copyRedisMapData.get(key);
                cacheDataListImpl.initAdd(formatTradeTime, klineVO);
            } catch (ParseException e) {
                logger.error("初始化redis缓存到内存,格式化时间字符串异常,e:{}", e);
            } catch (Exception e2) {
                logger.error("初始化redis缓存到内存,异常,e:{}", e2);
            }
        });
    }

    /**
     * Init redis to cache.
     *
     * @param coinMain           the coin main
     * @param coinOther          the coin other
     * @param klineTimeEnum      the kline time enum
     * @param formatTradeTimeStr the format trade time str
     * @param klineVO            the kline vo
     */
    public static void initRedisToCache(String coinMain, String coinOther, KlineTimeEnum klineTimeEnum, String formatTradeTimeStr, KlineVO klineVO) {
        try {
            String klineKeyStr = GenericKlineCacheData.buildKlineCacheKey(coinMain, coinOther, klineTimeEnum.getMsg());
            KlineCacheDataListImpl cacheDataListImpl = getKlineData(klineKeyStr, klineTimeEnum);
            LocalDateTime formatTradeTime = KlineHanderService.formatRedisCacheMapKey(formatTradeTimeStr);
            cacheDataListImpl.initAdd(formatTradeTime, klineVO);
        } catch (ParseException e) {
            logger.error("初始化redis缓存到内存,格式化时间字符串异常,e:{}", e);
        } catch (Exception e2) {
            logger.error("初始化redis缓存到内存,异常,e:{}", e2);
        }
    }
}
