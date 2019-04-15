package com.biao.web.controller;

import com.biao.config.sercurity.RedisSessionUser;
import com.biao.entity.Coin;
import com.biao.entity.CoinAddress;
import com.biao.entity.DepositAddress;
import com.biao.entity.PlatUser;
import com.biao.enums.CoinTypeEnum;
import com.biao.pojo.GlobalMessageResponseVo;
import com.biao.service.*;
import com.biao.vo.DepositAddressVO;
import com.biao.vo.DepositListVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Objects;

/**
 * 用户coin充币相关操作
 */
@RestController
@RequestMapping("/biao")
public class DepositController {

    @Autowired
    private CoinService coinService;
    @Autowired
    private DepositAddressService depositAddressService;
    @Autowired
    private CoinAddressService coinAddressService;
    @Autowired
    private DepositLogService depositLogService;
    @Autowired
    private PlatUserService platUserService;


    @RequestMapping("/coin/user/address/v1/{id}")
    @ResponseBody
    public Mono<GlobalMessageResponseVo> coinUserAddress(@PathVariable("id") String id) {
        Mono<SecurityContext> context
                = ReactiveSecurityContextHolder.getContext();
        return context
                .filter(c -> c.getAuthentication() != null)
                .map(SecurityContext::getAuthentication).map(Authentication::getPrincipal).cast(RedisSessionUser.class).map(user -> {
                    String userId = user.getId();
                    //判断该币种是主币 还是代币
                    Coin coin = coinService.findById(id);
                    if (null == coin) {
                        return GlobalMessageResponseVo.newErrorInstance("获取地址失败");
                    }
                    //DepositAddress depositAddress = null;
                    if (coin.getCoinType().toString().equals(CoinTypeEnum.NO.getCode())) {
                        DepositAddress depositAddress = depositAddressService.findByUserIdAndCoinId(userId, id);
                        if (null == depositAddress) {
                            //获取一个新地址
                            CoinAddress coinAddress = coinAddressService.findByCoinId(id, userId);
                            if (null == coinAddress) {
                                return GlobalMessageResponseVo.newErrorInstance("获取地址失败");
                            }
                            return GlobalMessageResponseVo.newSuccessInstance((Object) coinAddress.getAddress());

                        } else {
                            return GlobalMessageResponseVo.newSuccessInstance((Object) depositAddress.getAddress());
                        }
                    } else if(coin.getCoinType().toString().equals(CoinTypeEnum.EOS.getCode())){

                        PlatUser platUser = platUserService.findById(userId);
                        DepositAddressVO depositAddressVO = new DepositAddressVO("dazieos55555",platUser.getInviteCode());
                        return GlobalMessageResponseVo.newSuccessInstance(depositAddressVO);

                    }else {
                        //通过coinId 确定coinType  通过coinType 确定是基于什么做的代币
                        DepositAddress depositAddress = depositAddressService.findByUserIdAndCoinId(userId, coin.getParentId());
                        if (null == depositAddress) {
                            //获取一个新地址
                            CoinAddress coinAddress = coinAddressService.findByCoinId(coin.getParentId(), userId);
                            if (null == coinAddress) {
                                return GlobalMessageResponseVo.newErrorInstance("获取失败");
                            }
                            return GlobalMessageResponseVo.newSuccessInstance((Object) coinAddress.getAddress());
                        } else {
                            return GlobalMessageResponseVo.newSuccessInstance((Object) depositAddress.getAddress());
                        }
                    }
                });
    }

    /**
     * 获取用户某个币种地址 如果没有就获取一个 从coinAddress
     *
     * @param id
     * @return
     */
    @RequestMapping("/coin/user/address/{id}")
    @ResponseBody
    public Mono<GlobalMessageResponseVo> coinNewUserAddress(@PathVariable("id") String id) {
        Mono<SecurityContext> context
                = ReactiveSecurityContextHolder.getContext();
        return context
                .filter(c -> c.getAuthentication() != null)
                .map(SecurityContext::getAuthentication).map(Authentication::getPrincipal).cast(RedisSessionUser.class).map(user -> {
                    String userId = user.getId();
                    //判断该币种是主币 还是代币
                    Coin coin = coinService.findById(id);
                    if (null == coin) {
                        return GlobalMessageResponseVo.newErrorInstance("获取地址失败");
                    }
                    //DepositAddress depositAddress = null;
                    if (coin.getCoinType().toString().equals(CoinTypeEnum.NO.getCode())) {
                        DepositAddress depositAddress = depositAddressService.findByUserIdAndCoinId(userId, id);
                        if (null == depositAddress) {
                            //获取一个新地址
                            CoinAddress coinAddress = coinAddressService.findByCoinId(id, userId);
                            if (null == coinAddress) {
                                return GlobalMessageResponseVo.newErrorInstance("获取地址失败");
                            }
                            return GlobalMessageResponseVo.newSuccessInstance(new DepositAddressVO(coinAddress.getAddress(),""));

                        } else {
                            return GlobalMessageResponseVo.newSuccessInstance(new DepositAddressVO(depositAddress.getAddress(),""));
                        }
                    } else if(coin.getCoinType().toString().equals(CoinTypeEnum.EOS.getCode())){

                           PlatUser platUser = platUserService.findById(userId);
                            DepositAddressVO depositAddressVO = new DepositAddressVO("dazieos55555",platUser.getInviteCode());
                            return GlobalMessageResponseVo.newSuccessInstance(depositAddressVO);

                    }else {
                        //通过coinId 确定coinType  通过coinType 确定是基于什么做的代币
                        DepositAddress depositAddress = depositAddressService.findByUserIdAndCoinId(userId, coin.getParentId());
                        if (null == depositAddress) {
                            //获取一个新地址
                            CoinAddress coinAddress = coinAddressService.findByCoinId(coin.getParentId(), userId);
                            if (null == coinAddress) {
                                return GlobalMessageResponseVo.newErrorInstance("获取失败");
                            }
                            return GlobalMessageResponseVo.newSuccessInstance(new DepositAddressVO(coinAddress.getAddress(),""));
                        } else {
                            return GlobalMessageResponseVo.newSuccessInstance(new DepositAddressVO(depositAddress.getAddress(),""));
                        }
                    }
                });
    }


    /**
     * 用户充值记录
     *
     * @param depositListVO
     * @return
     */
    @PostMapping("/coin/deposit/list")
    public Mono<GlobalMessageResponseVo> withDrawList(DepositListVO depositListVO) {

        Mono<SecurityContext> context
                = ReactiveSecurityContextHolder.getContext();

        return context.filter(c -> Objects.nonNull(c.getAuthentication()))
                .map(s -> s.getAuthentication().getPrincipal())
                .cast(RedisSessionUser.class)
                .map(e -> {
                    String userId = e.getId();
                    depositListVO.setUserId(userId);
                    return GlobalMessageResponseVo.newSuccessInstance(depositLogService.findPage(depositListVO));

                });
    }

    /**
     * 用户充提记录
     *
     * @param depositListVO
     * @return
     */
    @PostMapping("/coin/depdraw/list")
    public Mono<GlobalMessageResponseVo> depdrawList(DepositListVO depositListVO) {

        Mono<SecurityContext> context
                = ReactiveSecurityContextHolder.getContext();

        return context.filter(c -> Objects.nonNull(c.getAuthentication()))
                .map(s -> s.getAuthentication().getPrincipal())
                .cast(RedisSessionUser.class)
                .map(e -> {
                    String userId = e.getId();
                    depositListVO.setUserId(userId);
                    return GlobalMessageResponseVo.newSuccessInstance(depositLogService.findDepdrawLogPage(depositListVO));

                });
    }

}
