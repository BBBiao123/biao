package com.biao.service.balance;

import com.biao.entity.balance.BalanceUserCoinVolume;
import com.biao.entity.balance.BalanceUserCoinVolumeDetail;
import com.biao.vo.TradePairVO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

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
    void  balanceIncomeDetailNew(Map<String , BigDecimal> map,Map<String,TradePairVO>  tradePairMap);

   void  balanceIncomeCount();

    void  balanceJackpotIncomeCount(Map<String,TradePairVO>  tradePairMap);


}
