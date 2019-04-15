package com.biao.service;

import com.biao.entity.UserCoinVolumeBill;
import com.biao.pojo.UserCoinVolumeBillDTO;

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
public interface UserCoinVolumeBillService {
    /**
     * 审请一条记录实现.
     * dto 所有参数不允许为空.
     *
     * @param dto the dto
     * @return the long
     */
    long insert(UserCoinVolumeBillDTO dto);

    /**
     * 批量审请记录实现.
     *
     * @param dtoList the dto list
     * @return the long
     */
    long batchInsert(List<UserCoinVolumeBillDTO> dtoList);

    /**
     * 查询完成的历史记录.
     *
     * @return history;
     */
    List<UserCoinVolumeBill> findByHistory();

    /**
     * 删除历史记录.
     *
     * @return 记录.
     */
    long deleteHistory();


}
