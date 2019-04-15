package com.biao.service.impl;

import com.biao.mapper.MkNeo4jUserDao;
import com.biao.mapper.PlatUserDao;
import com.biao.mapper.mk2.Mk2PopularizeMiningConfDao;
import com.biao.mapper.mk2.Mk2PopularizeMiningGiveCoinLogDao;
import com.biao.mapper.mk2.Mk2PopularizeMiningTaskLogDao;
import com.biao.mapper.mk2.Mk2PopularizeMiningUserTmpDao;
import com.biao.neo4j.service.Neo4jPlatUserService;
import com.biao.service.Mk2MiningTeamService;
import com.biao.service.Mk2PopularizeMiningService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class Mk2MiningTeamServiceImpl implements Mk2MiningTeamService {

    private Logger logger = LoggerFactory.getLogger(Mk2MiningTeamServiceImpl.class);

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
    private Mk2PopularizeMiningService mk2PopularizeMiningService;


    @Autowired
    private Neo4jPlatUserService neo4jPlatUserService;

    private static final int sqlPageCount = 200; // mysql批量提交每批次数量

    private static final int neo4jPageCount = 500; // neo4j批量提交每批次提交数量

    private static final String TYPE_HOLD_COIN = "1";// 持币

    private static final String TYPE_TEAM_COIN = "2"; // 团队

    private static final int MINING_SCALE = 2;// 小数位

    private static final int DIVIDE_SCALE = 10; // 除法比例时小数位

    private static final String MINING_DEFAULT_COIN_SYMBOL = "UES";

    @Transactional
    public void doTeamCoinMining() {
//        LocalDateTime countDate = LocalDateTime.of(LocalDateTime.now().minusDays(1).toLocalDate(), LocalTime.MIN);// 昨天0点0分0秒
//        doTeamCoinMining(countDate);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void doTeamCoinMining(LocalDateTime countDate) {
//        Mk2PopularizeMiningConf teamConf = null;
//        try {
//            Mk2PopularizeMiningTaskLog teamLog = mk2PopularizeMiningTaskLogDao.findByTypeLately(TYPE_TEAM_COIN);
//            if (teamLog != null && (countDate.isEqual(teamLog.getCountDate()) || countDate.isBefore(teamLog.getCountDate()))) {
//                throw new PlatException(Constants.OPERRATION_ERROR, "本次团队挖矿时间已经挖过或已过期！[" + countDate.toLocalTime() + "]");
//            }
//            teamConf = mk2PopularizeMiningConfDao.findByType(TYPE_TEAM_COIN);
//            if (teamConf == null) {
//                logger.info("团队挖矿未配置。。。。。。。");
//                return;
//            }
//            BigDecimal giveTeamVol = teamCoinMining(teamConf, countDate);
//        } catch (PlatException e) {
//            logger.error("NEO4J本次团队挖矿失败", e);
//            String msg = e.getMsg().length() > 500 ? e.getMsg().substring(0, 500) : e.getMsg();
//            mk2PopularizeMiningService.saveTaskResult(TYPE_TEAM_COIN, null, null, null, BigDecimal.ZERO, countDate, "0", msg);
//            throw new PlatException(Constants.PARAM_ERROR, "NEO4J本次团队挖矿失败!");
//        } catch (Exception e) {
//            logger.error("NEO4J本次团队挖矿失败", e);
//            String msg = e.toString();
//            msg = msg.length() > 500 ? msg.substring(0, 500) : msg;
//            mk2PopularizeMiningService.saveTaskResult(TYPE_TEAM_COIN, null, null, null, BigDecimal.ZERO, countDate, "0", msg);
//            throw new PlatException(Constants.PARAM_ERROR, "NEO4J本次团队挖矿失败!");
//        }
    }

//    public BigDecimal teamCoinMining(Mk2PopularizeMiningConf conf, LocalDateTime countDate) {
//        // 1、计算本次团队挖矿量可挖总量
//        BigDecimal lastVolume = conf.getTotalVolume().subtract(conf.getGrantVolume() == null ? BigDecimal.ZERO : conf.getGrantVolume());
//        if (lastVolume.compareTo(BigDecimal.ZERO) != 1) {
//            logger.info("团队挖矿矿已挖完。。。。。。。");
//            throw new PlatException(Constants.OPERRATION_ERROR, "团队挖矿矿已挖完！");
//        }
//        BigDecimal perVolume = conf.getPerVolume(); // 本次团队挖矿送币配置送币总量
//        if (lastVolume.compareTo(perVolume) == -1) {
//            perVolume = lastVolume;
//        }
//        // 2、初始化Neo4j节点用户的lastBbVolume数量
//        Map<String, Neo4jPlatUser> neo4jPlatUserMap = initNeo4jUserLastBbVolume();
//        // 3、计算每个满足条件的团队的参与评比持币数量
//        List<String> userList = mk2PopularizeMiningUserTmpDao.findAllTeamUserIds(conf.getLeaderGreaterVolume());// 限制1 获得所有大于最小leader持币量的父节点
//        List<DestroyAccountLog> destroyAccountLogs = new ArrayList<>();
//        List<Mk2PopularizeMiningGiveCoinLog> teamTotalList = new ArrayList<>();
//        Mk2PopularizeMiningGiveCoinLog teamTotal = null; // 统计每个团队持币量
//        BigDecimal sumAllTeamVol = BigDecimal.ZERO;// sum所有的团队总和
//        for (String userId : userList) {
//            teamTotal = countTeamTotal(conf, countDate, userId);
//            if (Objects.nonNull(teamTotal)) {
//                teamTotalList.add(teamTotal);
//                sumAllTeamVol = sumAllTeamVol.add(teamTotal.getJoinVolume());
//            }
//        }
//        // 4、计算每个团队本次挖矿量
//        BigDecimal curMiningVol = BigDecimal.ZERO;// 团队送币量
//        Mk2PopularizeMiningGiveCoinLog holdMining = null;
//        Neo4jPlatUser neo4jPlatUser = null;
//        DestroyAccountLog destroyLog = null;
//        for (Mk2PopularizeMiningGiveCoinLog team : teamTotalList) {
//            curMiningVol = team.getJoinVolume().divide(sumAllTeamVol, DIVIDE_SCALE, BigDecimal.ROUND_DOWN).multiply(perVolume).setScale(MINING_SCALE, BigDecimal.ROUND_DOWN);
//
//            holdMining = mk2PopularizeMiningGiveCoinLogDao.findByUserIdTypeDate(team.getUserId(), Mk2PopularizeMiningServiceImpl.TYPE_HOLD_COIN, countDate);
//            // 该用户团队最大挖矿量 = 用户真实持币量（BB+超级钱包）- 持币送币量
//            BigDecimal maxGivVol = new BigDecimal(neo4jPlatUserMap.get(team.getUserId()).getLastBbVolume()).subtract(Objects.nonNull(holdMining) ? holdMining.getVolume() : BigDecimal.ZERO);
//            if (curMiningVol.compareTo(maxGivVol) == 1) {
//                destroyLog = mk2PopularizeMiningService.createDestroyAccountLog(team.getUserId(), curMiningVol.subtract(maxGivVol), team.getCoinId(), team.getCoinSymbol());
//                destroyAccountLogs.add(destroyLog);
//                curMiningVol = maxGivVol;
//            }
//            team.setVolume(curMiningVol);// 流水设置该团队送币
//            team.setTotalVolume(perVolume); // 流水设置 本次团队挖矿送币配置送币总量
//        }
//        // 4、保底校验，所有团队总送币量不能大于本次后台配置总送币量
////        if (totalMiningVol.compareTo(perVolume) == 1) { // 最终校验送出的币和本次能送币总量只和，若送出的币大于本次挖矿送币量则抛错
////            throw new PlatException(Constants.OPERRATION_ERROR, "更新挖矿团队已挖矿量失败！超出本次挖币总量！！！！");
////        }
//        // 5、保存送币流水记录
//        saveGiveCoinLogBatch(teamTotalList);// 批量保存挖矿送币流水
//        mk2PopularizeMiningService.saveDestoryLogBatch(destroyAccountLogs); // 保存销毁
//
//        // 6、更新配置已挖矿量
//        BigDecimal grantVol = Optional.ofNullable(conf.getGrantVolume()).orElse(BigDecimal.ZERO).add(perVolume); // 已挖矿量 = 已挖矿量 + 今日挖矿量
//        BigDecimal showGrantVolume = Optional.ofNullable(conf.getShowGrantVolume()).orElse(BigDecimal.ZERO).add(perVolume.multiply(Optional.ofNullable(conf.getDelayShowMultiple()).orElse(BigDecimal.ONE))); // 显示挖矿量 = 显示挖矿量 + 今天挖矿量 * 显示倍数
//        long count = mk2PopularizeMiningConfDao.updateMiningConf(grantVol, conf.getId(), showGrantVolume);
//        if (count != 1) {
//            throw new PlatException(Constants.OPERRATION_ERROR, "更新挖矿团队已挖矿量失败！[" + conf.getId() + "]");
//        }
//        // 7、保存本次团队挖矿任务记录
//        mk2PopularizeMiningService.saveTaskResult(conf.getType(), conf.getCoinId(), conf.getCoinSymbol(), conf.getPerVolume(), perVolume, countDate, "1", "本次团队挖矿成功，挖矿总量[" + perVolume + "]" );
//        return perVolume;
//    }
//
//    /**
//     * 初始化Neo4j节点用户的持币量
//     */
//    public Map<String, Neo4jPlatUser> initNeo4jUserLastBbVolume() {
//        neo4jPlatUserService.cleanAllUserLastBbVolume();
//        List<PlatUserNeo4j> platUsers = mkNeo4jUserDao.findAllUserBbVolumes(MINING_DEFAULT_COIN_SYMBOL, new BigDecimal(5)); // ==
//        List<Neo4jPlatUser> neo4jPlatUsers = new ArrayList<>();
//        Map<String, Neo4jPlatUser> neo4jPlatUserMap = new HashMap<>();
//        platUsers.forEach(u -> {
//            Neo4jPlatUser neo4jUser = new Neo4jPlatUser();
//            BeanUtils.copyProperties(u, neo4jUser);
//            neo4jPlatUsers.add(neo4jUser);
//            neo4jPlatUserMap.put(neo4jUser.getUserId(), neo4jUser);
//        });
//        updateNeo4jLastBbVolumeBatch(neo4jPlatUsers);
//        return neo4jPlatUserMap;
//    }
//
//    public Mk2PopularizeMiningGiveCoinLog countTeamTotal(Mk2PopularizeMiningConf conf, LocalDateTime countDate, String userId) {
//        // 1、计算userid团队总持币量
//        Double sumTeamHold = neo4jPlatUserService.sumTreeLastBbVolumeExSelf(userId);
//        if (sumTeamHold < conf.getGreaterVolume().doubleValue()) {// 限制2 团队总持币量小于配置时，不参与挖矿
//            return null;
//        }
//        // 2、查询直推下一层团队持币量
//        List<Neo4jPlatUser> downFirstUsers = neo4jPlatUserService.findDownFirstByUserId(userId);
//        List<BigDecimal> downFirstUserTeamHolds = new ArrayList<>();
//        downFirstUsers.forEach(user -> {
//            Double totalTeam = neo4jPlatUserService.sumJoinMiningVol(user.getUserId());
//            if (Objects.nonNull(totalTeam) && totalTeam > 0) {
//                downFirstUserTeamHolds.add(new BigDecimal(totalTeam));
//            }
//        });
//        // 3、查询大区持币量
//        BigDecimal teamMaxVol = BigDecimal.ZERO;//子团队最大持币量
//        for (BigDecimal subTeamVolume : downFirstUserTeamHolds) {// 找出子团队最大持币量
//            if (subTeamVolume.compareTo(teamMaxVol) == 1) {
//                teamMaxVol = subTeamVolume;
//            }
//        }
//        // 4、计算userId团队参与评比数量 =  根号3 （最大子团队之和） +  [（基准 * 10 + 零头） OR  基准 * 10]
//        BigDecimal teamCoefficientVol = BigDecimal.ZERO;// 根号3 （最大子团队之和） +  [（基准 * 10 + 零头） OR  基准 * 10]
//        BigDecimal powSumVol = BigDecimal.ZERO; // 需要开根号3的最大子团队之和
//        for (BigDecimal subTeamVolume : downFirstUserTeamHolds) {
//            if (subTeamVolume.compareTo(teamMaxVol) == 0) {
//                powSumVol = powSumVol.add(subTeamVolume); // 最大子团队之和
//            } else {
//                teamCoefficientVol = teamCoefficientVol.add(partCountVolume(conf, subTeamVolume)); // [（基准 * 10 + 零头） OR  基准 * 10]
//            }
//        }
//        teamCoefficientVol = teamCoefficientVol.add(new BigDecimal(Math.pow(powSumVol.doubleValue(), 1D / 3D))).setScale(MINING_SCALE, BigDecimal.ROUND_DOWN);// 根号3 （最大子团队之和） +  [（基准 * 10 + 零头） OR  基准 * 10]
//
//        return mk2PopularizeMiningService.createGiveCoinLog(TYPE_TEAM_COIN, userId, conf.getCoinId(), conf.getCoinSymbol(), BigDecimal.ZERO, BigDecimal.ZERO, countDate, 0L, teamCoefficientVol, teamMaxVol, new BigDecimal(sumTeamHold));
//    }
//
//    private BigDecimal partCountVolume(Mk2PopularizeMiningConf conf, BigDecimal volume) {
//        BigDecimal total = BigDecimal.ZERO; // [（基准 * 10 + 零头） OR  基准 * 10]
//        if (conf.getBaseVolume().compareTo(volume) == -1) {
//            total = volume.subtract(conf.getBaseVolume()).add(conf.getBaseVolume().multiply(conf.getBaseMultiple()));
//        } else {
//            total = volume.multiply(conf.getBaseMultiple());
//        }
//        return total;
//    }
//
////
////    private Mk2PopularizeMiningGiveCoinLog createGiveCoinLog(String type, String userId, String coinId    /**
//////     * 送币流水日志
//////     *
//////     * @param type          1持币挖矿，2团队挖矿
//////     * @param userId        送币人ID
//////     * @param coinId
//////     * @param coinSymbol
//////     * @param volume        送币数量
//////     * @param totalVol      本次挖矿总送币数量
//////     * @param countDate     本次挖矿时间
//////     * @param orderNo       排名
//////     * @param joinVolume    参与挖矿评比数量
//////     * @param maxSubVolume  大区持币数量
//////     * @param teamHoldTotal 团队总持币数量(自己除外)
//////     * @return
//////     */,
////                                                              String coinSymbol, BigDecimal volume, BigDecimal totalVol,
////                                                              LocalDateTime countDate, long orderNo, BigDecimal joinVolume,
////                                                              BigDecimal maxSubVolume, BigDecimal teamHoldTotal) {
////        PlatUser user = platUserDao.findById(userId);
////        Mk2PopularizeMiningGiveCoinLog log = new Mk2PopularizeMiningGiveCoinLog();
////        log.setId(SnowFlake.createSnowFlake().nextIdString());
////        log.setType(type);
////        log.setUserId(userId);
////        log.setMail(user.getMail());
////        log.setMobile(user.getMobile());
////        log.setIdCard(user.getIdCard());
////        log.setRealName(user.getRealName());
////        log.setCoinId(coinId);
////        log.setCoinSymbol(coinSymbol);
////        log.setVolume(volume);
////        log.setTotalVolume(totalVol);
//////        log.setRatio(volume.divide(totalVol).setScale(2));
////        log.setOrderNo(orderNo);
////        log.setJoinVolume(joinVolume);
////        log.setMaxSubVolume(maxSubVolume);
////        log.setCountDate(countDate);
////        log.setTeamHoldTotal(teamHoldTotal);
////        return log;
//////        Mk2PopularizeMiningGiveCoinLogDao.insert(log);
////    }
//
//    private void saveGiveCoinLogBatch(List<Mk2PopularizeMiningGiveCoinLog> giveCoinLogs) {
//        int page = giveCoinLogs.size() % sqlPageCount != 0 ? (giveCoinLogs.size() / sqlPageCount + 1) : (giveCoinLogs.size() / sqlPageCount);
//        List<Mk2PopularizeMiningGiveCoinLog> pageCoinLog = null;
//        for (int index = 1; index <= page; index++) {
//            if (index == page) {
//                pageCoinLog = giveCoinLogs.subList((index - 1) * sqlPageCount, giveCoinLogs.size());
//            } else {
//                pageCoinLog = giveCoinLogs.subList((index - 1) * sqlPageCount, index * sqlPageCount);
//            }
//            mk2PopularizeMiningGiveCoinLogDao.insertBatch(pageCoinLog);
//        }
//    }
//
//    private void updateNeo4jLastBbVolumeBatch(List<Neo4jPlatUser> neo4jPlatUsers) {
//        int page = neo4jPlatUsers.size() % neo4jPageCount != 0 ? (neo4jPlatUsers.size() / neo4jPageCount + 1) : (neo4jPlatUsers.size() / neo4jPageCount);
//        List<Neo4jPlatUser> pageVolumes = null;
//        for (int index = 1; index <= page; index++) {
//            if (index == page) {
//                pageVolumes = neo4jPlatUsers.subList((index - 1) * neo4jPageCount, neo4jPlatUsers.size());
//            } else {
//                pageVolumes = neo4jPlatUsers.subList((index - 1) * neo4jPageCount, index * neo4jPageCount);
//            }
//            neo4jPlatUserService.updateBatchUserLastBbVolume(pageVolumes);
//        }
//    }
}
