package com.biao.service.impl;

import com.biao.entity.mk2.Mk2PopularizeReleaseLog;
import com.biao.entity.mk2.Mk2UserAllLockCoin;
import com.biao.entity.mk2.Mk2UserStatisticsInfo;
import com.biao.mapper.MkCoinDestroyRecordDao;
import com.biao.mapper.mk2.Mk2PopularizeReleaseLogDao;
import com.biao.mapper.mk2.Mk2UserStatisticsInfoDao;
import com.biao.pojo.ResponsePage;
import com.biao.service.Mk2UserStatisticsInfoService;
import com.biao.util.DateUtils;
import com.biao.vo.UserStatisticsInfoVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class Mk2UserStatisticsInfoServiceImpl implements Mk2UserStatisticsInfoService {

    @Autowired
    private Mk2UserStatisticsInfoDao mk2UserStatisticsInfoDao;

    @Autowired
    private MkCoinDestroyRecordDao mkCoinDestroyRecordDao;

    @Autowired
    private Mk2PopularizeReleaseLogDao mk2PopularizeReleaseLogDao;

    private static final String COIN_SYMBOL_UES = "UES";

    /**
     * 获取userid当前团队的持币总量
     *
     * @param userId
     * @return
     */
    public BigDecimal getTeamHoldCoinTotal(String userId) {
        LocalDateTime lastMiningDate = mk2UserStatisticsInfoDao.lastMiningDateTime(); // 上一次挖矿时间
        return mk2UserStatisticsInfoDao.getTeamHoldCoinTotal(userId, COIN_SYMBOL_UES, lastMiningDate);
    }

    public Mk2UserStatisticsInfo getTotalInfo() {

        // 查询 挖矿总量和已挖矿量
        Mk2UserStatisticsInfo info = mk2UserStatisticsInfoDao.countMiningTotal();
        // 昨日挖矿
        BigDecimal lastMiningCoinVolumeTotal = BigDecimal.ZERO;
        LocalDateTime lastMiningDate = mk2UserStatisticsInfoDao.lastMiningDateTime(); // 上一次挖矿时间
        info.setLastMiningDate(lastMiningDate);
        if (lastMiningDate != null) {
            info.setLastBestHoldCoinVolume(mk2UserStatisticsInfoDao.findLastBestHoldCoinVolume(lastMiningDate)); // 上次挖矿最佳持币量
        }

        BigDecimal lastHoldMiningTotal = Optional.ofNullable(mk2UserStatisticsInfoDao.countLastHoldMiningTotal()).orElse(BigDecimal.ZERO);
        BigDecimal lastTeamMiningTotal = Optional.ofNullable(mk2UserStatisticsInfoDao.countLastTeamMiningTotal()).orElse(BigDecimal.ZERO);

        info.setLastMingHoldCoinTotal(lastHoldMiningTotal);// 上次持币挖矿总量
        info.setLastMingTeamCoinTotal(lastTeamMiningTotal);// 上次团队挖矿总量

        lastMiningCoinVolumeTotal = lastMiningCoinVolumeTotal.add(lastHoldMiningTotal);
        lastMiningCoinVolumeTotal = lastMiningCoinVolumeTotal.add(lastTeamMiningTotal);
        info.setLastMiningCoinVolumeTotal(lastMiningCoinVolumeTotal); // 上一次挖总量矿量 （持币挖矿 + 团队挖矿）
        // 锁仓总量
        BigDecimal lockCoinVolumeTotal = BigDecimal.ZERO;
        BigDecimal areaLockTotal = Optional.ofNullable(mk2UserStatisticsInfoDao.countAreaLockTotal()).orElse(BigDecimal.ZERO);// 合伙人锁定
        BigDecimal commonLockTotal = Optional.ofNullable(mk2UserStatisticsInfoDao.countCommonLockTotal()).orElse(BigDecimal.ZERO);// 普通锁定
        BigDecimal nodalLockTotal = Optional.ofNullable(mk2UserStatisticsInfoDao.countNodalLockTotal()).orElse(BigDecimal.ZERO);// 节点人锁定
        lockCoinVolumeTotal = lockCoinVolumeTotal.add(areaLockTotal);
        lockCoinVolumeTotal = lockCoinVolumeTotal.add(commonLockTotal);
        lockCoinVolumeTotal = lockCoinVolumeTotal.add(nodalLockTotal);
        info.setLockCoinVolumeTotal(lockCoinVolumeTotal);// 锁定总量

        info.setDestroyCoinVolumeTotal(mkCoinDestroyRecordDao.statByCoinSymbol(COIN_SYMBOL_UES)); // 销毁总量

        return info;
    }

    /**
     * 我的收益
     *
     * @param userId
     * @return
     */
    public Mk2UserStatisticsInfo getMyTotalInfo(String userId) {

        LocalDateTime lastBonusDate = mk2UserStatisticsInfoDao.lastBonusDateTime();
        LocalDateTime lastMiningDate = mk2UserStatisticsInfoDao.lastMiningDateTime();

        Mk2UserStatisticsInfo info = new Mk2UserStatisticsInfo();
        info.setMyLastBonusVolume(mk2UserStatisticsInfoDao.userLastBonusVolume(userId, lastBonusDate));
        info.setMyLastMiningVolume(mk2UserStatisticsInfoDao.userLastMiningVolume(userId, lastMiningDate));
        info.setMyLastMiningHoldVolume(mk2UserStatisticsInfoDao.userLastMiningHoldVolume(userId, lastMiningDate));
        info.setMyLastMiningTeamVolume(mk2UserStatisticsInfoDao.userLastMiningTeamVolume(userId, lastMiningDate));
        info.setMyHoldTotalMiningVolume(mk2UserStatisticsInfoDao.userTotalMiningVolumeByType(userId, "1"));
        info.setMyTeamTotalMiningVolume(mk2UserStatisticsInfoDao.userTotalMiningVolumeByType(userId, "2"));
        return info;
    }

    /**
     * 持币挖矿流水
     *
     * @param infoVO
     * @return
     */
    public ResponsePage<Mk2UserStatisticsInfo> getHoldMiningHistory(UserStatisticsInfoVO infoVO) {

        ResponsePage<Mk2UserStatisticsInfo> responsePage = new ResponsePage<>();
        Page<Mk2UserStatisticsInfo> page = PageHelper.startPage(infoVO.getCurrentPage(), infoVO.getShowCount());
        List<Mk2UserStatisticsInfo> data = mk2UserStatisticsInfoDao.findHoldMiningHistory(infoVO.getUserId());
        responsePage.setCount(page.getTotal());
        responsePage.setList(data);
        return responsePage;
    }

    /**
     * 团队挖矿流水
     *
     * @param infoVO
     * @return
     */
    public ResponsePage<Mk2UserStatisticsInfo> getTeamMiningHistory(UserStatisticsInfoVO infoVO) {
        ResponsePage<Mk2UserStatisticsInfo> responsePage = new ResponsePage<>();
        Page<Mk2UserStatisticsInfo> page = PageHelper.startPage(infoVO.getCurrentPage(), infoVO.getShowCount());
        List<Mk2UserStatisticsInfo> data = mk2UserStatisticsInfoDao.findTeamMiningHistory(infoVO.getUserId());
        responsePage.setCount(page.getTotal());
        responsePage.setList(data);
        return responsePage;
    }

    /**
     * 区域合伙人分红流水
     *
     * @param infoVO
     * @return
     */
    public ResponsePage<Mk2UserStatisticsInfo> getAreaMemberBonus(UserStatisticsInfoVO infoVO) {
        ResponsePage<Mk2UserStatisticsInfo> responsePage = new ResponsePage<>();
        Page<Mk2UserStatisticsInfo> page = PageHelper.startPage(infoVO.getCurrentPage(), infoVO.getShowCount());
        List<Mk2UserStatisticsInfo> fixedInfos = mk2UserStatisticsInfoDao.findAreaFixedBonusHistory(infoVO.getUserId());
        List<Mk2UserStatisticsInfo> phoneInfos = mk2UserStatisticsInfoDao.findAreaPhoneBonusHistory(infoVO.getUserId());
        Map<String, Mk2UserStatisticsInfo> phoneInfoMap = listInfo2Map(phoneInfos);
        List<Mk2UserStatisticsInfo> referInfos = mk2UserStatisticsInfoDao.findAreaReferBonusHistory(infoVO.getUserId());
        Map<String, Mk2UserStatisticsInfo> referInfoMap = listInfo2Map(referInfos);

        if (CollectionUtils.isNotEmpty(fixedInfos)) {
            fixedInfos.forEach(info -> {
                info.setAreaBonusPhoneVolume(phoneInfoMap.get(DateUtils.formaterDate(info.getAreaBonusDate().toLocalDate())).getAreaBonusPhoneVolume());
                info.setAreaBonusReferVolume(phoneInfoMap.get(DateUtils.formaterDate(info.getAreaBonusDate().toLocalDate())).getAreaBonusReferVolume());
            });
        }
        responsePage.setCount(page.getTotal());
        responsePage.setList(fixedInfos);
        return responsePage;
    }

    private Map<String, Mk2UserStatisticsInfo> listInfo2Map(List<Mk2UserStatisticsInfo> infos) {
        Map<String, Mk2UserStatisticsInfo> infoMap = new HashMap<String, Mk2UserStatisticsInfo>();
        if (CollectionUtils.isNotEmpty(infos)) {
            infos.forEach(info -> {
                infoMap.put(DateUtils.formaterDate(info.getAreaBonusDate().toLocalDate()), info);
            });
        }
        return infoMap;
    }

    /**
     * 节点人分红流水
     *
     * @param infoVO
     * @return
     */
    public ResponsePage<Mk2UserStatisticsInfo> getNodalMemberBonus(UserStatisticsInfoVO infoVO) {
        ResponsePage<Mk2UserStatisticsInfo> responsePage = new ResponsePage<>();
        Page<Mk2UserStatisticsInfo> page = PageHelper.startPage(infoVO.getCurrentPage(), infoVO.getShowCount());
        List<Mk2UserStatisticsInfo> data = mk2UserStatisticsInfoDao.findNodalBonusHistory(infoVO.getUserId());
        responsePage.setCount(page.getTotal());
        responsePage.setList(data);
        return responsePage;
    }

    /**
     * 普通会员分红流水
     *
     * @param infoVO
     * @return
     */
    public ResponsePage<Mk2UserStatisticsInfo> getCommonMemberBonus(UserStatisticsInfoVO infoVO) {
        ResponsePage<Mk2UserStatisticsInfo> responsePage = new ResponsePage<>();
        Page<Mk2UserStatisticsInfo> page = PageHelper.startPage(infoVO.getCurrentPage(), infoVO.getShowCount());
        List<Mk2UserStatisticsInfo> data = mk2UserStatisticsInfoDao.findCommonBonusHistory(infoVO.getUserId());
        responsePage.setCount(page.getTotal());
        responsePage.setList(data);
        return responsePage;
    }

    /**
     * 冻结币释放流水
     *
     * @param infoVO
     * @return
     */
    public ResponsePage<Mk2PopularizeReleaseLog> getMemberRelease(UserStatisticsInfoVO infoVO) {
        ResponsePage<Mk2PopularizeReleaseLog> responsePage = new ResponsePage<>();
        Page<Mk2PopularizeReleaseLog> page = PageHelper.startPage(infoVO.getCurrentPage(), infoVO.getShowCount());
        List<Mk2PopularizeReleaseLog> data = mk2PopularizeReleaseLogDao.findByUserIdAndRelationId(infoVO.getUserId(), infoVO.getRelationId());
        responsePage.setCount(page.getTotal());
        responsePage.setList(data);
        return responsePage;
    }

    /**
     * 用户锁币记录
     *
     * @param userId
     * @return
     */
    public List<Mk2UserAllLockCoin> getUserAllLockVolume(String userId) {
        return mk2UserStatisticsInfoDao.getUserAllLockVolume(userId);
    }
}
