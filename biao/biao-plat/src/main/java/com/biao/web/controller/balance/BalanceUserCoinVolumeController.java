package com.biao.web.controller.balance;

import com.biao.config.BalancePlatDayRateConfig;
import com.biao.config.sercurity.RedisSessionUser;
import com.biao.entity.*;
import com.biao.entity.balance.*;
import com.biao.handler.PlatDataHandler;
import com.biao.mapper.CoinDao;
import com.biao.mapper.OfflineTransferLogDao;
import com.biao.mapper.PlatUserDao;
import com.biao.pojo.GlobalMessageResponseVo;
import com.biao.pojo.RequestQuery;
import com.biao.pojo.ResponsePage;
import com.biao.query.UserFinanceQuery;
import com.biao.reactive.data.mongo.service.TradeDetailService;
import com.biao.service.*;
import com.biao.service.balance.*;
import com.biao.util.RsaUtils;
import com.biao.util.SnowFlake;
import com.biao.vo.PlatUserVO;
import com.biao.vo.TradePairVO;
import com.biao.vo.balance.*;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 余币宝controller
 *
 * @author liao
 */
@RestController
@RequestMapping("/biao")
public class BalanceUserCoinVolumeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BalanceUserCoinVolumeController.class);

    @Autowired
    private OfflineCoinVolumeService offlineCoinVolumeService;
    @Autowired
    private OfflineCoinService offlineCoinService;

    @Autowired
    private OfflineTransferLogService offlineTransferLogService;

    @Autowired
    private CoinService coinService;

    @Autowired
    private BalanceUserCoinVolumeService balanceUserCoinVolumeService;

    @Autowired
    private BalanceUserCoinCountVolumeService balanceUserCoinCountVolumeService;

    @Autowired
    private BalancePlatJackpotVolumeService balancePlatJackpotVolumeService;

    @Autowired
    private BalanceChangeUserCoinVolumeService balanceChangeUserCoinVolumeService;

    @Autowired
    private BalanceUserCoinVolumeDetailService balanceUserCoinVolumeDetailService;

    @Autowired
    private BalanceUserVolumeIncomeDetailService balanceUserVolumeIncomeDetailService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CoinDao coinDao;

    @Autowired
    private TradeDetailService tradeDetailService;

    @Autowired
    private Mk2MiningService mk2MiningService;

    @Autowired
    private UserCoinVolumeExService userCoinVolumeExService;

    @Autowired
    private BalancePlatDayRateConfig balancePlatDayRateConfig;

    @Autowired
    private PlatUserDao platUserDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final PlatDataHandler platDataHandler;

    private final OfflineTransferLogDao offlineTransferLogDao;


    @Autowired
    public BalanceUserCoinVolumeController(final PlatDataHandler platDataHandler, final OfflineTransferLogDao offlineTransferLogDao) {
        this.platDataHandler = platDataHandler;
        this.offlineTransferLogDao = offlineTransferLogDao;

    }

    /**
     * 根据用户查询所有币种余额收益信息
     *
     * @return
     */
    @GetMapping("/balance/volume/list")
    public Mono<GlobalMessageResponseVo> findAll() {

        Mono<SecurityContext> context
                = ReactiveSecurityContextHolder.getContext();

        return context.filter(c -> Objects.nonNull(c.getAuthentication()))
                .map(s -> s.getAuthentication().getPrincipal())
                .cast(RedisSessionUser.class)
                .map(e -> {
                    //查找coin所有币种
                    List<Coin> list = coinService.findAll();
                    List<BalanceCoinVolumeVO> listVo = new ArrayList<>(5);
                    list.forEach(coin -> {
                        BalanceCoinVolumeVO coinVolumeVO = new BalanceCoinVolumeVO();
                        BeanUtils.copyProperties(coin, coinVolumeVO);
                        coinVolumeVO.setId(null);
                        coinVolumeVO.setUserId(e.getId());
                        coinVolumeVO.setCoinId(coin.getId());
                        listVo.add(coinVolumeVO);

                    });
                    //查询余币宝资产信息
                    List<BalanceUserCoinVolume> listVolume = balanceUserCoinVolumeService.findAll(e.getId());

                    listVo.forEach(coinVolumeVO2 -> {
                        if (CollectionUtils.isNotEmpty(listVolume)) {
                            listVolume.forEach(userCoinVolume -> {

                                if (userCoinVolume != null) {
                                    if (userCoinVolume.getCoinSymbol().equals(coinVolumeVO2.getName())) {
                                        BeanUtils.copyProperties(userCoinVolume, coinVolumeVO2);
                                        coinVolumeVO2.setReferId(e.getReferId());

                                    }
                                }
                            });

                        }
                        BigDecimal balance = new BigDecimal(10000);
                        BigDecimal balance2 = new BigDecimal(1000);
                        BigDecimal balance3 = new BigDecimal(200);
                        BigDecimal dayRate = new BigDecimal(0);
                        String positionName = "";
                        if (coinVolumeVO2.getCoinBalance() != null && coinVolumeVO2.getCoinBalance().compareTo(balance3) >= 0) {
                            if (coinVolumeVO2.getCoinBalance().compareTo(balance) > 0) {
                                dayRate = dayRate.add(balancePlatDayRateConfig.getThreeDayRate().multiply(new BigDecimal(100)));
                                positionName = "三级仓";
                            } else if (coinVolumeVO2.getCoinBalance().compareTo(balance2) > 0) {
                                dayRate = dayRate.add(balancePlatDayRateConfig.getSecondDayRate().multiply(new BigDecimal(100)));
                                positionName = "二级仓";
                            } else {
                                positionName = "一级仓";
                                dayRate = dayRate.add(balancePlatDayRateConfig.getOneDayRate().multiply(new BigDecimal(100)));
                            }
                        } else {
                            positionName = "暂无仓位";
                        }
                        coinVolumeVO2.setRise(dayRate.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "%");
                        coinVolumeVO2.setPositionName(positionName);
                        //我的资产
                        if (coinVolumeVO2.getCoinBalance() != null) {
                            coinVolumeVO2.setCoinBalance(coinVolumeVO2.getCoinBalance().setScale(2, BigDecimal.ROUND_HALF_UP));
                        } else {
                            coinVolumeVO2.setCoinBalance(BigDecimal.ZERO);
                        }
                        //
                        if (coinVolumeVO2.getYesterdayIncome() != null) {
                            coinVolumeVO2.setYesterdayIncome(coinVolumeVO2.getYesterdayIncome().setScale(2, BigDecimal.ROUND_HALF_UP));
                        } else {
                            coinVolumeVO2.setYesterdayIncome(BigDecimal.ZERO);
                        }
                        //
                        if (coinVolumeVO2.getYesterdayReward() != null) {
                            coinVolumeVO2.setYesterdayReward(coinVolumeVO2.getYesterdayReward().setScale(2, BigDecimal.ROUND_HALF_UP));
                        } else {
                            coinVolumeVO2.setYesterdayReward(BigDecimal.ZERO);
                        }
                        //
                        if (coinVolumeVO2.getTeamAmount() != null) {
                            coinVolumeVO2.setTeamAmount(coinVolumeVO2.getTeamAmount().setScale(2, BigDecimal.ROUND_HALF_UP));
                        } else {
                            coinVolumeVO2.setTeamAmount(BigDecimal.ZERO);
                        }
                        //
                        if (coinVolumeVO2.getTeamCommunityAmount() != null) {
                            coinVolumeVO2.setTeamCommunityAmount(coinVolumeVO2.getTeamCommunityAmount().setScale(2, BigDecimal.ROUND_HALF_UP));
                        } else {
                            coinVolumeVO2.setTeamCommunityAmount(BigDecimal.ZERO);
                        }
                        //
                        if (coinVolumeVO2.getAccumulIncome() != null) {
                            coinVolumeVO2.setAccumulIncome(coinVolumeVO2.getAccumulIncome().setScale(2, BigDecimal.ROUND_HALF_UP));
                        } else {
                            coinVolumeVO2.setAccumulIncome(BigDecimal.ZERO);
                        }
                        //
                        if (coinVolumeVO2.getAccumulReward() != null) {
                            coinVolumeVO2.setAccumulReward(coinVolumeVO2.getAccumulReward().setScale(2, BigDecimal.ROUND_HALF_UP));
                        } else {
                            coinVolumeVO2.setAccumulReward(BigDecimal.ZERO);
                        }
                        //

                        if (coinVolumeVO2.getTeamLevel() == null) {
                            coinVolumeVO2.setTeamLevel("V0");
                        }
                        UserCoinVolume userVolume = userCoinVolumeExService.findByUserIdAndCoinSymbol(e.getId(), coinVolumeVO2.getName());
                        if (userVolume != null) {
                            coinVolumeVO2.setUserSurplus(userVolume.getVolume());
                        }
                        if (coinVolumeVO2.getUserSurplus() != null) {
                            coinVolumeVO2.setUserSurplus(coinVolumeVO2.getUserSurplus().setScale(2, BigDecimal.ROUND_HALF_UP));
                        } else {
                            coinVolumeVO2.setUserSurplus(BigDecimal.ZERO);
                        }
                    });
                    return GlobalMessageResponseVo
                            .newSuccessInstance(listVo);
                });
    }

    /**
     * 增加余币宝资产（转入）
     *
     * @param balanceCoinVolumeVO
     * @return
     */
    @PostMapping("/balance/volume/add")
    public Mono<GlobalMessageResponseVo> addBalance(BalanceCoinVolumeVO balanceCoinVolumeVO) {

        Mono<SecurityContext> context = ReactiveSecurityContextHolder.getContext();

        return context.filter(c -> Objects.nonNull(c.getAuthentication()))
                .map(s -> s.getAuthentication().getPrincipal())
                .cast(RedisSessionUser.class)
                .map(e -> {
                    System.out.println("用户：----"+e.getId()+"-----"+e.getMobile()+"-----"+e.getMail()+"-----"+e.getExPassword());
                    if(balanceCoinVolumeVO.getCoinNum().compareTo(BigDecimal.ZERO)<=0){
                        return GlobalMessageResponseVo.newErrorInstance("转入资产不能少于等于0");
                    }
                    if (StringUtils.isBlank(e.getExPassword())) {
                        return GlobalMessageResponseVo.newErrorInstance("请先设置交易密码");
                    }
                    if (StringUtils.isBlank(balanceCoinVolumeVO.getExPassword())) {
                        return GlobalMessageResponseVo.newErrorInstance("请输入交易密码");
                    }
                    String exDecryPassword = RsaUtils.decryptByPrivateKey(balanceCoinVolumeVO.getExPassword(), RsaUtils.DEFAULT_PRIVATE_KEY);
                    boolean result = passwordEncoder.matches(exDecryPassword, e.getExPassword());
                    if (!result) {
                        return GlobalMessageResponseVo.newErrorInstance("请输入正确的交易密码");
                    }

                    Map<String, List<TradePairVO>> tradePairListMap = platDataHandler.buildAllTradePair();
                    Map<String, TradePairVO> tradePairMap = new HashMap<String, TradePairVO>();
                    if (tradePairListMap != null && tradePairListMap.size() > 0) {
                        for (String key : tradePairListMap.keySet()) {
                            List<TradePairVO> list = tradePairListMap.get(key);
                            for (TradePairVO vo : list) {
                                tradePairMap.put(vo.getCoinOther(), vo);
                            }
                        }
                    }
                    TradePairVO tradePair = tradePairMap.get(balanceCoinVolumeVO.getCoinSymbol());
                    BigDecimal depositValue=balanceCoinVolumeVO.getCoinNum();
                    BigDecimal platPrice=BigDecimal.ZERO;

                    if(tradePair!=null && tradePair.getLatestPrice() != null && tradePair.getLatestPrice().compareTo(BigDecimal.ZERO)>0){
                        platPrice=platPrice.add(tradePair.getLatestPrice());
                    }
                    if(platPrice.compareTo(BigDecimal.ZERO)<=0 && tradePair!=null){
                        BigDecimal coinPrice=balanceUserCoinVolumeDetailService.findPriceByCoinSymbolUpdateDate(balanceCoinVolumeVO.getCoinSymbol());
                        if(coinPrice != null){
                            platPrice=platPrice.add(coinPrice);
                        }
                    }
                    if(!"USDT".equalsIgnoreCase(balanceCoinVolumeVO.getCoinSymbol())){
                        depositValue=depositValue.multiply(platPrice);
                    }
                    BalanceUserCoinVolume balanceUserCoinVolume = new BalanceUserCoinVolume();
                    System.out.println("用户ID：" + balanceCoinVolumeVO.getUserId() + "-----币种：" + balanceCoinVolumeVO.getCoinSymbol());
                    List<BalanceUserCoinVolume> flaglist = balanceUserCoinVolumeService.findAll(balanceCoinVolumeVO.getUserId());
                    BeanUtils.copyProperties(balanceCoinVolumeVO, balanceUserCoinVolume);

                    balanceUserCoinVolume.setMail(e.getMail());
                    balanceUserCoinVolume.setMobile(e.getMobile());
                    balanceUserCoinVolume.setCoinSymbol(balanceCoinVolumeVO.getCoinSymbol());
                    balanceUserCoinVolume.setCoinBalance(balanceCoinVolumeVO.getCoinNum());
                    balanceUserCoinVolume.setDepositValue(depositValue);
                    if(StringUtils.isBlank(balanceUserCoinVolume.getUserId())){
                        balanceUserCoinVolume.setUserId(e.getId());
                    }
                    BigDecimal balance = new BigDecimal(10000);
                    BigDecimal balance2 = new BigDecimal(1000);
                    if (balanceUserCoinVolume.getCoinBalance().compareTo(balance) > 0) {
                        balanceUserCoinVolume.setDayRate(balancePlatDayRateConfig.getThreeDayRate());
                    } else if (balanceUserCoinVolume.getCoinBalance().compareTo(balance2) > 0) {
                        balanceUserCoinVolume.setDayRate(balancePlatDayRateConfig.getSecondDayRate());
                    } else {
                        balanceUserCoinVolume.setDayRate(balancePlatDayRateConfig.getOneDayRate());
                    }
                    balanceUserCoinVolume.setReferId(e.getReferId());
                    balanceUserCoinVolume.setCreateDate(LocalDateTime.now());
//                    balanceUserCoinVolumeService.save(balanceUserCoinVolume);
                    balanceUserCoinVolumeService.balanceVolume(balanceUserCoinVolume,balancePlatDayRateConfig.getRewardNum());
//                    if (!CollectionUtils.isNotEmpty(flaglist)) {
//                        List<BalancePlatJackpotVolumeDetail> jackpotList = balancePlatJackpotVolumeService.findAll("MG");
//                        BalancePlatJackpotVolumeDetail jackpotDetail = new BalancePlatJackpotVolumeDetail();
//                        if (CollectionUtils.isNotEmpty(jackpotList)) {
//                            jackpotDetail = jackpotList.get(0);
//                        }
//                        if (jackpotDetail.getAllCoinIncome() != null) {
//                            jackpotDetail.setAllCoinIncome(jackpotDetail.getAllCoinIncome().add(balancePlatDayRateConfig.getRewardNum()));
//                        } else {
//                            jackpotDetail.setAllCoinIncome(balancePlatDayRateConfig.getRewardNum());
//                        }
//                        if (jackpotDetail.getRewardDate() == null) {
//                            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//                            LocalDateTime time = LocalDateTime.now();
//                            String localTime = df.format(time);
//                            LocalDateTime ldt = LocalDateTime.parse(balancePlatDayRateConfig.getRewardDateStr(), df);
//                            jackpotDetail.setRewardDate(ldt);
//                        }
//                        if (jackpotDetail.getCoinSymbol() == null) {
//                            jackpotDetail.setCoinSymbol("MG");
//                        }
//                        if (StringUtils.isNotEmpty(jackpotDetail.getId()) && !"null".equals(jackpotDetail.getId())) {
//                            balancePlatJackpotVolumeService.updateById(jackpotDetail);
//                        } else {
//                            jackpotDetail.setCreateDate(LocalDateTime.now());
//                            balancePlatJackpotVolumeService.save(jackpotDetail);
//                        }
//                    }
//                    BalanceChangeUserCoinVolume balanceChangeUserCoinVolume = new BalanceChangeUserCoinVolume();
//                    balanceChangeUserCoinVolume.setCoinNum(balanceCoinVolumeVO.getCoinNum());
//                    balanceChangeUserCoinVolume.setUserId(balanceUserCoinVolume.getUserId());
//                    balanceChangeUserCoinVolume.setCoinSymbol(balanceCoinVolumeVO.getCoinSymbol());
//                    balanceChangeUserCoinVolume.setCoinPlatSymbol("MG");
//                    balanceChangeUserCoinVolume.setFlag(0);
//                    balanceChangeUserCoinVolume.setMail(e.getMail());
//                    balanceChangeUserCoinVolume.setMobile(e.getMobile());
//                    balanceChangeUserCoinVolume.setBalanceId(balanceUserCoinVolume.getId());
//                    balanceChangeUserCoinVolumeService.save(balanceChangeUserCoinVolume);

//                    userCoinVolumeExService.updateOutcome(null, userCoinIncome2, e.getId(), balanceUserCoinVolume.getCoinSymbol(), false);
//                    Coin coinVo = coinDao.findByName(balanceUserCoinVolume.getCoinSymbol());
//                    OfflineTransferLog offlineTransferLog = new OfflineTransferLog();
//                    String id = SnowFlake.createSnowFlake().nextIdString();
//                    offlineTransferLog.setId(id);
//                    offlineTransferLog.setCreateDate(LocalDateTime.now());
//                    offlineTransferLog.setUpdateDate(LocalDateTime.now());
//                    offlineTransferLog.setUserId(e.getId());
//                    offlineTransferLog.setCoinSymbol(balanceUserCoinVolume.getCoinSymbol());
//                    offlineTransferLog.setVolume(userCoinIncome2);
//                    offlineTransferLog.setType(20);//常规账户到挖矿部落
//                    offlineTransferLog.setCoinId(coinVo.getId());
//                    offlineTransferLog.setSourceVolume(BigDecimal.ZERO);
//                    offlineTransferLogDao.insert(offlineTransferLog);
                    return GlobalMessageResponseVo.newSuccessInstance("操作成功！");
                });
    }

    /**
     * 减少余币宝资产（转出）
     *
     * @param balanceCoinVolumeVO
     * @return
     */
    @PostMapping("/balance/volume/deleteNum")
    public Mono<GlobalMessageResponseVo> deleteNum(BalanceCoinVolumeVO balanceCoinVolumeVO) {

        Mono<SecurityContext> context
                = ReactiveSecurityContextHolder.getContext();

        return context.filter(c -> Objects.nonNull(c.getAuthentication()))
                .map(s -> s.getAuthentication().getPrincipal())
                .cast(RedisSessionUser.class)
                .map(e -> {
                    BalanceUserCoinVolume balanceUserCoinVolume = new BalanceUserCoinVolume();
                    BeanUtils.copyProperties(balanceCoinVolumeVO, balanceUserCoinVolume);
                    balanceUserCoinVolume.setMail(e.getMail());
                    balanceUserCoinVolume.setMobile(e.getMobile());
                    balanceUserCoinVolume.setCoinSymbol(balanceCoinVolumeVO.getName());
                    balanceUserCoinVolume.setCoinBalance(balanceUserCoinVolume.getCoinBalance().subtract(balanceCoinVolumeVO.getCoinNum()));
                    BigDecimal balance = new BigDecimal(10000);
                    BigDecimal balance2 = new BigDecimal(1000);
                    if (balanceUserCoinVolume.getCoinBalance().compareTo(balance) > 0) {
                        balanceUserCoinVolume.setDayRate(balancePlatDayRateConfig.getThreeDayRate());
                    } else if (balanceUserCoinVolume.getCoinBalance().compareTo(balance2) > 0) {
                        balanceUserCoinVolume.setDayRate(balancePlatDayRateConfig.getSecondDayRate());
                    } else {
                        balanceUserCoinVolume.setDayRate(balancePlatDayRateConfig.getOneDayRate());
                    }
                    if (StringUtils.isNotEmpty(balanceCoinVolumeVO.getId()) && !"null".equals(balanceCoinVolumeVO.getId())) {
                        balanceUserCoinVolumeService.updateById(balanceUserCoinVolume);
                    } else {

                        balanceUserCoinVolumeService.save(balanceUserCoinVolume);
                    }
                    BalanceChangeUserCoinVolume balanceChangeUserCoinVolume = new BalanceChangeUserCoinVolume();
                    balanceChangeUserCoinVolume.setCoinNum(balanceCoinVolumeVO.getCoinNum());
                    balanceChangeUserCoinVolume.setUserId(balanceCoinVolumeVO.getUserId());
                    balanceChangeUserCoinVolume.setCoinSymbol(balanceCoinVolumeVO.getName());
                    balanceChangeUserCoinVolume.setFlag(1);
                    balanceChangeUserCoinVolume.setMail(e.getMail());
                    balanceChangeUserCoinVolume.setMobile(e.getMobile());
                    balanceChangeUserCoinVolumeService.save(balanceChangeUserCoinVolume);
                    userCoinVolumeExService.updateIncome(null, balanceCoinVolumeVO.getCoinNum(), e.getId(), balanceUserCoinVolume.getCoinSymbol(), false);
                    return GlobalMessageResponseVo.newSuccessInstance("操作成功！");
                });
    }

    /**
     * 获取用户邀请码
     *
     * @return
     */
    @GetMapping("/balance/volume/invoteCode")
    public Mono<GlobalMessageResponseVo> findUserCode() {

        Mono<SecurityContext> context
                = ReactiveSecurityContextHolder.getContext();
        return context.filter(c -> Objects.nonNull(c.getAuthentication()))
                .map(s -> s.getAuthentication().getPrincipal())
                .cast(RedisSessionUser.class)
                .map(e -> {
                    return GlobalMessageResponseVo.newSuccessInstance(e);
                });
    }

    /**
     * 查询转入记录
     *
     * @return
     */
    @GetMapping("/balance/volume/change")
    public Mono<GlobalMessageResponseVo> findChangeAll() {

        Mono<SecurityContext> context
                = ReactiveSecurityContextHolder.getContext();

        return context.filter(c -> Objects.nonNull(c.getAuthentication()))
                .map(s -> s.getAuthentication().getPrincipal())
                .cast(RedisSessionUser.class)
                .map(e -> {
                    //查询余币宝资产信息
                    List<BalanceChangeUserCoinVolume> listVolume = balanceChangeUserCoinVolumeService.findAll();
                    List<Coin> list = coinService.findAll();
                    List<BalanceChangeCoinVolumeVO> listVo = new ArrayList<>();
                    listVolume.forEach(coin -> {
                        BalanceChangeCoinVolumeVO coinVolumeVO = new BalanceChangeCoinVolumeVO();
                        BeanUtils.copyProperties(coin, coinVolumeVO);

                        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                        LocalDateTime time = coin.getCreateDate();
                        String createStr = df.format(time);
                        coinVolumeVO.setCreateStr(createStr);
                        String userName = null;
                        if (org.apache.commons.lang3.StringUtils.isNotBlank(coin.getMobile())) {
                            userName = coin.getMobile().substring(0, 3) + "******" + coin.getMobile().substring(coin.getMobile().length() - 2);
                        } else if (org.apache.commons.lang3.StringUtils.isNotBlank(coin.getMail())) {
                            int index = coin.getMail().indexOf("@");
                            if (index > 4) {
                                userName = coin.getMail().substring(0, index - 4) + "****" + coin.getMail().substring(index);
                            } else {
                                userName = coin.getMail().substring(0, 1) + "****" + coin.getMail().substring(index);
                            }

                        }
                        if (coinVolumeVO.getCoinNum() != null) {
                            coinVolumeVO.setCoinNum(coinVolumeVO.getCoinNum().setScale(2, BigDecimal.ROUND_HALF_UP));
                        }
                        coinVolumeVO.setUserName(userName);

                        listVo.add(coinVolumeVO);

                    });
                    return GlobalMessageResponseVo.newSuccessInstance(listVo);
                });
    }

    /**
     * 排行榜查询
     *
     * @return
     */
    @GetMapping("/balance/volume/rank")
    public Mono<GlobalMessageResponseVo> findByRank() {

        Mono<SecurityContext> context
                = ReactiveSecurityContextHolder.getContext();

        return context.filter(c -> Objects.nonNull(c.getAuthentication()))
                .map(s -> s.getAuthentication().getPrincipal())
                .cast(RedisSessionUser.class)
                .map(e -> {

                    int userRank = 0;
                    List<BalanceCoinVolumeVO> listVo = new ArrayList<>();
                    List<BalanceUserCoinVolume> listVolume = balanceUserCoinVolumeService.findByRank();
                    if(CollectionUtils.isNotEmpty(listVolume)){
                        int len=listVolume.size();
                        for (int i = 0; i < len; i++) {
                            BalanceCoinVolumeVO coinVolumeVO = new BalanceCoinVolumeVO();
                            BeanUtils.copyProperties(listVolume.get(i), coinVolumeVO);
                            coinVolumeVO.setOrdNum(i + 1);
                            String userName = null;
                            if (org.apache.commons.lang3.StringUtils.isNotBlank(coinVolumeVO.getMobile())) {
                                userName = coinVolumeVO.getMobile().substring(0, 3) + "******" + coinVolumeVO.getMobile().substring(coinVolumeVO.getMobile().length() - 2);
                            } else if (org.apache.commons.lang3.StringUtils.isNotBlank(coinVolumeVO.getMail())) {
                                int index = coinVolumeVO.getMail().indexOf("@");
                                if (index > 4) {
                                    userName = coinVolumeVO.getMail().substring(0, index - 4) + "****" + coinVolumeVO.getMail().substring(index);
                                } else {
                                    userName = coinVolumeVO.getMail().substring(0, 1) + "****" + coinVolumeVO.getMail().substring(index);
                                }

                            }
                            coinVolumeVO.setUserName(userName);
                            if (coinVolumeVO.getCoinBalance() != null) {
                                coinVolumeVO.setCoinBalance(coinVolumeVO.getCoinBalance().setScale(2, BigDecimal.ROUND_HALF_UP));
                            }
                            coinVolumeVO.setCoinSymbol("USDT");
                            listVo.add(coinVolumeVO);
                            if (e.getId().equals(coinVolumeVO.getUserId())) {
                                userRank = coinVolumeVO.getOrdNum();
                            }
                        }
                    }
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("userRank", userRank);
                    map.put("list", listVo);
                    return GlobalMessageResponseVo.newSuccessInstance(map);
                });
    }

    /**
     * 我的社区邀请明细
     *
     * @return
     */
    @GetMapping("/balance/volume/inviteUserList")
    public Mono<GlobalMessageResponseVo> findInviteUserList() {

        Mono<SecurityContext> context
                = ReactiveSecurityContextHolder.getContext();

        return context.filter(c -> Objects.nonNull(c.getAuthentication()))
                .map(s -> s.getAuthentication().getPrincipal())
                .cast(RedisSessionUser.class)
                .map(e -> {
                    //查询余币宝资产信息
                    List<PlatUser> users = platUserDao.findInvitesById(e.getId());
                    List<BalanceCoinVolumeVO> listVo = new ArrayList<>();
                    users.forEach(user -> {
                        BalanceCoinVolumeVO coinVolumeVO = new BalanceCoinVolumeVO();
                        BeanUtils.copyProperties(user, coinVolumeVO);
                        List<BalanceUserCoinVolume> balanceDetailList = balanceUserCoinVolumeService.findAll(user.getId());
                        //我的挖矿总额
                        BigDecimal coinBalance = BigDecimal.ZERO;
                        if (CollectionUtils.isNotEmpty(balanceDetailList)) {
                            for (BalanceUserCoinVolume userCoinVolume : balanceDetailList) {
                                BigDecimal balanceNum = userCoinVolume.getDepositValue();
                                coinBalance = coinBalance.add(balanceNum);
                            }
                        }
                        coinVolumeVO.setCoinBalance(coinBalance);
                        BigDecimal balance = new BigDecimal(10000);
                        BigDecimal balance2 = new BigDecimal(1000);
                        BigDecimal balance3 = new BigDecimal(200);
                        BigDecimal dayRate = new BigDecimal(0);
                        String positionName = "";
                        if (coinVolumeVO.getCoinBalance() != null && coinVolumeVO.getCoinBalance().compareTo(balance3) >= 0) {
                            if (coinVolumeVO.getCoinBalance().compareTo(balance) > 0) {
                                dayRate = dayRate.add(balancePlatDayRateConfig.getThreeDayRate().multiply(new BigDecimal(100)));
                                positionName = "三级仓";
                            } else if (coinVolumeVO.getCoinBalance().compareTo(balance2) > 0) {
                                dayRate = dayRate.add(balancePlatDayRateConfig.getSecondDayRate().multiply(new BigDecimal(100)));
                                positionName = "二级仓";
                            } else {
                                positionName = "一级仓";
                                dayRate = dayRate.add(balancePlatDayRateConfig.getOneDayRate().multiply(new BigDecimal(100)));
                            }
                        } else {
                            positionName = "暂无仓位";
                        }
                        coinVolumeVO.setRise(dayRate.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "%");
                        coinVolumeVO.setPositionName(positionName);

                        //社区挖矿总额
                        BigDecimal teamSumRecord = BigDecimal.ZERO;
                        //小区
                        BigDecimal teamCommunitySumRecord = BigDecimal.ZERO;
                        //社区有效用户数
                        Integer validNum = 0;
                        //一级邀请人数
                        int oneInvite = 0;
                        List<BigDecimal> childTeamSumRecordList = new ArrayList<BigDecimal>();
                        List<String> validList = new ArrayList<String>();


                        List<PlatUser> userList = platUserDao.findInvitesById(user.getId());

                        List<BalanceUserCoinVolume> childUserVolumeList = new ArrayList<BalanceUserCoinVolume>();
                        if (CollectionUtils.isNotEmpty(userList)) {
                            for (PlatUser platUser : userList) {
                                List<BalanceUserCoinVolume> userVolumeTmpList = balanceUserCoinVolumeService.findAll(platUser.getId());
                                if (CollectionUtils.isNotEmpty(userVolumeTmpList)) {
                                    childUserVolumeList.addAll(userVolumeTmpList);
                                }
                            }
                        }

                        Map<String, List<BalanceUserCoinVolume>> balanceUserCoinVolumeMap = new HashMap<String, List<BalanceUserCoinVolume>>();
                        if (CollectionUtils.isNotEmpty(childUserVolumeList)) {
                            for (BalanceUserCoinVolume balanceUserCoinVolume : childUserVolumeList) {
                                List<BalanceUserCoinVolume> balanceUserCoinCountVolumeList = balanceUserCoinVolumeMap.get(balanceUserCoinVolume.getUserId());
                                if (CollectionUtils.isNotEmpty(balanceUserCoinCountVolumeList)) {
                                    balanceUserCoinCountVolumeList.add(balanceUserCoinVolume);
                                } else {
                                    balanceUserCoinCountVolumeList = new ArrayList<BalanceUserCoinVolume>();
                                    balanceUserCoinCountVolumeList.add(balanceUserCoinVolume);
                                    balanceUserCoinVolumeMap.put(balanceUserCoinVolume.getUserId(), balanceUserCoinCountVolumeList);
                                }
                            }
                        }
                        List<BalanceUserCoinVolume> childUserVolumeAllList = new ArrayList<BalanceUserCoinVolume>();

                        for (PlatUser platUser : userList) {
                            BigDecimal childTeamSumRecord = BigDecimal.ZERO;
                            List<BalanceUserCoinVolume> countlist = balanceUserCoinVolumeMap.get(platUser.getId());
                            BigDecimal childCoinBalance = BigDecimal.ZERO;
                            if (CollectionUtils.isNotEmpty(countlist)) {
                                for (BalanceUserCoinVolume countVolume : countlist) {
                                    BigDecimal coinCount = countVolume.getDepositValue();
                                    childCoinBalance = childCoinBalance.add(coinCount);
                                }
                            }
                            if (childCoinBalance.compareTo(new BigDecimal(200)) >= 0) {
                                validList.add("1");
                                oneInvite++;
                            }
                            childTeamSumRecord = childTeamSumRecord.add(childCoinBalance);
                            BalanceUserCoinVolume balanceUserCoinVolume = new BalanceUserCoinVolume();
                            balanceUserCoinVolume.setUserId(platUser.getId());
                            balanceUserCoinVolume.setCoinBalance(childCoinBalance);
                            childUserVolumeAllList.add(balanceUserCoinVolume);
                            List<PlatUser> platChildList = platUserDao.findInvitesById(platUser.getId());
                            List<BigDecimal> childSumRecordList = new ArrayList<BigDecimal>();
                            if (CollectionUtils.isNotEmpty(platChildList)) {
                                treeCommunityUserList(platChildList, childSumRecordList, validList);
                            }
                            if (CollectionUtils.isNotEmpty(childSumRecordList)) {
                                for (BigDecimal childSum : childSumRecordList) {
                                    childTeamSumRecord = childTeamSumRecord.add(childSum);
                                }
                            }
                            teamSumRecord = teamSumRecord.add(childTeamSumRecord);
                            childTeamSumRecordList.add(childTeamSumRecord);
                        }
                        BigDecimal childMax = BigDecimal.ZERO;
                        if (CollectionUtils.isNotEmpty(childTeamSumRecordList)) {
                            childMax = Collections.max(childTeamSumRecordList);
                        }
                        teamCommunitySumRecord = teamCommunitySumRecord.add(teamSumRecord.subtract(childMax));
                        String teamLevel = "V0";
                        if (teamSumRecord != null) {
                            if (oneInvite >= 10 && teamCommunitySumRecord != null && teamCommunitySumRecord.compareTo(new BigDecimal(10000000)) >= 0) {
                                teamLevel = "V5";
                            } else if (oneInvite >= 10 && teamCommunitySumRecord != null && teamCommunitySumRecord.compareTo(new BigDecimal(5000000)) >= 0 && teamCommunitySumRecord.compareTo(new BigDecimal(10000000)) < 0) {
                                teamLevel = "V4";
                            } else if (oneInvite >= 10 && teamCommunitySumRecord != null && teamCommunitySumRecord.compareTo(new BigDecimal(1000000)) >= 0 && teamCommunitySumRecord.compareTo(new BigDecimal(5000000)) < 0) {
                                teamLevel = "V3";
                            } else if (oneInvite >= 10 && teamCommunitySumRecord != null && teamCommunitySumRecord.compareTo(new BigDecimal(300000)) >= 0 && teamCommunitySumRecord.compareTo(new BigDecimal(1000000)) < 0) {
                                teamLevel = "V2";
                            } else if (oneInvite >= 5 && teamSumRecord.compareTo(new BigDecimal(100000)) >= 0) {
                                teamLevel = "V1";
                            }
                        }
                        coinVolumeVO.setOneInvite(oneInvite);
                        coinVolumeVO.setTeamAmount(teamSumRecord);
                        coinVolumeVO.setTeamCommunityAmount(teamCommunitySumRecord);
                        coinVolumeVO.setTeamLevel(teamLevel);
                        coinVolumeVO.setValidNum(validList.size());

                        String userName = null;
                        if (org.apache.commons.lang3.StringUtils.isNotBlank(user.getMobile())) {
                            userName = user.getMobile().substring(0, 3) + "******" + user.getMobile().substring(user.getMobile().length() - 2);
                        } else if (org.apache.commons.lang3.StringUtils.isNotBlank(user.getMail())) {
                            int index = user.getMail().indexOf("@");
                            if (index > 4) {
                                userName = user.getMail().substring(0, index - 4) + "****" + user.getMail().substring(index);
                            } else {
                                userName = user.getMail().substring(0, 1) + "****" + user.getMail().substring(index);
                            }

                        }
                        coinVolumeVO.setUserName(userName);
                        coinVolumeVO.setCoinSymbol("USDT");
                        coinVolumeVO.setCoinPlatSymbol("MG");
                        listVo.add(coinVolumeVO);

                    });
                    return GlobalMessageResponseVo.newSuccessInstance(listVo);
                });
    }

    /**
     * 查询活动参与人数
     *
     * @return
     */
    @GetMapping("/balance/volume/countNum")
    public Mono<GlobalMessageResponseVo> findByCountNum() {

        Mono<SecurityContext> context
                = ReactiveSecurityContextHolder.getContext();

        return context.filter(c -> Objects.nonNull(c.getAuthentication()))
                .map(s -> s.getAuthentication().getPrincipal())
                .cast(RedisSessionUser.class)
                .map(e -> {
                    int countNum = balanceUserCoinVolumeService.findByCountNum();
                    return GlobalMessageResponseVo.newSuccessInstance(countNum);
                });
    }

    /**
     * 查询登录用户转入记录
     *
     * @return
     */
    @GetMapping("/balance/volume/userChange")
    public Mono<GlobalMessageResponseVo> findUserChange() {

        Mono<SecurityContext> context
                = ReactiveSecurityContextHolder.getContext();

        return context.filter(c -> Objects.nonNull(c.getAuthentication()))
                .map(s -> s.getAuthentication().getPrincipal())
                .cast(RedisSessionUser.class)
                .map(e -> {
                    //查询余币宝资产信息
                    List<BalanceChangeUserCoinVolume> listVolume = balanceChangeUserCoinVolumeService.findChangeByUserId(e.getId());
                    List<BalanceChangeCoinVolumeVO> listVo = new ArrayList<>();
                    listVolume.forEach(coin -> {
                        BalanceChangeCoinVolumeVO coinVolumeVO = new BalanceChangeCoinVolumeVO();
                        BeanUtils.copyProperties(coin, coinVolumeVO);

                        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                        LocalDateTime time = coin.getCreateDate();
                        String createStr = df.format(time);
                        coinVolumeVO.setCreateStr(createStr);
                        coinVolumeVO.setCreateTime(time.toInstant(ZoneOffset.of("+8")).toEpochMilli());
                        String userName = null;
                        if (org.apache.commons.lang3.StringUtils.isNotBlank(coin.getMobile())) {
                            userName = coin.getMobile().substring(0, 3) + "******" + coin.getMobile().substring(coin.getMobile().length() - 2);
                        } else if (org.apache.commons.lang3.StringUtils.isNotBlank(coin.getMail())) {
                            int index = coin.getMail().indexOf("@");
                            if (index > 4) {
                                userName = coin.getMail().substring(0, index - 4) + "****" + coin.getMail().substring(index);
                            } else {
                                userName = coin.getMail().substring(0, 1) + "****" + coin.getMail().substring(index);
                            }

                        }
                        coinVolumeVO.setUserName(userName);
                        if (coinVolumeVO.getCoinNum() != null) {
                            coinVolumeVO.setCoinNum(coinVolumeVO.getCoinNum().setScale(2, BigDecimal.ROUND_HALF_UP));
                        }

                        if (coinVolumeVO.getAccumulIncome() != null) {
                            coinVolumeVO.setAccumulIncome(coinVolumeVO.getAccumulIncome().setScale(2, BigDecimal.ROUND_HALF_UP));
                        }
                        listVo.add(coinVolumeVO);

                    });
                    return GlobalMessageResponseVo.newSuccessInstance(listVo);
                });
    }

    /**
     * 减少余币宝资产（按订单转出）
     *
     * @param balanceCoinVolumeVO
     * @return
     */
    @PostMapping("/balance/volume/takeOutIncome")
    public Mono<GlobalMessageResponseVo> takeOutIncome(BalanceChangeCoinVolumeVO balanceChangeCoinVolumeVO) {

        Mono<SecurityContext> context
                = ReactiveSecurityContextHolder.getContext();

        return context.filter(c -> Objects.nonNull(c.getAuthentication()))
                .map(s -> s.getAuthentication().getPrincipal())
                .cast(RedisSessionUser.class)
                .map(e -> {
                    if (StringUtils.isBlank(e.getExPassword())) {
                        return GlobalMessageResponseVo.newErrorInstance("请先设置交易密码");
                    }
                    if (StringUtils.isBlank(balanceChangeCoinVolumeVO.getExPassword())) {
                        return GlobalMessageResponseVo.newErrorInstance("请输入交易密码");
                    }
                    String exDecryPassword = RsaUtils.decryptByPrivateKey(balanceChangeCoinVolumeVO.getExPassword(), RsaUtils.DEFAULT_PRIVATE_KEY);
                    boolean result = passwordEncoder.matches(exDecryPassword, e.getExPassword());
                    if (!result) {
                        return GlobalMessageResponseVo.newErrorInstance("请输入正确的交易密码");
                    }
                    BalanceChangeUserCoinVolume balanceUserCoinVolume = new BalanceChangeUserCoinVolume();
                    BeanUtils.copyProperties(balanceChangeCoinVolumeVO, balanceUserCoinVolume);
                    balanceUserCoinVolume.setMobile(e.getMobile());
                    balanceUserCoinVolume.setMail(e.getMail());
//                    balanceChangeUserCoinVolumeService.save(balanceUserCoinVolume);
//                    BalanceChangeUserCoinVolume balanceUserCoinVolumeUpdate = new BalanceChangeUserCoinVolume();
//                    BeanUtils.copyProperties(balanceChangeCoinVolumeVO, balanceUserCoinVolumeUpdate);
//                    balanceUserCoinVolumeUpdate.setTakeOutDate(LocalDateTime.now());
//                    balanceChangeUserCoinVolumeService.updateById(balanceUserCoinVolumeUpdate);
//                    balanceUserCoinVolumeService.deleteByBalanceId(balanceChangeCoinVolumeVO.getId());
                    LocalDateTime localDateTimeNow = LocalDateTime.now();
                    Instant instant = Instant.ofEpochMilli(balanceChangeCoinVolumeVO.getCreateTime());
                    ZoneId zone = ZoneId.systemDefault();
                    LocalDateTime createDate = LocalDateTime.ofInstant(instant, zone);
                    Duration duration = Duration.between(createDate, localDateTimeNow);
                    long longTime = duration.toDays();
                    BigDecimal coinNum = balanceChangeCoinVolumeVO.getCoinNum();
                    if (longTime < 30) {
                        coinNum = coinNum.subtract(coinNum.multiply(new BigDecimal(0.05)));
                    }
                    balanceUserCoinVolumeService.balanceOutVolume(balanceUserCoinVolume,coinNum);
//                    Coin coinVo = coinDao.findByName(balanceUserCoinVolume.getCoinSymbol());
//                    OfflineTransferLog offlineTransferLog = new OfflineTransferLog();
//                    String id = SnowFlake.createSnowFlake().nextIdString();
//                    offlineTransferLog.setId(id);
//                    offlineTransferLog.setCreateDate(LocalDateTime.now());
//                    offlineTransferLog.setUpdateDate(LocalDateTime.now());
//                    offlineTransferLog.setUserId(e.getId());
//                    offlineTransferLog.setCoinSymbol(balanceUserCoinVolume.getCoinSymbol());
//                    offlineTransferLog.setVolume(coinNum);
//                    offlineTransferLog.setType(21);//挖矿部落到常规账户
//                    offlineTransferLog.setCoinId(coinVo.getId());
//                    offlineTransferLog.setSourceVolume(BigDecimal.ZERO);
//                    offlineTransferLogDao.insert(offlineTransferLog);
//                    userCoinVolumeExService.updateIncome(null, coinNum, balanceChangeCoinVolumeVO.getUserId(), balanceChangeCoinVolumeVO.getCoinSymbol(), false);
                    return GlobalMessageResponseVo.newSuccessInstance("操作成功！");
                });
    }

    /**
     * 查询登录用户财务明细
     *
     * @return
     */
    @GetMapping("/balance/volume/financeDetails")
    public Mono<GlobalMessageResponseVo> findUserFinanceDetails(UserFinanceQuery requestQuery) {

        Mono<SecurityContext> context
                = ReactiveSecurityContextHolder.getContext();

        return context.filter(c -> Objects.nonNull(c.getAuthentication()))
                .map(s -> s.getAuthentication().getPrincipal())
                .cast(RedisSessionUser.class)
                .map(e -> {
                    requestQuery.setUserId(e.getId());
                    if ("0".equals(requestQuery.getRewardType())) {
                        requestQuery.setRewardType(null);
                    }
                    ResponsePage<BalanceUserVolumeIncomeDetailVO> responsePage = new ResponsePage<>();
                    Page<BalanceUserVolumeIncomeDetailVO> page = PageHelper.startPage(requestQuery.getCurrentPage(), requestQuery.getShowCount());
                    //查询余币宝资产信息
                    List<BalanceUserVolumeIncomeDetail> listVolume = balanceUserVolumeIncomeDetailService.findAll(requestQuery);
                    List<BalanceUserVolumeIncomeDetailVO> listVo = new ArrayList<>();
                    listVolume.forEach(coin -> {
                        BalanceUserVolumeIncomeDetailVO coinVolumeVO = new BalanceUserVolumeIncomeDetailVO();
                        BeanUtils.copyProperties(coin, coinVolumeVO);

                        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                        LocalDateTime time = coin.getIncomeDate();
                        String createStr = df.format(time);
                        coinVolumeVO.setCreateStr(createStr);
                        if (coinVolumeVO.getDetailReward() != null) {
                            coinVolumeVO.setDetailReward(coinVolumeVO.getDetailReward().setScale(2, BigDecimal.ROUND_HALF_UP));
                        }
                        listVo.add(coinVolumeVO);

                    });
                    responsePage.setCount(page.getTotal());
                    responsePage.setList(listVo);
                    return GlobalMessageResponseVo.newSuccessInstance(responsePage);
                });
    }

    /**
     * 查询登录用户存取记录
     *
     * @return
     */
    @GetMapping("/balance/volume/accessList")
    public Mono<GlobalMessageResponseVo> findUserAccessList() {
        Mono<SecurityContext> context
                = ReactiveSecurityContextHolder.getContext();

        return context.filter(c -> Objects.nonNull(c.getAuthentication()))
                .map(s -> s.getAuthentication().getPrincipal())
                .cast(RedisSessionUser.class)
                .map(e -> {
                    //查询余币宝资产信息
                    List<BalanceChangeUserCoinVolume> listVolume = balanceChangeUserCoinVolumeService.findChangeAllByUserId(e.getId());

                    List<BalanceChangeCoinVolumeVO> listVo = new ArrayList<>();
                    listVolume.forEach(coin -> {
                        BalanceChangeCoinVolumeVO coinVolumeVO = new BalanceChangeCoinVolumeVO();
                        BeanUtils.copyProperties(coin, coinVolumeVO);
                        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                        LocalDateTime time = coin.getCreateDate();
                        LocalDateTime takeOutTime = coin.getTakeOutDate();
                        String createStr = df.format(time);
                        coinVolumeVO.setCreateStr(createStr);
                        if (takeOutTime != null) {
                            String takeOutTimeStr = df.format(takeOutTime);
                            coinVolumeVO.setTakeOutTimeStr(takeOutTimeStr);
                            // coinVolumeVO.setCreateStr(takeOutTimeStr);
                        }
                        if (coinVolumeVO.getCoinNum() != null) {
                            coinVolumeVO.setCoinNum(coinVolumeVO.getCoinNum().setScale(2, BigDecimal.ROUND_HALF_UP));
                        }
                        listVo.add(coinVolumeVO);
                    });
                    return GlobalMessageResponseVo.newSuccessInstance(listVo);
                });
    }

    /**
     * 查询奖池金额和开奖时间
     *
     * @return
     */
    @GetMapping("/balance/volume/jackpotIncome")
    public Mono<GlobalMessageResponseVo> findBySumJackpotIncome() {

        BalancePlatJackpotVolumeDetail jackpotDetail = new BalancePlatJackpotVolumeDetail();
        List<BalancePlatJackpotVolumeDetail> jackpotList = balancePlatJackpotVolumeService.findAll("MG");
        if (CollectionUtils.isNotEmpty(jackpotList)) {
            jackpotDetail = jackpotList.get(0);
        }
        if (jackpotDetail.getAllCoinIncome() == null) {
            jackpotDetail.setAllCoinIncome(BigDecimal.ZERO);
        } else {
            jackpotDetail.setAllCoinIncome(jackpotDetail.getAllCoinIncome().setScale(2, BigDecimal.ROUND_HALF_UP));
        }

        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime time = LocalDateTime.now();
        LocalDateTime ldt = LocalDateTime.parse(balancePlatDayRateConfig.getRewardDateStr(), df);
        Duration duration = Duration.between(ldt, time);
        long days = duration.toDays(); //相差的天数
        long hours = duration.toHours();//相差的小时数
        long minutes = duration.toMinutes();//相差的分钟数
        long millis = duration.toMillis();//相差毫秒数
        if (millis < 0) {
            duration = Duration.between(time, ldt);
            millis = duration.toMillis();//相差毫秒数
        } else {
            long modDays = days % 10;
            long chaMillis = millis - days * 24 * 60 * 60 * 1000;
//            long modHours=hours%6;
//            long chaMillis=millis-hours*60*60*1000;
            millis = (10 - modDays) * 24 * 60 * 60 * 1000 - chaMillis;
        }
        int day = Math.round(millis / 1000 / 60 / 60 / 24);
        // 时
        int hour = Math.round(millis / 1000 / 60 / 60 % 24);
        // 分
        int minute = Math.round(millis / 1000 / 60 % 60);
        // 秒
        int second = Math.round(millis / 1000 % 60);

        System.out.println(String.format("%s天%s时%s分%s秒", day, hour, minute, second));

        jackpotDetail.setRewardTime(millis);
        jackpotDetail.setCoinSymbol("MG");
        return Mono.just(GlobalMessageResponseVo.newSuccessInstance(jackpotDetail));
    }

    /**
     * 根据用户查询所有币种余额收益信息 支持平台币
     *
     * @return
     */
    @GetMapping("/balance/volume/digIncomeInfo")
    public Mono<GlobalMessageResponseVo> findIncomeInfo() {

        Mono<SecurityContext> context
                = ReactiveSecurityContextHolder.getContext();

        return context.filter(c -> Objects.nonNull(c.getAuthentication()))
                .map(s -> s.getAuthentication().getPrincipal())
                .cast(RedisSessionUser.class)
                .map(e -> {
                    BalanceCoinVolumeVO listVo = new BalanceCoinVolumeVO();
                    //查询余币宝资产信息
                    List<BalanceUserCoinCountVolume> listVolume = balanceUserCoinCountVolumeService.findAll(e.getId());
                    if (CollectionUtils.isNotEmpty(listVolume)) {
                        BeanUtils.copyProperties(listVolume.get(0), listVo);
                    }

                    List<BalanceUserCoinVolume> balanceDetailList = balanceUserCoinVolumeService.findAll(e.getId());
                    //我的挖矿总额
                    BigDecimal coinBalance = BigDecimal.ZERO;
                    if (CollectionUtils.isNotEmpty(balanceDetailList)) {
                        for (BalanceUserCoinVolume userCoinVolume : balanceDetailList) {
//                            TradePairVO tradePair = tradePairMap.get(userCoinVolume.getCoinSymbol());
//                            BigDecimal balanceNum = userCoinVolume.getCoinBalance();
//                            if (tradePair != null && tradePair.getLatestPrice() != null) {
//                                BigDecimal coinLastPirce = tradePair.getLatestPrice();
//                                if (coinLastPirce != null) {
//                                    balanceNum = balanceNum.multiply(coinLastPirce);
//                                }
//                            }
                            coinBalance = coinBalance.add(userCoinVolume.getDepositValue());
                        }
                    }
                    listVo.setCoinBalance(coinBalance);
                    BigDecimal balance = new BigDecimal(10000);
                    BigDecimal balance2 = new BigDecimal(1000);
                    BigDecimal balance3 = new BigDecimal(200);
                    BigDecimal dayRate = new BigDecimal(0);
                    String positionName = "";
                    if (listVo.getCoinBalance() != null && listVo.getCoinBalance().compareTo(balance3) >= 0) {
                        if (listVo.getCoinBalance().compareTo(balance) > 0) {
                            dayRate = dayRate.add(balancePlatDayRateConfig.getThreeDayRate().multiply(new BigDecimal(100)));
                            positionName = "三级仓";
                        } else if (listVo.getCoinBalance().compareTo(balance2) > 0) {
                            dayRate = dayRate.add(balancePlatDayRateConfig.getSecondDayRate().multiply(new BigDecimal(100)));
                            positionName = "二级仓";
                        } else {
                            positionName = "一级仓";
                            dayRate = dayRate.add(balancePlatDayRateConfig.getOneDayRate().multiply(new BigDecimal(100)));
                        }
                    } else {
                        positionName = "暂无仓位";
                    }
                    listVo.setRise(dayRate.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "%");
                    listVo.setPositionName(positionName);
                    //一级邀请人数
                    int oneInvite = 0;
                    //社区挖矿总额
                    BigDecimal teamSumRecord = BigDecimal.ZERO;
                    //小区区挖矿总额
                    BigDecimal teamCommunitySumRecord = BigDecimal.ZERO;
                    //社区有效用户数
                    Integer validNum = 0;
                    List<BigDecimal> childTeamSumRecordList = new ArrayList<BigDecimal>();
                    List<String> validList = new ArrayList<String>();
                    List<BalanceUserCoinVolume> childUserVolumeList = balanceUserCoinVolumeService.findInvitesByUserId(e.getId());
                    Map<String, List<BalanceUserCoinVolume>> balanceUserCoinVolumeMap = new HashMap<String, List<BalanceUserCoinVolume>>();
                    if (CollectionUtils.isNotEmpty(childUserVolumeList)) {
                        for (BalanceUserCoinVolume balanceUserCoinVolume : childUserVolumeList) {
                            List<BalanceUserCoinVolume> balanceUserCoinCountVolumeList = balanceUserCoinVolumeMap.get(balanceUserCoinVolume.getUserId());
                            if (CollectionUtils.isNotEmpty(balanceUserCoinCountVolumeList)) {
                                balanceUserCoinCountVolumeList.add(balanceUserCoinVolume);
                            } else {
                                balanceUserCoinCountVolumeList = new ArrayList<BalanceUserCoinVolume>();
                                balanceUserCoinCountVolumeList.add(balanceUserCoinVolume);
                                balanceUserCoinVolumeMap.put(balanceUserCoinVolume.getUserId(), balanceUserCoinCountVolumeList);
                            }
                        }
                    }
                    List<BalanceUserCoinVolume> childUserVolumeAllList = new ArrayList<BalanceUserCoinVolume>();

                    for (String key : balanceUserCoinVolumeMap.keySet()) {
                        List<BigDecimal> childSumRecordList = new ArrayList<BigDecimal>();
                        BigDecimal childTeamSumRecord = BigDecimal.ZERO;
                        List<BalanceUserCoinVolume> countlist = balanceUserCoinVolumeMap.get(key);
                        BigDecimal childCoinBalance = BigDecimal.ZERO;
                        if (CollectionUtils.isNotEmpty(countlist)) {
                            for (BalanceUserCoinVolume countVolume : countlist) {
                                BigDecimal coinCount = countVolume.getDepositValue();
                                childCoinBalance = childCoinBalance.add(coinCount);
                            }
                        }
                        if (childCoinBalance.compareTo(new BigDecimal(200)) >= 0) {
                            oneInvite++;
                            validList.add("1");
                        }
                        childTeamSumRecord = childTeamSumRecord.add(childCoinBalance);
                        BalanceUserCoinVolume balanceUserCoinVolume = new BalanceUserCoinVolume();
                        balanceUserCoinVolume.setUserId(key);
                        balanceUserCoinVolume.setCoinBalance(childCoinBalance);
                        childUserVolumeAllList.add(balanceUserCoinVolume);
                        List<PlatUser> platChildList = platUserDao.findInvitesById(key);
                        if (CollectionUtils.isNotEmpty(platChildList)) {
                            treeCommunityUserList(platChildList,childSumRecordList, validList);
                        }
                        if (CollectionUtils.isNotEmpty(childSumRecordList)) {
                            for (BigDecimal childSum : childSumRecordList) {
                                childTeamSumRecord = childTeamSumRecord.add(childSum);
                            }
                        }
                        teamSumRecord = teamSumRecord.add(childTeamSumRecord);
                        childTeamSumRecordList.add(childTeamSumRecord);
                    }
                    BigDecimal childMax = BigDecimal.ZERO;
                    if (CollectionUtils.isNotEmpty(childTeamSumRecordList)) {
                        childMax = Collections.max(childTeamSumRecordList);
                    }

                    teamCommunitySumRecord = teamCommunitySumRecord.add(teamSumRecord.subtract(childMax));
                    String teamLevel = "V0";
                    if (teamSumRecord != null) {
                        if (oneInvite >= 10 && teamCommunitySumRecord != null && teamCommunitySumRecord.compareTo(new BigDecimal(10000000)) >= 0) {
                            teamLevel = "V5";
                        } else if (oneInvite >= 10 && teamCommunitySumRecord != null && teamCommunitySumRecord.compareTo(new BigDecimal(5000000)) >= 0 && teamCommunitySumRecord.compareTo(new BigDecimal(10000000)) < 0) {
                            teamLevel = "V4";
                        } else if (oneInvite >= 10 && teamCommunitySumRecord != null && teamCommunitySumRecord.compareTo(new BigDecimal(1000000)) >= 0 && teamCommunitySumRecord.compareTo(new BigDecimal(5000000)) < 0) {
                            teamLevel = "V3";
                        } else if (oneInvite >= 10 && teamCommunitySumRecord != null && teamCommunitySumRecord.compareTo(new BigDecimal(300000)) >= 0 && teamCommunitySumRecord.compareTo(new BigDecimal(1000000)) < 0) {
                            teamLevel = "V2";
                        } else if (oneInvite >= 5 && teamSumRecord.compareTo(new BigDecimal(100000)) >= 0) {
                            teamLevel = "V1";
                        }
                    }
                    listVo.setOneInvite(oneInvite);
                    listVo.setTeamAmount(teamSumRecord);
                    listVo.setTeamCommunityAmount(teamCommunitySumRecord);
                    listVo.setTeamLevel(teamLevel);
                    listVo.setValidNum(validList.size());
                    if (listVo.getCoinSymbol() == null) {
                        listVo.setCoinSymbol("USDT");
                    }
                    if (listVo.getCoinPlatSymbol() == null) {
                        listVo.setCoinPlatSymbol("MG");
                    }
                    if (listVo.getCoinBalance() != null) {
                        listVo.setCoinBalance(listVo.getCoinBalance().setScale(2, BigDecimal.ROUND_HALF_UP));
                    } else {
                        listVo.setCoinBalance(BigDecimal.ZERO);
                    }
                    //
                    if (listVo.getYesterdayIncome() != null) {
                        listVo.setYesterdayIncome(listVo.getYesterdayIncome().setScale(2, BigDecimal.ROUND_HALF_UP));
                    } else {
                        listVo.setYesterdayIncome(BigDecimal.ZERO);
                    }
                    //
                    if (listVo.getYesterdayReward() != null) {
                        listVo.setYesterdayReward(listVo.getYesterdayReward().setScale(2, BigDecimal.ROUND_HALF_UP));
                    } else {
                        listVo.setYesterdayReward(BigDecimal.ZERO);
                    }
                    //
                    if (listVo.getTeamAmount() != null) {
                        listVo.setTeamAmount(listVo.getTeamAmount().setScale(2, BigDecimal.ROUND_HALF_UP));
                    } else {
                        listVo.setTeamAmount(BigDecimal.ZERO);
                    }
                    //
                    if (listVo.getTeamCommunityAmount() != null) {
                        listVo.setTeamCommunityAmount(listVo.getTeamCommunityAmount().setScale(2, BigDecimal.ROUND_HALF_UP));
                    } else {
                        listVo.setTeamCommunityAmount(BigDecimal.ZERO);
                    }
                    //
                    if (listVo.getAccumulIncome() != null) {
                        listVo.setAccumulIncome(listVo.getAccumulIncome().setScale(2, BigDecimal.ROUND_HALF_UP));
                    } else {
                        listVo.setAccumulIncome(BigDecimal.ZERO);
                    }
                    //
                    if (listVo.getAccumulReward() != null) {
                        listVo.setAccumulReward(listVo.getAccumulReward().setScale(2, BigDecimal.ROUND_HALF_UP));
                    } else {
                        listVo.setAccumulReward(BigDecimal.ZERO);
                    }
                    //

                    if (listVo.getTeamLevel() == null) {
                        listVo.setTeamLevel("V0");
                    }
                    UserCoinVolume userVolume = userCoinVolumeExService.findByUserIdAndCoinSymbol(e.getId(), listVo.getCoinPlatSymbol());
                    if (userVolume != null) {
                        listVo.setUserSurplus(userVolume.getVolume());
                    }
                    if (listVo.getUserSurplus() != null) {
                        listVo.setUserSurplus(listVo.getUserSurplus().setScale(2, BigDecimal.ROUND_HALF_UP));
                    } else {
                        listVo.setUserSurplus(BigDecimal.ZERO);
                    }

                    return GlobalMessageResponseVo.newSuccessInstance(listVo);
                });
    }

    /**
     * 资产转出时，验证交易密码
     *
     * @return
     */
    @PostMapping("/balance/volume/vaildExPassword")
    public Mono<GlobalMessageResponseVo> vaildExPassword(PlatUserVO platUserVO) {

        Mono<SecurityContext> context = ReactiveSecurityContextHolder.getContext();

        return context.filter(c -> Objects.nonNull(c.getAuthentication()))
                .map(s -> s.getAuthentication().getPrincipal())
                .cast(RedisSessionUser.class)
                .map(e -> {
                    if (StringUtils.isBlank(e.getExPassword())) {
                        return GlobalMessageResponseVo.newErrorInstance("请先设置交易密码");
                    }
                    String exDecryPassword = RsaUtils.decryptByPrivateKey(platUserVO.getExPassword(), RsaUtils.DEFAULT_PRIVATE_KEY);
                    boolean result = passwordEncoder.matches(exDecryPassword, e.getExPassword());
                    if (!result) {
                        return GlobalMessageResponseVo.newErrorInstance("请输入正确的交易密码");
                    }
                    return GlobalMessageResponseVo.newSuccessInstance("交易密码验证成功");
                });
    }

    public void treeCommunityUserList(List<PlatUser> userList,List childSumRecordList, List validList) {
        List<BalanceUserCoinVolume> userVolumeList = new ArrayList<BalanceUserCoinVolume>();
        for (PlatUser platUser : userList) {
            List<BalanceUserCoinVolume> userVolumeTmpList = balanceUserCoinVolumeService.findAll(platUser.getId());
            if (CollectionUtils.isNotEmpty(userVolumeTmpList)) {
                userVolumeList.addAll(userVolumeTmpList);
            }
        }
        Map<String, List<BalanceUserCoinVolume>> balanceUserCoinVolumeMap = new HashMap<String, List<BalanceUserCoinVolume>>();
        if (CollectionUtils.isNotEmpty(userVolumeList)) {
            for (BalanceUserCoinVolume balanceUserCoinVolume : userVolumeList) {
                List<BalanceUserCoinVolume> balanceUserCoinCountVolumeList = balanceUserCoinVolumeMap.get(balanceUserCoinVolume.getUserId());
                if (CollectionUtils.isNotEmpty(balanceUserCoinCountVolumeList)) {
                    balanceUserCoinCountVolumeList.add(balanceUserCoinVolume);
                } else {
                    balanceUserCoinCountVolumeList = new ArrayList<BalanceUserCoinVolume>();
                    balanceUserCoinCountVolumeList.add(balanceUserCoinVolume);
                    balanceUserCoinVolumeMap.put(balanceUserCoinVolume.getUserId(), balanceUserCoinCountVolumeList);
                }
            }
        }
        for (PlatUser platUser : userList) {

            List<BalanceUserCoinVolume> countlist = balanceUserCoinVolumeMap.get(platUser.getId());
            BigDecimal childCoinBalance = BigDecimal.ZERO;
            if (CollectionUtils.isNotEmpty(countlist)) {
                for (BalanceUserCoinVolume countVolume : countlist) {
//                    BigDecimal lastPrice = BigDecimal.ZERO;
//                    BigDecimal coinCount = countVolume.getCoinBalance();
//                    TradePairVO tradePair = tradePairMap.get(countVolume.getCoinSymbol());
//                    if (tradePair != null && tradePair.getLatestPrice() != null) {
//                        coinCount = lastPrice.multiply(countVolume.getCoinBalance());
//                    }
                    BigDecimal coinCount = countVolume.getDepositValue();
                    childCoinBalance = childCoinBalance.add(coinCount);
                }
            }
            if (childCoinBalance.compareTo(new BigDecimal(200)) >= 0) {
                validList.add("1");
            }


            childSumRecordList.add(childCoinBalance);
            List<PlatUser> platChildList = platUserDao.findInvitesById(platUser.getId());
            if (CollectionUtils.isNotEmpty(platChildList)) {
                treeCommunityUserList(platChildList, childSumRecordList, validList);
            }
        }

    }

    /**
     * @return
     */
    @GetMapping("/balance/volume/coinList")
    public Mono<GlobalMessageResponseVo> findCoinList() {
        Mono<SecurityContext> context
                = ReactiveSecurityContextHolder.getContext();

        return context.filter(c -> Objects.nonNull(c.getAuthentication()))
                .map(s -> s.getAuthentication().getPrincipal())
                .cast(RedisSessionUser.class)
                .map(e -> {
                    List<Coin> listVo = new ArrayList<Coin>();
                    String[] coinArr = balancePlatDayRateConfig.getCoinSmybol().split(",");
                    for (String coinName : coinArr) {
                        Coin coinVo = coinDao.findByName(coinName);
                        if (coinVo != null) {
                            listVo.add(coinVo);
                        }
                    }

                    return GlobalMessageResponseVo.newSuccessInstance(listVo);
                });
    }

    /**
     * 主动发起计算收益 for test
     *
     * @return
     */
    @GetMapping("/balance/count")
    public void count() {
        //静态收益和平级奖的利率支持配置
        Map<String, BigDecimal> dayRateMap=new HashMap<String, BigDecimal>();
        dayRateMap.put("oneDayRate",balancePlatDayRateConfig.getOneDayRate());
        dayRateMap.put("secondDayRate",balancePlatDayRateConfig.getSecondDayRate());
        dayRateMap.put("threeDayRate",balancePlatDayRateConfig.getThreeDayRate());
        dayRateMap.put("equalReward",balancePlatDayRateConfig.getEqualReward());
        //每天收益和奖励计算
        Map<String, List<TradePairVO>>  allTrade=platDataHandler.buildAllTradePair();
        Map<String,TradePairVO>  tradePairMap=new HashMap<String,TradePairVO>();
        if(allTrade !=null && allTrade.size()>0){
            for(String key : allTrade.keySet()){
                List<TradePairVO> list=allTrade.get(key);
                for (TradePairVO vo:list){
                    tradePairMap.put(vo.getCoinOther(),vo);
                }
            }
        }
        BigDecimal platPrice=BigDecimal.ZERO;
        TradePairVO tradePair=tradePairMap.get("MG");
        if(tradePair!=null && tradePair.getLatestPrice() != null && tradePair.getLatestPrice().compareTo(BigDecimal.ZERO)>0){
            platPrice=platPrice.add(tradePair.getLatestPrice());
        }
        if(platPrice.compareTo(BigDecimal.ZERO)<=0){
            platPrice=platPrice.add(balanceUserCoinVolumeDetailService.findPriceByUpdateDate());
        }
        balanceUserCoinVolumeDetailService.balanceIncomeDetailNew(dayRateMap,platPrice);
        balanceUserCoinVolumeDetailService.balanceIncomeCount();
        BalancePlatCoinPriceVolume balancePlat=new BalancePlatCoinPriceVolume();
        balancePlat.setPrice(platPrice);
        balancePlat.setCoinPlatSymbol("MG");
        balanceUserCoinVolumeDetailService.insertPlatPrice(balancePlat);
    }

//    @GetMapping("/balance/jackpot")
//    public void balanceJackpotIncomeCount() {
//        Map<String, List<TradePairVO>> allTrade = platDataHandler.buildAllTradePair();
//        Map<String, TradePairVO> tradePairMap = new HashMap<String, TradePairVO>();
//        if (allTrade != null && allTrade.size() > 0) {
//            for (String key : allTrade.keySet()) {
//                List<TradePairVO> list = allTrade.get(key);
//                for (TradePairVO vo : list) {
//                    tradePairMap.put(vo.getCoinOther(), vo);
//                }
//            }
//        }
//        balanceUserCoinVolumeDetailService.balanceJackpotIncomeCount(tradePairMap);
//    }


}

