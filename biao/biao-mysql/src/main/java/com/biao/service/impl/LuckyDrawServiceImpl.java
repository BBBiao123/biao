package com.biao.service.impl;

import com.biao.constant.Constants;
import com.biao.entity.OfflineCoinVolume;
import com.biao.entity.PlatUser;
import com.biao.entity.UserCoinVolume;
import com.biao.entity.lucky.MkLuckyDrawConfig;
import com.biao.entity.lucky.MkLuckyDrawPlayer;
import com.biao.exception.PlatException;
import com.biao.mapper.lucky.MkLuckyDrawConfigDao;
import com.biao.mapper.lucky.MkLuckyDrawPlayerDao;
import com.biao.mapper.lucky.MkLuckyDrawRecordDao;
import com.biao.pojo.RequestQuery;
import com.biao.pojo.ResponsePage;
import com.biao.service.LuckyDrawService;
import com.biao.service.OfflineCoinVolumeService;
import com.biao.service.UserCoinVolumeExService;
import com.biao.util.DateUtils;
import com.biao.util.SnowFlake;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LuckyDrawServiceImpl implements LuckyDrawService {

    private static Logger logger = LoggerFactory.getLogger(LuckyDrawServiceImpl.class);

    @Autowired
    private MkLuckyDrawPlayerDao mkLuckyDrawPlayerDao;

    @Autowired
    private MkLuckyDrawConfigDao mkLuckyDrawConfigDao;

    @Autowired
    private MkLuckyDrawRecordDao mkLuckyDrawRecordDao;

    @Autowired
    private OfflineCoinVolumeService offlineCoinVolumeService;

    @Autowired(required = false)
    private UserCoinVolumeExService userCoinVolumeService;

    @Override
    public Map<String, Object> getData() {
        Map<String, Object> result = new HashMap<>();

        MkLuckyDrawConfig mkLuckyDrawConfig = mkLuckyDrawConfigDao.findOne();
        if (ObjectUtils.isEmpty(mkLuckyDrawConfig)) {
            return this.getFailResult(result);
        }

        result.put("isActivity", mkLuckyDrawConfig.getStatus());
        if (ObjectUtils.isEmpty(mkLuckyDrawConfig.getPoolVolume())) {
            result.put("poolVolume", "");
        } else {
            result.put("poolVolume", mkLuckyDrawConfig.getPoolVolume());
        }
        result.put("isRemit", "1");
        result.put("periods", mkLuckyDrawConfig.getPeriods());
        result.put("symbol", mkLuckyDrawConfig.getCoinSymbol());
        result.put("fee", mkLuckyDrawConfig.getDeductFee());

        MkLuckyDrawPlayer luckyDrawPlayer = mkLuckyDrawPlayerDao.findLastWinner();
        if (ObjectUtils.isEmpty(luckyDrawPlayer)) {
            result.put("prizeVolume", "");
            result.put("awardDate", "");
            result.put("winners", "");
        } else {
            result.put("prizeVolume", luckyDrawPlayer.getLuckyVolume());
            result.put("awardDate", DateUtils.formaterLocalDateTime(luckyDrawPlayer.getDrawDate(), "MM-dd HH:mm"));
            List<Map<String, String>> candidateMapList = new ArrayList<>();
            Map<String, String> cMap = new HashMap<>();
            cMap.put("username", this.maskOff(StringUtils.isEmpty(luckyDrawPlayer.getMobile()) ? luckyDrawPlayer.getMail() : luckyDrawPlayer.getMobile()));
            cMap.put("isRefer", "0");
            candidateMapList.add(cMap);
            result.put("winners", candidateMapList);
        }

        List<Map<String, String>> electorMapList = new ArrayList<>();
        List<MkLuckyDrawPlayer> mkLuckyDrawPlayerList = mkLuckyDrawPlayerDao.findLastList();
        mkLuckyDrawPlayerList.forEach(elector -> {
            String username = org.springframework.util.StringUtils.isEmpty(elector.getMobile()) ? elector.getMail() : elector.getMobile();
            Map<String, String> eMap = new HashMap<>();
            eMap.put("username", this.maskOff(username));
            eMap.put("achieveDate", DateUtils.formaterLocalDateTime(elector.getCreateDate(), "MM-dd HH:mm:ss"));
            electorMapList.add(eMap);
        });
        result.put("candidates", electorMapList);
        return result;

    }

    private String maskOff(String username) {
        if (org.springframework.util.StringUtils.isEmpty(username)) return "";

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

    private Map<String, Object> getFailResult(Map<String, Object> result) {
        result.put("poolVolume", BigDecimal.ZERO);
        result.put("isActivity", "0");
        result.put("isRemit", "0");
        result.put("prizeVolume", BigDecimal.ZERO);
        result.put("winners", "");
        result.put("awardDate", "");
        result.put("candidates", "");
        result.put("periods", "");
        result.put("symbol", "");
        result.put("fee", "");
        return result;
    }

    @Override
    @Transactional
    public void in(PlatUser platUser) {
        //检查用户状态
        this.checkPlatUser(platUser);
        //检查抽奖活动规则
        MkLuckyDrawConfig mkLuckyDrawConfig = this.checkAndGetConfig();
        //检查用户常规账户是否满足最低要求, 检查手续费是否满足
        OfflineCoinVolume userVolume = this.checkAngGetUserCoinVolume(platUser, mkLuckyDrawConfig);
        //扣手续费
        String playerId = SnowFlake.createSnowFlake().nextIdString();
        offlineCoinVolumeService.coinVolumeSubtract(platUser.getId(), mkLuckyDrawConfig.getCoinId(), mkLuckyDrawConfig.getDeductFee(), playerId);
        //更新抽奖活动
        this.updateConfig(mkLuckyDrawConfig);
        //创建参与者
        this.createPlayer(mkLuckyDrawConfig, platUser, userVolume, playerId);
    }

    private void checkPlatUser(PlatUser platUser) {

        if (ObjectUtils.isEmpty(platUser) || StringUtils.isEmpty(platUser.getId())) {
            logger.info("数据非法");
            throw new PlatException(Constants.PARAM_ERROR, "数据非法");
        }
    }

    private MkLuckyDrawConfig checkAndGetConfig() {
        MkLuckyDrawConfig mkLuckyDrawConfig = mkLuckyDrawConfigDao.findOne();
        if (ObjectUtils.isEmpty(mkLuckyDrawConfig)) {
            logger.info("抽奖活动数据非法");
            throw new PlatException(Constants.PARAM_ERROR, "抽奖活动数据非法");
        }

        if (StringUtils.isEmpty(mkLuckyDrawConfig.getStatus()) || !"1".equals(mkLuckyDrawConfig.getStatus())) {
            logger.info("抽奖活动未开启");
            throw new PlatException(Constants.PARAM_ERROR, "抽奖活动未开启");
        }

        if (ObjectUtils.isEmpty(mkLuckyDrawConfig.getMinVolume()) || mkLuckyDrawConfig.getMinVolume().compareTo(BigDecimal.ZERO) <= 0) {
            logger.info("抽奖活动数据非法");
            throw new PlatException(Constants.PARAM_ERROR, "抽奖活动数据非法");
        }

        if (ObjectUtils.isEmpty(mkLuckyDrawConfig.getDeductFee()) || mkLuckyDrawConfig.getDeductFee().compareTo(BigDecimal.ZERO) <= 0) {
            logger.info("抽奖活动数据非法");
            throw new PlatException(Constants.PARAM_ERROR, "抽奖活动数据非法");
        }

        if (ObjectUtils.isEmpty(mkLuckyDrawConfig.getPoolVolume()) || mkLuckyDrawConfig.getPoolVolume().compareTo(BigDecimal.ZERO) <= 0) {
            logger.info("抽奖活动数据非法");
            throw new PlatException(Constants.PARAM_ERROR, "抽奖活动数据非法");
        }

        if (ObjectUtils.isEmpty(mkLuckyDrawConfig.getStartVolume()) || mkLuckyDrawConfig.getStartVolume().compareTo(BigDecimal.ZERO) <= 0) {
            logger.info("抽奖活动数据非法");
            throw new PlatException(Constants.PARAM_ERROR, "抽奖活动数据非法");
        }

        if (ObjectUtils.isEmpty(mkLuckyDrawConfig.getVolume()) || mkLuckyDrawConfig.getVolume().compareTo(BigDecimal.ZERO) <= 0) {
            logger.info("抽奖活动数据非法");
            throw new PlatException(Constants.PARAM_ERROR, "抽奖活动数据非法");
        }

        if (ObjectUtils.isEmpty(mkLuckyDrawConfig.getStepAddVolume()) || mkLuckyDrawConfig.getStepAddVolume().compareTo(BigDecimal.ZERO) <= 0) {
            logger.info("抽奖活动数据非法");
            throw new PlatException(Constants.PARAM_ERROR, "抽奖活动数据非法");
        }

        return mkLuckyDrawConfig;
    }

    private UserCoinVolume checkAndGetUserVolume(MkLuckyDrawConfig mkLuckyDrawConfig, PlatUser platUser) {
        UserCoinVolume volume = userCoinVolumeService.findByUserIdAndCoinSymbol(platUser.getId(), mkLuckyDrawConfig.getCoinSymbol());
        if (ObjectUtils.isEmpty(volume) || StringUtils.isEmpty(volume.getUserId())) {
            logger.info("用户常规账户为空");
            throw new PlatException(Constants.PARAM_ERROR, "用户常规账户为空");
        }

        if (volume.getVolume().compareTo(mkLuckyDrawConfig.getMinVolume()) < 0) {
            logger.info("用户常规账户资产小于抽奖活动设置最低值");
            throw new PlatException(Constants.PARAM_ERROR, "用户常规账户资产小于抽奖活动设置最低值：" + String.valueOf(mkLuckyDrawConfig.getMinVolume()));
        }
        return volume;
    }

    private OfflineCoinVolume checkAngGetUserCoinVolume(PlatUser platUser, MkLuckyDrawConfig mkLuckyDrawConfig) {
        OfflineCoinVolume offlineCoinVolume = offlineCoinVolumeService.findByUserIdAndCoinId(platUser.getId(), mkLuckyDrawConfig.getCoinId());
        if (ObjectUtils.isEmpty(offlineCoinVolume) || StringUtils.isEmpty(offlineCoinVolume.getUserId())) {
            logger.error("参与失败，手续费资产账户不存在");
            throw new PlatException(Constants.PARAM_ERROR, "参与失败，手续费资产账户不存在");
        }

        if (offlineCoinVolume.getVolume().compareTo(mkLuckyDrawConfig.getMinVolume()) < 0) {
            logger.info("用户C2C账户资产小于抽奖活动设置最低值");
            throw new PlatException(Constants.PARAM_ERROR, "用户C2C账户资产小于抽奖活动设置最低值：" + String.valueOf(mkLuckyDrawConfig.getMinVolume()));
        }

        if (offlineCoinVolume.getVolume().compareTo(mkLuckyDrawConfig.getDeductFee()) < 0) {
            logger.error("参与失败，手续费不足");
            throw new PlatException(Constants.PARAM_ERROR, "参与失败，手续费不足，最低要求：" + String.valueOf(mkLuckyDrawConfig.getDeductFee()));
        }
        return offlineCoinVolume;
    }

    private void updateConfig(MkLuckyDrawConfig mkLuckyDrawConfig) {
        mkLuckyDrawConfig.setPoolVolume(mkLuckyDrawConfig.getPoolVolume().add(mkLuckyDrawConfig.getStepAddVolume()));
        if (ObjectUtils.isEmpty(mkLuckyDrawConfig.getGrantVolume())) {
            mkLuckyDrawConfig.setGrantVolume(BigDecimal.ZERO);
        }
        BigDecimal remainVolume = mkLuckyDrawConfig.getVolume().subtract(mkLuckyDrawConfig.getGrantVolume());
        if (mkLuckyDrawConfig.getPoolVolume().compareTo(remainVolume) > 0) {
            mkLuckyDrawConfig.setPoolVolume(remainVolume);
        }
        mkLuckyDrawConfig.setFee(mkLuckyDrawConfig.getFee().add(mkLuckyDrawConfig.getDeductFee()));
        mkLuckyDrawConfig.setPlayerNumber(mkLuckyDrawConfig.getPlayerNumber() + 1);
        Timestamp timestamp = Timestamp.valueOf(mkLuckyDrawConfig.getUpdateDate());
        long count = mkLuckyDrawConfigDao.updateConfig(mkLuckyDrawConfig.getId(), mkLuckyDrawConfig.getPoolVolume(), mkLuckyDrawConfig.getPlayerNumber(), mkLuckyDrawConfig.getFee(), timestamp);
        if (count != 1) {
            logger.info("更新活动异常");
            throw new PlatException(Constants.PARAM_ERROR, "更新活动异常");
        }
    }

    private void createPlayer(MkLuckyDrawConfig mkLuckyDrawConfig, PlatUser platUser, OfflineCoinVolume userCoinVolume, String playerId) {
        MkLuckyDrawPlayer mkLuckyDrawPlayer = new MkLuckyDrawPlayer();
        mkLuckyDrawPlayer.setId(playerId);
        mkLuckyDrawPlayer.setStatus("0");
        mkLuckyDrawPlayer.setPeriods(mkLuckyDrawConfig.getPeriods());
        mkLuckyDrawPlayer.setCoinId(mkLuckyDrawConfig.getCoinId());
        mkLuckyDrawPlayer.setCoinSymbol(mkLuckyDrawConfig.getCoinSymbol());
        mkLuckyDrawPlayer.setDeductFee(mkLuckyDrawConfig.getDeductFee());
        mkLuckyDrawPlayer.setUserId(platUser.getId());
        mkLuckyDrawPlayer.setMail(platUser.getMail());
        mkLuckyDrawPlayer.setMobile(platUser.getMobile());
        mkLuckyDrawPlayer.setRealName(platUser.getRealName());
        mkLuckyDrawPlayer.setVolume(userCoinVolume.getVolume());
        mkLuckyDrawPlayer.setCreateDate(LocalDateTime.now());
        mkLuckyDrawPlayer.setUpdateDate(LocalDateTime.now());
        mkLuckyDrawPlayer.setCreateBy("lucky");
        mkLuckyDrawPlayer.setUpdateBy("lucky");
        long count = mkLuckyDrawPlayerDao.insert(mkLuckyDrawPlayer);
        if (count != 1) {
            logger.info("更新活动异常");
            throw new PlatException(Constants.PARAM_ERROR, "更新活动异常");
        }
    }

    @Override
    public ResponsePage<MkLuckyDrawPlayer> findPage(RequestQuery requestQuery) {
        ResponsePage<MkLuckyDrawPlayer> responsePage = new ResponsePage<>();
        Page<MkLuckyDrawPlayer> page = PageHelper.startPage(requestQuery.getCurrentPage(), requestQuery.getShowCount());
        List<MkLuckyDrawPlayer> data = mkLuckyDrawPlayerDao.findWinnerList();
        data.forEach(mkLuckyDrawPlayer -> {
            mkLuckyDrawPlayer.setMail(this.maskOff(StringUtils.isEmpty(mkLuckyDrawPlayer.getMobile()) ? mkLuckyDrawPlayer.getMail() : mkLuckyDrawPlayer.getMobile()));
            mkLuckyDrawPlayer.setMobile("");
            mkLuckyDrawPlayer.setRealName("");
            mkLuckyDrawPlayer.setVolume(BigDecimal.ZERO);
            mkLuckyDrawPlayer.setDeductFee(BigDecimal.ZERO);
            mkLuckyDrawPlayer.setUserId("");
        });
        responsePage.setCount(page.getTotal());
        responsePage.setList(data);
        return responsePage;
    }

    @Override
    public ResponsePage<MkLuckyDrawPlayer> findMyPage(RequestQuery requestQuery, PlatUser platUser) {
        ResponsePage<MkLuckyDrawPlayer> responsePage = new ResponsePage<>();
        Page<MkLuckyDrawPlayer> page = PageHelper.startPage(requestQuery.getCurrentPage(), requestQuery.getShowCount());
        List<MkLuckyDrawPlayer> data = mkLuckyDrawPlayerDao.findByUserId(platUser.getId());
        data.forEach(mkLuckyDrawPlayer -> {
            mkLuckyDrawPlayer.setUserId("");
        });
        responsePage.setCount(page.getTotal());
        responsePage.setList(data);
        return responsePage;
    }
}
