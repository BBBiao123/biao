package com.biao.web.controller.balance;

import com.biao.config.BalancePlatDayRateConfig;
import com.biao.config.sercurity.RedisSessionUser;
import com.biao.constant.Constants;
import com.biao.entity.*;
import com.biao.entity.balance.BalanceChangeUserCoinVolume;
import com.biao.entity.balance.BalanceUserCoinVolume;
import com.biao.pojo.GlobalMessageResponseVo;
import com.biao.reactive.data.mongo.service.TradeDetailService;
import com.biao.service.*;
import com.biao.service.balance.BalanceChangeUserCoinVolumeService;
import com.biao.service.balance.BalanceUserCoinVolumeService;
import com.biao.util.StringUtil;
import com.biao.vo.CoinVolumeVO;
import com.biao.vo.OfflineTransferListVO;
import com.biao.vo.OfflineTransferVO;
import com.biao.vo.OfflineVolumeVO;
import com.biao.vo.balance.BalanceChangeCoinVolumeVO;
import com.biao.vo.balance.BalanceCoinVolumeVO;
import com.biao.web.valid.ValidateFiled;
import com.biao.web.valid.ValidateGroup;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    private OrderService orderService;

    @Autowired
    private TradeDetailService tradeDetailService;

    @Autowired
    private Mk2MiningService mk2MiningService;

    @Autowired
    private UserCoinVolumeExService userCoinVolumeExService;

    @Autowired
    private BalancePlatDayRateConfig balancePlatDayRateConfig;

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
                    if (CollectionUtils.isNotEmpty(listVolume)) {
                        listVo.forEach(coinVolumeVO2 -> {
                            listVolume.forEach(userCoinVolume -> {
                                if (userCoinVolume != null) {
                                    if (userCoinVolume.getCoinSymbol().equals(coinVolumeVO2.getName())) {
                                        BigDecimal balance=new BigDecimal(5000);
                                        BigDecimal dayRate=new BigDecimal(0);
                                        if(userCoinVolume.getCoinBalance().compareTo(balance)>0){
                                            dayRate=dayRate.add(balancePlatDayRateConfig.getSecondDayRate().multiply(new BigDecimal(100)));
                                        }else{
                                            dayRate=dayRate.add(balancePlatDayRateConfig.getOneDayRate().multiply(new BigDecimal(100)));
                                        }
                                        coinVolumeVO2.setRise( dayRate.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "%");
                                        BeanUtils.copyProperties(userCoinVolume, coinVolumeVO2);
                                        coinVolumeVO2.setReferId(e.getReferId());
                                    }
                                }
                            });
                        });
                    }
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
                    BeanUtils.copyProperties(balanceCoinVolumeVO,balanceUserCoinVolume );
                    balanceUserCoinVolume.setCoinSymbol(balanceCoinVolumeVO.getName());
                    balanceUserCoinVolume.setCoinBalance(balanceUserCoinVolume.getCoinBalance().add(balanceCoinVolumeVO.getCoinNum()));
                    BigDecimal balance=new BigDecimal(5000);
                    if(balanceUserCoinVolume.getCoinBalance().compareTo(balance)>0){
                        balanceUserCoinVolume.setDayRate(balancePlatDayRateConfig.getSecondDayRate());
                    }else{
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
                    balanceUserCoinVolume.setCoinSymbol(balanceCoinVolumeVO.getName());
                    balanceUserCoinVolume.setCoinBalance( balanceUserCoinVolume.getCoinBalance().subtract(balanceCoinVolumeVO.getCoinNum()));
                    BigDecimal balance=new BigDecimal(5000);
                    if(balanceUserCoinVolume.getCoinBalance().compareTo(balance)>0){
                        balanceUserCoinVolume.setDayRate(balancePlatDayRateConfig.getSecondDayRate());
                    }else{
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
                    balanceChangeUserCoinVolume.setFlag(2);
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
                        coinVolumeVO.setUserName(userName);

                        listVo.add(coinVolumeVO);

                    });
                    return GlobalMessageResponseVo.newSuccessInstance(listVo);
                });
    }
}

