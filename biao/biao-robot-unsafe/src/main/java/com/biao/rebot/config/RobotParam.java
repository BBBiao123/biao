package com.biao.rebot.config;

import com.biao.enums.TradeEnum;
import com.biao.rebot.SymbolInfo;
import com.biao.rebot.common.Constants;
import com.biao.rebot.common.NameThreadFactory;
import com.biao.rebot.dao.RobotSupper;
import com.biao.rebot.service.async.AsyncNotify;
import com.google.common.base.Joiner;
import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

/**
 * 机器人配置参数；
 *
 *
 */
public class RobotParam {

    private final Logger logger = LoggerFactory.getLogger(RobotParam.class);
    /**
     * 需要机器人帮你交易的交易对信息；
     */
    private Map<String, RobotWeight> params = Maps.newConcurrentMap();

    /**
     * token设置.
     */
    private Map<String, String> tokens = Maps.newConcurrentMap();

    /**
     * 保存所有的url信息；
     */
    private Map<String, String> requestUrls = Maps.newHashMap();

    /**
     * ctx信息；
     */
    private RobotCtx robotCtx;

    /**
     * 代理.
     */
    private Proxy proxy;

    /**
     * 接收异步消息的处理.
     */
    private final Set<AsyncNotify> notify = Sets.newConcurrentHashSet();

    /**
     * 获取当前的代理设置.
     *
     * @return proxy. proxy
     */
    public Proxy proxy() {
        return proxy;
    }

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
     * Complete init.
     *
     * @param param the param
     */
    public void completeInit(RobotWeight param) {
        String symbol = joinSymbol(param.getCoinMain(), param.getCoinOther());
        param.setInit(false);
        params.put(symbol, param);
        RobotSupper.get().gerRobotConfig().updateStatus(false);
    }

    /**
     * 对象持有；
     */
    private static final class RobotParamHolder {
        /**
         * The Param.
         */
        static final RobotParam PARAM = new RobotParam();
    }

    /**
     * 初始化；
     */
    public void init() {
        /*
         * 初始化一些配置信息；
         */
        applyConfig();
        //初始化机器人配置信息
        initRobotWeight(true);
        //启动一个配置线程处理
        @SuppressWarnings("all")
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1, new NameThreadFactory("robot_config_load"));
        executorService.scheduleAtFixedRate(() -> initRobotWeight(false), 60, 60, TimeUnit.SECONDS);
        logger.info("初始化配置信息成功！");
    }

    /**
     * 设置一个配置变化处理信息.
     *
     * @param configChange the config change
     */
    public void addConfigChange(AsyncNotify configChange) {
        this.notify.add(configChange);
    }

    /**
     * 配置信息处理；
     */
    private void applyConfig() {
        requestUrls.put(Constants.ORDER_NO_URL, Joiner.on("/").join(robotCtx.getUrl(), Constants.ORDER_NO_URL));
        requestUrls.put(Constants.SELL_URL, Joiner.on("/").join(robotCtx.getUrl(), Constants.SELL_URL));
        requestUrls.put(Constants.BUY_URL, Joiner.on("/").join(robotCtx.getUrl(), Constants.BUY_URL));
        requestUrls.put(Constants.LOGIN, Joiner.on("/").join(robotCtx.getUrl(), Constants.LOGIN));
        requestUrls.put(Constants.CANCEL_URL, Joiner.on("/").join(robotCtx.getUrl(), Constants.CANCEL_URL));
        //校验相关配置信息；
        checkNotNull(robotCtx.getJdbcPass(), "params 【jdbcPass】 is null");
        checkNotNull(robotCtx.getJdbcUrl(), "params 【jdbcUrl】 is null");
        checkNotNull(robotCtx.getJdbcUser(), "params 【JdbcUser】 is null");
        checkNotNull(robotCtx.getUrl(), "params 【Url】 is null");
        //设置代理.
        if (robotCtx.getProxy() != null) {
            String address = robotCtx.getProxy().getAddress();
            Integer port = robotCtx.getProxy().getPort();
            if (StringUtils.isNoneBlank(address) && port != null) {
                this.proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(address, port));
            }
        }

    }

    private void initRobotWeight(boolean init) {
        List<RobotWeight> collect = RobotSupper.get().gerRobotConfig().queryAll().stream().filter(e ->
                StringUtils.isNoneBlank(e.getLogin().getUserId()) &&
                        StringUtils.isNoneBlank(e.getLogin().getUserName()) &&
                        StringUtils.isNoneBlank(e.getLogin().getPassword())).collect(toList());
        if (!init) {
            configChange(collect);
        } else {
            if (!collect.isEmpty()) {
                initSymbol(collect);
            }

        }
    }

    private void initSymbol(List<RobotWeight> collect) {
        //初始化交易对信息
        Map<String, SymbolInfo> symbolInfoMap = RobotSupper.get().getSymbolConfig().queryBySymbol();
        collect.forEach(e -> {
            String s = joinSymbol(e.getCoinMain(), e.getCoinOther());
            SymbolInfo symbolInfo = symbolInfoMap.get(s);
            e.setSymbolInfo(symbolInfo);
            String symbolf = joinSymbolByType(e);
            params.put(symbolf, e);
        });
    }

    /**
     * Symbolf string.
     *
     * @param weight the weight
     * @return the string
     */
    public static String joinSymbolByType(RobotWeight weight) {
        return joinSymbolByType(weight.getCoinMain(), weight.getCoinOther(), weight.getTradeEnum());
    }

    private void configChange(List<RobotWeight> newList) {
        try {
            Map<String, RobotWeight> collect = newList.stream().collect(toMap(RobotParam::joinSymbolByType, e -> e));
            MapDifference<String, RobotWeight> difference = Maps.difference(collect, params);
            //移除多的.
            Map<String, RobotWeight> rightMap = difference.entriesOnlyOnRight();
            Map<String, RobotWeight> leftMap = difference.entriesOnlyOnLeft();
            long count = newList.stream().filter(RobotWeight::getInit).count();
            boolean eq = rightMap.isEmpty() && leftMap.isEmpty() && count <= 0;
            if (!eq && !notify.isEmpty()) {
                rightMap.forEach((k, v) -> params.remove(k));
                initSymbol(newList);
                notify.forEach(AsyncNotify::reset);
            }
        } catch (Exception ex) {
            logger.error("reset error {}", ex);
        }
    }

    /**
     * 获取一个对象；
     *
     * @return this ;
     */
    public static RobotParam get() {
        return RobotParamHolder.PARAM;
    }

    /**
     * 获取一个可以交易对配置信息；
     *
     * @return 交易对 ；
     */
    public Collection<RobotWeight> getParams() {
        return Maps.newHashMap(params).values();
    }

    /**
     * 获取一个可以交易对配置信息；
     *
     * @return 交易对 ；
     */
    public Map<String, RobotWeight> getParamsMap() {
        return Maps.newHashMap(params);
    }

    /**
     * 获取一个可以交易对配置信息；
     *
     * @param symbol the joinSymbol
     * @return 交易对 ；
     */
    public RobotWeight getRobotWeight(String symbol) {
        return params.get(symbol);
    }

    /**
     * Symbol string.
     *
     * @param coinMain  the coin main
     * @param coinOther the coin other
     * @return the string
     */
    public static String joinSymbol(String coinMain, String coinOther) {
        return coinOther + coinMain;
    }

    /**
     * Symbol string.
     *
     * @param coinMain  the coin main
     * @param coinOther the coin other
     * @return the string
     */
    public static String joinSymbolByType(String coinMain, String coinOther, TradeEnum tradeEnum) {
        return joinSymbol(coinMain, coinOther) + "_" + tradeEnum.name();
    }

    /**
     * Gets users.
     *
     * @return the users
     */
    public List<Login> getUsers() {
        return getParams().stream().map(RobotWeight::getLogin).distinct().collect(Collectors.toList());
    }

    /**
     * Has user boolean.
     *
     * @param userId the user id
     * @return the boolean
     */
    public boolean hasUser(String userId) {
        return tokens.containsKey(userId);
    }

    /**
     * 获取 访问的url;
     *
     * @param key the key
     * @return key ;
     * @see Constants
     */
    public String getRequestUrl(String key) {
        return requestUrls.get(key);
    }

    /**
     * 刷新token;
     *
     * @param sp 实现；
     */
    public void refreshedToken(Supplier<Map<String, String>> sp) {
        tokens.putAll(sp.get());
    }

    /**
     * 刷新token;
     *
     * @param userId the user id
     * @param token  the token
     */
    private void refreshedToken(String userId, String token) {
        tokens.put(userId, token);
    }

    /**
     * /**
     * 获取一个访问的headers;
     *
     * @param symbol the joinSymbol
     * @param login  the login 登录接口.
     * @return headers ;
     */
    public Map<String, String> headers(String symbol, Supplier<String> login) {
        Map<String, String> headers = Maps.newHashMap();
        RobotWeight robotWeight = params.get(symbol);
        if (robotWeight == null) {
            return Collections.emptyMap();
        }
        String userId = robotWeight.getLogin().getUserId();
        String s = tokens.get(userId);
        if (StringUtils.isBlank(s)) {
            refreshedToken(userId, login.get());
            s = tokens.get(userId);
        }
        headers.put(Constants.STOKEN, s);
        return headers;
    }
}

