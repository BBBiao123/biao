package com.biao.service.impl.balance;

import com.biao.entity.Coin;
import com.biao.entity.PlatUser;
import com.biao.entity.balance.*;
import com.biao.mapper.CoinDao;
import com.biao.mapper.PlatUserDao;
import com.biao.mapper.balance.*;
import com.biao.redis.RedisCacheManager;
import com.biao.service.UserCoinVolumeExService;
import com.biao.service.balance.BalanceUserCoinVolumeDetailService;
import com.biao.service.balance.BalanceUserCoinVolumeService;
import com.biao.util.DateUtils;
import com.biao.util.SnowFlake;
import com.biao.vo.KlineVO;
import com.biao.vo.TradePairVO;
import com.biao.vo.redis.RedisExPairVO;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 *余币宝统计和明细service
 */
@Service
public class BalanceUserCoinVolumeDetailServiceImpl implements BalanceUserCoinVolumeDetailService {

    @Autowired(required = false)
    private BalanceUserCoinVolumeDetailDao balanceUserCoinVolumeDetailDao;

    @Autowired(required = false)
    private BalanceUserVolumeIncomeDetailDao balanceUserVolumeIncomeDetailDao;

    @Autowired(required = false)
    private BalanceUserCoinVolumeDao balanceUserCoinVolumeDao;

    @Autowired
    private UserCoinVolumeExService userCoinVolumeExService;

    @Autowired(required = false)
    private PlatUserDao platUserDao;

    @Autowired
    private CoinDao coinDao;

    @Autowired(required = false)
    private BalanceChangeUserCoinVolumeDao balanceChangeUserCoinVolumeDao;

    @Autowired(required = false)
    private BalanceUserCoinCountVolumeDao balanceUserCoinCountVolumeDao;

    @Autowired(required = false)
    private BalancePlatJackpotVolumeDetailDao balancePlatJackpotVolumeDetailDao;

    @Override
    public String save(BalanceUserCoinVolumeDetail balanceUserCoinVolumeDetail) {
        String id = SnowFlake.createSnowFlake().nextIdString();

        balanceUserCoinVolumeDetail.setId(id);
        balanceUserCoinVolumeDetailDao.insert(balanceUserCoinVolumeDetail);
        return id;
    }
    @Override
    public void updateById(BalanceUserCoinVolumeDetail balanceUserCoinVolumeDetail) {
        balanceUserCoinVolumeDetailDao.updateById(balanceUserCoinVolumeDetail);
    }

    @Override
    public List<BalanceUserCoinVolumeDetail> findAll(String userId) {
        return balanceUserCoinVolumeDetailDao.findByUserId(userId);
    }

    @Override
    public void  balanceIncomeCount(){
        List<BalanceUserCoinCountVolume>  balanceUserCoinVolumeList= balanceUserCoinCountVolumeDao.findAll();
        if (CollectionUtils.isNotEmpty(balanceUserCoinVolumeList)) {
            balanceUserCoinVolumeList.forEach(e -> {
                List<BalanceUserCoinVolumeDetail> balanceDetailList=balanceUserCoinVolumeDetailDao.findByUserIdAndCoin(e.getUserId(),e.getCoinSymbol());
                if (CollectionUtils.isNotEmpty(balanceDetailList)) {
                    balanceDetailList.forEach(balanceDetail -> {
                       if(balanceDetail.getStaticsIncome() != null && balanceDetail.getStaticsIncome().compareTo(BigDecimal.ZERO)>0) {
                           BalanceUserVolumeIncomeDetail balanceIncomeDetail = new BalanceUserVolumeIncomeDetail();
                           String id = SnowFlake.createSnowFlake().nextIdString();
                           balanceIncomeDetail.setId(id);
                           balanceIncomeDetail.setRewardType("1");
                           balanceIncomeDetail.setDetailReward(balanceDetail.getStaticsIncome());
                           balanceIncomeDetail.setCoinSymbol(e.getCoinPlatSymbol());
                           balanceIncomeDetail.setIncomeDate(balanceDetail.getIncomeDate());
                           balanceIncomeDetail.setUserId(e.getUserId());
                           balanceIncomeDetail.setVersion(0);
                           balanceIncomeDetail.setCreateDate(LocalDateTime.now());
                           balanceUserVolumeIncomeDetailDao.insert(balanceIncomeDetail);
                       }
                        if(balanceDetail.getDynamicsIncome() != null && balanceDetail.getDynamicsIncome().compareTo(BigDecimal.ZERO)>0) {
                            BalanceUserVolumeIncomeDetail balanceIncomeDetail2=new BalanceUserVolumeIncomeDetail();
                            String id2 = SnowFlake.createSnowFlake().nextIdString();
                            balanceIncomeDetail2.setId(id2);
                            balanceIncomeDetail2.setRewardType("2");
                            balanceIncomeDetail2.setDetailReward(balanceDetail.getDynamicsIncome());
                            balanceIncomeDetail2.setCoinSymbol(e.getCoinPlatSymbol());
                            balanceIncomeDetail2.setIncomeDate(balanceDetail.getIncomeDate());
                            balanceIncomeDetail2.setUserId(e.getUserId());
                            balanceIncomeDetail2.setVersion(0);
                            balanceIncomeDetail2.setCreateDate(LocalDateTime.now());
                            balanceUserVolumeIncomeDetailDao.insert(balanceIncomeDetail2);
                        }
                        if(balanceDetail.getCommunityManageReward() != null && balanceDetail.getCommunityManageReward().compareTo(BigDecimal.ZERO)>0) {
                            BalanceUserVolumeIncomeDetail balanceIncomeDetail3=new BalanceUserVolumeIncomeDetail();
                            String id3 = SnowFlake.createSnowFlake().nextIdString();
                            balanceIncomeDetail3.setId(id3);
                            balanceIncomeDetail3.setRewardType("3");
                            balanceIncomeDetail3.setDetailReward(balanceDetail.getCommunityManageReward());
                            balanceIncomeDetail3.setCoinSymbol(e.getCoinPlatSymbol());
                            balanceIncomeDetail3.setIncomeDate(balanceDetail.getIncomeDate());
                            balanceIncomeDetail3.setUserId(e.getUserId());
                            balanceIncomeDetail3.setVersion(0);
                            balanceIncomeDetail3.setCreateDate(LocalDateTime.now());
                            balanceUserVolumeIncomeDetailDao.insert(balanceIncomeDetail3);
                        }
                        if(balanceDetail.getEqualityReward() != null && balanceDetail.getEqualityReward().compareTo(BigDecimal.ZERO)>0) {
                            BalanceUserVolumeIncomeDetail balanceIncomeDetail4=new BalanceUserVolumeIncomeDetail();
                            String id4 = SnowFlake.createSnowFlake().nextIdString();
                            balanceIncomeDetail4.setId(id4);
                            balanceIncomeDetail4.setRewardType("4");
                            balanceIncomeDetail4.setDetailReward(balanceDetail.getEqualityReward());
                            balanceIncomeDetail4.setCoinSymbol(e.getCoinPlatSymbol());
                            balanceIncomeDetail4.setIncomeDate(balanceDetail.getIncomeDate());
                            balanceIncomeDetail4.setUserId(e.getUserId());
                            balanceIncomeDetail4.setVersion(0);
                            balanceIncomeDetail4.setCreateDate(LocalDateTime.now());
                            balanceUserVolumeIncomeDetailDao.insert(balanceIncomeDetail4);
                        }
                        if(balanceDetail.getLevelDifferenceReward() != null && balanceDetail.getLevelDifferenceReward().compareTo(BigDecimal.ZERO)>0) {
                            BalanceUserVolumeIncomeDetail balanceIncomeDetail5=new BalanceUserVolumeIncomeDetail();
                            String id5 = SnowFlake.createSnowFlake().nextIdString();
                            balanceIncomeDetail5.setId(id5);
                            balanceIncomeDetail5.setRewardType("5");
                            balanceIncomeDetail5.setDetailReward(balanceDetail.getLevelDifferenceReward());
                            balanceIncomeDetail5.setCoinSymbol(e.getCoinPlatSymbol());
                            balanceIncomeDetail5.setIncomeDate(balanceDetail.getIncomeDate());
                            balanceIncomeDetail5.setUserId(e.getUserId());
                            balanceIncomeDetail5.setVersion(0);
                            balanceIncomeDetail5.setCreateDate(LocalDateTime.now());
                            balanceUserVolumeIncomeDetailDao.insert(balanceIncomeDetail5);
                        }

                        //总资产计算
                        e.setCoinBalance(e.getCoinBalance());
                        e.setYesterdayIncome(balanceDetail.getDetailIncome().add(balanceDetail.getDetailReward()));
                        if(e.getAccumulIncome() !=null){
                            e.setAccumulIncome(e.getAccumulIncome().add(e.getYesterdayIncome()));
                        }else{
                            e.setAccumulIncome(e.getYesterdayIncome());
                        }
                        if(e.getAccumulReward() !=null){
                            e.setAccumulReward(e.getAccumulReward().add(e.getYesterdayIncome()).subtract(balanceDetail.getStaticsIncome()));
                        }else{
                            e.setAccumulReward(e.getYesterdayIncome().subtract(balanceDetail.getStaticsIncome()));
                        }
                        //总收入
                        if(e.getSumRevenue() !=null){
                            e.setSumRevenue(e.getSumRevenue().add(balanceDetail.getSumRevenue()));
                        }else {
                            e.setSumRevenue(balanceDetail.getSumRevenue());
                        }
                        if(balanceDetail.getSumRevenue() != null){
                            userCoinVolumeExService.updateIncome(null,balanceDetail.getSumRevenue(),balanceDetail.getUserId(),e.getCoinPlatSymbol(),false);
                        }
                        //昨日收入
                        e.setYesterdayRevenue(balanceDetail.getSumRevenue());
                        //团队刷单总值
                        e.setTeamAmount(balanceDetail.getTeamRecord());

                        //小区刷单市值
                        e.setTeamCommunityAmount(balanceDetail.getTeamCommunityRecord());
                        //社区级别
                        e.setTeamLevel("V"+String.valueOf(balanceDetail.getTeamLevel()));
                        //社区有效用户数
                        e.setValidNum(balanceDetail.getValidNum());
                        //一级邀请人数
                        e.setOneInvite(balanceDetail.getNodeNumber());
                        //刷单奖
                        if(e.getScalpingReward() != null){
                            e.setScalpingReward(e.getScalpingReward().add(balanceDetail.getStaticsIncome()));
                        }else {
                            e.setScalpingReward(balanceDetail.getStaticsIncome());
                        }
                        //分享奖
                        if(e.getShareReward()!= null){
                            e.setShareReward(e.getShareReward().add(balanceDetail.getDynamicsIncome()));
                        }else {
                            e.setShareReward(balanceDetail.getDynamicsIncome());
                        }
                        //社区管理奖
                        if(e.getCommunityManageReward() != null){
                            e.setCommunityManageReward(e.getCommunityManageReward().add(balanceDetail.getCommunityManageReward()));
                        }else {
                            e.setCommunityManageReward(balanceDetail.getCommunityManageReward());
                        }
                        //平级奖
                        if(e.getEqualityReward() != null){
                            e.setEqualityReward(e.getEqualityReward().add(balanceDetail.getEqualityReward()));
                        }else {
                            e.setEqualityReward(balanceDetail.getEqualityReward());
                        }
                        //级益奖
                        if(e.getDifferentialReward() != null){
                            e.setDifferentialReward(e.getDifferentialReward().add(balanceDetail.getCommunityManageReward()));
                        }else {
                            e.setDifferentialReward(balanceDetail.getCommunityManageReward());
                        }
                        e.setYesterdayStaticsIncome(balanceDetail.getStaticsIncome());
                        e.setYesterdayDynamicsIncome(balanceDetail.getDynamicsIncome());
                        e.setYesterdayCommunityReward(balanceDetail.getCommunityManageReward());
                        e.setYesterdayEqualityReward(balanceDetail.getEqualityReward());
                        balanceDetail.setVersion(0);
                        balanceUserCoinCountVolumeDao.updateById(e);
                        balanceUserCoinVolumeDetailDao.updateById(balanceDetail);
                    });
                }

            });
        }

    }
    @Override
    public void balanceIncomeDetail(){
        List<BalanceUserCoinVolume>  balanceUserCoinVolumeList= balanceUserCoinVolumeDao.findAll();

        if (CollectionUtils.isNotEmpty(balanceUserCoinVolumeList)) {
            balanceUserCoinVolumeList.forEach(e -> {
                List<PlatUser> platUserList= platUserDao.findInvitesById(e.getUserId());
                //动态收益和社区奖励计算 begin
                //动态收益
                BigDecimal   detailIncomeTotal=e.getCoinBalance().multiply(e.getDayRate());

                BigDecimal  dayRate=new BigDecimal(0);
                //社区奖励
                BigDecimal detailRewardTotal=new BigDecimal(0);

                if(CollectionUtils.isNotEmpty(platUserList)){
                    List<PlatUser> allPlatUserList=new ArrayList<PlatUser>();
                    allPlatUserList.addAll(platUserList);
                    treePlatUserList(platUserList,allPlatUserList);
                    Map<String,BigDecimal> detailRewardMap=new HashMap<String,BigDecimal>();
                    calRewardTotal(detailRewardMap,allPlatUserList,e);
                    int length=platUserList.size();
                   for (int i=0;i<length;i++) {
                       PlatUser platUser=platUserList.get(i);
                        List<BalanceUserCoinVolume> childList = balanceUserCoinVolumeDao.findByUserIdAndCoin(platUser.getId(),e.getCoinSymbol());
                        BalanceUserCoinVolume balanceTmp = null;
                        if (CollectionUtils.isNotEmpty(childList)) {
                            balanceTmp = childList.get(0);
                        }

                       if(balanceTmp != null){
                           detailIncomeTotal= detailIncomeTotal.add(balanceTmp.getCoinBalance().multiply(balanceTmp.getDayRate()).divide(new BigDecimal(2)));
                       }
                        if(length>=3){

                           PlatUser  platUserBen=  platUserDao.findById(e.getUserId());
                           if(platUserBen !=null && StringUtils.isNotEmpty( platUserBen.getReferId()) ){
                               List<BalanceUserCoinVolume> supList = balanceUserCoinVolumeDao.findByUserIdAndCoin(platUserBen.getReferId(),e.getCoinSymbol());
                               BalanceUserCoinVolume balanceSupTmp = null;
                               if (CollectionUtils.isNotEmpty(supList)) {
                                   balanceSupTmp = supList.get(0);
                               }
                               if(balanceSupTmp != null){
                                   detailIncomeTotal= detailIncomeTotal.add(balanceSupTmp.getCoinBalance().multiply(balanceSupTmp.getDayRate()).multiply(new BigDecimal(0.15)));//此处不好计算，因为此时的上级动态收益无法确定
                               }
                           }
                       }
                       if(length>5){
                           if( i<=length-5){
                               if(dayRate.compareTo(new BigDecimal(0.005))<0) {
                                   dayRate= dayRate.add(new BigDecimal(0.001));
                               }

                           }
                       }
                   }

                    if(detailRewardMap.get("communityBalance") != null){
                        if(length>=10 && detailRewardMap.get("communityBalance").compareTo(new BigDecimal(10000000))>=0 ){
                            //待确认
                        }else if(length>=10 && detailRewardMap.get("communityBalance").compareTo(new BigDecimal(5000000))>=0 ){
                            detailRewardTotal=  detailRewardTotal.add(detailRewardMap.get("communityIncome").multiply(new BigDecimal(0.20)));
                        }else if(length>=10 && detailRewardMap.get("communityBalance").compareTo(new BigDecimal(2000000))>=0 && detailRewardMap.get("communityBalance").compareTo(new BigDecimal(5000000))<0){
                            detailRewardTotal= detailRewardTotal.add(detailRewardMap.get("communityIncome").multiply(new BigDecimal(0.15)));
                        }else if(length>=10 && detailRewardMap.get("communityBalance").compareTo(new BigDecimal(500000))>=0 && detailRewardMap.get("communityBalance").compareTo(new BigDecimal(2000000))<0){
                            detailRewardTotal= detailRewardTotal.add(detailRewardMap.get("communityIncome").multiply(new BigDecimal(0.1)));
                        }else if(length>=5  && detailRewardMap.get("communityBalance").compareTo(new BigDecimal(100000))>=0){
                                detailRewardTotal=detailRewardTotal.add(detailRewardMap.get("communityIncome").multiply(new BigDecimal(0.05)));
                        }
                    }

                }
                //动态收益和社区奖励计算 end


                BalanceUserCoinVolumeDetail balanceUserCoinVolumeDetail=new BalanceUserCoinVolumeDetail();
                String id = SnowFlake.createSnowFlake().nextIdString();
                balanceUserCoinVolumeDetail.setId(id);
                balanceUserCoinVolumeDetail.setCoinSymbol(e.getCoinSymbol());
                balanceUserCoinVolumeDetail.setUserId(e.getUserId());

                //收益明细总计
                balanceUserCoinVolumeDetail.setDetailIncome(detailIncomeTotal);

                //奖励算法
                balanceUserCoinVolumeDetail.setDetailReward(detailRewardTotal);
                //收益日期精确到天
                balanceUserCoinVolumeDetail.setIncomeDate(LocalDateTime.now().minusHours(1));
                balanceUserCoinVolumeDetail.setCreateDate(LocalDateTime.now());
                balanceUserCoinVolumeDetail.setUpdateDate(LocalDateTime.now());
                balanceUserCoinVolumeDetail.setVersion(1);
                balanceUserCoinVolumeDetailDao.insert(balanceUserCoinVolumeDetail);
            });
        }
    }
    public  void treePlatUserList(List<PlatUser> userList,List<PlatUser> allUserList) {
        for (PlatUser user : userList) {

            List<PlatUser> platList= platUserDao.findInvitesById(user.getId());
            //遍历出父id等于参数的id，add进子节点集合
            if (CollectionUtils.isNotEmpty(platList)) {
                //递归遍历下一级
                allUserList.addAll(platList);
                treePlatUserList(platList,allUserList);

            }
        }

    }

    public  void calRewardTotal(Map<String,BigDecimal> detailRewardMap, List<PlatUser> allUserList, BalanceUserCoinVolume userCoinVolume) {
        BigDecimal communityIncome=new BigDecimal(0);
        BigDecimal communityBalance=new BigDecimal(0);
        for (PlatUser user : allUserList) {
            List<BalanceUserCoinVolume> platBalanceList = balanceUserCoinVolumeDao.findByUserIdAndCoin(user.getId(),userCoinVolume.getCoinSymbol());
            BalanceUserCoinVolume balanceVolumeTmp = null;
            if (CollectionUtils.isNotEmpty(platBalanceList)) {
                balanceVolumeTmp = platBalanceList.get(0);
            }
            if(balanceVolumeTmp != null){
                communityIncome=communityIncome.add(balanceVolumeTmp.getCoinBalance().multiply(balanceVolumeTmp.getDayRate()));
                communityBalance= communityBalance.add(balanceVolumeTmp.getCoinBalance());
            }
        }
        detailRewardMap.put("communityIncome",communityIncome);
        detailRewardMap.put("communityBalance",communityBalance);

    }

    /**
     * 重新梳理收益规账新方法
     */
    @Override
    public void balanceIncomeDetailNew(Map<String ,BigDecimal> map,Map<String,TradePairVO>  tradePairMap){
        //静态收益、动态收益1和3 计算
        staticsIncomeAndPartDynamics(map,tradePairMap);

        //团队业绩、团队小区业绩、社区总收益 计算
        communityRecordAndLevel();

        //社区管理奖 计算
        calcManagementAward(map,tradePairMap);

        //动态收益2、平级奖 计算
        calcIncomeAndEqualAward(map);
    }
    public  void treeCommunityUserList(List<BalanceUserCoinCountVolume> userList,List<BalanceUserCoinCountVolume> allUserList) {
        for (BalanceUserCoinCountVolume user : userList) {

            List<BalanceUserCoinCountVolume> platList= balanceUserCoinCountVolumeDao.findInvitesById(user.getUserId(),user.getCoinSymbol());
            //遍历出父id等于参数的id，add进子节点集合
            if (CollectionUtils.isNotEmpty(platList)) {
                //递归遍历下一级
                allUserList.addAll(platList);
                treeCommunityUserList(platList,allUserList);
            }
        }

    }
    public  boolean treeCommunityUserDetailList(List<BalanceUserCoinVolumeDetail> userList) {
        for (BalanceUserCoinVolumeDetail user : userList) {

              if (user.getTeamLevel()>0){
                  return true;
              }
              List<BalanceUserCoinVolumeDetail> platList= balanceUserCoinVolumeDetailDao.findAllByReferId(user.getUserId(),user.getCoinSymbol());
            //遍历出父id等于参数的id，add进子节点集合
            if (CollectionUtils.isNotEmpty(platList)) {

                treeCommunityUserDetailList(platList);
            }
        }
        return false;
    }
    /**
     * 静态收益计算和动态收益1和3计算
     */
    public void staticsIncomeAndPartDynamics(Map<String ,BigDecimal> map,Map<String,TradePairVO>  tradePairMap){
        List<BalanceUserCoinVolume>  balanceUserCoinVolumeList2= balanceUserCoinVolumeDao.findAll();
        Map<String,List<BalanceUserCoinCountVolume>> balanceUserCoinVolumeMap=new HashMap<String,List<BalanceUserCoinCountVolume>>();
        if (CollectionUtils.isNotEmpty(balanceUserCoinVolumeList2)) {
            for(BalanceUserCoinVolume balanceUserCoinVolume: balanceUserCoinVolumeList2){
                BalanceUserCoinCountVolume balanceUserCoinCountVolume=new BalanceUserCoinCountVolume();
                BeanUtils.copyProperties(balanceUserCoinVolume, balanceUserCoinCountVolume);
                List<BalanceUserCoinCountVolume> balanceUserCoinCountVolumeList=balanceUserCoinVolumeMap.get(balanceUserCoinCountVolume.getUserId());
                if(CollectionUtils.isNotEmpty(balanceUserCoinCountVolumeList)){
                    balanceUserCoinCountVolumeList.add(balanceUserCoinCountVolume);
                }else{
                    balanceUserCoinCountVolumeList=new ArrayList<BalanceUserCoinCountVolume>();
                    balanceUserCoinCountVolumeList.add(balanceUserCoinCountVolume);
                    balanceUserCoinVolumeMap.put(balanceUserCoinCountVolume.getUserId(),balanceUserCoinCountVolumeList);
                }
            }

        }

        for(String key:balanceUserCoinVolumeMap.keySet()){
            List<BalanceUserCoinCountVolume>  countlist=balanceUserCoinVolumeMap.get(key);
            BalanceUserCoinCountVolume balanceCountVolume =new BalanceUserCoinCountVolume();

            BigDecimal coinBalance=BigDecimal.ZERO;
            if (CollectionUtils.isNotEmpty(countlist)) {
                balanceCountVolume=countlist.get(0);
                for(BalanceUserCoinCountVolume countVolume: countlist){
                    BigDecimal lastPrice=BigDecimal.ZERO;
                    BigDecimal coinCount=BigDecimal.ZERO;

                    TradePairVO tradePair=tradePairMap.get(countVolume.getCoinSymbol());

                    if(tradePair!=null && tradePair.getLatestPrice() != null){
                        lastPrice=tradePair.getLatestPrice();
                    }
                    if(countVolume.getCoinBalance() != null){
                        coinCount=lastPrice.multiply(countVolume.getCoinBalance());
                    }
                    if("USDT".equals(countVolume.getCoinSymbol()) && countVolume.getCoinBalance() != null){
                        coinCount=countVolume.getCoinBalance();
                    }
                    coinBalance=coinBalance.add(coinCount);

                }
            }
            balanceCountVolume.setId(null);
            List<BalanceUserCoinCountVolume> balanceCountList= balanceUserCoinCountVolumeDao.findByUserId(balanceCountVolume.getUserId());
            if (CollectionUtils.isNotEmpty(balanceCountList)) {
                balanceCountVolume=balanceCountList.get(0);
            }
            balanceCountVolume.setCoinBalance(coinBalance);
            balanceCountVolume.setCoinSymbol("USDT");
            balanceCountVolume.setCoinPlatSymbol("MG");
            if( org.apache.commons.lang.StringUtils.isNotEmpty(balanceCountVolume.getId()) &&  !"null".equals(balanceCountVolume.getId())){
                balanceUserCoinCountVolumeDao.updateById(balanceCountVolume);
            }else{
                String id = SnowFlake.createSnowFlake().nextIdString();
                balanceCountVolume.setId(id);
                balanceCountVolume.setCreateDate(LocalDateTime.now());
                balanceUserCoinCountVolumeDao.insert(balanceCountVolume);
            }

        }
        List<BalanceUserCoinCountVolume>  balanceUserCoinVolumeList= balanceUserCoinCountVolumeDao.findAll();
        if (CollectionUtils.isNotEmpty(balanceUserCoinVolumeList)) {
            balanceUserCoinVolumeList.forEach(e->{
                //用户静态收益计算 begin
                BigDecimal   staticsIncomeTotal=new BigDecimal(0);
                //用户静态收益计算 end
                //动态收益 1 和3
                BigDecimal   dynamicsIncomeTotal=new BigDecimal(0);
                //动态收益3的静态收益增长率
                BigDecimal   dayRate=new BigDecimal(0);

                //社区静态总收益
                BigDecimal   communityStaticsIncome=new BigDecimal(0);

                BigDecimal platPrice=BigDecimal.ZERO;

                //社区静态总收益
                BigDecimal   oneLevelIncome=new BigDecimal(0);

                //团队总业绩
                BigDecimal   teamRecord=new BigDecimal(0);
                BigDecimal teamCoinRecord=new BigDecimal(0);


                //直推节点个数
                int length=0;
                //仓位分界线
                BigDecimal balance=new BigDecimal(5000);
                BigDecimal balance2=new BigDecimal(1000);
                //社区有效用户数
                int userNum=0;
                TradePairVO tradePair=tradePairMap.get("MG");
                if(tradePair != null && tradePair.getLatestPrice() != null){
                    platPrice=tradePair.getLatestPrice();
                }
                List<BalanceUserCoinCountVolume>  childUserVolumeList= balanceUserCoinCountVolumeDao.findInvitesById(e.getUserId(),e.getCoinSymbol());
                if(CollectionUtils.isNotEmpty(childUserVolumeList)){
                    List<BalanceUserCoinCountVolume> allPlatUserList=new ArrayList<BalanceUserCoinCountVolume>();
                    allPlatUserList.addAll(childUserVolumeList);
                    treeCommunityUserList(childUserVolumeList,allPlatUserList);
                    userNum=allPlatUserList.size();
                    for(BalanceUserCoinCountVolume childUserVolume : allPlatUserList){
                        BigDecimal childRate=new BigDecimal(0);
                        if(childUserVolume.getCoinBalance().compareTo(balance)>0){
                            childRate=map.get("threeDayRate");
                        } else if (childUserVolume.getCoinBalance().compareTo(balance2)>0){
                            childRate=map.get("secondDayRate");
                        } else{
                            childRate=map.get("oneDayRate");
                        }
                        BigDecimal childCoinBalance=BigDecimal.ZERO;
                        if(platPrice.compareTo(BigDecimal.ZERO)>0) {
                            communityStaticsIncome = communityStaticsIncome.add(childUserVolume.getCoinBalance().divide(platPrice,16,BigDecimal.ROUND_HALF_UP ).multiply(childRate));
                            teamRecord = teamRecord.add(childUserVolume.getCoinBalance());
                        }
                    }
                    length=childUserVolumeList.size();
                    for (int i=0;i<length;i++) {
                        BalanceUserCoinCountVolume balanceTmp = childUserVolumeList.get(i);
                        if(balanceTmp != null){
                            BigDecimal childRateSec=new BigDecimal(0);
                            if(balanceTmp.getCoinBalance().compareTo(balance)>0){
                                childRateSec=map.get("threeDayRate");
                            }else if (balanceTmp.getCoinBalance().compareTo(balance2)>0){
                                childRateSec=map.get("secondDayRate");
                            } else{
                                childRateSec=map.get("oneDayRate");
                            }
                            if(platPrice.compareTo(BigDecimal.ZERO)>0) {
                                dynamicsIncomeTotal= dynamicsIncomeTotal.add(balanceTmp.getCoinBalance().divide(platPrice,16,BigDecimal.ROUND_HALF_UP ).multiply(childRateSec).multiply(new BigDecimal(0.5)));
                                oneLevelIncome=   oneLevelIncome.add(balanceTmp.getCoinBalance().divide(platPrice,16,BigDecimal.ROUND_HALF_UP ).multiply(childRateSec));
                            }

                        }
                        //动态收益2
                        if(length>=3){
                            //重写规则
                        }
                        if(length>5){
                            if( i<length-5){
                                if(dayRate.compareTo(new BigDecimal(0.0025))<0) {
                                    dayRate= dayRate.add(new BigDecimal(0.0005));
                                }

                            }
                        }
                    }


                }

                //静态收益
                //收益率通过配置计算
                if(platPrice.compareTo(BigDecimal.ZERO)>0) {
                    if(e.getCoinBalance().compareTo(balance)>0){
                        staticsIncomeTotal=staticsIncomeTotal.add(e.getCoinBalance().divide(platPrice,16,BigDecimal.ROUND_HALF_UP ).multiply(map.get("threeDayRate")));
                    }else if (e.getCoinBalance().compareTo(balance2)>0){
                        staticsIncomeTotal=staticsIncomeTotal.add(e.getCoinBalance().divide(platPrice,16,BigDecimal.ROUND_HALF_UP ).multiply(map.get("secondDayRate")));
                    }else{
                        staticsIncomeTotal=staticsIncomeTotal.add(e.getCoinBalance().divide(platPrice,16,BigDecimal.ROUND_HALF_UP ).multiply(map.get("oneDayRate")));
                    }
                }


                List<BalanceChangeUserCoinVolume> balanceChangeList=balanceChangeUserCoinVolumeDao.findChangeByUserId(e.getUserId());
                if (CollectionUtils.isNotEmpty(balanceChangeList)) {
                    for(BalanceChangeUserCoinVolume balanceChangeVolume : balanceChangeList ){
                        BigDecimal changeIncome=new BigDecimal(0);
                        if(platPrice.compareTo(BigDecimal.ZERO)>0) {
                            TradePairVO tradeChangePair=tradePairMap.get(balanceChangeVolume.getCoinSymbol());
                           BigDecimal coinNum= balanceChangeVolume.getCoinNum();
                            if(tradeChangePair != null && tradeChangePair.getLatestPrice() != null){
                                coinNum=coinNum.multiply(tradePair.getLatestPrice());
                            }
                            if(e.getCoinBalance().compareTo(balance)>0){
                                changeIncome=changeIncome.add(coinNum.multiply(map.get("threeDayRate")));
                            }else if (e.getCoinBalance().compareTo(balance2)>0){
                                changeIncome=changeIncome.add(coinNum.multiply(map.get("secondDayRate")));
                            } else{
                                changeIncome=changeIncome.add(coinNum.multiply(map.get("oneDayRate")));
                            }
                        }

                       if(balanceChangeVolume.getAccumulIncome() !=null){
                           balanceChangeVolume.setAccumulIncome(balanceChangeVolume.getAccumulIncome().add(changeIncome));
                       }else {
                           balanceChangeVolume.setAccumulIncome(changeIncome);
                       }
                        balanceChangeUserCoinVolumeDao.updateById(balanceChangeVolume);
                    }
                }
                //动态收益1和3
//                dynamicsIncomeTotal=dynamicsIncomeTotal.add(e.getCoinBalance().multiply(dayRate));
                if(platPrice.compareTo(BigDecimal.ZERO)>0) {
                    staticsIncomeTotal=staticsIncomeTotal.add(e.getCoinBalance().multiply(dayRate));
                }
                BalanceUserCoinVolumeDetail balanceUserCoinVolumeDetail=new BalanceUserCoinVolumeDetail();
                String id = SnowFlake.createSnowFlake().nextIdString();
                balanceUserCoinVolumeDetail.setId(id);
                balanceUserCoinVolumeDetail.setCoinSymbol(e.getCoinSymbol());
                balanceUserCoinVolumeDetail.setUserId(e.getUserId());

                balanceUserCoinVolumeDetail.setStaticsIncome(staticsIncomeTotal);
                balanceUserCoinVolumeDetail.setDynamicsIncome(dynamicsIncomeTotal);
                balanceUserCoinVolumeDetail.setTeamRecord(teamRecord);
                balanceUserCoinVolumeDetail.setCommunityStaticsIncome(communityStaticsIncome);
                balanceUserCoinVolumeDetail.setNodeNumber(length);

                //社区有效用户数
                balanceUserCoinVolumeDetail.setValidNum(userNum);

                //收益明细总计
                balanceUserCoinVolumeDetail.setDetailIncome(new BigDecimal(0));

                //奖励算法
                balanceUserCoinVolumeDetail.setDetailReward(new BigDecimal(0));
                balanceUserCoinVolumeDetail.setCommunityManageReward(new BigDecimal(0));
                balanceUserCoinVolumeDetail.setEqualityReward(new BigDecimal(0));
                balanceUserCoinVolumeDetail.setLevelDifferenceReward(new BigDecimal(0));
                balanceUserCoinVolumeDetail.setSumRevenue(new BigDecimal(0));
                teamCoinRecord=teamRecord.add(e.getCoinBalance());
                balanceUserCoinVolumeDetail.setTeamCoinRecord(teamCoinRecord);
                balanceUserCoinVolumeDetail.setOneLevelIncome(oneLevelIncome);
                balanceUserCoinVolumeDetail.setReferId(e.getReferId());
                //收益日期精确到天
                balanceUserCoinVolumeDetail.setIncomeDate(LocalDateTime.now());
                balanceUserCoinVolumeDetail.setCreateDate(LocalDateTime.now());
                balanceUserCoinVolumeDetail.setUpdateDate(LocalDateTime.now());
                balanceUserCoinVolumeDetail.setVersion(1);
                balanceUserCoinVolumeDetailDao.insert(balanceUserCoinVolumeDetail);
            });

        }

    }


    /**
     * 计算小区业绩、等级
     */
    public void communityRecordAndLevel(){
        List<BalanceUserCoinVolumeDetail>  balanceVolumeDetailList=balanceUserCoinVolumeDetailDao.findAll();
        if (CollectionUtils.isNotEmpty(balanceVolumeDetailList)) {
            balanceVolumeDetailList.forEach(e ->{
                BigDecimal maxCommunityRecord=balanceUserCoinVolumeDetailDao.findByReferId(e.getUserId(),e.getCoinSymbol());
                if(maxCommunityRecord != null){
                    e.setTeamCommunityRecord(e.getTeamRecord().subtract(maxCommunityRecord));
                }
                //社区等级
                int teamLevel=0;
                //直推节点
                int length=e.getNodeNumber();

                BigDecimal teamRecord=e.getTeamRecord();
                BigDecimal  teamCommunityRecord=e.getTeamCommunityRecord();
                if(teamRecord != null){
                    if(length>=10 && teamCommunityRecord !=null && teamCommunityRecord.compareTo(new BigDecimal(10000000))>=0 ){
                        teamLevel=5;
                    }else if(length>=10 && teamCommunityRecord !=null && teamCommunityRecord.compareTo(new BigDecimal(5000000))>=0 && teamCommunityRecord.compareTo(new BigDecimal(10000000))<0 ){
                        teamLevel=4;
                    }else if(length>=10 && teamCommunityRecord !=null && teamCommunityRecord.compareTo(new BigDecimal(1000000))>=0 && teamCommunityRecord.compareTo(new BigDecimal(5000000))<0){
                        teamLevel=3;
                    }else if(length>=10 && teamCommunityRecord !=null && teamCommunityRecord.compareTo(new BigDecimal(300000))>=0 && teamCommunityRecord.compareTo(new BigDecimal(1000000))<0){
                        teamLevel=2;
                    }else if(length>=5  && teamRecord.compareTo(new BigDecimal(100000))>=0){
                        teamLevel=1;
                    }
                }
                e.setTeamLevel(teamLevel);
                balanceUserCoinVolumeDetailDao.updateById(e);
            });

        }
    }
    /**
     * 计算社区管理奖
     */
    public void  calcManagementAward(Map<String ,BigDecimal> map,Map<String,TradePairVO>  tradePairMap){
        List<BalanceUserCoinVolumeDetail>  balanceVolumeDetailList=balanceUserCoinVolumeDetailDao.findAll();
        //全网络静态收益加权平均分红
       Map<String,BigDecimal>  goalStaticsIncomeMap=new HashMap<String,BigDecimal> ();
        List<Coin> coinList = coinDao.findAll();
        TradePairVO tradePair=tradePairMap.get("MG");
        BigDecimal platPrice=BigDecimal.ZERO;
        if(tradePair != null && tradePair.getLatestPrice() != null){
            platPrice=tradePair.getLatestPrice();
        }
        if(CollectionUtils.isNotEmpty(coinList)){
             for(Coin coin:coinList){
                 List<BalanceUserCoinCountVolume>  allVolumeList= balanceUserCoinCountVolumeDao.findByCoin(coin.getName());
                 List<BalanceUserCoinVolumeDetail>  detailGoalist=balanceUserCoinVolumeDetailDao.findGlobalByCoin(coin.getName());
                 BigDecimal coinIncome=new BigDecimal(0);

                 //V5等级个数
                 int len=0;
                 if(CollectionUtils.isNotEmpty(allVolumeList)){
                     for(BalanceUserCoinCountVolume balanceUserCoinVolume : allVolumeList){
                         BigDecimal dayRate=new BigDecimal(0);
                         BigDecimal balance=new BigDecimal(5000);
                         BigDecimal balance2=new BigDecimal(1000);
                         if(balanceUserCoinVolume.getCoinBalance().compareTo(balance)>0){
                             dayRate=map.get("threeDayRate");
                         }else if (balanceUserCoinVolume.getCoinBalance().compareTo(balance2)>0){
                             dayRate=map.get("secondDayRate");
                         } else{
                             dayRate=map.get("oneDayRate");
                         }
                         if(platPrice.compareTo(BigDecimal.ZERO)>0){
                             coinIncome.add(balanceUserCoinVolume.getCoinBalance().divide(platPrice,16,BigDecimal.ROUND_HALF_UP ).multiply(dayRate));
                         }
                     }
                 }
                 if(CollectionUtils.isNotEmpty(detailGoalist)){
                     len=detailGoalist.size();
                 }
                 if(len != 0){
                     coinIncome=coinIncome.multiply(new BigDecimal(0.05)).divide(new BigDecimal(len));
                 }
                 goalStaticsIncomeMap.put(coin.getName(),coinIncome);
             }
        }

        List<BalanceUserCoinVolumeDetail>  balanceVolumeDetailList2=balanceUserCoinVolumeDetailDao.findAll();
        List<BalanceUserCoinVolumeDetail>  balanceVolumeDetailList3=new ArrayList<BalanceUserCoinVolumeDetail>();
        if(CollectionUtils.isNotEmpty(balanceVolumeDetailList2)) {
            balanceVolumeDetailList2.forEach(balanceDetail2->{
                List<BalanceUserCoinVolumeDetail> childDetailList2= balanceUserCoinVolumeDetailDao.findAllByReferId(balanceDetail2.getUserId(),balanceDetail2.getCoinSymbol());
                boolean flag =treeCommunityUserDetailList(childDetailList2);
                if(!flag && balanceDetail2.getTeamLevel()>0){
                    balanceVolumeDetailList3.add(balanceDetail2);
                }
                BigDecimal communityManageReward=balanceDetail2.getCommunityManageReward();
                if(balanceDetail2.getTeamLevel()==5){
                    BigDecimal goalBonus=goalStaticsIncomeMap.get(balanceDetail2.getCoinSymbol());
                    communityManageReward.add(goalBonus);
                    balanceDetail2.setCommunityManageReward(communityManageReward);
                    balanceUserCoinVolumeDetailDao.updateById(balanceDetail2);
                }

            });
        }
        treeCalcUserDetailRewardList(balanceVolumeDetailList3,map,true);

//        if (CollectionUtils.isNotEmpty(balanceVolumeDetailList)) {
//
//            balanceVolumeDetailList.forEach(e ->{
//                //整个社区静态收益
//                BigDecimal  communityStaticsIncome= e.getCommunityStaticsIncome();
//
//                //实际社区静态收益
//                BigDecimal  realityStaticsIncome= e.getCommunityStaticsIncome();
//
//                //社区管理奖
//                BigDecimal  communityManageReward=new BigDecimal(0);
//                List<BalanceUserCoinVolumeDetail> childDetailList=balanceUserCoinVolumeDetailDao.findAllByReferId(e.getUserId(),e.getCoinSymbol());
//                if (CollectionUtils.isNotEmpty(childDetailList)) {
//                    for(BalanceUserCoinVolumeDetail childDetail : childDetailList){
//                        if(e.getTeamLevel()>0 && e.getTeamLevel()<=childDetail.getTeamLevel()){
//                            //级差制算法 上级社区级别低于等于下级
//                            realityStaticsIncome=realityStaticsIncome.subtract(childDetail.getCommunityStaticsIncome());
//                        }
//                    }
//
//                }
//
//                 int teamLevel=e.getTeamLevel();
//
//                    if(teamLevel==5){
//                        communityManageReward=  communityManageReward.add(realityStaticsIncome.multiply(new BigDecimal(0.20)));
//
//                        //加静态收益总和的平均分红
//                        BigDecimal goalBonus=goalStaticsIncomeMap.get(e.getCoinSymbol());
//                        communityManageReward.add(goalBonus);
//
//                    }else if(teamLevel==4 ){
//                        communityManageReward=  communityManageReward.add(realityStaticsIncome.multiply(new BigDecimal(0.20)));
//                    }else if(teamLevel==3){
//                        communityManageReward= communityManageReward.add(realityStaticsIncome.multiply(new BigDecimal(0.15)));
//                    }else if(teamLevel==2){
//                        communityManageReward= communityManageReward.add(realityStaticsIncome.multiply(new BigDecimal(0.1)));
//                    }else if(teamLevel==1){
//                        communityManageReward=communityManageReward.add(realityStaticsIncome.multiply(new BigDecimal(0.05)));
//                    }
//                  e.setRealityStaticsIncome(realityStaticsIncome);
//                 e.setCommunitySumManageReward(communityManageReward);
//                balanceUserCoinVolumeDetailDao.updateById(e);
//            });
//
//        }
//          //级差制  上级社区等级大于下级
//        List<BalanceUserCoinVolumeDetail>  balanceDetailList=balanceUserCoinVolumeDetailDao.findAll();
//        if (CollectionUtils.isNotEmpty(balanceDetailList)) {
//
//            balanceDetailList.forEach(e ->{
//                //整个社区静态收益
//                BigDecimal  communityStaticsIncome= e.getCommunityStaticsIncome();
//
//                //实际社区静态收益
//                BigDecimal  realityStaticsIncome= e.getCommunityStaticsIncome();
//
//                //社区管理奖
//                BigDecimal  communityManageReward=e.getCommunitySumManageReward();
//                int teamLevel=e.getTeamLevel();
//
//                BigDecimal diffManageReward=BigDecimal.ZERO;
//                List<BalanceUserCoinVolumeDetail> childDetailList=balanceUserCoinVolumeDetailDao.findAllByReferId(e.getUserId(),e.getCoinSymbol());
//                if (CollectionUtils.isNotEmpty(childDetailList)) {
//                    for(BalanceUserCoinVolumeDetail childDetail : childDetailList){
//                        if(e.getTeamLevel()>1 && e.getTeamLevel()>childDetail.getTeamLevel()){
//                            BigDecimal realityChildIncome=childDetail.getRealityStaticsIncome();
//                            if(teamLevel==5){
//                                if(childDetail.getTeamLevel()==4 ){
//                                    diffManageReward=BigDecimal.ZERO ;
//                                }else if(childDetail.getTeamLevel()==3){
//                                    diffManageReward= diffManageReward.add(realityChildIncome.multiply(new BigDecimal(0.05)));
//                                }else if(childDetail.getTeamLevel()==2){
//                                    diffManageReward= diffManageReward.add(realityChildIncome.multiply(new BigDecimal(0.1)));
//                                }else if(childDetail.getTeamLevel()==1){
//                                    diffManageReward=diffManageReward.add(realityChildIncome.multiply(new BigDecimal(0.15)));
//                                }
//                            }
//                            if(teamLevel==4){
//                                 if(childDetail.getTeamLevel()==3){
//                                    diffManageReward= diffManageReward.add(realityChildIncome.multiply(new BigDecimal(0.05)));
//                                }else if(childDetail.getTeamLevel()==2){
//                                    diffManageReward= diffManageReward.add(realityChildIncome.multiply(new BigDecimal(0.1)));
//                                }else if(childDetail.getTeamLevel()==1){
//                                    diffManageReward=diffManageReward.add(realityStaticsIncome.multiply(new BigDecimal(0.15)));
//                                }
//                            }
//                            if(teamLevel==3){
//                                 if(childDetail.getTeamLevel()==2){
//                                    diffManageReward= diffManageReward.add(realityChildIncome.multiply(new BigDecimal(0.05)));
//                                }else if(childDetail.getTeamLevel()==1){
//                                    diffManageReward=diffManageReward.add(realityStaticsIncome.multiply(new BigDecimal(0.1)));
//                                }
//                            }
//                            if(teamLevel==2){
//                                if(childDetail.getTeamLevel()==1){
//                                    diffManageReward=diffManageReward.add(realityChildIncome.multiply(new BigDecimal(0.05)));
//                                }
//                            }
//
//                            //级差制算法 上级社区级别高于下级
//                            BigDecimal childCommunityManageReward=childDetail.getCommunitySumManageReward();
//                            if(childCommunityManageReward != null){
//                                communityManageReward= communityManageReward.subtract(childCommunityManageReward);
//                            }
//                        }
//                    }
//
//                }
//
//                e.setCommunityManageReward(communityManageReward.subtract(diffManageReward));
//                e.setLevelDifferenceReward(diffManageReward);
//                balanceUserCoinVolumeDetailDao.updateById(e);
//            });

//        }
    }

    /**
     *
     * @param userList
     * @return
     */
    public  void treeCalcUserDetailRewardList(List<BalanceUserCoinVolumeDetail> userList,Map<String ,BigDecimal> map,boolean flag) {
        if (CollectionUtils.isNotEmpty(userList)) {
            List<BalanceUserCoinVolumeDetail> fatherDetailAllList=new ArrayList<BalanceUserCoinVolumeDetail>();
            userList.forEach(balanceDetail3->{
                //本-社区管理奖
                BigDecimal benMangReward=BigDecimal.ZERO;
                BigDecimal benChildIncome=balanceDetail3.getOneLevelIncome();
                if(flag){
                    benChildIncome=balanceDetail3.getCommunityStaticsIncome();
                }
                int  benTeamLevel=balanceDetail3.getTeamLevel();
                if(benTeamLevel==5){
                    benMangReward=  benMangReward.add(benChildIncome.multiply(new BigDecimal(0.30)));
                }else if(benTeamLevel==4 ){
                    benMangReward=  benMangReward.add(benChildIncome.multiply(new BigDecimal(0.25)));
                }else if(benTeamLevel==3){
                    benMangReward= benMangReward.add(benChildIncome.multiply(new BigDecimal(0.20)));
                }else if(benTeamLevel==2){
                    benMangReward= benMangReward.add(benChildIncome.multiply(new BigDecimal(0.15)));
                }else if(benTeamLevel==1){
                    benMangReward=benMangReward.add(benChildIncome.multiply(new BigDecimal(0.1)));
                }
                balanceDetail3.setCommunityManageReward(benMangReward);
                balanceUserCoinVolumeDetailDao.updateById(balanceDetail3);
                if(benTeamLevel!=0){
                    calcFatherUserList(balanceDetail3,balanceDetail3,map,flag,true,0);
                }
                List<BalanceUserCoinVolumeDetail> fatherDetailList3= balanceUserCoinVolumeDetailDao.findByUserIdAndCoin(balanceDetail3.getReferId(),balanceDetail3.getCoinSymbol());
                if(benTeamLevel==0){
                    if (CollectionUtils.isNotEmpty(fatherDetailList3)) {
                        fatherDetailList3.forEach(e->{
                            BigDecimal benChildIncome2=balanceDetail3.getOneLevelIncome();
                            if(flag){
                                benChildIncome2=balanceDetail3.getCommunityStaticsIncome();
                            }
                            e.setOneLevelIncome(e.getOneLevelIncome().add(benChildIncome2));
                        });
                    }
                }
                if (CollectionUtils.isNotEmpty(fatherDetailList3)) {
                    fatherDetailAllList.addAll(fatherDetailList3);
                }
            });
            treeCalcUserDetailRewardList(fatherDetailAllList,map,false);
        }
    }

    /**
     *
     * @param userList
     * @param sumIncome
     */
    public  void  calcFatherUserList(BalanceUserCoinVolumeDetail userDetail,BalanceUserCoinVolumeDetail userReferDetail,Map<String ,BigDecimal> map,boolean flag,boolean equalFlag,int superTeamLevel) {
        List<BalanceUserCoinVolumeDetail> fatherDetailList3= balanceUserCoinVolumeDetailDao.findByUserIdAndCoin(userReferDetail.getReferId(),userReferDetail.getCoinSymbol());
        int benTeamLevel=userDetail.getTeamLevel();
        BalanceUserCoinVolumeDetail fatherDetail3=null;
        BigDecimal realityChildIncome=userDetail.getOneLevelIncome();
        if(flag){
            realityChildIncome=userDetail.getCommunityStaticsIncome();
        }

        if (CollectionUtils.isNotEmpty(fatherDetailList3)) {
            fatherDetail3=fatherDetailList3.get(0);
            int fatherTeamLevel=fatherDetail3.getTeamLevel();
            BigDecimal fatherDiffReward=BigDecimal.ZERO;
            BigDecimal fatherEqualReward=BigDecimal.ZERO;
            if(fatherDetail3.getLevelDifferenceReward() != null){
                fatherDiffReward=fatherDetail3.getLevelDifferenceReward();
            }
            if(fatherDetail3.getEqualityReward() != null){
                fatherEqualReward=fatherDetail3.getEqualityReward();
            }
            if(superTeamLevel != fatherTeamLevel) {

                if (fatherTeamLevel == benTeamLevel) {
                    if (equalFlag) {
                        //平级奖
                        BigDecimal userIncome=userDetail.getDynamicsIncome().add(userDetail.getStaticsIncome()).add(userDetail.getLevelDifferenceReward()).add(userDetail.getCommunityManageReward());
                        fatherEqualReward = fatherEqualReward.add(userIncome.multiply(map.get("equalReward")));
                    }
                    equalFlag = false;
                } else if (fatherTeamLevel < benTeamLevel) {

                } else {
                    if (fatherTeamLevel == 5) {
                        if (benTeamLevel == 4) {
                            fatherDiffReward = fatherDiffReward.add(realityChildIncome.multiply(new BigDecimal(0.05)));
                        } else if (benTeamLevel == 3) {
                            fatherDiffReward = fatherDiffReward.add(realityChildIncome.multiply(new BigDecimal(0.1)));
                        } else if (benTeamLevel == 2) {
                            fatherDiffReward = fatherDiffReward.add(realityChildIncome.multiply(new BigDecimal(0.15)));
                        } else if (benTeamLevel == 1) {
                            fatherDiffReward = fatherDiffReward.add(realityChildIncome.multiply(new BigDecimal(0.20)));
                        }
                    }
                    if (fatherTeamLevel == 4) {
                        if (benTeamLevel == 3) {
                            fatherDiffReward = fatherDiffReward.add(realityChildIncome.multiply(new BigDecimal(0.05)));
                        } else if (benTeamLevel == 2) {
                            fatherDiffReward = fatherDiffReward.add(realityChildIncome.multiply(new BigDecimal(0.1)));
                        } else if (benTeamLevel == 1) {
                            fatherDiffReward = fatherDiffReward.add(realityChildIncome.multiply(new BigDecimal(0.15)));
                        }
                    }
                    if (fatherTeamLevel == 3) {
                        if (benTeamLevel == 2) {
                            fatherDiffReward = fatherDiffReward.add(realityChildIncome.multiply(new BigDecimal(0.05)));
                        } else if (benTeamLevel == 1) {
                            fatherDiffReward = fatherDiffReward.add(realityChildIncome.multiply(new BigDecimal(0.1)));
                        }
                    }
                    if (fatherTeamLevel == 2) {
                        if (benTeamLevel == 1) {
                            fatherDiffReward = fatherDiffReward.add(realityChildIncome.multiply(new BigDecimal(0.05)));
                        }
                    }
                }
                fatherDetail3.setEqualityReward(fatherEqualReward);
                fatherDetail3.setLevelDifferenceReward(fatherDiffReward);
                balanceUserCoinVolumeDetailDao.updateById(fatherDetail3);
            }
            calcFatherUserList(userDetail,fatherDetail3,map,false,equalFlag,fatherDetail3.getTeamLevel());
        }


    }
    /**
     * 动态收益2和平级奖
     */
    public void  calcIncomeAndEqualAward(Map<String ,BigDecimal> map){
        //动态收益2，从最顶层计算，依次类推
        List<BalanceUserCoinVolumeDetail>  suprerDetailList= balanceUserCoinVolumeDetailDao.findSuprer();
        if (CollectionUtils.isNotEmpty(suprerDetailList)) {
            suprerDetailList.forEach(e->{
                e.setDetailIncome(e.getStaticsIncome().add(e.getDynamicsIncome()));
                e.setDetailReward(e.getCommunityManageReward().add(e.getEqualityReward()).add(e.getLevelDifferenceReward()));
                e.setSumRevenue(e.getDetailIncome().add(e.getDetailReward()));
                balanceUserCoinVolumeDetailDao.updateById(e);
                BigDecimal sumIncome=e.getDetailIncome().add(e.getDetailReward());
                List<BalanceUserCoinVolumeDetail>  childDetailList = balanceUserCoinVolumeDetailDao.findAllByReferId(e.getUserId(),e.getCoinSymbol());
                if (CollectionUtils.isNotEmpty(childDetailList)) {
                    treeSuperUserList(childDetailList,sumIncome);
                }
            });
        }
        //平级奖
//        List<BalanceUserCoinVolumeDetail> balanceDetailList=  balanceUserCoinVolumeDetailDao.findAll();
//        if (CollectionUtils.isNotEmpty(balanceDetailList)) {
//            balanceDetailList.forEach(balanceDetail -> {
//                List<BalanceUserCoinVolumeDetail>  childBalanceDetailList = balanceUserCoinVolumeDetailDao.findAllByReferId(balanceDetail.getUserId(),balanceDetail.getCoinSymbol());
//                List<BalanceUserCoinVolumeDetail>  childEqualUserList= treeChildEqualUserList(childBalanceDetailList,balanceDetail.getTeamLevel());
//                BigDecimal   equalityReward=new BigDecimal(0);
//                if(CollectionUtils.isNotEmpty(childEqualUserList)){
//                    for(BalanceUserCoinVolumeDetail childEqualUser: childEqualUserList){
//                        equalityReward=equalityReward.add(childEqualUser.getDetailReward().multiply(map.get("equalReward")));
//                    }
//                }
//                balanceDetail.setEqualityReward(equalityReward);
//                balanceDetail.setSumRevenue(equalityReward.add(balanceDetail.getDetailIncome()).add(balanceDetail.getCommunityManageReward()).add(balanceDetail.getLevelDifferenceReward()));
//                balanceDetail.setDetailReward(balanceDetail.getCommunityManageReward().add(equalityReward).add(balanceDetail.getLevelDifferenceReward()));
//                balanceUserCoinVolumeDetailDao.updateById(balanceDetail);
//            });
//        }
    }

    /**
     * 计算动态收益的上级收益加权平均分红
     * @param userList
     * @param sumIncome
     */
    public  void treeSuperUserList(List<BalanceUserCoinVolumeDetail> userList,BigDecimal sumIncome) {
        int length=0;
        List<BalanceUserCoinVolumeDetail> userList2=new ArrayList<BalanceUserCoinVolumeDetail>();
        for (BalanceUserCoinVolumeDetail user : userList) {
            if(user.getValidNum()>=3){
                length++;
                userList2.add(user);
            }
        }
        for (BalanceUserCoinVolumeDetail user : userList) {
            if(length>0 && user.getValidNum()>=3){
                user.setDynamicsIncome(user.getDynamicsIncome().add(sumIncome.multiply(new BigDecimal(0.15)).divide(new BigDecimal(length),16,BigDecimal.ROUND_HALF_UP )));
            }
            user.setDetailIncome(user.getStaticsIncome().add(user.getDynamicsIncome()));
            user.setDetailReward(user.getCommunityManageReward().add(user.getEqualityReward()).add(user.getLevelDifferenceReward()));
            user.setSumRevenue(user.getDetailIncome().add(user.getDetailReward()));
            balanceUserCoinVolumeDetailDao.updateById(user);
            List<BalanceUserCoinVolumeDetail> platList= balanceUserCoinVolumeDetailDao.findAllByReferId(user.getUserId(),user.getCoinSymbol());
            //遍历出父id等于参数的id，add进子节点集合
            if (CollectionUtils.isNotEmpty(platList)) {
                //递归遍历下一级
                //allUserList.addAll(platList);
                treeSuperUserList(platList,user.getDetailIncome().add(user.getCommunityManageReward()));
            }
        }

    }

    /**
     * 平级奖查找对应节点
     * @param userList
     * @param level
     * @return
     */
    public  List<BalanceUserCoinVolumeDetail>  treeChildEqualUserList(List<BalanceUserCoinVolumeDetail> userList,int level) {
        List<BalanceUserCoinVolumeDetail> equalList=new ArrayList<BalanceUserCoinVolumeDetail>();
        for (BalanceUserCoinVolumeDetail user : userList) {
            if(user.getTeamLevel()==level){
                equalList.add(user);
            }
        }
        if(equalList.size()>0){
            return equalList;
        }
        List<BalanceUserCoinVolumeDetail> childList=new ArrayList<BalanceUserCoinVolumeDetail>();
        for (BalanceUserCoinVolumeDetail user : userList) {

            List<BalanceUserCoinVolumeDetail> platList= balanceUserCoinVolumeDetailDao.findAllByReferId(user.getUserId(),user.getCoinSymbol());
            //遍历出父id等于参数的id，add进子节点集合
            if (CollectionUtils.isNotEmpty(platList)) {
                //递归遍历下一级
                //allUserList.addAll(platList);
                childList.addAll(platList);
            }
        }
        if(childList.size()==0){
            return childList;
        }
        treeChildEqualUserList(childList,level);
       return equalList;
    }

/***
 *
 */
  @Override
  public void  balanceJackpotIncomeCount(){

      List<BalanceUserCoinCountVolume> countVolumeList=balanceUserCoinCountVolumeDao.findByTopJackpot();
      List<BalancePlatJackpotVolumeDetail> jackpotVolumeList=balancePlatJackpotVolumeDetailDao.findByUserIdAndCoin("MG")  ;
      if(CollectionUtils.isNotEmpty(jackpotVolumeList)){
          BalancePlatJackpotVolumeDetail jackpotVolume=jackpotVolumeList.get(0);
          BigDecimal allIncome=jackpotVolume.getAllCoinIncome();
          BigDecimal subIncome=BigDecimal.ZERO;
          if(allIncome != null && allIncome.compareTo(BigDecimal.ZERO)>0){
              if(CollectionUtils.isNotEmpty(countVolumeList)){
                 int len=countVolumeList.size();
                  for(int i=0;i<len;i++){
                      BalanceUserCoinCountVolume countVolume=countVolumeList.get(i);
                      BigDecimal avgIncome=BigDecimal.ZERO;
                      if(i==0){
                          avgIncome=allIncome.multiply(new BigDecimal(0.1));
                          subIncome=subIncome.add(avgIncome);
                      }else if(i==1){
                          avgIncome=allIncome.multiply(new BigDecimal(0.06));
                          subIncome=subIncome.add(avgIncome);
                      }else if(i==2){
                          avgIncome=allIncome.multiply(new BigDecimal(0.04));
                          subIncome=subIncome.add(avgIncome);
                      }else if(i==3){
                          avgIncome=allIncome.multiply(new BigDecimal(0.02));
                          subIncome=subIncome.add(avgIncome);
                      }else{
                          avgIncome=allIncome.multiply(new BigDecimal(0.005));
                          subIncome=subIncome.add(avgIncome);
                      }
                      userCoinVolumeExService.updateIncome(null,avgIncome,countVolume.getUserId(),jackpotVolume.getCoinSymbol(),false);
                  }

              }
          }
          jackpotVolume.setAllCoinIncome(allIncome.subtract(subIncome));
          LocalDateTime localTime=LocalDateTime.now();
          jackpotVolume.setRewardDate(localTime.plusDays(10));
          balancePlatJackpotVolumeDetailDao.updateById(jackpotVolume);
      }else{
          BalancePlatJackpotVolumeDetail jackpotVolumeDetail=new BalancePlatJackpotVolumeDetail();
          jackpotVolumeDetail.setAllCoinIncome(BigDecimal.ZERO);
          LocalDateTime localTime=LocalDateTime.now();
          jackpotVolumeDetail.setRewardDate(localTime.plusDays(10));
          String id = SnowFlake.createSnowFlake().nextIdString();
          jackpotVolumeDetail.setId(id);
          balancePlatJackpotVolumeDetailDao.insert(jackpotVolumeDetail);
      }



  }

}
