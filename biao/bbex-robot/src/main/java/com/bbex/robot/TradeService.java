package com.bbex.robot;

import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * 交易的处理信息；
 *
 * @author p
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
     * @param pass pass;
     * @param user user;
     * @return token;
     */
    public String login(String user, String pass) {
        String url = RobotPamart.get().getReqeustUrl(Constants.LOGIN);
        Map<String, String> params = Maps.newHashMap();
        params.put(Constants.LOGIN_USERNAME, user);
        params.put(Constants.LOGIN_PASSWORD, pass);
        Response response = oh.requestForm(params, RobotPamart.get().headers(), url);
        if (response.sccuess()) {
            logger.info("登录成功！", response.getMsg());
            return response.getMap().getString(Constants.LOGIN_TOKEN);
        } else {
            logger.error("登录失败！", response.getMsg());
            //登陆不上重试五分钟
            Integer initSecond = 0;
            for (int i = 0; i < 5; i++) {
                try {
                    Thread.sleep(initSecond + 60000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Response result = oh.requestForm(params, RobotPamart.get().headers(), url);
                if (result.sccuess()) {
                    logger.info("登录成功！", response.getMsg());
                    return response.getMap().getString(Constants.LOGIN_TOKEN);
                } else {
                    logger.error("登录失败" + i + 1 + " 次 ！", response.getMsg());
                    continue;
                }
            }
            throw new RuntimeException("登录失败！");
        }
    }

    /**
     * 获取一个订单号；
     *
     * @return 订单号；
     */
    public String getOrderNo() {
        String url = RobotPamart.get().getReqeustUrl(Constants.ORDER_NO_URL);
        Map<String, String> params = Maps.newHashMap();
        Response response = oh.requestForm(params, RobotPamart.get().headers(), url);
        if (!response.sccuess()) {
            logger.error("没有获取到正确的订单号！{}", response.getMsg());
            if (response.getMsg().equals("用户被踢出")) {
                //执行重新登陆
                RobotCtx ctx = new ConfigLoader().loader(RobotCtx.class);
                RobotPamart.get().refreshedToken(() -> this.login(ctx.getLoginUser(), ctx.getLoginPass()));
                Response responseAgain = oh.requestForm(params, RobotPamart.get().headers(), url);
                if (responseAgain.sccuess()) {
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
        String url = RobotPamart.get().getReqeustUrl(Constants.BUY_URL);
        Map<String, String> params = Maps.newHashMap();
        params.put(Constants.TRADE_ORDER_NO, trade.getOrderNo());
        params.put(Constants.TRADE_VOLUME, String.valueOf(trade.getVolume().doubleValue()));
        params.put(Constants.COIN_MAIN, trade.getCoinMain());
        params.put(Constants.COIN_OTHER, trade.getCoinOther());
        params.put(Constants.TRADE_USER_ID, trade.getUserId());
        params.put(Constants.TRADE_PRICE, String.valueOf(trade.getPrice().doubleValue()));
        Response response = oh.requestForm(params, RobotPamart.get().headers(), url);
        if (response.sccuess()) {
            logger.info("【买入】交易已成功{}:{}", trade, response.getMsg());
            return true;
        } else {
            logger.warn("【买入】交易未成功{}:{}", trade, response.getMsg());
            return false;
        }
    }

    /**
     * 交易；
     *
     * @param trade 交易对象；
     */
    public boolean sellOut(Trade trade) {
        String url = RobotPamart.get().getReqeustUrl(Constants.SELL_URL);
        Map<String, String> params = Maps.newHashMap();
        params.put(Constants.TRADE_ORDER_NO, trade.getOrderNo());
        params.put(Constants.TRADE_VOLUME, String.valueOf(trade.getVolume().doubleValue()));
        params.put(Constants.COIN_MAIN, trade.getCoinMain());
        params.put(Constants.COIN_OTHER, trade.getCoinOther());
        params.put(Constants.TRADE_USER_ID, trade.getUserId());
        params.put(Constants.TRADE_PRICE, String.valueOf(trade.getPrice().doubleValue()));
        Response response = oh.requestForm(params, RobotPamart.get().headers(), url);
        if (response.sccuess()) {
            logger.info("【卖出】交易已成功{}:{}", trade, response.getMsg());
            return true;
        } else {
            logger.warn("【卖出】交易未成功{}:{}", trade, response.getMsg());
            return false;
        }
    }
}
