package com.biao.service.balance;

import com.biao.entity.balance.BalancePlatJackpotVolumeDetail;
import com.biao.entity.balance.BalanceUserCoinVolume;

import java.util.List;

/**
 * 余币宝接口
 *
 */
public interface BalancePlatJackpotVolumeService {

    String save(BalancePlatJackpotVolumeDetail balanceUserCoinVolume);

    /**
     * Update by id.
     *
     * @param 更新余币宝资产
     */
    void updateById(BalancePlatJackpotVolumeDetail balanceUserCoinVolume);

    /**
     * Find all list.
     *
     * @param 根据用户查询余币宝资产信息
     * @return the list
     */
    List<BalancePlatJackpotVolumeDetail> findAll(String userId);


}
