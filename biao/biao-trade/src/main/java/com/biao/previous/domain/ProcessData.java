package com.biao.previous.domain;

import lombok.Data;

/**
 * Process的数据处理;
 *
 */
@Data
public class ProcessData {
    /**
     * 是否跳过数据处理;
     */
    private Boolean skip = false;

    /**
     * 数据发生的时候;
     */
    private transient Long concurrentTime;

    /**
     * 订单是否已经完成;
     */
    private Boolean orderComplete = false;

    /**
     * 是否为取消订单
     */
    private Boolean cancelComplete = false;
}
