package com.biao.service.impl;

import com.biao.entity.Coin;
import com.biao.entity.OfflineTransferLog;
import com.biao.entity.PlatUser;
import com.biao.entity.register.UserRegisterLottery;
import com.biao.entity.register.UserRegisterLotteryLog;
import com.biao.entity.register.UserRegisterLotteryRefer;
import com.biao.entity.register.UserRegisterLotteryRule;
import com.biao.enums.OfflineTransferLogEnum;
import com.biao.enums.SourceTypeEnum;
import com.biao.enums.UserCoinVolumeEventEnum;
import com.biao.lottery.LotteryContext;
import com.biao.lottery.LotteryUtils;
import com.biao.lottery.LotteryVO;
import com.biao.mapper.OfflineTransferLogDao;
import com.biao.mapper.PlatUserDao;
import com.biao.mapper.register.UserRegisterLotteryLogDao;
import com.biao.mapper.register.UserRegisterLotteryReferDao;
import com.biao.pojo.UserCoinVolumeBillDTO;
import com.biao.redis.RedisCacheManager;
import com.biao.service.CoinService;
import com.biao.service.UserCoinVolumeBillService;
import com.biao.service.register.UserRegisterLotteryService;
import com.biao.util.SnowFlake;
import com.beust.jcommander.internal.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.RandomUtils;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.locks.Lock;

/**
 * The type User register lottery service.
 *
 *  ""(Myth)
 */
@Service("userRegisterLotteryService")
public class UserRegisterLotteryServiceImpl implements UserRegisterLotteryService {

    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(UserRegisterLotteryServiceImpl.class);

    private static final String LOTTERY_NOT_EXIST = "注册抽奖活动不存在或者未开启！";

    private static final String MARK = "注册抽奖";

    private static final String MARK_LOTTERY = "注册人持币挖矿-推荐人";

    private static final int TIME_HOUR = 48;

    private final PlatUserDao platUserDao;

    private final UserRegisterLotteryLogDao userRegisterLotteryLogDao;

    private final OfflineTransferLogDao offlineTransferLogDao;

    private final RedisCacheManager redisCacheManager;

    private final RedissonClient rsclient;

    private final CoinService coinService;

    private final UserRegisterLotteryReferDao userRegisterLotteryReferDao;

    private final UserCoinVolumeBillService billService;

    /**
     * Instantiates a new User register lottery service.
     *
     * @param platUserDao                 the plat user dao
     * @param userRegisterLotteryLogDao   the user register lottery log dao
     * @param redisCacheManager           the redis cache manager
     * @param rsclient                    the rsclient
     * @param offlineTransferLogDao       the offline transfer log dao
     * @param coinService                 the coin service
     * @param userRegisterLotteryReferDao the user register lottery refer dao
     * @param billService                 the bill service
     */
    @Autowired(required = false)
    public UserRegisterLotteryServiceImpl(final PlatUserDao platUserDao,
                                          final UserRegisterLotteryLogDao userRegisterLotteryLogDao,
                                          final RedisCacheManager redisCacheManager,
                                          final RedissonClient rsclient,
                                          final OfflineTransferLogDao offlineTransferLogDao,
                                          final CoinService coinService,
                                          final UserRegisterLotteryReferDao userRegisterLotteryReferDao,
                                          final UserCoinVolumeBillService billService) {
        this.platUserDao = platUserDao;
        this.userRegisterLotteryLogDao = userRegisterLotteryLogDao;
        this.redisCacheManager = redisCacheManager;
        this.rsclient = rsclient;
        this.offlineTransferLogDao = offlineTransferLogDao;
        this.coinService = coinService;
        this.userRegisterLotteryReferDao = userRegisterLotteryReferDao;
        this.billService = billService;
    }


    @Override
    public Boolean checkLottery(final String userId) {
        final PlatUser user = platUserDao.findById(userId);
        if (Objects.isNull(user)) {
            LOGGER.error("用户不存在！或者传入的userId不正确:{}", userId);
            return false;
        }
        final UserRegisterLottery lottery = redisCacheManager.acquireRegisterLottery();
        if (Objects.isNull(lottery)) {
            LOGGER.error("注册抽奖活动不存在或者未开启！");
            return false;
        }

        final List<UserRegisterLotteryRule> rules = redisCacheManager.acquireLotteryRule(lottery.getId());
        if (CollectionUtils.isEmpty(rules)) {
            LOGGER.error("注册抽奖活动的规则未配置,不能进行抽奖！");
            return false;
        }

        //用户的手机绑定时间要在活动开启时间之后
        final LocalDateTime mobileAuditDate = user.getMobileAuditDate();
        final LocalDateTime startDate = lottery.getStartDate();
        if (Objects.isNull(mobileAuditDate)
                || Objects.isNull(startDate)
                || mobileAuditDate.isBefore(startDate)) {
            return false;

        }
        //用户绑定手机时间后如果48小时未参与抽奖则无资格
        final LocalDateTime now = LocalDateTime.now();
        if (mobileAuditDate.plusHours(TIME_HOUR).isBefore(now)) {
            return false;
        }
        //检查用户有没参与过抽奖，抽过奖的无资格
        final UserRegisterLotteryLog log = userRegisterLotteryLogDao.findRegisterByUserId(userId);
        if (Objects.nonNull(log)) {
            LOGGER.error("该用户已经参与过抽奖:{}", userId);
            return false;
        }
        //如果活动总量已经发放完毕，也无资格再参与抽奖
        final BigDecimal total = userRegisterLotteryLogDao.sumTotal();
        if (Objects.nonNull(total)
                && Objects.nonNull(lottery.getTotalPrize())
                && lottery.getTotalPrize().compareTo(total) < 0) {
            LOGGER.error("==========注册活动总奖励额度已经全部发放完毕，不能再参与");
            return false;
        }
        return true;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public LotteryVO lottery(final String userId, final String source) {
        final SourceTypeEnum sourceTypeEnum = SourceTypeEnum.getByMsg(source);
        LotteryVO vo = new LotteryVO();
        if (Objects.isNull(sourceTypeEnum)) {
            vo.setMsg("传入的参数错误");
            return vo;
        }
        //使用redis来存储一个标识，防止用户并发的参与
        final Lock lock = rsclient.getLock(userId);
        try {
            lock.lock();
            //这里最好要再加一下检查，反正用户停留在页面过长时间未抽奖，或者其他人的攻击
            if (!this.checkLottery(userId)) {
                vo.setMsg("无资格参与活动或者活动已经关闭!");
                return vo;
            }
            final UserRegisterLottery lottery = redisCacheManager.acquireRegisterLottery();
            if (Objects.isNull(lottery)) {
                LOGGER.error(LOTTERY_NOT_EXIST);
                vo.setMsg(LOTTERY_NOT_EXIST);
                return vo;
            }
            final List<UserRegisterLotteryRule> rules = redisCacheManager.acquireLotteryRule(lottery.getId());
            if (CollectionUtils.isEmpty(rules)) {
                LOGGER.error("注册抽奖活动的规则未配置,不能进行抽奖！");
                vo.setMsg(LOTTERY_NOT_EXIST);
                return vo;
            }

            LotteryContext lotteryContext = new LotteryContext();
            LotteryUtils.calAwardProbability(lotteryContext, rules);
            final UserRegisterLotteryRule lotteryRule = LotteryUtils.beginLottery(lotteryContext, rules);
            if (Objects.nonNull(lotteryRule)) {
                final Integer minCount = lotteryRule.getMinCount();
                final Integer maxCount = lotteryRule.getMaxCount();
                //生成抽奖数量
                int realVolume = RandomUtils.nextInt(minCount, maxCount);

                //判断推荐人是否符合资格，符合资格推荐人也需要加资产
                final PlatUser user = platUserDao.findById(userId);

                //判断推荐人是否符合资格，符合资格推荐人也需要加资产
                final String referId = user.getReferId();

                //给用户加资产
                final BigDecimal logVolume = new BigDecimal(realVolume);

                final UserRegisterLotteryLog log = buildLog(userId, logVolume, lottery.getCoinSymbol(), lottery.getId(),
                        lottery.getName(), user.getMail(), user.getMobile(), user.getMobileAuditDate(),
                        MARK, 0, user.getReferId(), source, lotteryRule.getId());
                userRegisterLotteryLogDao.insert(log);

                UserCoinVolumeBillDTO billDTO = new UserCoinVolumeBillDTO();
                billDTO.setCoinSymbol(lottery.getCoinSymbol());
                billDTO.setForceLock(false);
                billDTO.setMark(MARK);
                billDTO.setOpLockVolume(new BigDecimal(0));
                billDTO.setOpVolume(logVolume);
                billDTO.setUserId(userId);
                billDTO.setOpSign(new UserCoinVolumeEventEnum[]{UserCoinVolumeEventEnum.ADD_VOLUME});
                billDTO.setPriority(5);
                billDTO.setRefKey(log.getId());
                billDTO.setSource(MARK);

                billService.insert(billDTO);

                final Coin coin = coinService.findByName(lottery.getCoinSymbol());
                if (Objects.nonNull(coin)) {
                    saveTransferLog(userId, coin.getId(), lottery.getCoinSymbol(), logVolume, OfflineTransferLogEnum.REGISTER_LOTTERY);
                }
                //记录用户推荐人日志
                UserRegisterLotteryRefer registerLotteryRefer = new UserRegisterLotteryRefer();
                registerLotteryRefer.setUserId(userId);
                registerLotteryRefer.setReferId(referId);
                registerLotteryRefer.setCoinSymbol(lottery.getCoinSymbol());
                registerLotteryRefer.setLotteryId(lottery.getId());
                registerLotteryRefer.setLotteryName(lottery.getName());
                registerLotteryRefer.setRuleId(lotteryRule.getId());
                registerLotteryRefer.setVolume(logVolume);
                registerLotteryRefer.setId(SnowFlake.createSnowFlake().nextIdString());
                registerLotteryRefer.setCreateDate(LocalDateTime.now());
                userRegisterLotteryReferDao.insert(registerLotteryRefer);

                String info = "恭喜您已经抽中:" + realVolume + "个" + lottery.getCoinSymbol() + ",抽中的资产已经发放到您的币币资产中!";
                vo.setCoinSymbol(lottery.getCoinSymbol());
                vo.setCount(realVolume);
                vo.setMsg(info);
            } else {
                vo.setMsg("您未中奖!");
            }
            return vo;
        } catch (Exception e) {
            e.printStackTrace();
            vo.setMsg("系统内部错误");
            throw e;
        } finally {
            lock.unlock();
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void executeLotteryRefer() {
        final List<UserRegisterLotteryRefer> lotteryRefers = userRegisterLotteryReferDao.existsUserMining();
        if (CollectionUtils.isNotEmpty(lotteryRefers)) {
            List<String> idList = Lists.newArrayList(lotteryRefers.size());
            //判断推荐人设置总数量是否已经达到
            for (UserRegisterLotteryRefer refer : lotteryRefers) {
                //新增日志
                final PlatUser refUser = platUserDao.findById(refer.getReferId());

                final UserRegisterLotteryLog refLog = buildLog(refer.getReferId(), refer.getVolume(), refer.getCoinSymbol(),
                        refer.getLotteryId(),
                        refer.getLotteryName(), refUser.getMail(), refUser.getMobile(), refUser.getMobileAuditDate(),
                        MARK_LOTTERY, 1, refUser.getReferId(), "auto", refer.getRuleId());
                userRegisterLotteryLogDao.insert(refLog);

                UserCoinVolumeBillDTO billDTO = new UserCoinVolumeBillDTO();
                billDTO.setCoinSymbol(refer.getCoinSymbol());
                billDTO.setForceLock(false);
                billDTO.setMark(MARK_LOTTERY);
                billDTO.setOpLockVolume(new BigDecimal(0));
                billDTO.setOpVolume(refer.getVolume());
                billDTO.setUserId(refer.getReferId());
                billDTO.setOpSign(new UserCoinVolumeEventEnum[]{UserCoinVolumeEventEnum.ADD_VOLUME});
                billDTO.setPriority(5);
                billDTO.setRefKey(refLog.getId());
                billDTO.setSource(MARK_LOTTERY);

                billService.insert(billDTO);

                final Coin coin = coinService.findByName(refer.getCoinSymbol());
                if (Objects.nonNull(coin)) {
                    saveTransferLog(refer.getReferId(), coin.getId(), refer.getCoinSymbol(), refer.getVolume(), OfflineTransferLogEnum.REGISTER_LOTTERY_RECOMMEND);
                }
                idList.add(refer.getId());
            }
            userRegisterLotteryReferDao.batchDelete(idList);
        }
    }


    private void saveTransferLog(final String userId, final String coinId, final String coinSymbol,
                                 final BigDecimal volume, final OfflineTransferLogEnum offlineTransferLogEnum) {
        OfflineTransferLog offlineTransferLog = new OfflineTransferLog();
        String id = SnowFlake.createSnowFlake().nextIdString();
        offlineTransferLog.setId(id);
        offlineTransferLog.setCreateDate(LocalDateTime.now());
        offlineTransferLog.setUpdateDate(LocalDateTime.now());
        offlineTransferLog.setUserId(userId);
        offlineTransferLog.setCoinSymbol(coinSymbol);
        offlineTransferLog.setVolume(volume);
        offlineTransferLog.setType(Integer.valueOf(offlineTransferLogEnum.getCode()));
        offlineTransferLog.setCoinId(coinId);
        offlineTransferLogDao.insert(offlineTransferLog);
    }

    private UserRegisterLotteryLog buildLog(final String userId, final BigDecimal realVolume, final String coinSymbol,
                                            final String lotteryId, final String lotteryName,
                                            final String mail, final String phone, final LocalDateTime phoneDate,
                                            final String reason, final Integer reasonType, final String recommendId,
                                            final String source, final String ruleId) {
        UserRegisterLotteryLog log = new UserRegisterLotteryLog();
        log.setId(SnowFlake.createSnowFlake().nextIdString());
        log.setCoinSymbol(coinSymbol);
        log.setCreateDate(LocalDateTime.now());
        log.setLotteryId(lotteryId);
        log.setLotteryName(lotteryName);
        log.setMail(mail);
        log.setPhone(phone);
        log.setPhoneDate(phoneDate);
        log.setRealVolume(realVolume);
        log.setReason(reason);
        log.setReasonType(reasonType);
        log.setRecommendId(recommendId);
        log.setSource(source);
        log.setUserId(userId);
        log.setRuleId(ruleId);
        return log;

    }
}
