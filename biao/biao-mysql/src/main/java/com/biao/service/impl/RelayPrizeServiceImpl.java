package com.biao.service.impl;

import com.biao.constant.MarketConstants;
import com.biao.entity.relay.MkRelayPrizeCandidate;
import com.biao.entity.relay.MkRelayPrizeConfig;
import com.biao.entity.relay.MkRelayPrizeElector;
import com.biao.entity.relay.MkRelayRemitLog;
import com.biao.mapper.relay.MkRelayPrizeCandidateDao;
import com.biao.mapper.relay.MkRelayPrizeConfigDao;
import com.biao.mapper.relay.MkRelayPrizeElectorDao;
import com.biao.mapper.relay.MkRelayRemitLogDao;
import com.biao.pojo.RequestQuery;
import com.biao.pojo.ResponsePage;
import com.biao.service.RelayPrizeService;
import com.biao.util.DateUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
public class RelayPrizeServiceImpl implements RelayPrizeService {

    private static Logger logger = LoggerFactory.getLogger(RelayPrizeServiceImpl.class);

    @Value("${relayPrizeTime:20}")
    private long RELAY_PRIZE_TIME;

    @Autowired
    private MkRelayPrizeConfigDao mkRelayPrizeConfigDao;

    @Autowired
    private MkRelayPrizeCandidateDao mkRelayPrizeCandidateDao;

    @Autowired
    private MkRelayPrizeElectorDao mkRelayPrizeElectorDao;

    @Autowired
    private MkRelayRemitLogDao mkRelayRemitLogDao;

    @Override
    public Map<String, Object> getData() {
        Map<String, Object> result = new HashMap<>();

        LocalDateTime curDateTime = LocalDateTime.now();

        //当前接力撞奖
        MkRelayPrizeConfig mkRelayPrizeConfig = mkRelayPrizeConfigDao.findActiveOne();
        if (ObjectUtils.isEmpty(mkRelayPrizeConfig)) {
            return this.getFailResult(result);
        } else {
            result.put("poolVolume", mkRelayPrizeConfig.getCurPoolVolume());
            if (curDateTime.isBefore(LocalDateTime.of(curDateTime.toLocalDate(), LocalTime.parse(mkRelayPrizeConfig.getBeginTime()))) ||
                    curDateTime.isAfter(LocalDateTime.of(curDateTime.toLocalDate(), LocalTime.parse(mkRelayPrizeConfig.getEndTime())))) {
                result.put("isActivity", "0");
            } else {
                result.put("isActivity", "1");
            }
            result.put("isRemit", mkRelayPrizeConfig.getIsRemit());
        }

        logger.info(String.format("开始时间[%s],结束时间[%s],奖金总量[%s],起步奖金[%s],新增奖金[%s],币种[%s],已发放数量[%s],当前奖池总量[%s],是否打款[%s],获奖时间间隔[%s]分钟",
                mkRelayPrizeConfig.getBeginTime(), mkRelayPrizeConfig.getEndTime(), String.valueOf(mkRelayPrizeConfig.getVolume()), String.valueOf(mkRelayPrizeConfig.getStartVolume()), String.valueOf(mkRelayPrizeConfig.getStepAddVolume()),
                mkRelayPrizeConfig.getCoinSymbol(), String.valueOf(mkRelayPrizeConfig.getGrantVolume()), String.valueOf(mkRelayPrizeConfig.getCurPoolVolume()), mkRelayPrizeConfig.getIsRemit(), String.valueOf(RELAY_PRIZE_TIME)));

        if (RELAY_PRIZE_TIME <= 0L) {
            logger.info(String.format("[%s]中奖时间间隔有异常！", curDateTime.toLocalDate().toString()));
            return this.getFailResult(result);
        }

        //最近获奖人
        MkRelayPrizeCandidate mkRelayPrizeCandidate = mkRelayPrizeCandidateDao.findAwardOne();
        List<Map<String, String>> candidateMapList = new ArrayList<>();
        if (ObjectUtils.isEmpty(mkRelayPrizeCandidate)) {
            result.put("prizeVolume", BigDecimal.ZERO);
        } else {
            result.put("prizeVolume", mkRelayPrizeCandidate.getPrizeVolume());
            result.put("awardDate", DateUtils.formaterLocalDateTime(mkRelayPrizeCandidate.getUpdateDate(), "MM-dd HH:mm"));
            String username = StringUtils.isEmpty(mkRelayPrizeCandidate.getMobile()) ? mkRelayPrizeCandidate.getMail() : mkRelayPrizeCandidate.getMobile();
            Map<String, String> cMap = new HashMap<>();
            cMap.put("username", this.maskOff(username));
            cMap.put("isRefer", "0");
            candidateMapList.add(cMap);

            String referUserName = StringUtils.isEmpty(mkRelayPrizeCandidate.getReferMobile()) ? mkRelayPrizeCandidate.getReferMail() : mkRelayPrizeCandidate.getReferMobile();
            if (!StringUtils.isEmpty(referUserName)) {
                Map<String, String> rMap = new HashMap<>();
                rMap.put("username", this.maskOff(referUserName));
                rMap.put("isRefer", "1");
                candidateMapList.add(rMap);
            }
        }
        result.put("winners", candidateMapList);
        //获奖人名单列表
//        List<MkRelayPrizeCandidate> mkRelayPrizeCandidateList = mkRelayPrizeCandidateDao.findLastList();
//        List<Map<String,String>> candidateMapList = new ArrayList<>();
//        mkRelayPrizeCandidateList.forEach(candidate -> {
//            String username = StringUtils.isEmpty(candidate.getMobile()) ? candidate.getMail() : candidate.getMobile();
//            Map<String,String> cMap = new HashMap<>();
//            cMap.put("username", this.maskOff(username));
//            if(!result.containsKey("awardDate")){
//                result.put("awardDate", DateUtils.formaterLocalDateTime(candidate.getUpdateDate(), "MM-dd HH:mm"));
//            }
//            candidateMapList.add(cMap);
//
//            try {
//                String referUserName = StringUtils.isEmpty(candidate.getReferMobile()) ? candidate.getReferMail() : candidate.getReferMobile();
//                if(StringUtils.isEmpty(referUserName)){
//
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        });


        //最近候选人
        MkRelayPrizeCandidate lastCandidate = mkRelayPrizeCandidateDao.findActiveOne();
        if (ObjectUtils.isEmpty(lastCandidate)) {
            result.put("remainSecond", "");
        } else {
            LocalDateTime prizeDateTime = null;
            if (lastCandidate.getAchieveDate().toLocalDate().isBefore(curDateTime.toLocalDate())) {
                Duration duration = Duration.between(lastCandidate.getEndDate().minusMinutes(RELAY_PRIZE_TIME), lastCandidate.getAchieveDate());
                Long diff = duration.toMillis();
                Long minutes = 1000 * 60L;
                diff = (long) Math.ceil(diff.doubleValue() / minutes.doubleValue());
                Long interval = MarketConstants.RELAY_TASK_INTERVAL;
                Double flag = Math.ceil(diff.doubleValue() / interval.doubleValue());
                diff = flag.longValue() * interval;
                prizeDateTime = LocalDateTime.of(curDateTime.toLocalDate(), LocalTime.parse(mkRelayPrizeConfig.getBeginTime())).plusMinutes(diff);
            } else {
                Long diff = RELAY_PRIZE_TIME;
                Long interval = MarketConstants.RELAY_TASK_INTERVAL;
                Double flag = Math.ceil(diff.doubleValue() / interval.doubleValue());
                diff = flag.longValue() * interval;
                prizeDateTime = lastCandidate.getCreateDate().plusMinutes(diff); //当前获选人的中奖时间
            }
            Duration duration = Duration.between(curDateTime, prizeDateTime);
            long diff = duration.toMillis() / 1000;
            if (diff < 0) {
                result.put("remainSecond", "0");
            } else {
                result.put("remainSecond", String.valueOf(diff));
            }
        }

        //获选人列表
        List<Map<String, String>> electorMapList = new ArrayList<>();
        List<MkRelayPrizeElector> mkRelayPrizeElectorList = new ArrayList<>();
        if (!ObjectUtils.isEmpty(lastCandidate)) {
            if (ObjectUtils.isEmpty(mkRelayPrizeCandidate)) {
                mkRelayPrizeElectorList = mkRelayPrizeElectorDao.findLastList(LocalDateTime.now(), RELAY_PRIZE_TIME);
            } else {
                mkRelayPrizeElectorList = mkRelayPrizeElectorDao.findLastListByDate(mkRelayPrizeCandidate.getAchieveDate(), LocalDateTime.now(), RELAY_PRIZE_TIME);
            }
        }

        mkRelayPrizeElectorList.forEach(elector -> {
            String username = StringUtils.isEmpty(elector.getMobile()) ? elector.getMail() : elector.getMobile();
            Map<String, String> eMap = new HashMap<>();
            eMap.put("username", this.maskOff(username));
            eMap.put("achieveDate", DateUtils.formaterLocalDateTime(elector.getReachDate(), "MM-dd HH:mm:ss"));
            electorMapList.add(eMap);
        });
        result.put("candidates", electorMapList);

        //活动未开启
        if ("0".equals(String.valueOf(result.get("isActivity")))) {
            result.put("remainSecond", "");
        }

        if (!result.containsKey("awardDate")) {
            result.put("awardDate", "");
        }

        return result;
    }

    private Map<String, Object> getFailResult(Map<String, Object> result) {
        result.put("poolVolume", BigDecimal.ZERO);
        result.put("isActivity", "0");
        result.put("isRemit", "0");
        result.put("prizeVolume", BigDecimal.ZERO);
        result.put("winners", "");
        result.put("remainSecond", "");
        result.put("awardDate", "");
        result.put("candidates", "");
        return result;
    }

    private String maskOff(String username) {
        if (StringUtils.isEmpty(username)) return "";

        String usernameTmp = username;
        String tail = "";
        if (username.indexOf("@") > 0) {
            usernameTmp = username.substring(0, username.indexOf("@"));
            tail = username.substring(username.indexOf("@"), username.length());
        }

        String startStr = "";
        String endStr = "";
        if (usernameTmp.length() > 7) {
            startStr = usernameTmp.substring(0, 3);
            endStr = usernameTmp.substring(usernameTmp.length() - 4, usernameTmp.length());
        } else {
            startStr = usernameTmp.substring(0, 1);
            endStr = usernameTmp.substring(usernameTmp.length() - 2, usernameTmp.length());
        }

        return startStr.concat("****").concat(endStr).concat(tail);
    }

    @Override
    public ResponsePage<MkRelayPrizeCandidate> findPage(RequestQuery requestQuery) {
        ResponsePage<MkRelayPrizeCandidate> responsePage = new ResponsePage<>();
        Page<MkRelayPrizeCandidate> page = PageHelper.startPage(requestQuery.getCurrentPage(), requestQuery.getShowCount());
        List<MkRelayPrizeCandidate> data = mkRelayPrizeCandidateDao.findList();

        data.forEach(mkRelayPrizeCandidate -> {

            mkRelayPrizeCandidate.setMail(this.maskOff(mkRelayPrizeCandidate.getMail()));

            if (StringUtils.isEmpty(mkRelayPrizeCandidate.getReferMail())) {
                mkRelayPrizeCandidate.setReferMail("");
            } else {
                mkRelayPrizeCandidate.setReferMail(this.maskOff(mkRelayPrizeCandidate.getReferMail()));
            }
        });
        responsePage.setCount(page.getTotal());
        responsePage.setList(data);
        return responsePage;
    }

    @Override
    public ResponsePage<MkRelayRemitLog> findWinnersPage(RequestQuery requestQuery) {
        ResponsePage<MkRelayRemitLog> responsePage = new ResponsePage<>();
        Page<MkRelayRemitLog> page = PageHelper.startPage(requestQuery.getCurrentPage(), requestQuery.getShowCount());
        List<MkRelayRemitLog> data = mkRelayRemitLogDao.findList();

        data.forEach(mkRelayRemitLog -> {
            mkRelayRemitLog.setMail(this.maskOff(mkRelayRemitLog.getMail()));
        });
        responsePage.setCount(page.getTotal());
        responsePage.setList(data);
        return responsePage;
    }

    @Override
    public List<MkRelayPrizeCandidate> findCandidateList() {

        List<MkRelayPrizeCandidate> data = mkRelayPrizeCandidateDao.findAllList();
        data.forEach(mkRelayPrizeCandidate -> {
            mkRelayPrizeCandidate.setMail(this.maskOff(mkRelayPrizeCandidate.getMail()));
        });

        return data;
    }

    @Override
    public MkRelayPrizeConfig findRelayPrize() {
        MkRelayPrizeConfig mkRelayPrizeConfig = mkRelayPrizeConfigDao.findActiveOne();
        if (Objects.nonNull(mkRelayPrizeConfig)) {
            MkRelayPrizeConfig config = new MkRelayPrizeConfig();
            config.setMinVolume(mkRelayPrizeConfig.getMinVolume());
            config.setStartVolume(mkRelayPrizeConfig.getStartVolume());
            return config;
        } else {
            return null;
        }
    }
}
