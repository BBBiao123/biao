package com.biao.service.balance;

import com.biao.entity.balance.BalanceChangeUserCoinVolume;

import java.util.List;

/**
 * 余币宝接口
 *
 */
public interface BalanceChangeUserCoinVolumeService {

    String save(BalanceChangeUserCoinVolume balanceUserCoinVolume);

    /**
     * Update by id.
     *
     * @param 更新余币宝资产
     */
    void updateById(BalanceChangeUserCoinVolume balanceUserCoinVolume);

    /**
     * Find all list.
     *
     * @param 根据用户查询余币宝资产信息
     * @return the list
     */
    List<BalanceChangeUserCoinVolume> findAll();


}
