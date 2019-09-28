package com.biao.service.impl.balance;

import com.biao.constant.Constants;
import com.biao.entity.Coin;
import com.biao.entity.OfflineTransferLog;
import com.biao.entity.UserCoinVolume;
import com.biao.entity.balance.BalanceChangeUserCoinVolume;
import com.biao.entity.balance.BalancePlatJackpotVolumeDetail;
import com.biao.entity.balance.BalanceUserCoinVolume;
import com.biao.exception.PlatException;
import com.biao.mapper.CoinDao;
import com.biao.mapper.OfflineTransferLogDao;
import com.biao.mapper.balance.BalanceChangeUserCoinVolumeDao;
import com.biao.mapper.balance.BalancePlatJackpotVolumeDetailDao;
import com.biao.mapper.balance.BalanceUserCoinVolumeDao;
import com.biao.service.UserCoinVolumeExService;
import com.biao.service.balance.BalanceUserCoinVolumeService;
import com.biao.util.SnowFlake;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *余币宝对应service
 */
@Service
public class BalanceUserCoinVolumeServiceImpl implements BalanceUserCoinVolumeService {

    @Autowired(required = false)
    private BalanceUserCoinVolumeDao balanceUserCoinVolumeDao;
    @Autowired(required = false)
    private BalancePlatJackpotVolumeDetailDao balancePlatJackpotVolumeDetailDao;
    @Autowired
    private CoinDao coinDao;
    @Autowired(required = false)
    private BalanceChangeUserCoinVolumeDao balanceChangeUserCoinVolumeDao;

    @Autowired(required = false)
    private  OfflineTransferLogDao offlineTransferLogDao;

    @Autowired
    private UserCoinVolumeExService userCoinVolumeExService;

    private static volatile Map<String, Integer> cacheMap = new ConcurrentHashMap<>();

    @Override
    public String save(BalanceUserCoinVolume balanceUserCoinVolume) {
        String id = SnowFlake.createSnowFlake().nextIdString();

        balanceUserCoinVolume.setId(id);
        balanceUserCoinVolumeDao.insert(balanceUserCoinVolume);
        return id;
    }
    @Override
    public void updateById(BalanceUserCoinVolume balanceUserCoinVolume) {
        balanceUserCoinVolumeDao.updateById(balanceUserCoinVolume);
    }

    @Override
    public List<BalanceUserCoinVolume> findAll(String userId) {
        return balanceUserCoinVolumeDao.findByUserId(userId);
    }

    @Override
    public List<BalanceUserCoinVolume> findByRank() {
        return balanceUserCoinVolumeDao.findByRank();
    }

    @Override
    public List<BalanceUserCoinVolume>  findByUserIdAndCoin(String userId,String coinSymbol){
        return  balanceUserCoinVolumeDao.findByUserIdAndCoin(userId,coinSymbol);
    }

    @Override
    public int findByCountNum(){
        return balanceUserCoinVolumeDao.findByCountNum();
    }

    public  List<BalanceUserCoinVolume> findInvitesByUserId(String userId){
        return  balanceUserCoinVolumeDao.findInvitesByUserId(userId);
    }
    @Override
    public List<BalanceUserCoinVolume> findByAllRank() {
        return balanceUserCoinVolumeDao.findByAllRank();
    }

    @Override
    public long deleteByBalanceId(String changeId){
        return balanceUserCoinVolumeDao.deleteByBalanceId(changeId);
    }

    @Override
    @Transactional
    public void balanceVolume( BalanceUserCoinVolume balanceUserCoinVolume,BalanceChangeUserCoinVolume balanceChangeUserCoinVolume,BigDecimal rewardNum){
          String key="balanceVolume:"+balanceUserCoinVolume.getUserId();
          Integer vaildValue= cacheMap.get(key);
          System.out.println("------"+vaildValue);
          if(vaildValue ==null){
              vaildValue=0;
              cacheMap.put(key,0);
          }
        UserCoinVolume userVolume = userCoinVolumeExService.findByUserIdAndCoinSymbol(balanceUserCoinVolume.getUserId(), balanceUserCoinVolume.getCoinSymbol());
        BigDecimal userCoinIncome = userVolume.getVolume();
        BigDecimal userCoinIncome2 = userVolume.getVolume();
        userCoinIncome= userCoinIncome.setScale(6, BigDecimal.ROUND_HALF_UP);
        BigDecimal coinBalance=balanceUserCoinVolume.getCoinBalance();
        if(coinBalance.compareTo(userCoinIncome)==0){
            coinBalance=userCoinIncome2;
        }
        if (coinBalance.compareTo(userCoinIncome2) >0) {
            throw new PlatException(10099999, "资产不足...");
        }
        List<BalanceUserCoinVolume> flaglist = balanceUserCoinVolumeDao.findByUserId(balanceUserCoinVolume.getUserId());
        String balanceUserCoinVolumeId = SnowFlake.createSnowFlake().nextIdString();
        balanceUserCoinVolume.setId(balanceUserCoinVolumeId);
        if( balanceChangeUserCoinVolume.getFlag() != 0){
            balanceUserCoinVolume.setLockFlag(1);
        }
        balanceUserCoinVolumeDao.insert(balanceUserCoinVolume);
        if (!CollectionUtils.isNotEmpty(flaglist)) {
            List<BalancePlatJackpotVolumeDetail> jackpotList = balancePlatJackpotVolumeDetailDao.findByUserIdAndCoin("MG");
            BalancePlatJackpotVolumeDetail jackpotDetail = new BalancePlatJackpotVolumeDetail();
            if (CollectionUtils.isNotEmpty(jackpotList)) {
                jackpotDetail = jackpotList.get(0);
            }
            if (jackpotDetail.getAllCoinIncome() != null) {
                jackpotDetail.setAllCoinIncome(jackpotDetail.getAllCoinIncome().add(rewardNum));
            } else {
                jackpotDetail.setAllCoinIncome(rewardNum);
            }

            if (jackpotDetail.getCoinSymbol() == null) {
                jackpotDetail.setCoinSymbol("MG");
            }
            if (StringUtils.isNotEmpty(jackpotDetail.getId()) && !"null".equals(jackpotDetail.getId())) {
                balancePlatJackpotVolumeDetailDao.updateById(jackpotDetail);
            } else {
                jackpotDetail.setCreateDate(LocalDateTime.now());
                String jackpotDetailId = SnowFlake.createSnowFlake().nextIdString();
                jackpotDetail.setId(jackpotDetailId);
                balancePlatJackpotVolumeDetailDao.insert(jackpotDetail);
            }
        }
//        BalanceChangeUserCoinVolume balanceChangeUserCoinVolume = new BalanceChangeUserCoinVolume();
        balanceChangeUserCoinVolume.setCoinNum(balanceUserCoinVolume.getCoinBalance());
        balanceChangeUserCoinVolume.setUserId(balanceUserCoinVolume.getUserId());
        balanceChangeUserCoinVolume.setCoinSymbol(balanceUserCoinVolume.getCoinSymbol());
        balanceChangeUserCoinVolume.setCoinPlatSymbol("MG");
        balanceChangeUserCoinVolume.setMail(balanceUserCoinVolume.getMail());
        balanceChangeUserCoinVolume.setMobile(balanceUserCoinVolume.getMobile());
        balanceChangeUserCoinVolume.setBalanceId(balanceUserCoinVolume.getId());
        String balanceChangeUserCoinVolumeId = SnowFlake.createSnowFlake().nextIdString();
        balanceChangeUserCoinVolume.setId(balanceChangeUserCoinVolumeId);
        balanceChangeUserCoinVolume.setCreateDate(LocalDateTime.now());
        balanceChangeUserCoinVolumeDao.insert(balanceChangeUserCoinVolume);


        Coin coinVo = coinDao.findByName(balanceUserCoinVolume.getCoinSymbol());
        OfflineTransferLog offlineTransferLog = new OfflineTransferLog();
        String id = SnowFlake.createSnowFlake().nextIdString();
        offlineTransferLog.setId(id);
        offlineTransferLog.setCreateDate(LocalDateTime.now());
        offlineTransferLog.setUpdateDate(LocalDateTime.now());
        offlineTransferLog.setUserId(balanceUserCoinVolume.getUserId());
        offlineTransferLog.setCoinSymbol(balanceUserCoinVolume.getCoinSymbol());
        offlineTransferLog.setVolume(coinBalance);
        offlineTransferLog.setType(20);//常规账户到挖矿部落
        offlineTransferLog.setCoinId(coinVo.getId());
        offlineTransferLog.setSourceVolume(BigDecimal.ZERO);
        offlineTransferLogDao.insert(offlineTransferLog);

       long count= userCoinVolumeExService.updateOutcome(null, coinBalance, balanceUserCoinVolume.getUserId(), balanceUserCoinVolume.getCoinSymbol(), false);
        if (count <= 0){
            throw new PlatException(10077777, "转入失败");
        }
        Integer vaildValue2= cacheMap.get(key);
        if(vaildValue != vaildValue2){

            throw new PlatException(10077777, "转入失败");
        }
        cacheMap.put(key,vaildValue2+1);
    }
    @Override
    @Transactional
    public void balanceOutVolume(BalanceChangeUserCoinVolume balanceUserCoinVolume, BigDecimal coinNum){
        String key="balanceOutVolume:"+balanceUserCoinVolume.getUserId();
        Integer vaildValue= cacheMap.get(key);
        if(vaildValue ==null){
            vaildValue=0;
            cacheMap.put(key,0);
        }
        BalanceChangeUserCoinVolume balanceUserCoinVolumeUpdate = new BalanceChangeUserCoinVolume();
        BeanUtils.copyProperties(balanceUserCoinVolume, balanceUserCoinVolumeUpdate);
        balanceUserCoinVolume.setTakeOutDate(LocalDateTime.now());
        balanceUserCoinVolume.setFlag(1);
        String balanceUserCoinVolumeId = SnowFlake.createSnowFlake().nextIdString();
        balanceUserCoinVolume.setId(balanceUserCoinVolumeId);
        balanceUserCoinVolume.setCreateDate(LocalDateTime.now());
        balanceChangeUserCoinVolumeDao.insert(balanceUserCoinVolume);
        balanceUserCoinVolumeUpdate.setTakeOutDate(LocalDateTime.now());
        balanceChangeUserCoinVolumeDao.updateById(balanceUserCoinVolumeUpdate);
        long numFlag=balanceUserCoinVolumeDao.deleteByBalanceId(balanceUserCoinVolumeUpdate.getId());
        if(numFlag <=0){
            throw new PlatException(10066666, "记录已转出");
        }
        Coin coinVo = coinDao.findByName(balanceUserCoinVolume.getCoinSymbol());
        OfflineTransferLog offlineTransferLog = new OfflineTransferLog();
        String id = SnowFlake.createSnowFlake().nextIdString();
        offlineTransferLog.setId(id);
        offlineTransferLog.setCreateDate(LocalDateTime.now());
        offlineTransferLog.setUpdateDate(LocalDateTime.now());
        offlineTransferLog.setUserId(balanceUserCoinVolume.getUserId());
        offlineTransferLog.setCoinSymbol(balanceUserCoinVolume.getCoinSymbol());
        offlineTransferLog.setVolume(coinNum);
        offlineTransferLog.setType(21);//挖矿部落到常规账户
        offlineTransferLog.setCoinId(coinVo.getId());
        offlineTransferLog.setSourceVolume(BigDecimal.ZERO);
        offlineTransferLogDao.insert(offlineTransferLog);

       long count= userCoinVolumeExService.updateIncome(null, coinNum, balanceUserCoinVolume.getUserId(), balanceUserCoinVolume.getCoinSymbol(), false);
        if (count <= 0){
            throw new PlatException(10066666, "转出失败");
        }
        Integer vaildValue2= cacheMap.get(key);
        if(vaildValue != vaildValue2){
            throw new PlatException(10066666, "转出失败");
        }
        cacheMap.put(key,vaildValue2+1);
    }
}
