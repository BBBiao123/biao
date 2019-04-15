package com.biao.kline.cache;

import com.biao.vo.KlineVO;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * The type Merge ex.
 */
@Data
public class MergeEx {

    /**
     * 最大值.
     */
    public BigDecimal max = new BigDecimal(0);
    /**
     * sum.
     */
    public BigDecimal volume = new BigDecimal(0);
    /**
     * 最小值.
     */
    public BigDecimal min = new BigDecimal(0);
    /**
     * 时间.
     */
    public LocalDateTime time;
    /**
     * 第一个值.
     */
    public BigDecimal first = new BigDecimal(0);
    /**
     * 最后一个值.
     */
    public BigDecimal last = new BigDecimal(0);


    /**
     * Convert kline vo.
     *
     * @param mergeEx the merge ex
     * @return the kline vo
     */
    public static KlineVO convert(MergeEx mergeEx) {
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


}
