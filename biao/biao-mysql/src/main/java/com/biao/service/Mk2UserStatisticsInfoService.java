package com.biao.service;

import com.biao.entity.mk2.Mk2PopularizeReleaseLog;
import com.biao.entity.mk2.Mk2UserAllLockCoin;
import com.biao.entity.mk2.Mk2UserStatisticsInfo;
import com.biao.pojo.ResponsePage;
import com.biao.vo.UserStatisticsInfoVO;

import java.math.BigDecimal;
import java.util.List;


public interface Mk2UserStatisticsInfoService {

    BigDecimal getTeamHoldCoinTotal(String userId);

    Mk2UserStatisticsInfo getTotalInfo();

    Mk2UserStatisticsInfo getMyTotalInfo(String userId);

    ResponsePage<Mk2UserStatisticsInfo> getHoldMiningHistory(UserStatisticsInfoVO infoVO);

    ResponsePage<Mk2UserStatisticsInfo> getTeamMiningHistory(UserStatisticsInfoVO infoVO);

    ResponsePage<Mk2UserStatisticsInfo> getAreaMemberBonus(UserStatisticsInfoVO infoVO);

    ResponsePage<Mk2UserStatisticsInfo> getNodalMemberBonus(UserStatisticsInfoVO infoVO);

    ResponsePage<Mk2UserStatisticsInfo> getCommonMemberBonus(UserStatisticsInfoVO infoVO);

    ResponsePage<Mk2PopularizeReleaseLog> getMemberRelease(UserStatisticsInfoVO infoVO);

    List<Mk2UserAllLockCoin> getUserAllLockVolume(String userId);
}
