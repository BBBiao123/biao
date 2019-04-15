package com.biao.service.impl;

import com.biao.constant.Constants;
import com.biao.constant.DistributeRuleTypeEnum;
import com.biao.constant.DistributeTaskStatusEnum;
import com.biao.entity.MkCommonTaskRecord;
import com.biao.entity.MkDistributeLog;
import com.biao.enums.OrderEnum;
import com.biao.exception.PlatException;
import com.biao.mapper.MkCommonTaskRecordDao;
import com.biao.mapper.MkDistributeRuleDao;
import com.biao.reactive.data.mongo.service.TradeDetailService;
import com.biao.service.MkMiningRuleTaskService;
import com.biao.service.UserCoinVolumeExService;
import com.biao.util.DateUtils;
import com.biao.vo.TradeExPairDayAvgVO;
import com.biao.vo.TradeUserCoinFeeVO;
import com.biao.vo.UserTradeVO;
import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.thymeleaf.util.MapUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service("MkMiningRuleTaskService_1")
public class MkMiningRuleTaskServiceImpl implements MkMiningRuleTaskService {

    private static org.slf4j.Logger logger = LoggerFactory.getLogger(MkMiningRuleTaskServiceImpl.class);

    private final BigDecimal PERCENTAGE_DIVIDE = BigDecimal.TEN.multiply(BigDecimal.TEN);

    @Autowired
    private MkDistributeRuleDao mkDistributeRuleDao;

    @Autowired
    private TradeDetailService tradeDetailService;

    @Autowired
    private UserCoinVolumeExService userCoinVolumeService;

    @Autowired
    private MkMiningRuleTaskServiceImpl mkMiningRuleTaskService;

    @Autowired
    MkCommonTaskRecordDao mkCommonTaskRecordDao;

    @Override
    public void miningDayTaskEntry() {

        LocalDate curDateTime = LocalDate.now();
//        curDateTime = curDateTime.minusDays(-1);
        String curDate = DateUtils.formaterDate(curDateTime);

//        Map<String ,Object> recordMap = mkDistributeRuleDao.findTaskRecordByDay(curDate,DistributeRuleTypeEnum.MINING.getCode());
        MkCommonTaskRecord taskRecord = mkCommonTaskRecordDao.findByTaskDateAndType(LocalDateTime.of(curDateTime, LocalTime.MIN), DistributeRuleTypeEnum.MINING.getCode());

        if (Objects.nonNull(taskRecord)) {
            logger.info(String.format("[%s]执行失败，挖矿规则已存在成功记录，无需再次执行", curDate));
            return;
        }

        //插入新的任务记录
//        String recordId = UUID.randomUUID().toString().replace("-","");
        MkCommonTaskRecord mkCommonTaskRecord = null;
        Map<String, Object> miningMap = null;  //挖矿规则
        Map<String, Object> resultMap = new HashMap<>(); //任务执行结果
        resultMap.put("grantTotalVolume", BigDecimal.ZERO);
        resultMap.put("releaseTotalVolume", BigDecimal.ZERO);

        try {
            //插入初始化的任务记录
            mkCommonTaskRecord = this.insertTaskRecord(DistributeRuleTypeEnum.MINING.getCode(), LocalDateTime.of(curDateTime, LocalTime.MIN), "", "", BigDecimal.ZERO, LocalDateTime.now());
            //获取当前已启动的挖矿规则
            miningMap = this.getMiningRule(curDateTime);
            //执行挖矿规则
            mkMiningRuleTaskService.execMiningDayTaskCore(miningMap, curDateTime, resultMap);
        } catch (PlatException e) {
            logger.error(e.getMsg());
            this.updateTaskRecord(mkCommonTaskRecord, BigDecimal.ZERO, DistributeTaskStatusEnum.FAILURE.getCode(), e.getMsg(), "", "");
            return;
        } catch (Exception e) {
            logger.error(String.format("[%s]执行失败，系统异常！", curDate) + "message：" + e.getMessage());
            this.updateTaskRecord(mkCommonTaskRecord, BigDecimal.ZERO, DistributeTaskStatusEnum.FAILURE.getCode(), String.format("[%s]执行失败，系统异常！", curDate), "", "");
            return;
        }

        //更新分销规则任务记录
        String remark = String.format("执行成功,手续费占比[%s],币种代号[%s],币种总数量[%s]，任务前总数量[%s], 释放总量[%s]", String.valueOf(miningMap.get("percentage")), String.valueOf(miningMap.get("coin_symbol")), String.valueOf(miningMap.get("volume")), String.valueOf(miningMap.get("grant_volume")), String.valueOf(resultMap.get("releaseTotalVolume")));
//        mkDistributeRuleDao.updateTaskRecord(this.transferToBigDecimal(resultMap, "grantTotalVolume"), DistributeTaskStatusEnum.SUCCESS.getCode(), recordId, remark);
        this.updateTaskRecord(mkCommonTaskRecord, this.transferToBigDecimal(resultMap, "grantTotalVolume"), DistributeTaskStatusEnum.SUCCESS.getCode(), remark, String.valueOf(miningMap.get("coin_id")), String.valueOf(miningMap.get("coin_symbol")));

    }

    @Transactional
    public void execMiningDayTaskCore(Map<String, Object> miningMap, LocalDate curDateTime, Map resultMap) {

        logger.info(String.format("start [%s]执行挖矿规则,{}", DateUtils.formaterDate(curDateTime)), miningMap);

        //计算前天交易手续费，并把手续费转成主区币种（ETH,BTC,USDT）
        List<TradeUserCoinFeeVO> userCoinFeeList = this.calcUserTradeFee(curDateTime.minusDays(1));
        if (CollectionUtils.isEmpty(userCoinFeeList)) {
            logger.info(String.format("无[%s]成交记录！", DateUtils.formaterDate(curDateTime.minusDays(1))));
            return;
        }

        //返回用户平台币数量,{useId, volume}
        Map<String, BigDecimal> userRefundMap = new HashMap<>();

        //把手续费转换成平台币
        BigDecimal grantTotalVolume = this.transferTradeFeeToPlatCoin(userCoinFeeList, userRefundMap, DateUtils.formaterDate(curDateTime.minusDays(1)), miningMap);

        //把所有返还数量返回上级
        resultMap.put("grantTotalVolume", String.valueOf(grantTotalVolume));

        //把平台发放给用户
        List<MkDistributeLog> mkDistributeLogList = new ArrayList<>();
        this.grantPlatCoinToUser(miningMap, grantTotalVolume, userRefundMap, curDateTime, mkDistributeLogList);

        //更新挖矿规则发放平台币数量
        this.updateMiningRule(resultMap, miningMap, DateUtils.formaterDate(curDateTime));

        //释放挖矿冻结账户的冻结平台币的数量
        BigDecimal releaseTotalVolume = this.releaseDistributeAccount(curDateTime, miningMap, grantTotalVolume, mkDistributeLogList);
        resultMap.put("releaseTotalVolume", String.valueOf(releaseTotalVolume));

        //插入日志与明细(缺)
        this.insertDistributeLog(mkDistributeLogList);

        logger.info(String.format("end [%s]执行挖矿规则,{}", DateUtils.formaterDate(curDateTime)), miningMap);

    }

    private List<TradeUserCoinFeeVO> calcUserTradeFee(LocalDate curDate) {

        logger.info(String.format("start [%s]计算前天交易手续费,并把手续费转成主区币种（ETH,BTC,USDT）", DateUtils.formaterDate(curDate)));

        //查询交易列表
        List<UserTradeVO> userTradeVOList = tradeDetailService.findByDate(DateUtils.formaterLocalDateTime(DateUtils.getDayStart(curDate)), DateUtils.formaterLocalDateTime(DateUtils.getDayEnd(curDate)));
        if (CollectionUtils.isEmpty(userTradeVOList)) {
            logger.info(String.format("无[%s]成交记录！", curDate));
            return null;
        }

        //计算每一笔交易的手续费，并转换成主区币种
        List<TradeUserCoinFeeVO> userCoinFeeList = new ArrayList<>();
        userTradeVOList.forEach(userTradeVO -> {
            TradeUserCoinFeeVO tradeUserCoinFee = new TradeUserCoinFeeVO();
            tradeUserCoinFee.setCoinMain(userTradeVO.getCoinMain());
            tradeUserCoinFee.setCoinOther(userTradeVO.getCoinOther());
            tradeUserCoinFee.setUserId(userTradeVO.getUserId());
            tradeUserCoinFee.setOrderNo(userTradeVO.getOrderNo());

            if (userTradeVO.getExType() == 0) { //买,手续费装换成主区币(ETH|BTC|USDT)
                tradeUserCoinFee.setExFee(userTradeVO.getExFee().multiply(userTradeVO.getPrice()));
            } else if (userTradeVO.getExType() == 1) { //卖，手续费就主区币（ETH|BTC|USDT）
                tradeUserCoinFee.setExFee(userTradeVO.getExFee());
            }
            userCoinFeeList.add(tradeUserCoinFee);
        });

        logger.info(String.format("end [%s]计算前天交易手续费,并把手续费转成主区币种（ETH, BTC,USDT）", curDate));
        return userCoinFeeList;
    }

    private BigDecimal transferTradeFeeToPlatCoin(List<TradeUserCoinFeeVO> userCoinFeeList, Map<String, BigDecimal> userRefundMap, String curDate, Map<String, Object> miningMap) {

        //获取前天，平台币/USDT,均价
        TradeExPairDayAvgVO platCoinDayAvgVO = this.getExPairDayAvg(curDate, "USDT", String.valueOf(miningMap.get("coin_symbol")));

        Map<String, TradeExPairDayAvgVO> avgMap = new HashMap<>();
        userCoinFeeList.forEach(tradeUserCoinFeeVO -> {

            //过滤费率为0
            if (tradeUserCoinFeeVO.getExFee().compareTo(new BigDecimal("0.00000001")) < 0) return;

            //1, 把所有的费率都转换成USDT
            BigDecimal curUSDTFee = BigDecimal.ZERO;
            if ("USDT".equals(tradeUserCoinFeeVO.getCoinMain())) {
                curUSDTFee = tradeUserCoinFeeVO.getExFee();
            } else if ("BTC".equals(tradeUserCoinFeeVO.getCoinMain())) {

                TradeExPairDayAvgVO btcDayAvgVO = null;
                if (avgMap.containsKey("BTC")) {
                    btcDayAvgVO = avgMap.get("BTC");
                } else {
                    btcDayAvgVO = this.getExPairDayAvg(curDate, "USDT", "BTC");
                    avgMap.put("BTC", btcDayAvgVO);
                }
                curUSDTFee = tradeUserCoinFeeVO.getExFee().multiply(btcDayAvgVO.getAvgPrice());
            } else if ("ETH".equals(tradeUserCoinFeeVO.getCoinMain())) {
                TradeExPairDayAvgVO ethDayAvgVO = null;
                if (avgMap.containsKey("ETH")) {
                    ethDayAvgVO = avgMap.get("ETH");
                } else {
                    ethDayAvgVO = this.getExPairDayAvg(curDate, "USDT", "ETH");
                    avgMap.put("ETH", ethDayAvgVO);
                }
                curUSDTFee = tradeUserCoinFeeVO.getExFee().multiply(ethDayAvgVO.getAvgPrice());
            }

            //2, USDT折算成平台币，按用户分组累加
            BigDecimal curRefundVolume = curUSDTFee.divide(platCoinDayAvgVO.getAvgPrice(), 8, BigDecimal.ROUND_HALF_DOWN);
            if (userRefundMap.containsKey(tradeUserCoinFeeVO.getUserId())) {
                userRefundMap.put(tradeUserCoinFeeVO.getUserId(), userRefundMap.get(tradeUserCoinFeeVO.getUserId()).add(curRefundVolume));
            } else {
                userRefundMap.put(tradeUserCoinFeeVO.getUserId(), curRefundVolume);
            }
        });

        //当前任务发放总量
        BigDecimal grantTotalVolume = BigDecimal.valueOf(userRefundMap.entrySet().stream().mapToDouble(e -> e.getValue().doubleValue()).sum());
        grantTotalVolume = grantTotalVolume.divide(BigDecimal.ONE, 8, BigDecimal.ROUND_HALF_DOWN);
        return grantTotalVolume;
    }

    private TradeExPairDayAvgVO getExPairDayAvg(String curDate, String coinMain, String coinOther) {
        //获取前天均价
        TradeExPairDayAvgVO avgVO = tradeDetailService.findDayAverageByExPair(curDate, coinMain, coinOther);

        //不存在均价
        if (null == avgVO) {
            throw new PlatException(Constants.DISTRIBUTE_TASK_ERROR, String.format("执行失败，无[%s][%s/%s]均价!", curDate, coinOther, coinMain));
        }
        ////当天均价小于等于零
        if (BigDecimal.ZERO.compareTo(avgVO.getAvgPrice()) >= 0) {
            throw new PlatException(Constants.DISTRIBUTE_TASK_ERROR, String.format("执行失败，[%s][%s/%s]均价为：0", curDate, coinOther, coinMain));
        }

        avgVO.setAvgPrice(avgVO.getAvgPrice().divide(BigDecimal.ONE, 8, BigDecimal.ROUND_DOWN));
        logger.info(String.format("[%s][%s/%s]的均价为：[%s]", curDate, coinOther, coinMain, String.valueOf(avgVO.getAvgPrice())));
        return avgVO;
    }

    private List<MkDistributeLog> grantPlatCoinToUser(Map<String, Object> miningMap, BigDecimal grantTotalVolume, Map<String, BigDecimal> userRefundMap, LocalDate curDateTime, List<MkDistributeLog> mkDistributeLogList) {

        BigDecimal volume = BigDecimal.valueOf(Double.valueOf(String.valueOf(miningMap.get("volume"))));//挖矿规则释放总量
        BigDecimal grantVolume = null == miningMap.get("grant_volume") ? BigDecimal.ZERO : this.transferToBigDecimal(miningMap, "grant_volume");//已发放数量
        BigDecimal remainVolume = volume.subtract(grantVolume); //当前可发放数量

        //当前超出平添可发放数量
        if (grantTotalVolume.compareTo(remainVolume) > 0) {
            logger.info(String.format("[%s]执行失败，超出平添发放数量，总量为[%s], 已发放数量[%s], 当前发放数量[%s]", DateUtils.formaterDate(curDateTime), String.valueOf(volume), String.valueOf(grantVolume), String.valueOf(grantTotalVolume)));
            throw new PlatException(Constants.DISTRIBUTE_TASK_ERROR, String.format("[%s]执行失败，超出平添发放数量，总量为[%s],已发放数量[%s],当前发放数量[%s]", DateUtils.formaterDate(curDateTime), String.valueOf(volume), String.valueOf(grantVolume), String.valueOf(grantTotalVolume)));
        }

        //更新用户资产
        userRefundMap.forEach((userId, refundVolume) -> {

            if (refundVolume.compareTo(BigDecimal.ZERO) <= 0) {
                logger.info(String.format("当前用户[%s]的返还金额过小[%s], 不执行打款！", userId, String.valueOf(refundVolume)));
                return;
            }

//            long count = userCoinVolumeService.updateIncome(OrderEnum.OrderStatus.ALL_SUCCESS, refundVolume, userId, String.valueOf(miningMap.get("coin_symbol")));
            long count = 0L;
            if (count <= 0) {
                logger.info(String.format("[%s]执行失败，更新用户资产", DateUtils.formaterDate(curDateTime)));
                throw new PlatException(Constants.DISTRIBUTE_TASK_ERROR, String.format("[%s]执行失败，更新用户资产", DateUtils.formaterDate(curDateTime)));
            }

            MkDistributeLog mkDistributeLog = new MkDistributeLog();
            Map<String, Object> userMap = mkDistributeRuleDao.findUserById(userId);
            mkDistributeLog.setMail(String.valueOf(userMap.get("mail")));
            mkDistributeLog.setMobile(String.valueOf(userMap.get("mobile")));
            mkDistributeLog.setId(UUID.randomUUID().toString().replace("-", ""));
            mkDistributeLog.setUserId(userId);
            mkDistributeLog.setUsername("");
            mkDistributeLog.setStatus("1");
            mkDistributeLog.setType(DistributeRuleTypeEnum.MINING.getCode());
            mkDistributeLog.setCoinId(String.valueOf(miningMap.get("coin_id")));
            mkDistributeLog.setCoinSymbol(String.valueOf(miningMap.get("coin_symbol")));
            mkDistributeLog.setVolume(refundVolume);
            mkDistributeLog.setCreateDate(LocalDateTime.now());
            mkDistributeLog.setRemark(String.format("挖矿规则释放总量[%s], 当前返回挖矿数量[%s]", String.valueOf(volume), String.valueOf(refundVolume)));
            mkDistributeLog.setBeginDate(DateUtils.getDayStart(curDateTime.minusDays(1)));
            mkDistributeLog.setEndDate(DateUtils.getDayEnd(curDateTime.minusDays(1)));
            mkDistributeLog.setTaskDate(LocalDateTime.of(curDateTime, LocalTime.MIN));
            mkDistributeLogList.add(mkDistributeLog);

            //当前实际挖矿返还数量
            BigDecimal realRefundVolume = BigDecimal.valueOf(mkDistributeLogList.stream().mapToDouble(e -> Double.valueOf(String.valueOf(e.getVolume()))).sum());
            if (realRefundVolume.compareTo(grantTotalVolume) > 0) {
                logger.info(String.format("[%s]执行失败，[%s]实际挖矿返还数量[%s],超过统计返还数量[%s],", DateUtils.formaterDate(curDateTime), String.valueOf(miningMap.get("coin_symbol")), realRefundVolume.toString(), grantTotalVolume.toString()));
                throw new PlatException(Constants.DISTRIBUTE_TASK_ERROR, String.format("[%s]执行失败，[%s]实际挖矿返还数量[%s],超过统计返还数量[%s],", DateUtils.formaterDate(curDateTime), String.valueOf(miningMap.get("coin_symbol")), realRefundVolume.toString(), grantTotalVolume.toString()));
            }
        });

        //返回更新用户资产日志
        return mkDistributeLogList;
    }

    private Map<String, Object> getMiningRule(LocalDate curDateTime) {
        //查询启用的挖矿规则
        Map<String, Object> miningMap = mkDistributeRuleDao.findMiningRule();
        if (MapUtils.isEmpty(miningMap)) {
            logger.info(String.format("[%s]执行失败,不存在已启动的挖矿规则，无法执行任务！", DateUtils.formaterDate(curDateTime)));
            throw new PlatException(Constants.DISTRIBUTE_TASK_ERROR, String.format("[%s]执行失败,不存在已启动的挖矿规则，无法执行任务！", DateUtils.formaterDate(curDateTime)));
        }
        return miningMap;
    }

    private BigDecimal releaseDistributeAccount(LocalDate curDateTime, Map<String, Object> miningMap, BigDecimal grantTotalVolume, List<MkDistributeLog> mkDistributeLogList) {

        //查询挖矿冻结账户列表
        List<Map<String, Object>> distributeAccountList = mkDistributeRuleDao.findDistributeAccount();
        if (CollectionUtils.isEmpty(distributeAccountList)) {
            logger.info(String.format("[%s]不存在挖矿冻结账户，无法释放！", DateUtils.formaterDate(curDateTime)));
            return BigDecimal.ZERO;
        }

        BigDecimal volume = this.transferToBigDecimal(miningMap, "volume");//按比例释放挖矿冻结账户的冻结资产
        BigDecimal releaseRate = grantTotalVolume.divide(volume, 8, BigDecimal.ROUND_HALF_DOWN);//释放比例 = grantTotalVolume(当前挖矿总数量)/volume（挖矿规则总数量）
        Map<String, BigDecimal> releaseMap = new HashMap<>(); //释放总量
        releaseMap.put("releaseTotalVolume", BigDecimal.ZERO);
        distributeAccountList.forEach(accMap -> {
            //冻结币种与挖矿币种不相符
            if (!StringUtils.equals(String.valueOf(accMap.get("coin_symbol")), String.valueOf(miningMap.get("coin_symbol")))) {
                logger.info(String.format("[%s]执行失败，挖矿冻结账户币种与挖矿规则币种名称不相符！", DateUtils.formaterDate(curDateTime)));
                throw new PlatException(Constants.DISTRIBUTE_TASK_ERROR, String.format("[%s]执行失败，挖矿冻结账户币种与挖矿规则币种名称不相符！", DateUtils.formaterDate(curDateTime)));
            }

            BigDecimal lockVolume = this.transferToBigDecimal(accMap, "lock_volume"); //当前账户冻结资产
            BigDecimal releaseVolume = this.transferToBigDecimal(accMap, "release_volume"); //当前账户已释放资产
            BigDecimal availableVolume = lockVolume.add(releaseVolume); //当前账户可释放资产 = 当前账户冻结资产 - 当前账户已释放资产
            BigDecimal curReleaseVolume = lockVolume.multiply(releaseRate); //当前释放数量 = 释放比例*冻结总资产

            if (availableVolume.compareTo(curReleaseVolume) <= 0) {
                logger.info(String.format("[%s]执行失败，挖矿冻结账户冻结数量[%s]不足释放本次释放数量！", DateUtils.formaterDate(curDateTime)));
                throw new PlatException(Constants.DISTRIBUTE_TASK_ERROR, String.format("[%s]执行失败，挖矿冻结账户冻结数量不足释放本次释放数量！", DateUtils.formaterDate(curDateTime)));
            }

            long count = mkDistributeRuleDao.updateDistributeAccount(String.valueOf(accMap.get("id")), curReleaseVolume);
            if (count <= 0) {
                logger.info(String.format("[%s]执行失败，更新挖矿冻结账户[%s]出现异常！", DateUtils.formaterDate(curDateTime), String.valueOf(accMap.get("id"))));
                throw new PlatException(Constants.DISTRIBUTE_TASK_ERROR, String.format("[%s]执行失败，更新挖矿冻结账户[%s]出现异常！", DateUtils.formaterDate(curDateTime), String.valueOf(accMap.get("user_id"))));
            }

//            count = userCoinVolumeService.updateIncome(OrderEnum.OrderStatus.ALL_SUCCESS, curReleaseVolume, String.valueOf(accMap.get("user_id")), String.valueOf(miningMap.get("coin_symbol")));
            if (count <= 0) {
                logger.info(String.format("[%s]执行失败，更新用户[%s]资产出现异常！", DateUtils.formaterDate(curDateTime), String.valueOf(accMap.get("user_id"))));
                throw new PlatException(Constants.DISTRIBUTE_TASK_ERROR, String.format("[%s]执行失败，更新用户[%s]资产出现异常！", DateUtils.formaterDate(curDateTime), String.valueOf(accMap.get("user_id"))));
            }

            if (releaseMap.containsKey("releaseTotalVolume")) {
                releaseMap.put("releaseTotalVolume", releaseMap.get("releaseTotalVolume").add(curReleaseVolume));
            }

            MkDistributeLog mkDistributeLog = new MkDistributeLog();
            Map<String, Object> userMap = mkDistributeRuleDao.findUserById(String.valueOf(accMap.get("user_id")));
            mkDistributeLog.setMail(String.valueOf(userMap.get("mail")));
            mkDistributeLog.setMobile(String.valueOf(userMap.get("mobile")));
            mkDistributeLog.setId(UUID.randomUUID().toString().replace("-", ""));
            mkDistributeLog.setUserId(String.valueOf(accMap.get("user_id")));
            mkDistributeLog.setUsername(String.valueOf(accMap.get("username")));
            mkDistributeLog.setStatus("1");
            mkDistributeLog.setType(DistributeRuleTypeEnum.MINING.getCode());
            mkDistributeLog.setCoinId(String.valueOf(miningMap.get("coin_id")));
            mkDistributeLog.setCoinSymbol(String.valueOf(miningMap.get("coin_symbol")));
            mkDistributeLog.setVolume(curReleaseVolume);
            mkDistributeLog.setCreateDate(LocalDateTime.now());
            mkDistributeLog.setRemark(String.format("用户冻结总资产[%s], 执行前用户已释放资产[%s], 当前释放资产[%s], 释放比例[%s]", String.valueOf(lockVolume), String.valueOf(releaseVolume), String.valueOf(curReleaseVolume), String.valueOf(releaseRate)));
            mkDistributeLog.setBeginDate(DateUtils.getDayStart(curDateTime.minusDays(1)));
            mkDistributeLog.setEndDate(DateUtils.getDayEnd(curDateTime.minusDays(1)));
            mkDistributeLog.setTaskDate(LocalDateTime.of(curDateTime, LocalTime.MIN));
            mkDistributeLogList.add(mkDistributeLog);
        });

        return releaseMap.get("releaseTotalVolume");
    }

    private void updateMiningRule(Map<String, Object> resultMap, Map<String, Object> miningMap, String curDate) {
        long count = mkDistributeRuleDao.updateMiningRuleById(this.transferToBigDecimal(resultMap, "grantTotalVolume"), String.valueOf(miningMap.get("id")));
        if (count <= 0) {
            throw new PlatException(Constants.DISTRIBUTE_TASK_ERROR, String.format("[%s]执行失败，更新挖矿规则发放平台币数量！", curDate));
        }
    }

    public MkCommonTaskRecord insertTaskRecord(String type, LocalDateTime taskDate, String coinId, String coinSymbol, BigDecimal volume, LocalDateTime execute_time) {
        MkCommonTaskRecord mkCommonTaskRecord = new MkCommonTaskRecord();
        mkCommonTaskRecord.setId(UUID.randomUUID().toString().replace("-", ""));
        mkCommonTaskRecord.setType(type);
        mkCommonTaskRecord.setCoinId(coinId);
        mkCommonTaskRecord.setCoinSymbol(coinSymbol);
        mkCommonTaskRecord.setVolume(volume);
        mkCommonTaskRecord.setTaskDate(taskDate);
        mkCommonTaskRecord.setExecuteTime(execute_time);
        mkCommonTaskRecord.setStatus("0");
        mkCommonTaskRecordDao.insert(mkCommonTaskRecord);
        return mkCommonTaskRecord;
    }

    public void updateTaskRecord(MkCommonTaskRecord mkCommonTaskRecord, BigDecimal volume, String status, String remark, String coinId, String coinSymbol) {
        mkCommonTaskRecord.setVolume(volume);
        mkCommonTaskRecord.setStatus(status);
        mkCommonTaskRecord.setRemark(remark);
        mkCommonTaskRecord.setCoinId(coinId);
        mkCommonTaskRecord.setCoinSymbol(coinSymbol);
        mkCommonTaskRecordDao.update(mkCommonTaskRecord);
    }

    private void insertDistributeLog(List<MkDistributeLog> mkDistributeLogList) {
        mkDistributeRuleDao.batchInsertDistributeLog(mkDistributeLogList);
    }

    private BigDecimal transferToBigDecimal(Map<String, Object> paramMap, String key) {
        return paramMap.containsKey(key) ? BigDecimal.valueOf(Double.valueOf(String.valueOf(paramMap.get(key)))) : BigDecimal.ZERO;
    }
}
