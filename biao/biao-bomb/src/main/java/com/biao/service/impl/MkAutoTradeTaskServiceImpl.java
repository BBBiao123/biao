package com.biao.service.impl;

import com.biao.constant.AutoTradeMonitorStatusEnum;
import com.biao.constant.AutoTradeSettingStatusEnum;
import com.biao.constant.Constants;
import com.biao.constant.TimeUnitEnum;
import com.biao.entity.MkAutoTradeMonitor;
import com.biao.entity.MkAutoTradeSetting;
import com.biao.mapper.MkAutoTradeMonitorDao;
import com.biao.mapper.MkAutoTradeSettingDao;
import com.biao.service.MkAutoTradeTaskService;
import com.biao.util.RsaUtils;
import com.biao.utils.MkAutoTradeContext;
import com.biao.utils.MonitorTask;
import com.biao.utils.PlatApiService;
import com.biao.utils.TradeTask;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.UUID;

import static com.biao.util.RsaUtils.DEFAULT_PUBLIC_KEY;

@Service
public class MkAutoTradeTaskServiceImpl implements MkAutoTradeTaskService {

    Logger logger = LoggerFactory.getLogger(MkAutoTradeTaskServiceImpl.class);

    @Autowired
    private MkAutoTradeSettingDao mkAutoTradeSettingDao;

    @Autowired
    private MkAutoTradeMonitorDao mkAutoTradeMonitorDao;

    @Autowired
    private PlatApiService platApiService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void triggerAutoTradeTask() {

        this.handleActiveMonitor();

        this.initAutoTrade();

    }

    @Override
    public void clearMonitorRedis() {
        if (stringRedisTemplate.hasKey(Constants.MK_AUTO_TRADE_MONITOR)) {
            stringRedisTemplate.delete(Constants.MK_AUTO_TRADE_MONITOR);
        }
    }

    public void handleActiveMonitor() {

        List<MkAutoTradeMonitor> mkAutoTradeMonitors = mkAutoTradeMonitorDao.findActiveMonitor();
        if (CollectionUtils.isEmpty(mkAutoTradeMonitors)) {
            logger.info("无活动中的记录");
            return;
        }

        mkAutoTradeMonitors.forEach(mkAutoTradeMonitor -> {
            if (mkAutoTradeMonitor.getEndDate().isBefore(LocalDateTime.now())) {
                mkAutoTradeMonitorDao.updateMonitorEnd(mkAutoTradeMonitor.getId(), AutoTradeMonitorStatusEnum.END.getCode(), LocalDateTime.now(), "系统异常结束！");
            } else {
                MkAutoTradeSetting mkAutoTradeSetting = mkAutoTradeSettingDao.findById(mkAutoTradeMonitor.getSettingId());
                MkAutoTradeContext mkAutoTradeContext = this.initAutoTradeContext(mkAutoTradeSetting);
                mkAutoTradeContext.setMonitorId(mkAutoTradeMonitor.getId());
                mkAutoTradeContext.setOrderVolume(mkAutoTradeMonitor.getOrderVolume());
                mkAutoTradeContext.setOrderPrice(mkAutoTradeContext.getOrderPrice());
                mkAutoTradeContext.setOrderNumber(mkAutoTradeMonitor.getOrderNumber());
                mkAutoTradeContext.setStatus(mkAutoTradeMonitor.getStatus());
                mkAutoTradeContext.setBeginDate(mkAutoTradeMonitor.getBeginDate());

                this.login(mkAutoTradeContext); //用户登录
                this.createAutoTradeTimer(mkAutoTradeContext); //创建Timer
            }
        });
    }

    public void initAutoTrade() {

        //提前五分钟装载
        LocalDateTime curDateTime = LocalDateTime.now();
        LocalDateTime predictDateTime = curDateTime.minusMinutes(-5);
        List<MkAutoTradeSetting> mkAutoTradeSettings = mkAutoTradeSettingDao.findLoadingAutoTrade(predictDateTime, curDateTime);

        if (CollectionUtils.isEmpty(mkAutoTradeSettings)) {
            logger.info("无可加载记录！");
            return;
        }

        //装载
        mkAutoTradeSettings.forEach(mkAutoTradeSetting -> {
            this.loadAutoTradeSetting(mkAutoTradeSetting);
        });
    }

    private void loadAutoTradeSetting(MkAutoTradeSetting mkAutoTradeSetting) {

        MkAutoTradeContext mkAutoTradeContext = this.initAutoTradeContext(mkAutoTradeSetting);

        MkAutoTradeMonitor mkAutoTradeMonitor = this.createAutoTradeMonitor(mkAutoTradeSetting);
        mkAutoTradeContext.setMonitorId(mkAutoTradeMonitor.getId());

        this.login(mkAutoTradeContext); //用户登录
        this.createAutoTradeTimer(mkAutoTradeContext); //创建Timer

    }

    private MkAutoTradeContext initAutoTradeContext(MkAutoTradeSetting mkAutoTradeSetting) {
        MkAutoTradeContext mkAutoTradeContext = new MkAutoTradeContext();
        mkAutoTradeContext.setSettingId(mkAutoTradeSetting.getId());
        mkAutoTradeContext.setType(mkAutoTradeSetting.getType());
        mkAutoTradeContext.setStatus(AutoTradeMonitorStatusEnum.READY.getCode());
        mkAutoTradeContext.setCoinMainSymbol(mkAutoTradeSetting.getCoinMainSymbol());
        mkAutoTradeContext.setCoinOtherSymbol(mkAutoTradeSetting.getCoinOtherSymbol());
        mkAutoTradeContext.setBeginDate(mkAutoTradeSetting.getBeginDate());
        mkAutoTradeContext.setEndDate(mkAutoTradeSetting.getEndDate());
        mkAutoTradeContext.setUserId(mkAutoTradeSetting.getUsername());
        mkAutoTradeContext.setUsername(mkAutoTradeSetting.getUsername());
        mkAutoTradeContext.setPassword(RsaUtils.encryptByPublicKey(mkAutoTradeSetting.getPass(), DEFAULT_PUBLIC_KEY));
        mkAutoTradeContext.setMail(mkAutoTradeSetting.getMail());
        mkAutoTradeContext.setMobile(mkAutoTradeSetting.getMobile());
        mkAutoTradeContext.setMinVolume(mkAutoTradeSetting.getMinVolume());
        mkAutoTradeContext.setMaxVolume(mkAutoTradeSetting.getMaxVolume());
        mkAutoTradeContext.setMinPrice(mkAutoTradeSetting.getMinPrice());
        mkAutoTradeContext.setMaxPrice(mkAutoTradeSetting.getMaxPrice());
        mkAutoTradeContext.setPricePrecision(mkAutoTradeSetting.getPricePrecision());
        mkAutoTradeContext.setVolumePrecision(mkAutoTradeSetting.getVolumePrecision());
        mkAutoTradeContext.setFrequency(mkAutoTradeSetting.getFrequency());
        mkAutoTradeContext.setTimeUnit(mkAutoTradeSetting.getTimeUnit());
        mkAutoTradeContext.setOrderNumber(0);
        mkAutoTradeContext.setOrderVolume(BigDecimal.ZERO);
        mkAutoTradeContext.setOrderPrice(BigDecimal.ZERO);
        mkAutoTradeContext.setCreateBy(mkAutoTradeSetting.getCreateBy());
        mkAutoTradeContext.setCreateByName(mkAutoTradeSetting.getCreateByName());
        mkAutoTradeContext.setToken("");
        return mkAutoTradeContext;
    }

    private MkAutoTradeMonitor createAutoTradeMonitor(MkAutoTradeSetting mkAutoTradeSetting) {
        MkAutoTradeMonitor mkAutoTradeMonitor = new MkAutoTradeMonitor();
        mkAutoTradeMonitor.setId(UUID.randomUUID().toString().replace("-", ""));
        mkAutoTradeMonitor.setSettingId(mkAutoTradeSetting.getId());
        mkAutoTradeMonitor.setType(mkAutoTradeSetting.getType());
        mkAutoTradeMonitor.setStatus(AutoTradeMonitorStatusEnum.READY.getCode());
        mkAutoTradeMonitor.setCoinMainSymbol(mkAutoTradeSetting.getCoinMainSymbol());
        mkAutoTradeMonitor.setCoinOtherSymbol(mkAutoTradeSetting.getCoinOtherSymbol());
        mkAutoTradeMonitor.setBeginDate(mkAutoTradeSetting.getBeginDate());
        mkAutoTradeMonitor.setEndDate(mkAutoTradeSetting.getEndDate());
        mkAutoTradeMonitor.setUserId(mkAutoTradeSetting.getUserId());
        mkAutoTradeMonitor.setUsername(mkAutoTradeSetting.getUsername());
        mkAutoTradeMonitor.setMail(mkAutoTradeSetting.getMail());
        mkAutoTradeMonitor.setMobile(mkAutoTradeSetting.getMobile());
        mkAutoTradeMonitor.setMinVolume(mkAutoTradeSetting.getMinVolume());
        mkAutoTradeMonitor.setMaxVolume(mkAutoTradeSetting.getMaxVolume());
        mkAutoTradeMonitor.setMinPrice(mkAutoTradeSetting.getMinPrice());
        mkAutoTradeMonitor.setMaxPrice(mkAutoTradeSetting.getMaxPrice());
        mkAutoTradeMonitor.setFrequency(mkAutoTradeSetting.getFrequency());
        mkAutoTradeMonitor.setTimeUnit(mkAutoTradeSetting.getTimeUnit());
        mkAutoTradeMonitor.setOrderNumber(0);
        mkAutoTradeMonitor.setOrderVolume(BigDecimal.ZERO);
        mkAutoTradeMonitor.setOrderPrice(BigDecimal.ZERO);
        mkAutoTradeMonitor.setCreateBy(mkAutoTradeSetting.getCreateBy());
        mkAutoTradeMonitor.setCreateByName(mkAutoTradeSetting.getCreateByName());
        mkAutoTradeMonitorDao.insert(mkAutoTradeMonitor);
        return mkAutoTradeMonitor;
    }

    private void login(MkAutoTradeContext mkAutoTradeContext) {
        try {
            mkAutoTradeContext.setToken(platApiService.login(mkAutoTradeContext.getUsername(), mkAutoTradeContext.getPassword()));
        } catch (Exception e) {
            logger.error("登录失败。。。" + e);
            mkAutoTradeMonitorDao.updateMonitorEnd(mkAutoTradeContext.getMonitorId(), AutoTradeMonitorStatusEnum.END.getCode(), null, "登录失败！");
            mkAutoTradeSettingDao.updateSettingStatus(mkAutoTradeContext.getSettingId(), AutoTradeSettingStatusEnum.FORBIDDEN.getCode());
            return;
        }

    }

    private void createAutoTradeTimer(MkAutoTradeContext mkAutoTradeContext) {

        //校验登录Token
        if (StringUtils.isEmpty(mkAutoTradeContext.getToken())) {
            return;
        }

        Timer timer = new Timer();
        this.createMonitorTask(timer, mkAutoTradeContext); //创建监控任务
        this.createTradeTask(timer, mkAutoTradeContext); //创建下单任务
    }

    private void createTradeTask(Timer timer, MkAutoTradeContext mkAutoTradeContext) {

        TradeTask tradeTask = new TradeTask(mkAutoTradeContext);
        long period = 1000L;

        if (TimeUnitEnum.HOUR.getCode().equals(mkAutoTradeContext.getTimeUnit())) {
            period = mkAutoTradeContext.getFrequency() * 60 * 60 * period;
        } else if (TimeUnitEnum.MINUTE.getCode().equals(mkAutoTradeContext.getTimeUnit())) {
            period = mkAutoTradeContext.getFrequency() * 60 * period;
        } else {
            period = mkAutoTradeContext.getFrequency() * period;
        }
        timer.schedule(tradeTask, this.toDate(mkAutoTradeContext.getBeginDate()), period);
    }

    private void createMonitorTask(Timer timer, MkAutoTradeContext mkAutoTradeContext) {
        MonitorTask monitorTask = new MonitorTask(timer, mkAutoTradeContext, stringRedisTemplate);
        stringRedisTemplate.opsForHash().put(Constants.MK_AUTO_TRADE_MONITOR, mkAutoTradeContext.getMonitorId(), mkAutoTradeContext.getStatus());
        timer.schedule(monitorTask, this.toDate(mkAutoTradeContext.getBeginDate()), 1000);
    }

    private Date toDate(LocalDateTime dateTime) {
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = dateTime.atZone(zoneId);
        return Date.from(zdt.toInstant());
    }
}
