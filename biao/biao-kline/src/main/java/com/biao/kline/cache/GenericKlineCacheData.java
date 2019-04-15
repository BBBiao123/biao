package com.biao.kline.cache;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * The type Generic kline cache data.
 */
public class GenericKlineCacheData {

    /**
     * The constant DEF_CAP . 
     */
    public static final int DEF_CAP = 1500;
    
    
    public static boolean INIT_COMPLATE = false ;
    
    private static volatile AtomicInteger countNum = new AtomicInteger(0);

    /**
     * Build kline cache key string.
     *
     * @param coinMain     the coin main
     * @param coinOther    the coin other
     * @param klineTimeMsg the kline time msg
     * @return the string
     */
    public static String buildKlineCacheKey(final String coinMain, final String coinOther,
                                            final String klineTimeMsg) {
        return "kline:" + coinMain + "_" + coinOther + ":"
                + klineTimeMsg;
    }
    
    public static void initBlock() {
    	while(!GenericKlineCacheData.INIT_COMPLATE) {
    		try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}
    		int addNum = countNum.incrementAndGet();
    		if(addNum >= 600) {
    			GenericKlineCacheData.initComplate();
    		}
    	}
    }
    
    public static void initComplate() {
    	GenericKlineCacheData.INIT_COMPLATE = true ;
    	countNum.set(0);
    }
    
}
