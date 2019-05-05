package com.biao.service.balance;

import com.biao.entity.balance.BalanceUserCoinVolume;
import com.biao.entity.balance.BalanceUserCoinVolumeDetail;

import java.util.List;

/**
 * 余币宝详情接口
 *
 */
public interface BalanceUserCoinVolumeDetailService {

    String save(BalanceUserCoinVolumeDetail balanceUserCoinVolumeDetail);

    /**
     * Update by id.
     *
     * @param  更新余币宝详细表
     */
    void updateById(BalanceUserCoinVolumeDetail balanceUserCoinVolumeDetail);

    /**
     * Find all list.
     *
     * @param  查询明细列表信息
     * @return the list
     */
    List<BalanceUserCoinVolumeDetail> findAll(String userId);

   void  balanceIncomeDetail();
    void  balanceIncomeDetailNew();


   void  balanceIncomeCount();


}
