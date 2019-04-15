package com.biao.service.impl;

import com.biao.constant.Constants;
import com.biao.entity.mk2.Mk2PopularizeCommonMember;
import com.biao.entity.mk2.Mk2PopularizeMember;
import com.biao.entity.mk2.Mk2PopularizeMemberRule;
import com.biao.entity.mk2.Mk2PopularizeReleaseLog;
import com.biao.enums.UserCoinVolumeEventEnum;
import com.biao.exception.PlatException;
import com.biao.mapper.mk2.*;
import com.biao.pojo.UserCoinVolumeBillDTO;
import com.biao.service.Mk2MemberReleaseService;
import com.biao.service.UserCoinVolumeBillService;
import com.biao.util.SnowFlake;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.biao.enums.CycleEnum.*;


@Service
public class Mk2MemberReleaseServiceImpl implements Mk2MemberReleaseService {

    private Logger logger = LoggerFactory.getLogger(Mk2MemberReleaseServiceImpl.class);
    @Autowired
    private Mk2MemberReleaseService mk2MemberReleaseService;

    @Autowired
    private Mk2PopularizeReleaseLogDao releaseLogDao;

    @Autowired
    private Mk2PopularizeCommonMemberDao commonMemberDao;

    @Autowired
    private Mk2PopularizeMemberRuleDao mk2PopularizeMemberRuleDao;

    @Autowired
    private UserCoinVolumeBillService userCoinVolumeBillService;

    private static final BigDecimal PERCENT = new BigDecimal(100);

    private static final int BONUS_SCALE = 2;// 两位小数位

    private static final int pageCount = 200;

    private static final String RELEASE_TYPE_MEMBER = "1";
    private static final String RELEASE_TYPE_NODEL = "2";
    private static final String RELEASE_TYPE_AREA = "3";

    private static final String CYCLE_BEGIN = "cycleBegin";
    private static final String CYCLE_END = "cycleEnd";
    private static final String CYCLE_NULL = "cycleNull";

    @Override
    @Transactional
    public void releaseLockVolume() {
        try {
            releaseBatchCommenMemberLock();
        } catch (Exception e) {
            logger.error("释放任务抛错??", e);
            throw new PlatException(Constants.PARAM_ERROR, "释放任务抛错!");
        } finally {
            mk2MemberReleaseService.saveTaskLog(LocalDateTime.of(LocalDateTime.now().toLocalDate(), LocalTime.MIN), BigDecimal.ZERO);// 记录日志
        }

    }

    /**
     * 批量释放普通会员冻结的币
     */
    private void releaseBatchCommenMemberLock() {
        Mk2PopularizeMemberRule rule = mk2PopularizeMemberRuleDao.findByType(RELEASE_TYPE_MEMBER);
        if (Objects.isNull(rule) || !"1".equals(rule.getReleaseOpen())) {
            logger.error("解锁节普通会员配置未开启!!");
            return;
        }
        logger.info("本周期释放规则配置：{}", rule.toString());
        // 查询需要释放的冻结
        List<Mk2PopularizeCommonMember> commonMembers = commonMemberDao.findReleaseCommonMember();
        if (CollectionUtils.isEmpty(commonMembers)) {
            logger.info("需要释放的会员集未空！");
            return;
        }
        // 1、开始释放，设置本次释放版本号
        rule.setReleaseVersion(rule.getReleaseVersion() + 1);
        // 2、插入释放日志
        for (Mk2PopularizeCommonMember commonMember : commonMembers) {
            try {
                mk2MemberReleaseService.releaseCommonMemberLock(commonMember, rule);
            } catch (Exception e) {
                logger.error("解锁节普通会员失败1：{}", commonMember.toString());
                logger.error("解锁节普通会员失败2" + commonMember.getId() + "]", e);
            }
        }
        // 3、根据释放日志，将本次版本号内的会员释放记录按需求插入bail表中，等待异步加资产
        try {
            mk2MemberReleaseService.commonMemberUpdateBBvolume(rule);
        } catch (Exception e) {
            logger.error("解锁节普通会员失败3", e);
        }

    }

    /**
     * 释放单个会员冻结的币
     * @param commonMember 会员信息
     * @param rule 释放配置
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void releaseCommonMemberLock(Mk2PopularizeCommonMember commonMember, Mk2PopularizeMemberRule rule) {

        logger.info("单个会员释放开始：{}", commonMember.toString());
        // 判断该会员的释放周期，在配置中是否开启
        boolean isMatchMemberRule = checkMemberInfoMatchRule(commonMember, rule);
        if (!isMatchMemberRule) {
            logger.info("该会员信息与释放配置不匹配：{}", commonMember.getId());
            return;
        }
        Mk2PopularizeReleaseLog lastReleaseLog = releaseLogDao.findLastByMemberId(commonMember.getId(), RELEASE_TYPE_MEMBER);
        LocalDateTime now = LocalDateTime.now();
        // 1、判断上一期是否需要补发
        LocalDateTime preCycleBeginTime = getCycleTime(now, commonMember.getReleaseCycle(), CYCLE_BEGIN, 1);
        LocalDateTime preCycleEndTime = getCycleTime(now, commonMember.getReleaseCycle(), CYCLE_END, 1);
        if (Objects.isNull(preCycleBeginTime) || Objects.isNull(preCycleEndTime)) {// 判断释放周期起止时间参数是否正确
            logger.error("会员释放参数设置错误：{}", commonMember.toString());
            return;
        }
        // 补发规则：会员释放开始时间要早于上周期的结束时间，且上一次释放时间早于上周期的开始时间，则表示上周期未释放，需要补释放
        if (commonMember.getReleaseBeginDate().isBefore(preCycleEndTime) &&
                (Objects.isNull(lastReleaseLog) || lastReleaseLog.getReleaseCycleDate().isBefore(preCycleBeginTime))) {
            LocalDateTime preCycTime = getCycleTime(now, commonMember.getReleaseCycle(), CYCLE_NULL, 1);
            logger.info("单个会员：{}释放补发日期：{}，会员信息：{}", commonMember.getId(), preCycTime, commonMember.toString());
            commonMemberReleaseCycle(preCycTime, commonMember, rule); // 补发释放
        }
        // 2、本期发放
        // 本期发放规则：1周期当天2会员释放开始时间必须早于本周期的结束时间3本周起不存在历史释放成功记录
        int nowCycleOrder = getCycleOrder(now, commonMember.getReleaseCycle());
        int confCycleOrder = getCycleOrder(commonMember.getReleaseBeginDate(), commonMember.getReleaseCycle());
        LocalDateTime curCycleBeginTime = getCycleTime(now, commonMember.getReleaseCycle(), CYCLE_BEGIN, 0);
        LocalDateTime curCycleEndTime = getCycleTime(now, commonMember.getReleaseCycle(), CYCLE_END, 0);
        if (nowCycleOrder == confCycleOrder && nowCycleOrder != -1  // 1周期当天
                && commonMember.getReleaseBeginDate().isBefore(curCycleEndTime) // 2会员释放开始时间必须早于本周期的结束时间
                && (Objects.isNull(lastReleaseLog) || lastReleaseLog.getReleaseCycleDate().isBefore(curCycleBeginTime))) { // 3本周起不存在历史释放成功记录
            logger.info("单个会员：{}发放当期：{}，会员信息：{}", commonMember.getId(), now, commonMember.toString());
            commonMemberReleaseCycle(now, commonMember, rule); // 本次释放
        }
        logger.info("单个会员释放结束：{}", commonMember.toString());
    }

    /**
     * 会员币释放核心逻辑
     * @param cycleTime
     * @param commonMember
     * @param rule
     */
    private void commonMemberReleaseCycle(LocalDateTime cycleTime, Mk2PopularizeCommonMember commonMember, Mk2PopularizeMemberRule rule) {

        // 判断该会员是否未有余量需要释放
        BigDecimal surplusVol = commonMember.getLockVolume().subtract(commonMember.getReleaseVolume() == null ? BigDecimal.ZERO : commonMember.getReleaseVolume());
        if (surplusVol.compareTo(BigDecimal.ZERO) < 1) {// 已经全部释放，本次不需释放
            logger.info("该会员已经释放完：{}", commonMember.toString());
            return;
        }
        String releaseOver = "0";// 锁定量是否全部释放0否1是
        BigDecimal curReleaseVol = commonMember.getReleaseCycleRatio().divide(PERCENT).multiply(commonMember.getLockVolume()).setScale(BONUS_SCALE, BigDecimal.ROUND_DOWN);
        if (curReleaseVol.compareTo(surplusVol) > -1) {
            curReleaseVol = surplusVol;
            releaseOver = "1";
        }
        commonMember.setReleaseOver(releaseOver);
        commonMember.setReleaseVolume(commonMember.getReleaseVolume().add(curReleaseVol));
        logger.info("释放用户冻结-加本次释放数量：{}", commonMember.toString());
        if (curReleaseVol.compareTo(BigDecimal.ZERO) < 1) {
            logger.info("本次释放数量为0，不需释放:{}", commonMember.getId());
            return;
        }
        // 1、更新该会员的币的释放数量
        long count = commonMemberDao.updateMemberRelease(commonMember);
        if (count != 1) {
            throw new PlatException(Constants.OPERRATION_ERROR, "更新普通会员释放数量，解锁余量不够！[" + commonMember.toString() + "]");
        }
        // 2、记录该会员本周期释放日志
        Mk2PopularizeReleaseLog log = saveMemberReleaseLog(commonMember, rule.getType(), curReleaseVol, cycleTime, commonMember.getReleaseCycleRatio(), rule.getReleaseVersion(),"会员币释放");
    }

    /**
     * 异步打币到会员的BB账户
     * @param rule
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void commonMemberUpdateBBvolume(Mk2PopularizeMemberRule rule) {
        // 更新会员释放配置版本号
        long count = mk2PopularizeMemberRuleDao.updateMemberRule(rule);
        if (count != 1) {
            throw new PlatException(Constants.OPERRATION_ERROR, "更新普通会员释放版本号失败！");
        }
        // 批量更新会员的BB账户可用余量
        updateUserBBvolume(rule.getType(), rule.getReleaseVersion(), "会员释放");
    }

    /**
     * 落库到异步表
     * @param type
     * @param releaseVersion
     */
    private void updateUserBBvolume(String type, Long releaseVersion, String mark) {

        // 1、根据type类型和版本号查询释放日志
        List<Mk2PopularizeReleaseLog> releaseLogs = releaseLogDao.findByTypeAndVersion(type, releaseVersion);

        UserCoinVolumeEventEnum[] emst = {UserCoinVolumeEventEnum.ADD_VOLUME};//

        List<UserCoinVolumeBillDTO> listBbVolume = releaseLogs.stream().map(giveCoin -> {
            UserCoinVolumeBillDTO coinVolumeBill = new UserCoinVolumeBillDTO();
            coinVolumeBill.setCoinSymbol(giveCoin.getCoinSymbol());
            coinVolumeBill.setUserId(giveCoin.getUserId());
            coinVolumeBill.setRefKey(giveCoin.getId());
            coinVolumeBill.setForceLock(true);
            coinVolumeBill.setMark(giveCoin.getType());
            coinVolumeBill.setSource(mark);// 描述一下
            coinVolumeBill.setOpSign(emst);
            coinVolumeBill.setOpLockVolume(BigDecimal.ZERO);
            coinVolumeBill.setOpVolume(giveCoin.getReleaseVolume());
            coinVolumeBill.setPriority(1);// 优先级
            return coinVolumeBill;
        }).collect(Collectors.toList());

        // 插入到BB资产操作队列表中，异步给币
        if (CollectionUtils.isEmpty(listBbVolume)) {
            return;
        }
        int page = listBbVolume.size() % pageCount != 0 ? (listBbVolume.size() / pageCount + 1) : (listBbVolume.size() / pageCount);
        List<UserCoinVolumeBillDTO> pageList = null;
        for (int index = 1; index <= page; index++) {
            if (index == page) {
                pageList = listBbVolume.subList((index - 1) * pageCount, listBbVolume.size());
            } else {
                pageList = listBbVolume.subList((index - 1) * pageCount, index * pageCount);
            }
            userCoinVolumeBillService.batchInsert(pageList);
        }
    }

    /**
     * 校验会员的属性和释放规则配置是否匹配：1、校验是否周期是否开启 2、校验会员类型是否开启
     * @param commonMember
     * @param rule
     * @return
     */
    private boolean checkMemberInfoMatchRule(Mk2PopularizeCommonMember commonMember, Mk2PopularizeMemberRule rule) {
        boolean memberCycleIsOpen =  checkCycleIsOpen(commonMember.getReleaseCycle(), rule);
        boolean memberTypeIsOpen = false;
        if (StringUtils.isNotBlank(commonMember.getType()) && StringUtils.isNotBlank(rule.getReleaseType())) {
            String[] types = rule.getReleaseType().split(",");
            List<String> typeList = Arrays.asList(types);
            memberTypeIsOpen = typeList.contains(commonMember.getType());
        }
        logger.info("checkMemberInfoMatchRule会员ID：{}，memberCycleIsOpen：{}，memberTypeIsOpen：{}", commonMember.getId(), memberCycleIsOpen, memberTypeIsOpen);
        return memberCycleIsOpen && memberTypeIsOpen;
    }

    /**
     * 根据周期，判断该周期是否开启释放配置
     * @param cycle
     * @param rule
     * @return
     */
    private boolean checkCycleIsOpen(String cycle, Mk2PopularizeMemberRule rule) {
        if (DAY.equals(cycle) && "1".equals(rule.getReleaseDay())) {
            return true;
        }
        if (WEEK.equals(cycle) && "1".equals(rule.getReleaseWeek())) {
            return true;
        }
        if (MONTH.equals(cycle) && "1".equals(rule.getReleaseMonth())) {
            return true;
        }
        if (YEAR.equals(cycle) && "1".equals(rule.getReleaseYear())) {
            return true;
        }
        return false;
    }

    /**
     * 获取目标时间开始的周期第一天或最后一天
     * @param targetTime 目标时间
     * @param cycle 周期类型 1天2周3月4年
     * @param beginOrEndOrNull 周期第一天或最后一天或当期当天
     * @param minusCycle 周期
     * @return
     */
    private LocalDateTime getCycleTime(LocalDateTime targetTime, String cycle, String beginOrEndOrNull, int minusCycle) {//cycle: 1天2周3月4年
        // 天
        if (DAY.equals(cycle) && CYCLE_BEGIN.equals(beginOrEndOrNull)) {// 天开始
            return LocalDateTime.of(targetTime.minusDays(minusCycle).toLocalDate(), LocalTime.MIN);
        }
        if (DAY.equals(cycle) && CYCLE_END.equals(beginOrEndOrNull)) {// 天结束
            return LocalDateTime.of(targetTime.minusDays(minusCycle).toLocalDate(), LocalTime.MAX);
        }
        if (DAY.equals(cycle) && CYCLE_NULL.equals(beginOrEndOrNull)) {// 天当期
            return targetTime.minusDays(minusCycle);
        }
        // 周
        if (WEEK.equals(cycle) && CYCLE_BEGIN.equals(beginOrEndOrNull)) {// 周一
            return LocalDateTime.of(targetTime.minusWeeks(minusCycle).with(DayOfWeek.MONDAY).toLocalDate(), LocalTime.MIN);
        }
        if (WEEK.equals(cycle) && CYCLE_END.equals(beginOrEndOrNull)) {// 周日
            return LocalDateTime.of(targetTime.minusWeeks(minusCycle).with(DayOfWeek.SUNDAY).toLocalDate(), LocalTime.MIN);
        }
        if (WEEK.equals(cycle) && CYCLE_NULL.equals(beginOrEndOrNull)) {// 周当期
            return targetTime.minusWeeks(minusCycle);
        }
        // 月
        if (MONTH.equals(cycle) && CYCLE_BEGIN.equals(beginOrEndOrNull)) {// 月第一天
            return LocalDateTime.of(targetTime.minusMonths(minusCycle).with(TemporalAdjusters.firstDayOfMonth()).toLocalDate(), LocalTime.MIN);
        }
        if (MONTH.equals(cycle) && CYCLE_END.equals(beginOrEndOrNull)) {// 月最后一天
            return LocalDateTime.of(targetTime.minusMonths(minusCycle).with(TemporalAdjusters.lastDayOfMonth()).toLocalDate(), LocalTime.MIN);
        }
        if (MONTH.equals(cycle) && CYCLE_NULL.equals(beginOrEndOrNull)) {// 月当期
            return targetTime.minusMonths(minusCycle);
        }
        // 年
        if (YEAR.equals(cycle) && CYCLE_BEGIN.equals(beginOrEndOrNull)) {// 年第一天
            return LocalDateTime.of(targetTime.minusYears(minusCycle).with(TemporalAdjusters.firstDayOfYear()).toLocalDate(), LocalTime.MIN);
        }
        if (YEAR.equals(cycle) && CYCLE_END.equals(beginOrEndOrNull)) {// 年最后一天
            return LocalDateTime.of(targetTime.minusYears(minusCycle).with(TemporalAdjusters.lastDayOfYear()).toLocalDate(), LocalTime.MIN);
        }
        if (YEAR.equals(cycle) && CYCLE_NULL.equals(beginOrEndOrNull)) {// 年当期
            return targetTime.minusYears(minusCycle);
        }
        logger.error("获取周期起止时间错误，cycle:{}, beginOrEnd:{}, minusCycle:{}", cycle, beginOrEndOrNull, minusCycle);
        return null;
    }

    /**
     * 计算targetTime是cycle的第几天
     * @param targetTime
     * @param cycle
     * @return
     */
    private int getCycleOrder(LocalDateTime targetTime, String cycle) {
        if (DAY.equals(cycle)) {
            return 1;
        }
        if (WEEK.equals(cycle)) {
            return targetTime.getDayOfWeek().getValue();
        }
        if (MONTH.equals(cycle)) {
            return targetTime.getDayOfMonth();
        }
        if (YEAR.equals(cycle)) {
            return targetTime.getDayOfYear();
        }
        return -1;
    }

    /**
     * 保存释放记录
     * @param member
     * @param type
     * @param releaseVol
     * @param cycleDate
     * @param ratio 释放比例
     * @param releaseVersion
     * @param remark
     * @return
     */
    private Mk2PopularizeReleaseLog saveMemberReleaseLog(Mk2PopularizeMember member, String type, BigDecimal releaseVol, LocalDateTime cycleDate, BigDecimal ratio, Long releaseVersion, String remark) {
        Mk2PopularizeReleaseLog log = new Mk2PopularizeReleaseLog();
        log.setId(SnowFlake.createSnowFlake().nextIdString());
        log.setRelationId(member.getId());
        log.setType(type);
        log.setUserId(member.getUserId());
        log.setMail(member.getMail());
        log.setMobile(member.getMobile());
        log.setCoinId(member.getCoinId());
        log.setCoinSymbol(member.getCoinSymbol());
        log.setReleaseVolume(releaseVol);
        log.setReleaseCycleDate(cycleDate);
        log.setReleaseStatus("1");
        log.setReleaseVersion(releaseVersion);
        log.setReleaseSource("0"); // 0表示task释放，1表示手动释放
        log.setReleaseCycleRatio(ratio);
        log.setRemark(remark);
        releaseLogDao.insert(log);
        return log;
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveTaskLog(LocalDateTime taskDate, BigDecimal releaseVolume) {
        LocalDateTime dbTaskDate = releaseLogDao.findReleaseTaskByDate(taskDate);
        if (dbTaskDate == null) {
            releaseLogDao.insertReleaseTaskLog(taskDate, releaseVolume);
        }
    }
}