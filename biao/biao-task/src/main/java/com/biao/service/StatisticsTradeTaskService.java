package com.biao.service;

/**
 *  ""1
 */
public interface StatisticsTradeTaskService {

    /**
     * statisticsTradeTask
     *
     * @param coinMain BTC USDT ETH
     */
    void statisticsTradeTask(String coinMain);

    /**
     * everyMinForDayTrade.
     *
     * @param coinMain BTC USDT ETH
     */
    void everyMinForDayTrade(String coinMain);

    /**
     * 每天交易量统计.
     */
    void statisticsTradeDay();

    /**
     * 增量统计主区k线数据
     *
     * @param coinMain
     */
    void incrementStatisticsTradeTask(String coinMain);


    /**
     * 交易流水的手续费
     * 统计交易手续费  0:买入  1:卖出   TradeDetail:交易流水
     */
    void statisticsTradeDetailFree();


    /**
     * 统计用户每天交易手续费
     */
    void statisticsUserTradeFreesTask();

    /**
     * 补全用户电话所属地区
     */
    void userGeoOneTask();

    /**
     * 用户同步任务
     */
    void userSynTask();

    /**
     * 用户图片迁移
     */
    void userPhoteTransferTask();

    /**
     * 用户同步发送短信
     */
    void userSynSendMessageTask();

    /**
     * 清理验证码图片任务
     */
    void clearValidPhotoTask();
}
