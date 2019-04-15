package com.biao.rebot.service.async;

import java.math.BigDecimal;

/**
 * AsyncDepth.
 * <p>
 *     深度相关的数据.
 * <p>
 * 18-12-18下午2:43
 *
 *  "" sixh
 */
public interface AsyncDepth<D extends AsyncData> extends AsyncData<D> {

    /**
     * 获取价格.
     */
    BigDecimal getPrice();

    /**
     * 获取数量.
     * @return 数量.
     */
    BigDecimal getVolume();
}
