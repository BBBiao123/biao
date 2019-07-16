package com.bbex.business;

import com.azazar.bitcoin.jsonrpcclient.BitcoinJSONRPCClient;
import com.bbex.entity.Coin;
import com.bbex.entity.UserCoinVolume;
import com.bbex.entity.WithdrawLog;
import com.bbex.enums.WithdrawStatusEnum;
import com.bbex.mapper.*;
import com.bbex.redis.RedisCacheManager;
import com.bbex.util.RpcClient;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

@Service
public class WithdrawService {

    private static Logger logger = LoggerFactory.getLogger(WithdrawService.class);

    BitcoinJSONRPCClient client = null;
    @Autowired
    PlatUserDao platUserDao;

    @Autowired
    CoinDao coinDao;

    @Autowired
    CoinAddressDao coinAddressDao;
    @Autowired
    UserCoinVolumeDao userCoinVolumeDao;
    @Autowired
    WithdrawLogDao withdrawLogDao;
    @Autowired
    DepositLogDao depositLogDao;

    @Value("${symbolUrl}")
    private String symbolUrl;

    @Autowired
    private RedisCacheManager redisCacheManager;
    //1:基于以太  2:基于量子 3：基于小蚂 4：基于EOS
    @Transactional
    public void executeWithdraw(WithdrawLog withdraw) throws Exception {
        String symbol = withdraw.getCoinSymbol().toLowerCase();
        Coin coin = coinDao.findByName(symbol);
        if (!Objects.isNull(coin)) {
            //根据用户id查找用户 更新用户资产的冻结部分为冻结资产减去提现资产
            UserCoinVolume ucv = userCoinVolumeDao.findByUserIdAndCoinSymbol(withdraw.getUserId(), withdraw.getCoinSymbol());
            if (!Objects.isNull(ucv)) {
                BigDecimal outLockVolume = ucv.getOutLockVolume().subtract(withdraw.getVolume());
                if(outLockVolume.compareTo(BigDecimal.ZERO) ==-1){
                    throw new RuntimeException();
                }
                Timestamp updateDate = Timestamp.valueOf(ucv.getUpdateDate());
                userCoinVolumeDao.updateOnlyOutLockVolumeBySymbol(withdraw.getUserId(), symbol, outLockVolume, updateDate);
            }
            //将txId 状态 更新到提现表
            client = RpcClient.getClient(symbolUrl);
            //获取
            String txId = client.sendToAddress(withdraw.getAddress().trim(), withdraw.getRealVolume().doubleValue());
            if (StringUtils.isEmpty(txId)) return;
            withdrawLogDao.updateTxIdAndStatusById(txId, WithdrawStatusEnum.PAY.getCode(), withdraw.getId());
//            redisCacheManager.cleanUserCoinVolume(withdraw.getUserId(), withdraw.getCoinSymbol());
        } else {
            return;
        }
    }

    public List<WithdrawLog> findWithdrawLogBySymbol(String symbol) {
        return withdrawLogDao.findAllByCoinSymbolAndStatus(symbol, 1);
    }
}
