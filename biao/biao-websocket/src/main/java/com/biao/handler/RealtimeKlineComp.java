package com.biao.handler;

import com.biao.pojo.MatchStreamDto;
import com.biao.util.DateUtils;
import com.biao.vo.TradePairVO;
import com.google.common.base.Joiner;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("all")
public enum RealtimeKlineComp {
    INST;
    /**
     * 对像;
     */
    private Map<String, MergeEx> cache = new ConcurrentHashMap<>();

    /**
     * 计算数据;
     *
     * @param merge 旧对象;
     * @param dto   当前对象;
     * @return 计算后的对象;
     */
    private MergeEx comp0(MergeEx merge, MatchStreamDto dto) {
        /*
         * 当前分钟数;
         */
        LocalDateTime currentMini = merge.time;
        /*
         * 当前时间;
         */
        LocalDateTime localDateTime = LocalDateTime.now();
        try {
            localDateTime = DateUtils.parseLocalDateTime(dto.getMinuteTime());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        BigDecimal price = dto.getPrice();
        BigDecimal volume = dto.getVolume();
        if (currentMini == null || currentMini.isBefore(localDateTime)) {
            merge.min = price;
            merge.max = price;
            merge.volume = volume;
            merge.first = price;
            merge.time = localDateTime;
            merge.last = price;
            return merge;
        }
        merge.last = price;
        //判断大于
        boolean maxFlag = price.compareTo(merge.max) > 0;
        if (maxFlag) {
            merge.max = price;
        }
        boolean minFlag = price.compareTo(merge.min) <= 0;
        if (minFlag) {
            merge.min = price;
        }
        BigDecimal volume1 = merge.volume;
        merge.volume = volume1.add(volume);
        return merge;
    }

    /**
     * 返回计算结果;
     *
     * @param dto dto;
     * @return TradePairVO
     */
    public TradePairVO comp(MatchStreamDto dto) {
        String join = Joiner.on("_").join(dto.getCoinMain(), dto.getCoinOther());
        MergeEx old;
        if (cache.containsKey(join)) {
            old = cache.get(join);
        } else {
            old = new MergeEx();
        }
        MergeEx mergeEx = comp0(old, dto);
        cache.put(join, mergeEx);
        TradePairVO vo = new TradePairVO();
        vo.setMinuteTime(mergeEx.time);
        vo.setHighestPrice(mergeEx.max);
        vo.setLowerPrice(mergeEx.min);
        vo.setDayCount(mergeEx.volume);
        vo.setFirstPrice(mergeEx.first);
        vo.setLatestPrice(mergeEx.last);
        return vo;
    }

    /**
     * 内部处理的对象;
     */
    private class MergeEx {
        /**
         * 最大值;
         */
        private BigDecimal max = new BigDecimal(0);
        /**
         * sum;
         */
        private BigDecimal volume = new BigDecimal(0);
        /**
         * 最小值;
         */
        private BigDecimal min = new BigDecimal(0);
        /**
         * 时间;
         */
        private LocalDateTime time;
        /**
         * 第一个值;
         */
        private BigDecimal first = new BigDecimal(0);
        /**
         * 最后一个值;
         */
        private BigDecimal last = new BigDecimal(0);
    }
}
