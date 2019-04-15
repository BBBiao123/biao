package com.biao.service;

import com.biao.entity.WithdrawLog;
import com.biao.pojo.ResponsePage;
import com.biao.vo.WithdrawListVO;

public interface WithdrawLogService {

    String save(WithdrawLog withdrawLog);

    void updateById(WithdrawLog withdrawLog);

    WithdrawLog findById(String id);

    void withDrawCancel(String userId, String id);

    ResponsePage<WithdrawLog> findPage(WithdrawListVO withdrawListVO);

    void updateStatusById(String userId, String code, String id);
}
