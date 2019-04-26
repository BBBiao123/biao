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
import java.util.List;

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
                //动态收益计算
                BigDecimal   detailIncomeTotal=e.getCoinBalance().multiply(e.getDayRate());
                BigDecimal  dayRate=new BigDecimal(0);
                if(CollectionUtils.isNotEmpty(platUserList)){
                    int length=platUserList.size();
                   for (int i=0;i<length;i++) {
                       PlatUser platUser=platUserList.get(i);
                        List<BalanceUserCoinVolume> childList = balanceUserCoinVolumeDao.findByUserIdAndCoin(platUser.getId(),e.getCoinSymbol());
                        BalanceUserCoinVolume balanceTmp = null;
                        if (CollectionUtils.isNotEmpty(childList)) {
                            balanceTmp = childList.get(0);
                        }
                        if(balanceTmp != null){
                            detailIncomeTotal.add(balanceTmp.getCoinBalance().multiply(balanceTmp.getDayRate()).divide(new BigDecimal(2)));
                        }
                       if(length>=3){
                           if(balanceTmp != null){
                               detailIncomeTotal.add(balanceTmp.getCoinBalance().multiply(balanceTmp.getDayRate()).divide(new BigDecimal(2)));
                           }
                           PlatUser  platUserBen=  platUserDao.findById(e.getUserId());
                           if(platUserBen !=null && StringUtils.isNotEmpty( platUserBen.getReferId()) ){
                               List<BalanceUserCoinVolume> supList = balanceUserCoinVolumeDao.findByUserIdAndCoin(platUserBen.getReferId(),e.getCoinSymbol());
                               BalanceUserCoinVolume balanceSupTmp = null;
                               if (CollectionUtils.isNotEmpty(supList)) {
                                   balanceSupTmp = supList.get(0);
                               }
                               if(balanceSupTmp != null){
                                   detailIncomeTotal.add(balanceSupTmp.getCoinBalance().multiply(balanceSupTmp.getDayRate()).multiply(new BigDecimal(0.15)));
                               }
                           }
                       }

                       if(length>5){
                          if( i<=length-5){
                              if(dayRate.compareTo(new BigDecimal(0.005))<0) {
                                  dayRate.add(new BigDecimal(0.001));
                              }

                          }
                       }
                    };


                }
                BalanceUserCoinVolumeDetail balanceUserCoinVolumeDetail=new BalanceUserCoinVolumeDetail();
                String id = SnowFlake.createSnowFlake().nextIdString();
                balanceUserCoinVolumeDetail.setId(id);
                balanceUserCoinVolumeDetail.setCoinSymbol(e.getCoinSymbol());
                balanceUserCoinVolumeDetail.setUserId(e.getUserId());

                //收益明细总计
                balanceUserCoinVolumeDetail.setDetailIncome(detailIncomeTotal);

                //奖励算法
                balanceUserCoinVolumeDetail.setDetailReward(new BigDecimal(2));
                //收益日期精确到天
                balanceUserCoinVolumeDetail.setIncomeDate(LocalDateTime.now().minusHours(1));
                balanceUserCoinVolumeDetail.setCreateDate(LocalDateTime.now());
                balanceUserCoinVolumeDetail.setUpdateDate(LocalDateTime.now());
                balanceUserCoinVolumeDetail.setVersion(1);
                balanceUserCoinVolumeDetailDao.insert(balanceUserCoinVolumeDetail);
            });
        }
    }


}
