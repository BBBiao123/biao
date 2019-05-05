package com.biao.service.impl.balance;

import com.biao.entity.PlatUser;
import com.biao.entity.balance.BalanceUserCoinVolume;
import com.biao.entity.balance.BalanceUserCoinVolumeDetail;
import com.biao.mapper.PlatUserDao;
import com.biao.mapper.balance.BalanceUserCoinVolumeDao;
import com.biao.mapper.balance.BalanceUserCoinVolumeDetailDao;
import com.biao.service.balance.BalanceUserCoinVolumeDetailService;
import com.biao.service.balance.BalanceUserCoinVolumeService;
import com.biao.util.SnowFlake;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *余币宝统计和明细service
 */
@Service
public class BalanceUserCoinVolumeDetailServiceImpl implements BalanceUserCoinVolumeDetailService {

    @Autowired(required = false)
    private BalanceUserCoinVolumeDetailDao balanceUserCoinVolumeDetailDao;

    @Autowired(required = false)
    private BalanceUserCoinVolumeDao balanceUserCoinVolumeDao;

    @Autowired(required = false)
    private PlatUserDao platUserDao;

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
        List<BalanceUserCoinVolume>  balanceUserCoinVolumeList= balanceUserCoinVolumeDao.findAll();
        if (CollectionUtils.isNotEmpty(balanceUserCoinVolumeList)) {
            balanceUserCoinVolumeList.forEach(e -> {
                List<BalanceUserCoinVolumeDetail> balanceDetailList=balanceUserCoinVolumeDetailDao.findByUserIdAndCoin(e.getUserId(),e.getCoinSymbol());
                if (CollectionUtils.isNotEmpty(balanceUserCoinVolumeList)) {
                    balanceDetailList.forEach(balanceDetail -> {
                        e.setCoinBalance(e.getCoinBalance().add(balanceDetail.getDetailIncome()));
                        e.setYesterdayIncome(balanceDetail.getDetailIncome());
                        e.setYesterdayReward(balanceDetail.getDetailReward());
                        if(e.getAccumulIncome() !=null){
                            e.setAccumulIncome(e.getAccumulIncome().add(balanceDetail.getDetailIncome()));
                        }else{
                            e.setAccumulIncome(balanceDetail.getDetailIncome());
                        }
                        if(e.getAccumulReward() !=null){
                            e.setAccumulIncome(e.getAccumulIncome().add(balanceDetail.getDetailIncome()));
                        }else{
                            e.setAccumulIncome(balanceDetail.getDetailReward());
                        }
                        balanceDetail.setVersion(0);
                        balanceUserCoinVolumeDao.updateById(e);
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
    public void balanceIncomeDetailNew(){

        List<BalanceUserCoinVolume>  balanceUserCoinVolumeList= balanceUserCoinVolumeDao.findAll();
        if (CollectionUtils.isNotEmpty(balanceUserCoinVolumeList)) {
            balanceUserCoinVolumeList.forEach(e->{
                //用户静态收益计算 begin
                BigDecimal   staticsIncomeTotal=new BigDecimal(0);
                //用户静态收益计算 end
                //动态收益 1 和3
                BigDecimal   dynamicsIncomeTotal=new BigDecimal(0);
                //动态收益3的静态收益增长率
                BigDecimal   dayRate=new BigDecimal(0);

                //社区静态收益
                BigDecimal   communityStaticsIncome=new BigDecimal(0);
                //团队业绩
                BigDecimal   teamRecord=new BigDecimal(0);

                int teamLevel=0;

                List<BalanceUserCoinVolume>  childUserVolumeList= balanceUserCoinVolumeDao.findInvitesById(e.getUserId(),e.getCoinSymbol());
                if(CollectionUtils.isNotEmpty(childUserVolumeList)){
                    List<BalanceUserCoinVolume> allPlatUserList=new ArrayList<BalanceUserCoinVolume>();
                    allPlatUserList.addAll(childUserVolumeList);
                    treeCommunityUserList(childUserVolumeList,allPlatUserList);
                    //团队业绩和社区静态收益
                    Map<String,BigDecimal> detailRewardMap=new HashMap<String,BigDecimal>();
                    for(BalanceUserCoinVolume childUserVolume:allPlatUserList){
                        communityStaticsIncome=communityStaticsIncome.add(childUserVolume.getCoinBalance().multiply(childUserVolume.getDayRate()));
                        teamRecord=teamRecord.add(childUserVolume.getCoinBalance());
                    }

                  int  length=childUserVolumeList.size();
                    for (int i=0;i<length;i++) {

                        BalanceUserCoinVolume balanceTmp = childUserVolumeList.get(i);

                        if(balanceTmp != null){
                            dynamicsIncomeTotal= dynamicsIncomeTotal.add(balanceTmp.getCoinBalance().multiply(balanceTmp.getDayRate()).divide(new BigDecimal(2)));
                        }
                        //动态收益2
                        if(length>=3){

                           //重写规则
                        }
                        if(length>5){
                            if( i<=length-5){
                                if(dayRate.compareTo(new BigDecimal(0.005))<0) {
                                    dayRate= dayRate.add(new BigDecimal(0.001));
                                }

                            }
                        }
                    }

                    if(teamRecord != null){
                        if(length>=10 && teamRecord.compareTo(new BigDecimal(10000000))>=0 ){
                            teamLevel=5;
                        }else if(length>=10 && teamRecord.compareTo(new BigDecimal(5000000))>=0 ){
                            teamLevel=4;
                        }else if(length>=10 && teamRecord.compareTo(new BigDecimal(2000000))>=0 && teamRecord.compareTo(new BigDecimal(5000000))<0){
                            teamLevel=3;
                        }else if(length>=10 && teamRecord.compareTo(new BigDecimal(500000))>=0 && teamRecord.compareTo(new BigDecimal(2000000))<0){
                            teamLevel=2;
                        }else if(length>=5  && teamRecord.compareTo(new BigDecimal(100000))>=0){
                            teamLevel=1;
                        }
                    }

                }

                //静态收益
                staticsIncomeTotal=staticsIncomeTotal.add(e.getCoinBalance().multiply(e.getDayRate()));
                //动态收益1和3
                dynamicsIncomeTotal=dynamicsIncomeTotal.add(e.getCoinBalance().multiply(dayRate));
                BalanceUserCoinVolumeDetail balanceUserCoinVolumeDetail=new BalanceUserCoinVolumeDetail();
                String id = SnowFlake.createSnowFlake().nextIdString();
                balanceUserCoinVolumeDetail.setId(id);
                balanceUserCoinVolumeDetail.setCoinSymbol(e.getCoinSymbol());
                balanceUserCoinVolumeDetail.setUserId(e.getUserId());

                balanceUserCoinVolumeDetail.setStaticsIncome(staticsIncomeTotal);
                //收益明细总计
                balanceUserCoinVolumeDetail.setDetailIncome(new BigDecimal(0));

                //奖励算法
                balanceUserCoinVolumeDetail.setDetailReward(new BigDecimal(0));
                //收益日期精确到天
                balanceUserCoinVolumeDetail.setIncomeDate(LocalDateTime.now().minusHours(1));
                balanceUserCoinVolumeDetail.setCreateDate(LocalDateTime.now());
                balanceUserCoinVolumeDetail.setUpdateDate(LocalDateTime.now());
                balanceUserCoinVolumeDetail.setVersion(1);
                balanceUserCoinVolumeDetailDao.insert(balanceUserCoinVolumeDetail);
            });

        }

    }
    public  void treeCommunityUserList(List<BalanceUserCoinVolume> userList,List<BalanceUserCoinVolume> allUserList) {
        for (BalanceUserCoinVolume user : userList) {

            List<BalanceUserCoinVolume> platList= balanceUserCoinVolumeDao.findInvitesById(user.getUserId(),user.getCoinSymbol());
            //遍历出父id等于参数的id，add进子节点集合
            if (CollectionUtils.isNotEmpty(platList)) {
                //递归遍历下一级
                allUserList.addAll(platList);
                treeCommunityUserList(platList,allUserList);
            }
        }

    }
}
