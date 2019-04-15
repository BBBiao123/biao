package com.biao.service;

public interface Mk1DividendRuleService {
    /**
     * 手续费打款到指定账户
     */
    void triggerRemitFeeToPlatAccount();

    /**
     * 用户资产快照
     */
    void triggerUserCoinVolumeSnapshot();
}
