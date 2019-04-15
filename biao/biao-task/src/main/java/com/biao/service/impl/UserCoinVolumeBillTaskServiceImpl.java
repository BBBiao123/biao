package com.biao.service.impl;

import com.biao.entity.UserCoinVolumeBill;
import com.biao.entity.UserCoinVolumeBillHistory;
import com.biao.service.UserCoinVolumeBillHistoryService;
import com.biao.service.UserCoinVolumeBillService;
import com.biao.service.UserCoinVolumeBillTaskService;
import com.biao.util.Splitter;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * UserCoinVolumeBillTaskServiceImpl.
 * <p>
 * <p>
 * 19-1-3下午6:04
 *
 *  "" sixh
 */
@Component
public class UserCoinVolumeBillTaskServiceImpl implements UserCoinVolumeBillTaskService {

    @Autowired
    private UserCoinVolumeBillService billService;

    @Autowired
    private UserCoinVolumeBillHistoryService billHistoryService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public long execute() {
        List<UserCoinVolumeBill> byHistory = billService.findByHistory();
        int size = byHistory != null ? byHistory.size() : 0;
        if (size > 0) {
            List<UserCoinVolumeBillHistory> collect = byHistory.stream().map(e -> {
                UserCoinVolumeBillHistory history = new UserCoinVolumeBillHistory();
                BeanUtils.copyProperties(e, history);
                return history;
            }).collect(Collectors.toList());
            List<Splitter.Entry> entries = Splitter.baseSplitter(100, byHistory.size());
            for (Splitter.Entry entry : entries) {
                List<UserCoinVolumeBillHistory> histories = collect.subList(entry.pageStartRow, entry.pageEndRow);
                billHistoryService.batchInsert(histories);
            }
            billService.deleteHistory();
        }
        return size;
    }
}
