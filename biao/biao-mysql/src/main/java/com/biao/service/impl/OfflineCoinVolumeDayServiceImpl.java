package com.biao.service.impl;

import com.biao.mapper.OfflineCoinVolumeDayDao;
import com.biao.service.OfflineCoinVolumeDayService;
import com.biao.util.DateUtils;
import com.biao.vo.OfflineCoinVolumeDayVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service("offlineCoinVolumeDayService")
public class OfflineCoinVolumeDayServiceImpl implements OfflineCoinVolumeDayService {

    private static Logger logger = LoggerFactory.getLogger(OfflineCoinVolumeDayServiceImpl.class);

    @Autowired
    private OfflineCoinVolumeDayDao offlineCoinVolumeDayDao;

    @Override
    public Long batchInsertSelect(OfflineCoinVolumeDayVO coinVolumeDayVO) {
        String countDay = DateUtils.formaterDate(LocalDate.now());
        Long count = offlineCoinVolumeDayDao.selectByDay(countDay);
        if (count != null && count > 0) {
            logger.error("统计商户的对账时间date:{},该时段已经统计了，不能重复统计", countDay);
            return 0L;
        }
        return offlineCoinVolumeDayDao.batchInsertSelect(coinVolumeDayVO.getSymbols(), coinVolumeDayVO.getStartTime(), coinVolumeDayVO.getEndTime());
    }

}
