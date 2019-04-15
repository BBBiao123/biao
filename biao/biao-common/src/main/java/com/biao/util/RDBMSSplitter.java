package com.biao.util;

import java.util.ArrayList;
import java.util.List;

/**
 * 对与关系数据为的分页确立。
 * pageStartRow:开始的数据行
 * pageEndRow:指需要加载的数据数量
 * 注意与BaseSplitter的区别
 *
 *  ""
 */
public class RDBMSSplitter extends Splitter {
    /**
     * 数据总行数；
     */
    private Integer allCount;
    /**
     * 开始的时候；
     */
    private Integer start;
    /**
     * 保证不丢失最后一条数据的变量计算；
     */
    private Integer ccc;

    /**
     * RDBM数据库的切分规则；
     *
     * @param adviceNumber 需要切分的次数；
     * @param start        开始；
     * @param allCount     数据总行数；
     */
    RDBMSSplitter(int adviceNumber, int start, int allCount) {
        super(adviceNumber);
        this.allCount = allCount;
        this.start = start;
        this.ccc = start + allCount;
    }

    /**
     * mysql数据库分页封装
     *
     * @return 封装完的对象
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
            /*
             * 这个判断确认不会丢失最后一页数据，
             * 因为 totalCount/adviceNumber 不整除时，如果不做判断会丢失最后一页
             */
            entry.pageStartRow = start;
            if (i == (adviceNumber - 1)) {
                entry.pageEndRow = (ccc - entry.pageStartRow) == 0 ? batchSize : (ccc - entry.pageStartRow);
            } else {
                entry.pageEndRow = batchSize;
                this.start = batchSize + start;
            }
            entry.count = entry.pageEndRow;
            intervalCountList.add(entry);
        }
        return intervalCountList;
    }

}
