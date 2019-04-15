package com.biao.util;

import java.util.List;

/**
 * 数据数据查询分解器实现
 * <p>
 * Author chenbin
 *
 *  ""
 * @version 1.0
 **/
public abstract class Splitter {
    /**
     * 分解次数
     */
    int adviceNumber;

    /**
     * 初始构造
     *
     * @param adviceNumber 预计封装的次数
     */
    Splitter(int adviceNumber) {
        this.adviceNumber = adviceNumber;
    }

    /**
     * 使用Redbms算法进行切分数据点返回；
     *
     * @param adviceNumber 预计切分的次数；
     * @param start        开始
     * @param allCount     数据总行数；
     * @return 切分完的数据点；
     * @see RDBMSSplitter#spliter()
     */
    public static List<Entry> rdbmsSplitter(int adviceNumber, int start, int allCount) {
        return new RDBMSSplitter(adviceNumber, start, allCount).spliter();
    }

    /**
     * 使用Redbms算法进行切分数据点返回；
     *
     * @param adviceNumber 预计切分的次数；
     * @param allCount     数据总行数；
     * @return 切分完的数据点；
     * @see BaseSplitter#spliter()
     */
    public static List<Entry> baseSplitter(int adviceNumber, int allCount) {
        return new BaseSplitter(adviceNumber, allCount).spliter();
    }

    /**
     * 使用Redbms算法进行切分数据点返回；
     *
     * @param adviceNumber 预计切分的次数；
     * @param start        开始；
     * @param end          结束；
     * @return 切分完的数据点；
     * @see BetweenSplitter#spliter()
     */
    public static List<Entry> betweenSplitter(int adviceNumber, int start, int end) {
        return new BetweenSplitter(adviceNumber, start, end).spliter();
    }

    /**
     * 分解数据器
     *
     * @return 分解结果对象集合
     */
    abstract List<Entry> spliter();

    /**
     * 分解对象封装
     */
    public class Entry {
        /**
         * 开始;
         */
        public int pageStartRow;
        /**
         * 结束，目前来说根据不同的分解器，有不同的意义产生，具体查询对应的实现类解释
         */
        public int pageEndRow;
        /**
         * 产生的数据条数；
         */
        public int count;

        @Override
        public String toString() {
            final StringBuffer sb = new StringBuffer("Entry{");
            sb.append("pageStartRow=").append(pageStartRow);
            sb.append(", pageEndRow=").append(pageEndRow);
            sb.append(", count=").append(count);
            sb.append('}');
            return sb.toString();
        }
    }
}
