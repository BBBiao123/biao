package com.biao.handler;

import com.biao.BaseTest;
import com.biao.util.JsonUtils;
import com.biao.vo.KlineResult;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *  ""(Myth)
 */
public class KlineDataHandlerTest extends BaseTest {

    @Autowired
    private KlineDataHandler klineDataHandler;

    @Test
    public void buildResult() {
        final KlineResult klineResult = klineDataHandler.buildResult("LOOM", "USDT", "1m");
        System.out.println(JsonUtils.toJson(klineResult));
    }
}