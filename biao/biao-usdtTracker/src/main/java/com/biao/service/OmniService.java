package com.biao.service;

import com.biao.cache.UsdtGuavaCacheManager;
import com.biao.client.MyOmniClient;
import com.biao.config.OmniConfig;
import com.biao.constant.UsdtConstants;
import com.biao.entity.AddressConfig;
import com.biao.entity.Coin;
import com.biao.entity.CoinAddress;
import com.biao.entity.DepositAddress;
import com.biao.entity.DepositLog;
import com.biao.entity.UserCoinVolume;
import com.biao.entity.WithdrawLog;
import com.biao.enums.DepositCoinTypeEnum;
import com.biao.enums.TradePairEnum;
import com.biao.enums.UserCoinVolumeEventEnum;
import com.biao.enums.WithdrawStatusEnum;
import com.biao.mapper.AddressConfigDao;
import com.biao.mapper.CoinAddressDao;
import com.biao.mapper.CoinBlockDao;
import com.biao.mapper.CoinDao;
import com.biao.mapper.DepositAddressDao;
import com.biao.mapper.DepositLogDao;
import com.biao.mapper.UserCoinVolumeDao;
import com.biao.mapper.WithdrawLogDao;
import com.biao.pojo.UserCoinVolumeBillDTO;
import com.biao.redis.RedisCacheManager;
import com.biao.util.CheckUtil;
import com.biao.util.SnowFlake;
import foundation.omni.CurrencyID;
import foundation.omni.OmniValue;
import foundation.omni.PropertyType;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.bitcoinj.core.Address;
import org.bitcoinj.core.Sha256Hash;
import org.bitcoinj.params.MainNetParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * The type Omni service.
 */
@Component
public class OmniService {

    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(OmniService.class);

    private static final Lock LOCK = new ReentrantLock();

    private final MyOmniClient omniClient;

    private final UsdtGuavaCacheManager usdtGuavaCacheManager;

    private final RedisCacheManager redisCacheManager;

    private final CoinBlockDao coinBlockDao;

    private final DepositAddressDao depositAddressDao;

    private final UserCoinVolumeDao userCoinVolumeDao;

    private final WithdrawLogDao withdrawLogDao;

    private final DepositLogDao depositLogDao;

    private final UserCoinVolumeBillService userCoinVolumeBillService;

    private final OmniConfig config;

    private final CoinDao coinDao;

    private final CoinAddressDao coinAddressDao;

    private final AddressConfigDao addressConfigDao;

    @Value("${usdtAddrCount:10}")
    private Integer count;


    /**
     * Instantiates a new Omni service.
     *
     * @param omniClient                the omni client
     * @param usdtGuavaCacheManager     the usdt guava cache manager
     * @param coinBlockDao              the coin block dao
     * @param depositAddressDao         the deposit address dao
     * @param userCoinVolumeDao         the user coin volume dao
     * @param redisCacheManager         the redis cache manager
     * @param withdrawLogDao            the withdraw log dao
     * @param depositLogDao             the deposit log dao
     * @param userCoinVolumeBillService the user coin volume bill service
     */
    @Autowired(required = false)
    public OmniService(final MyOmniClient omniClient,
                       final UsdtGuavaCacheManager usdtGuavaCacheManager,
                       final CoinBlockDao coinBlockDao,
                       final DepositAddressDao depositAddressDao,
                       final UserCoinVolumeDao userCoinVolumeDao,
                       final RedisCacheManager redisCacheManager,
                       final WithdrawLogDao withdrawLogDao,
                       final DepositLogDao depositLogDao,
                       final UserCoinVolumeBillService userCoinVolumeBillService,
                       final OmniConfig config, CoinDao coinDao, CoinAddressDao coinAddressDao, AddressConfigDao addressConfigDao) {
        this.omniClient = omniClient;
        this.usdtGuavaCacheManager = usdtGuavaCacheManager;
        this.coinBlockDao = coinBlockDao;
        this.depositAddressDao = depositAddressDao;
        this.userCoinVolumeDao = userCoinVolumeDao;
        this.redisCacheManager = redisCacheManager;
        this.withdrawLogDao = withdrawLogDao;
        this.depositLogDao = depositLogDao;
        this.userCoinVolumeBillService = userCoinVolumeBillService;
        this.config = config;
        this.coinDao = coinDao;
        this.coinAddressDao = coinAddressDao;
        this.addressConfigDao = addressConfigDao;
    }

    /**
     * Usdt send task.
     */
    public void usdtSendTask() {
        final List<WithdrawLog> withdrawLogs = withdrawLogDao.findAllByCoinSymbolAndStatus(TradePairEnum.USDT.getKey(),
                Integer.valueOf(WithdrawStatusEnum.AUDIT_PASS.getCode()));
        LOGGER.info("usdt提现查询结果： " + withdrawLogs.toString());
        if (CollectionUtils.isNotEmpty(withdrawLogs)) {
            LOGGER.info("=========提现条数为===={}", withdrawLogs.size());
            for (WithdrawLog withdraw : withdrawLogs) {
                usdtSend(withdraw);
            }
        }
    }

    /**
     * 执行usdt提现操作.
     *
     * @param withdraw the withdraw
     */
    @Transactional(rollbackFor = Exception.class)
    public void usdtSend(final WithdrawLog withdraw) {
        try {
            LOCK.lock();

            if (!CheckUtil.checkBtcAddress(withdraw.getAddress())) {
                LOGGER.error("地址非法:{}", withdraw.getAddress());
                return;
            }
            UserCoinVolume userCoinVolume = userCoinVolumeDao.findByUserIdAndCoinSymbol(withdraw.getUserId(), withdraw.getCoinSymbol());
            if (!Objects.isNull(userCoinVolume)) {

                final BigDecimal outLockVolume = userCoinVolume.getOutLockVolume();
                if (outLockVolume.compareTo(BigDecimal.ZERO) <= 0) {
                    return;
                }
                BigDecimal result = outLockVolume.subtract(withdraw.getVolume());
                if (result.compareTo(BigDecimal.ZERO) < 0) {
                    LOGGER.error("用户锁定资产为负数，用户id为:{}", withdraw.getUserId());
                    return;
                }
                Timestamp updateDate = Timestamp.valueOf(userCoinVolume.getUpdateDate());

                long r = userCoinVolumeDao.updateOnlyOutLockVolumeBySymbol(withdraw.getUserId(), withdraw.getCoinSymbol(), result, updateDate);
                if (r <= 0) {
                    LOGGER.error("更新用户锁定资产失败:{}", withdraw.getUserId());
                    return;
                }
                redisCacheManager.cleanUserCoinVolume(withdraw.getUserId(), UsdtConstants.USDT_SMYBOL);
                long res = withdrawLogDao.updateStatusById(WithdrawStatusEnum.PAY.getCode(), withdraw.getId());
                if (res <= 0) {
                    LOGGER.error("更新withdrawLog日志状态失败:{}", withdraw.getUserId());
                    return;
                }
                String hash;
                try {
                    LOGGER.info("=========开始执行给用户提现=========用户id：{},提现数量为:{}",
                            withdraw.getUserId(), withdraw.getRealVolume());
                    hash = omniSendUsdt(config.getSendAddr(), withdraw.getAddress(), withdraw.getRealVolume());
                    LOGGER.info("=========钱包发送成功=========用户id:{},提现数量为:{},hash:{}",
                            withdraw.getUserId(), withdraw.getRealVolume(), hash);
                } catch (IOException e) {
                    LOGGER.error("用户提现失败：用户id为：{},地址为:{},原因：{}", withdraw.getUserId(), withdraw.getAddress(), e.getMessage(), e);
                    throw new RuntimeException();
                }
                long txCount = withdrawLogDao.updateTxIdAndStatusById(hash, WithdrawStatusEnum.PAY.getCode(), withdraw.getId());
                LOGGER.info("=====updateTxid to database====txCount:==========={}",
                        txCount);
            }
        } finally {
            LOCK.unlock();
        }
    }

    /**
     * Collect task.
     */
    public void collectTask() {
        final List<DepositLog> depositLogList = depositLogDao.findAllByCoinSymbolAndRaiseStatus(UsdtConstants.USDT_SMYBOL, 1);

        if (CollectionUtils.isNotEmpty(depositLogList)) {
            LOGGER.info("=========归集条数为===={}", depositLogList.size());
            for (DepositLog log : depositLogList) {
                collect(log);
            }
        }
    }

    /**
     * Collect.
     *
     * @param log the log
     */
    @Transactional(rollbackFor = Exception.class)
    public void collect(final DepositLog log) {
        String hash;
        try {
            LOGGER.info("=========开始执行归集=========用户id：{},归集数量为:{}",
                    log.getUserId(), log.getVolume());
            hash = omniFundedSend(log.getAddress(), config.getSendAddr(), log.getVolume(), config.getSendAddr());
            LOGGER.info("=========钱包发送成功=========用户id:{},提现数量为:{},hash:{}",
                    log.getUserId(), log.getVolume(), hash);
        } catch (IOException e) {
            LOGGER.error("归集提现失败：用户id为：{},地址为:{},原因：{}", log.getUserId(), log.getAddress(), e.getMessage(), e);
            throw new RuntimeException();
        }
        long txCount = depositLogDao.updateRaiseStatusById(2, log.getId());
        if (txCount > 0) {
            LOGGER.info("=====归集成功:==========={}",
                    log.toString());
        }

    }

    public void executeAddress() throws Exception {
        try {
            LOGGER.info("定时执行生成usdt地址开始");
            AddressConfig addressConfig = addressConfigDao.findByName(UsdtConstants.USDT_SMYBOL);
            LOGGER.info("查询生成地址的币种： " + addressConfig.toString());
            if (Objects.isNull(addressConfig)) {
                return;
            }
            if (addressConfig.getStatus() == 0) {
                return;
            }
            List<CoinAddress> addressesList = new ArrayList<>(10);
            Coin coin = coinDao.findByName(UsdtConstants.USDT_SMYBOL);
            LOGGER.info("查询生成地址的币种： " + coin.toString());
            if (!Objects.isNull(coin)) {
                for (int i = 0; i < count; i++) {
                    CoinAddress coinAddress = buildCoinAddr(coin);
                    String address = getNewAddress();
                    if (StringUtils.isEmpty(address)) {
                        continue;
                    }
                    coinAddress.setAddress(address);
                    addressesList.add(coinAddress);
                }
            }
            //此处写持久层逻辑
            coinAddressDao.batchInsert(addressesList);
        } catch (Exception e) {
            throw new Exception("生成地址失败");
        }
    }


    /**
     * 获取区块上的一个地址.
     *
     * @return String new address
     */
    public String getNewAddress() {
        String address = "";
        try {
            final Address newAddress = omniClient.getNewAddress();
            LOGGER.info("获取区块上的一个新地址： " + newAddress.toBase58());
            return newAddress.toBase58();
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error("omni client connection is exception :{}", e.getMessage());
        }
        return address;
    }


    /**
     * Sync block.
     *
     * @throws Exception the exception
     */
    public void syncBlock() throws Exception {
        final Integer blockHeight = usdtGuavaCacheManager.getBlockHeight(UsdtConstants.USDT_SMYBOL);

        final Integer blockCount = getBlockCount();

        LOGGER.info("USDT 钱包区块高度为:{},系统已经处理的高度为" + ":{}", blockCount, blockHeight);

        //如果钱包高度比处理高度大

        int index = blockHeight + 1;

        while (index <= blockCount) {
            LOGGER.info("USDT 钱包区块高度为index:{},系统已经处理的高度为blockCount:{}", blockCount, index);
            final List<Sha256Hash> sha256Hashes = omniClient.omniListBlockTransactions(index);
            if (CollectionUtils.isNotEmpty(sha256Hashes)) {
                for (Sha256Hash sha256Hash : sha256Hashes) {
                    final Map<String, Object> dataMap = omniClient.omniGetTransaction(sha256Hash);
                    executeUsdt(dataMap);
                }
            }
            if (index == blockCount) {
                //更新缓存
                usdtGuavaCacheManager.cacheBlockHeight(UsdtConstants.USDT_SMYBOL, blockCount);
                //更新数据库
                coinBlockDao.updateHeight(UsdtConstants.USDT_SMYBOL, blockCount);
            }
            index++;
        }
    }

    /**
     * bb add 走中间表.
     *
     * @param dataMap dataMap
     */
    private void executeUsdt(final Map<String, Object> dataMap) {
        if (dataMap == null) {
            return;
        }

        //如果不是usdt
        Object propertyid = dataMap.get("propertyid");
        if (Objects.isNull(propertyid)) {
            return;
        }
        final long propertyId = Long.parseLong(String.valueOf(propertyid));
        if (propertyId != UsdtConstants.USDT_ID) {
            return;
        }

        //如果 是无效的
        final Boolean pass = Boolean.valueOf(String.valueOf(dataMap.get("valid")));

        if (!pass) {
            final String invalidReason = String.valueOf(dataMap.get("invalidreason"));
            LOGGER.error(invalidReason);
            return;
        }

        final String txId = String.valueOf(dataMap.get("txid"));

        final BigDecimal amount = new BigDecimal(String.valueOf(dataMap.get("amount")));

        if (amount.compareTo(BigDecimal.ZERO) > 0) {

            //地址，判断这个地址在我们的交易所是否存在，是哪个用户的地址
            final String address = String.valueOf(dataMap.get("referenceaddress"));

            //判断该地址有没有关联平台用户
            final DepositAddress depositAddress = depositAddressDao.findByAddress(address);
            if (Objects.isNull(depositAddress)) {
                return;
            }

            //根据tx查询日志有没有
            DepositLog depositLog = depositLogDao.findByTxIdAndSymbol(txId, UsdtConstants.USDT_SMYBOL);
            if (!Objects.isNull(depositLog)) {
                return;
            }

            String userId = depositAddress.getUserId();
            UserCoinVolumeBillDTO dto = new UserCoinVolumeBillDTO();
            dto.setCoinSymbol(UsdtConstants.USDT_SMYBOL);
            dto.setUserId(userId);
            dto.setForceLock(false);
            dto.setMark("充值usdt");
            dto.setOpLockVolume(BigDecimal.ZERO);
            dto.setOpVolume(amount);
            UserCoinVolumeEventEnum[] ems = {UserCoinVolumeEventEnum.ADD_VOLUME};
            dto.setOpSign(ems);
            dto.setSource("usdtwallet");
            dto.setRefKey(txId);
            long count = userCoinVolumeBillService.insert(dto);
            if (count <= 0) {
                LOGGER.error("用户充值上账{},coinSymbol{},volume{}", userId, UsdtConstants.USDT_SMYBOL, amount);
            }
            //新增记录日志
            saveLog(depositAddress, amount, address, txId);
        }

    }

    private String omniSendUsdt(final String fromAddress, final String toAddress, final BigDecimal amount) throws IOException {
        Sha256Hash sha256Hash = omniClient.omniSend(Address.fromBase58(MainNetParams.get(), fromAddress),
                Address.fromBase58(MainNetParams.get(), toAddress),
                new CurrencyID(UsdtConstants.USDT_ID), OmniValue.of(amount, PropertyType.DIVISIBLE));
        return sha256Hash.toString();
    }

    private String omniFundedSend(final String fromAddress, final String toAddress, final BigDecimal amount, final String feeaddress) throws IOException {
        Sha256Hash sha256Hash = omniClient.omnifundedSend(Address.fromBase58(MainNetParams.get(), fromAddress),
                Address.fromBase58(MainNetParams.get(), toAddress),
                new CurrencyID(UsdtConstants.USDT_ID), OmniValue.of(amount, PropertyType.DIVISIBLE),
                Address.fromBase58(MainNetParams.get(), feeaddress));
        return sha256Hash.toString();

    }

    private Integer getBlockCount() throws IOException {
        return omniClient.getBlockCount();
    }

    private void saveLog(final DepositAddress depositAddress, final BigDecimal amount, final String address, final String txId) {
        //新增记录日志
        DepositLog depositLog = new DepositLog();
        //0 代表确认中  1:代表成功
        depositLog.setStatus(1);
        //1 代表eth 代币  0:代表btc 代币
        depositLog.setCoinType(DepositCoinTypeEnum.BTC.getCode());
        depositLog.setCreateDate(LocalDateTime.now());
        depositLog.setUpdateDate(LocalDateTime.now());
        depositLog.setUserId(depositAddress.getUserId());
        depositLog.setCoinSymbol(UsdtConstants.USDT_SMYBOL);
        depositLog.setCoinId(config.getCoinId());
        depositLog.setVolume(amount);
        depositLog.setConfirms(BigInteger.ZERO);
        depositLog.setTxId(txId);
        depositLog.setAddress(address);
        depositLog.setId(SnowFlake.createSnowFlake().nextIdString());
        depositLog.setBlockNumber(BigInteger.ZERO);
        depositLogDao.insert(depositLog);
    }

    private CoinAddress buildCoinAddr(Coin coin) {
        CoinAddress coinAddress = new CoinAddress();
        coinAddress.setStatus("0");
        coinAddress.setUserId("0");
        coinAddress.setCreateDate(LocalDateTime.now());
        coinAddress.setUpdateDate(LocalDateTime.now());
        coinAddress.setType(1);
        coinAddress.setCoinId(coin.getParentId());
        coinAddress.setSymbol(UsdtConstants.USDT_SMYBOL);
        coinAddress.setId(SnowFlake.createSnowFlake().nextIdString());
        return coinAddress;
    }

}
