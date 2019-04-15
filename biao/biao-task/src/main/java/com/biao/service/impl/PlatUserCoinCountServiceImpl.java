package com.biao.service.impl;

import com.biao.entity.PlatUserCoinCount;
import com.biao.mapper.PlatUserCoinCountDao;
import com.biao.service.PlatUserCoinCountService;
import com.biao.util.SnowFlake;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class PlatUserCoinCountServiceImpl implements PlatUserCoinCountService {

    @Autowired
    private PlatUserCoinCountDao platUserCoinCountDao;

    /**
     * 统计当天用户持币总量
     */
    @Override
    public void countUserCoin() {

        LocalDateTime countDate = LocalDateTime.of(LocalDateTime.now().toLocalDate(), LocalTime.MIN);// 当天日期
        List<PlatUserCoinCount> userCoinCounts = platUserCoinCountDao.countByType();
        if (CollectionUtils.isNotEmpty(userCoinCounts)) {
            userCoinCounts.forEach(count -> {
                count.setId(SnowFlake.createSnowFlake().nextIdString());
                count.setCountDate(countDate);
            });
            platUserCoinCountDao.insertBatch(userCoinCounts);
        }
    }
}
