package com.biao.utils;

import com.biao.config.BombConfig;
import com.biao.constant.Constants;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Component
public class PlatApiService {

    private final Logger logger = LoggerFactory.getLogger(PlatApiService.class);

    @Autowired
    private BombConfig bombConfig;

    private OkHttpHelper oh = OkHttpHelper.get();

    /**
     * 登录处理一下；
     *
     * @param pass pass;
     * @param user user;
     * @return token;
     */
    public String login(String user, String pass) {
        String url = StringUtils.concat(bombConfig.getContext(), Constants.LOGIN);
        Map<String, String> params = Maps.newHashMap();
        params.put(Constants.LOGIN_USERNAME, user);
        params.put(Constants.LOGIN_PASSWORD, pass);
        Response response = oh.requestForm(params, new HashMap<>(), url);
        if (response.sccuess()) {
            logger.info("登录成功！", response.getMsg());
            return response.getMap().getString(Constants.LOGIN_TOKEN);
        } else {
            logger.error("登录失败！", response.getMsg());
            throw new RuntimeException("登录失败！");
        }
    }

    public Map<String, String> headers(String token) {
        Map<String, String> headers = Maps.newHashMap();
        headers.put(Constants.STOKEN, token);
        return headers;
    }

    /**
     * 获取一个订单号；
     *
     * @return 订单号；
     */
    public String getOrderNo(String token) {
        String url = StringUtils.concat(bombConfig.getContext(), Constants.ORDER_NO_URL);
        Map<String, String> params = Maps.newHashMap();
        Response response = oh.requestForm(params, this.headers(token), url);
        if (!response.sccuess()) {
            logger.error("没有获取到正确的订单号！{}", response.getMsg());
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
    public Response buyIn(Trade trade, String token) {
        String url = StringUtils.concat(bombConfig.getContext(), Constants.BUY_URL);
        Map<String, String> params = Maps.newHashMap();
        params.put(Constants.TRADE_ORDER_NO, trade.getOrderNo());
        params.put(Constants.TRADE_VOLUME, trade.getVolume().toString());
        params.put(Constants.COIN_MAIN, trade.getCoinMain());
        params.put(Constants.COIN_OTHER, trade.getCoinOther());
        params.put(Constants.TRADE_USER_ID, trade.getUserId());
        params.put(Constants.TRADE_PRICE, trade.getPrice().toString());
        Response response = oh.requestForm(params, this.headers(token), url);
        if (response.sccuess()) {
            logger.info("【买入】交易已成功{}:{}", trade, response.getMsg());
//            return true;
        } else {
            logger.warn("【买入】交易未成功{}:{}", trade, response.getMsg());
//            return false;
        }

        return response;
    }

    /**
     * 交易；
     *
     * @param trade 交易对象；
     */
    public Response sellOut(Trade trade, String token) {
        String url = StringUtils.concat(bombConfig.getContext(), Constants.SELL_URL);
        Map<String, String> params = Maps.newHashMap();
        params.put(Constants.TRADE_ORDER_NO, trade.getOrderNo());
        params.put(Constants.TRADE_VOLUME, trade.getVolume().toString());
        params.put(Constants.COIN_MAIN, trade.getCoinMain());
        params.put(Constants.COIN_OTHER, trade.getCoinOther());
        params.put(Constants.TRADE_USER_ID, trade.getUserId());
        params.put(Constants.TRADE_PRICE, trade.getPrice().toString());
        Response response = oh.requestForm(params, this.headers(token), url);
        if (response.sccuess()) {
            logger.info("【卖出】交易已成功{}:{}", trade, response.getMsg());
//            return true;
        } else {
            logger.warn("【卖出】交易未成功{}:{}", trade, response.getMsg());
//            return false;
        }
        return response;
    }
}
