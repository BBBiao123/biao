package com.biao.service.balance;

import com.biao.entity.PlatUser;
import com.biao.entity.balance.BalanceChangeUserCoinVolume;
import com.biao.entity.balance.BalanceUserCoinVolume;
import com.biao.vo.TradePairVO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

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
    long deleteByBalanceId(String changeId);

    void balanceVolume(BalanceUserCoinVolume balanceUserCoinVolume, BigDecimal rewardNum);
    void balanceOutVolume( BalanceChangeUserCoinVolume balanceUserCoinVolume, BigDecimal coinNum);
}
