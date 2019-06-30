package com.biao.web.controller.balance;

import com.biao.config.BalancePlatDayRateConfig;
import com.biao.config.sercurity.RedisSessionUser;
import com.biao.entity.*;
import com.biao.entity.balance.BalanceChangeUserCoinVolume;
import com.biao.entity.balance.BalanceUserCoinVolume;
import com.biao.entity.balance.BalanceUserCoinVolumeDetail;
import com.biao.entity.balance.BalanceUserVolumeIncomeDetail;
import com.biao.mapper.PlatUserDao;
import com.biao.pojo.GlobalMessageResponseVo;
import com.biao.pojo.RequestQuery;
import com.biao.pojo.ResponsePage;
import com.biao.query.UserFinanceQuery;
import com.biao.reactive.data.mongo.service.TradeDetailService;
import com.biao.service.*;
import com.biao.service.balance.BalanceChangeUserCoinVolumeService;
import com.biao.service.balance.BalanceUserCoinVolumeDetailService;
import com.biao.service.balance.BalanceUserCoinVolumeService;
import com.biao.service.balance.BalanceUserVolumeIncomeDetailService;
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
    private BalanceChangeUserCoinVolumeService balanceChangeUserCoinVolumeService;

    @Autowired
    private BalanceUserCoinVolumeDetailService balanceUserCoinVolumeDetailService;

    @Autowired
    private BalanceUserVolumeIncomeDetailService balanceUserVolumeIncomeDetailService;

    @Autowired
    private OrderService orderService;

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

    /**
     * 根据用户查询所有币种余额收益信息
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
                            BigDecimal balance = new BigDecimal(5000);
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
     * @param balanceCoinVolumeVO
     * @return
     */
    @PostMapping("/balance/volume/add")
    public Mono<GlobalMessageResponseVo> addBalance(BalanceCoinVolumeVO balanceCoinVolumeVO) {

        Mono<SecurityContext> context
                = ReactiveSecurityContextHolder.getContext();

        return context.filter(c -> Objects.nonNull(c.getAuthentication()))
                .map(s -> s.getAuthentication().getPrincipal())
                .cast(RedisSessionUser.class)
                .map(e -> {
                    BalanceUserCoinVolume  balanceUserCoinVolume=new BalanceUserCoinVolume();
                    System.out.println("用户ID："+balanceCoinVolumeVO.getUserId()+"-----币种："+balanceCoinVolumeVO.getName());
                    List<BalanceUserCoinVolume> listVolume = balanceUserCoinVolumeService.findByUserIdAndCoin(balanceCoinVolumeVO.getUserId(),balanceCoinVolumeVO.getName());
                            if (CollectionUtils.isNotEmpty(listVolume)) {
                                balanceUserCoinVolume=listVolume.get(0);
                            }
                    BeanUtils.copyProperties(balanceCoinVolumeVO,balanceUserCoinVolume );
                    balanceUserCoinVolume.setMail(e.getMail());
                    balanceUserCoinVolume.setMobile(e.getMobile());
                    balanceUserCoinVolume.setCoinSymbol(balanceCoinVolumeVO.getName());
                    if(balanceUserCoinVolume.getCoinBalance() !=null){
                        balanceUserCoinVolume.setCoinBalance(balanceUserCoinVolume.getCoinBalance().add(balanceCoinVolumeVO.getCoinNum()));
                    }else{
                        balanceUserCoinVolume.setCoinBalance(balanceCoinVolumeVO.getCoinNum());
                    }
                    BigDecimal balance=new BigDecimal(5000);
                    BigDecimal balance2=new BigDecimal(1000);
                    if(balanceUserCoinVolume.getCoinBalance().compareTo(balance)>0){
                        balanceUserCoinVolume.setDayRate(balancePlatDayRateConfig.getThreeDayRate());
                    }else if(balanceUserCoinVolume.getCoinBalance().compareTo(balance2)>0){
                        balanceUserCoinVolume.setDayRate(balancePlatDayRateConfig.getSecondDayRate());
                    } else{
                        balanceUserCoinVolume.setDayRate(balancePlatDayRateConfig.getOneDayRate());
                    }
                    balanceUserCoinVolume.setReferId(e.getReferId());
                    System.out.println("主键ID-----"+balanceUserCoinVolume.getId());
                    if( StringUtils.isNotEmpty(balanceUserCoinVolume.getId()) &&  !"null".equals(balanceUserCoinVolume.getId())){
                        System.out.println("主键ID-----更新："+balanceUserCoinVolume.getId());
                        balanceUserCoinVolumeService.updateById(balanceUserCoinVolume);
                    }else{
                        System.out.println("主键ID-----插入"+balanceUserCoinVolume.getId());
                        balanceUserCoinVolume.setCreateDate(LocalDateTime.now());
                        balanceUserCoinVolumeService.save(balanceUserCoinVolume);
                    }
                    BalanceChangeUserCoinVolume   balanceChangeUserCoinVolume =new BalanceChangeUserCoinVolume();
                    balanceChangeUserCoinVolume.setCoinNum(balanceCoinVolumeVO.getCoinNum());
                    balanceChangeUserCoinVolume.setUserId(balanceCoinVolumeVO.getUserId());
                    balanceChangeUserCoinVolume.setCoinSymbol(balanceCoinVolumeVO.getName());
                    balanceChangeUserCoinVolume.setFlag(0);
                    balanceChangeUserCoinVolume.setMail(e.getMail());
                    balanceChangeUserCoinVolume.setMobile(e.getMobile());
                    balanceChangeUserCoinVolumeService.save(balanceChangeUserCoinVolume);
                    userCoinVolumeExService.updateOutcome(null,balanceCoinVolumeVO.getCoinNum(),e.getId(),balanceUserCoinVolume.getCoinSymbol(),false);
                    return GlobalMessageResponseVo
                            .newSuccessInstance("操作成功！");
                });
    }

    /**
     * 减少余币宝资产（转出）
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
                    BalanceUserCoinVolume  balanceUserCoinVolume=new BalanceUserCoinVolume();
                    BeanUtils.copyProperties(balanceCoinVolumeVO, balanceUserCoinVolume);
                    balanceUserCoinVolume.setMail(e.getMail());
                    balanceUserCoinVolume.setMobile(e.getMobile());
                    balanceUserCoinVolume.setCoinSymbol(balanceCoinVolumeVO.getName());
                    balanceUserCoinVolume.setCoinBalance( balanceUserCoinVolume.getCoinBalance().subtract(balanceCoinVolumeVO.getCoinNum()));
                    BigDecimal balance=new BigDecimal(5000);
                    BigDecimal balance2=new BigDecimal(1000);
                    if(balanceUserCoinVolume.getCoinBalance().compareTo(balance)>0){
                        balanceUserCoinVolume.setDayRate(balancePlatDayRateConfig.getThreeDayRate());
                    }else if (balanceUserCoinVolume.getCoinBalance().compareTo(balance2)>0){
                        balanceUserCoinVolume.setDayRate(balancePlatDayRateConfig.getSecondDayRate());
                    } else{
                        balanceUserCoinVolume.setDayRate(balancePlatDayRateConfig.getOneDayRate());
                    }
                    if( StringUtils.isNotEmpty(balanceCoinVolumeVO.getId()) &&  !"null".equals(balanceCoinVolumeVO.getId())){
                        balanceUserCoinVolumeService.updateById(balanceUserCoinVolume);
                    }else{

                        balanceUserCoinVolumeService.save(balanceUserCoinVolume);
                    }
                    BalanceChangeUserCoinVolume   balanceChangeUserCoinVolume =new BalanceChangeUserCoinVolume();
                    balanceChangeUserCoinVolume.setCoinNum(balanceCoinVolumeVO.getCoinNum());
                    balanceChangeUserCoinVolume.setUserId(balanceCoinVolumeVO.getUserId());
                    balanceChangeUserCoinVolume.setCoinSymbol(balanceCoinVolumeVO.getName());
                    balanceChangeUserCoinVolume.setFlag(1);
                    balanceChangeUserCoinVolume.setMail(e.getMail());
                    balanceChangeUserCoinVolume.setMobile(e.getMobile());
                    balanceChangeUserCoinVolumeService.save(balanceChangeUserCoinVolume);
                    userCoinVolumeExService.updateIncome(null,balanceCoinVolumeVO.getCoinNum(),e.getId(),balanceUserCoinVolume.getCoinSymbol(),false);
                    return GlobalMessageResponseVo.newSuccessInstance("操作成功！");
                });
    }

    /**
     * 获取用户邀请码
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
                        String userName=null;
                        if(coin.getMobile() != null ){
                            userName=coin.getMobile().substring(0,3)+"******"+coin.getMobile().substring(coin.getMobile().length()-2);
                        }else if (coin.getMail() != null){
                            int index =coin.getMail().indexOf("@");
                            if(index>4){
                                userName=coin.getMail().substring(0,index-4)+"****"+coin.getMail().substring(index);
                            }else{
                                userName=coin.getMail().substring(0,1)+"****"+coin.getMail().substring(index);
                            }

                        }
                        if(coinVolumeVO.getCoinNum() != null){
                            coinVolumeVO.setCoinNum(coinVolumeVO.getCoinNum().setScale(2,BigDecimal.ROUND_HALF_UP));
                        }
                        coinVolumeVO.setUserName(userName);

                        listVo.add(coinVolumeVO);

                    });
                    return GlobalMessageResponseVo.newSuccessInstance(listVo);
                });
    }
    /**
     * 排行榜查询
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
                    //查询余币宝资产信息
                    List<BalanceUserCoinVolume> listVolume = balanceUserCoinVolumeService.findByRank();
                    List<BalanceCoinVolumeVO> listVo = new ArrayList<>();
                    int userRank=0;
                    for (BalanceUserCoinVolume coin:listVolume){
                        BalanceCoinVolumeVO coinVolumeVO = new BalanceCoinVolumeVO();
                        BeanUtils.copyProperties(coin, coinVolumeVO);

                        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                        LocalDateTime time = coin.getCreateDate();
                        String createStr = df.format(time);
                        coinVolumeVO.setCreateStr(createStr);
                        String userName=null;
                        if(coin.getMobile() != null ){
                            userName=coin.getMobile().substring(0,3)+"******"+coin.getMobile().substring(coin.getMobile().length()-2);
                        }else if (coin.getMail() != null){
                            int index =coin.getMail().indexOf("@");
                            if(index>4){
                                userName=coin.getMail().substring(0,index-4)+"****"+coin.getMail().substring(index);
                            }else{
                                userName=coin.getMail().substring(0,1)+"****"+coin.getMail().substring(index);
                            }

                        }
                        coinVolumeVO.setUserName(userName);
                        if(coinVolumeVO.getCoinBalance() != null){
                            coinVolumeVO.setCoinBalance(coinVolumeVO.getCoinBalance().setScale(2, BigDecimal.ROUND_HALF_UP));
                        }
                        listVo.add(coinVolumeVO);
                        if(e.getId().equals(coin.getUserId())){
                            userRank=coin.getOrdNum();
                        }

                    }

                    Map<String,Object> map=new HashMap<String,Object>();
                    map.put("userRank",userRank);
                    map.put("list",listVo);
                    return GlobalMessageResponseVo.newSuccessInstance(map);
                });
    }

    /**
     * 我的社区邀请明细
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
                        //前期只查USDT
                        List<BalanceUserCoinVolume> listVolume = balanceUserCoinVolumeService.findByUserIdAndCoin(user.getId(),"USDT");
                        if(listVolume != null && listVolume.size()>0){
                            BalanceUserCoinVolume balanceVolume=listVolume.get(0);
                            coinVolumeVO.setTeamLevel(balanceVolume.getTeamLevel());
                            coinVolumeVO.setValidNum(balanceVolume.getValidNum());
                            coinVolumeVO.setTeamAmount(balanceVolume.getTeamAmount());
                            coinVolumeVO.setCoinBalance(balanceVolume.getCoinBalance());
                        }else{
                            coinVolumeVO.setTeamLevel("V0");
                            coinVolumeVO.setValidNum(0);
                            coinVolumeVO.setTeamAmount(BigDecimal.ZERO);
                        }
                        if(coinVolumeVO.getTeamLevel() == null){
                            coinVolumeVO.setTeamLevel("V0");
                        }
                        if(coinVolumeVO.getTeamAmount() == null){
                            coinVolumeVO.setTeamAmount(BigDecimal.ZERO);
                        }
                        String userName=null;
                        if(user.getMobile() != null ){
                            userName=user.getMobile().substring(0,3)+"******"+user.getMobile().substring(user.getMobile().length()-2);
                        }else if (user.getMail() != null){
                            int index =user.getMail().indexOf("@");
                            if(index>4){
                                userName=user.getMail().substring(0,index-4)+"****"+user.getMail().substring(index);
                            }else{
                                userName=user.getMail().substring(0,1)+"****"+user.getMail().substring(index);
                            }

                        }
                        coinVolumeVO.setUserName(userName);
                        listVo.add(coinVolumeVO);

                    });
                    return GlobalMessageResponseVo.newSuccessInstance(listVo);
                });
    }

    /**
     * 查询活动参与人数
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
                     int countNum=balanceUserCoinVolumeService.findByCountNum();
                    return GlobalMessageResponseVo.newSuccessInstance(countNum);
                });
    }

    /**
     * 查询登录用户转入记录
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
                        String userName=null;
                        if(coin.getMobile() != null ){
                            userName=coin.getMobile().substring(0,3)+"******"+coin.getMobile().substring(coin.getMobile().length()-2);
                        }else if (coin.getMail() != null){
                            int index =coin.getMail().indexOf("@");
                            if(index>4){
                                userName=coin.getMail().substring(0,index-4)+"****"+coin.getMail().substring(index);
                            }else{
                                userName=coin.getMail().substring(0,1)+"****"+coin.getMail().substring(index);
                            }

                        }
                        coinVolumeVO.setUserName(userName);
                        if(coinVolumeVO.getCoinNum() != null){
                            coinVolumeVO.setCoinNum(coinVolumeVO.getCoinNum().setScale(2, BigDecimal.ROUND_HALF_UP));
                        }

                        if(coinVolumeVO.getAccumulIncome() != null){
                            coinVolumeVO.setAccumulIncome(coinVolumeVO.getAccumulIncome().setScale(2, BigDecimal.ROUND_HALF_UP));
                        }
                        listVo.add(coinVolumeVO);

                    });
                    return GlobalMessageResponseVo.newSuccessInstance(listVo);
                });
    }
    /**
     * 减少余币宝资产（按订单转出）
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
                    BalanceChangeUserCoinVolume  balanceUserCoinVolume=new BalanceChangeUserCoinVolume();
                    BeanUtils.copyProperties(balanceChangeCoinVolumeVO, balanceUserCoinVolume);
                    balanceUserCoinVolume.setTakeOutDate(LocalDateTime.now());;
                    balanceUserCoinVolume.setFlag(1);
                    if( StringUtils.isNotEmpty(balanceChangeCoinVolumeVO.getId()) &&  !"null".equals(balanceChangeCoinVolumeVO.getId())){
                        balanceChangeUserCoinVolumeService.updateById(balanceUserCoinVolume);
                    }else{

                        balanceChangeUserCoinVolumeService.save(balanceUserCoinVolume);
                    }
                    List<BalanceUserCoinVolume> listVolume = balanceUserCoinVolumeService.findByUserIdAndCoin(balanceChangeCoinVolumeVO.getUserId(),balanceChangeCoinVolumeVO.getCoinSymbol());
                    listVolume.forEach(balanceUserCoinVolume2 -> {
                        balanceUserCoinVolume2.setCoinBalance(balanceUserCoinVolume2.getCoinBalance().subtract(balanceChangeCoinVolumeVO.getCoinNum()));
                        if( BigDecimal.ZERO.compareTo( balanceUserCoinVolume2.getCoinBalance())>0){
                            balanceUserCoinVolume2.setCoinBalance(BigDecimal.ZERO);
                        }
                        balanceUserCoinVolumeService.updateById(balanceUserCoinVolume2);
                    });
                    LocalDateTime localDateTimeNow = LocalDateTime.now();
                    Instant instant=Instant.ofEpochMilli(balanceChangeCoinVolumeVO.getCreateTime());
                    ZoneId zone=ZoneId.systemDefault();
                    LocalDateTime createDate=   LocalDateTime.ofInstant(instant,zone);
                    Duration duration = Duration.between(createDate,localDateTimeNow);
                    long longTime=duration.toDays();
                    BigDecimal coinNum=balanceChangeCoinVolumeVO.getCoinNum();
                    if (longTime<30){
                        coinNum=coinNum.subtract(coinNum.multiply(new BigDecimal(0.05)));
                    }
                    userCoinVolumeExService.updateIncome(null,coinNum,balanceChangeCoinVolumeVO.getUserId(),balanceChangeCoinVolumeVO.getCoinSymbol(),false);
                    return GlobalMessageResponseVo.newSuccessInstance("操作成功！");
                });
    }
    /**
     * 查询登录用户财务明细
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
                   if("0".equals(requestQuery.getRewardType() )) {
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
                        if(coinVolumeVO.getDetailReward() != null){
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
                        if(takeOutTime != null){
                            String takeOutTimeStr=df.format(takeOutTime);
                            coinVolumeVO.setTakeOutTimeStr(takeOutTimeStr);
                            coinVolumeVO.setCreateStr(takeOutTimeStr);
                        }
                        if(coinVolumeVO.getCoinNum() != null){
                            coinVolumeVO.setCoinNum(coinVolumeVO.getCoinNum().setScale(2, BigDecimal.ROUND_HALF_UP));
                        }
                        listVo.add(coinVolumeVO);
                    });
                    return GlobalMessageResponseVo.newSuccessInstance(listVo);
                });
    }
    /**
     * 主动发起计算收益 for test
     * @return
     */
    @GetMapping("/balance/count")
    public Mono<GlobalMessageResponseVo> count() {
        LOGGER.info("exexute balanceIncomeDetail  start ....");
        //        balanceUserCoinVolumeDetailService.balanceIncomeDetail();
        //静态收益和平级奖的利率支持配置
        Map<String, BigDecimal> dayRateMap=new HashMap<String, BigDecimal>();
        dayRateMap.put("oneDayRate",new BigDecimal(0.003));
        dayRateMap.put("secondDayRate",new BigDecimal(0.005));
        dayRateMap.put("threeDayRate",new BigDecimal(0.008));
        dayRateMap.put("equalReward",new BigDecimal(0.1));
        //每天收益和奖励计算
        balanceUserCoinVolumeDetailService.balanceIncomeDetailNew(dayRateMap);
        balanceUserCoinVolumeDetailService.balanceIncomeCount();
        LOGGER.info("exexute balanceIncomeDetail  end   ....");
        Mono<SecurityContext> context
                = ReactiveSecurityContextHolder.getContext();
        return context.filter(c -> Objects.nonNull(c.getAuthentication()))
                .map(s -> s.getAuthentication().getPrincipal())
                .cast(RedisSessionUser.class)
                .map(e -> {
                    return GlobalMessageResponseVo.newSuccessInstance("操作成功！");
                });
    }

}

