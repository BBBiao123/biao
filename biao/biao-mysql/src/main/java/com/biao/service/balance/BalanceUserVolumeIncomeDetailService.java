package com.biao.service.balance;

import com.biao.entity.balance.BalanceUserCoinVolumeDetail;
import com.biao.entity.balance.BalanceUserVolumeIncomeDetail;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 余币宝详情接口
 *
 */
public interface BalanceUserVolumeIncomeDetailService {

    String save(BalanceUserVolumeIncomeDetail balanceUserCoinVolumeDetail);

    /**
     * Update by id.
     *
     * @param  更新余币宝详细表
     */
    void updateById(BalanceUserVolumeIncomeDetail balanceUserCoinVolumeDetail);

    /**
     * Find all list.
     *
     * @param  查询明细列表信息
     * @return the list
     */
    List<BalanceUserVolumeIncomeDetail> findAll(String userId);

   void  balanceIncomeDetail();
    void  balanceIncomeDetailNew(Map<String, BigDecimal> map);


   void  balanceIncomeCount();


}
