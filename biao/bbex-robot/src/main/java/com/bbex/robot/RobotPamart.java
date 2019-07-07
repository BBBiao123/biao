package com.bbex.robot;

import com.bbex.robot.dao.RobotSupper;
import com.google.common.base.Joiner;
import com.google.common.base.Supplier;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Robot参数；
 *
 * @author p
 */
public class RobotPamart {
    private final Logger logger = LoggerFactory.getLogger(RobotPamart.class);

    /**
     * 需要机器人帮你交易的交易对信息；
     */
    private List<RobotWeight> params = Lists.newCopyOnWriteArrayList();

    /**
     * 需要机器人帮你交易的交易对信息；
     */
    private List<RobotWeight> paramsCache = Lists.newCopyOnWriteArrayList();

    /**
     * 保存所有的url信息；
     */
    private Map<String, String> requestUrls = Maps.newHashMap();
    /**
     * ctx信息；
     */
    private RobotCtx robotCtx;

    /**
     * 获取配置文件的一些信息；
     *
     * @return 信息 ；
     */
    public RobotCtx getRobotCtx() {
        return robotCtx;
    }

    /**
     * 设置 一些信息；
     *
     * @param robotCtx 信息；
     */
    public void setRobotCtx(RobotCtx robotCtx) {
        this.robotCtx = robotCtx;
    }

    /**
     * 对象持有；
     */
    private static final class RobotPamartHolder {
        /**
         * The Pamart.
         */
        static final RobotPamart PAMART = new RobotPamart();
    }

    /**
     * 初始化；
     */
    public void init() {
        /*
         * 初始化一些配置信息；
         */
        applyConfig();
        logger.info("初始化配置信息成功！");
    }

    /**
     * 配置信息处理；
     */
    private void applyConfig() {
        params.addAll(RobotSupper.get().gerRobotConfig().queryAll());
        requestUrls.put(Constants.ORDER_NO_URL, Joiner.on("/").join(robotCtx.getUrl(), Constants.ORDER_NO_URL));
        requestUrls.put(Constants.SELL_URL, Joiner.on("/").join(robotCtx.getUrl(), Constants.SELL_URL));
        requestUrls.put(Constants.BUY_URL, Joiner.on("/").join(robotCtx.getUrl(), Constants.BUY_URL));
        requestUrls.put(Constants.LOGIN, Joiner.on("/").join(robotCtx.getUrl(), Constants.LOGIN));
        //校验相关配置信息；
        checkNotNull(robotCtx.getJdbcPass(), "params 【jdbcPass】 is null");
        checkNotNull(robotCtx.getJdbcUrl(), "params 【jdbcUrl】 is null");
        checkNotNull(robotCtx.getJdbcUser(), "params 【JdbcUser】 is null");
        checkNotNull(robotCtx.getLoginPass(), "params 【LoginPass】 is null");
        checkNotNull(robotCtx.getLoginUser(), "params 【LoginUser】 is null");
        checkNotNull(robotCtx.getUrl(), "params 【Url】 is null");
    }

    /**
     * 配置信息处理；
     */
    public  void reloadParamsCache() {
        paramsCache.clear();
        logger.info("------"+this.getRobotCtx().getJdbcUrl());
        paramsCache.addAll(new RobotSupper().gerRobotConfig().queryAll());
    }

    /**
     * 配置信息处理；
     */
    public void reloadParams() {
        params.clear();
        params.addAll(new RobotSupper().gerRobotConfig().queryAll());
    }


    /**
     * 获取一个对象；
     *
     * @return this ;
     */
    public static RobotPamart get() {
        return RobotPamartHolder.PAMART;
    }

    /**
     * 获取一个可以交易对配置信息；
     *
     * @return 交易对 ；
     */
    public List<RobotWeight> getParams() {
        return Lists.newArrayList(params);
    }

    /**
     * 获取一个可以交易对配置信息；
     *
     * @return 交易对 ；
     */
    public List<RobotWeight> getRealParams() {
        return params;
    }

    /**
     * 获取 访问的url;
     *
     * @param key the key
     * @return key ;
     * @see Constants
     */
    public String getReqeustUrl(String key) {
        return requestUrls.get(key);
    }

    /**
     * 刷新一下token;
     *
     * @param token the token
     */
    public void refreshedToken(String token) {
        robotCtx.setToken(token);
    }

    /**
     * 刷新token;
     *
     * @param sp 实现；
     */
    public void refreshedToken(Supplier<String> sp) {
        robotCtx.setToken(sp.get());
    }

    /**
     * /**
     * 获取一个访问的headers;
     *
     * @return headers ;
     */
    public Map<String, String> headers() {
        Map<String, String> headers = Maps.newHashMap();
        headers.put(Constants.STOKEN, robotCtx.getToken());
        return headers;
    }

    /**
     * 是否需要更新配置参数
     *
     * @return
     */
    public Boolean paramsUpdate() {
        if (params == null || params.size() < 1) {
            return false;
        }

        if (paramsCache == null || paramsCache.size() < 1 || paramsCache.size() != params.size()) {
            return true;
        }
        //比较配置是否被修改
        for (int i = 0; i < params.size(); i++) {
            RobotWeight robotWeight = params.get(i);
            RobotWeight robotWeightCache = paramsCache.get(i);
            if (!robotWeight.getCoinMain().equals(robotWeightCache.getCoinMain())) {
                return true;
            }
            if (!robotWeight.getCoinOther().equals(robotWeightCache.getCoinOther())) {
                return true;
            }
            if (!robotWeight.getVolumeRange().equals(robotWeightCache.getVolumeRange())) {
                return true;
            }
            if (!robotWeight.getPriceRange().equals(robotWeightCache.getPriceRange())) {
                return true;
            }
            if (!robotWeight.getTradeEnum().equals(robotWeightCache.getTradeEnum())) {
                return true;
            }
        }
        return false;
    }
}

