package com.biao.service.impl.otc;

import com.biao.constant.Constants;
import com.biao.entity.Coin;
import com.biao.entity.OfflineCoinVolume;
import com.biao.entity.PlatUser;
import com.biao.entity.mk2.Mk2PopularizeCommonMember;
import com.biao.entity.otc.OtcAccountSecret;
import com.biao.enums.CycleEnum;
import com.biao.enums.MemberLockEnum;
import com.biao.exception.PlatException;
import com.biao.mapper.CoinDao;
import com.biao.mapper.OfflineCoinVolumeDao;
import com.biao.mapper.PlatUserDao;
import com.biao.mapper.mk2.Mk2PopularizeCommonMemberDao;
import com.biao.mapper.otc.OtcAccountSecretDao;
import com.biao.mapper.otc.OtcOfflineOrderDetailDao;
import com.biao.pojo.ResponsePage;
import com.biao.service.OfflineCoinVolumeService;
import com.biao.service.PlatUserService;
import com.biao.service.otc.OtcAgentService;
import com.biao.util.DateUtils;
import com.biao.util.SnowFlake;
import com.biao.util.otc.MD5Util;
import com.biao.vo.otc.OtcAgentVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import static com.biao.enums.UserTagEnum.OTC_ADVERT;

@Service
public class OtcAgentServiceImpl implements OtcAgentService {

    @Autowired
    private OtcOfflineOrderDetailDao otcOfflineOrderDetailDao;

    @Autowired
    private PlatUserDao platUserDao;

    @Autowired
    private OfflineCoinVolumeDao offlineCoinVolumeDao;

//    private static final long AGENT_COUNT = 20;

    @Autowired
    private OtcAccountSecretDao otcAccountSecretDao;

    @Autowired
    private PlatUserService platUserService;

    @Autowired
    private Mk2PopularizeCommonMemberDao mk2PopularizeCommonMemberDao;

    @Autowired
    private CoinDao coinDao;

    @Autowired
    private OfflineCoinVolumeService offlineCoinVolumeService;

    private static final String AGENT_OTC_PREFIX = "OTC-";

    public OtcAgentVO countAgentDetail(String userId, String loginSource) {
        LocalDateTime beginTime = LocalDateTime.of(LocalDateTime.now().toLocalDate(), LocalTime.MIN);// 今天最小时间
        LocalDateTime endTime = LocalDateTime.of(LocalDateTime.now().toLocalDate(), LocalTime.MAX);// 今天最大时间
        OtcAgentVO otcAgentVO = new OtcAgentVO();
        otcAgentVO.setCountDayDetail(otcOfflineOrderDetailDao.countDayDetail(userId, loginSource, beginTime, endTime));
        otcAgentVO.setSumDayDetailVolume(otcOfflineOrderDetailDao.sumDayDetailVolume(userId, loginSource, beginTime, endTime));
        otcAgentVO.setCountDetailNoComplete(otcOfflineOrderDetailDao.countNoCompleteDetail(userId, loginSource));
        return otcAgentVO;
    }

    @Override
    public ResponsePage<OtcAgentVO> findGroupAgent(OtcAgentVO otcAgentVO, Map<String, String> paramMap) {
        checkSecretKey(otcAgentVO.getKey(), paramMap); // 秘钥校验
        ResponsePage<OtcAgentVO> responsePage = new ResponsePage<>();
        Page<OtcAgentVO> page = PageHelper.startPage(otcAgentVO.getCurrentPage(), otcAgentVO.getShowCount());
        List<PlatUser> offlineOrders = platUserDao.findByTag(otcAgentVO.getTag());

        List<OtcAgentVO> agentVos = new ArrayList<>();
        offlineOrders.forEach(user -> {
            OtcAgentVO otcAgent = new OtcAgentVO();
            BeanUtils.copyProperties(user, otcAgent);
            // 身份证号加密
            agentVos.add(otcAgent);
        });
        responsePage.setCount(page.getTotal());
        responsePage.setList(agentVos);
        return responsePage;
    }

    public ResponsePage<OtcAgentVO> findGroupAgentAndVolume(OtcAgentVO otcAgentVO) {

        // 查询银商组的UES币
        List<OfflineCoinVolume> agentUEScoins = offlineCoinVolumeDao.findByUserTagUES(otcAgentVO.getTag());
        Map<String, OfflineCoinVolume> coinVolumeMap = new HashMap<>();
        agentUEScoins.forEach(coin -> {
            coinVolumeMap.put(coin.getUserId(), coin);
        });
        // 查询银商组人员
        ResponsePage<OtcAgentVO> responsePage = new ResponsePage<>();
        Page<OtcAgentVO> page = PageHelper.startPage(otcAgentVO.getCurrentPage(), otcAgentVO.getShowCount());
        List<PlatUser> offlineOrders = platUserDao.findByTag(otcAgentVO.getTag());

        List<OtcAgentVO> agentVos = new ArrayList<>();
        offlineOrders.forEach(user -> {
            OtcAgentVO otcAgent = new OtcAgentVO();
            BeanUtils.copyProperties(user, otcAgent);
            OfflineCoinVolume agentCoin = coinVolumeMap.get(user.getId());
            if (Objects.nonNull(agentCoin)) {
                otcAgent.setVolume(agentCoin.getVolume());
            } else {
                otcAgent.setVolume(BigDecimal.ZERO);
            }
            // 身份证号加密
            agentVos.add(otcAgent);
        });

        responsePage.setCount(page.getTotal());
        responsePage.setList(agentVos);
        return responsePage;
    }

    public OtcAgentVO findByMobileOrMail(OtcAgentVO otcAgentVO, Map<String, String> paramMap) {
        checkSecretKey(otcAgentVO.getKey(), paramMap);
        String mobileOrMail = otcAgentVO.getMobile();
        if (StringUtils.isBlank(mobileOrMail)) {
            mobileOrMail = otcAgentVO.getMail();
        }
        if (StringUtils.isBlank(mobileOrMail)) {
            return null;
        }
//        PlatUser user = platUserDao.findByMobieOrMail(mobileOrMail);
        PlatUser platUser = platUserService.findByLoginName(mobileOrMail);
        if (Objects.isNull(platUser)) {
            platUser = platUserService.findById(mobileOrMail);
        }
        OtcAgentVO otcAgent = new OtcAgentVO();
        if (Objects.nonNull(platUser)) {
            BeanUtils.copyProperties(platUser, otcAgent);
        } else {
            return null;
        }
        return otcAgent;
    }

    @Transactional
    public void saveAgent(OtcAgentVO otcAgentVO, Map<String, String> paramMap) {
        checkSecretKey(otcAgentVO.getKey(), paramMap);
        if (StringUtils.isBlank(otcAgentVO.getTag()) || !otcAgentVO.getTag().startsWith(AGENT_OTC_PREFIX)) {
            throw new PlatException(Constants.PARAM_ERROR, "标签代码错误");
        }
        PlatUser user = platUserDao.findById(otcAgentVO.getUserId());
        if (Objects.isNull(user) || StringUtils.isNotBlank(user.getTag())) {
            throw new PlatException(Constants.PARAM_ERROR, "用户不存在或用户已经打其他标签");
        }

//        long countAgent = platUserDao.countByTag(otcAgentVO.getTag());
//        if (countAgent > AGENT_COUNT) {
//            throw new PlatException(Constants.PARAM_ERROR, "银商组内成员已满" + AGENT_COUNT);
//        }
        user.setTag(otcAgentVO.getTag());
        long count = platUserDao.updateUserTag(user);
        if (count != 1) {
            throw new PlatException(Constants.UPDATE_ERROR, "更新失败");
        }

        // 设置广告商，需要冻结币种
        saveOtcGGS(otcAgentVO, user);
    }

    private void saveOtcGGS(OtcAgentVO otcAgentVO, PlatUser user) {
        if (StringUtils.isBlank(otcAgentVO.getTag()) || !otcAgentVO.getTag().startsWith(OTC_ADVERT.getCode())) {
            return;
        }
        List<String> ggsCoins = Arrays.asList("UES","EUC");
        if (!ggsCoins.contains(otcAgentVO.getCoinSymbol()) || Objects.isNull(otcAgentVO.getVolume()) || otcAgentVO.getVolume().compareTo(BigDecimal.ZERO) < 1) {
            throw new PlatException(Constants.PARAM_ERROR, "广告商的冻结币种或数量设置错误");
        }
        Coin coin = coinDao.findByName(otcAgentVO.getCoinSymbol());
        String referId = SnowFlake.createSnowFlake().nextIdString();

        // 1、用户C2C账户减可用资产
        offlineCoinVolumeService.coinVolumeSubtract(otcAgentVO.getUserId(), coin.getId(), otcAgentVO.getVolume(), referId);

        // 2、记录用户会员冻结
        Mk2PopularizeCommonMember mk = new Mk2PopularizeCommonMember();
        mk.setId(referId);
        mk.setCoinId(coin.getId());
        mk.setCoinSymbol(otcAgentVO.getCoinSymbol());
        mk.setCreateDate(LocalDateTime.now());
        mk.setLockVolume(otcAgentVO.getVolume());
        mk.setMail(user.getMail());
        mk.setMobile(user.getMobile());
        try {
            mk.setReleaseBeginDate(DateUtils.parseLocalDateTime("2020-12-30 00:00:00"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        mk.setReleaseCycle(CycleEnum.YEAR.getCode());
        mk.setReleaseCycleRatio(BigDecimal.valueOf(20L));
        mk.setReleaseOver("0");
        mk.setReleaseVolume(BigDecimal.ZERO);
        mk.setUpdateDate(LocalDateTime.now());
        mk.setUserId(user.getId());
        mk.setIdCard(user.getIdCard());
        mk.setLockStatus("1");
        mk.setRealName(user.getRealName());
        mk.setType(MemberLockEnum.ADVERTISER_LOCK.getCode());
        mk.setRelationId("");
        mk.setParentId("");
        mk.setRemark(MemberLockEnum.ADVERTISER_LOCK.getMark());
        mk2PopularizeCommonMemberDao.insert(mk);

    }

    private void checkSecretKey(String key, Map<String, String> paramMap) {
        OtcAccountSecret accountSecret = otcAccountSecretDao.findByPublishSource("otc");
        if (Objects.isNull(accountSecret)) {
            throw new PlatException(Constants.PARAM_ERROR, "OTC信息未设置");
        }

        String mykey = MD5Util.getSign(paramMap, accountSecret.getSecret());
        if (StringUtils.isBlank(key) || StringUtils.isBlank(mykey) || !key.equals(mykey)) {
            throw new PlatException(Constants.PARAM_ERROR, "秘钥加密错误");
        }
    }
}
