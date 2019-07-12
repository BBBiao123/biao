package com.biao.service.impl;

import com.biao.constant.Constants;
import com.biao.entity.Coin;
import com.biao.entity.PlatUser;
import com.biao.entity.mk2.Mk2PopularizeRegisterCoin;
import com.biao.entity.mk2.Mk2PopularizeRegisterConf;
import com.biao.entity.mk2.Mk2PopularizeTaskLog;
import com.biao.exception.PlatException;
import com.biao.mapper.CoinDao;
import com.biao.mapper.PlatUserDao;
import com.biao.mapper.mk2.Mk2PopularizeRegisterCoinDao;
import com.biao.mapper.mk2.Mk2PopularizeRegisterConfDao;
import com.biao.mapper.mk2.Mk2PopularizeTaskLogDao;
import com.biao.service.MkPromoteRuleTaskService;
import com.biao.service.RegisterUserCoinService;
import com.biao.util.DateUtils;
import com.biao.util.SnowFlake;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.biao.service.UserCoinVolumeService;

@Service("MkPromoteRuleTaskService_2")
public class RegisterUserCoinServiceImpl implements RegisterUserCoinService, MkPromoteRuleTaskService {

    private Logger logger = LoggerFactory.getLogger(RegisterUserCoinServiceImpl.class);
    @Autowired
    private Mk2PopularizeRegisterCoinDao mk2PopularizeRegisterCoinDao;

    @Autowired
    private Mk2PopularizeRegisterConfDao mk2PopularizeRegisterConfDao;

    @Autowired
    private PlatUserDao platUserDao;

    @Autowired
    private Mk2PopularizeTaskLogDao mk2PopularizeTaskLogDao;

    @Autowired
    private UserCoinVolumeService userCoinVolumeService;

    @Autowired
    private RegisterUserCoinService registerUserCoinService;

    @Autowired
    private CoinDao coinDao;

    /**
     * 注册送币币种名称
     */
    @Value("${mk2GiveCoin}")
    private String coinSymbol;

    private static final int pageCount = 100;

    @Override
    @Transactional
    public void promoteDayTaskEntry() {
        registerUserCoinService.registerGiveCoin();
    }

    @Override
    @Transactional
    public void registerGiveCoin() {
        LocalDateTime curDateTime = LocalDateTime.now();
        String curDate = DateUtils.getCurrentDate();
        LocalDateTime lastDateTime = null;
//        curDateTime = curDateTime.minusDays(1);

        Mk2PopularizeRegisterConf conf = null;
        try {
            //查询注册送币规则
            List<Mk2PopularizeRegisterConf> confList = mk2PopularizeRegisterConfDao.findConf();
            if (CollectionUtils.isEmpty(confList) || confList.size() > 1) {
                throw new PlatException(Constants.PARAM_ERROR, "送币规则设置错误！");
            } else {
                conf = confList.get(0);
            }
            Coin coin = coinDao.findByName(coinSymbol);
            conf.setCoinId(coin.getId());
            conf.setCoinSymbol(coinSymbol);

//            // 校验当天是否已送过币
//            checkDone(curDate);
            lastDateTime = this.getAndCheckLastSuccessTime(curDateTime);
            // 送币初始化
            long sumVolume = giveCoin(lastDateTime, curDateTime, conf);
            // 送币到个人资金账户
            transferCoin();
            // 记录当天送币结果
            registerUserCoinService.taskLog("1", "执行成功", curDate, sumVolume, conf, curDateTime);
        } catch (PlatException e) {
            logger.error("注册送币TASK执行失败", e);
            String msg = e.getMsg().length() > 200 ? e.getMsg().substring(0, 200) : e.getMsg();
            registerUserCoinService.taskLog("0", msg, curDate, 0L, conf, curDateTime);
            throw new PlatException(Constants.PARAM_ERROR, "注册送币TASK执行失败!");
        } catch (Exception e) {
            logger.error("注册送币TASK执行失败", e);
            String msg = e.toString();
            msg = msg.length() > 200 ? msg.substring(0, 200) : msg;
            registerUserCoinService.taskLog("0", msg, curDate, 0L, conf, curDateTime);
            throw new PlatException(Constants.PARAM_ERROR, "注册送币TASK执行失败!");
        }

    }

    /**
     * 送币到个人资金账户
     *
     * @param curDateTime
     * @return
     */
    private long giveCoin(LocalDateTime lastDateTime, LocalDateTime curDateTime, Mk2PopularizeRegisterConf conf) {

        //2、 查询审核通过的送币用户
        List<PlatUser> userList = platUserDao.findAuditByBeginAndEnd(DateUtils.formaterLocalDateTime(lastDateTime), DateUtils.formaterLocalDateTime(curDateTime));
//        long taskLogCount = mk2PopularizeTaskLogDao.queryCountAll();
//        if (taskLogCount < 1) {
//            userList = platUserDao.findFormerAudit(curDate);
//        } else {
//            userList platUserDao.findYesterdayAudit(curDate);
//        }
        if (CollectionUtils.isEmpty(userList)) {
            return 0L;
        }


        // 2.1 对送币账户分批进行送币（防止用户过多导致数据库压力）
        List<Mk2PopularizeRegisterCoin> coinList = new ArrayList<Mk2PopularizeRegisterCoin>();
        long sumVolume = 0;
        int page = userList.size() % pageCount != 0 ? (userList.size() / pageCount + 1) : (userList.size() / pageCount);
        List<PlatUser> pageUsers = null;
        for (int index = 1; index <= page; index++) {
            coinList.clear();
            if (index == page) {
                pageUsers = userList.subList((index - 1) * pageCount, userList.size());
            } else {
                pageUsers = userList.subList((index - 1) * pageCount, index * pageCount);
            }
            sumVolume += pageUsers.stream().map(e -> {
                return giveCoin4Register(e, conf, coinList);
            }).reduce(0L, (a, b) -> {
                return a + b;
            });
            if (CollectionUtils.isEmpty(coinList)) {
                throw new PlatException(Constants.PARAM_ERROR, "初始化用户送币失败");
            }
            //3、初始化送币人送币数量
            mk2PopularizeRegisterCoinDao.insertBatch(coinList);
        }
        //4、修改送出币个数
        updateConfVolume(sumVolume, conf);
        return sumVolume;
    }


    /**
     * 计算注册者和推荐人送币
     *
     * @param user
     * @param conf
     * @param coinList
     * @return
     */
    private long giveCoin4Register(PlatUser user, Mk2PopularizeRegisterConf conf, List<Mk2PopularizeRegisterCoin> coinList) {

        // 判断该用户注册是否已经送过币，若送过，则不予重复送
        long hadGiveCoinCount = mk2PopularizeRegisterCoinDao.findHadRegisterGive(user.getId());
        if (hadGiveCoinCount > 0) {
            return 0L;
        }

        long volume = conf.getRegisterVolume();
        // 注册者送币
        Mk2PopularizeRegisterCoin registerCoin = new Mk2PopularizeRegisterCoin();
        registerCoin.setId(SnowFlake.createSnowFlake().nextIdString());
        registerCoin.setRegisterConfId(conf.getId());
        registerCoin.setConfName(conf.getName());
        registerCoin.setUserId(user.getId());
        registerCoin.setMail(user.getMail());
        registerCoin.setMobile(user.getMobile());
        registerCoin.setUserName(user.getRealName());
        registerCoin.setVolume(conf.getRegisterVolume());
        registerCoin.setCoinId(conf.getCoinId());
        registerCoin.setCoinSymbol(conf.getCoinSymbol());
        registerCoin.setStatus("1");
        coinList.add(registerCoin);
        //mk2PopularizeRegisterCoinDao.insert(registerCoin);

        // 推荐人送币
        if (StringUtils.isNotBlank(user.getReferId())) {
            PlatUser referUser = platUserDao.findById(user.getReferId());
            if (referUser == null) {
                throw new PlatException(Constants.OPERRATION_ERROR, "推荐人不存在，数据错误！[" + user.getReferId() + "]");
            }
            Mk2PopularizeRegisterCoin referCoin = new Mk2PopularizeRegisterCoin();
            referCoin.setId(SnowFlake.createSnowFlake().nextIdString());
            referCoin.setRegisterConfId(conf.getId());
            referCoin.setConfName(conf.getName());
            referCoin.setUserId(referUser.getId());
            referCoin.setUserName(referUser.getRealName());
            referCoin.setMobile(referUser.getMobile());
            referCoin.setMail(referUser.getMail());
            referCoin.setVolume(conf.getReferVolume());
            referCoin.setCoinId(conf.getCoinId());
            referCoin.setCoinSymbol(conf.getCoinSymbol());
            referCoin.setForUserId(user.getId());
            referCoin.setStatus("1");
            //mk2PopularizeRegisterCoinDao.insert(referCoin);
            coinList.add(referCoin);
            volume = volume + conf.getReferVolume();
        }
        return volume;
    }

    /**
     * 更新总送币量
     *
     * @param subtractVol
     * @param conf
     */
    private void updateConfVolume(long subtractVol, Mk2PopularizeRegisterConf conf) {
        long last = conf.getTotalVolume() - conf.getGiveVolume() - subtractVol;
        if (last >= 0) {
            int count = mk2PopularizeRegisterConfDao.updateConfVolume(subtractVol, conf.getId());
            if (count != 1) {
                throw new PlatException(Constants.OPERRATION_ERROR, "注册送币配置有误！待减数量:[" + subtractVol + "]配置ID:[" + conf.getId() + "]");
            }
        } else {
            throw new PlatException(Constants.OPERRATION_ERROR, "注册送币可送余量不够！");
        }
    }

    /**
     * 送币到用户的用户资产账户
     */
    private void transferCoin() {
        List<Mk2PopularizeRegisterCoin> registerCoins = mk2PopularizeRegisterCoinDao.findTransfer(DateUtils.getCurrentDate());
        registerCoins.forEach(e -> {
            userCoinVolumeService.updateIncome(null, new BigDecimal(e.getVolume()), e.getUserId(), e.getCoinSymbol());
            mk2PopularizeRegisterCoinDao.updateTransferSuccess(e.getId());
        });
    }

    /**
     * 查询该日期是否已经执行过送币
     *
     * @param curDate
     */
    private void checkDone(String curDate) {
        List<Mk2PopularizeTaskLog> taskLogList = mk2PopularizeTaskLogDao.findByDate(curDate);
        if (CollectionUtils.isNotEmpty(taskLogList)) {
            throw new PlatException(Constants.OPERRATION_ERROR, "该日期已经执行过注册送币[" + curDate + "]");
        }
    }

    /**
     * 查询上一次执行成功记录
     */
    private LocalDateTime getAndCheckLastSuccessTime(LocalDateTime curDateTime) {
        LocalDateTime lastDateTime = null;
        Mk2PopularizeTaskLog mk2PopularizeTaskLog = mk2PopularizeTaskLogDao.getLastSuccessLog();
        if (null != mk2PopularizeTaskLog) {
            lastDateTime = mk2PopularizeTaskLog.getExecuteTime();
        } else {
            try {
                lastDateTime = DateUtils.getDayStart(DateUtils.parseLocalDate("2018-07-18"));
            } catch (Exception e) {
                throw new PlatException(Constants.OPERRATION_ERROR, "日期解析异常!");
            }
        }

        if (curDateTime.isBefore(lastDateTime)) {
            logger.info(String.format("当前执行时间必须在最近执行时间之后，当前时间[%s],最近执行时间[%s]", DateUtils.formaterLocalDateTime(curDateTime), DateUtils.formaterLocalDateTime(lastDateTime)));
            throw new PlatException(Constants.OPERRATION_ERROR, String.format("当前执行时间必须在最近执行时间之后，当前时间[%s],最近执行时间[%s]", DateUtils.formaterLocalDateTime(curDateTime), DateUtils.formaterLocalDateTime(lastDateTime)));
        }
        return lastDateTime;
    }


    /**
     * 记录本次执行task执行结果
     *
     * @param status
     * @param reason
     * @param curDate
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void taskLog(String status, String reason, String curDate, Long dayGiveColume, Mk2PopularizeRegisterConf conf, LocalDateTime curDateTime) {
        Mk2PopularizeTaskLog taskLog = new Mk2PopularizeTaskLog();
        taskLog.setId(SnowFlake.createSnowFlake().nextIdString());
        taskLog.setType("1");
        taskLog.setTypeName(conf != null ? conf.getName() : "");
        taskLog.setDayGiveColume(dayGiveColume);
        taskLog.setParamTaskDate(curDate);
        taskLog.setStatus(status);
        taskLog.setReason(reason);
        taskLog.setExecuteTime(curDateTime);
        mk2PopularizeTaskLogDao.insert(taskLog);
    }

}
