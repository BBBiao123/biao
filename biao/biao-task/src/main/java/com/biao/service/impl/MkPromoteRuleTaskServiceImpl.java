package com.biao.service.impl;

import com.biao.constant.Constants;
import com.biao.constant.DistributeRuleTypeEnum;
import com.biao.constant.DistributeTaskStatusEnum;
import com.biao.entity.MkCommonTaskRecord;
import com.biao.entity.MkDistributeLog;
import com.biao.entity.UserRelation;
import com.biao.enums.OrderEnum;
import com.biao.exception.PlatException;
import com.biao.mapper.MkCommonTaskRecordDao;
import com.biao.mapper.MkDistributeRuleDao;
import com.biao.mapper.PlatUserDao;
import com.biao.reactive.data.mongo.service.TradeDetailService;
import com.biao.redis.RedisCacheManager;
import com.biao.service.MkPromoteRuleTaskService;
import com.biao.service.UserCoinVolumeExService;
import com.biao.util.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.thymeleaf.util.MapUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service("MkPromoteRuleTaskService_1")
public class MkPromoteRuleTaskServiceImpl implements MkPromoteRuleTaskService {

    private static org.slf4j.Logger logger = LoggerFactory.getLogger(MkPromoteRuleTaskServiceImpl.class);

    private final BigDecimal PERCENTAGE_DIVIDE = BigDecimal.TEN.multiply(BigDecimal.TEN);

    @Autowired
    private MkDistributeRuleDao mkDistributeRuleDao;

    @Autowired
    private TradeDetailService tradeDetailService;

    @Autowired
    private UserCoinVolumeExService userCoinVolumeService;

    @Autowired
    private MkPromoteRuleTaskServiceImpl mkPromoteRuleTaskService;

    @Autowired
    private PlatUserDao platUserDao;

    @Autowired
    RedisCacheManager redisCacheManager;

    @Autowired
    MkCommonTaskRecordDao mkCommonTaskRecordDao;

    @Override
    public void promoteDayTaskEntry() {

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
        Map<String, Object> promoteMap = null; //会员推广规则
        List<Map<String, Object>> promoteDetailList = null; //;会员推广明细
        Map<String, Object> resultMap = new HashMap<>(); //结果集
        resultMap.put("grantTotalVolume", BigDecimal.ZERO);

        try {
            mkCommonTaskRecord = this.insertTaskRecord(DistributeRuleTypeEnum.PROMOTE.getCode(), LocalDateTime.of(curDateTime.toLocalDate(), LocalTime.MIN), "", "", BigDecimal.ZERO, curDateTime);
            promoteMap = this.getPromoteRule(curDate);
            promoteDetailList = this.getPromoteDetailByPromoteId(String.valueOf(promoteMap.get("id")), curDate);
            mkPromoteRuleTaskService.execPromoteDayTaskCore(promoteMap, promoteDetailList, resultMap, lastDateTime, curDateTime);
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
        StringBuilder details = new StringBuilder("");
        promoteDetailList.forEach(map -> {
            if (StringUtils.isEmpty(details.toString())) {
                details.append(String.format("[%s,%s]", String.valueOf(map.get("level")), String.valueOf(map.get("volume"))));
            } else {
                details.append(String.format(",[%s,%s]", String.valueOf(map.get("level")), String.valueOf(map.get("volume"))));
            }
        });


        String remark = "";
        if (BigDecimal.ZERO.compareTo(this.transferToBigDecimal(resultMap, "grantTotalVolume")) == 0) {
            remark = "无实名认证用户！";
        } else {
            remark = String.format("执行成功, 推广总量[%s],任务执行前已发放数量[%s],推广明细%s", String.valueOf(promoteMap.get("volume")), String.valueOf(promoteMap.get("grant_volume")), details.toString());
        }

        this.updateTaskRecord(mkCommonTaskRecord, this.transferToBigDecimal(resultMap, "grantTotalVolume"), DistributeTaskStatusEnum.SUCCESS.getCode(), remark, String.valueOf(promoteMap.get("coin_id")), String.valueOf(promoteMap.get("coin_symbol")));
    }


    public LocalDateTime getAndCheckLastSuccessLog(LocalDateTime curDateTime) {
        LocalDateTime lastDateTime = null;
        MkCommonTaskRecord mkCommonTaskRecord = mkCommonTaskRecordDao.findOne(DistributeRuleTypeEnum.PROMOTE.getCode());

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
    public void execPromoteDayTaskCore(Map<String, Object> promoteMap, List<Map<String, Object>> promoteDetailList, Map<String, Object> resultMap, LocalDateTime lastDateTime, LocalDateTime curDateTime) {

        //获取当前实名认证的用户
        List<Map<String, Object>> userList = mkDistributeRuleDao.findAuditedUserByDate(DateUtils.formaterLocalDateTime(lastDateTime), DateUtils.formaterLocalDateTime(curDateTime));

        //当前实名认证的用户为空返回
        if (CollectionUtils.isEmpty(userList)) {
            logger.info(String.format("无实名认证的用户", DateUtils.getCurrentDate()));
            return;
        }

        //根据推广规则寻找父级
        List<Map<String, Object>> parentList = this.getAuditedUserParentList(promoteMap, promoteDetailList, LocalDate.now(), userList);

        //记录营销日志
        List<MkDistributeLog> mkDistributeLogList = new ArrayList<>();

        //发放奖励平台币
        this.grantPromoteCoinToUser(userList, parentList, promoteMap, promoteDetailList, mkDistributeLogList, LocalDate.now());

        //计算当前一共分发平台的数量
        BigDecimal grantVolume = BigDecimal.valueOf(mkDistributeLogList.stream().mapToDouble(e -> Double.valueOf(String.valueOf(e.getVolume()))).sum());
        resultMap.put("grantTotalVolume", grantVolume);

        if (grantVolume.compareTo(this.transferToBigDecimal(promoteMap, "volume").subtract(this.transferToBigDecimal(promoteMap, "grant_volume"))) > 0) {
            throw new PlatException(Constants.DISTRIBUTE_TASK_ERROR, String.format("[%s]执行失败，超出会员推广发放平台币数量！", DateUtils.formaterDate(LocalDate.now())));
        }

        //更细会员推广规则，分发了平台的币的数量，超出总数量则抛出异常
        this.updatePromoteRuleById(grantVolume, promoteMap, LocalDate.now());

        //插入分销日志
        this.insertDistributeLog(mkDistributeLogList);

    }

    private List<Map<String, Object>> getAuditedUserParentList(Map<String, Object> promoteMap, List<Map<String, Object>> promoteDetailList, LocalDate curDateTime, List<Map<String, Object>> userList) {

        List<Map<String, Object>> parentList = new ArrayList<>();
        userList.forEach(userMap -> {
            promoteDetailList.forEach(detailMap -> {
                if (Integer.valueOf(String.valueOf(detailMap.get("level"))) != 1) {
                    List<Map<String, Object>> curParentList = mkDistributeRuleDao.findUserParentByUser(String.valueOf(userMap.get("tree_id")), Integer.parseInt(String.valueOf(userMap.get("deth"))), Integer.valueOf(String.valueOf(detailMap.get("level"))), String.valueOf(userMap.get("user_id")));
                    if (!CollectionUtils.isEmpty(curParentList)) {
                        parentList.addAll(curParentList);
                    }
                }
            });
        });

        return parentList;
    }

    private void grantPromoteCoinToUser(List<Map<String, Object>> userList, List<Map<String, Object>> parentList, Map<String, Object> promoteMap, List<Map<String, Object>> promoteDetailList, List<MkDistributeLog> mkDistributeLogList, LocalDate curDateTime) {

        List<Map<String, Object>> grantList = userList;
        if (!CollectionUtils.isEmpty(parentList)) {
            grantList.addAll(parentList);
        }

        BigDecimal leftVolume = this.transferToBigDecimal(promoteMap, "volume").subtract(this.transferToBigDecimal(promoteMap, "grant_volume"));

        if (leftVolume.compareTo(BigDecimal.ZERO) <= 0) {
            logger.info(String.format("[%s]执行失败，会员推广可发放的数量为零！", DateUtils.formaterDate(curDateTime)));
            throw new PlatException(Constants.DISTRIBUTE_TASK_ERROR, String.format("[%s]执行失败，会员推广可发放的数量为零！", DateUtils.formaterDate(curDateTime)));
        }

        grantList.forEach(map -> {
            int level = Integer.valueOf(String.valueOf(map.get("level")));
            Optional<Map<String, Object>> promoteDetailMap = promoteDetailList.stream().filter(detailMap -> level == Integer.valueOf(String.valueOf(detailMap.get("level")))).findFirst();

            if (ObjectUtils.isEmpty(promoteDetailMap)) return;
            if (MapUtils.isEmpty(promoteDetailMap.get())) return;

            BigDecimal volume = this.transferToBigDecimal(promoteDetailMap.get(), "volume");
            String userId = String.valueOf(map.get("user_id"));

            //打款给用户
//            long count = userCoinVolumeService.updateIncome(OrderEnum.OrderStatus.ALL_SUCCESS, volume, userId, String.valueOf(promoteMap.get("coin_symbol")));
            long count = 0L;
//            redisCacheManager.cleanUserCoinVolume(userId, String.valueOf(promoteMap.get("coin_symbol")));

            if (count <= 0) {
                throw new PlatException(Constants.DISTRIBUTE_TASK_ERROR, String.format("[%s]执行失败，更新用户资产", DateUtils.formaterDate(curDateTime)));
            }

            MkDistributeLog mkDistributeLog = new MkDistributeLog();
            Map<String, Object> userMap = mkDistributeRuleDao.findUserById(userId);
            mkDistributeLog.setMail(String.valueOf(userMap.get("mail")));
            mkDistributeLog.setMobile(String.valueOf(userMap.get("mobile")));
            mkDistributeLog.setId(UUID.randomUUID().toString().replace("-", ""));
            mkDistributeLog.setUserId(userId);
            mkDistributeLog.setUsername(String.valueOf(map.get("username")));
            mkDistributeLog.setStatus("1");
            mkDistributeLog.setType(DistributeRuleTypeEnum.PROMOTE.getCode());
            mkDistributeLog.setCoinId(String.valueOf(promoteMap.get("coin_id")));
            mkDistributeLog.setCoinSymbol(String.valueOf(promoteMap.get("coin_symbol")));
            mkDistributeLog.setVolume(volume);
            mkDistributeLog.setCreateDate(LocalDateTime.now());
            mkDistributeLog.setRemark(String.format("会员实名认证时间[%s],会员层级[%s], 奖励数量[%s]", String.valueOf(userMap.get("audit_date")), String.valueOf(promoteDetailMap.get().get("level")), String.valueOf(volume)));
            mkDistributeLog.setBeginDate(DateUtils.getDayStart(curDateTime.minusDays(1)));
            mkDistributeLog.setEndDate(DateUtils.getDayEnd(curDateTime.minusDays(1)));
            mkDistributeLog.setTaskDate(LocalDateTime.of(curDateTime, LocalTime.MIN));
            mkDistributeLogList.add(mkDistributeLog);

            //当前实际发放数量
            BigDecimal realGrantVolume = BigDecimal.valueOf(mkDistributeLogList.stream().mapToDouble(log -> Double.valueOf(String.valueOf(log.getVolume()))).sum());
            if (leftVolume.compareTo(realGrantVolume) < 0) {
                logger.info(String.format("[%s]执行失败，会员推广可发放的数量为不足！", DateUtils.formaterDate(curDateTime)));
                throw new PlatException(Constants.DISTRIBUTE_TASK_ERROR, String.format("[%s]执行失败，会员推广可发放的数量[%s]不足！", leftVolume.toString(), DateUtils.formaterDate(curDateTime)));
            }

        });
    }

    private void updatePromoteRuleById(BigDecimal grantVolume, Map<String, Object> promoteMap, LocalDate curDateTime) {
        long count = mkDistributeRuleDao.updatePromoteRuleById(grantVolume, String.valueOf(promoteMap.get("id")));
        if (count <= 0) {
            if (count <= 0) {
                throw new PlatException(Constants.DISTRIBUTE_TASK_ERROR, String.format("[%s]执行失败，更新会员推广发放平台币数量！", DateUtils.formaterDate(curDateTime)));
            }
        }
    }

    private Map<String, Object> getPromoteRule(String curDate) {
        Map<String, Object> promoteMap = mkDistributeRuleDao.findPromoteRule();
        if (MapUtils.isEmpty(promoteMap)) {
            logger.info(String.format("[%s]执行失败,不存在已启动的会员推广规则，无法执行任务！", curDate));
            throw new PlatException(Constants.DISTRIBUTE_TASK_ERROR, String.format("[%s]执行失败,不存在已启动的会员推广规则，无法执行任务！", curDate));
        }

        if (this.transferToBigDecimal(promoteMap, "volume").compareTo(this.transferToBigDecimal(promoteMap, "grant_volume")) <= 0) {
            logger.info(String.format("[%s]执行失败,会员推广的平台币已分发完！", curDate));
            throw new PlatException(Constants.DISTRIBUTE_TASK_ERROR, String.format("[%s]执行失败,会员推广的平台币已分发完，无法执行任务！", curDate));
        }

        return promoteMap;
    }

    private List<Map<String, Object>> getPromoteDetailByPromoteId(String promoteId, String curDate) {
        List<Map<String, Object>> promoteDetailList = mkDistributeRuleDao.findPromoteDetailByPromoteId(promoteId);
        if (CollectionUtils.isEmpty(promoteDetailList)) {
            logger.info(String.format("[%s]执行失败,不存在已启动的会员推广规则明细，无法执行任务！", curDate));
            throw new PlatException(Constants.DISTRIBUTE_TASK_ERROR, String.format("[%s]执行失败,不存在已启动的推广明细明细，无法执行任务！", curDate));
        }
        return promoteDetailList;
    }

    private BigDecimal transferToBigDecimal(Map<String, Object> paramMap, String key) {
        return paramMap.containsKey(key) ? BigDecimal.valueOf(Double.valueOf(String.valueOf(paramMap.get(key)))) : BigDecimal.ZERO;
    }

    private void insertDistributeLog(List<MkDistributeLog> mkDistributeLogList) {
        mkDistributeRuleDao.batchInsertDistributeLog(mkDistributeLogList);
    }


    @Transactional
    public void initPlatUserRelation() {

        try {
            mkDistributeRuleDao.deleteUserRelation();

            List<Map<String, Object>> userList = mkDistributeRuleDao.getUserTreeId();

            Map<String, String> userReferMap = new HashMap<>();
            Map<String, String> userTreeIdMap = new HashMap<>();

            userList.forEach(uMap -> {
                userReferMap.put(String.valueOf(uMap.get("id")), String.valueOf(uMap.get("refer_id")));
            });

            userReferMap.forEach((userId, referId) -> {
                        String curTreeId = userId + ",";
                        String curReferId = referId;
                        while (StringUtils.isNotEmpty(curReferId)) {
                            curTreeId = curReferId.concat(",").concat(curTreeId);
                            curReferId = userReferMap.get(curReferId);
                        }
                        userTreeIdMap.put(userId, curTreeId);
                    }
            );


            List<UserRelation> userRelationList = new ArrayList<>();
            userList.forEach(map -> {
                if (StringUtils.isEmpty(String.valueOf(map.get("refer_id")))) {
                    UserRelation userRelation = new UserRelation();
                    userRelation.setId(UUID.randomUUID().toString().replace("-", ""));
                    userRelation.setUserId(String.valueOf(map.get("id")));
                    userRelation.setDeth(1);
                    userRelation.setLevel(1);
                    userRelation.setParentId(null);
                    userRelation.setTopParentId(null);
                    userRelation.setTreeId(userTreeIdMap.get(String.valueOf(map.get("id"))));
                    userRelation.setUsername(String.valueOf(map.get("username")));
                    userRelation.setCreateDate(LocalDateTime.now());
                    userRelation.setUpdateDate(LocalDateTime.now());
                    userRelationList.add(userRelation);
                } else {
                    String userTreeId = userTreeIdMap.get(String.valueOf(map.get("id")));
                    String[] userTreedIds = userTreeId.split(",");
                    String topParentId = userTreedIds[0];
                    int deth = userTreedIds.length;
                    int perLevelCount = 1; // 每层记录1个父节点
                    int level = ((deth - 1) / perLevelCount) + 1;

                    for (int i = level; i > 0; i--) {

                        int end = i == level ? deth : i * perLevelCount;
                        int start = (i - 1) * perLevelCount + 1;
                        StringBuilder curTreeId = new StringBuilder("");
                        for (int j = start - 1; j < end; j++) {
                            curTreeId.append(userTreedIds[j]).append(",");
                        }

                        UserRelation userRelation = new UserRelation();
                        userRelation.setId(UUID.randomUUID().toString().replace("-", ""));
                        userRelation.setUserId(String.valueOf(map.get("id")));
                        userRelation.setDeth(deth);
                        userRelation.setLevel(i);
                        userRelation.setParentId(String.valueOf(map.get("refer_id")));
                        userRelation.setTopParentId(topParentId);
                        userRelation.setTreeId(curTreeId.toString());
                        userRelation.setUsername(String.valueOf(map.get("username")));
                        userRelation.setCreateDate(LocalDateTime.now());
                        userRelation.setUpdateDate(LocalDateTime.now());
                        userRelationList.add(userRelation);
                    }
                }
            });
//            platUserDao.batchInsertUserRelation(userRelationList);
            int pageCount = 1000;
            int page = userRelationList.size() % pageCount != 0 ? (userRelationList.size() / pageCount + 1) : (userRelationList.size() / pageCount);
            List<UserRelation> pageTmp = null;
            for (int index = 1; index <= page; index++) {
                if (index == page) {
                    pageTmp = userRelationList.subList((index - 1) * pageCount, userRelationList.size());
                } else {
                    pageTmp = userRelationList.subList((index - 1) * pageCount, index * pageCount);
                }
                platUserDao.batchInsertUserRelation(pageTmp);
            }

        } catch (Exception e) {
            logger.info("初始化用户层级关系失败，" + e.getMessage());
            return;
        }

        logger.info("初始化用户层级关系成功！");
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
