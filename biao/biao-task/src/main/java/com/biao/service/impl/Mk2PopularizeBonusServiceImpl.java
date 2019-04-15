package com.biao.service.impl;

import com.biao.constant.Constants;
import com.biao.entity.PlatUser;
import com.biao.entity.UserCoinVolume;
import com.biao.entity.mk2.*;
import com.biao.entity.mkcommon.MkCommonUserCoinFee;
import com.biao.exception.PlatException;
import com.biao.mapper.CoinDao;
import com.biao.mapper.PlatUserDao;
import com.biao.mapper.UserCoinVolumeDao;
import com.biao.mapper.mk2.*;
import com.biao.mapper.mkcommon.MkCommonUserCoinFeeDao;
import com.biao.redis.RedisCacheManager;
import com.biao.service.Mk2PopularizeBonusService;
import com.biao.service.MkCommonService;
import com.biao.util.SnowFlake;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

//import com.biao.service.UserCoinVolumeService;

@Service
public class Mk2PopularizeBonusServiceImpl implements Mk2PopularizeBonusService {

    private Logger logger = LoggerFactory.getLogger(Mk2PopularizeBonusServiceImpl.class);

    @Autowired
    private MkCommonService mkCommonService;

    @Autowired
    private UserCoinVolumeDao userCoinVolumeDao;

    @Autowired
    private MkCommonUserCoinFeeDao mkCommonUserCoinFeeDao;

    @Autowired
    private Mk2PopularizeMemberRuleDao mk2PopularizeMemberRuleDao;

    @Autowired
    private Mk2PopularizeBonusAccountDao mk2PopularizeBonusAccountDao;

    @Autowired
    private Mk2PopularizeBonusAccountLogDao mk2PopularizeBonusAccountLogDao;

    @Autowired
    private Mk2PopularizeAreaMemberDao mk2PopularizeAreaMemberDao;

    @Autowired
    private Mk2PopularizeNodalMemberDao mk2PopularizeNodalMemberDao;

    @Autowired
    private PlatUserDao platUserDao;

    @Autowired
    private Mk2PopularizeBonusMemberLogDao mk2PopularizeBonusMemberLogDao;

//    @Autowired
//    private UserCoinVolumeService userCoinVolumeService;

    @Autowired
    private Mk2PopularizeBonusTaskLogDao mk2PopularizeBonusTaskLogDao;

    @Autowired
    private Mk2PopularizeBonusService mk2PopularizeBonusService;

    @Autowired
    private CoinDao coinDao;

    @Autowired
    RedisCacheManager redisCacheManager;

    private static final String BONUS_COIN_SYMBOL = "USDT"; // 分红币种,默认USDT

    private static String BONUS_COIN_ID = "";// 分红币种ID,默认USDT

    private static final BigDecimal PERCENT = new BigDecimal(100);

    private static final int BONUS_SCALE = 8; // 小数位

    private static final int pageCount = 100;

    @Transactional
    public void doPopularizeBonusTask() {

        LocalDateTime curDateTime = LocalDateTime.now();
        LocalDateTime lastDateBeginTime = LocalDateTime.of(curDateTime.minusDays(1).toLocalDate(), LocalTime.MIN);// 昨天0点0分0秒
        LocalDateTime lastDateEndTime = LocalDateTime.of(curDateTime.minusDays(1).toLocalDate(), LocalTime.MAX);// 昨天23点59分59秒
        BONUS_COIN_ID = coinDao.findByName(BONUS_COIN_SYMBOL).getId(); // 分红币种ID,默认USDT
        Mk2PopularizeBonusTaskLog taskLog = mk2PopularizeBonusTaskLogDao.findLatelyBonusTask();
        if (taskLog != null) {
            lastDateBeginTime = taskLog.getEndDate();
        }
        if (lastDateBeginTime.isAfter(lastDateEndTime) || lastDateBeginTime.isEqual(lastDateEndTime)) {
            mk2PopularizeBonusService.saveTaskResult("0", BigDecimal.ZERO, BONUS_COIN_ID, BONUS_COIN_SYMBOL, lastDateBeginTime, lastDateEndTime, "该时间段分红已经执行过");
            return;
        }
        UserCoinVolume userCoinVolume = null;
        long lockStatus = 0L;
        try {
            Mk2PopularizeBonusAccount bonusAccount = mk2PopularizeBonusAccountDao.findByTpye("1");// 分红账户
            if (bonusAccount == null) {
                throw new PlatException(Constants.PARAM_ERROR, "分红账户未设置！");
            }
            userCoinVolume = userCoinVolumeDao.findByUserIdAndCoinSymbol(bonusAccount.getUserId(), BONUS_COIN_SYMBOL);
            if (userCoinVolume == null || userCoinVolume.getVolume().compareTo(BigDecimal.ZERO) != 1) {
                throw new PlatException(Constants.PARAM_ERROR, "分红账户USDT数量为空！[" + bonusAccount.getUserId() + "]");
            }
            // 锁分红账户资产
//            lockStatus = userCoinVolumeService.addLockVolume(userCoinVolume.getUserId(), userCoinVolume.getCoinSymbol(), userCoinVolume.getVolume(), true);
//            if (lockStatus != 1) {
//                throw new PlatException(Constants.PARAM_ERROR, "分红账户USDT锁失败！[" + userCoinVolume.getUserId() + "]");
//            }
            // 用户分红：节点人，区域合伙人（固定，推荐，手机号）
            logger.info("开始分红==============" + lastDateBeginTime);
            BigDecimal bonusVolume = doPopularizeBonus(lastDateBeginTime, lastDateEndTime, userCoinVolume);
            logger.info("结束分红==============" + lastDateBeginTime);
            // 所有分红完毕后，减分红账户用户的锁定数量
//            long count = userCoinVolumeService.subtractLockVolume(userCoinVolume.getUserId(), userCoinVolume.getCoinSymbol(), userCoinVolume.getVolume(), false);
//            if (count != 1) {
//                throw new PlatException(Constants.PARAM_ERROR, "分红账户USDT扣减失败！[" + bonusAccount.getUserId() + "]");
//            }
            mk2PopularizeBonusService.saveTaskResult("1", bonusVolume, BONUS_COIN_ID, BONUS_COIN_SYMBOL, lastDateBeginTime, lastDateEndTime, "分红成功");
        } catch (PlatException e) {
            logger.error("分红TASK执行失败", e);
            String msg = e.getMsg().length() > 200 ? e.getMsg().substring(0, 200) : e.getMsg();
            mk2PopularizeBonusService.saveTaskResult("0", BigDecimal.ZERO, BONUS_COIN_ID, BONUS_COIN_SYMBOL, lastDateBeginTime, lastDateEndTime, msg);
            doRollBackLockVolume(userCoinVolume.getUserId(), userCoinVolume.getVolume(), lockStatus);// 执行失败，分红账户解锁
            throw new PlatException(Constants.PARAM_ERROR, "分红TASK执行失败");
        } catch (Exception e) {
            logger.error("分红TASK执行失败", e);
            String msg = e.toString();
            msg = msg.length() > 200 ? msg.substring(0, 200) : msg;
            mk2PopularizeBonusService.saveTaskResult("0", BigDecimal.ZERO, BONUS_COIN_ID, BONUS_COIN_SYMBOL, lastDateBeginTime, lastDateEndTime, msg);
            doRollBackLockVolume(userCoinVolume.getUserId(), userCoinVolume.getVolume(), lockStatus);// 执行失败，分红账户解锁
            throw new PlatException(Constants.PARAM_ERROR, "分红TASK执行失败");
        }

    }

    private void doRollBackLockVolume(String userId, BigDecimal volume, long lockStatus) {
//        try {
//            if (lockStatus == 1L) {
//                userCoinVolumeService.subtractLockVolume(userId, BONUS_COIN_SYMBOL, volume, true);
//                redisCacheManager.cleanUserCoinVolume(userId, BONUS_COIN_SYMBOL);
//            }
//        } catch (Exception e) {
//            logger.error("分红账户clean失败", e);
//            throw new PlatException(Constants.PARAM_ERROR, "分红账户clean失败");
//        }
    }

    private BigDecimal doPopularizeBonus(LocalDateTime beginDate, LocalDateTime endDate, UserCoinVolume userCoinVolume) {

        // 分红总量
        BigDecimal bonusTotalVol = userCoinVolume.getVolume(); // 分红总量

        BigDecimal sumTotalFee = mkCommonUserCoinFeeDao.sumDateUserTotal(beginDate, endDate);// 分红开始结束时间内平台所有用户手续费USDT汇总
        BigDecimal lastVolume = BigDecimal.ZERO;// 各类分红最后剩余的渣渣留给营运商数量

        Mk2PopularizeBonusAccount agentAccount = mk2PopularizeBonusAccountDao.findByTpye("2");// 营运商账户
        List<Mk2PopularizeAreaMember> areaMembers = mk2PopularizeAreaMemberDao.findAllAreaMember();
        // 1、区域合伙人分红
        Mk2PopularizeMemberRule areaMemberRule = mk2PopularizeMemberRuleDao.findByType("3");// 区域合伙人分红规则
        // 1.1、区域合伙人固定分红总量 = 分红总量 * （区域固定分红比例 / 100）* 区域合伙人总量
        BigDecimal areaFixedTotalVolume = bonusTotalVol.multiply(areaMemberRule.getBonusRatio().divide(PERCENT)).multiply(new BigDecimal(areaMemberRule.getTotalMember())).setScale(BONUS_SCALE, BigDecimal.ROUND_DOWN);
        lastVolume = dofixedBonus2AreaMember(areaMemberRule, areaFixedTotalVolume, beginDate, endDate, areaMembers);// 区域合伙人固定分红
        doBonus2AgentAccount(agentAccount, lastVolume, beginDate, endDate, "区域合伙人固定分红后的渣渣扔给营运商");

        // 1.2、区域合伙人推荐分红总量 = 分红总量 * （区域推荐分红比例 / 100）;
        BigDecimal areaReferTotalVolume = bonusTotalVol.multiply(areaMemberRule.getReferBonusRatio().divide(PERCENT)).setScale(BONUS_SCALE, BigDecimal.ROUND_DOWN);
        lastVolume = doReferBonus2AreaMember(sumTotalFee, areaReferTotalVolume, beginDate, endDate); // 区域合伙人推荐分红
        doBonus2AgentAccount(agentAccount, lastVolume, beginDate, endDate, "区域合伙人推荐分红后的渣渣扔给营运商");

        // 1.3、区域合伙人手机分红总量 = 分红总量 * （区域手机分红比例 / 100）;
        BigDecimal areaPhoneTotalVolume = bonusTotalVol.multiply(areaMemberRule.getPhoneBonusRatio().divide(PERCENT)).setScale(BONUS_SCALE, BigDecimal.ROUND_DOWN);
        lastVolume = doPhoneBonus2AreaMember(sumTotalFee, areaPhoneTotalVolume, beginDate, endDate, areaMembers); // 区域合伙人手机号分红
        doBonus2AgentAccount(agentAccount, lastVolume, beginDate, endDate, "区域合伙人手机号分红后的渣渣扔给营运商");

        // 2、节点人分红
        Mk2PopularizeMemberRule nodalMemberRule = mk2PopularizeMemberRuleDao.findByType("2");// 节点人分红规则
        // 节点人分红总量 = 分红总量 * （节点固定分红比例 / 100） * 节点人总量
        BigDecimal nodalTotalVolume = bonusTotalVol.multiply(nodalMemberRule.getBonusRatio().divide(PERCENT)).multiply(new BigDecimal(nodalMemberRule.getTotalMember())).setScale(BONUS_SCALE, BigDecimal.ROUND_DOWN);
        lastVolume = doBonusNodalMember(nodalMemberRule, nodalTotalVolume, beginDate, endDate);
        doBonus2AgentAccount(agentAccount, lastVolume, beginDate, endDate, "节点人分红后的渣渣扔给营运商");

        // 3、普通会员分红
        Mk2PopularizeMemberRule commonMemberRule = mk2PopularizeMemberRuleDao.findByType("1");// 普通会员分红规则
        //  普通会员分红总量 = 分红总量 * （普通会员分红比例 / 100）
        BigDecimal commonTotalVolume = bonusTotalVol.multiply(commonMemberRule.getBonusRatio().divide(PERCENT)).setScale(BONUS_SCALE, BigDecimal.ROUND_DOWN);
        lastVolume = doBonusCommonMember(sumTotalFee, commonTotalVolume, beginDate, endDate);
        doBonus2AgentAccount(agentAccount, lastVolume, beginDate, endDate, "普通会员分红后的渣渣扔给营运商");

        // 4、营运商分红
        lastVolume = bonusTotalVol.subtract(areaFixedTotalVolume).subtract(areaReferTotalVolume).subtract(areaPhoneTotalVolume).subtract(nodalTotalVolume).subtract(commonTotalVolume).setScale(8, BigDecimal.ROUND_DOWN);
        doBonus2AgentAccount(agentAccount, lastVolume, beginDate, endDate, "所有会员（普通/节点人/区域合伙人）分红完后剩余的营运商利润。");

        return bonusTotalVol;// 本次task分红总量
    }

    private void doBonus2AgentAccount(Mk2PopularizeBonusAccount agentAccount, BigDecimal lastVolume, LocalDateTime beginDate, LocalDateTime endDate, String remark) {

        if (BigDecimal.ZERO.compareTo(lastVolume) == 1) {
            throw new PlatException(Constants.OPERRATION_ERROR, remark + "更新营运商币资产失败！[userId:" + agentAccount.getUserId() + "],[volume:" + lastVolume + "]");
        }
        saveToAgentAccountLog(agentAccount, lastVolume, beginDate, endDate, remark);
        // 更新用户币资产
        updateUserCoinVolume(lastVolume, agentAccount.getUserId(), "更新营运商币资产失败！[userId:" + agentAccount.getUserId() + "],[volume:" + lastVolume + "]");
    }

    private void saveToAgentAccountLog(Mk2PopularizeBonusAccount agentAccount, BigDecimal incomeVol, LocalDateTime beginDate, LocalDateTime endDate, String remark) {
        Mk2PopularizeBonusAccountLog log = new Mk2PopularizeBonusAccountLog();
        log.setId(SnowFlake.createSnowFlake().nextIdString());
        log.setUserId(agentAccount.getUserId());
        log.setMail(agentAccount.getMail());
        log.setMobile(agentAccount.getMobile());
        log.setIdCard(agentAccount.getIdCard());
        log.setBonusDateBegin(beginDate);
        log.setBonusDateEnd(endDate);
        log.setCoinId(BONUS_COIN_ID);
        log.setCoinSymbol(BONUS_COIN_SYMBOL);
        log.setIncomeVolume(incomeVol);
        UserCoinVolume userCoinVolume = userCoinVolumeDao.findByUserIdAndCoinId(agentAccount.getUserId(), BONUS_COIN_ID);
        log.setBeforIncomeVolume(userCoinVolume != null ? userCoinVolume.getVolume() : BigDecimal.ZERO);
        log.setStatus("1");
        log.setRemark(remark);
        mk2PopularizeBonusAccountLogDao.insert(log);
    }

    /**
     * 区域合伙人固定分红
     *
     * @param areaTotalVolume
     * @return
     */
    private BigDecimal dofixedBonus2AreaMember(Mk2PopularizeMemberRule areaMemberRule, BigDecimal areaTotalVolume, LocalDateTime beginDate, LocalDateTime endDate, List<Mk2PopularizeAreaMember> areaMembers) {

        BigDecimal lastVolume = areaTotalVolume;
        BigDecimal perMemberVol = areaTotalVolume.divide(new BigDecimal(areaMemberRule.getTotalMember()), BONUS_SCALE, BigDecimal.ROUND_DOWN).setScale(BONUS_SCALE, BigDecimal.ROUND_DOWN);
        if (CollectionUtils.isNotEmpty(areaMembers)) {
            for (Mk2PopularizeAreaMember member : areaMembers) {
                doBonus2Shareholder(member, perMemberVol, beginDate, endDate, "固定[总量：" + areaTotalVolume + "]", null, null, "1");
                lastVolume = lastVolume.subtract(perMemberVol);
            }
        }
        if (lastVolume.compareTo(BigDecimal.ZERO) == -1) {
            throw new PlatException(Constants.OPERRATION_ERROR, "区域合伙人固定分红失败，分红超出可分总量");
        }
        return lastVolume;
    }

    /**
     * 区域合伙人推荐分红
     *
     * @param areaTotalVolume
     * @return
     */
    private BigDecimal doReferBonus2AreaMember(BigDecimal sumTotalFee, BigDecimal areaTotalVolume, LocalDateTime beginDate, LocalDateTime endDate) {

        List<String> areaMemberUserIds = mk2PopularizeAreaMemberDao.findDistinctAreaMembers();// 所有区域合伙人和股东的USERID去重
        BigDecimal lastVolume = areaTotalVolume;
        MkCommonUserCoinFee userCoinFee = null;
        BigDecimal curUserVol = null;
        List<Mk2PopularizeBonusMemberLog> memberLogs = new ArrayList<Mk2PopularizeBonusMemberLog>();// 送币流水日志集
        Mk2PopularizeBonusMemberLog log = null;// 送币流水日志
        if (CollectionUtils.isNotEmpty(areaMemberUserIds)) {
            for (String userId : areaMemberUserIds) {
                mkCommonUserCoinFeeDao.cleanMk2PopularizeAreaMemberTmp(); // 清空临时表mk2_popularize_area_member_tmp
                mkCommonUserCoinFeeDao.cleanmkCommonUserRelationSubTemp(); // 清空临时表mk_common_user_relation_bonus
                mkCommonUserCoinFeeDao.insertSubNotCountAreaMemberUserId(userId); // 将当前userid的下级的合伙人userid插入到临时表清空临时表mk2_popularize_area_member_tmp
                mkCommonUserCoinFeeDao.insertNotCountSubUserId(); // 将合伙人的推荐树下子userid插入临时表清空临时表mk_common_user_relation_bonus
                userCoinFee = mkCommonUserCoinFeeDao.sumAreaMemberReferUsdt(userId, beginDate, endDate);
                if (userCoinFee != null && userCoinFee.getExUsdtVol() != null && userCoinFee.getExUsdtVol().compareTo(BigDecimal.ZERO) == 1) {
                    curUserVol = userCoinFee.getExUsdtVol().divide(sumTotalFee, BONUS_SCALE, BigDecimal.ROUND_DOWN).multiply(areaTotalVolume).setScale(BONUS_SCALE, BigDecimal.ROUND_DOWN);
                    // 更新区域合伙人推荐分红币资产
                    updateUserCoinVolume(curUserVol, userCoinFee.getUserId(), "更新区域合伙人推荐分红币资产失败！[userId:" + userId + "],[volume:" + curUserVol + "]");
                    log = getMemberLog("3", userId, null, areaTotalVolume, curUserVol, beginDate, endDate, "[推荐总量：" + areaTotalVolume + "][合伙人/股东：" + curUserVol + "]推荐分红", null, userCoinFee.getExUsdtVol(), sumTotalFee, "3");
                    memberLogs.add(log);
                } else {
                    curUserVol = BigDecimal.ZERO;
                }
                lastVolume = lastVolume.subtract(curUserVol);
            }
            if (lastVolume.compareTo(BigDecimal.ZERO) == -1) {
                throw new PlatException(Constants.OPERRATION_ERROR, "合伙人推荐分红比例大于100！");
            }

        }
        saveToMemberLogBatch(memberLogs);// 批量保存送币流水日志
        return lastVolume;
    }

    /**
     * 区域合伙人手机区域分红
     *
     * @param areaTotalVolume
     * @return
     */
    private BigDecimal doPhoneBonus2AreaMember(BigDecimal sumTotalFee, BigDecimal areaTotalVolume, LocalDateTime beginDate, LocalDateTime endDate, List<Mk2PopularizeAreaMember> areaMembers) {

        BigDecimal lastVolume = areaTotalVolume;
        MkCommonUserCoinFee userCoinFee = null;
        BigDecimal curUserVol = null;
        if (CollectionUtils.isNotEmpty(areaMembers)) {
            for (Mk2PopularizeAreaMember areaMember : areaMembers) {
                userCoinFee = mkCommonUserCoinFeeDao.sumAreaMemberPhoneUsdt(areaMember.getId(), beginDate, endDate);
                if (userCoinFee != null && userCoinFee.getExUsdtVol() != null && userCoinFee.getExUsdtVol().compareTo(BigDecimal.ZERO) == 1) {
                    curUserVol = userCoinFee.getExUsdtVol().divide(sumTotalFee, BONUS_SCALE, BigDecimal.ROUND_DOWN).multiply(areaTotalVolume).setScale(BONUS_SCALE, BigDecimal.ROUND_DOWN);
                    doBonus2Shareholder(areaMember, curUserVol, beginDate, endDate, "号码归属地[总量：" + areaTotalVolume + "]", userCoinFee.getExUsdtVol(), sumTotalFee, "2");
                } else {
                    curUserVol = BigDecimal.ZERO;
                }
                lastVolume = lastVolume.subtract(curUserVol);
            }
        }
        return lastVolume;
    }

    /**
     * 区域合伙人及其下属股东分红
     *
     * @param areaMember
     * @param areaMemberVolume
     * @param beginDate
     * @param endDate
     * @param remark
     */
    private void doBonus2Shareholder(Mk2PopularizeAreaMember areaMember, BigDecimal areaMemberVolume, LocalDateTime beginDate, LocalDateTime endDate, String remark, BigDecimal joinVolume, BigDecimal joinTotalVolume, String areaBonusType) {
        List<Mk2PopularizeAreaMember> shareholders = mk2PopularizeAreaMemberDao.findShareholderByAreaMemberId(areaMember.getId());
        BigDecimal lastVolume = areaMemberVolume;
        BigDecimal curUserVol = null;
        List<Mk2PopularizeBonusMemberLog> memberLogs = new ArrayList<Mk2PopularizeBonusMemberLog>();// 送币流水日志集
        Mk2PopularizeBonusMemberLog log = null;// 送币流水日志
        // 股东分红
        if (CollectionUtils.isNotEmpty(shareholders)) {
            for (Mk2PopularizeAreaMember shareholder : shareholders) {
                curUserVol = shareholder.getRatio().divide(PERCENT).multiply(areaMemberVolume).setScale(BONUS_SCALE, BigDecimal.ROUND_DOWN);
                log = getMemberLog("4", shareholder.getUserId(), shareholder.getId(), areaMemberVolume, curUserVol, beginDate, endDate, remark + "[区域总量：" + areaMemberVolume + "][股东：" + curUserVol + "][" + areaMember.getAreaName() + "]股东分红", areaMember.getAreaName(), null, null, areaBonusType);
                memberLogs.add(log);
                // 更新区域合伙人股东币资产
                updateUserCoinVolume(curUserVol, shareholder.getUserId(), "更新区域合伙人股东" + remark + "分红币资产失败！[userId:" + shareholder.getUserId() + "],[volume:" + curUserVol + "]");
                lastVolume = lastVolume.subtract(curUserVol);
            }
        }
        // 区域合伙人分红
        log = getMemberLog("3", areaMember.getUserId(), areaMember.getId(), areaMemberVolume, lastVolume, beginDate, endDate, remark + "[区域总量：" + areaMemberVolume + "][合伙人：" + lastVolume + "][" + areaMember.getAreaName() + "]合伙人分红", areaMember.getAreaName(), joinVolume, joinTotalVolume, areaBonusType);
        memberLogs.add(log);
        saveToMemberLogBatch(memberLogs);// 批量保存送币流水日志
        if (BigDecimal.ZERO.compareTo(lastVolume) == 1) {
            return;
        }
        // 更新区域合伙人币资产
        updateUserCoinVolume(lastVolume, areaMember.getUserId(), "更新区域合伙人" + remark + "分红币资产失败！[userId:" + areaMember.getUserId() + "],[volume:" + lastVolume + "]");
    }

    /**
     * 节点人分红
     *
     * @param nodalTotalVolume
     * @return
     */
    private BigDecimal doBonusNodalMember(Mk2PopularizeMemberRule nodalMemberRule, BigDecimal nodalTotalVolume, LocalDateTime beginDate, LocalDateTime endDate) {

        BigDecimal lastVolume = nodalTotalVolume;
        BigDecimal perMemberVol = nodalTotalVolume.divide(new BigDecimal(nodalMemberRule.getTotalMember()), BONUS_SCALE, BigDecimal.ROUND_DOWN).setScale(BONUS_SCALE, BigDecimal.ROUND_DOWN);
        List<Mk2PopularizeNodalMember> nodalMembers = mk2PopularizeNodalMemberDao.findAllNodalMember();

        List<Mk2PopularizeBonusMemberLog> memberLogs = new ArrayList<Mk2PopularizeBonusMemberLog>();// 送币流水日志集
        Mk2PopularizeBonusMemberLog log = null;// 送币流水日志

        if (CollectionUtils.isNotEmpty(nodalMembers)) {
            for (Mk2PopularizeNodalMember member : nodalMembers) {
                if (perMemberVol.compareTo(BigDecimal.ZERO) == 1) {
                    log = getMemberLog("2", member.getUserId(), member.getId(), nodalTotalVolume, perMemberVol, beginDate, endDate, "[总量：" + nodalTotalVolume + "][节点人：" + perMemberVol + "]节点人分红", null, null, null, null);
                    memberLogs.add(log);
                    // 更新节点人币资产
                    updateUserCoinVolume(perMemberVol, member.getUserId(), "更新节点人币资产失败！[userId:" + member.getUserId() + "],[volume:" + perMemberVol + "]");
                }
                lastVolume = lastVolume.subtract(perMemberVol);
            }
        }
        saveToMemberLogBatch(memberLogs);// 批量保存送币流水日志
        return lastVolume;
    }

    /**
     * 普通用户分红
     *
     * @param commonTotalVolume
     * @return
     */
    private BigDecimal doBonusCommonMember(BigDecimal sumTotalFee, BigDecimal commonTotalVolume, LocalDateTime beginDate, LocalDateTime endDate) {

        BigDecimal lastVolume = commonTotalVolume;
        int maxDeth = mkCommonUserCoinFeeDao.countMaxDeth();

        List<Mk2PopularizeBonusMemberLog> memberLogs = new ArrayList<Mk2PopularizeBonusMemberLog>();// 送币流水日志集
        Mk2PopularizeBonusMemberLog log = null;// 送币流水日志
        List<MkCommonUserCoinFee> commonUserCoinFees = null;
        BigDecimal curUserVol = null;
        for (int index = 1; index <= maxDeth; index++) {
            commonUserCoinFees = mkCommonUserCoinFeeDao.sumCommonMemberUsdtByDeth(beginDate, endDate, index);// 根据树的深度分层统计，避免数据过大查询慢
            if (CollectionUtils.isNotEmpty(commonUserCoinFees)) {
                for (MkCommonUserCoinFee userCoinFee : commonUserCoinFees) {
                    if (userCoinFee.getExUsdtVol() == null) {
                        continue;
                    }
                    curUserVol = userCoinFee.getExUsdtVol().divide(sumTotalFee, BONUS_SCALE, BigDecimal.ROUND_DOWN).multiply(commonTotalVolume).setScale(BONUS_SCALE, BigDecimal.ROUND_DOWN);
                    if (curUserVol.compareTo(BigDecimal.ZERO) == 1) {
                        log = getMemberLog("1", userCoinFee.getUserId(), userCoinFee.getId(), commonTotalVolume, curUserVol, beginDate, endDate, "[总量：" + commonTotalVolume + "][普通用户：" + curUserVol + "]普通用户分红", null, userCoinFee.getExUsdtVol(), sumTotalFee, null);
                        memberLogs.add(log);
                        // 更新普通用户币资产
                        updateUserCoinVolume(curUserVol, userCoinFee.getUserId(), "更新普通用户币资产失败！[userId:" + userCoinFee.getUserId() + "],[volume:" + curUserVol + "]");
                    }
                    lastVolume = lastVolume.subtract(curUserVol);
                }
            }
        }
        saveToMemberLogBatch(memberLogs);// 批量保存送币流水日志
        // 返回剩余
        return lastVolume;
    }

    private void saveToMemberLogBatch(List<Mk2PopularizeBonusMemberLog> memberLogs) {
        int page = memberLogs.size() % pageCount != 0 ? (memberLogs.size() / pageCount + 1) : (memberLogs.size() / pageCount);
        List<Mk2PopularizeBonusMemberLog> pageMemberLog = null;
        for (int index = 1; index <= page; index++) {
            if (index == page) {
                pageMemberLog = memberLogs.subList((index - 1) * pageCount, memberLogs.size());
            } else {
                pageMemberLog = memberLogs.subList((index - 1) * pageCount, index * pageCount);
            }
            mk2PopularizeBonusMemberLogDao.insertBatch(pageMemberLog);
        }
    }

    private Mk2PopularizeBonusMemberLog getMemberLog(String type, String userId, String relationId, BigDecimal totalVol, BigDecimal incomeVol, LocalDateTime beginDate, LocalDateTime endDate, String remark, String areaName, BigDecimal joinVolume, BigDecimal joinTotalVolume, String areaBonusType) {
        PlatUser platUser = platUserDao.findById(userId);
        if (platUser == null) {
            logger.error("***********************************************************" + userId);
        }
        Mk2PopularizeBonusMemberLog log = new Mk2PopularizeBonusMemberLog();
        log.setId(SnowFlake.createSnowFlake().nextIdString());
        log.setType(type);
        log.setRelationId(relationId);
        log.setUserId(platUser.getId());
        log.setMail(platUser.getMail());
        log.setMobile(platUser.getMobile());
        log.setCoinId(BONUS_COIN_ID);
        log.setCoinSymbol(BONUS_COIN_SYMBOL);
        log.setTotalVolume(totalVol);
        log.setIncomeVolume(incomeVol);
        log.setAreaName(areaName);
        log.setJoinVolume(joinVolume);
        log.setJoinTotalVolume(joinTotalVolume);
        log.setAreaBonusType(areaBonusType);
        UserCoinVolume userCoinVolume = userCoinVolumeDao.findByUserIdAndCoinId(userId, BONUS_COIN_ID);
        log.setBeforIncomeVolume(userCoinVolume != null ? userCoinVolume.getVolume() : BigDecimal.ZERO);
        log.setBonusDateBegin(beginDate);
        log.setBonusDateEnd(endDate);
        log.setRemark(remark);
        return log;
//        mk2PopularizeBonusMemberLogDao.insert(log);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveTaskResult(String status, BigDecimal bonusVolume, String coinId, String coinSymbol, LocalDateTime beginDate, LocalDateTime endDate, String remark) {
        Mk2PopularizeBonusTaskLog log = new Mk2PopularizeBonusTaskLog();
        log.setId(SnowFlake.createSnowFlake().nextIdString());
        log.setStatus(status);
        log.setBonusVolume(bonusVolume);
        log.setCoinId(coinId);
        log.setCoinSymbol(coinSymbol);
        log.setBeginDate(beginDate);
        log.setEndDate(endDate);
        log.setRemark(remark);
        mk2PopularizeBonusTaskLogDao.insert(log);
    }

    /**
     * 更新用户资产
     *
     * @param income
     * @param userId
     * @param errorMsg
     */
    private void updateUserCoinVolume(BigDecimal income, String userId, String errorMsg) {
//        long count = userCoinVolumeService.updateIncome(null, income, userId, BONUS_COIN_SYMBOL);
//        redisCacheManager.cleanUserCoinVolume(userId, BONUS_COIN_SYMBOL);
//        if (count != 1) {
//            throw new PlatException(Constants.OPERRATION_ERROR, errorMsg);
//        }
    }

}
