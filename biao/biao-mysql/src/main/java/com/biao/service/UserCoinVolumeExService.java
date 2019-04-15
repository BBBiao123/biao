package com.biao.service;

import com.biao.entity.UserCoinVolume;
import com.biao.enums.OrderEnum;
import com.biao.pojo.UserCoinVolumeOpDTO;

import java.math.BigDecimal;
import java.util.List;

/**
 * The interface User coin volume service.
 *
 */
public interface UserCoinVolumeExService {

    /**
     * Update by id.
     *
     * @param userCoinVolume the user coin volume
     */
    void updateById(UserCoinVolume userCoinVolume);

    /**
     * Find all list.
     *
     * @param userId the user id
     * @return the list
     */
    List<UserCoinVolume> findAll(String userId);

    /**
     * 修改用户已经花费的金额；
     *
     * @param status       订单状态；
     * @param spent        花费的金额；
     * @param refundVolume 退回原帐号的金额;
     * @param userId       用户id;
     * @param coinSymbol   币种；
     * @return 操作成功 ！
     */
    long updateSpent(OrderEnum.OrderStatus status, BigDecimal spent, BigDecimal refundVolume, String userId, String coinSymbol);

    /**
     * 修改用户得到的金额；
     *
     * @param status     订单状态；
     * @param income     得到金额
     * @param userId     用户id;
     * @param coinSymbol 币种；
     * @return 操作成功 ；不抛异常，只返回成功失败标识，0失败1成功
     */
    long updateIncome(OrderEnum.OrderStatus status, BigDecimal income, String userId, String coinSymbol);

    /**
     * 修改用户得到的金额；
     *
     * @param status     订单状态；
     * @param income     得到金额
     * @param userId     用户id;
     * @param coinSymbol 币种；
     * @param force      the force 强制拿锁.
     * @return 操作成功 ；不抛异常，只返回成功失败标识，0失败1成功
     */
    long updateIncome(OrderEnum.OrderStatus status, BigDecimal income, String userId, String coinSymbol, boolean force);


    /**
     * 修改用户得到的金额；
     *
     * @param status     订单状态；
     * @param income     得到金额
     * @param userId     用户id;
     * @param coinSymbol 币种；
     * @return 操作成功 ；加资产失败抛异常
     */
    void updateIncomeException(OrderEnum.OrderStatus status, BigDecimal income, String userId, String coinSymbol);

    /**
     * 修改用户得到的金额；
     *
     * @param status     订单状态；
     * @param outCome    减金额
     * @param userId     用户id;
     * @param coinSymbol 币种；
     * @return 操作成功 ；不抛异常，只返回成功失败标识，0失败1成功
     */
    long updateOutcome(OrderEnum.OrderStatus status, BigDecimal outCome, String userId, String coinSymbol);

    /**
     * 修改用户得到的金额；
     *
     * @param status     订单状态；
     * @param outCome    减金额
     * @param userId     用户id;
     * @param coinSymbol 币种；
     * @param force      the force 强制拿锁.
     * @return 操作成功 ；不抛异常，只返回成功失败标识，0失败1成功
     */
    long updateOutcome(OrderEnum.OrderStatus status, BigDecimal outCome, String userId, String coinSymbol, boolean force);

    /**
     * 修改用户得到的金额；
     *
     * @param status     订单状态；
     * @param outCome    减金额
     * @param userId     用户id;
     * @param coinSymbol 币种；
     * @return 操作成功 ；抛异常，减资产失败抛异常
     */
    void updateOutcomeException(OrderEnum.OrderStatus status, BigDecimal outCome, String userId, String coinSymbol);

    /**
     * Find by user id and coin id user coin volume.
     *
     * @param userId the user id
     * @param coinId the coin id
     * @return the user coin volume
     */
    UserCoinVolume findByUserIdAndCoinId(String userId, String coinId);


    /**
     * Find by user id and coin symbol user coin volume.
     *
     * @param userId     the user id
     * @param coinSymbol the coin symbol
     * @return the user coin volume
     */
    UserCoinVolume findByUserIdAndCoinSymbol(String userId, String coinSymbol);


    /**
     * 修改用户的资产信息，这个方法在修改的时候：blockVolume增加 总volume减少;
     * 非强制锁，有可能会失败.
     * @param userId           userId;
     * @param symbol           symbol
     * @param blockVolume      需要锁定的资源信息;
     * @param isSubtractVolume 是否操作volume subtract;
     * @return 成功 ;
     */
    long addLockVolume(String userId, String symbol, BigDecimal blockVolume, boolean isSubtractVolume);

    /**
     * 修改用户的资产信息，这个方法在修改的时候：blockVolume减少 总volume不变
     *
     * @param userId      userId;
     * @param symbol      symbol
     * @param blockVolume 需要锁定的资源信息;
     * @param isAddVolume 是否操作volume add;
     * @return 1 :成功 0失败;
     */
    long subtractLockVolume(String userId, String symbol, BigDecimal blockVolume, boolean isAddVolume);

    /**
     * 修改用户的资产信息，这个方法在修改的时候：blockVolume减少 总volume不变
     *
     * @param userId      userId;
     * @param symbol      symbol
     * @param blockVolume 需要锁定的资源信息;
     * @param isAddVolume 是否操作volume add;
     * @param force       the force 强制拿锁.
     * @return 1 :成功 0失败;
     */
    long subtractLockVolume(String userId, String symbol, BigDecimal blockVolume, boolean isAddVolume, boolean force);

    /**
     * Volume action by rlock long.
     *
     * @param dto       the dto
     * @param forceLock the force lock
     * @return the long
     */
    long volumeActionByRlock(UserCoinVolumeOpDTO dto,
                                    boolean forceLock);



}
