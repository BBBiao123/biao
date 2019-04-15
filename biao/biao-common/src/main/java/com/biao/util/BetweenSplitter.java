package com.biao.util;

import java.util.ArrayList;
import java.util.List;

/**
 * <p> 用于间距之前的数据切分。
 * 注意需要与RDBMSSplitter的切分算法区分；他们
 * 有本质上的区分；
 * <p>
 * * 例 ： 算法语义：
 * 从start到end的位置结束需要切分adviceNumber次；
 * start=10 , end=100; adviceNumber=10
 * <p>
 * </p>
 *
 *
 */
public class BetweenSplitter extends Splitter {
    /**
     * 开始的位置；
     */
    private Integer start;
    /**
     * 结束的位置；
     */
    private Integer end;
    /**
     * 可能的数据总条件；
     */
    private Integer allCount;

    /**
     * @param adviceNumber 希望切分的次数；
     * @param start        开始位置；
     * @param end          结束的位置
     */
      BetweenSplitter(int adviceNumber, int start, int end) {
        super(adviceNumber);
        this.start = start;
        this.end = end;
        this.allCount = end - start;
    }

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
            Entry entry = new Entry();

            entry.pageStartRow = start;
            /*
             * 这个判断确认不会超出最后一页的数据；
             *
             */
            if (start > end) {
                break;
            } else {
                entry.pageEndRow = (batchSize + start) > end ? end : (batchSize + start);
                this.start = batchSize + start + 1;
            }
            entry.count = (entry.pageEndRow - entry.pageStartRow)+1;
            intervalCountList.add(entry);
        }
        return intervalCountList;
    }
}
