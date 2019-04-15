package com.biao.service.impl;

import com.biao.constant.Constants;
import com.biao.constant.DistributeRuleTypeEnum;
import com.biao.constant.DistributeTaskStatusEnum;
import com.biao.entity.MkCommonTaskRecord;
import com.biao.entity.MkDistributeLog;
import com.biao.entity.MkDividendStat;
import com.biao.entity.UserCoinVolume;
import com.biao.enums.OrderEnum;
import com.biao.exception.PlatException;
import com.biao.mapper.MkCommonTaskRecordDao;
import com.biao.mapper.MkDistributeRuleDao;
import com.biao.mapper.MkDividendStatDao;
import com.biao.reactive.data.mongo.service.TradeDetailService;
import com.biao.redis.RedisCacheManager;
import com.biao.service.Mk1DividendRuleService;
import com.biao.service.MkDividendRuleTaskService;
import com.biao.service.UserCoinVolumeExService;
import com.biao.util.DateUtils;
import com.biao.vo.UserTradeVO;
import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.thymeleaf.util.MapUtils;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.*;

@Service("MkDividendRuleTaskService_1")
public class MkDividendRuleTaskServiceImpl implements MkDividendRuleTaskService, Mk1DividendRuleService {

    private static org.slf4j.Logger logger = LoggerFactory.getLogger(MkDividendRuleTaskServiceImpl.class);

    private final BigDecimal PERCENTAGE_DIVIDE = BigDecimal.TEN.multiply(BigDecimal.TEN);

    @Autowired
    private MkDistributeRuleDao mkDistributeRuleDao;

    @Autowired
    private TradeDetailService tradeDetailService;

    @Autowired
    private UserCoinVolumeExService userCoinVolumeService;

    @Autowired
    private MkDividendRuleTaskServiceImpl mkDividendRuleTaskService;

//    @Autowired
//    private RedisCacheManager redisCacheManager;

    @Autowired
    private MkCommonTaskRecordDao mkCommonTaskRecordDao;

    @Autowired
    private MkDividendStatDao mkDividendStatDao;

    @Override
    public void dividendDayTaskEntry() {

        LocalDate curDateTime = LocalDate.now();
//        curDateTime = curDateTime.minusDays(-1);
        String curDate = DateUtils.formaterDate(curDateTime);

//        Map<String ,Object> recordMap = mkDistributeRuleDao.findTaskRecordByDay(curDate, DistributeRuleTypeEnum.DIVIDEND.getCode());
        MkCommonTaskRecord taskRecord = mkCommonTaskRecordDao.findByTaskDateAndType(LocalDateTime.of(curDateTime, LocalTime.MIN), DistributeRuleTypeEnum.DIVIDEND.getCode());
        if (Objects.nonNull(taskRecord)) {
            logger.info(String.format("[%s]执行失败，营销规则已存在成功记录，无需再次执行", curDate));
            return;
        }

        Map<String, Object> dividendMap = null;
        List<Map<String, Object>> dividendDetailList = null;
        Map<String, Object> resultMap = new HashMap<>();
        MkCommonTaskRecord mkCommonTaskRecord = null;

        try {
            //预先插入任务记录
            mkCommonTaskRecord = this.insertTaskRecord(DistributeRuleTypeEnum.DIVIDEND.getCode(), LocalDateTime.of(curDateTime, LocalTime.MIN), "", "", BigDecimal.ZERO, LocalDateTime.now());
            //获取营销规则
            dividendMap = this.getDividendRule(curDate);
            //获取营销规则明细
            dividendDetailList = this.getDividendDetailByDividendId(String.valueOf(dividendMap.get("id")), curDate);
            //执行分红
            mkDividendRuleTaskService.execDividendDayTaskCore(dividendMap, dividendDetailList, resultMap, curDateTime);
        } catch (PlatException e) {
            logger.error(e.getMsg());
            this.updateTaskRecord(mkCommonTaskRecord, BigDecimal.ZERO, DistributeTaskStatusEnum.FAILURE.getCode(), e.getMsg(), "", "");
            return;
        } catch (Exception e) {
            logger.error(String.format("[%s]执行失败，系统异常！", curDate) + "message：" + e);
            this.updateTaskRecord(mkCommonTaskRecord, BigDecimal.ZERO, DistributeTaskStatusEnum.FAILURE.getCode(), String.format("[%s]执行失败，系统异常！", curDate), "", "");
            return;
        }

        //更新营销规则任务记录
        StringBuilder details = new StringBuilder("");
        dividendDetailList.forEach(map -> {
            String accountType = "1".equals(String.valueOf(map.get("account_type"))) ? "普通会员" : "平台";
            if (StringUtils.isEmpty(details.toString())) {
                details.append(String.format("[%s,%s,%s]", accountType, String.valueOf(map.get("percentage")), String.valueOf(map.get("remark"))));
            } else {
                details.append(String.format(",[%s,%s,%s]", accountType, String.valueOf(map.get("percentage")), String.valueOf(map.get("remark"))));
            }
        });

        String remark = String.format("执行成功，%s; 手续费占比[%s], 用户持有币种[%s], 分红明细%s", String.valueOf(resultMap.get("remark")), String.valueOf(dividendMap.get("percentage")), String.valueOf(dividendMap.get("plat_coin_symbol")), details);
        this.updateTaskRecord(mkCommonTaskRecord, BigDecimal.ZERO, DistributeTaskStatusEnum.SUCCESS.getCode(), remark, "", "");
    }


    @Transactional
    public void execDividendDayTaskCore(Map<String, Object> dividendMap, List<Map<String, Object>> dividendDetailList, Map<String, Object> resultMap, LocalDate curDateTime) {

        Arrays.asList("USDT", "BTC", "ETH").forEach(coin_symbol -> {
            dividendMap.put("coin_symbol", coin_symbol.toString());
            resultMap.put("grantTotalVolume", BigDecimal.ZERO);
            resultMap.put("realGrantVolume", BigDecimal.ZERO);

            this.remitDividend(dividendMap, dividendDetailList, resultMap, curDateTime);

            resultMap.put(coin_symbol.concat("GrantTotalVolume"), this.transferToBigDecimal(resultMap, "grantTotalVolume"));
            resultMap.put(coin_symbol.concat("RealGrantVolume"), this.transferToBigDecimal(resultMap, "realGrantVolume"));

            if (resultMap.containsKey("remark")) {
                resultMap.put("remark", String.valueOf(resultMap.get("remark")) + String.format(";[%s]实际分红数量[%s], 手续费资产[%s]", coin_symbol, String.valueOf(resultMap.get("realGrantVolume")), String.valueOf(resultMap.get("grantTotalVolume"))));
            } else {
                resultMap.put("remark", String.format("[%s]实际分红数量[%s], 手续费资产[%s]", coin_symbol, String.valueOf(resultMap.get("realGrantVolume")), String.valueOf(resultMap.get("grantTotalVolume"))));
            }
        });

        //插入分红统计
        this.insertDividendStat(dividendMap, resultMap, curDateTime);
    }

    private void insertDividendStat(Map<String, Object> dividendMap, Map<String, Object> resultMap, LocalDate curDateTime) {
//        long count = mkDistributeRuleDao.insertDividendStat(UUID.randomUUID().toString().replace("-",""), LocalDateTime.of(curDateTime.minusDays(1), LocalTime.MIN),String.valueOf(dividendMap.get("plat_coin_id")),String.valueOf(dividendMap.get("plat_coin_symbol")),this.transferToBigDecimal(resultMap,"totalPlatCoinVolume"),
//                                                this.transferToBigDecimal(resultMap,"USDTGrantTotalVolume"), this.transferToBigDecimal(resultMap,"BTCGrantTotalVolume"), this.transferToBigDecimal(resultMap,"ETHGrantTotalVolume"),
//                                                this.transferToBigDecimal(resultMap,"USDTRealGrantVolume"), this.transferToBigDecimal(resultMap,"BTCRealGrantVolume"), this.transferToBigDecimal(resultMap,"ETHRealGrantVolume"),new BigDecimal("1000"));

        BigDecimal per = new BigDecimal("1000");
        BigDecimal perRate = per.divide(this.transferToBigDecimal(resultMap, "totalPlatCoinVolume"), 8, BigDecimal.ROUND_DOWN);

        MkDividendStat mkDividendStat = new MkDividendStat();
        mkDividendStat.setId(UUID.randomUUID().toString().replace("-", ""));
        mkDividendStat.setStatDate(LocalDateTime.of(curDateTime.minusDays(1), LocalTime.MIN));
        mkDividendStat.setCoinId(String.valueOf(dividendMap.get("plat_coin_id")));
        mkDividendStat.setCoinSymbol(String.valueOf(dividendMap.get("plat_coin_symbol")));
        mkDividendStat.setVolume(this.transferToBigDecimal(resultMap, "totalPlatCoinVolume"));
        mkDividendStat.setUsdtVolume(this.transferToBigDecimal(resultMap, "USDTGrantTotalVolume"));
        mkDividendStat.setBtcVolume(this.transferToBigDecimal(resultMap, "BTCGrantTotalVolume"));
        mkDividendStat.setEthVolume(this.transferToBigDecimal(resultMap, "ETHGrantTotalVolume"));
        mkDividendStat.setUsdtRealVolume(this.transferToBigDecimal(resultMap, "USDTRealGrantVolume"));
        mkDividendStat.setBtcRealVolume(this.transferToBigDecimal(resultMap, "BTCRealGrantVolume"));
        mkDividendStat.setEthRealVolume(this.transferToBigDecimal(resultMap, "ETHRealGrantVolume"));
        mkDividendStat.setPer(per);
        mkDividendStat.setUsdtPerVolume(mkDividendStat.getUsdtRealVolume().multiply(perRate));
        mkDividendStat.setBtcPerVolume(mkDividendStat.getBtcRealVolume().multiply(perRate));
        mkDividendStat.setEthPerVolume(mkDividendStat.getEthVolume().multiply(perRate));
        mkDividendStat.setRemark("");
        mkDividendStat.setCreateDate(LocalDateTime.now());

        long count = mkDividendStatDao.insert(mkDividendStat);

        if (count <= 0) {
            logger.info(String.format("[%s]执行失败，插入分红统计！", DateUtils.formaterDate(curDateTime)));
            throw new PlatException(Constants.DISTRIBUTE_TASK_ERROR, String.format("[%s]执行失败，插入分红统计！", DateUtils.formaterDate(curDateTime)));
        }
    }

    private void remitDividend(Map<String, Object> dividendMap, List<Map<String, Object>> dividendDetailList, Map<String, Object> resultMap, LocalDate curDateTime) {

        //获取用户平台账户快照的数量
        List<Map<String, Object>> userVolumeList = this.getUserVolumeSnapshotList(DateUtils.formaterDate(curDateTime.minusDays(1)), String.valueOf(dividendMap.get("plat_coin_symbol")));
        BigDecimal totalPlatCoinVolume = BigDecimal.valueOf(userVolumeList.stream().mapToDouble(e -> Double.valueOf(String.valueOf(e.get("volume")))).sum());
        resultMap.put("totalPlatCoinVolume", totalPlatCoinVolume);

        //查询手续费账户当前的USDT余额
        UserCoinVolume userCoinVolume = userCoinVolumeService.findByUserIdAndCoinSymbol(String.valueOf(dividendMap.get("user_id")), String.valueOf(dividendMap.get("coin_symbol")));

        if (ObjectUtils.isEmpty(userCoinVolume) || StringUtils.isEmpty(userCoinVolume.getUserId())) {
            logger.info(String.format("[%s]执行失败，手续费资产[%s]账户为空！", DateUtils.formaterDate(curDateTime), String.valueOf(dividendMap.get("coin_symbol"))));
            return;
        }

        BigDecimal grantTotalVolume = userCoinVolume.getVolume();
        grantTotalVolume = grantTotalVolume.divide(BigDecimal.ONE, 8, BigDecimal.ROUND_DOWN);
        resultMap.put("grantTotalVolume", grantTotalVolume);

        if (BigDecimal.ZERO.compareTo(grantTotalVolume) >= 0) {
            logger.info(String.format("[%s]执行失败，手续费资产[%s]为0！", DateUtils.formaterDate(curDateTime), String.valueOf(dividendMap.get("coin_symbol"))));
            return;
        }

        //锁定手续费账户用户
        userCoinVolumeService.addLockVolume(userCoinVolume.getUserId(), userCoinVolume.getCoinSymbol(), userCoinVolume.getVolume(), true);
//        redisCacheManager.cleanUserCoinVolume(userCoinVolume.getUserId(), userCoinVolume.getCoinSymbol());

        //营销日志
        List<MkDistributeLog> mkDistributeLogList = new ArrayList<>();

        //按比例把USDT打款给用户
        this.remitMainCoinDividend(userVolumeList, userCoinVolume.getVolume(), dividendMap, dividendDetailList, mkDistributeLogList, curDateTime);

        //实际分红数量
        BigDecimal realGrantVolume = BigDecimal.valueOf(mkDistributeLogList.stream().mapToDouble(mkDistributeLog -> Double.valueOf(String.valueOf(mkDistributeLog.getVolume()))).sum());
        realGrantVolume = realGrantVolume.divide(BigDecimal.ONE, 8, BigDecimal.ROUND_DOWN);
        resultMap.put("realGrantVolume", realGrantVolume);

        //释放手续账户的当前锁定余额
        userCoinVolumeService.subtractLockVolume(userCoinVolume.getUserId(), userCoinVolume.getCoinSymbol(), grantTotalVolume, false);
//        redisCacheManager.cleanUserCoinVolume(userCoinVolume.getUserId(), userCoinVolume.getCoinSymbol());

        //把剩余数量返回到平台账户
        if (grantTotalVolume.subtract(realGrantVolume).compareTo(BigDecimal.ZERO) > 0) {
//            userCoinVolumeService.updateIncome(OrderEnum.OrderStatus.ALL_SUCCESS, grantTotalVolume.subtract(realGrantVolume), userCoinVolume.getUserId(), userCoinVolume.getCoinSymbol());
//            redisCacheManager.cleanUserCoinVolume(userCoinVolume.getUserId(), userCoinVolume.getCoinSymbol());
        }

        if (CollectionUtils.isEmpty(mkDistributeLogList)) {
            logger.info("分红流水为空！");
            return;
        }

        this.updateDividendRule(resultMap, dividendMap, curDateTime);

        //插入日志与明细(缺)
        this.insertDistributeLog(mkDistributeLogList);
    }


    private void remitMainCoinDividend(List<Map<String, Object>> userVolumeList, BigDecimal coinMainTradeFee, Map<String, Object> dividendMap, List<Map<String, Object>> dividendDetailList, List<MkDistributeLog> mkDistributeLogList, LocalDate curDateTime) {

        BigDecimal percentage = this.transferToBigDecimal(dividendMap, "percentage");
        final BigDecimal totalVolume = coinMainTradeFee.multiply(percentage).divide(PERCENTAGE_DIVIDE, 8, BigDecimal.ROUND_DOWN);

        dividendDetailList.forEach(detailMap -> {
            BigDecimal curPercentage = this.transferToBigDecimal(detailMap, "percentage");//当前营销明细的百分比
            BigDecimal curVolume = totalVolume.multiply(curPercentage).divide(PERCENTAGE_DIVIDE, 8, BigDecimal.ROUND_DOWN); //当前营销明细的分红数量
            String accountType = String.valueOf(detailMap.get("account_type")); //当前营销的用户类型

            if (curVolume.compareTo(BigDecimal.ZERO) <= 0) {
                logger.info(String.format("当前分红占比太小，无法分红"));
                return;
            }

            //会员直接分享所有持有平台的用户
            if ("1".equals(accountType)) {
                detailMap.put("grantVolume", String.valueOf(curVolume));
                this.remitDividendToUser(userVolumeList, detailMap, dividendMap, mkDistributeLogList, curDateTime);
            } else {
                //其他类型直接打给用户
                String userId = String.valueOf(String.valueOf(detailMap.get("user_id")));

//                long count = userCoinVolumeService.updateIncome(OrderEnum.OrderStatus.ALL_SUCCESS, curVolume, userId, String.valueOf(dividendMap.get("coin_symbol")));
                long count = 0L;
//                redisCacheManager.cleanUserCoinVolume(userId, String.valueOf(dividendMap.get("coin_symbol")));
                if (count <= 0) {
                    throw new PlatException(Constants.DISTRIBUTE_TASK_ERROR, String.format("[%s]执行失败，更新用户资产", DateUtils.formaterDate(curDateTime)));
                }

                MkDistributeLog mkDistributeLog = new MkDistributeLog();
                Map<String, Object> userMap = mkDistributeRuleDao.findUserById(userId);
                mkDistributeLog.setMail(String.valueOf(userMap.get("mail")));
                mkDistributeLog.setMobile(String.valueOf(userMap.get("mobile")));
                mkDistributeLog.setId(UUID.randomUUID().toString().replace("-", ""));
                mkDistributeLog.setUserId(userId);
                mkDistributeLog.setUsername(String.valueOf(detailMap.get("username")));
                mkDistributeLog.setStatus("1");
                mkDistributeLog.setType(DistributeRuleTypeEnum.DIVIDEND.getCode());
                mkDistributeLog.setCoinId("");
                mkDistributeLog.setCoinSymbol(String.valueOf(dividendMap.get("coin_symbol")));
                mkDistributeLog.setVolume(curVolume);
                mkDistributeLog.setCreateDate(LocalDateTime.now());
                mkDistributeLog.setRemark(String.format("分红总资产[%s], 该平台账户占比[%s]%%, 分红数量[%s]", String.valueOf(totalVolume), String.valueOf(curPercentage), String.valueOf(curVolume)));
                mkDistributeLog.setBeginDate(DateUtils.getDayStart(curDateTime.minusDays(1)));
                mkDistributeLog.setEndDate(DateUtils.getDayEnd(curDateTime.minusDays(1)));
                mkDistributeLog.setTaskDate(LocalDateTime.of(curDateTime, LocalTime.MIN));
                mkDistributeLogList.add(mkDistributeLog);
            }

            //当前实际分红数量
            BigDecimal realGrantVolume = BigDecimal.valueOf(mkDistributeLogList.stream().mapToDouble(mkDistributeLog -> Double.valueOf(String.valueOf(mkDistributeLog.getVolume()))).sum());
            if (realGrantVolume.compareTo(coinMainTradeFee) > 0) {
                throw new PlatException(Constants.DISTRIBUTE_TASK_ERROR, String.format("[%s]执行失败，[%s]实际分红数量[%s],超过手续费资产[%s],",
                        DateUtils.formaterDate(curDateTime), String.valueOf(dividendMap.get("coin_symbol")), realGrantVolume.toString(), coinMainTradeFee.toString()));
            }
        });
    }

    private void remitDividendToUser(List<Map<String, Object>> userVolumeList, Map dividendDetailMap, Map dividendMap, List<MkDistributeLog> mkDistributeLogList, LocalDate curDateTime) {

        final BigDecimal grantVolume = this.transferToBigDecimal(dividendDetailMap, "grantVolume").divide(BigDecimal.ONE, 8, BigDecimal.ROUND_DOWN);

        //所有用户持有平台的总数量
        BigDecimal totalPlatCoinVolume = BigDecimal.valueOf(userVolumeList.stream().mapToDouble(e -> Double.valueOf(String.valueOf(e.get("volume")))).sum());
        userVolumeList.forEach(volumeMap -> {
            String userId = String.valueOf(String.valueOf(volumeMap.get("user_id")));
            BigDecimal curVolume = this.transferToBigDecimal(volumeMap, "volume"); //当前用户持有的平台币数量
            curVolume = curVolume.divide(BigDecimal.ONE, 8, BigDecimal.ROUND_DOWN);
            BigDecimal curGrantRate = curVolume.divide(totalPlatCoinVolume, 8, BigDecimal.ROUND_DOWN); //当前用户持有平台币的占比

            if (curGrantRate.compareTo(BigDecimal.ZERO) <= 0) {
                logger.info(String.format("当前用户[%s]持有平台币占比太小，无法分红", userId));
                return;
            }

            BigDecimal curGrantVolume = grantVolume.multiply(curGrantRate); //当前用户分红数量
            curGrantVolume = curGrantVolume.divide(BigDecimal.ONE, 8, BigDecimal.ROUND_DOWN);
            if (curGrantVolume.compareTo(BigDecimal.ZERO) <= 0) {
                logger.info(String.format("当前用户[%s]分红数量太小，无法分红", userId));
                return;
            }

            //把分红打款给用户
//            long count = userCoinVolumeService.updateIncome(OrderEnum.OrderStatus.ALL_SUCCESS, curGrantVolume, userId, String.valueOf(dividendMap.get("coin_symbol")));
            long count = 0L;
//            redisCacheManager.cleanUserCoinVolume(userId, String.valueOf(dividendMap.get("coin_symbol")));
            if (count <= 0) {
                throw new PlatException(Constants.DISTRIBUTE_TASK_ERROR, String.format("[%s]执行失败，更新用户资产", DateUtils.formaterDate(curDateTime)));
            }

            MkDistributeLog mkDistributeLog = new MkDistributeLog();
            Map<String, Object> userMap = mkDistributeRuleDao.findUserById(userId);

            if (MapUtils.isEmpty(userMap)) {
                throw new PlatException(Constants.DISTRIBUTE_TASK_ERROR, String.format("[%s]执行失败，无法查询用户[%s]信息", DateUtils.formaterDate(curDateTime), userId));
            }

            mkDistributeLog.setMail(String.valueOf(userMap.get("mail")));
            mkDistributeLog.setMobile(String.valueOf(userMap.get("mobile")));
            mkDistributeLog.setId(UUID.randomUUID().toString().replace("-", ""));
            mkDistributeLog.setUserId(userId);
            mkDistributeLog.setUsername(String.valueOf(dividendDetailMap.get("username")));
            mkDistributeLog.setStatus("1");
            mkDistributeLog.setType(DistributeRuleTypeEnum.DIVIDEND.getCode());
            mkDistributeLog.setCoinId("");
            mkDistributeLog.setCoinSymbol(String.valueOf(dividendMap.get("coin_symbol")));
            mkDistributeLog.setVolume(curGrantVolume);
            mkDistributeLog.setCreateDate(LocalDateTime.now());
            mkDistributeLog.setRemark(String.format("会员分红资产[%s], 平台币总量[%s], 用户持有量[%s], 占比[%s]%%, 分红数量[%s]", String.valueOf(grantVolume), String.valueOf(totalPlatCoinVolume), String.valueOf(curVolume), String.valueOf(curGrantRate.multiply(PERCENTAGE_DIVIDE)), String.valueOf(curGrantVolume)));
            mkDistributeLog.setBeginDate(DateUtils.getDayStart(curDateTime.minusDays(1)));
            mkDistributeLog.setEndDate(DateUtils.getDayEnd(curDateTime.minusDays(1)));
            mkDistributeLog.setTaskDate(LocalDateTime.of(curDateTime, LocalTime.MIN));
            mkDistributeLogList.add(mkDistributeLog);

        });

    }

    private Map<String, Object> getDividendRule(String curDate) {

        Map<String, Object> dividendMap = mkDistributeRuleDao.findDividendRule();
        if (MapUtils.isEmpty(dividendMap)) {
            logger.info(String.format("[%s]执行失败,不存在已启动的分红规则，无法执行任务！", curDate));
            throw new PlatException(Constants.DISTRIBUTE_TASK_ERROR, String.format("[%s]执行失败,不存在已启动的分红规则，无法执行任务！", curDate));
        }
        return dividendMap;
    }

    private List<Map<String, Object>> getDividendDetailByDividendId(String dividendId, String curDate) {

        List<Map<String, Object>> dividendDetailList = mkDistributeRuleDao.findDividendDetailByDividendId(dividendId);
        if (CollectionUtils.isEmpty(dividendDetailList)) {
            logger.info(String.format("[%s]执行失败,不存在已启动的分红规则明细，无法执行任务！", curDate));
            throw new PlatException(Constants.DISTRIBUTE_TASK_ERROR, String.format("[%s]执行失败,不存在已启动的分红规则明细，无法执行任务！", curDate));
        }
        return dividendDetailList;
    }

    private List<Map<String, Object>> getUserVolumeSnapshotList(String curDate, String coinSymbol) {
        List<Map<String, Object>> userVolumeList = mkDistributeRuleDao.findUserVolumeSnapshotList(curDate, coinSymbol);
        if (CollectionUtils.isEmpty(userVolumeList)) {
            logger.info(String.format("执行失败,不存在[%s]的用户资产快照，无法执行任务！", curDate));
            throw new PlatException(Constants.DISTRIBUTE_TASK_ERROR, String.format("执行失败,不存在[%s]的用户资产快照，无法执行任务！", curDate));
        }
        return userVolumeList;
    }

    @Override
    public void triggerRemitFeeToPlatAccount() {

        LocalDateTime curDateTime = LocalDateTime.now();
        String curDate = DateUtils.formaterDate(curDateTime.toLocalDate());
        LocalDateTime lastDateTime = null;

        try {
            lastDateTime = this.getAndCheckLastSuccessLog(curDateTime);
            logger.info(String.format("最近执行时间为：%s, 当前执行时间为：%s", DateUtils.formaterLocalDateTime(lastDateTime), DateUtils.formaterLocalDateTime(curDateTime)));
        } catch (Exception e) {
            logger.error("获取最近执行时间出现异常！", e);
            return;
        }

        MkCommonTaskRecord mkCommonTaskRecord = null;
        Map<String, Object> dividendMap = null;
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("result", "");

        try {
            //获取营销规则
            dividendMap = this.getDividendRule(curDate);
        } catch (PlatException e) {
            logger.error(e.getMsg());
            return;
        } catch (Exception e) {
            logger.error(String.format("[%s]执行失败，系统异常！", curDate) + "message：" + e.getMessage());
            return;
        }

        try {
            //预先插入日志
            mkCommonTaskRecord = this.insertTaskRecord(DistributeRuleTypeEnum.EXFEE_REMIT.getCode(), LocalDateTime.of(curDateTime.toLocalDate(), LocalTime.MIN), "", "", BigDecimal.ZERO, curDateTime);
            //手续费归集
            mkDividendRuleTaskService.exeRemitFeeToPlatAccountCore(dividendMap, resultMap, lastDateTime, curDateTime);
        } catch (PlatException e) {
            logger.error(e.getMsg());
            this.updateTaskRecord(mkCommonTaskRecord, BigDecimal.ZERO, DistributeTaskStatusEnum.FAILURE.getCode(), e.getMsg(), "", "");
            return;
        } catch (Exception e) {
            logger.error(String.format("[%s]执行失败，系统异常！", curDate) + "message：" + e.getMessage());
            this.updateTaskRecord(mkCommonTaskRecord, BigDecimal.ZERO, DistributeTaskStatusEnum.FAILURE.getCode(), String.format("[%s]执行失败，系统异常！", curDate), "", "");
            return;
        }

        //更新营销规则任务记录
        logger.info(String.format("[%s]执行成功，[%s]", curDate, String.valueOf(resultMap.get("result"))));
        this.updateTaskRecord(mkCommonTaskRecord, BigDecimal.ZERO, DistributeTaskStatusEnum.SUCCESS.getCode(), String.valueOf(resultMap.get("result")), "", "");

    }

    public LocalDateTime getAndCheckLastSuccessLog(LocalDateTime curDateTime) {

        LocalDateTime lastDateTime = null;
        MkCommonTaskRecord mkCommonTaskRecord = mkCommonTaskRecordDao.findOne(DistributeRuleTypeEnum.EXFEE_REMIT.getCode());

        if (Objects.nonNull(mkCommonTaskRecord)) {
            lastDateTime = mkCommonTaskRecord.getExecuteTime();
        } else {
            try {
                lastDateTime = DateUtils.getDayStart(DateUtils.parseLocalDate("2018-01-01"));
            } catch (Exception e) {
                logger.error("手续费打款-记录成功执行日期解析异常!");
                throw new PlatException(Constants.DISTRIBUTE_TASK_ERROR, "记录成功执行日期解析异常!");
            }
        }

        if (curDateTime.isBefore(lastDateTime)) {
            logger.error(String.format("当前执行时间必须在最近执行时间之后，当前时间[%s],最近执行时间[%s]", DateUtils.formaterLocalDateTime(curDateTime), DateUtils.formaterLocalDateTime(lastDateTime)));
            throw new PlatException(Constants.OPERRATION_ERROR, String.format("当前执行时间必须在最近执行时间之后，当前时间[%s],最近执行时间[%s]", DateUtils.formaterLocalDateTime(curDateTime), DateUtils.formaterLocalDateTime(lastDateTime)));
        }

        return lastDateTime;
    }


    @Transactional
    public void exeRemitFeeToPlatAccountCore(Map<String, Object> dividendMap, Map<String, Object> resultMap, LocalDateTime lastDateTime, LocalDateTime curDateTime) {

        Map<String, Object> coinFeeMap = this.calcCoinTradeFee(lastDateTime, curDateTime);
        if (MapUtils.isEmpty(coinFeeMap)) {
            logger.info(String.format("[%s]无成交记录！", curDateTime.toLocalDate()));
            resultMap.put("result", "无成交记录,手续费为0;");
            return;
        }

        List<MkDistributeLog> mkDistributeLogList = new ArrayList<>();
        this.remitCoinFeeToUser(coinFeeMap, dividendMap, mkDistributeLogList, resultMap, lastDateTime, curDateTime);

        if (!CollectionUtils.isEmpty(mkDistributeLogList)) {
            this.insertDistributeLog(mkDistributeLogList);
        }

    }

    private Map<String, Object> calcCoinTradeFee(LocalDateTime lastDateTime, LocalDateTime curDateTime) {

        //查询交易列表
        List<UserTradeVO> userTradeVOList = tradeDetailService.findByDate(DateUtils.formaterLocalDateTime(lastDateTime), DateUtils.formaterLocalDateTime(curDateTime));
        if (CollectionUtils.isEmpty(userTradeVOList)) {
            logger.info(String.format("[%s]无成交记录！", curDateTime.toLocalDate()));
            return null;
        }

        Map<String, Object> coinFeeMap = new HashMap<>();
        userTradeVOList.forEach(userTradeVO -> {
            String curCoinSymbol = "";
            if (userTradeVO.getExType() == 0) { //买
                curCoinSymbol = userTradeVO.getCoinOther();
            } else if (userTradeVO.getExType() == 1) { //卖
                curCoinSymbol = userTradeVO.getCoinMain();
            }

            if (userTradeVO.getExFee().compareTo(BigDecimal.ZERO) > 0) {
                if (coinFeeMap.containsKey(curCoinSymbol)) {
                    coinFeeMap.put(curCoinSymbol, this.transferToBigDecimal(coinFeeMap, curCoinSymbol).add(userTradeVO.getExFee()));
                } else {
                    coinFeeMap.put(curCoinSymbol, userTradeVO.getExFee());
                }
            }
        });

        return coinFeeMap;
    }

    private void remitCoinFeeToUser(Map<String, Object> coinFeeMap, Map dividendMap, List<MkDistributeLog> mkDistributeLogList, Map<String, Object> resultMap, LocalDateTime lastDateTime, LocalDateTime curDateTime) {

        coinFeeMap.forEach((coinSymbol, exFee) -> {

            //把分红打款给用户
//            long count = userCoinVolumeService.updateIncome(OrderEnum.OrderStatus.ALL_SUCCESS, BigDecimal.valueOf(Double.valueOf(String.valueOf(exFee))), String.valueOf(dividendMap.get("user_id")), coinSymbol);
            long count = 0L;
//            redisCacheManager.cleanUserCoinVolume(String.valueOf(dividendMap.get("user_id")), coinSymbol);

            if (count <= 0) {
                throw new PlatException(Constants.DISTRIBUTE_TASK_ERROR, String.format("[%s]执行失败，更新用户资产", DateUtils.formaterDate(curDateTime.toLocalDate())));
            }

            MkDistributeLog mkDistributeLog = new MkDistributeLog();
            Map<String, Object> userMap = mkDistributeRuleDao.findUserById(String.valueOf(dividendMap.get("user_id")));
            mkDistributeLog.setMail(String.valueOf(userMap.get("mail")));
            mkDistributeLog.setMobile(String.valueOf(userMap.get("mobile")));
            mkDistributeLog.setId(UUID.randomUUID().toString().replace("-", ""));
            mkDistributeLog.setUserId(String.valueOf(dividendMap.get("user_id")));
            mkDistributeLog.setUsername(String.valueOf(dividendMap.get("username")));
            mkDistributeLog.setStatus("1");
            mkDistributeLog.setType(DistributeRuleTypeEnum.EXFEE_REMIT.getCode());
            mkDistributeLog.setCoinId("");
            mkDistributeLog.setCoinSymbol(coinSymbol);
            mkDistributeLog.setVolume(BigDecimal.valueOf(Double.valueOf(String.valueOf(exFee))));
            mkDistributeLog.setCreateDate(LocalDateTime.now());
            mkDistributeLog.setRemark(String.format("币种[%s], 手续费数量[%s]", coinSymbol, String.valueOf(exFee)));
            mkDistributeLog.setBeginDate(lastDateTime);
            mkDistributeLog.setEndDate(curDateTime);
            mkDistributeLog.setTaskDate(LocalDateTime.of(curDateTime.toLocalDate(), LocalTime.MIN));
            mkDistributeLogList.add(mkDistributeLog);

            if (resultMap.containsKey("result") && StringUtils.isNotEmpty(String.valueOf(resultMap.get("result")))) {
                resultMap.put("result", String.valueOf(resultMap.get("result")) + String.format(",[%s:%s]", coinSymbol, String.valueOf(exFee)));
            } else {
                resultMap.put("result", String.format("[%s:%s], [%s:%s]", mkDistributeLog.getUserId(), mkDistributeLog.getMail(), coinSymbol, String.valueOf(exFee)));
            }
        });
    }

    @Override
    @Transactional
    public void triggerUserCoinVolumeSnapshot() {

        try {
            LocalDate curDateTime = LocalDate.now();
//            curDateTime = curDateTime.minusDays(1);
            String curDate = DateUtils.formaterDate(curDateTime);

            mkDistributeRuleDao.deleteUserVolumeSnapshot(curDate);
            long count = mkDistributeRuleDao.findUserVolumeSnapshot(curDate);
            if (count > 0) {
                logger.info(String.format("[%s]执行失败,已存在用户资产快照！", curDate));
                return;
            } else {
                count = mkDistributeRuleDao.insertUserVolumeSnapshot(curDate);
                if (count == 0) {
                    logger.info(String.format("[%s]执行失败,生成用户资产快照失败！", curDate));
                    return;
                }
                logger.info(String.format("[%s]执行成功-生成用户资产快照！", curDate));
            }
        } catch (Exception e) {
            logger.info("生成用户快照失败，" + e.getMessage());
        }

    }

    private void insertDistributeLog(List<MkDistributeLog> mkDistributeLogList) {
        mkDistributeRuleDao.batchInsertDistributeLog(mkDistributeLogList);
    }

    private void updateDividendRule(Map<String, Object> resultMap, Map<String, Object> dividendMap, LocalDate curDateTime) {
//        long count = mkDistributeRuleDao.updateDividendRuleById(this.transferToBigDecimal(resultMap,"realGrantVolume"), String.valueOf(dividendMap.get("id")));
        long count = mkDistributeRuleDao.updateDividendRuleByIdAndCoin(this.transferToBigDecimal(resultMap, "realGrantVolume"), String.valueOf(dividendMap.get("id")), String.valueOf(dividendMap.get("coin_symbol")).toLowerCase().concat("_grant_volume"));
        if (count <= 0) {
            throw new PlatException(Constants.DISTRIBUTE_TASK_ERROR, String.format("[%s]执行失败，更新分红规则发放数量！", DateUtils.formaterDate(curDateTime)));
        }
    }

    private BigDecimal transferToBigDecimal(Map<String, Object> paramMap, String key) {
        return paramMap.containsKey(key) ? BigDecimal.valueOf(Double.valueOf(String.valueOf(paramMap.get(key)))) : BigDecimal.ZERO;
    }

    public static LocalDateTime localDateFromTimestamp(Timestamp timestamp) {
        return LocalDateTime.ofInstant(timestamp.toInstant(), ZoneOffset.ofHours(0));
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

}
