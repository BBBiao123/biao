package com.biao.service;

import com.biao.entity.UserCoinVolumeBillHistory;

import java.util.List;

/**
 * UserCoinVolumeBillService.
 * <p>
 * 用户资产审请处理.
 * <p>
 * 19-1-2下午5:30
 *
 *  "" sixh
 */
public interface UserCoinVolumeBillHistoryService {
    /**
     * 批量审请记录实现.
     *
     * @param dtoList the dto list
     * @return the long
     */
    long batchInsert(List<UserCoinVolumeBillHistory> dtoList);
}
