package com.biao.service.impl;

import com.biao.constant.Constants;
import com.biao.entity.mk2.Mk2PopularizeMiningTeamConf;
import com.biao.exception.PlatException;
import com.biao.mapper.mk2.Mk2PopularizeMiningTeamSortDao;
import com.biao.service.Mk2MiningTeamSortTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

@Service
public class Mk2MiningTeamSortTaskServiceImpl implements Mk2MiningTeamSortTaskService {

    private static final Logger LOGGER = LoggerFactory.getLogger(Mk2MiningTeamSortTaskServiceImpl.class);

    @Autowired
    private Mk2PopularizeMiningTeamSortDao mk2PopularizeMiningTeamSortDao;

    @Override
    @Transactional
    public void doSortTeamMinging() {
        LOGGER.info("争霸开始0...");
        LocalDateTime sortDate = LocalDateTime.of(LocalDateTime.now().toLocalDate(), LocalTime.MIN);// 当天统计团队挖矿排名时间
        Mk2PopularizeMiningTeamConf conf = mk2PopularizeMiningTeamSortDao.findConf();
        if (Objects.isNull(conf)) {
            LOGGER.error("百日争霸参数未配置");
            throw new PlatException(Constants.OPERRATION_ERROR, "百日争霸参数未配置！");
        }
        LOGGER.info("争霸开始1...");
        mk2PopularizeMiningTeamSortDao.deleteTeamSort(); // 清除历史统计排名
        LOGGER.info("争霸开始2...");
        mk2PopularizeMiningTeamSortDao.sortTeamMingingTemp(sortDate, conf.getSortBeginDate(), conf.getSortEndDate()); // 团队挖矿排名入库temp,排名无间断，create_by =temp
        LOGGER.info("争霸开始3...");
        mk2PopularizeMiningTeamSortDao.sortTeamMinging(); // 排名间断，eg: 1,2,2,4
        LOGGER.info("争霸开始4...");
        mk2PopularizeMiningTeamSortDao.sortTeamMingingList();// 去上一步前100个，为批量排名展示
        LOGGER.info("争霸开始5...");
        mk2PopularizeMiningTeamSortDao.deleteTeamTempSort(); // 删除团队挖矿排名入库temp,排名无间断，create_by =temp
    }
}
