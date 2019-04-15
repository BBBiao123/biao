package com.biao.service.impl;

import com.biao.entity.UserCoinVolumeBillHistory;
import com.biao.mapper.UserCoinVolumeBillHistoryDao;
import com.biao.service.UserCoinVolumeBillHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * UserCoinVolumeBillHistoryServiceImpl.
 * <p>
 * <p>
 * 19-1-3下午5:46
 *
 *  "" sixh
 */
@Component
public class UserCoinVolumeBillHistoryServiceImpl implements UserCoinVolumeBillHistoryService {

    @Autowired
    private UserCoinVolumeBillHistoryDao billHistoryDao;

    @Override
    public long batchInsert(List<UserCoinVolumeBillHistory> dtoList) {
        return billHistoryDao.insertBatch(dtoList);
    }
}
