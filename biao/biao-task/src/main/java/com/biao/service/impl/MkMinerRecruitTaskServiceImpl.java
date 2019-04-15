package com.biao.service.impl;

import com.biao.entity.MkMinerRecruit;
import com.biao.entity.PlatUser;
import com.biao.entity.mk2.Mk2PopularizeMiningGiveCoinLog;
import com.biao.mapper.MkMinerRecruitDao;
import com.biao.mapper.PlatUserDao;
import com.biao.mapper.mk2.Mk2PopularizeMiningGiveCoinLogDao;
import com.biao.service.MkMinerRecruitTaskService;
import com.biao.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MkMinerRecruitTaskServiceImpl implements MkMinerRecruitTaskService {

    private static Logger logger = LoggerFactory.getLogger(MkMinerRecruitTaskServiceImpl.class);

    private static String MINER_RECRUIT_END_TIME = "2018-11-01 23:59:59";

    @Autowired
    private MkMinerRecruitDao mkMinerRecruitDao;

    @Autowired
    private Mk2PopularizeMiningGiveCoinLogDao mk2PopularizeMiningGiveCoinLogDao;

    @Autowired
    private PlatUserDao platUserDao;


    @Override
    @Transactional
    public void reachStandardTaskEntry() {


        LocalDateTime endDateTime = LocalDateTime.now();
        try {
            LocalDateTime actEndDateTime = DateUtils.parseLocalDateTime(MINER_RECRUIT_END_TIME);
            if (endDateTime.isAfter(actEndDateTime)) {
                endDateTime = actEndDateTime;
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            return;
        }

        List<MkMinerRecruit> mkMinerRecruitList = mkMinerRecruitDao.findUnStandardList();
        if (CollectionUtils.isEmpty(mkMinerRecruitList)) {
            logger.info("不存在未达标的矿主！");
            return;
        }

        final LocalDateTime curEndDateTime = endDateTime;
        mkMinerRecruitList.forEach(mkMinerRecruit -> {

            String isStandard = "1";
            //1,报名第二天有持币挖矿的数据
            Mk2PopularizeMiningGiveCoinLog mk2PopularizeMiningGiveCoinLog;
            if ("0".equals(mkMinerRecruit.getIsStandard())) {
                mk2PopularizeMiningGiveCoinLog = mk2PopularizeMiningGiveCoinLogDao.findByDate(mkMinerRecruit.getCreateDate().plusDays(1), mkMinerRecruit.getUserId());
                if (ObjectUtils.isEmpty(mk2PopularizeMiningGiveCoinLog)) {
                    logger.info(String.format("矿主[%s]报名第二天未有持币挖矿的数据", mkMinerRecruit.getUserId()));
                    isStandard = "0";
                }
            }

            //2,以报名时间开始，直推人数超过30人；
            List<PlatUser> platUserList = platUserList = platUserDao.findByReferIdAndDate(mkMinerRecruit.getUserId(), mkMinerRecruit.getCreateDate(), curEndDateTime);
            if (CollectionUtils.isEmpty(platUserList) || platUserList.size() < 30) {
                logger.info(String.format("矿主[%s]直推人数未超过30人", mkMinerRecruit.getUserId()));
            }


            //3,直推人数超过30人中每个人持币挖矿达到或超过20天
            int number = 0;
            if (ObjectUtils.isEmpty(mkMinerRecruit.getReachNumber()) || mkMinerRecruit.getReachNumber() < 30) {
                if (!CollectionUtils.isEmpty(platUserList)) {
                    for (PlatUser platUser : platUserList) {
                        long count = mk2PopularizeMiningGiveCoinLogDao.countByUserId(platUser.getId(), curEndDateTime);
                        if (count >= 20) {
                            number += 1;
                        }
                    }
                }
            }

            String flag = "0".equals(isStandard) ? "无" : "有";
            Integer size = CollectionUtils.isEmpty(platUserList) ? 0 : platUserList.size();
            mkMinerRecruit.setIsStandard(isStandard);
            mkMinerRecruit.setInviteNumber(size);
            mkMinerRecruit.setReachNumber(number);
            mkMinerRecruit.setRemark(String.format("第二天持币挖矿的数据[%s],直推人数[%s],直推持币挖矿达到或超过20天人数[%s]", flag, String.valueOf(size), String.valueOf(number)));
            logger.info(String.format("第二天持币挖矿的数据[%s],直推人数[%s],直推持币挖矿达到或超过20天人数[%s]", flag, String.valueOf(size), String.valueOf(number)));
            mkMinerRecruitDao.update(mkMinerRecruit);

        });

    }
}
