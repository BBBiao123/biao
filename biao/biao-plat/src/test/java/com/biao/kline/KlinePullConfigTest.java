package com.biao.kline;

import com.biao.BaseTest;
import com.biao.entity.kline.KlinePullConfig;
import com.biao.enums.TradePairEnum;
import com.biao.mapper.kline.KlinePullConfigDao;
import com.biao.util.SnowFlake;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.EnumMap;

/**
 *  ""(Myth)
 */
public class KlinePullConfigTest extends BaseTest {

    private static final String BTC = "BTC";

    private static final String ETH = "ETH";

    private static final String LTC = "LTC";

    private static final String OMG = "OMG";

    private static final String ZIL = "ZIL";

    private static final String ZRX = "ZRX";

    private static final String AE = "AE";

    private static final String TRX = "TRX";

    private static final String KNC = "KNC";

    private static final String BNT = "BNT";

    private static final String LRC = "LRC";

    private static final String LOOM = "LOOM";

    private static final String POA = "POA";

    private static final String BINANCE_KLINE = "BINANCE:KLINE:";

    private static final EnumMap<TradePairEnum, String[]> enumMap = new EnumMap(TradePairEnum.class);

    static {
        enumMap.put(TradePairEnum.USDT, new String[]{BTC, ETH, LTC, TRX});
        enumMap.put(TradePairEnum.BTC, new String[]{ETH, LTC, OMG, ZIL, "THETA", "POWR", "MCO", "AION", "REP", "BLZ", AE});
        enumMap.put(TradePairEnum.ETH, new String[]{BTC, LTC, OMG, ZIL, ZRX, AE, TRX, KNC, BNT, LRC, LOOM, POA,
                "THETA", "POWR", "MCO", "AION", "REP", "BLZ"});
    }

    @Autowired(required = false)
    private KlinePullConfigDao klinePullConfigDao;

    @Test
    public void testInsert() {

        enumMap.forEach((k, v) -> {
            for (String coinOther : v) {
                KlinePullConfig klinePullConfig = new KlinePullConfig();
                klinePullConfig.setCoinMain(k.getKey());
                klinePullConfig.setId(SnowFlake.createSnowFlake().nextIdString());
                klinePullConfig.setCoinOther(coinOther);
                klinePullConfig.setProxyed(true);
                klinePullConfig.setStatus(true);
                klinePullConfig.setExchangeName("binance");
                klinePullConfig.setPullUrl("https://www.binance.com/api/v1/klines");
                klinePullConfig.setCreateBy("1");
                klinePullConfig.setUpdateBy("1");
                klinePullConfig.setUpdateDate(LocalDateTime.now());
                klinePullConfig.setCreateDate(LocalDateTime.now());
                klinePullConfigDao.insert(klinePullConfig);

            }
        });

    }
}
