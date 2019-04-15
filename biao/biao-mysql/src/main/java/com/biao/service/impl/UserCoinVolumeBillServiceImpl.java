package com.biao.service.impl;

import com.biao.config.UserVolumeBillConfig;
import com.biao.current.threadpool.BbexThreadFactory;
import com.biao.entity.UserCoinVolumeBill;
import com.biao.enums.UserCoinVolumeBillStatusEnum;
import com.biao.enums.UserCoinVolumeEventEnum;
import com.biao.mapper.UserCoinVolumeBillDao;
import com.biao.pojo.UserCoinVolumeBillDTO;
import com.biao.pojo.UserCoinVolumeOpDTO;
import com.biao.service.UserCoinVolumeBillService;
import com.biao.service.UserCoinVolumeExService;
import com.biao.util.HashSelect;
import com.biao.util.SnowFlake;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * UserCoinVolumeBillServiceImpl.
 * <p>
 * 用户资产申请处理.
 * <p>
 * 19-1-2下午5:34
 *
 *  "" sixh
 */
@Component
public class UserCoinVolumeBillServiceImpl implements UserCoinVolumeBillService {

    private Logger logger = LoggerFactory.getLogger(UserCoinVolumeBillServiceImpl.class);

    private HashSelect select;

    private final Integer defQueueSize = 100000;
    private final Integer defRetryCount = 2;

    @Autowired
    private UserCoinVolumeBillDao userCoinVolumeBillDao;

    @Autowired
    private UserVolumeBillConfig billConfig;

    @Autowired
    private UserCoinVolumeExService coinVolumeService;

    @Autowired
    private PlatformTransactionManager transactionManager;

    /**
     * 定义一个优先级队列.
     */
    @SuppressWarnings("all")
    private final BlockingQueue<UserCoinVolumeBill> queue = new PriorityBlockingQueue(defQueueSize, (Comparator<UserCoinVolumeBill>) (o1, o2) -> o2.getPriority() - o1.getPriority());

    @PostConstruct
    @SuppressWarnings("all")
    public void postMain() {
        if (billConfig == null) {
            return;
        }
        if (billConfig.getNeed() == null || billConfig.getNeed() <= 0) {
            return;
        }
        if (billConfig.getNeeds() == null || billConfig.getNeeds() <= 0) {
            return;
        }
        select = HashSelect.create(billConfig.getNeeds());
        if (billConfig.getTaskFlag()) {
            logger.info("加载billConfig信息，执行节点数：{},当前节点号：{},执行时间S:{}", billConfig.getNeeds(), billConfig.getNeed(), billConfig.getTime());
            ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor(new BbexThreadFactory("user_coin_volume_bill_put"));
            executorService.scheduleAtFixedRate(() -> running(), 30, billConfig.getTime(), TimeUnit.SECONDS);
            ExecutorService tackService = Executors.newSingleThreadExecutor(new BbexThreadFactory("user_coin_volume_bill_tack"));
            tackService.execute(new AsyncRun());
        }
    }

    private void running() {
        //需要spring事务保证.
        Integer need = billConfig.getNeed();
        DefaultTransactionDefinition defaultTransactionDefinition = new DefaultTransactionDefinition();
        defaultTransactionDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus transaction = transactionManager.getTransaction(defaultTransactionDefinition);
        try {
            List<UserCoinVolumeBill> userCoinVolumeBills = userCoinVolumeBillDao.findByStatusToUnprocessed(need, defRetryCount);
            for (UserCoinVolumeBill userCoinVolumeBill : userCoinVolumeBills) {
                //这里需要修改数据库状态.
                long l = userCoinVolumeBillDao.updateStatusAndRetryCount(userCoinVolumeBill.getId(), UserCoinVolumeBillStatusEnum.PROCESSING.getStatus());
                if (l > 0) {
                    queue.put(userCoinVolumeBill);
                }
            }
            transactionManager.commit(transaction);
        } catch (Exception ex) {
            transactionManager.rollback(transaction);
            logger.error("发送UserCoinVolumeBill失败，事务回滚,等待下次发起....", ex);
        }
    }

    @Override
    public long insert(UserCoinVolumeBillDTO dto) {
        List<UserCoinVolumeBill> bills = parse(Lists.newArrayList(dto));
        if (bills.size() <= 0) {
            logger.warn("没有通过数据校验{}", dto);
            return 0L;
        }
        return userCoinVolumeBillDao.insert(bills.get(0));
    }

    @Override
    public long batchInsert(List<UserCoinVolumeBillDTO> dtoList) {
        List<UserCoinVolumeBill> bills = parse(dtoList);
        return userCoinVolumeBillDao.insertBatch(bills);
    }

    @Override
    public List<UserCoinVolumeBill> findByHistory() {
        return userCoinVolumeBillDao.findHistorys();
    }

    @Override
    public long deleteHistory() {
        return userCoinVolumeBillDao.deleteHistory();
    }

    public List<UserCoinVolumeBill> parse(List<UserCoinVolumeBillDTO> list) {
        return list.stream().filter(dto -> {
            if (StringUtils.isBlank(dto.getCoinSymbol())) {
                logger.warn("coinSymbol is null,{}", dto);
                return false;
            }
            if (StringUtils.isBlank(dto.getUserId())) {
                logger.warn("userId is null,{}", dto);
                return false;
            }
            if (StringUtils.isBlank(dto.getRefKey())) {
                logger.warn("refKey is null,{}", dto);
                return false;
            }
            if (StringUtils.isBlank(dto.getMark())) {
                logger.warn("mark is null,{}", dto);
                return false;
            }
            if (StringUtils.isBlank(dto.getSource())) {
                logger.warn("source is null,{}", dto);
                return false;
            }
            if (dto.getOpSign() == null || dto.getOpSign().length <= 0) {
                logger.warn("没有传入正确的操作符.,{}", dto);
                return false;
            }
            int opSign = UserCoinVolumeEventEnum.parseEvent(dto.getOpSign());
            boolean flag = false;
            if (UserCoinVolumeEventEnum.ADD_VOLUME.check(opSign) || UserCoinVolumeEventEnum.SUB_VOLUME.check(opSign)) {
                flag = true;
                if (dto.getOpVolume() == null || dto.getOpVolume().doubleValue() <= 0) {
                    logger.warn("opVolume is null or 0,{}", dto);
                    return false;
                }
            }
            if (UserCoinVolumeEventEnum.SUB_LOCKVOLUME.check(opSign) || UserCoinVolumeEventEnum.ADD_LOCKVOLUME.check(opSign)) {
                flag = true;
                if (dto.getOpLockVolume() == null || dto.getOpLockVolume().doubleValue() <= 0) {
                    logger.warn("opLockVolume is null or 0,{}", dto);
                    return false;
                }
            }
            if (!flag) {
                logger.warn("没有传入正确的操作符,{}", dto);
                return false;
            }
            return true;
        }).map(dto -> {

            if (dto.getPriority() == null) {
                dto.setPriority(5);
            }
            int opSign = UserCoinVolumeEventEnum.parseEvent(dto.getOpSign());
            UserCoinVolumeBill bill = new UserCoinVolumeBill();
            bill.setId(SnowFlake.createSnowFlake().nextIdString());
            bill.setCoinSymbol(dto.getCoinSymbol());
            bill.setUserId(dto.getUserId());
            bill.setSource(dto.getSource());
            bill.setMark(dto.getMark());
            bill.setOpLockVolume(dto.getOpLockVolume());
            bill.setOpVolume(dto.getOpVolume());
            bill.setOpSign(opSign);
            bill.setPriority(dto.getPriority());
            bill.setRefKey(dto.getRefKey());
            String key = bill.getUserId() + "_" + bill.getCoinSymbol();
            bill.setHash(select.select(key));
            bill.setCreateDate(LocalDateTime.now());
            bill.setCreateBy("plat");
            bill.setRetryCount(0);
            bill.setStatus(UserCoinVolumeBillStatusEnum.UNPROCESSED.getStatus());
            bill.setForceLock(dto.getForceLock());
            return bill;
        }).collect(Collectors.toList());
    }

    /**
     * 异步执行的操作.
     */
    class AsyncRun implements Runnable {
        @Override
        public void run() {
            while (true) {
                int status = UserCoinVolumeBillStatusEnum.SUCCESS.getStatus();
                String id = "";
                try {
                    UserCoinVolumeBill take = queue.take();
                    id = take.getId();
                    UserCoinVolumeOpDTO dto = new UserCoinVolumeOpDTO();
                    dto.setUserId(take.getUserId());
                    dto.setOpSign(take.getOpSign());
                    dto.setOpVolume(take.getOpVolume());
                    dto.setOpLockVolume(take.getOpLockVolume());
                    dto.setSymbol(take.getCoinSymbol());
                    long l = coinVolumeService.volumeActionByRlock(dto, take.getForceLock());
                    if (l > 0) {
                        status = UserCoinVolumeBillStatusEnum.SUCCESS.getStatus();
                        logger.info("成功执行BB资产操作billId:{}:{}", id, dto);
                    } else {
                        if (take.getForceLock()) {
                            status = UserCoinVolumeBillStatusEnum.FAILURE.getStatus();
                            logger.error("失败执行BB资产操作不会重试billId:{}:{}", id, dto);
                        } else {
                            status = Objects.equals(take.getRetryCount(), defRetryCount - 1) ? UserCoinVolumeBillStatusEnum.FAILURE.getStatus() : UserCoinVolumeBillStatusEnum.RETRY.getStatus();
                            logger.warn("失败执行BB资产操作等待重试billId:{}:{}", id, dto);
                        }
                    }
                } catch (Exception ex) {
                    status = UserCoinVolumeBillStatusEnum.FAILURE.getStatus();
                    logger.error("执行UserCoinVolumeBill失败....", ex);
                } finally {
                    if (StringUtils.isNoneBlank(id)) {
                        try {
                            userCoinVolumeBillDao.updateStatus(id, status);
                        } catch (Exception ex) {
                            logger.error("finally 修改状态失败!{}", id);
                        }
                    }
                }
            }

        }
    }
}
