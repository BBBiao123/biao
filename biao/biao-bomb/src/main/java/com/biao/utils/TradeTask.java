package com.biao.utils;

import com.biao.constant.AutoTradeMonitorStatusEnum;
import com.biao.constant.AutoTradeOrderStatusEnum;
import com.biao.entity.MkAutoTradeOrder;
import com.biao.mapper.MkAutoTradeMonitorDao;
import com.biao.mapper.MkAutoTradeOrderDao;
import com.biao.spring.SpringBeanFactoryContext;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.TimerTask;
import java.util.UUID;

public class TradeTask extends TimerTask {

    private final Logger logger = LoggerFactory.getLogger(PlatApiService.class);

    private PlatApiService platApiService;

    private MkAutoTradeOrderDao mkAutoTradeOrderDao;

    private MkAutoTradeMonitorDao mkAutoTradeMonitorDao;

    private MkAutoTradeContext mkAutoTradeContext;

    public TradeTask(MkAutoTradeContext mkAutoTradeContext) {
        this.mkAutoTradeContext = mkAutoTradeContext;
        this.platApiService = (PlatApiService) SpringBeanFactoryContext.findBean(PlatApiService.class);
        this.mkAutoTradeOrderDao = (MkAutoTradeOrderDao) SpringBeanFactoryContext.findBean(MkAutoTradeOrderDao.class);
        this.mkAutoTradeMonitorDao = (MkAutoTradeMonitorDao) SpringBeanFactoryContext.findBean(MkAutoTradeMonitorDao.class);

    }

    @Override
    public void run() {

        if (mkAutoTradeContext.getBeginDate().isAfter(LocalDateTime.now()) || !AutoTradeMonitorStatusEnum.RUNNING.getCode().equals(mkAutoTradeContext.getStatus())) {
            return;
        }

        Trade trade = new Trade();
        this.makeTradeParam(mkAutoTradeContext, trade);

        if ("2".equals(mkAutoTradeContext.getType())) {
            this.placeOrderEntry(trade, "1", mkAutoTradeContext);
            this.placeOrderEntry(trade, "0", mkAutoTradeContext);
        } else {
            this.placeOrderEntry(trade, mkAutoTradeContext.getType(), mkAutoTradeContext);
        }

    }

    public static BigDecimal getPrice(MkAutoTradeContext mkAutoTradeContext) {
        double price = RandomUtils.nextDouble(mkAutoTradeContext.getMinPrice().doubleValue(), mkAutoTradeContext.getMaxPrice().doubleValue());
        return new BigDecimal(price).setScale(mkAutoTradeContext.getPricePrecision(), BigDecimal.ROUND_HALF_UP);
    }

    public static BigDecimal getVolume(MkAutoTradeContext mkAutoTradeContext) {
        double volume = RandomUtils.nextDouble(mkAutoTradeContext.getMinVolume().doubleValue(), mkAutoTradeContext.getMaxVolume().doubleValue());
        return new BigDecimal(volume).setScale(mkAutoTradeContext.getVolumePrecision(), BigDecimal.ROUND_HALF_UP);
    }


    public void makeTradeParam(MkAutoTradeContext mkAutoTradeContext, Trade trade) {

        trade.setStatus(AutoTradeOrderStatusEnum.FAIL.getCode());
        trade.setPrice(BigDecimal.ZERO);
        trade.setVolume(BigDecimal.ZERO);

//        trade.setOrderNo(orderNo);
        trade.setPrice(getPrice(mkAutoTradeContext));
        trade.setCoinMain(mkAutoTradeContext.getCoinMainSymbol());
        trade.setCoinOther(mkAutoTradeContext.getCoinOtherSymbol());
        trade.setUserId(mkAutoTradeContext.getUserId());
        trade.setVolume(getVolume(mkAutoTradeContext));
    }

    public void placeOrderEntry(Trade trade, String type, MkAutoTradeContext mkAutoTradeContext) {
        this.placeOrder(trade, type);
        this.createAutoTradeOrder(mkAutoTradeContext, trade);
    }

    public void placeOrder(Trade trade, String type) {

        String orderNo = "";
        try {
            orderNo = platApiService.getOrderNo(mkAutoTradeContext.getToken());
            trade.setOrderNo(orderNo);
        } catch (Exception e) {
            logger.info("获取订单号失败：" + e);
            trade.setRemark("获取订单号失败");
            return;
        }

        if (StringUtils.isEmpty(trade.getOrderNo())) {
            return;
        }

        Response response = null;
        try {
            if ("0".equals(type)) {  //买入
                response = platApiService.buyIn(trade, mkAutoTradeContext.getToken());
            } else { //卖出
                response = platApiService.sellOut(trade, mkAutoTradeContext.getToken());
            }
        } catch (Exception e) {
            logger.error("下单失败：" + e);
        }


        String tradeType = "0".equals(type) ? "买入" : "卖出";
        if (null != response && response.sccuess()) {
            mkAutoTradeContext.setOrderNumber(mkAutoTradeContext.getOrderNumber() + 1);
            mkAutoTradeContext.setOrderVolume(mkAutoTradeContext.getOrderVolume().add(trade.getVolume()));
            mkAutoTradeContext.setOrderPrice(mkAutoTradeContext.getOrderPrice().add(trade.getVolume().multiply(trade.getPrice())));
            trade.setStatus(AutoTradeOrderStatusEnum.SUCCESS.getCode());
            trade.setRemark(tradeType);
            mkAutoTradeMonitorDao.updateOrderInfo(mkAutoTradeContext.getMonitorId(), trade.getVolume(), trade.getVolume().multiply(trade.getPrice()));
        } else {
            if (null != response) {
                trade.setRemark(response.getMsg());
            } else {
                trade.setRemark("下单失败");
            }
        }
    }

    public void createAutoTradeOrder(MkAutoTradeContext mkAutoTradeContext, Trade trade) {
        MkAutoTradeOrder mkAutoTradeOrder = new MkAutoTradeOrder();
        mkAutoTradeOrder.setId(UUID.randomUUID().toString().replace("-", ""));
        mkAutoTradeOrder.setType(mkAutoTradeContext.getType());
        mkAutoTradeOrder.setCoinMainSymbol(mkAutoTradeContext.getCoinMainSymbol());
        mkAutoTradeOrder.setCoinOtherSymbol(mkAutoTradeContext.getCoinOtherSymbol());
        mkAutoTradeOrder.setUserId(mkAutoTradeContext.getUserId());
        mkAutoTradeOrder.setMail(mkAutoTradeContext.getMail());
        mkAutoTradeOrder.setMobile(mkAutoTradeContext.getMobile());
        mkAutoTradeOrder.setPrice(trade.getPrice());
        mkAutoTradeOrder.setVolume(trade.getVolume());
        mkAutoTradeOrder.setMonitorId(mkAutoTradeContext.getMonitorId());
        mkAutoTradeOrder.setSettingId(mkAutoTradeContext.getSettingId());
        mkAutoTradeOrder.setStatus(trade.getStatus());
        mkAutoTradeOrder.setRemark(trade.getRemark());
        mkAutoTradeOrder.setCreateBy(mkAutoTradeContext.getCreateBy());
        mkAutoTradeOrder.setCreateByName(mkAutoTradeContext.getCreateByName());
        mkAutoTradeOrder.setCreateDate(LocalDateTime.now());
        mkAutoTradeOrder.setUpdateDate(LocalDateTime.now());
        mkAutoTradeOrderDao.insert(mkAutoTradeOrder);
    }
}
