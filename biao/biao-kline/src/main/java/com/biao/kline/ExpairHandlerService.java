package com.biao.kline;

import com.biao.enums.KlineTimeEnum;
import com.biao.kline.pool.ThreadPoolUtils;
import com.biao.kline.vo.KlineHandlerVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExpairHandlerService implements Runnable {

    private static Logger logger = LoggerFactory.getLogger(ExpairHandlerService.class);

    private KlineHandlerVO klineHandlerVO;

    public ExpairHandlerService(KlineHandlerVO klineHandlerVO) {
        this.klineHandlerVO = klineHandlerVO;
    }

    @Override
    public void run() {
        try {
            for (KlineTimeEnum klineTimeEnum : KlineTimeEnum.values()) {
                logger.info("计算kline,处理交易对mainCoin:{},otherCoin:{},kline的单位:{}", klineHandlerVO.getCoinMain(), klineHandlerVO.getCoinOther(), klineTimeEnum.getMsg());
                String selectKey = buildSelectKey(klineTimeEnum.getMsg());
                KlineHanderService klineHanderService = new KlineHanderService(klineHandlerVO, klineTimeEnum);
                ThreadPoolUtils.getKlineInstance().select(selectKey).execute(klineHanderService);
            }
        } catch (Exception e) {
            logger.error("处理交易对mainCoin:{},otherCoin:{},error:{}", klineHandlerVO.getCoinMain(), klineHandlerVO.getCoinOther(), e);
        }
    }

    private String buildSelectKey(String klineTimeMsg) {
        return klineHandlerVO.getCoinMain() + "_" + klineHandlerVO.getCoinOther() + ":" + klineTimeMsg;
    }
}
