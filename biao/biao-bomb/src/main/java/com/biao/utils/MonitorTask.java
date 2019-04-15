package com.biao.utils;

import com.biao.constant.AutoTradeMonitorStatusEnum;
import com.biao.constant.Constants;
import com.biao.entity.MkAutoTradeMonitor;
import com.biao.mapper.MkAutoTradeMonitorDao;
import com.biao.spring.SpringBeanFactoryContext;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.Timer;
import java.util.TimerTask;

public class MonitorTask extends TimerTask {

    Logger logger = LoggerFactory.getLogger(MonitorTask.class);

    private Timer timer;
    private MkAutoTradeContext mkAutoTradeContext;
    private MkAutoTradeMonitorDao mkAutoTradeMonitorDao;
    private StringRedisTemplate stringRedisTemplate;


    public MonitorTask(Timer timer, MkAutoTradeContext mkAutoTradeContext, StringRedisTemplate stringRedisTemplate) {
        this.timer = timer;
        this.mkAutoTradeContext = mkAutoTradeContext;
        this.mkAutoTradeMonitorDao = (MkAutoTradeMonitorDao) SpringBeanFactoryContext.findBean(MkAutoTradeMonitorDao.class);
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public void run() {
        if (mkAutoTradeContext.getStatus().equals(AutoTradeMonitorStatusEnum.READY.getCode())) {
            logger.info("正常启动");
            mkAutoTradeContext.setStatus(AutoTradeMonitorStatusEnum.RUNNING.getCode());
            mkAutoTradeMonitorDao.updateMonitorBegin(mkAutoTradeContext.getMonitorId(), AutoTradeMonitorStatusEnum.RUNNING.getCode(), LocalDateTime.now(), "正常启动");
            stringRedisTemplate.opsForHash().put(Constants.MK_AUTO_TRADE_MONITOR, mkAutoTradeContext.getMonitorId(), mkAutoTradeContext.getStatus());

            //预防Redis-put之前boss把monitor终止
            MkAutoTradeMonitor endMonitor = mkAutoTradeMonitorDao.findEndMonitorById(mkAutoTradeContext.getMonitorId());
            if (!ObjectUtils.isEmpty(endMonitor)) {
                stringRedisTemplate.opsForHash().put(Constants.MK_AUTO_TRADE_MONITOR, mkAutoTradeContext.getMonitorId(), AutoTradeMonitorStatusEnum.END.getCode());
            }
            return;
        }

        if (mkAutoTradeContext.getEndDate().isBefore(LocalDateTime.now())) {
            logger.info("自然结束");
            mkAutoTradeContext.setStatus(AutoTradeMonitorStatusEnum.END.getCode());
            mkAutoTradeMonitorDao.updateMonitorEnd(mkAutoTradeContext.getMonitorId(), AutoTradeMonitorStatusEnum.END.getCode(), LocalDateTime.now(), "自然结束");
            stringRedisTemplate.delete(Constants.MK_AUTO_TRADE_MONITOR);
            timer.cancel();
            return;
        }

        if (mkAutoTradeContext.getStatus().equals(AutoTradeMonitorStatusEnum.RUNNING.getCode())) {
            logger.info("运行中");
            if (!stringRedisTemplate.hasKey(Constants.MK_AUTO_TRADE_MONITOR) || !stringRedisTemplate.opsForHash().hasKey(Constants.MK_AUTO_TRADE_MONITOR, mkAutoTradeContext.getMonitorId())) {
                MkAutoTradeMonitor mkAutoTradeMonitor = mkAutoTradeMonitorDao.findById(mkAutoTradeContext.getMonitorId());
                mkAutoTradeContext.setStatus(mkAutoTradeMonitor.getStatus());
                stringRedisTemplate.opsForHash().put(Constants.MK_AUTO_TRADE_MONITOR, mkAutoTradeContext.getMonitorId(), mkAutoTradeMonitor.getStatus());

                //预防Redis-put之前boss把monitor终止
                MkAutoTradeMonitor endMonitor = mkAutoTradeMonitorDao.findEndMonitorById(mkAutoTradeContext.getMonitorId());
                if (!ObjectUtils.isEmpty(endMonitor)) {
                    stringRedisTemplate.opsForHash().put(Constants.MK_AUTO_TRADE_MONITOR, mkAutoTradeContext.getMonitorId(), AutoTradeMonitorStatusEnum.END.getCode());
                }
            }

            String status = String.valueOf(stringRedisTemplate.opsForHash().get(Constants.MK_AUTO_TRADE_MONITOR, mkAutoTradeContext.getMonitorId()));
            if (StringUtils.isNotEmpty(status) && status.equals(AutoTradeMonitorStatusEnum.END.getCode())) {
                logger.info("系统中断结束");
                mkAutoTradeContext.setStatus(AutoTradeMonitorStatusEnum.END.getCode());
                mkAutoTradeMonitorDao.updateMonitorEnd(mkAutoTradeContext.getMonitorId(), AutoTradeMonitorStatusEnum.END.getCode(), LocalDateTime.now(), "系统中断结束");
                stringRedisTemplate.delete(Constants.MK_AUTO_TRADE_MONITOR);
                timer.cancel();
                return;
            }
        }
    }
}
