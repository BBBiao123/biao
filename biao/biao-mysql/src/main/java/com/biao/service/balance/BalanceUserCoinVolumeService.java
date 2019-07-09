package com.biao.service.balance;

import com.biao.entity.balance.BalanceUserCoinVolume;

import java.util.List;

/**
 * 余币宝接口
 *
 */
public interface BalanceUserCoinVolumeService {

    String save(BalanceUserCoinVolume balanceUserCoinVolume);

    /**
     * Update by id.
     *
     * @param 更新余币宝资产
     */
    void updateById(BalanceUserCoinVolume balanceUserCoinVolume);

    /**
     * Find all list.
     *
     * @param 根据用户查询余币宝资产信息
     * @return the list
     */
    List<BalanceUserCoinVolume> findAll(String userId);

    public List<BalanceUserCoinVolume> findByRank();

    List<BalanceUserCoinVolume> findByUserIdAndCoin(String userId,String coinSymbol);
    int findByCountNum();

    List<BalanceUserCoinVolume> findInvitesByUserId(String userId);

    List<BalanceUserCoinVolume> findByAllRank();
}
