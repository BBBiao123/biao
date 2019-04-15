package com.biao.service;

import com.biao.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *  ""zj
 */
public class BalanceSheetSnapshotTest extends BaseTest {

    @Autowired
    private BalanceSheetSnapshotTaskService balanceSheetSnapshotTaskService;

    @Test
    public void triggerBalanceSheetSnapshotEntry() {
        balanceSheetSnapshotTaskService.triggerBalanceSheetSnapshotEntry();
    }
}