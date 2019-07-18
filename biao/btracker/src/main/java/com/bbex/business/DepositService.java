package com.bbex.business;

import com.azazar.bitcoin.jsonrpcclient.Bitcoin;
import com.biao.entity.CoinAddress;
import com.biao.entity.DepositLog;
import com.biao.entity.UserCoinVolume;
import com.biao.enums.UserCoinVolumeEventEnum;
import com.biao.mapper.CoinAddressDao;
import com.biao.mapper.DepositLogDao;
import com.biao.mapper.UserCoinVolumeDao;
import com.biao.pojo.UserCoinVolumeBillDTO;
import com.biao.redis.RedisCacheManager;
import com.biao.service.UserCoinVolumeBillService;
import com.biao.util.JsonUtils;
import com.bbex.util.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class DepositService {

    private static Logger logger = LoggerFactory.getLogger(DepositService.class);
    @Autowired
    private CoinAddressDao coinAddressDao;
    @Autowired
    private UserCoinVolumeDao userCoinVolumeDao;
    @Autowired
    private DepositLogDao depositLogDao;

    @Autowired
    private UserCoinVolumeBillService userCoinVolumeBillService;
    @Transactional
    public void executeDeposit(Bitcoin.Transaction transaction) {
        CoinAddress coinAddress = coinAddressDao.findByAddress(transaction.address());
        if (Objects.isNull(coinAddress)) {
            return;
        }
        DepositLog exsitDepositLog = depositLogDao.findByTxId(transaction.txId());
        if (Objects.isNull(exsitDepositLog)) {
            DepositLog depositLog = new DepositLog();
            depositLog.setStatus(1);
            depositLog.setCoinType("0");//BTC 0  ETH 1
            depositLog.setCreateDate(LocalDateTime.now());
            depositLog.setUpdateDate(LocalDateTime.now());
            depositLog.setUserId(coinAddress.getUserId());
            depositLog.setCoinSymbol(coinAddress.getSymbol());
            depositLog.setCoinId(coinAddress.getCoinId());
            depositLog.setVolume(BigDecimal.valueOf(transaction.amount()));
            depositLog.setConfirms(BigInteger.valueOf(transaction.confirmations()));
            depositLog.setTxId(transaction.txId());
            depositLog.setAddress(transaction.address());
            depositLog.setBlockNumber(BigInteger.ZERO);
            depositLog.setId(SnowFlake.createSnowFlake().nextIdString());
            depositLog.setRaiseStatus(0);
            depositLogDao.insert(depositLog);

            //给用户对应资产加上去
            UserCoinVolumeBillDTO dto = new UserCoinVolumeBillDTO();
            dto.setCoinSymbol(coinAddress.getSymbol());
            dto.setUserId(coinAddress.getUserId());
            dto.setForceLock(false);
            dto.setMark("充值"+coinAddress.getSymbol());
            dto.setOpLockVolume(BigDecimal.ZERO);
            dto.setOpVolume(depositLog.getVolume());
            UserCoinVolumeEventEnum[] emst = {
                    UserCoinVolumeEventEnum.ADD_VOLUME
            };
            dto.setOpSign(emst);
            dto.setSource(coinAddress.getSymbol()+"wallet");
            dto.setRefKey(transaction.txId());//唯一性
            long count = userCoinVolumeBillService.insert(dto);
            // long count = userCoinVolumeExService.updateIncome(null,log.getVolume(),log.getUserId(),log.getCoinSymbol());
            if(count <= 0) {
                logger.error("用户充值上账{},coinSymbol{},volume{}", coinAddress.getUserId(), coinAddress.getSymbol(), depositLog.getVolume());
            }

        } else {
            logger.info("充值记录已经存在" + transaction.txId(), "coinAddress:" + transaction.address());
        }
    }
}
