package com.biao.service.impl;

import com.biao.constant.Constants;
import com.biao.entity.PlatUser;
import com.biao.entity.UserCoinVolume;
import com.biao.entity.UserCoinVolumeHistory;
import com.biao.entity.relay.*;
import com.biao.enums.UserCoinVolumeEventEnum;
import com.biao.exception.PlatException;
import com.biao.mapper.PlatUserDao;
import com.biao.mapper.UserCoinVolumeDao;
import com.biao.mapper.UserCoinVolumeHistoryDao;
import com.biao.mapper.relay.*;
import com.biao.pojo.UserCoinVolumeBillDTO;
import com.biao.redis.RedisCacheManager;
import com.biao.service.MkRelayPrizeTaskService;
import com.biao.service.UserCoinVolumeBillService;
import com.biao.util.DateUtils;
import com.biao.util.SnowFlake;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
public class MkRelayPrizeTaskServiceImpl implements MkRelayPrizeTaskService {

    private static Logger logger = LoggerFactory.getLogger(MkRelayPrizeTaskServiceImpl.class);

    @Value("${relayPrizeTime:20}")
    private long RELAY_PRIZE_TIME;

    @Autowired
    private MkRelayPrizeConfigDao mkRelayPrizeConfigDao;

    @Autowired
    private MkRelayPrizeElectorDao mkRelayPrizeElectorDao;

    @Autowired
    private MkRelayTaskRecordDao mkRelayTaskRecordDao;

    @Autowired
    private MkRelayPrizeCandidateDao mkRelayPrizeCandidateDao;

    @Autowired
    private MkRelayRemitLogDao mkRelayRemitLogDao;

    @Autowired
    private PlatUserDao platUserDao;

    @Autowired
    private UserCoinVolumeBillService userCoinVolumeBillService;

//    @Autowired
//    private RedisCacheManager redisCacheManager;

    @Autowired
    private MkRelayPrizeTaskServiceImpl mkRelayPrizeTaskService;

    @Autowired
    private MkRelayAutoRecordDao mkRelayAutoRecordDao;

    @Autowired
    private MkRelayAutoConfigDao mkRelayAutoConfigDao;

    @Autowired
    private UserCoinVolumeDao userCoinVolumeDao;

    @Autowired
    private UserCoinVolumeHistoryDao userCoinVolumeHistoryDao;

    @Override
    public void relayPrizeTaskEntry() {

        logger.info("start relayPrizeTaskEntry......");

        //获取启用的接力撞奖规则
        LocalDateTime curDateTime = LocalDateTime.now();
        MkRelayPrizeConfig mkRelayPrizeConfig = mkRelayPrizeConfigDao.findActiveOne();
        MkRelayTaskRecord mkRelayTaskRecord = mkRelayTaskRecordDao.findLastOneByDate(curDateTime);

        if (!checkConfig(mkRelayPrizeConfig, curDateTime, mkRelayTaskRecord)) {
            return;
        }

        try {
            mkRelayPrizeTaskService.execRelayPrizeCore(mkRelayPrizeConfig, curDateTime);
        } catch (PlatException pe) {
            logger.error("接力出现业务异常：", pe);
            this.createTaskRecord(mkRelayPrizeConfig, pe.getMsg(), curDateTime);
        } catch (Exception e) {
            logger.info("系统异常：", e);
            this.createTaskRecord(mkRelayPrizeConfig, e.getMessage(), curDateTime);
        }

        logger.info("end relayPrizeTaskEntry......");
    }

    private boolean checkConfig(MkRelayPrizeConfig mkRelayPrizeConfig, LocalDateTime curDateTime, MkRelayTaskRecord mkRelayTaskRecord) {

        if (ObjectUtils.isEmpty(mkRelayPrizeConfig)) {
            logger.info(String.format("[%s]没有可用的接力撞奖规则！", curDateTime.toLocalDate().toString()));
            return false;
        }

        logger.info(String.format("开始时间[%s],结束时间[%s],奖金总量[%s],起步奖金[%s],新增奖金[%s],币种[%s],已发放数量[%s],当前奖池总量[%s],是否打款[%s],最低参与数量[%s],获奖时间间隔[%s]分钟",
                mkRelayPrizeConfig.getBeginTime(), mkRelayPrizeConfig.getEndTime(), String.valueOf(mkRelayPrizeConfig.getVolume()), String.valueOf(mkRelayPrizeConfig.getStartVolume()), String.valueOf(mkRelayPrizeConfig.getStepAddVolume()),
                mkRelayPrizeConfig.getCoinSymbol(), String.valueOf(mkRelayPrizeConfig.getGrantVolume()), String.valueOf(mkRelayPrizeConfig.getCurPoolVolume()), mkRelayPrizeConfig.getIsRemit(), String.valueOf(mkRelayPrizeConfig.getMinVolume()), String.valueOf(RELAY_PRIZE_TIME)));

        if (curDateTime.isBefore(LocalDateTime.of(curDateTime.toLocalDate(), LocalTime.parse(mkRelayPrizeConfig.getBeginTime())))) {
            logger.info(String.format("当天的活动未开始！[%s]", curDateTime.toString()));
            return false;
        }

        LocalDateTime endDateTime = LocalDateTime.of(curDateTime.toLocalDate(), LocalTime.parse(mkRelayPrizeConfig.getEndTime()));
        if (ObjectUtils.isEmpty(mkRelayTaskRecord)) {
            if (curDateTime.isAfter(endDateTime) && !DateUtils.formaterLocalDateTime(curDateTime).equals(DateUtils.formaterLocalDateTime(endDateTime))) {
                logger.info(String.format("当天的活动已结束！[%s]", curDateTime.toString()));
                return false;
            }
        } else {
            LocalDateTime lastBeginDateTime = mkRelayTaskRecord.getBeginDate();
            if (endDateTime.isBefore(lastBeginDateTime) || endDateTime.isEqual(lastBeginDateTime)) {
                logger.info(String.format("当天的活动已结束！[%s]", curDateTime.toString()));
                return false;
            }
        }

        return true;
    }

    private void createTaskRecord(MkRelayPrizeConfig mkRelayPrizeConfig, String remark, LocalDateTime curDateTime) {
        MkRelayTaskRecord mkRelayTaskRecord = new MkRelayTaskRecord();
        mkRelayTaskRecord.setId(UUID.randomUUID().toString().replace("-", ""));
        mkRelayTaskRecord.setType("1");
        mkRelayTaskRecord.setStatus("0");
        mkRelayTaskRecord.setBeginDate(curDateTime);
        mkRelayTaskRecord.setEndDate(LocalDateTime.now());
        mkRelayTaskRecord.setCoinId(ObjectUtils.isEmpty(mkRelayPrizeConfig) ? "" : mkRelayPrizeConfig.getCoinId());
        mkRelayTaskRecord.setCoinSymbol(ObjectUtils.isEmpty(mkRelayPrizeConfig) ? "" : mkRelayPrizeConfig.getCoinSymbol());
        mkRelayTaskRecord.setPoolVolume(ObjectUtils.isEmpty(mkRelayPrizeConfig) ? null : mkRelayPrizeConfig.getCurPoolVolume());
        mkRelayTaskRecord.setIncreaseNumber(0);
        mkRelayTaskRecord.setIncreaseVolume(BigDecimal.ZERO);
        mkRelayTaskRecord.setPrizeNumber(0);
        mkRelayTaskRecord.setPrizeVolume(BigDecimal.ZERO);
        mkRelayTaskRecord.setPrizeNumber(0);
        mkRelayTaskRecord.setPrizeVolume(BigDecimal.ZERO);
        mkRelayTaskRecord.setRemark(remark.substring(0, remark.length() > 300 ? 300 : remark.length()));
        mkRelayTaskRecord.setCreateDate(LocalDateTime.now());
        mkRelayTaskRecord.setUpdateDate(LocalDateTime.now());
        mkRelayTaskRecordDao.insert(mkRelayTaskRecord);
    }

    @Transactional
    public void execRelayPrizeCore(MkRelayPrizeConfig mkRelayPrizeConfig, LocalDateTime curDateTime) {

        //检查接力撞奖规则
        this.checkAndInitConfig(mkRelayPrizeConfig, curDateTime);

        //统计参与资格用户
        List<MkRelayPrizeElector> mkRelayPrizeElectorList = new ArrayList<>();
        this.collectElector(mkRelayPrizeConfig, mkRelayPrizeElectorList, curDateTime);

        //当前候选人是否达到获奖资格，奖励，整数倍，归零
        List<MkRelayRemitLog> mkRelayRemitLogList = new ArrayList<>();
        mkRelayRemitLogList = this.handleCandidatePrize(mkRelayPrizeConfig, curDateTime, mkRelayPrizeElectorList);

        //更新接力撞奖资金池
        this.regulatePrizePool(mkRelayPrizeConfig, mkRelayPrizeElectorList);

        //更新候选人,是否需要检查当前候选人是否达到获奖情况
        this.chooseCandidate(mkRelayPrizeElectorList, mkRelayPrizeConfig, mkRelayRemitLogList);

        //插入打款日志， 中奖者， 推荐人， 归集账户
        if (!CollectionUtils.isEmpty(mkRelayRemitLogList)) {
            mkRelayRemitLogDao.batchInsert(mkRelayRemitLogList);
        }

        //任务执行记录
        this.createTaskRecord(mkRelayPrizeConfig, mkRelayPrizeElectorList, mkRelayRemitLogList, curDateTime);

        //重新启动接力自动撞奖
        this.restartAutoRelay();
    }

    private void checkAndInitConfig(MkRelayPrizeConfig mkRelayPrizeConfig, LocalDateTime curDateTime) {

        if (ObjectUtils.isEmpty(mkRelayPrizeConfig)) {
            logger.info(String.format("[%s]没有可用的接力撞奖规则！", curDateTime.toLocalDate().toString()));
            throw new PlatException(Constants.RELAY_PRIZE_ERROR, "没有可用的接力撞奖规则！");
        }

        if (RELAY_PRIZE_TIME <= 0L) {
            logger.info(String.format("[%s]中奖时间间隔有异常！", curDateTime.toLocalDate().toString()));
            throw new PlatException(Constants.RELAY_PRIZE_ERROR, "中奖时间间隔有异常！");
        }

        if (ObjectUtils.isEmpty(mkRelayPrizeConfig.getMinVolume()) || mkRelayPrizeConfig.getMinVolume().compareTo(BigDecimal.ZERO) <= 0) {
            logger.info(String.format("[%s]最低参与数量不为空或小于等于零！", curDateTime.toLocalDate().toString()));
            throw new PlatException(Constants.RELAY_PRIZE_ERROR, "最低参与数量不为空或小于等于零！");
        }

        //设置初始值
        if (ObjectUtils.isEmpty(mkRelayPrizeConfig.getCurPoolVolume()) || mkRelayPrizeConfig.getCurPoolVolume().compareTo(BigDecimal.ZERO) == 0) {
            logger.info("初始化奖金池！");
            mkRelayPrizeConfig.setCurPoolVolume(mkRelayPrizeConfig.getStartVolume());
            mkRelayPrizeConfigDao.update(mkRelayPrizeConfig);
        }


        //设置发放奖金
        if (ObjectUtils.isEmpty(mkRelayPrizeConfig.getGrantVolume())) {
            mkRelayPrizeConfig.setGrantVolume(BigDecimal.ZERO);
        }

    }

    private List<MkRelayRemitLog> handleCandidatePrize(MkRelayPrizeConfig mkRelayPrizeConfig, LocalDateTime curDateTime, List<MkRelayPrizeElector> mkRelayPrizeElectorList) {

        MkRelayPrizeCandidate mkRelayPrizeCandidate = mkRelayPrizeCandidateDao.findActiveOne();
        if (ObjectUtils.isEmpty(mkRelayPrizeCandidate)) {
            logger.info("不存在活动的获奖候选人！");
            return null;
        } else {
            MkRelayPrizeElector mkRelayPrizeElector = null;
            if (!CollectionUtils.isEmpty(mkRelayPrizeElectorList)) {
                //取倒数第一个，离当前候选人参与时间最近的一个
                mkRelayPrizeElector = mkRelayPrizeElectorList.get(mkRelayPrizeElectorList.size() - 1);
            }
            if (this.isReachPrizeDateTime(mkRelayPrizeCandidate, mkRelayPrizeConfig, mkRelayPrizeElector, curDateTime)) {
                return this.drawPrize(mkRelayPrizeCandidate, mkRelayPrizeConfig); //执行中奖
            }
        }
        return null;
    }

    private boolean isReachPrizeDateTime(MkRelayPrizeCandidate mkRelayPrizeCandidate, MkRelayPrizeConfig mkRelayPrizeConfig, MkRelayPrizeElector mkRelayPrizeElector, LocalDateTime curDateTime) {

        LocalDateTime achieveDateTime = mkRelayPrizeCandidate.getAchieveDate(); //	候选人到达时间
        LocalDateTime curPrizeDateTime = null; //候选人获奖时间
        //获取当前候选人的获奖时间
        if (achieveDateTime.toLocalDate().isBefore(curDateTime.toLocalDate())) { //候选人不是当天
            //候选人还需要多长时间中奖
            Duration duration = Duration.between(mkRelayPrizeCandidate.getEndDate().minusMinutes(RELAY_PRIZE_TIME), achieveDateTime);
            long diff = duration.toMillis() / 1000;
            //当天活动开始加上时间差
            curPrizeDateTime = LocalDateTime.of(curDateTime.toLocalDate(), LocalTime.parse(mkRelayPrizeConfig.getBeginTime())).plusSeconds(diff);
        } else {
            curPrizeDateTime = mkRelayPrizeCandidate.getAchieveDate().plusMinutes(RELAY_PRIZE_TIME); //当前获选人的中奖时间
        }

        if (ObjectUtils.isEmpty(mkRelayPrizeElector)) {
            //与当前时间比较
            LocalDateTime curEndDate = LocalDateTime.of(curDateTime.toLocalDate(), LocalTime.parse(mkRelayPrizeConfig.getEndTime()));
            if (curDateTime.isAfter(curPrizeDateTime) && curEndDate.isAfter(curPrizeDateTime)) {
                return true;
            }
        } else {
            //离当前候选人参与时间最近的一个选举人o
            if (mkRelayPrizeElector.getReachDate().isAfter(curPrizeDateTime)) {
                return true;
            }
        }
        return false;
    }

    private void collectElector(MkRelayPrizeConfig mkRelayPrizeConfig, List<MkRelayPrizeElector> mkRelayPrizeElectorList, LocalDateTime curDateTime) {

        MkRelayTaskRecord mkRelayTaskRecord = mkRelayTaskRecordDao.findLastOneByDate(curDateTime); //取当天最近执行记录
        LocalDateTime beginDate = LocalDateTime.of(curDateTime.toLocalDate(), LocalTime.parse(mkRelayPrizeConfig.getBeginTime()));

        //当天存在执行记录则上一次执行时间
        if (!ObjectUtils.isEmpty(mkRelayTaskRecord)) {
            beginDate = mkRelayTaskRecord.getBeginDate();
        }

        //当前时间在活动结束时间之前则取当前时间
        LocalDateTime endDate = LocalDateTime.of(curDateTime.toLocalDate(), LocalTime.parse(mkRelayPrizeConfig.getEndTime()));
        if (curDateTime.isBefore(endDate)) {
            endDate = curDateTime;
        }

        //查询当天满足条件的资产账户，volume >= 5000, 时间在活动时间范围，没有参加接力撞奖活动
        List<UserCoinVolume> userCoinVolumeList = mkRelayPrizeElectorDao.findEffectiveUser(mkRelayPrizeConfig.getCoinSymbol(), beginDate, endDate, mkRelayPrizeConfig.getMinVolume());
        if (CollectionUtils.isEmpty(userCoinVolumeList)) {
            logger.info("无满足条件的用户！");
            return;
        }

        //插入选举人
        userCoinVolumeList.forEach(userCoinVolume -> {
            PlatUser platUser = platUserDao.findById(userCoinVolume.getUserId());
            MkRelayPrizeElector mkRelayPrizeElector = new MkRelayPrizeElector();
            mkRelayPrizeElector.setId(UUID.randomUUID().toString().replace("-", ""));
            mkRelayPrizeElector.setUserId(userCoinVolume.getUserId());
            mkRelayPrizeElector.setMail(platUser.getMail());
            mkRelayPrizeElector.setMobile(platUser.getMobile());
            mkRelayPrizeElector.setRealName(platUser.getRealName());
            mkRelayPrizeElector.setReferId(platUser.getReferId());
            if (StringUtils.isNotEmpty(platUser.getReferId())) {
                PlatUser referUser = platUserDao.findById(platUser.getReferId());
                mkRelayPrizeElector.setReferMail(referUser.getMail());
                mkRelayPrizeElector.setReferMobile(referUser.getMobile());
                mkRelayPrizeElector.setReferRealName(referUser.getRealName());
            }
            mkRelayPrizeElector.setCoinId(userCoinVolume.getCoinId());
            mkRelayPrizeElector.setCoinSymbol(userCoinVolume.getCoinSymbol());
            mkRelayPrizeElector.setVolume(userCoinVolume.getVolume());
            mkRelayPrizeElector.setReachDate(userCoinVolume.getUpdateDate());
            mkRelayPrizeElector.setBeginDate(LocalDateTime.of(curDateTime.toLocalDate(), LocalTime.parse(mkRelayPrizeConfig.getBeginTime())));
            mkRelayPrizeElector.setEndDate(LocalDateTime.of(curDateTime.toLocalDate(), LocalTime.parse(mkRelayPrizeConfig.getEndTime())));
            mkRelayPrizeElector.setRemark("");
            mkRelayPrizeElector.setCreateDate(curDateTime);
            mkRelayPrizeElector.setUpdateDate(curDateTime);
            mkRelayPrizeElectorList.add(mkRelayPrizeElector);
        });

        mkRelayPrizeElectorDao.batchInsert(mkRelayPrizeElectorList);
    }

    private void regulatePrizePool(MkRelayPrizeConfig mkRelayPrizeConfig, List<MkRelayPrizeElector> mkRelayPrizeElectorList) {

        if ("0".equals(mkRelayPrizeConfig.getStatus())) {
            logger.info("活动已终止");
            return;
        }

        if (!CollectionUtils.isEmpty(mkRelayPrizeElectorList)) {
            logger.info("当前增加：" + mkRelayPrizeElectorList.size() + "用户...");
            //第一次运行为空，初始化为起始值
            if (ObjectUtils.isEmpty(mkRelayPrizeConfig.getCurPoolVolume()) || BigDecimal.ZERO.compareTo(mkRelayPrizeConfig.getCurPoolVolume()) >= 0) {
                mkRelayPrizeConfig.setCurPoolVolume(mkRelayPrizeConfig.getStartVolume());
            }

            BigDecimal addVolume = mkRelayPrizeConfig.getStepAddVolume().multiply(BigDecimal.valueOf(mkRelayPrizeElectorList.size()));
            mkRelayPrizeConfig.setCurPoolVolume(mkRelayPrizeConfig.getCurPoolVolume().add(addVolume));
            BigDecimal leftVolume = mkRelayPrizeConfig.getVolume().subtract(mkRelayPrizeConfig.getGrantVolume());
            //当前撞奖池超过剩余可发放量
            if (mkRelayPrizeConfig.getCurPoolVolume().compareTo(leftVolume) > 0) {
                mkRelayPrizeConfig.setCurPoolVolume(leftVolume);
            }
            mkRelayPrizeConfigDao.update(mkRelayPrizeConfig);
        }
        //检测当前接力撞奖池
        this.checkPrizePool(mkRelayPrizeConfig);

    }

    private void chooseCandidate(List<MkRelayPrizeElector> mkRelayPrizeElectorList, MkRelayPrizeConfig mkRelayPrizeConfig, List<MkRelayRemitLog> mkRelayRemitLogList) {

        if ("0".equals(mkRelayPrizeConfig.getStatus())) {
            logger.info("活动已终止");
            return;
        }

        MkRelayPrizeElector mkRelayPrizeElector = null;
        if (CollectionUtils.isEmpty(mkRelayPrizeElectorList)) {
            logger.info(String.format("当前无[%s]满足参加的用户！", DateUtils.formaterLocalDateTime(LocalDateTime.now())));
            return;
        } else {
            mkRelayPrizeElector = mkRelayPrizeElectorList.get(0); //取排名第一
        }

        MkRelayPrizeCandidate mkRelayPrizeCandidate = mkRelayPrizeCandidateDao.findActiveOne(); // 取出当前获奖候选人
        if (!ObjectUtils.isEmpty(mkRelayPrizeCandidate)) {
            //执行失效
            this.loseCandidate(mkRelayPrizeCandidate);
        }
        //新建候选人
        this.createCandidate(mkRelayPrizeElector);

        final MkRelayPrizeElector electorCandidate = mkRelayPrizeElector;
        mkRelayPrizeElectorList.forEach(elector -> {
            if (!elector.getId().equals(electorCandidate.getId())) {
                //新建候选人
                MkRelayPrizeCandidate candidate = this.createCandidate(elector);
                //立即执行失效
                this.loseCandidate(candidate);
            }
        });

    }

    private void loseCandidate(MkRelayPrizeCandidate mkRelayPrizeCandidate) {
        mkRelayPrizeCandidate.setStatus("2"); //已失效
        mkRelayPrizeCandidate.setLostTime(LocalDateTime.now());
        mkRelayPrizeCandidate.setIsPrize("0"); //未中奖
        mkRelayPrizeCandidate.setUpdateDate(LocalDateTime.now());
        mkRelayPrizeCandidateDao.update(mkRelayPrizeCandidate);
    }

    private MkRelayPrizeCandidate createCandidate(MkRelayPrizeElector mkRelayPrizeElector) {
        MkRelayPrizeCandidate mkRelayPrizeCandidate = new MkRelayPrizeCandidate();
        mkRelayPrizeCandidate.setId(UUID.randomUUID().toString().replace("-", ""));
        mkRelayPrizeCandidate.setElectorId(mkRelayPrizeElector.getId());
        mkRelayPrizeCandidate.setAchieveDate(mkRelayPrizeElector.getReachDate());
        mkRelayPrizeCandidate.setCoinId(mkRelayPrizeElector.getCoinId());
        mkRelayPrizeCandidate.setCoinSymbol(mkRelayPrizeElector.getCoinSymbol());
        mkRelayPrizeCandidate.setUserId(mkRelayPrizeElector.getUserId());
        mkRelayPrizeCandidate.setMail(mkRelayPrizeElector.getMail());
        mkRelayPrizeCandidate.setMobile(mkRelayPrizeElector.getMobile());
        mkRelayPrizeCandidate.setRealName(mkRelayPrizeElector.getRealName());
        mkRelayPrizeCandidate.setReferId(mkRelayPrizeElector.getReferId());
        mkRelayPrizeCandidate.setReferMail(mkRelayPrizeElector.getReferMail());
        mkRelayPrizeCandidate.setReferMobile(mkRelayPrizeElector.getReferMobile());
        mkRelayPrizeCandidate.setReferRealName(mkRelayPrizeElector.getReferRealName());
        mkRelayPrizeCandidate.setVolume(mkRelayPrizeElector.getVolume());
        mkRelayPrizeCandidate.setRemark("");
        mkRelayPrizeCandidate.setIsPrize("0");
        mkRelayPrizeCandidate.setPrizeVolume(BigDecimal.ZERO);
        mkRelayPrizeCandidate.setStatus("0");
        mkRelayPrizeCandidate.setBeginDate(mkRelayPrizeElector.getBeginDate());
        mkRelayPrizeCandidate.setEndDate(mkRelayPrizeElector.getEndDate());
        mkRelayPrizeCandidate.setCreateDate(mkRelayPrizeElector.getCreateDate());
        mkRelayPrizeCandidate.setUpdateDate(LocalDateTime.now());
        mkRelayPrizeCandidateDao.insert(mkRelayPrizeCandidate);
        return mkRelayPrizeCandidate;
    }

    private List<MkRelayRemitLog> drawPrize(MkRelayPrizeCandidate mkRelayPrizeCandidate, MkRelayPrizeConfig mkRelayPrizeConfig) {

        try {
            //检查当前候选人，是否已中奖，是否失效
            this.checkCandidate(mkRelayPrizeCandidate);
        } catch (Exception e) {
            logger.error("检查当前候选人出现异常！", e);
            return null;
        }

        //检查当前奖金池
        this.checkPrizePool(mkRelayPrizeConfig);

        //打款日志流水
        List<MkRelayRemitLog> mkRelayRemitLogList = new ArrayList<>();
        //当前接力撞奖池
        BigDecimal poolVolume = mkRelayPrizeConfig.getCurPoolVolume();
        //撞奖池的整数倍
        int i = poolVolume.divide(mkRelayPrizeConfig.getStartVolume()).intValue();

        if (i > 1) {
            //发放奖金给候选人，
            BigDecimal curPrizeVolume = poolVolume.subtract(mkRelayPrizeConfig.getStartVolume().multiply(BigDecimal.valueOf(i - 1)));
            mkRelayRemitLogList.addAll(this.remitPrize(curPrizeVolume, mkRelayPrizeCandidate, mkRelayPrizeConfig));
            //候选人追溯整数倍的选举人
            List<MkRelayPrizeElector> electors = mkRelayPrizeElectorDao.findPrizeElector(i - 1, mkRelayPrizeCandidate.getAchieveDate());
            electors.forEach(mkRelayPrizeElector -> {
                MkRelayPrizeCandidate curCandidate = mkRelayPrizeCandidateDao.findByElectorId(mkRelayPrizeElector.getId());
                if (ObjectUtils.isEmpty(curCandidate)) {
                    curCandidate = this.createCandidate(mkRelayPrizeElector);
                    curCandidate.setRemark(String.format("奖金池[%s]已到达初始值[%s]的%s倍, 新增候选人", String.valueOf(poolVolume), String.valueOf(mkRelayPrizeConfig.getStartVolume()), String.valueOf(i)));
                } else {
                    curCandidate.setRemark(String.format("奖金池[%s]已到达初始值[%s]的%s倍, 重新激活候选人", String.valueOf(poolVolume), String.valueOf(mkRelayPrizeConfig.getStartVolume()), String.valueOf(i)));
                }
                mkRelayRemitLogList.addAll(this.remitPrize(mkRelayPrizeConfig.getStartVolume(), curCandidate, mkRelayPrizeConfig));
            });
        } else {
            //只有一个位获奖者
            mkRelayRemitLogList.addAll(this.remitPrize(poolVolume, mkRelayPrizeCandidate, mkRelayPrizeConfig));
        }

        //清空当前资金池与更新已发放奖金,如奖金无法支撑则结束该活动
        this.clearPrizePool(mkRelayPrizeConfig);

        //返回打款日志
        return mkRelayRemitLogList;
    }

    private void clearPrizePool(MkRelayPrizeConfig mkRelayPrizeConfig) {

        if (ObjectUtils.isEmpty(mkRelayPrizeConfig.getGrantVolume())) {
            mkRelayPrizeConfig.setGrantVolume(BigDecimal.ZERO);
        }

        //发放奖金+当前撞奖池奖金
        mkRelayPrizeConfig.setGrantVolume(mkRelayPrizeConfig.getGrantVolume().add(mkRelayPrizeConfig.getCurPoolVolume()));
        //剩余可发放奖金与初始化奖金比较
        BigDecimal leftVolume = mkRelayPrizeConfig.getVolume().subtract(mkRelayPrizeConfig.getGrantVolume());
        if (leftVolume.compareTo(BigDecimal.ZERO) > 0) {
            mkRelayPrizeConfig.setCurPoolVolume(mkRelayPrizeConfig.getStartVolume());
            //当前剩余可发放奖金比初始奖金小，奖金池为剩余可发放奖金
            if (leftVolume.compareTo(mkRelayPrizeConfig.getStartVolume()) < 0) {
                mkRelayPrizeConfig.setCurPoolVolume(leftVolume);
            }
        } else {
            logger.info("剩余可发放奖金已为零，终止接力撞奖活动！");
            mkRelayPrizeConfig.setCurPoolVolume(BigDecimal.ZERO);
            mkRelayPrizeConfig.setStatus("0");
        }
        mkRelayPrizeConfigDao.update(mkRelayPrizeConfig);

    }

    private void checkCandidate(MkRelayPrizeCandidate mkRelayPrizeCandidate) {

        if (mkRelayPrizeCandidate.getIsPrize().equals("1") || mkRelayPrizeCandidate.getStatus().equals("1")) {
            logger.info(String.format("当前候选人[%s]已获奖！", mkRelayPrizeCandidate.getUserId()));
            throw new PlatException(Constants.RELAY_PRIZE_ERROR, String.format("当前候选人[%s]已获奖！", mkRelayPrizeCandidate.getUserId()));
        }

        if (mkRelayPrizeCandidate.getStatus().equals("2")) {
            logger.info(String.format("当前候选人[%s]已失效！", mkRelayPrizeCandidate.getUserId()));
            throw new PlatException(Constants.RELAY_PRIZE_ERROR, String.format("当前候选人[%s]已失效！", mkRelayPrizeCandidate.getUserId()));
        }
    }

    private void

    checkPrizePool(MkRelayPrizeConfig mkRelayPrizeConfig) {

        if (mkRelayPrizeConfig.getCurPoolVolume().compareTo(BigDecimal.ZERO) < 0) {
            logger.info(String.format("当前接力撞奖池[%s]小于零", mkRelayPrizeConfig.getCurPoolVolume().toString()));
            throw new PlatException(Constants.RELAY_PRIZE_ERROR, "当前接力撞奖池小于零");
        }

        BigDecimal totalVolume = mkRelayPrizeConfig.getVolume();
        BigDecimal grantVolume = ObjectUtils.isEmpty(mkRelayPrizeConfig.getGrantVolume()) ? BigDecimal.ZERO : mkRelayPrizeConfig.getGrantVolume();
        if (mkRelayPrizeConfig.getCurPoolVolume().compareTo(totalVolume.subtract(grantVolume)) > 0) {
            logger.info(String.format("当前接力撞奖池[%s]大于可发放奖金[%s]!", mkRelayPrizeConfig.getCurPoolVolume().toString(), totalVolume.subtract(grantVolume).toString()));
            throw new PlatException(Constants.RELAY_PRIZE_ERROR, "当前接力撞奖池大于可发放奖金!");
        }
    }

    private List<MkRelayRemitLog> remitPrize(BigDecimal prizeVolume, MkRelayPrizeCandidate mkRelayPrizeCandidate, MkRelayPrizeConfig mkRelayPrizeConfig) {

        if (prizeVolume.compareTo(mkRelayPrizeConfig.getStartVolume()) < 0) {
            logger.error(String.format("发放奖金[%s]出现异常", String.valueOf(prizeVolume)));
            throw new PlatException(Constants.RELAY_PRIZE_ERROR, String.format("发放奖金[%s]出现异常", String.valueOf(prizeVolume)));
        }

        //打款日志流水
        List<MkRelayRemitLog> mkRelayRemitLogList = new ArrayList<>();
        //发放一半奖金给获奖者
        BigDecimal halfPrizeVolume = prizeVolume.divide(BigDecimal.valueOf(2L), 8, BigDecimal.ROUND_DOWN);
        //生成打款日志
        MkRelayRemitLog remitLog = this.createRemitLog(mkRelayPrizeCandidate, mkRelayPrizeConfig, halfPrizeVolume, "0");
        //更新获奖者资产
        this.remitVolume(mkRelayPrizeCandidate.getUserId(), mkRelayPrizeConfig, halfPrizeVolume, "中奖者", remitLog);
        //更新候选人获奖信息
        this.awardCandidate(mkRelayPrizeCandidate, halfPrizeVolume);
        //打款日志添加到日志集合
        mkRelayRemitLogList.add(remitLog);

        //把剩下的打给推荐人或归集账户
        halfPrizeVolume = prizeVolume.subtract(halfPrizeVolume);
        if (StringUtils.isNotEmpty(mkRelayPrizeCandidate.getReferId())) {
            //生成打款日志
            MkRelayRemitLog mkRelayRemitLog = this.createRemitLog(mkRelayPrizeCandidate, mkRelayPrizeConfig, halfPrizeVolume, "1");
            //更新用户推荐人资产
            this.remitVolume(mkRelayPrizeCandidate.getReferId(), mkRelayPrizeConfig, halfPrizeVolume, "中奖者推荐人",mkRelayRemitLog);
            //打款日志添加到日志集合
            mkRelayRemitLogList.add(mkRelayRemitLog);
        } else {
            //生成打款日志
            MkRelayRemitLog mkRelayRemitLog = this.createRemitLog(mkRelayPrizeCandidate, mkRelayPrizeConfig, halfPrizeVolume, "2");
            //归集到平台账户
            this.remitVolume(mkRelayPrizeConfig.getUserId(), mkRelayPrizeConfig, halfPrizeVolume, "归集账户", mkRelayRemitLog);
            //打款日志添加到日志集合
            mkRelayRemitLogList.add(mkRelayRemitLog);
        }

        return mkRelayRemitLogList;
    }

    private void remitVolume(String userId, MkRelayPrizeConfig mkRelayPrizeConfig, BigDecimal volume, String userTypeSymbol,MkRelayRemitLog mkRelayRemitLog) {

        logger.info(String.format("start 给%s[%s]打款[%s,%s]", userTypeSymbol, userId, mkRelayPrizeConfig.getCoinSymbol(), String.valueOf(volume)));

        if ("1".equals(mkRelayPrizeConfig.getIsRemit())) {
            logger.info("接力撞奖打款开关已开启：" + DateUtils.formaterLocalDateTime(LocalDateTime.now()));
//            long count = userCoinVolumeService.updateIncome(OrderEnum.OrderStatus.ALL_SUCCESS, volume, userId, mkRelayPrizeConfig.getCoinSymbol());
//            redisCacheManager.cleanUserCoinVolume(userId, mkRelayPrizeConfig.getCoinSymbol());//清除redis
            UserCoinVolumeBillDTO billDTO = new UserCoinVolumeBillDTO();
            billDTO.setCoinSymbol(mkRelayPrizeConfig.getCoinSymbol());
            billDTO.setForceLock(true);
            billDTO.setMark("接力撞奖打款");
            billDTO.setOpLockVolume(new BigDecimal(0));
            billDTO.setOpVolume(volume);
            billDTO.setUserId(userId);
            billDTO.setOpSign(new UserCoinVolumeEventEnum[]{UserCoinVolumeEventEnum.ADD_VOLUME});
            billDTO.setPriority(5);
            billDTO.setRefKey(mkRelayRemitLog.getId());
            billDTO.setSource("RELAY");

            long count = userCoinVolumeBillService.insert(billDTO);
            if (count <= 0) {
                throw new PlatException(Constants.RELAY_PRIZE_ERROR, "打款出现异常！");
            }
        } else {
            logger.info("接力撞奖打款开关已关闭：" + DateUtils.formaterLocalDateTime(LocalDateTime.now()));
        }

        logger.info(String.format("end 给%s[%s]打款[%s,%s]", userTypeSymbol, userId, mkRelayPrizeConfig.getCoinSymbol(), String.valueOf(volume)));

    }

    private void awardCandidate(MkRelayPrizeCandidate mkRelayPrizeCandidate, BigDecimal halfPrizeVolume) {
        mkRelayPrizeCandidate.setIsPrize("1"); // 已中奖
        mkRelayPrizeCandidate.setPrizeVolume(halfPrizeVolume);
        mkRelayPrizeCandidate.setStatus("1"); //状态改为已中奖
        mkRelayPrizeCandidate.setUpdateDate(LocalDateTime.now());
        mkRelayPrizeCandidateDao.update(mkRelayPrizeCandidate);
    }

    private MkRelayRemitLog createRemitLog(MkRelayPrizeCandidate mkRelayPrizeCandidate, MkRelayPrizeConfig mkRelayPrizeConfig, BigDecimal volume, String userType) {
        MkRelayRemitLog mkRelayRemitLog = new MkRelayRemitLog();
        mkRelayRemitLog.setId(UUID.randomUUID().toString().replace("-", ""));
        mkRelayRemitLog.setCoinId(mkRelayPrizeCandidate.getCoinId());
        mkRelayRemitLog.setCoinSymbol(mkRelayPrizeCandidate.getCoinSymbol());
        mkRelayRemitLog.setVolume(volume);
        mkRelayRemitLog.setUserType(userType);
        mkRelayRemitLog.setIsRemit(mkRelayPrizeConfig.getIsRemit());
        mkRelayRemitLog.setCreateDate(LocalDateTime.now());
        if ("1".equals(userType)) { //推荐人
            PlatUser platUser = platUserDao.findById(mkRelayPrizeCandidate.getReferId());
            mkRelayRemitLog.setUserId(platUser.getId());
            mkRelayRemitLog.setMail(platUser.getMail());
            mkRelayRemitLog.setMobile(platUser.getMobile());
            mkRelayRemitLog.setRealName(platUser.getRealName());
            mkRelayRemitLog.setRemark(String.format("获奖者其推荐人奖励[%s,%s]", mkRelayPrizeCandidate.getCoinSymbol(), String.valueOf(volume)));
        } else if ("0".equals(userType)) { //中奖者
            mkRelayRemitLog.setUserId(mkRelayPrizeCandidate.getUserId());
            mkRelayRemitLog.setMail(mkRelayPrizeCandidate.getMail());
            mkRelayRemitLog.setMobile(mkRelayPrizeCandidate.getMobile());
            mkRelayRemitLog.setRealName(mkRelayPrizeCandidate.getRealName());
            mkRelayRemitLog.setRefereeId(mkRelayPrizeCandidate.getReferId());
            mkRelayRemitLog.setReferMail(mkRelayPrizeCandidate.getReferMail());
            mkRelayRemitLog.setReferMobile(mkRelayPrizeCandidate.getReferMobile());
            mkRelayRemitLog.setReferRealName(mkRelayPrizeCandidate.getReferRealName());
            mkRelayRemitLog.setRemark(String.format("获奖者奖励[%s,%s]", mkRelayPrizeCandidate.getCoinSymbol(), String.valueOf(volume)));
        } else {
            //归集到平台账户
            mkRelayRemitLog.setUserId(mkRelayPrizeConfig.getUserId());
            mkRelayRemitLog.setMail(mkRelayPrizeConfig.getMail());
            mkRelayRemitLog.setMobile(mkRelayPrizeConfig.getMobile());
            mkRelayRemitLog.setRealName(mkRelayPrizeConfig.getRealName());
            mkRelayRemitLog.setRemark(String.format("归集商户[%s,%s]", mkRelayPrizeCandidate.getCoinSymbol(), String.valueOf(volume)));
        }
        return mkRelayRemitLog;
    }

    private void restartAutoRelay() {
        logger.info("重新启动接力自动撞奖---entry");
        MkRelayAutoConfig mkRelayAutoConfig = mkRelayAutoConfigDao.findUnActiveOne();
        if (Objects.isNull(mkRelayAutoConfig) || Objects.isNull(mkRelayAutoConfig.getStartRewardNumber()) || mkRelayAutoConfig.getStartRewardNumber() <= 0) {
            logger.info("启动接力自动撞奖已启动");
            return;
        }

        Integer rewardNumber = mkRelayTaskRecordDao.countByDate(mkRelayAutoConfig.getUpdateDate());
        if (rewardNumber >= 0 && rewardNumber >= mkRelayAutoConfig.getStartRewardNumber()) {
            logger.info("重新启动接力自动撞奖");
            mkRelayAutoConfig.setStatus("1");
            mkRelayAutoConfig.setRemark("重新启动接力自动撞奖：" + DateUtils.formaterLocalDateTime(LocalDateTime.now()));
            mkRelayAutoConfig.setUpdateDate(LocalDateTime.now());
            mkRelayAutoConfigDao.update(mkRelayAutoConfig);
        }

    }

    private void createTaskRecord(MkRelayPrizeConfig mkRelayPrizeConfig, List<MkRelayPrizeElector> mkRelayPrizeElectorList, List<MkRelayRemitLog> mkRelayRemitLogList, LocalDateTime curDateTime) {
        MkRelayTaskRecord mkRelayTaskRecord = new MkRelayTaskRecord();
        mkRelayTaskRecord.setId(UUID.randomUUID().toString().replace("-", ""));
        mkRelayTaskRecord.setType("1");
        mkRelayTaskRecord.setStatus("1");
        mkRelayTaskRecord.setBeginDate(curDateTime);
        mkRelayTaskRecord.setEndDate(LocalDateTime.now());
        mkRelayTaskRecord.setCoinId(mkRelayPrizeConfig.getCoinId());
        mkRelayTaskRecord.setCoinSymbol(mkRelayPrizeConfig.getCoinSymbol());
        mkRelayTaskRecord.setPoolVolume(mkRelayPrizeConfig.getCurPoolVolume());
        mkRelayTaskRecord.setCreateDate(LocalDateTime.now());
        mkRelayTaskRecord.setUpdateDate(LocalDateTime.now());

        if ("0".equals(mkRelayPrizeConfig.getStatus())) {
            mkRelayTaskRecord.setRemark("执行成功！此记录执行后活动将结束,请检查当前撞奖池资金！");
        } else {
            BigDecimal leftVolume = mkRelayPrizeConfig.getVolume().subtract(mkRelayPrizeConfig.getGrantVolume());
            if (leftVolume.compareTo(mkRelayPrizeConfig.getCurPoolVolume()) == 0) {
                mkRelayTaskRecord.setRemark("执行成功！当前撞奖池已达到剩余可发放奖金！");
            } else {
                mkRelayTaskRecord.setRemark("执行成功！");
            }
        }

        if (CollectionUtils.isEmpty(mkRelayPrizeElectorList)) {
            mkRelayTaskRecord.setIncreaseNumber(0);
            mkRelayTaskRecord.setIncreaseVolume(BigDecimal.ZERO);
        } else {
            Integer incNumber = mkRelayPrizeElectorList.size();
            BigDecimal incVolume = mkRelayPrizeConfig.getStepAddVolume().multiply(BigDecimal.valueOf(incNumber));
            mkRelayTaskRecord.setIncreaseNumber(incNumber);
            mkRelayTaskRecord.setIncreaseVolume(incVolume);
        }

        if (CollectionUtils.isEmpty(mkRelayRemitLogList)) {
            mkRelayTaskRecord.setPrizeNumber(0);
            mkRelayTaskRecord.setPrizeVolume(BigDecimal.ZERO);
        } else {
            BigDecimal prizeVolume = BigDecimal.valueOf(mkRelayRemitLogList.stream().mapToDouble(mkRelayRemitLog -> Double.valueOf(String.valueOf(mkRelayRemitLog.getVolume()))).sum());
            mkRelayTaskRecord.setPrizeVolume(prizeVolume);
            mkRelayTaskRecord.setPrizeNumber(mkRelayRemitLogList.size());
        }
        mkRelayTaskRecordDao.insert(mkRelayTaskRecord);
    }

    @Override
    public void relayAutoTaskEntry() {
        logger.info("start relayAutoTaskEntry......");

        //获取启用的接力撞奖规则
        LocalDateTime curDateTime = LocalDateTime.now();
        MkRelayPrizeConfig mkRelayPrizeConfig = mkRelayPrizeConfigDao.findActiveOne();
        MkRelayTaskRecord mkRelayTaskRecord = mkRelayTaskRecordDao.findLastOneByDate(curDateTime);

        if (!checkConfig(mkRelayPrizeConfig, curDateTime, mkRelayTaskRecord)) {
            return;
        }

        MkRelayAutoConfig mkRelayAutoConfig = mkRelayAutoConfigDao.findActiveOne();
        if (ObjectUtils.isEmpty(mkRelayAutoConfig)) {
            logger.info("不存在启动中的接力自动撞奖配置！");
            return;
        }

        try {
            mkRelayPrizeTaskService.execAutoPrizeCore(mkRelayPrizeConfig, curDateTime);
        } catch (PlatException pe) {
            logger.error("接力自动撞奖出现业务异常：", pe);
            this.createAutoRecord(mkRelayPrizeConfig, pe.getMsg(), curDateTime);
        } catch (Exception e) {
            logger.info("接力自动撞奖系统异常：", e);
            this.createAutoRecord(mkRelayPrizeConfig, e.getMessage(), curDateTime);
        }

        logger.info("end relayAutoTaskEntry......");
    }

    private void createAutoRecord(MkRelayPrizeConfig mkRelayPrizeConfig, String remark, LocalDateTime curDateTime) {
        MkRelayAutoRecord mkRelayAutoRecord = new MkRelayAutoRecord();
        mkRelayAutoRecord.setId(UUID.randomUUID().toString().replace("-", ""));
        mkRelayAutoRecord.setStatus("0");
        mkRelayAutoRecord.setBeginDate(curDateTime);
        mkRelayAutoRecord.setEndDate(LocalDateTime.now());
        mkRelayAutoRecord.setCoinId(ObjectUtils.isEmpty(mkRelayPrizeConfig) ? "" : mkRelayPrizeConfig.getCoinId());
        mkRelayAutoRecord.setCoinSymbol(ObjectUtils.isEmpty(mkRelayPrizeConfig) ? "" : mkRelayPrizeConfig.getCoinSymbol());
        mkRelayAutoRecord.setVolume(BigDecimal.ZERO);
        mkRelayAutoRecord.setRemark(remark.substring(0, remark.length() > 300 ? 300 : remark.length()));
        mkRelayAutoRecord.setCreateDate(LocalDateTime.now());
        mkRelayAutoRecord.setUpdateDate(LocalDateTime.now());
        mkRelayAutoRecordDao.insert(mkRelayAutoRecord);
    }

    @Transactional
    public void execAutoPrizeCore(MkRelayPrizeConfig mkRelayPrizeConfig, LocalDateTime curDateTime) {

        mkRelayPrizeConfig.setRemark(""); //当作返回标识
        MkRelayPrizeCandidate mkRelayPrizeCandidate = mkRelayPrizeCandidateDao.findActiveOne(); //获取当前候选人

        //选择一个用户参与接力撞奖
        PlatUser platUser = this.pickOneToRelay(mkRelayPrizeCandidate, mkRelayPrizeConfig, curDateTime);

        //接力自动撞奖记录
        this.createAutoRecord(mkRelayPrizeConfig, mkRelayPrizeCandidate, platUser, curDateTime);

    }

    private LocalDateTime getCurPrizeDateTime(MkRelayPrizeCandidate mkRelayPrizeCandidate, MkRelayPrizeConfig mkRelayPrizeConfig, LocalDateTime curDateTime) {
        LocalDateTime achieveDateTime = mkRelayPrizeCandidate.getAchieveDate(); //	候选人到达时间
        LocalDateTime curPrizeDateTime = null; //候选人获奖时间
        //获取当前候选人的获奖时间
        if (achieveDateTime.toLocalDate().isBefore(curDateTime.toLocalDate())) { //候选人不是当天
            Duration duration = Duration.between(mkRelayPrizeCandidate.getEndDate().minusMinutes(RELAY_PRIZE_TIME), achieveDateTime);  //候选人还需要多长时间中奖
            long diff = duration.toMillis() / 1000;
            curPrizeDateTime = LocalDateTime.of(curDateTime.toLocalDate(), LocalTime.parse(mkRelayPrizeConfig.getBeginTime())).plusSeconds(diff);
        } else {
            curPrizeDateTime = mkRelayPrizeCandidate.getAchieveDate().plusMinutes(RELAY_PRIZE_TIME); //当前获选人的中奖时间
        }

        logger.info(String.format("当前候选人[%s]的中奖时间[%s]", mkRelayPrizeCandidate.getUserId(), DateUtils.formaterLocalDateTime(curPrizeDateTime)));
        return curPrizeDateTime;
    }

    private PlatUser pickOneToRelay(MkRelayPrizeCandidate mkRelayPrizeCandidate, MkRelayPrizeConfig mkRelayPrizeConfig, LocalDateTime curDateTime) {

        if (ObjectUtils.isEmpty(mkRelayPrizeCandidate)) {
            mkRelayPrizeConfig.setRemark("当前无候选人！");
            logger.info("当前无候选人！");
            return null;
        }

        LocalDateTime curPrizeDateTime = this.getCurPrizeDateTime(mkRelayPrizeCandidate, mkRelayPrizeConfig, curDateTime);
        Duration duration = Duration.between(curDateTime, curPrizeDateTime);
        Long left = duration.toMillis() / 1000; //候选人离中奖时间剩余秒数

        if (left <= 0) {
            mkRelayPrizeConfig.setRemark(String.format("当前候选人的中奖时间[%s]已到达！", DateUtils.formaterLocalDateTime(curPrizeDateTime)));
            logger.info(String.format("当前候选人的中奖时间[%s]已到达！", DateUtils.formaterLocalDateTime(curPrizeDateTime)));
            return null;
        }

        PlatUser platUser = null;
        if (left <= 240) {
            logger.info(String.format("当前候选人离中奖时间[%s]还剩[%s]秒", DateUtils.formaterLocalDateTime(curPrizeDateTime), String.valueOf(left)));
            platUser = mkRelayPrizeElectorDao.findOneUserByTag("RELAY");
            if (!ObjectUtils.isEmpty(platUser)) {
                //检测是否存在预候选人
                if (this.checkExistPreCandidate(mkRelayPrizeConfig, mkRelayPrizeCandidate, curDateTime, curPrizeDateTime)) {
                    return null;
                }
                //获取预参与时间
                LocalDateTime prePrizeDateTime = this.getPrePrizeDateTime(left, platUser, mkRelayPrizeConfig, curDateTime);
                //跟新用户资产自动参与接力撞奖
                this.updateUserCoinVolume(platUser, prePrizeDateTime, mkRelayPrizeConfig, curDateTime);
            } else {
                mkRelayPrizeConfig.setRemark("当前无可用的接力自动撞奖用户，请及时跟进！");
                logger.info("当前无可用的接力自动撞奖用户，请及时跟进！");
            }
        } else {
            mkRelayPrizeConfig.setRemark("中奖时间不在范围之内！");
            logger.info("当前候选人的中奖时间不在接力自动撞奖时间范围之内！");
        }
        return platUser;
    }

    private boolean checkExistPreCandidate(MkRelayPrizeConfig mkRelayPrizeConfig, MkRelayPrizeCandidate mkRelayPrizeCandidate, LocalDateTime curDateTime, LocalDateTime curPrizeDateTime) {

        MkRelayAutoRecord mkRelayAutoRecord = mkRelayAutoRecordDao.findByCandidateId(mkRelayPrizeCandidate.getId());
        if (!ObjectUtils.isEmpty(mkRelayAutoRecord) && StringUtils.isNotEmpty(mkRelayAutoRecord.getUserId())) {
            UserCoinVolume userCoinVolume = userCoinVolumeDao.findByUserIdAndCoinId(mkRelayAutoRecord.getUserId(), mkRelayPrizeConfig.getCoinId());
            if (!ObjectUtils.isEmpty(userCoinVolume) && userCoinVolume.getUpdateDate().isBefore(curPrizeDateTime)) {
                LocalDateTime beginDateTime = LocalDateTime.of(curDateTime.toLocalDate(), LocalTime.parse(mkRelayPrizeConfig.getBeginTime()));
                LocalDateTime endDateTime = LocalDateTime.of(curDateTime.toLocalDate(), LocalTime.parse(mkRelayPrizeConfig.getEndTime()));
                if (userCoinVolume.getUpdateDate().isAfter(beginDateTime) && userCoinVolume.getUpdateDate().isBefore(endDateTime)) {
                    logger.info(String.format("当前候选人已有用户自动参与接力撞奖，参与时间[%s]", DateUtils.formaterLocalDateTime(mkRelayAutoRecord.getReachDate())));
                    mkRelayPrizeConfig.setRemark(String.format("预参与时间[%s]", DateUtils.formaterLocalDateTime(mkRelayAutoRecord.getReachDate())));
                    return true;
                }
            }
        }
        return false;
    }

    private LocalDateTime getPrePrizeDateTime(Long left, PlatUser platUser, MkRelayPrizeConfig mkRelayPrizeConfig, LocalDateTime curDateTime) {

        long updateTimeDiff = 0L; //当前候选人获奖时间在两分钟之内，用户更新资产时间为当前时间
        if (left > 120) {
            Random rand = new Random();
            int maxLeft = left.intValue() - 120; //最大偏量减两分钟， 最大变量在[1-180]
            updateTimeDiff = rand.nextInt(maxLeft) + 1; //随机取个时间
        }
        logger.info(String.format("更新用户[%s]资产,时间偏量[%s]秒", platUser.getId(), String.valueOf(updateTimeDiff)));
        //如果预参与时间在活动结束时间之后，则取活动结束作为预参与时间
        LocalDateTime prePrizeDateTime = curDateTime.plusSeconds(updateTimeDiff);
        LocalDateTime endDateTime = LocalDateTime.of(curDateTime.toLocalDate(), LocalTime.parse(mkRelayPrizeConfig.getEndTime()));
        if (prePrizeDateTime.isAfter(endDateTime)) {
            prePrizeDateTime = endDateTime;
        }

        logger.info(String.format("用户[%s]预参与时间[%s]", platUser.getId(), DateUtils.formaterLocalDateTime(prePrizeDateTime)));
        return prePrizeDateTime;
    }

    private void updateUserCoinVolume(PlatUser platUser, LocalDateTime prePrizeDateTime, MkRelayPrizeConfig mkRelayPrizeConfig, LocalDateTime curDateTime) {
        UserCoinVolume userCoinVolume = userCoinVolumeDao.findByUserIdAndCoinId(platUser.getId(), mkRelayPrizeConfig.getCoinId());
        BigDecimal addVolume = mkRelayPrizeConfig.getMinVolume().add(BigDecimal.ONE); //最小金额增加1
        long count = 0L;
        if (ObjectUtils.isEmpty(userCoinVolume)) {
            userCoinVolume = new UserCoinVolume();
            userCoinVolume.setId(SnowFlake.createSnowFlake().nextIdString());
            userCoinVolume.setCoinId(mkRelayPrizeConfig.getCoinId());
            userCoinVolume.setCoinSymbol(mkRelayPrizeConfig.getCoinSymbol());
            userCoinVolume.setLockVolume(BigDecimal.ZERO);
            userCoinVolume.setUserId(platUser.getId());
            userCoinVolume.setVolume(addVolume);
            userCoinVolume.setFlag((short) 0);
            userCoinVolume.setCreateBy("relay");
            userCoinVolume.setUpdateBy("relay");
            userCoinVolume.setCreateDate(prePrizeDateTime);
            userCoinVolume.setUpdateDate(prePrizeDateTime);
            count = userCoinVolumeDao.insert(userCoinVolume);
        } else {
            if (userCoinVolume.getVolume().compareTo(addVolume) >= 0) {
                addVolume = BigDecimal.ONE;
                userCoinVolume.setVolume(userCoinVolume.getVolume().add(addVolume));
            } else {
                addVolume = addVolume.subtract(userCoinVolume.getVolume());
                userCoinVolume.setVolume(addVolume);
            }
            userCoinVolume.setUpdateBy("relay");
            userCoinVolume.setUpdateDate(prePrizeDateTime);
            count = userCoinVolumeDao.updateById(userCoinVolume);
        }

        if (count <= 0) {
            logger.error(String.format("接力自动撞奖打款[%s]出现异常！", platUser.getId()));
            throw new PlatException(Constants.RELAY_PRIZE_ERROR, String.format("接力自动撞奖打款[%s]出现异常！", platUser.getId()));
        }

        UserCoinVolumeHistory userCoinVolumeHistory = new UserCoinVolumeHistory();
        userCoinVolumeHistory.setId(UUID.randomUUID().toString().replace("-", ""));
        userCoinVolumeHistory.setUserId(platUser.getId());
        userCoinVolumeHistory.setAccount(StringUtils.isEmpty(platUser.getMobile()) ? platUser.getMail() : platUser.getMobile());
        userCoinVolumeHistory.setAccount(userCoinVolumeHistory.getAccount().concat("(RELAY)"));
        userCoinVolumeHistory.setCoinId(mkRelayPrizeConfig.getCoinId());
        userCoinVolumeHistory.setCoinSymbol(mkRelayPrizeConfig.getCoinSymbol());
        userCoinVolumeHistory.setVolume(addVolume);
        userCoinVolumeHistory.setType("other_scene");
        userCoinVolumeHistory.setCreateDate(prePrizeDateTime);
        userCoinVolumeHistory.setUpdateDate(prePrizeDateTime);
        userCoinVolumeHistory.setCreateBy("relay");
        userCoinVolumeHistory.setUpdateBy("relay");
        userCoinVolumeHistoryDao.insert(userCoinVolumeHistory);

        //返回数据
        mkRelayPrizeConfig.setGrantVolume(userCoinVolume.getVolume());
        mkRelayPrizeConfig.setCreateDate(prePrizeDateTime);
    }

    private void createAutoRecord(MkRelayPrizeConfig mkRelayPrizeConfig, MkRelayPrizeCandidate mkRelayPrizeCandidate, PlatUser platUser, LocalDateTime curDateTime) {
        MkRelayAutoRecord mkRelayAutoRecord = new MkRelayAutoRecord();
        mkRelayAutoRecord.setId(UUID.randomUUID().toString().replace("-", ""));
        mkRelayAutoRecord.setStatus("1");
        mkRelayAutoRecord.setBeginDate(curDateTime);
        mkRelayAutoRecord.setEndDate(LocalDateTime.now());
        mkRelayAutoRecord.setCoinId(mkRelayPrizeConfig.getCoinId());
        mkRelayAutoRecord.setCoinSymbol(mkRelayPrizeConfig.getCoinSymbol());
        mkRelayAutoRecord.setVolume(BigDecimal.ZERO);
        mkRelayAutoRecord.setRemark(mkRelayPrizeConfig.getRemark());
        if (!ObjectUtils.isEmpty(platUser)) {
            mkRelayAutoRecord.setUserId(platUser.getId());
            mkRelayAutoRecord.setMail(platUser.getMail());
            mkRelayAutoRecord.setMobile(platUser.getMobile());
            mkRelayAutoRecord.setVolume(mkRelayPrizeConfig.getGrantVolume());
            if (!ObjectUtils.isEmpty(mkRelayPrizeCandidate)) {
                mkRelayAutoRecord.setCandidateId(mkRelayPrizeCandidate.getId());
            }
            mkRelayAutoRecord.setReachDate(mkRelayPrizeConfig.getCreateDate());
        }
        long count = mkRelayPrizeElectorDao.countUserByTag("RELAY");
        mkRelayAutoRecord.setRemark(mkRelayAutoRecord.getRemark().concat(String.format(",还剩[%s]个用户！", String.valueOf(count))));
        mkRelayAutoRecord.setCreateDate(LocalDateTime.now());
        mkRelayAutoRecord.setUpdateDate(LocalDateTime.now());
        mkRelayAutoRecordDao.insert(mkRelayAutoRecord);
    }
}
