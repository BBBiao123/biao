package com.biao.business;

import com.biao.constant.Constants;
import com.biao.entity.EthTokenWithdraw;
import com.biao.entity.UserCoinVolume;
import com.biao.entity.WithdrawLog;
import com.biao.enums.WithdrawStatusEnum;
import com.biao.exception.PlatException;
import com.biao.init.Environment;
import com.biao.mapper.*;
import com.biao.redis.RedisCacheManager;
import com.biao.util.JsonUtils;
import com.biao.wallet.ethereum.TokenClient;
import com.biao.wallet.ethereum.TransactionClient;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

import static com.biao.constant.Constant.TOKEN_ADDRESS_MAP;
import static com.biao.constant.Constant.TOKEN_MAP;

@Service
public class WithdrawService {

    private static Logger logger = LoggerFactory.getLogger(WithdrawService.class);
    @Autowired
    CoinDao coinDao;

    @Autowired
    CoinAddressDao coinAddressDao;

    @Autowired
    UserCoinVolumeDao userCoinVolumeDao;
    @Autowired
    WithdrawLogDao withdrawLogDao;

    @Autowired
    PlatUserDao platUserDao;

    @Autowired
    EthTokenVolumeDao ethTokenVolumeDao;

    @Autowired
    EthTokenWithdrawDao ethTokenWithdrawDao;

    @Autowired
    private RedisCacheManager redisCacheManager;
    //1:基于以太  2:基于量子 3：基于小蚂 4：基于EOS

    /**
     * 1 :  审核通过 2 :审核bu通过
     *
     * @return
     */
    public List<WithdrawLog> findETHWithdrawLog() {
        return withdrawLogDao.findAllByCoinSymbolAndStatus("ETH", 1);
    }

    @Transactional
    public void executeWithdrawETH(WithdrawLog withdraw) throws Exception {
        String symbol = withdraw.getCoinSymbol();
        String fromAddress = Environment.fromAddress;
        //获取余额
        BigInteger addrAmount = TransactionClient.getBalance(fromAddress);
        BigDecimal bdAmount = Convert.fromWei(new BigDecimal(addrAmount), Convert.Unit.ETHER);
        BigDecimal realVolume = withdraw.getRealVolume();
        if (bdAmount.compareTo(realVolume) == 1) {
            //根据用户id查找用户 更新用户资产的冻结部分为冻结资产减去提现资产
            UserCoinVolume userCoinVolume = userCoinVolumeDao.findByUserIdAndCoinSymbol(withdraw.getUserId(), withdraw.getCoinSymbol());
            if (!Objects.isNull(userCoinVolume)) {
                BigDecimal outLockVolume = userCoinVolume.getOutLockVolume().subtract(withdraw.getVolume());
                if (outLockVolume.compareTo(BigDecimal.ZERO) == -1) {
                    logger.error("用户锁定资产异常:{}", JsonUtils.toJson(withdraw));
                    return;
                }
                Timestamp updateDate = Timestamp.valueOf(userCoinVolume.getUpdateDate());
                userCoinVolumeDao.updateOnlyOutLockVolumeBySymbol(withdraw.getUserId(), symbol, outLockVolume, updateDate);
            }
            //将txId 状态 更新到提现表
            String txId = TransactionClient.sendETH(fromAddress, withdraw.getAddress().trim(), realVolume);
            //TransactionClient.sendTransaction(fromAddress, Environment.password, withdraw.getAddress().trim(),realVolume );
            logger.info("eth withdraw address:{} volume:{} txid:{}", withdraw.getAddress(), withdraw.getVolume(), txId);
            withdrawLogDao.updateTxIdAndStatusById(txId, WithdrawStatusEnum.PAY.getCode(), withdraw.getId());
        } else {
            logger.error("地址余额不足{}{}:bdAmout{},realVolume{}", symbol, fromAddress, bdAmount, realVolume);
        }
    }

    public List<WithdrawLog> findErc20WithdrawLog() {
        return withdrawLogDao.findAllByCoinTypeAndStatus("1", 1);
    }

    @Transactional
    public void executeWithdrawERC20(WithdrawLog withdraw) throws Exception {
        String symbol = withdraw.getCoinSymbol().toUpperCase();
        String fromAddress = Environment.fromAddress;
        String password = Environment.password;
        Web3j web3j = Web3j.build(new HttpService(Environment.RPC_URL));
        Admin admin = Admin.build(new HttpService(Environment.RPC_URL));
        String contractAddress = TOKEN_ADDRESS_MAP.get(symbol);
        int decimals = TOKEN_MAP.get(symbol).getDecimals();
        logger.info("symbol:{},contractAddress:{}", symbol, contractAddress);
        //获取钱包代币余额
        BigInteger addrAmount = TokenClient.getTokenBalance(web3j, fromAddress, contractAddress);
        logger.info("钱包代币余额：" + addrAmount);
        BigDecimal bdAmount = BigDecimal.ZERO;
        BigInteger amount = BigInteger.ZERO;
        if (decimals == 0) {
            bdAmount = new BigDecimal(addrAmount);
            amount = Convert.toWei(withdraw.getRealVolume(), Convert.Unit.WEI).toBigInteger();
        } else if (decimals == 2) {
            bdAmount =new BigDecimal(addrAmount).divide(new BigDecimal(100));
            amount = withdraw.getRealVolume().divide(new BigDecimal(100)).toBigInteger();
        } else if (decimals == 3) {
            bdAmount = Convert.fromWei(new BigDecimal(addrAmount), Convert.Unit.KWEI);
            amount = Convert.toWei(withdraw.getRealVolume(), Convert.Unit.KWEI).toBigInteger();
        } else if (decimals == 5) {
            bdAmount = Convert.fromWei(new BigDecimal(addrAmount), Convert.Unit.MWEI).divide(BigDecimal.valueOf(100L));
            amount = Convert.toWei(withdraw.getRealVolume(), Convert.Unit.MWEI).toBigInteger().multiply(BigInteger.valueOf(100L));
        } else if (decimals == 6) {
            bdAmount = Convert.fromWei(new BigDecimal(addrAmount), Convert.Unit.MWEI);
            amount = Convert.toWei(withdraw.getRealVolume(), Convert.Unit.MWEI).toBigInteger();
        } else if (decimals == 8) {
            bdAmount = Convert.fromWei(new BigDecimal(addrAmount), Convert.Unit.MWEI).divide(BigDecimal.valueOf(100L));
            amount = Convert.toWei(withdraw.getRealVolume(), Convert.Unit.MWEI).toBigInteger().multiply(BigInteger.valueOf(100L));
        } else if (decimals == 12) {
            bdAmount = Convert.fromWei(new BigDecimal(addrAmount), Convert.Unit.SZABO);
            amount = Convert.toWei(withdraw.getRealVolume(), Convert.Unit.SZABO).toBigInteger();
        } else if (decimals == 18) {
            bdAmount = Convert.fromWei(new BigDecimal(addrAmount), Convert.Unit.ETHER);
            amount = Convert.toWei(withdraw.getRealVolume(), Convert.Unit.ETHER).toBigInteger();
        }

        logger.info("bdAmount{},realVolume{}", bdAmount, withdraw.getRealVolume());
        if (bdAmount.compareTo(withdraw.getRealVolume()) >= 0) {

            //根据用户id查找用户 更新用户资产的冻结部分为冻结资产减去提现资产
            UserCoinVolume userCoinVolume = userCoinVolumeDao.findByUserIdAndCoinSymbol(withdraw.getUserId(), withdraw.getCoinSymbol());
            if (!Objects.isNull(userCoinVolume)) {
                BigDecimal outLockVolume = userCoinVolume.getOutLockVolume().subtract(withdraw.getVolume());
                if (outLockVolume.compareTo(BigDecimal.ZERO) == -1) {
                    logger.error("用户锁定资产异常{}", JsonUtils.toJson(withdraw));
                    return;
                }
                Timestamp updateDate = Timestamp.valueOf(userCoinVolume.getUpdateDate());
                long count = userCoinVolumeDao.updateOnlyOutLockVolumeBySymbol(withdraw.getUserId(), withdraw.getCoinSymbol(), outLockVolume, updateDate);
                if (count <= 0)
                    throw new PlatException(Constants.WITHDRAW_ERROR, "提现失败");
            }
            //将txId 状态 更新到提现表
            String txId = TokenClient.sendTokenTransaction(admin, web3j, fromAddress, password, withdraw.getAddress(), TOKEN_ADDRESS_MAP.get(symbol), amount);
            logger.info("eth withdraw txid{}:", txId);
            if(StringUtils.isEmpty(txId)){
                logger.error("user{}withdraw{}amout ", withdraw.getUserId(),withdraw.getCoinSymbol(),withdraw.getVolume());
                //throw new PlatException(Constants.WITHDRAW_ERROR, "提现失败");
            }
            withdrawLogDao.updateTxIdAndStatusById(txId, WithdrawStatusEnum.PAY.getCode(), withdraw.getId());
        } else {
            logger.error("地址余额不足{}{}:bdAmout{},amount{}", symbol, fromAddress, bdAmount, amount);
        }
    }





    public List<WithdrawLog> findWithdrawLogByConfirmStatus(Integer confirmStatus, String coinType) {
        return withdrawLogDao.findWithdrawLogByConfirmStatusAndCoinType(confirmStatus, coinType);//0 正常 1：成功 2 失败
    }

    @Transactional
    public void executeConfirmStatus(WithdrawLog withdrawLog) {
        if ( withdrawLog.getStatus() == 3) {
           if(StringUtils.isEmpty(withdrawLog.getTxId())) {//hash 为空
               withdrawLog.setConfirmStatus(3);
               withdrawLogDao.updateById(withdrawLog);
           }else{
               Web3j web3j = Web3j.build(new HttpService(Environment.RPC_URL));
               EthGetTransactionReceipt receipt = null;
               try {
                   receipt = web3j.ethGetTransactionReceipt(withdrawLog.getTxId()).send();

               } catch (IOException e) {
                   e.printStackTrace();
               }
               if (Objects.isNull(receipt.getResult())) {
                   withdrawLog.setConfirmStatus(3);
                   withdrawLogDao.updateById(withdrawLog);
               } else {
                   String result = receipt.getResult().getStatus();

                   if ("0x1".equals(result)) {

                       withdrawLog.setConfirmStatus(1);
                       withdrawLogDao.updateById(withdrawLog);
                       logger.info("================{}", result);
                   } else {
                       withdrawLog.setConfirmStatus(2);
                       withdrawLogDao.updateById(withdrawLog);
                   }
               }
           }
        }
        return;
    }

    //
    @Transactional
    public void resendERCToken(WithdrawLog withdrawLog) {
        String symbol = withdrawLog.getCoinSymbol();
        if (symbol.equals("ETH")) return;
        String fromAddress = Environment.fromAddress;
        String password = Environment.password;
        Web3j web3j = Web3j.build(new HttpService(Environment.RPC_URL));
        Admin admin = Admin.build(new HttpService(Environment.RPC_URL));

        int decimals = TOKEN_MAP.get(symbol).getDecimals();

        BigDecimal bdAmount = BigDecimal.ZERO;
        BigInteger amount = BigInteger.ZERO;
        if (decimals == 0) {
            amount = Convert.toWei(withdrawLog.getRealVolume(), Convert.Unit.WEI).toBigInteger();
        } else if (decimals == 3) {
            amount = Convert.toWei(withdrawLog.getRealVolume(), Convert.Unit.KWEI).toBigInteger();
        } else if (decimals == 5) {
            amount = Convert.toWei(withdrawLog.getRealVolume(), Convert.Unit.MWEI).toBigInteger().multiply(BigInteger.valueOf(100L));
        } else if (decimals == 6) {
            amount = Convert.toWei(withdrawLog.getRealVolume(), Convert.Unit.MWEI).toBigInteger();
        } else if (decimals == 8) {
            amount = Convert.toWei(withdrawLog.getRealVolume(), Convert.Unit.MWEI).toBigInteger().multiply(BigInteger.valueOf(100L));
        } else if (decimals == 12) {
            amount = Convert.toWei(withdrawLog.getRealVolume(), Convert.Unit.SZABO).toBigInteger();
        } else if (decimals == 18) {
            amount = Convert.toWei(withdrawLog.getRealVolume(), Convert.Unit.ETHER).toBigInteger();
        }

        logger.info("bdAmount{},realVolume{}", bdAmount, withdrawLog.getRealVolume());
        //将txId 状态 更新到提现表
        String txId = TokenClient.sendTokenTransaction(admin, web3j, fromAddress, password, withdrawLog.getAddress(), TOKEN_ADDRESS_MAP.get(withdrawLog.getCoinSymbol()), amount);
        logger.info("{} withdraw txid{}:", symbol, txId);
        withdrawLog.setTxId(txId);
        withdrawLog.setConfirmStatus(0);
        withdrawLogDao.updateById(withdrawLog);
        return;
    }

}
