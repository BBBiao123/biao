package com.biao.rebot.service;

import com.bbex.robot.RobotPamart;
import com.biao.enums.TradeEnum;
import com.biao.rebot.common.Constants;
import com.biao.rebot.common.OkHttpHelper;
import com.biao.rebot.common.Response;
import com.biao.rebot.config.*;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * 交易的处理信息；
 *
 *
 */
public class TradeService {
    /**
     * 请求体；
     */
    private OkHttpHelper oh = OkHttpHelper.get();

    private final Logger logger = LoggerFactory.getLogger(TradeService.class);

    /**
     * 登录处理一下；
     *
     * @param user user;
     * @param pass pass;
     * @return token ;
     */
    public String login(String user, String pass) {
        String url = RobotParam.get().getRequestUrl(Constants.LOGIN);
        Map<String, String> params = Maps.newHashMap();
        params.put(Constants.LOGIN_USERNAME, user);
        params.put(Constants.LOGIN_PASSWORD, pass);
        Response response = oh.requestForm(params, Maps.newHashMap(), url);

        if (response.success()) {
            logger.info("登录成功！", response.getMsg());
            return response.getMap().getString(com.bbex.robot.Constants.LOGIN_TOKEN);
        } else {
            logger.error("登录失败！", response.getMsg());
            //登陆不上重试十分钟
            Integer initSecond = 0;
            for (int i = 0; i < 5; i++) {
                try {
                    Thread.sleep(initSecond + 120000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Response result = oh.requestForm(params, RobotPamart.get().headers(), url);
                if (result.success()) {
                    logger.info("登录成功！", response.getMsg());
                    return response.getMap().getString(com.bbex.robot.Constants.LOGIN_TOKEN);
                } else {
                    logger.error("登录失败" + i + 1 + " 次 ！", response.getMsg());
                    continue;
                }
            }
            throw new RuntimeException("登录失败！");
        }

//        if (response.success()) {
//            logger.info("登录成功！{}", response.getMsg());
//            return response.getMap().getString(Constants.LOGIN_TOKEN);
//        } else {
//            logger.error("登录失败！{}", response.getMsg());
//            return "";
//        }
    }


    /**
     * 登录处理一下；
     *
     * @param users the users
     * @return token ;
     */
    public Map<String, String> login(List<Login> users) {
        Map<String, String> tokens = Maps.newHashMap();
        users.forEach(e -> {
            String token = login(e.getUserName(), e.getPassword());
            if (StringUtils.isNoneBlank(token)) {
                tokens.put(e.getUserId(), token);
            }
        });
        return tokens;
    }

    private Supplier<String> login(String symbolic) {
        return () -> {
            RobotWeight robotWeight = RobotParam.get().getRobotWeight(symbolic);
            return login(robotWeight.getLogin().getUserName(), robotWeight.getLogin().getPassword());
        };
    }

    /**
     * 获取一个订单号；
     *
     * @return 订单号 ；
     */
    public String getOrderNo(String symbolf) {
        String url = RobotParam.get().getRequestUrl(Constants.ORDER_NO_URL);
        Map<String, String> params = Maps.newHashMap();
        Response response = oh.requestForm(params, RobotParam.get().headers(symbolf, login(symbolf)), url);
        if (!response.success()) {
            logger.error("--------没有获取到正确的订单号！{}", response.getMsg());
            if (response.getMsg().equals("用户被踢出")) {
                //执行重新登陆
                try {
                    Thread.sleep(180000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                RobotCtx ctx = new ConfigLoader().loader(RobotCtx.class);
                RobotParam.get().refreshedToken(() -> this.login(RobotParam.get().getUsers()));
                Response responseAgain = oh.requestForm(params, RobotParam.get().headers(symbolf, login(symbolf)), url);
                if (responseAgain.success()) {
                    return response.getMap().getString(Constants.TRADE_ORDER_NO);
                }
            }
            throw new RuntimeException("没有获取到正确的订单号。" + response.getMsg());
        }
        return response.getMap().getString(Constants.TRADE_ORDER_NO);
    }

    /**
     * 交易；
     *
     * @param trade 交易对象；
     * @return true or false;
     */
    public boolean buyIn(Trade trade) {
        String url = RobotParam.get().getRequestUrl(Constants.BUY_URL);
        Map<String, String> params = getParams(trade);
        String symbolf = RobotParam.joinSymbolByType(trade.getCoinMain(), trade.getCoinOther(), TradeEnum.BUY);
        Response response = oh.requestForm(params, RobotParam.get().headers(symbolf, login(symbolf)), url);
        if (response.success()) {
            logger.info("【买入】交易已成功{}:{}", trade, response.getMsg());
            return true;
        } else {
            logger.warn("【买入】交易未成功{}:{}", trade, response.getMsg());
            return false;
        }
    }

    private Map<String, String> getParams(Trade trade) {
        Map<String, String> params = Maps.newHashMap();
        params.put(Constants.TRADE_ORDER_NO, trade.getOrderNo());
        BigDecimal volume1 = trade.getVolume();
        String volume =volume1.scale() > 0 ? String.valueOf(volume1.doubleValue()) : String.valueOf(volume1.intValue());
        BigDecimal price1 = trade.getPrice();
        String price =price1.scale() > 0 ? String.valueOf(price1.doubleValue()) :String.valueOf(price1.intValue());
        params.put(Constants.TRADE_VOLUME, volume);
        params.put(Constants.COIN_MAIN, trade.getCoinMain());
        params.put(Constants.COIN_OTHER, trade.getCoinOther());
        params.put(Constants.TRADE_USER_ID, trade.getUserId());
        params.put(Constants.TRADE_PRICE, price);
        return params;
    }

    /**
     * 交易；
     *
     * @param trade 交易对象；
     * @return the boolean
     */
    public boolean sellOut(Trade trade) {
        String url = RobotParam.get().getRequestUrl(Constants.SELL_URL);
        Map<String, String> params = getParams(trade);
        String symbolf = RobotParam.joinSymbolByType(trade.getCoinMain(), trade.getCoinOther(), TradeEnum.BUY);
        Response response = oh.requestForm(params, RobotParam.get().headers(symbolf, login(symbolf)), url);
        if (response.success()) {
            logger.info("【卖出】交易已成功{}:{}", trade, response.getMsg());
            return true;
        } else {
            logger.warn("【卖出】交易未成功{}:{}", trade, response.getMsg());
            return false;
        }
    }

    /**
     * 取消订单.
     *
     * @param orderNo orderNo.
     * @return 取消. boolean
     */
    public boolean cancel(String orderNo, String symbol) {
        String url = RobotParam.get().getRequestUrl(Constants.CANCEL_URL);
        Map<String, String> params = Maps.newHashMap();
        params.put(Constants.TRADE_ORDER_NO, orderNo);
        Response response;
        try {
            response = oh.requestRestGet(params, RobotParam.get().headers(symbol, login(symbol)), url);
        } catch (UnsupportedEncodingException e) {
            logger.error("发送GET请求失败{}", e);
            return false;
        }
        if (response.success()) {
            logger.info("【取消订单】交易已成功{}:{}", orderNo, response.getMsg());
            return true;
        } else {
            logger.warn("【取消订单】交易未成功{}:{}", orderNo, response.getMsg());
            return false;
        }
    }
}
