package com.biao.kline.impl;

import com.biao.enums.KlineTimeEnum;
import com.biao.kline.cache.GenericKlineCacheData;
import com.biao.kline.cache.KlineCacheDataList;
import com.biao.kline.cache.MergeEx;
import com.biao.kline.vo.KlineHandlerVO;
import com.biao.util.DateUtils;
import com.biao.vo.KlineVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The type Kline cache data list.
 */
public class KlineCacheDataListImpl {

    private static Logger logger = LoggerFactory.getLogger(KlineCacheDataList.class);

    private MergeEx[] mergeOnePart = new MergeEx[GenericKlineCacheData.DEF_CAP];

    private MergeEx[] mergeOtherPart = new MergeEx[GenericKlineCacheData.DEF_CAP];

    private KlineTimeEnum klineTimeEnum;

    private AtomicInteger exCount = new AtomicInteger(0);

    private LocalDateTime startTime;

    /**
     * 最后一个写的位置.
     */
    private int lastIndex;

    /**
     * Instantiates a new Kline cache data list.
     *
     * @param klineTimeEnum the kline time enum
     */
    public KlineCacheDataListImpl(KlineTimeEnum klineTimeEnum) {
        this.klineTimeEnum = klineTimeEnum;
        logger.info("初始化kline的数据结构,chronoUnit:{}", klineTimeEnum.getChronoUnit());
    }
    
    public KlineCacheDataListImpl(KlineTimeEnum klineTimeEnum,LocalDateTime startTime) {
        this.klineTimeEnum = klineTimeEnum;
        this.startTime = startTime ;
        logger.info("初始化kline的数据结构,chronoUnit:{},startTime:{}", klineTimeEnum.getChronoUnit(),startTime);
    }

    private static int getCap() {
        return GenericKlineCacheData.DEF_CAP;
    }

    private long getReadInterval(long interval) {
        return interval / klineTimeEnum.getInterval();
    }

    /**
     * Read kline vo list.
     *
     * @return the list
     */
    public List<KlineVO> readKlineVO() {
        List<MergeEx> mergeExs = readToMergeEx();
        if (CollectionUtils.isEmpty(mergeExs)) {
            return new ArrayList<>();
        }
        return mergeExs.stream().map(mergeEx -> {
            KlineVO klineVO = new KlineVO();
            klineVO.setH(mergeEx.max.toPlainString());
            klineVO.setL(mergeEx.min.toPlainString());
            klineVO.setC(mergeEx.last.toPlainString());
            klineVO.setO(mergeEx.first.toPlainString());
            klineVO.setV(mergeEx.volume.setScale(8, RoundingMode.HALF_DOWN).toPlainString());
            klineVO.setS("ok");
            klineVO.setT(String.valueOf(mergeEx.time.toInstant(ZoneOffset.of("+8")).toEpochMilli()));
            return klineVO;
        }).collect(Collectors.toList());
    }


    /**
     * Read to merge ex list.
     *
     * @return the list
     */
    public List<MergeEx> readToMergeEx() {
        List<MergeEx> mergeExs = new ArrayList<>(getCap() * 2);
        if (exCount.get() % 2 == 0) {
            if (lastIndex == (getCap() - 1)) {
                Stream.of(mergeOtherPart).filter(mergeOther -> mergeOther != null)
                        .forEach(mergeOther -> mergeExs.add(mergeOther));
                Stream.of(mergeOnePart).filter(mergeOne -> mergeOne != null)
                        .forEach(mergeOne -> mergeExs.add(mergeOne));
            } else {
                /*for (int cLastIndex = lastIndex + 1; cLastIndex < getCap(); cLastIndex++) {
                    if (mergeOnePart[cLastIndex] != null) {
                        mergeExs.add(mergeOnePart[cLastIndex]);
                    }
                }
                Stream.of(mergeOtherPart).filter(mergeOther -> mergeOther != null)
                        .forEach(mergeOther -> mergeExs.add(mergeOther));
                for (int cLastIndex = 0; cLastIndex <= lastIndex; cLastIndex++) {
                    if (mergeOnePart[cLastIndex] != null) {
                        mergeExs.add(mergeOnePart[cLastIndex]);
                    }
                }*/
                readArrayValue(mergeExs, mergeOnePart, mergeOtherPart,lastIndex);
            }
        } else {
            if (lastIndex == (getCap() - 1)) {
                Stream.of(mergeOnePart).filter(mergeOne -> mergeOne != null)
                        .forEach(mergeOne -> mergeExs.add(mergeOne));
                Stream.of(mergeOtherPart).filter(mergeOther -> mergeOther != null)
                        .forEach(mergeOther -> mergeExs.add(mergeOther));
            } else {
                /*for (int cLastIndex = lastIndex + 1; cLastIndex < getCap(); cLastIndex++) {
                    if (mergeOtherPart[cLastIndex] != null) {
                        mergeExs.add(mergeOtherPart[cLastIndex]);
                    }
                }
                Stream.of(mergeOnePart).filter(mergeOne -> mergeOne != null)
                        .forEach(mergeOne -> mergeExs.add(mergeOne));
                for (int cLastIndex = 0; cLastIndex <= lastIndex; cLastIndex++) {
                    if (mergeOtherPart[cLastIndex] != null) {
                        mergeExs.add(mergeOtherPart[cLastIndex]);
                    }
                }*/
                readArrayValue(mergeExs, mergeOtherPart, mergeOnePart,lastIndex);
            }

        }
        return mergeExs;
    }

    private void readArrayValue(List<MergeEx> mergeExs,MergeEx[] mOneExs,MergeEx[] mOtherExs,int index) {
    	for (int cLastIndex = index + 1; cLastIndex < getCap(); cLastIndex++) {
            if (mOneExs[cLastIndex] != null) {
                mergeExs.add(mOneExs[cLastIndex]);
            }
        }
        Stream.of(mOtherExs).filter(mergeOther -> mergeOther != null)
                .forEach(mergeOther -> mergeExs.add(mergeOther));
        for (int cLastIndex = 0; cLastIndex <= index; cLastIndex++) {
            if (mOneExs[cLastIndex] != null) {
                mergeExs.add(mOneExs[cLastIndex]);
            }
        }
    }
    
    /**
     * Init add.
     *
     * @param formatTradeTime the format trade time
     * @param klineVO         the kline vo
     */
    public void initAdd(LocalDateTime formatTradeTime, KlineVO klineVO) {
        int index = getMergeExIndex(formatTradeTime);
        MergeEx mergeEx = new MergeEx();
        mergeEx.first = new BigDecimal(klineVO.getO());
        mergeEx.last = new BigDecimal(klineVO.getC());
        mergeEx.max = new BigDecimal(klineVO.getH());
        mergeEx.min = new BigDecimal(klineVO.getL());
        mergeEx.volume = new BigDecimal(klineVO.getV());
        mergeEx.time = formatTradeTime;
        setArrayValue(index, mergeEx);
    }

    /**
     * Add merge ex.
     *
     * @param formatTradeTime the format trade time
     * @param klineHandlerVO  the kline handler vo
     * @return the merge ex
     */
    public MergeEx add(LocalDateTime formatTradeTime, KlineHandlerVO klineHandlerVO) {
        int index = getMergeExIndex(formatTradeTime);
        if(index==-1) {
        	//校正位置数据
        	return reviseMergeData(formatTradeTime, klineHandlerVO);
        }
        MergeEx mergeEx = null;
        if (exCount.get() % 2 == 0) {
            mergeEx = mergeOnePart[index];
        } else {
            mergeEx = mergeOtherPart[index];
        }
        if (mergeEx == null) {
            mergeEx = convert(klineHandlerVO, formatTradeTime);
            logger.info("============= 第一次设置的数据,周期单位:{},mergeEx:{},交易数据klineHandlerVO:{}", klineTimeEnum.getMsg(), mergeEx, klineHandlerVO);
        } else {
            logger.info("############# 周期单位:{},取出之前数据mergeEx:{},交易数据klineHandlerVO:{}", klineTimeEnum.getMsg(), mergeEx, klineHandlerVO);
            mergeEx = compAndMerge(mergeEx, klineHandlerVO, formatTradeTime);
            logger.info("============= 周期单位:{},合并计算之后mergeEx:{}", klineTimeEnum.getMsg(), mergeEx);
        }
        setArrayValue(index, mergeEx);
        return mergeEx;
    }
    
    private MergeEx reviseMergeData(LocalDateTime formatTradeTime, KlineHandlerVO klineHandlerVO) {
    	// 计算2个时间的间隔数
        long interval = startTime.until(formatTradeTime, klineTimeEnum.getChronoUnit());
        interval = getReadInterval(interval);
        //判断interval位置
        int startArrayIndex = exCount.get()-1 ;
        int startIndex = startArrayIndex >0?(startArrayIndex * getCap()):0 ;
        if(interval >= startIndex && interval<(exCount.get() * getCap())) {
        	 logger.info("校正数据  ============= 间隔interval:{},开始索引startArrayIndex:{},起始位置startIndex:{}",interval,startArrayIndex,startIndex);
        	MergeEx mergeEx = null;
        	int index = (int) (interval - startIndex) ;
        	logger.info("校正数据  ============= 间隔数据位置index:{},周期ex_count:{}",index,exCount.get());
        	if (exCount.get() % 2 == 0) {
        		mergeEx =  mergeOtherPart[index] ;
            } else {
            	mergeEx = mergeOnePart[index] ;
            }
        	if (mergeEx == null) {
                mergeEx = convert(klineHandlerVO, formatTradeTime);
                logger.info("校正数据  ============= 第一次设置的数据,周期单位:{},mergeEx:{},交易数据klineHandlerVO:{}", klineTimeEnum.getMsg(), mergeEx, klineHandlerVO);
            } else {
                logger.info("############# 周期单位:{},取出之前数据mergeEx:{},交易数据klineHandlerVO:{}", klineTimeEnum.getMsg(), mergeEx, klineHandlerVO);
                mergeEx = compAndMerge(mergeEx, klineHandlerVO, formatTradeTime);
                logger.info("校正数据  ============= 周期单位:{},合并计算之后mergeEx:{}", klineTimeEnum.getMsg(), mergeEx);
            }
        	if (exCount.get() % 2 == 0) {
        		mergeOtherPart[index] = mergeEx;
            } else {
            	mergeOnePart[index] = mergeEx;
            }
        	//不需要设置lastIndex
        	return mergeEx ;
        }
        logger.warn("校正数据  ============= 间隔interval:{},开始索引startArrayIndex:{},起始位置startIndex:{},数据不能被校验,被丢弃.",interval,startArrayIndex,startIndex);
		return null;
	}

	private void setArrayValue(int index,MergeEx mergeEx) {
    	if (exCount.get() % 2 == 0) {
            mergeOnePart[index] = mergeEx;
        } else {
            mergeOtherPart[index] = mergeEx;
        }
    	lastIndex = index;
    }

    private int getMergeExIndex(LocalDateTime formatTradeTime) {
        if (startTime == null) {
            startTime = formatTradeTime;
        }
        if(startTime.isAfter(formatTradeTime)) {
        	logger.error("周期单位:{},startTime:{},formatTradeTime:{},交易时间在开始时间之前,数据被丢弃.",klineTimeEnum.getMsg(),
                    DateUtils.formaterLocalDateTime(startTime), DateUtils.formaterLocalDateTime(formatTradeTime));
        	return -1 ;
        }
        // 计算2个时间的间隔数
        long interval = startTime.until(formatTradeTime, klineTimeEnum.getChronoUnit());
        long intervalTemp = interval;
        interval = getReadInterval(interval);
        int index = getIndex(interval);
        logger.info("增加kline数据,周期单位:{},startTime:{},formatTradeTime:{},计算时间间隔interval:{},realinterval:{},获取数据索引位置index:{},ex_count:{}", klineTimeEnum.getMsg(),
                DateUtils.formaterLocalDateTime(startTime), DateUtils.formaterLocalDateTime(formatTradeTime), intervalTemp, interval,index,exCount.get());
        if(index<0) {
        	logger.warn("周期单位:{},startTime:{},formatTradeTime:{},大于周期ex_count:{},数据被丢弃.",klineTimeEnum.getMsg(),
                    DateUtils.formaterLocalDateTime(startTime), DateUtils.formaterLocalDateTime(formatTradeTime),exCount.get());
        	return -1 ;
        }
        while (index >= getCapValue()) {
        	//清空一个格子
        	logger.info("周期单位:{},startTime:{},formatTradeTime:{},计算时间间隔interval:{},index:{},需要重新初始化。",klineTimeEnum.getMsg(),
                    DateUtils.formaterLocalDateTime(startTime), DateUtils.formaterLocalDateTime(formatTradeTime),
                    interval,index);
            if(exCount.get() % 2 == 0) {
            	mergeOtherPart = new MergeEx[GenericKlineCacheData.DEF_CAP];
            	logger.info("周期单位:{},formatTradeTime:{},初始化的时间startTime:{},interval:{},初始化的格子为mergeOtherPart",klineTimeEnum.getMsg(),
            			DateUtils.formaterLocalDateTime(formatTradeTime), DateUtils.formaterLocalDateTime(startTime),interval);
            }else {
            	mergeOnePart = new MergeEx[GenericKlineCacheData.DEF_CAP];
            	logger.info("周期单位:{},formatTradeTime:{},初始化的时间startTime:{},interval:{},初始化的格子为mergeOnePart",klineTimeEnum.getMsg(),
            			DateUtils.formaterLocalDateTime(formatTradeTime), DateUtils.formaterLocalDateTime(startTime),interval);
            }
            exCount.incrementAndGet();
            index = getIndex(interval);
        }
        logger.info("增加kline数据,周期单位:{},startTime:{},formatTradeTime:{},获取的位置index:{},ex_count:{}", klineTimeEnum.getMsg(),
                DateUtils.formaterLocalDateTime(startTime), DateUtils.formaterLocalDateTime(formatTradeTime), index,
                exCount.get());
        return index;
    }
    
    private int getIndex(long interval) {
        return (int) (interval - (exCount.get() * getCap()));
    }
    
    private int getCapValue() {
    	return getCap() ;
    }

    private static MergeEx convert(KlineHandlerVO klineHandlerVO, LocalDateTime formatTradeTime) {
        MergeEx mergeEx = new MergeEx();
        mergeEx.first = klineHandlerVO.getPrice();
        mergeEx.last = klineHandlerVO.getPrice();
        mergeEx.max = klineHandlerVO.getPrice();
        mergeEx.min = klineHandlerVO.getPrice();
        mergeEx.time = formatTradeTime;
        mergeEx.volume = klineHandlerVO.getVolume();
        return mergeEx;
    }

    private MergeEx compAndMerge(MergeEx merge, KlineHandlerVO klineHandlerVO, LocalDateTime formatTradeTime) {
        // 一个循环需要覆盖之前的数据
        if (merge.time.compareTo(formatTradeTime) != 0) {
            return convert(klineHandlerVO, formatTradeTime);
        }
        // 判断是否大于最高价
        BigDecimal price = klineHandlerVO.getPrice();
        BigDecimal volume = klineHandlerVO.getVolume();
        //判断数据是否是构造的k线数据 volumn=0
        if(volume.compareTo(new BigDecimal("0"))==0) {
        	logger.info("忽略数据 ************* 周期单位:{},startTime:{},formatTradeTime:{},忽略构造的k线交易数据",klineTimeEnum.getMsg(),
                    DateUtils.formaterLocalDateTime(startTime), DateUtils.formaterLocalDateTime(formatTradeTime));
        	return merge;
        }
        boolean maxFlag = price.compareTo(merge.max) > 0;
        if (maxFlag) {
            merge.max = price;
        }
        boolean minFlag = price.compareTo(merge.min) <= 0;
        if (minFlag) {
            merge.min = price;
        }
        merge.last = price;
        BigDecimal volume1 = merge.volume;
        merge.volume = volume1.add(volume);
        return merge;
    }

}
