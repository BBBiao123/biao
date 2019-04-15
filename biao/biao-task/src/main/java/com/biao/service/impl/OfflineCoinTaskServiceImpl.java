package com.biao.service.impl;

import com.biao.constant.Constants;
import com.biao.entity.OfflineCoin;
import com.biao.entity.OfflineCoinTaskRecord;
import com.biao.exception.PlatException;
import com.biao.mapper.OfflineCoinDao;
import com.biao.mapper.OfflineCoinTaskRecordDao;
import com.biao.service.OfflineCoinTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class OfflineCoinTaskServiceImpl implements OfflineCoinTaskService {

    private static Logger logger = LoggerFactory.getLogger(OfflineCoinTaskServiceImpl.class);
    private String OFF_LINE_COIN = "UES";

    @Autowired
    private OfflineCoinDao offlineCoinDao;

    @Autowired
    private OfflineCoinTaskRecordDao offlineCoinTaskRecordDao;


    @Override
    @Transactional
    public void updateOfflineCoinTaskEntry() {

        LocalDateTime curDateTime = LocalDateTime.now();

        OfflineCoinTaskRecord record = offlineCoinTaskRecordDao.findByCreateDate(curDateTime);
        if (!ObjectUtils.isEmpty(record)) {
            logger.info("当天已执行成功！");
            return;
        }

        OfflineCoin offlineCoin = offlineCoinDao.findBySymbol(OFF_LINE_COIN);
        if (ObjectUtils.isEmpty(offlineCoin)) {
            logger.info(String.format("C2C币种[%s]不存在！", OFF_LINE_COIN));
            return;
        }

        if (ObjectUtils.isEmpty(offlineCoin.getDayIncPrice())) {
            logger.info(String.format("C2C币种[%s]每日价格增量为空！", OFF_LINE_COIN));
            return;
        }

        OfflineCoinTaskRecord offlineCoinTaskRecord = new OfflineCoinTaskRecord();
        offlineCoinTaskRecord.setId(UUID.randomUUID().toString().replace("-", ""));
        offlineCoinTaskRecord.setCoinId(offlineCoin.getCoinId());
        offlineCoinTaskRecord.setSymbol(offlineCoin.getSymbol());
        offlineCoinTaskRecord.setBeforeMaxPrice(offlineCoin.getMaxPrice());
        offlineCoinTaskRecord.setBeforeMinPrice(offlineCoin.getMinPrice());
        offlineCoinTaskRecord.setDayIncPrice(offlineCoin.getDayIncPrice());
        offlineCoinTaskRecord.setCreateBy("task");

        try {
            offlineCoin.setMaxPrice(offlineCoin.getMaxPrice().add(offlineCoin.getDayIncPrice()));
            //            offlineCoin.setMinPrice(offlineCoin.getMinPrice().add(offlineCoin.getDayIncPrice()));
            if (offlineCoin.getMaxPrice().compareTo(offlineCoin.getMinPrice()) < 0) {
                logger.info("最大价格不能小于最小价格！");
                throw new PlatException(Constants.OPERRATION_ERROR, "最大价格不能小于最小价格！");
            }

            if (offlineCoin.getMaxPrice().compareTo(BigDecimal.ZERO) <= 0) {
                logger.info("最大价格不能小于零！");
                throw new PlatException(Constants.OPERRATION_ERROR, "最大价格不能小于等于零！");
            }

            offlineCoinDao.update(offlineCoin);
            offlineCoinTaskRecord.setStatus("1");
            offlineCoinTaskRecord.setRemark(String.format("更新后最大价格为[%s]", offlineCoin.getMaxPrice()));
        } catch (PlatException pe) {
            String remark = pe.getMsg().substring(0, pe.getMsg().length() > 300 ? 300 : pe.getMsg().length());
            offlineCoinTaskRecord.setStatus("0");
            offlineCoinTaskRecord.setRemark(pe.getMsg());
        } catch (Exception e) {
            String remark = e.getMessage().substring(0, e.getMessage().length() > 300 ? 300 : e.getMessage().length());
            offlineCoinTaskRecord.setStatus("0");
            offlineCoinTaskRecord.setRemark(remark);
        }

        offlineCoinTaskRecordDao.insert(offlineCoinTaskRecord);
    }
}
