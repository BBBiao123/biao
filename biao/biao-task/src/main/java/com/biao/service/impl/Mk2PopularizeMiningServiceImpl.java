package com.biao.service.impl;

import com.biao.constant.Constants;
import com.biao.entity.*;
import com.biao.entity.mk2.*;
import com.biao.enums.UserCoinVolumeEventEnum;
import com.biao.exception.PlatException;
import com.biao.mapper.*;
import com.biao.mapper.mk2.*;
import com.biao.neo4j.entity.Neo4jPlatUser;
import com.biao.neo4j.service.Neo4jPlatUserService;
import com.biao.pojo.UserCoinVolumeBillDTO;
import com.biao.service.Mk2PopularizeMiningService;
import com.biao.service.MkMiningRuleTaskService;
import com.biao.service.UserCoinVolumeBillService;
import com.biao.util.SnowFlake;
import com.biao.util.SuperBookAddrUtil;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service("MkMiningRuleTaskService_2")
public class Mk2PopularizeMiningServiceImpl implements Mk2PopularizeMiningService, MkMiningRuleTaskService {

    private Logger logger = LoggerFactory.getLogger(Mk2PopularizeMiningServiceImpl.class);

    @Autowired
    private Mk2PopularizeMiningUserTmpDao mk2PopularizeMiningUserTmpDao;

    @Autowired
    private Mk2PopularizeMiningGiveCoinLogDao mk2PopularizeMiningGiveCoinLogDao;

    @Autowired
    private Mk2PopularizeMiningConfDao mk2PopularizeMiningConfDao;

    @Autowired
    private PlatUserDao platUserDao;

    @Autowired
    private MkNeo4jUserDao mkNeo4jUserDao;

    @Autowired
    private Mk2PopularizeMiningTaskLogDao mk2PopularizeMiningTaskLogDao;

    @Autowired
    private SuperBookDao superBookDao;

    @Autowired
    private SuperBookConfDao superBookConfDao;

    @Autowired
    private DestroyAccountLogDao destroyAccountLogDao;

    @Autowired
    private SuperCoinVolumeConfDao superCoinVolumeConfDao;

    @Autowired
    private Mk2PopularizeMiningUserTmpBakDao mk2PopularizeMiningUserTmpBakDao;

    @Autowired
    private Mk2PopularizeMiningService mk2PopularizeMiningService;

    @Autowired
    private Neo4jPlatUserService neo4jPlatUserService;

    @Autowired
    private UserCoinVolumeBillService userCoinVolumeBillService;

    private static final int pageCount = 200;

    private static final int neo4jPageCount = 500; // neo4j批量提交每批次提交数量

    public static final String TYPE_HOLD_COIN = "1";// 持币

    private static final String TYPE_TEAM_COIN = "2"; // 团队

    private static final int MINING_SCALE = 2;// 小数位

    private static final int DIVIDE_SCALE = 10; // 除法比例时小数位

    private static final String MINING_DEFAULT_COIN_SYMBOL = "UES";

    private SuperBookConf superBookConf; // 超级账本配置属性

    private SuperCoinVolumeConf superCoinVolumeConf; // 超级钱包配置属性

//    private static final BigDecimal TEAM_BASE_MULTIPLY = new BigDecimal(10); // 团队挖矿小于10000以上的数量乘以的基数

    @Override
    public void miningDayTaskEntry() {
        doPopularizeMining();
    }

    /**
     * 挖矿说明：先根据配置清算送币量，清算完成后，再真实转币到BB账户
     */
    @Override
    public void doPopularizeMining() {
        LocalDateTime countDate = LocalDateTime.of(LocalDateTime.now().minusDays(1).toLocalDate(), LocalTime.MIN);// 昨天0点0分0秒
        // 挖矿临时表初始化
        superBookConf = superBookConfDao.findBySymbol(MINING_DEFAULT_COIN_SYMBOL); // 查询超级账本属性
        superCoinVolumeConf = superCoinVolumeConfDao.findOneBySymbol(MINING_DEFAULT_COIN_SYMBOL);
        mk2PopularizeMiningService.initMiningTable(countDate, MINING_DEFAULT_COIN_SYMBOL);
        /**
         * 1、清算
         */
        try {
            logger.info("持币挖矿开始。。。。。。。");
            mk2PopularizeMiningService.doHoldCoinMining(countDate);
            logger.info("持币挖矿开始。。。。。。。");
        } catch (Exception e) {
            logger.error("本次持币挖矿失败", e);
        }

        try {
            logger.info("团队挖矿开始。。。。。。。");
            mk2PopularizeMiningService.doTeamCoinMining(countDate);
            logger.info("团队挖矿结束。。。。。。。");
        } catch (Exception e) {
            logger.error("本次团队挖矿失败", e);
        }

        /**
         * 2、转币到BB资产
         */
        doMining2BbVolume(countDate);

        /**
         * 3、更新区块高度
         */
        superBookConfDao.updateAreaHeight(superBookConf);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void doHoldCoinMining(LocalDateTime countDate) {
        Mk2PopularizeMiningConf holdConf = null;
        try {
            Mk2PopularizeMiningTaskLog holdLog = mk2PopularizeMiningTaskLogDao.findByTypeLately(TYPE_HOLD_COIN);
            if (holdLog != null && (countDate.isEqual(holdLog.getCountDate()) || countDate.isBefore(holdLog.getCountDate()))) {
                throw new PlatException(Constants.OPERRATION_ERROR, "本次持币挖矿时间已经挖过或已过期！[" + countDate.toLocalTime() + "]");
            }
            holdConf = mk2PopularizeMiningConfDao.findByType(TYPE_HOLD_COIN);
            if (holdConf == null) {
                logger.info("持币挖矿未配置。。。。。。。");
                return;
            }
            BigDecimal giveHoldVol = holdCoinMining(holdConf, countDate);
        } catch (PlatException e) {
            logger.error("本次持币挖矿失败", e);
            String msg = e.getMsg().length() > 500 ? e.getMsg().substring(0, 500) : e.getMsg();
            mk2PopularizeMiningService.saveTaskResult(TYPE_HOLD_COIN, null, null, null, BigDecimal.ZERO, countDate, "0", msg);
            throw new PlatException(Constants.PARAM_ERROR, "本次持币挖矿失败!");
        } catch (Exception e) {
            logger.error("本次持币挖矿失败", e);
            String msg = e.toString();
            msg = msg.length() > 500 ? msg.substring(0, 500) : msg;
            mk2PopularizeMiningService.saveTaskResult(TYPE_HOLD_COIN, null, null, null, BigDecimal.ZERO, countDate, "0", msg);
            throw new PlatException(Constants.PARAM_ERROR, "本次持币挖矿失败!");
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void doTeamCoinMining(LocalDateTime countDate) {
        Mk2PopularizeMiningConf teamConf = null;
        try {
            Mk2PopularizeMiningTaskLog teamLog = mk2PopularizeMiningTaskLogDao.findByTypeLately(TYPE_TEAM_COIN);
            if (teamLog != null && (countDate.isEqual(teamLog.getCountDate()) || countDate.isBefore(teamLog.getCountDate()))) {
                throw new PlatException(Constants.OPERRATION_ERROR, "本次团队挖矿时间已经挖过或已过期！[" + countDate.toLocalTime() + "]");
            }
            teamConf = mk2PopularizeMiningConfDao.findByType(TYPE_TEAM_COIN);
            if (teamConf == null) {
                logger.info("团队挖矿未配置。。。。。。。");
                return;
            }
            BigDecimal giveTeamVol = teamCoinMining(teamConf, countDate);
        } catch (PlatException e) {
            logger.error("本次团队挖矿失败", e);
            String msg = e.getMsg().length() > 500 ? e.getMsg().substring(0, 500) : e.getMsg();
            mk2PopularizeMiningService.saveTaskResult(TYPE_TEAM_COIN, null, null, null, BigDecimal.ZERO, countDate, "0", msg);
            throw new PlatException(Constants.PARAM_ERROR, "本次团队挖矿失败!");
        } catch (Exception e) {
            logger.error("本次团队挖矿失败", e);
            String msg = e.toString();
            msg = msg.length() > 500 ? msg.substring(0, 500) : msg;
            mk2PopularizeMiningService.saveTaskResult(TYPE_TEAM_COIN, null, null, null, BigDecimal.ZERO, countDate, "0", msg);
            throw new PlatException(Constants.PARAM_ERROR, "本次团队挖矿失败!");
        }
    }


    public BigDecimal holdCoinMining(Mk2PopularizeMiningConf conf, LocalDateTime countDate) {
        BigDecimal lastVolume = conf.getTotalVolume().subtract(conf.getGrantVolume() == null ? BigDecimal.ZERO : conf.getGrantVolume());
        if (lastVolume.compareTo(BigDecimal.ZERO) != 1) {
            logger.info("持币挖矿矿已挖完。。。。。。。");
            throw new PlatException(Constants.OPERRATION_ERROR, "持币挖矿矿已挖完！");
        }
        BigDecimal perVolume = conf.getPerVolume();
        if (lastVolume.compareTo(perVolume) == -1) {
            perVolume = lastVolume;
        }
        sortHoldMiningOrder(conf, countDate); // 对满足持币挖矿数量的用户进行排名
        BigDecimal sumOrderNo = mk2PopularizeMiningUserTmpDao.countOrderNo(TYPE_HOLD_COIN, countDate, conf.getCoinId());
        // 开始计算持币挖矿送币
        List<Mk2PopularizeMiningGiveCoinLog> giveCoinLogs = new ArrayList<Mk2PopularizeMiningGiveCoinLog>();
        Mk2PopularizeMiningGiveCoinLog coinLog = null;
        List<DestroyAccountLog> destroyAccountLogs = new ArrayList<>();
        DestroyAccountLog destroyLog = null;
        BigDecimal curMiningVol = null;
        List<Mk2PopularizeMiningUserTmp> userTmps = mk2PopularizeMiningUserTmpDao.findByCondition(TYPE_HOLD_COIN, countDate, conf.getCoinId());
        for (Mk2PopularizeMiningUserTmp userTmp: userTmps) {
            curMiningVol = perVolume.multiply(new BigDecimal(userTmp.getOrderNo())).divide(sumOrderNo, DIVIDE_SCALE, BigDecimal.ROUND_DOWN).setScale(MINING_SCALE, BigDecimal.ROUND_DOWN);
            if (curMiningVol.compareTo(BigDecimal.ZERO) < 1) {
                continue;
            }
            if (curMiningVol.compareTo(userTmp.getSourceVolume()) == 1) { // 如果挖矿量大于个人持币量，则大于部分用来销毁
                // 销毁
                destroyLog = createDestroyAccountLog(userTmp.getUserId(), curMiningVol.subtract(userTmp.getSourceVolume()), userTmp.getCoinId(), userTmp.getCoinSymbol());
                destroyAccountLogs.add(destroyLog);
                // 本次持币挖矿不能大于个人持币量
                curMiningVol = userTmp.getSourceVolume();
            }
            if (curMiningVol.compareTo(BigDecimal.ZERO) == 1) {
                superBookConf.setAreaHeight(superBookConf.getAreaHeight() + 1);// 区块高度+1
                coinLog = createGiveCoinLog(TYPE_HOLD_COIN, userTmp.getUserId(), userTmp.getCoinId(), userTmp.getCoinSymbol(), curMiningVol, perVolume, countDate, userTmp.getOrderNo(), userTmp.getVolume(), BigDecimal.ZERO, BigDecimal.ZERO, userTmp.getSourceVolume());
                if (Objects.nonNull(coinLog)) {
                    giveCoinLogs.add(coinLog);
                }
            }
        }
        saveGiveCoinLogBatch(giveCoinLogs); // 保存个人持币挖矿送币流水
        saveDestoryLogBatch(destroyAccountLogs); // 保存销毁

        BigDecimal grantVol = Optional.ofNullable(conf.getGrantVolume()).orElse(BigDecimal.ZERO).add(perVolume);// 已挖矿量 = 已挖矿量 + 今天挖矿总量
        BigDecimal showGrantVolume = Optional.ofNullable(conf.getShowGrantVolume()).orElse(BigDecimal.ZERO).add(perVolume.multiply(Optional.ofNullable(conf.getDelayShowMultiple()).orElse(BigDecimal.ONE))); // 显示挖矿量 = 显示挖矿量 + 今天挖矿量 * 显示倍数
        long count = mk2PopularizeMiningConfDao.updateMiningConf(grantVol, conf.getId(), showGrantVolume);// 更新挖矿总送币量
        if (count != 1) {
            logger.info("更新挖矿持币已挖矿量失败。。。。。。。");
            throw new PlatException(Constants.OPERRATION_ERROR, "更新挖矿持币已挖矿量失败！[" + conf.getId() + "]");
        }
        // 记录task日志
        saveTaskResult(conf.getType(), conf.getCoinId(), conf.getCoinSymbol(), conf.getPerVolume(), perVolume, countDate, "1", "本次持币挖矿成功，挖矿总量[" + perVolume + "]");
        return perVolume;
    }

    private void sortHoldMiningOrder(Mk2PopularizeMiningConf conf, LocalDateTime countDate) {
        List<Mk2PopularizeMiningUserTmp> userTmps = mk2PopularizeMiningUserTmpDao.findUserTmpBySort(conf.getGreaterVolume()); // 按volume排序查询结果 eg:1,2,3,4....
        Mk2PopularizeMiningUserTmp preUserTmp = null;
        for (Mk2PopularizeMiningUserTmp userTmp : userTmps) {// 按volume相等则排名相等，eg:1,2,2,4...
            if (Objects.nonNull(preUserTmp) && preUserTmp.getVolume().compareTo(userTmp.getVolume()) == 0) {
                userTmp.setOrderNo(preUserTmp.getOrderNo());
            }
            preUserTmp = userTmp;
        }
        saveBatchHoldMiningUserTmp(userTmps);// 保存排名
    }


    public BigDecimal teamCoinMining(Mk2PopularizeMiningConf conf, LocalDateTime countDate) {
        // 1、计算本次团队挖矿量可挖总量
        BigDecimal lastVolume = conf.getTotalVolume().subtract(conf.getGrantVolume() == null ? BigDecimal.ZERO : conf.getGrantVolume());
        if (lastVolume.compareTo(BigDecimal.ZERO) != 1) {
            logger.info("团队挖矿矿已挖完。。。。。。。");
            throw new PlatException(Constants.OPERRATION_ERROR, "团队挖矿矿已挖完！");
        }
        BigDecimal perVolume = conf.getPerVolume(); // 本次团队挖矿送币配置送币总量
        if (lastVolume.compareTo(perVolume) == -1) {
            perVolume = lastVolume;
        }
        logger.info("团队挖矿a1" + LocalDateTime.now());
        // 2、初始化Neo4j节点用户的lastBbVolume数量
        Map<String, Neo4jPlatUser> neo4jPlatUserMap = initNeo4jUserLastBbVolume();
        logger.info("团队挖矿a2" + LocalDateTime.now());
        // 3、计算每个满足条件的团队的参与评比持币数量
        List<String> userList = mk2PopularizeMiningUserTmpDao.findAllTeamUserIds(conf.getLeaderGreaterVolume());// 限制1 获得所有大于最小leader持币量的父节点
        logger.info("团队挖矿a3" + LocalDateTime.now());
        List<DestroyAccountLog> destroyAccountLogs = new ArrayList<>();
        List<Mk2PopularizeMiningGiveCoinLog> teamTotalList = new ArrayList<>();
        Mk2PopularizeMiningGiveCoinLog teamTotal = null; // 统计每个团队持币量
        BigDecimal sumAllTeamVol = BigDecimal.ZERO;// sum所有的团队总和
        for (String userId : userList) {
            teamTotal = countTeamTotal(conf, countDate, userId);
            if (Objects.nonNull(teamTotal)) {
                teamTotalList.add(teamTotal);
                sumAllTeamVol = sumAllTeamVol.add(teamTotal.getJoinVolume());
            }
        }
        logger.info("团队挖矿a4" + LocalDateTime.now());
        // 4、计算每个团队本次挖矿量
        BigDecimal curMiningVol = BigDecimal.ZERO;// 团队送币量
        Mk2PopularizeMiningGiveCoinLog holdMining = null;
        DestroyAccountLog destroyLog = null;
        for (Mk2PopularizeMiningGiveCoinLog team : teamTotalList) {
            curMiningVol = team.getJoinVolume().divide(sumAllTeamVol, DIVIDE_SCALE, BigDecimal.ROUND_DOWN).multiply(perVolume).setScale(MINING_SCALE, BigDecimal.ROUND_DOWN);

            holdMining = mk2PopularizeMiningGiveCoinLogDao.findByUserIdTypeDate(team.getUserId(), Mk2PopularizeMiningServiceImpl.TYPE_HOLD_COIN, countDate);
            if (!neo4jPlatUserMap.containsKey(team.getUserId())) {
                continue;
            }
            // 该用户团队最大挖矿量 = 用户真实持币量（BB+超级钱包）- 持币送币量
            BigDecimal maxGivVol = new BigDecimal(neo4jPlatUserMap.get(team.getUserId()).getLastBbVolume()).subtract(Objects.nonNull(holdMining) ? holdMining.getVolume() : BigDecimal.ZERO);
            if (curMiningVol.compareTo(maxGivVol) == 1) {
                destroyLog = mk2PopularizeMiningService.createDestroyAccountLog(team.getUserId(), curMiningVol.subtract(maxGivVol), team.getCoinId(), team.getCoinSymbol());
                destroyAccountLogs.add(destroyLog);
                curMiningVol = maxGivVol;
            }
            if (curMiningVol.compareTo(BigDecimal.ZERO) == 1) {
                superBookConf.setAreaHeight(superBookConf.getAreaHeight() + 1);// 区块高度+1
            }
            team.setVolume(curMiningVol);// 流水设置该团队送币
            team.setTotalVolume(perVolume); // 流水设置 本次团队挖矿送币配置送币总量
            team.setAreaHeight(superBookConf.getAreaHeight());
        }
        logger.info("团队挖矿a5" + LocalDateTime.now());
        // 5、保存送币流水记录
        saveGiveCoinLogBatch(teamTotalList);// 批量保存挖矿送币流水
        saveDestoryLogBatch(destroyAccountLogs); // 保存销毁
        logger.info("团队挖矿a6" + LocalDateTime.now());

        // 6、更新配置已挖矿量
        BigDecimal grantVol = Optional.ofNullable(conf.getGrantVolume()).orElse(BigDecimal.ZERO).add(perVolume); // 已挖矿量 = 已挖矿量 + 今日挖矿量
        BigDecimal showGrantVolume = Optional.ofNullable(conf.getShowGrantVolume()).orElse(BigDecimal.ZERO).add(perVolume.multiply(Optional.ofNullable(conf.getDelayShowMultiple()).orElse(BigDecimal.ONE))); // 显示挖矿量 = 显示挖矿量 + 今天挖矿量 * 显示倍数
        long count = mk2PopularizeMiningConfDao.updateMiningConf(grantVol, conf.getId(), showGrantVolume);
        if (count != 1) {
            throw new PlatException(Constants.OPERRATION_ERROR, "更新挖矿团队已挖矿量失败！[" + conf.getId() + "]");
        }
        // 7、保存本次团队挖矿任务记录
        saveTaskResult(conf.getType(), conf.getCoinId(), conf.getCoinSymbol(), conf.getPerVolume(), perVolume, countDate, "1", "本次团队挖矿成功，挖矿总量[" + perVolume + "]" );
        return perVolume;
    }

    /**
     * 初始化Neo4j节点用户的持币量
     */
    public Map<String, Neo4jPlatUser> initNeo4jUserLastBbVolume() {
        neo4jPlatUserService.cleanAllUserLastBbVolume();
        List<PlatUserNeo4j> platUsers = mkNeo4jUserDao.findAllUserBbVolumes(MINING_DEFAULT_COIN_SYMBOL);
        List<Neo4jPlatUser> neo4jPlatUsers = new ArrayList<>();
        Map<String, Neo4jPlatUser> neo4jPlatUserMap = new HashMap<>();
        platUsers.forEach(u -> {
            Neo4jPlatUser neo4jUser = new Neo4jPlatUser();
            BeanUtils.copyProperties(u, neo4jUser);
            neo4jPlatUsers.add(neo4jUser);
            neo4jPlatUserMap.put(neo4jUser.getUserId(), neo4jUser);
        });
        updateNeo4jLastBbVolumeBatch(neo4jPlatUsers);
        return neo4jPlatUserMap;
    }

    public Mk2PopularizeMiningGiveCoinLog countTeamTotal(Mk2PopularizeMiningConf conf, LocalDateTime countDate, String userId) {
        // 1、计算userid团队总持币量
        Double sumTeamHold = neo4jPlatUserService.sumTreeLastBbVolumeExSelf(userId);
        if (sumTeamHold < conf.getGreaterVolume().doubleValue()) {// 限制2 团队总持币量小于配置时，不参与挖矿
            return null;
        }
        // 2、查询直推下一层团队持币量
        List<Neo4jPlatUser> downFirstUsers = neo4jPlatUserService.findDownFirstByUserId(userId);
        List<BigDecimal> downFirstUserTeamHolds = new ArrayList<>();
        downFirstUsers.forEach(user -> {
            Double totalTeam = neo4jPlatUserService.sumJoinMiningVol(user.getUserId());
            if (Objects.nonNull(totalTeam) && totalTeam > 0) {
                downFirstUserTeamHolds.add(new BigDecimal(totalTeam));
            }
        });
        // 3、查询大区持币量
        BigDecimal teamMaxVol = BigDecimal.ZERO;//子团队最大持币量
        for (BigDecimal subTeamVolume : downFirstUserTeamHolds) {// 找出子团队最大持币量
            if (subTeamVolume.compareTo(teamMaxVol) == 1) {
                teamMaxVol = subTeamVolume;
            }
        }
        // 4、计算userId团队参与评比数量 =  根号3 （最大子团队之和） +  [（基准 * 10 + 零头） OR  基准 * 10]
        BigDecimal teamCoefficientVol = BigDecimal.ZERO;// 根号3 （最大子团队之和） +  [（基准 * 10 + 零头） OR  基准 * 10]
        BigDecimal powSumVol = BigDecimal.ZERO; // 需要开根号3的最大子团队之和
        for (BigDecimal subTeamVolume : downFirstUserTeamHolds) {
            if (subTeamVolume.compareTo(teamMaxVol) == 0) {
                powSumVol = powSumVol.add(subTeamVolume); // 最大子团队之和
            } else {
                teamCoefficientVol = teamCoefficientVol.add(partCountVolume(conf, subTeamVolume)); // [（基准 * 10 + 零头） OR  基准 * 10]
            }
        }
        teamCoefficientVol = teamCoefficientVol.add(new BigDecimal(Math.pow(powSumVol.doubleValue(), 1D / 3D))).setScale(MINING_SCALE, BigDecimal.ROUND_DOWN);// 根号3 （最大子团队之和） +  [（基准 * 10 + 零头） OR  基准 * 10]

        return createGiveCoinLog(TYPE_TEAM_COIN, userId, conf.getCoinId(), conf.getCoinSymbol(), BigDecimal.ZERO, BigDecimal.ZERO, countDate, 0L, teamCoefficientVol, teamMaxVol, new BigDecimal(sumTeamHold), BigDecimal.ZERO);
    }

    private BigDecimal partCountVolume(Mk2PopularizeMiningConf conf, BigDecimal volume) {
        BigDecimal total = BigDecimal.ZERO; // [（基准 * 10 + 零头） OR  基准 * 10]
        if (conf.getBaseVolume().compareTo(volume) == -1) {
            total = volume.subtract(conf.getBaseVolume()).add(conf.getBaseVolume().multiply(conf.getBaseMultiple()));
        } else {
            total = volume.multiply(conf.getBaseMultiple());
        }
        return total;
    }

    private void updateNeo4jLastBbVolumeBatch(List<Neo4jPlatUser> neo4jPlatUsers) {
        int page = neo4jPlatUsers.size() % neo4jPageCount != 0 ? (neo4jPlatUsers.size() / neo4jPageCount + 1) : (neo4jPlatUsers.size() / neo4jPageCount);
        List<Neo4jPlatUser> pageVolumes = null;
        for (int index = 1; index <= page; index++) {
            if (index == page) {
                pageVolumes = neo4jPlatUsers.subList((index - 1) * neo4jPageCount, neo4jPlatUsers.size());
            } else {
                pageVolumes = neo4jPlatUsers.subList((index - 1) * neo4jPageCount, index * neo4jPageCount);
            }
            neo4jPlatUserService.updateBatchUserLastBbVolume(pageVolumes);
        }
    }


    private Mk2PopularizeMiningUserTmpBak createMiningUserTmpBak(String type, String userId, String subUserId, String coinId, String coinSymbol, BigDecimal volume, LocalDateTime countTime) {
        Mk2PopularizeMiningUserTmpBak tmpBak = new Mk2PopularizeMiningUserTmpBak();
        tmpBak.setId(SnowFlake.createSnowFlake().nextIdString());
        tmpBak.setType(type);
        tmpBak.setUserId(userId);
        tmpBak.setSubUserId(subUserId);
        tmpBak.setCoinId(coinId);
        tmpBak.setCoinSymbol(coinSymbol);
        tmpBak.setVolume(volume);
        tmpBak.setCountDate(countTime);
        //
        return tmpBak;
    }

    private void saveGiveCoinLogBatch(List<Mk2PopularizeMiningGiveCoinLog> giveCoinLogs) {
        int page = giveCoinLogs.size() % pageCount != 0 ? (giveCoinLogs.size() / pageCount + 1) : (giveCoinLogs.size() / pageCount);
        List<Mk2PopularizeMiningGiveCoinLog> pageCoinLog = null;
        for (int index = 1; index <= page; index++) {
            if (index == page) {
                pageCoinLog = giveCoinLogs.subList((index - 1) * pageCount, giveCoinLogs.size());
            } else {
                pageCoinLog = giveCoinLogs.subList((index - 1) * pageCount, index * pageCount);
            }
            mk2PopularizeMiningGiveCoinLogDao.insertBatch(pageCoinLog);
        }
    }

    public Mk2PopularizeMiningGiveCoinLog createGiveCoinLog(String type, String userId, String coinId,
                                                             String coinSymbol, BigDecimal volume, BigDecimal totalVol,
                                                             LocalDateTime countDate, long orderNo, BigDecimal joinVolume,
                                                             BigDecimal maxSubVolume, BigDecimal teamHoldTotal, BigDecimal sourceVolume) {
        PlatUser user = platUserDao.findById(userId);
        if (Objects.isNull(user)) {
            return null;
        }
        Mk2PopularizeMiningGiveCoinLog log = new Mk2PopularizeMiningGiveCoinLog();
        log.setId(SnowFlake.createSnowFlake().nextIdString());
        log.setType(type);
        log.setUserId(userId);
        log.setMail(user.getMail());
        log.setMobile(user.getMobile());
        log.setIdCard(user.getIdCard());
        log.setRealName(user.getRealName());
        log.setCoinId(coinId);
        log.setCoinSymbol(coinSymbol);
        log.setVolume(volume);
        log.setTotalVolume(totalVol);
//        log.setRatio(volume.divide(totalVol).setScale(2));
        log.setOrderNo(orderNo);
        log.setJoinVolume(joinVolume);
        log.setMaxSubVolume(maxSubVolume);
        log.setCountDate(countDate);
        log.setTeamHoldTotal(teamHoldTotal);
        log.setSourceVolume(sourceVolume);
        log.setInAddress(getInAddress(userId));
        log.setOutAddress(Objects.nonNull(superBookConf) ? superBookConf.getAddress() : null);
        log.setAreaHeight(Objects.nonNull(superBookConf) ? superBookConf.getAreaHeight() : 0L);
        log.setTxHash(SuperBookAddrUtil.createTxHash());
        log.setStatus("0"); // 0清算1转币
        return log;
//        mk2PopularizeMiningGiveCoinLogDao.insert(log);
    }

    public DestroyAccountLog createDestroyAccountLog(String userId, BigDecimal volume, String coinId, String coinSymbol) {
        PlatUser user = platUserDao.findById(userId);
        DestroyAccountLog log = new DestroyAccountLog();
        log.setId(SnowFlake.createSnowFlake().nextIdString());
        log.setUserId(userId);
        log.setMobile(user.getMobile());
        log.setMail(user.getMail());
        log.setCoinId(coinId);
        log.setCoinSymbol(coinSymbol);
        log.setVolume(volume);
        log.setType("1");
        return log;
    }

    public void saveDestoryLogBatch(List<DestroyAccountLog> destroyLogs) {
        int page = destroyLogs.size() % pageCount != 0 ? (destroyLogs.size() / pageCount + 1) : (destroyLogs.size() / pageCount);
        List<DestroyAccountLog> pageDestoryLog = null;
        for (int index = 1; index <= page; index++) {
            if (index == page) {
                pageDestoryLog = destroyLogs.subList((index - 1) * pageCount, destroyLogs.size());
            } else {
                pageDestoryLog = destroyLogs.subList((index - 1) * pageCount, index * pageCount);
            }
            destroyAccountLogDao.insertBatch(pageDestoryLog);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveTaskResult(String type, String coinId, String coinSymbol, BigDecimal miningVol, BigDecimal grantVol, LocalDateTime countDate, String status, String remark) {
        Mk2PopularizeMiningTaskLog log = new Mk2PopularizeMiningTaskLog();
        log.setId(SnowFlake.createSnowFlake().nextIdString());
        log.setType(type);
        log.setCoinId(coinId);
        log.setCoinSymbol(coinSymbol);
        log.setMiningVolume(miningVol);
        log.setGrantVolume(grantVol);
        log.setCountDate(countDate);
        log.setStatus(status);
        log.setRemark(remark);
        mk2PopularizeMiningTaskLogDao.insert(log);

    }

    /**
     * 计算送币数量后操作，真实转币，挖矿送币状态从0到1
     * @param countDate
     */
    public void doMining2BbVolume(LocalDateTime countDate) {
        mk2PopularizeMiningGiveCoinLogDao.deleteZeroGiveCoin(countDate); // 删除送币量小于等于0的记录
        List<Mk2PopularizeMiningGiveCoinLog> giveCoinLogList = mk2PopularizeMiningGiveCoinLogDao.findByCountDate(countDate);

        UserCoinVolumeEventEnum[] emst = {UserCoinVolumeEventEnum.ADD_VOLUME};//

        List<UserCoinVolumeBillDTO> listBbVolume = giveCoinLogList.stream().map(giveCoin -> {
            UserCoinVolumeBillDTO coinVolumeBill = new UserCoinVolumeBillDTO();
            coinVolumeBill.setCoinSymbol(giveCoin.getCoinSymbol());
            coinVolumeBill.setUserId(giveCoin.getUserId());
            coinVolumeBill.setRefKey(giveCoin.getId());
            coinVolumeBill.setForceLock(true);
            coinVolumeBill.setMark(giveCoin.getType());
            coinVolumeBill.setSource("挖矿");
            coinVolumeBill.setOpSign(emst);
            coinVolumeBill.setOpLockVolume(BigDecimal.ZERO);
            coinVolumeBill.setOpVolume(giveCoin.getVolume());
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

    private void saveBatchHoldMiningUserTmp(List<Mk2PopularizeMiningUserTmp> userTmps) {
        if (CollectionUtils.isEmpty(userTmps)) {
            return;
        }
        int page = userTmps.size() % pageCount != 0 ? (userTmps.size() / pageCount + 1) : (userTmps.size() / pageCount);
        List<Mk2PopularizeMiningUserTmp> pageUserTmps = null;
        for (int index = 1; index <= page; index++) {
            if (index == page) {
                pageUserTmps = userTmps.subList((index - 1) * pageCount, userTmps.size());
            } else {
                pageUserTmps = userTmps.subList((index - 1) * pageCount, index * pageCount);
            }
            mk2PopularizeMiningUserTmpDao.insertBatch(pageUserTmps);
        }
    }

    private String getInAddress(String userId) {
        SuperBook superBook = superBookDao.findByUserIdAndSymbol(userId, MINING_DEFAULT_COIN_SYMBOL);
        if (Objects.nonNull(superBook)) {
            return superBook.getAddress();
        } else {
            return null;
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void initMiningTable(LocalDateTime countDate, String coinSymbol) {
        mk2PopularizeMiningUserTmpDao.deleteHistoryUserTmp(); // 清除tmp表数据
        mk2PopularizeMiningUserTmpDao.deleteHistorySnapshot();// 清空常规用户持币快照表
        mk2PopularizeMiningUserTmpDao.deleteHistorySuperSnapshot(); // 清空用户超级钱包持币快照表
        mk2PopularizeMiningUserTmpDao.deleteHistoryUserTmpBak();// 个人持币挖矿中间表清理

        mk2PopularizeMiningUserTmpDao.snapshotUserCoinHold(coinSymbol);// 快照用户当前持币量
        mk2PopularizeMiningUserTmpDao.snapshotSuperCoinHold(coinSymbol); // 快照用户当前超级钱包快照表

        // BB资产 + 超级钱包 * 倍数 + 会员冻结 * 倍数
//        mk2PopularizeMiningUserTmpDao.insertUserVolumeTemp(countDate, MINING_DEFAULT_COIN_SYMBOL, superCoinVolumeConf.getMultiple(), superCoinVolumeConf.getMemberLockMultiple());// type=9 临时数据 插入常规账户符合挖矿条件
        initUserCoinVolumeTemp(countDate, coinSymbol);
    }

    private void initUserCoinVolumeTemp(LocalDateTime countDate, String coinSymbol) {
        // 查询用户BB数量
        List<Mk2PopularizeMiningUserTmpBak> userCoins =  mk2PopularizeMiningUserTmpDao.findUserCoin(countDate, coinSymbol);
        Map<String, Mk2PopularizeMiningUserTmpBak> mapUserCoin = new HashMap<>();
        userCoins.forEach(userCoin -> {
            mapUserCoin.put(userCoin.getUserId(), userCoin);
        });
        // 查询超级钱包数量
        List<Mk2PopularizeMiningUserTmpBak> superCoins = mk2PopularizeMiningUserTmpDao.findSuperUserCoin(coinSymbol);
        superCoins.forEach(superCoin -> {
            Mk2PopularizeMiningUserTmpBak temp = mapUserCoin.get(superCoin.getUserId());
            if (Objects.nonNull(temp)) {
                temp.setVolume(temp.getVolume().add(superCoin.getVolume().multiply(superCoinVolumeConf.getMultiple()))); // 超级钱包参与挖矿 乘以 配置倍数
                temp.setSourceVolume(temp.getSourceVolume().add(superCoin.getVolume()));
            }
        });
        List<Mk2PopularizeMiningUserTmpBak> lockCoins = mk2PopularizeMiningUserTmpDao.findLockUserCoin(coinSymbol);
        lockCoins.forEach(lockCoin -> {
            Mk2PopularizeMiningUserTmpBak temp = mapUserCoin.get(lockCoin.getUserId());
            if (Objects.nonNull(temp)) {
                temp.setVolume(temp.getVolume().add(lockCoin.getVolume().multiply(superCoinVolumeConf.getMemberLockMultiple()))); // 个人锁币参与挖矿  乘以 配置倍数
                temp.setSourceVolume(temp.getSourceVolume().add(lockCoin.getVolume()));
            }
        });
        // 批量保存tempbak数据
        int page = userCoins.size() % pageCount != 0 ? (userCoins.size() / pageCount + 1) : (userCoins.size() / pageCount);
        List<Mk2PopularizeMiningUserTmpBak> pageDates = null;
        for (int index = 1; index <= page; index++) {
            if (index == page) {
                pageDates = userCoins.subList((index - 1) * pageCount, userCoins.size());
            } else {
                pageDates = userCoins.subList((index - 1) * pageCount, index * pageCount);
            }
            mk2PopularizeMiningUserTmpBakDao.insertBatch(pageDates);
        }

    }

}
