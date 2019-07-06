package com.biao.service.balance;

import com.biao.entity.balance.BalanceUserCoinCountVolume;
import com.biao.entity.balance.BalanceUserCoinVolume;

import java.util.List;

/**
 * 余币宝接口
 *
 */
public interface BalanceUserCoinCountVolumeService {

    String save(BalanceUserCoinCountVolume balanceUserCoinVolume);

    /**
     * Update by id.
     *
     * @param 更新余币宝资产
     */
    void updateById(BalanceUserCoinCountVolume balanceUserCoinVolume);

    /**
     * Find all list.
     *
     * @param 根据用户查询余币宝资产信息
     * @return the list
     */
    List<BalanceUserCoinCountVolume> findAll(String userId);

    public List<BalanceUserCoinCountVolume> findByRank();

    List<BalanceUserCoinCountVolume> findByUserIdAndCoin(String userId, String coinSymbol);
    int findByCountNum();
}
