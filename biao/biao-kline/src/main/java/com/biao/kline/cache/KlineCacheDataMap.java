package com.biao.kline.cache;

import com.biao.enums.KlineTimeEnum;
import com.biao.kline.KlineHanderService;
import com.biao.kline.vo.KlineHandlerVO;
import com.biao.util.DateUtils;
import com.biao.vo.KlineVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class KlineCacheDataMap {

    private static volatile Map<String, Map<String, MergeEx>> cacheMap = new ConcurrentHashMap<>();

    private static Logger logger = LoggerFactory.getLogger(KlineCacheDataMap.class);

    private static Map<String, MergeEx> getKlineData(String key) {
        Map<String, MergeEx> klineData = cacheMap.get(key);
        if (klineData == null) {
            klineData = new ConcurrentHashMap<>(GenericKlineCacheData.DEF_CAP);
            cacheMap.put(key, klineData);
        }
        return klineData;
    }

    public static List<KlineVO> readKlineVO(String coinMain, String coinOhter, String klineTimeMsg) {
        return readKlineVO(GenericKlineCacheData.buildKlineCacheKey(coinMain, coinOhter, klineTimeMsg));
    }

    public static List<KlineVO> readKlineVO(String klineKeyStr) {
        Map<String, MergeEx> mapData = cacheMap.get(klineKeyStr);
        if (mapData != null) {
            return mapData.values().stream().sorted(new Comparator<MergeEx>() {
                @Override
                public int compare(MergeEx o1, MergeEx o2) {
                    return o1.time.compareTo(o2.time);
                }
            }).map(mergeEx -> {
                KlineVO klineVO = new KlineVO();
                klineVO.setH(mergeEx.max.toPlainString());
                klineVO.setL(mergeEx.min.toPlainString());
                klineVO.setC(mergeEx.last.toPlainString());
                klineVO.setO(mergeEx.first.toPlainString());
                klineVO.setV(mergeEx.volume.setScale(8, RoundingMode.HALF_DOWN).toPlainString());
                klineVO.setS("ok");
                klineVO.setT(String.valueOf(mergeEx.time.toInstant(ZoneOffset.of("+8")).toEpochMilli()));
                return klineVO;
            }).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    public static KlineVO readDataToRedis(KlineTimeEnum klineTimeEnum, String klineKeyStr,
                                          LocalDateTime formatTradeTime) {
        Map<String, MergeEx> map = getKlineData(klineKeyStr);
        String klineDateKeyStr = DateUtils.formaterLocalDateTime(formatTradeTime);
        MergeEx mergeEx = map.get(klineDateKeyStr);
        if (mergeEx == null) {
            return null;
        }
        KlineVO klineVO = new KlineVO();
        klineVO.setH(mergeEx.max.toPlainString());
        klineVO.setL(mergeEx.min.toPlainString());
        klineVO.setC(mergeEx.last.toPlainString());
        klineVO.setO(mergeEx.first.toPlainString());
        klineVO.setV(mergeEx.volume.setScale(8, RoundingMode.HALF_DOWN).toPlainString());
        klineVO.setS("ok");
        klineVO.setT(String.valueOf(mergeEx.time.toInstant(ZoneOffset.of("+8")).toEpochMilli()));
        return klineVO;
    }

    public static void initRedisToCache(String coinMain, String coinOther, KlineTimeEnum klineTimeEnum, Map<String, KlineVO> redisMapData) {
        if (redisMapData == null) {
            return;
        }
        redisMapData.forEach((key, value) -> {
            try {
                String klineKeyStr = GenericKlineCacheData.buildKlineCacheKey(coinMain, coinOther, klineTimeEnum.getMsg());
                Map<String, MergeEx> map = getKlineData(klineKeyStr);
                LocalDateTime formatTradeTime = KlineHanderService.formatRedisCacheMapKey(key);
                String klineDateKeyStr = DateUtils.formaterLocalDateTime(formatTradeTime);
                MergeEx mergeEx = new MergeEx();
                mergeEx.first = new BigDecimal(value.getO());
                mergeEx.last = new BigDecimal(value.getC());
                mergeEx.max = new BigDecimal(value.getH());
                mergeEx.min = new BigDecimal(value.getL());
                mergeEx.volume = new BigDecimal(value.getV());
                mergeEx.time = formatTradeTime;
                map.put(klineDateKeyStr, mergeEx);
            } catch (ParseException e) {
                logger.error("初始化redis缓存到内存,格式化时间字符串异常,e:{}", e);
            } catch (Exception e2) {
                logger.error("初始化redis缓存到内存,异常,e:{}", e2);
            }
        });
    }

    public static void initRedisToCache(String coinMain, String coinOther, KlineTimeEnum klineTimeEnum, String formatTradeTimeStr, KlineVO klineVO) {
        try {
            String klineKeyStr = GenericKlineCacheData.buildKlineCacheKey(coinMain, coinOther, klineTimeEnum.getMsg());
            Map<String, MergeEx> map = getKlineData(klineKeyStr);
            LocalDateTime formatTradeTime = KlineHanderService.formatRedisCacheMapKey(formatTradeTimeStr);
            MergeEx mergeEx = new MergeEx();
            mergeEx.first = new BigDecimal(klineVO.getO());
            mergeEx.last = new BigDecimal(klineVO.getC());
            mergeEx.max = new BigDecimal(klineVO.getH());
            mergeEx.min = new BigDecimal(klineVO.getL());
            mergeEx.volume = new BigDecimal(klineVO.getV());
            mergeEx.time = formatTradeTime;
            map.put(formatTradeTimeStr, mergeEx);
        } catch (ParseException e) {
            logger.error("初始化redis缓存到内存,格式化时间字符串异常,e:{}", e);
        } catch (Exception e2) {
            logger.error("初始化redis缓存到内存,异常,e:{}", e2);
        }
    }

    public static KlineVO putDataGetKlineVO(KlineTimeEnum klineTimeEnum, String klineKeyStr,
                                            LocalDateTime formatTradeTime, KlineHandlerVO klineHandlerVO) {
        MergeEx mergeEx = putData(klineTimeEnum, klineKeyStr, formatTradeTime, klineHandlerVO);
        if(mergeEx==null) {
        	return null ;
        }
        return MergeEx.convert(mergeEx);
    }

    public static MergeEx putData(KlineTimeEnum klineTimeEnum, String klineKeyStr, LocalDateTime formatTradeTime,
                                  KlineHandlerVO klineHandlerVO) {
        Map<String, MergeEx> map = getKlineData(klineKeyStr);
        String klineDateKeyStr = DateUtils.formaterLocalDateTime(formatTradeTime);
        MergeEx oldKline = map.get(klineDateKeyStr);
        if (oldKline == null) {
            MergeEx mergeEx = convert(klineHandlerVO, formatTradeTime);
            logger.info("============= 初始化kline数据,klineKeyStr:{},归集点formatTradeTime:{},第一条数据klineHandlerVO:{},设置之后mergeEx:{}",
                    klineKeyStr, klineDateKeyStr, klineHandlerVO, mergeEx);
            map.put(klineDateKeyStr, mergeEx);
            return mergeEx;
        } else {
            logger.info("############# 处理kline数据,klineKeyStr:{},归属时间formatTradeTime:{},取出的数据oldKline:{},数据klineHandlerVO:{}",
                    klineKeyStr, klineDateKeyStr, oldKline, klineHandlerVO);
            MergeEx newKline = compAndMerge(oldKline, klineHandlerVO);
            logger.info("============= 处理kline数据,klineKeyStr:{},归属时间formatTradeTime:{},合并计算之后mergeEx:{}",
                    klineKeyStr, klineDateKeyStr, newKline);
            map.put(klineDateKeyStr, newKline);
            return newKline;
        }
    }

    private static MergeEx convert(KlineHandlerVO klineHandlerVO, LocalDateTime formatTradeTime) {
        MergeEx mergeEx = new MergeEx();
        mergeEx.first = klineHandlerVO.getPrice();
        mergeEx.last = klineHandlerVO.getPrice();
        mergeEx.max = klineHandlerVO.getPrice();
        mergeEx.min = klineHandlerVO.getPrice();
        mergeEx.time = formatTradeTime;
        mergeEx.volume = klineHandlerVO.getVolume();
        return mergeEx;
    }

    private static MergeEx compAndMerge(MergeEx merge, KlineHandlerVO klineHandlerVO) {
        // 判断是否大于最高价
        BigDecimal price = klineHandlerVO.getPrice();
        BigDecimal volume = klineHandlerVO.getVolume();
        boolean maxFlag = price.compareTo(merge.max) > 0;
        if (maxFlag) {
            merge.max = price;
        }
        boolean minFlag = price.compareTo(merge.min) <= 0;
        if (minFlag) {
            merge.min = price;
        }
        merge.last = price;
        BigDecimal volume1 = merge.volume;
        merge.volume = volume1.add(volume);
        return merge;
    }

}
