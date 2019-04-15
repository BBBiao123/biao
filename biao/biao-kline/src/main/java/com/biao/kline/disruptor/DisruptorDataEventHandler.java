package com.biao.kline.disruptor;

import com.biao.binance.config.KlineData;
import com.biao.dao.KlineDataDao;
import com.biao.kline.vo.KlineDisruptorVO;
import com.biao.spring.SpringBeanFactoryContext;
import com.lmax.disruptor.WorkHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The type Disruptor data event handler.
 */
public class DisruptorDataEventHandler implements WorkHandler<DisruptorData> {

    private static Logger logger = LoggerFactory.getLogger(DisruptorDataEventHandler.class);

    
    private KlineDataDao klineDataDao;

    
    /**
     * Instantiates a new Disruptor data event handler.
     */
    public DisruptorDataEventHandler() {
    	klineDataDao = SpringBeanFactoryContext.findBean(KlineDataDao.class);
    }

    @Override
    public void onEvent(DisruptorData disruptorData) throws Exception {
        logger.info("disruptor handler data :{}", disruptorData);
        KlineDisruptorVO disruptorVO = (KlineDisruptorVO) disruptorData.getData();
        KlineData klineData = KlineData.createKlineData(disruptorVO.getKlineTimeEnum().getMsg(), disruptorVO.getFormatTradeTime(),
        		disruptorVO.getKlineVo(),disruptorVO.getExPair());
		klineDataDao.insert(klineData);
    }

}
