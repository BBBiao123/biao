package com.biao.util;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * <pre>
 * * 分解数据计算对象生成,
 * 这个主要用于分解类似list的数据
 * 从开始位置-->结束位置上的确立。
 * pageStartRow:开始位置
 * pageEndRow:结束位置
 * </pre>
 *
 *  ""
 */
public class BaseSplitter extends Splitter {
    /**
     * 数据总行数；
     */
    private Integer allCount;

    /**
     * 切分数据；
     *
     * @param adviceNumber 序计切分的次数；
     * @param allCount     数据总行数；
     */
    BaseSplitter(int adviceNumber, int allCount) {
        super(adviceNumber);
        this.allCount = allCount;
    }

    /**
     * 分解List对象
     *
     * @return 分解后的对象
     */
    @Override
    List<Entry> spliter() {
        //计算数据基数,分页计算
        int batchSize = allCount / adviceNumber;
        if (batchSize <= 0) {
            batchSize = allCount;
            adviceNumber = 1;
        }
        List<Entry> intervalCountList = new ArrayList<>();
        for (int i = 0; i < adviceNumber; i++) {
            Splitter.Entry entry = new Splitter.Entry();
            if (i == (adviceNumber - 1)) {
                entry.pageEndRow = allCount;
            } else {
                entry.pageEndRow = ((i * batchSize)) + (batchSize);
            }
            entry.pageStartRow = (i * batchSize);
            entry.count = entry.pageEndRow - entry.pageStartRow;
            intervalCountList.add(entry);
        }
        return intervalCountList;
    }
}
