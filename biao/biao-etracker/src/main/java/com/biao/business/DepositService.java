package com.biao.business;

import com.biao.entity.Coin;
import com.biao.entity.CoinAddress;
import com.biao.entity.DepositLog;
import com.biao.enums.DepositCoinTypeEnum;
import com.biao.enums.TokenStatusEnum;
import com.biao.init.Environment;
import com.biao.mapper.*;
import com.biao.util.SnowFlake;
import com.biao.wallet.ethereum.TokenClient;
import com.biao.wallet.ethereum.TransactionClient;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static com.biao.constant.Constant.TOKEN_ADDRESS_MAP;
import static com.biao.constant.Constant.TOKEN_MAP;

@Service
public class DepositService {

    private static Logger logger = LoggerFactory.getLogger(DepositService.class);

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
    @Autowired
    PlatUserDao platUserDao;

    @Value("${fromAddress}")
    private String fromAddress;

    /**
     * ETH
     *
     * @param transaction
     * @throws Exception
     */
    @Transactional
    public void executeDepositETH(Transaction transaction) throws Exception {
        CoinAddress coinAddress = coinAddressDao.findByAddress(transaction.getTo());
        if (Objects.isNull(coinAddress)) {
            logger.error("该地址不存在{}:", transaction.getTo());
            return;
        }
        //如果hash 存在
        DepositLog exsitDepositLog = depositLogDao.findByTxId(transaction.getHash());
        if (Objects.isNull(exsitDepositLog)) {
            DepositLog depositLog = new DepositLog();
            depositLog.setStatus(0);
            depositLog.setCoinType(DepositCoinTypeEnum.ETH.getCode());
            depositLog.setCreateDate(LocalDateTime.now());
            depositLog.setUpdateDate(LocalDateTime.now());
            depositLog.setUserId(coinAddress.getUserId());
            depositLog.setCoinSymbol(coinAddress.getSymbol());
            depositLog.setCoinId(coinAddress.getCoinId());
            BigDecimal volume = Convert.fromWei(new BigDecimal(transaction.getValue()), Convert.Unit.ETHER);
            depositLog.setVolume(volume);
            depositLog.setConfirms(BigInteger.ZERO);
            depositLog.setTxId(transaction.getHash());
            depositLog.setAddress(transaction.getTo());
            depositLog.setId(SnowFlake.createSnowFlake().nextIdString());
            depositLog.setBlockNumber(BigInteger.ZERO);
            depositLog.setRaiseStatus(0);
            depositLogDao.insert(depositLog);
        } else {
            logger.info("该充值记录已经被记录等待确认:{}", transaction.getHash());
        }
    }

    /**
     * LOOM交易监听
     *
     * @param
     */
    @Transactional
    public void executeDepositERC20Token(String from, String to, BigDecimal volume, String hash, String symbol) {
        Coin coin = coinDao.findByName(symbol);
        if (Objects.isNull(coin)) {
            logger.error("币种没有配置{}:", symbol);
            return;
        }
        CoinAddress coinAddress = coinAddressDao.findByAddress(to);
        if (Objects.isNull(coinAddress)) {
            logger.error("该地址不存在{}:", to);
            return;
        }
        DepositLog exsitDepositLog = depositLogDao.findByTxId(hash);
        if (Objects.isNull(exsitDepositLog)) {
            DepositLog depositLog = new DepositLog();

            if (coin.getTokenStatus().equals(TokenStatusEnum.NOT_IN_OUT.getCode()) || coin.getTokenStatus().equals(TokenStatusEnum.ONLY_OUT.getCode())) {
                depositLog.setStatus(5);//暂时不上账
            } else {
                if (symbol.equals("UES")) {
                    depositLog.setStatus(5);//暂时不上账
                } else {
                    depositLog.setStatus(0);
                }
            }
            depositLog.setCoinType("1");
            depositLog.setCreateDate(LocalDateTime.now());
            depositLog.setUpdateDate(LocalDateTime.now());
            depositLog.setUserId(coinAddress.getUserId());
            depositLog.setCoinSymbol(symbol);
            depositLog.setCoinId(coin.getId());
//            BigDecimal volume = Convert.fromWei(new BigDecimal(transferEventResponse._value), Convert.Unit.ETHER);
            depositLog.setVolume(volume);
            depositLog.setConfirms(BigInteger.ZERO);
            depositLog.setTxId(hash);
            depositLog.setAddress(to);
            depositLog.setId(SnowFlake.createSnowFlake().nextIdString());
            depositLog.setBlockNumber(BigInteger.ZERO);
            depositLogDao.insert(depositLog);
        } else {
            logger.info("该充值记录已经被记录等待确认:{}", hash);
        }
    }

    public List<DepositLog> findETHDepositLog() {
        return depositLogDao.findAllByCoinSymbolAndRaiseStatus("ETH", 1);
    }

    public void executeETHRaise(DepositLog depositLog) {
        String fromAddress = Environment.fromAddress;
        //将txId 状态 更新到提现表
        String txId = TransactionClient.sendETH(depositLog.getAddress().trim(), fromAddress, depositLog.getVolume().subtract(BigDecimal.valueOf(0.0005)));
        logger.info("eth deposit raise:{} volume:{} txid:{}", depositLog.getAddress(), depositLog.getVolume(), txId);
        long result = depositLogDao.updateRaiseStatusById(2, depositLog.getId());

    }

    public List<DepositLog> findErc20DepositLog() {

        return depositLogDao.findAllByCoinTypelAndRaiseStatus("1", 1);

    }

    public void executeErc20Raise(DepositLog depositLog) {
        if (depositLog.getCoinSymbol().equals("ETH")) return;

        String symbol = depositLog.getCoinSymbol().toUpperCase();
        String fromAddress = Environment.fromAddress;
        String password = Environment.password;
        Web3j web3j = Web3j.build(new HttpService(Environment.RPC_URL));
        Admin admin = Admin.build(new HttpService(Environment.RPC_URL));
        String contractAddress = TOKEN_ADDRESS_MAP.get(symbol);
        int decimals = TOKEN_MAP.get(symbol).getDecimals();
        logger.info("symbol:{},contractAddress:{}", symbol, contractAddress);
        //获取钱包代币余额
        //BigInteger addrAmount = TokenClient.getTokenBalance(web3j, depositLog.getAddress(), contractAddress);
        BigDecimal bdAmount = BigDecimal.ZERO;
        BigInteger amount = BigInteger.ZERO;
        if (decimals == 0) {
           // bdAmount = new BigDecimal(addrAmount);
            amount = Convert.toWei(depositLog.getVolume(), Convert.Unit.WEI).toBigInteger();
        } else if (decimals == 3) {
           // bdAmount = Convert.fromWei(new BigDecimal(addrAmount), Convert.Unit.KWEI);
            amount = Convert.toWei(depositLog.getVolume(), Convert.Unit.KWEI).toBigInteger();
        } else if (decimals == 5) {
           // bdAmount = Convert.fromWei(new BigDecimal(addrAmount), Convert.Unit.MWEI).divide(BigDecimal.valueOf(100L));
            amount = Convert.toWei(depositLog.getVolume(), Convert.Unit.MWEI).toBigInteger().multiply(BigInteger.valueOf(100L));
        } else if (decimals == 6) {
            //bdAmount = Convert.fromWei(new BigDecimal(addrAmount), Convert.Unit.MWEI);
            amount = Convert.toWei(depositLog.getVolume(), Convert.Unit.MWEI).toBigInteger();
        } else if (decimals == 8) {
          //  bdAmount = Convert.fromWei(new BigDecimal(addrAmount), Convert.Unit.MWEI).divide(BigDecimal.valueOf(100L));
            amount = Convert.toWei(depositLog.getVolume(), Convert.Unit.MWEI).toBigInteger().multiply(BigInteger.valueOf(100L));
        } else if (decimals == 12) {
           // bdAmount = Convert.fromWei(new BigDecimal(addrAmount), Convert.Unit.SZABO);
            amount = Convert.toWei(depositLog.getVolume(), Convert.Unit.SZABO).toBigInteger();
        } else if (decimals == 18) {
           // bdAmount = Convert.fromWei(new BigDecimal(addrAmount), Convert.Unit.ETHER);
            amount = Convert.toWei(depositLog.getVolume(), Convert.Unit.ETHER).toBigInteger();
        }

        logger.info("bdAmount{},Volume{}", bdAmount, depositLog.getVolume());

        BigInteger addressAmount = TransactionClient.getBalance(depositLog.getAddress());
        if(addressAmount.compareTo(BigInteger.ZERO)<0) return;
        //将txId 状态 更新到提现表
        String txId = TokenClient.sendTokenTransaction(admin, web3j, depositLog.getAddress(), password, fromAddress, TOKEN_ADDRESS_MAP.get(symbol), amount);
        logger.info("{} raise{} txid{}:", symbol, amount, txId);
        if(StringUtils.isEmpty(txId)){
            long result = depositLogDao.updateRaiseStatusById(9, depositLog.getId());
        }else{
            long result = depositLogDao.updateRaiseStatusById(2, depositLog.getId());
        }


    }
}
