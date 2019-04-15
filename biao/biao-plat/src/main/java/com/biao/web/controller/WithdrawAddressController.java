package com.biao.web.controller;

import com.biao.config.sercurity.RedisSessionUser;
import com.biao.entity.Coin;
import com.biao.entity.WithdrawAddress;
import com.biao.enums.CoinTypeEnum;
import com.biao.enums.WithdrawAddressStatusEnum;
import com.biao.pojo.GlobalMessageResponseVo;
import com.biao.service.CoinService;
import com.biao.service.WithdrawAddressService;
import com.biao.vo.WithdrawAddressVO;
import com.biao.web.valid.ValidateFiled;
import com.biao.web.valid.ValidateGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

/**
 * 用户提币地址管理
 */
@RestController
@RequestMapping("/biao")
public class WithdrawAddressController {

    @Autowired
    private WithdrawAddressService withdrawAddressService;

    @Autowired
    private CoinService coinService;

    /**
     * 添加提币地址
     *
     * @param withdrawAddressVO
     * @return
     */
    @ValidateGroup(fileds = {
            @ValidateFiled(index = 0, filedName = "coinId", notNull = true, errMsg = "币种id不能为空"),
            @ValidateFiled(index = 0, filedName = "symbol", notNull = true, errMsg = "币种符号不能为空"),
            @ValidateFiled(index = 0, filedName = "address", notNull = true, errMsg = "地址不能为空"),
            @ValidateFiled(index = 0, filedName = "tag", notNull = true, errMsg = "tag不能为空"),
    })
    @PostMapping("/withdraw/address/add")
    public Mono<GlobalMessageResponseVo> addAddress(WithdrawAddressVO withdrawAddressVO) {

        Mono<SecurityContext> context
                = ReactiveSecurityContextHolder.getContext();

        return context.filter(c -> Objects.nonNull(c.getAuthentication()))
                .map(s -> s.getAuthentication().getPrincipal())
                .cast(RedisSessionUser.class)
                .map(e -> {

                    String coinId = withdrawAddressVO.getCoinId();
                    String symbol = withdrawAddressVO.getSymbol();
                    Coin coin = coinService.findById(coinId);
                    String type = CoinTypeEnum.NO.getCode();
                    if (coin.getCoinType().equals(CoinTypeEnum.NO.getCode())) {


                    } else if (coin.getCoinType().equals(CoinTypeEnum.ETH.getCode())) {
                        type = CoinTypeEnum.ETH.getCode();
                    } else if (coin.getCoinType().equals(CoinTypeEnum.QTUM.getCode())) {
                        type = CoinTypeEnum.QTUM.getCode();
                    } else if (coin.getCoinType().equals(CoinTypeEnum.NEO.getCode())) {
                        type = CoinTypeEnum.NEO.getCode();

                    } else if (coin.getCoinType().equals(CoinTypeEnum.EOS.getCode())) {
                        type = CoinTypeEnum.EOS.getCode();
                    } else if (coin.getCoinType().equals(CoinTypeEnum.BTC.getCode())) {
                        type = CoinTypeEnum.BTC.getCode();
                    }
                    WithdrawAddress withdrawAddress = new WithdrawAddress();
                    withdrawAddress.setUserId(e.getId());
                    withdrawAddress.setCoinId(coinId);
                    withdrawAddress.setType(type);
                    withdrawAddress.setTag(withdrawAddressVO.getTag());
                    withdrawAddress.setAddress(withdrawAddressVO.getAddress().trim());
                    withdrawAddress.setCoinSymbol(symbol.trim());
                    withdrawAddressService.save(withdrawAddress);
                    return GlobalMessageResponseVo
                            .newSuccessInstance("操作成功！");
                });
    }

    /**
     * 币种地址删除
     *
     * @param id
     * @return
     */
    @GetMapping("/withdraw/address/delete/{id}")
    public Mono<GlobalMessageResponseVo> deleteAddress(@PathVariable("id") String id) {

        Mono<SecurityContext> context
                = ReactiveSecurityContextHolder.getContext();

        return context.filter(c -> Objects.nonNull(c.getAuthentication()))
                .map(s -> s.getAuthentication().getPrincipal())
                .cast(RedisSessionUser.class)
                .map(e -> {
                    String userId = e.getId();
                    withdrawAddressService.updateStatusByUserIdAndId(WithdrawAddressStatusEnum.CANCEL.getCode(), userId, id);
                    return GlobalMessageResponseVo
                            .newSuccessInstance("操作成功！");
                });
    }

    /**
     * 查看所有币种的提现地址
     *
     * @param
     * @return
     */
    @RequestMapping("/withdraw/address/list")
    public Mono<GlobalMessageResponseVo> withDrawAddressList() {

        Mono<SecurityContext> context
                = ReactiveSecurityContextHolder.getContext();

        return context.filter(c -> Objects.nonNull(c.getAuthentication()))
                .map(s -> s.getAuthentication().getPrincipal())
                .cast(RedisSessionUser.class)
                .map(e -> {
                    String userId = e.getId();
                    List<WithdrawAddress> list = withdrawAddressService.findAll(userId);
                    return GlobalMessageResponseVo.newSuccessInstance(list);

                });
    }

    /**
     * 查看某个币种的提现地址列表
     * 传入币种id
     *
     * @return
     */
    @GetMapping("/withdraw/address/list/{coinId}")
    public Mono<GlobalMessageResponseVo> withDrawAddressListSymbol(@PathVariable("coinId") String coinId) {

        Mono<SecurityContext> context
                = ReactiveSecurityContextHolder.getContext();

        return context.filter(c -> Objects.nonNull(c.getAuthentication()))
                .map(s -> s.getAuthentication().getPrincipal())
                .cast(RedisSessionUser.class)
                .map(e -> {
                    String userId = e.getId();
                    Coin coin = coinService.findById(coinId);
                    String type = CoinTypeEnum.NO.getCode();
                    if (coin.getCoinType().equals(type)) {
                        //直接把该种币的地址查出来 userId coinId status
                        List<WithdrawAddress> list = withdrawAddressService.findByUserIdAndCoinId(userId, coinId);
                        return GlobalMessageResponseVo
                                .newSuccessInstance(list);
                    } else {//根据type 查询出来
                        type = coin.getCoinType();
                        List<WithdrawAddress> list = withdrawAddressService.findByUserIdAndType(userId, type);
                        return GlobalMessageResponseVo
                                .newSuccessInstance(list);
                    }
                });
    }

}
