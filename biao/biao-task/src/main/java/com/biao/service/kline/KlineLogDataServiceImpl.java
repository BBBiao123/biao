package com.biao.service.kline;

import com.biao.constant.RedisKeyConstant;
import com.biao.constant.TradeConstant;
import com.biao.entity.ExPair;
import com.biao.enums.KlineTimeEnum;
import com.biao.kafka.interceptor.SampleMessage;
import com.biao.mapper.ExPairDao;
import com.biao.pojo.MatchStreamDto;
import com.biao.reactive.data.mongo.domain.RedisMatchStream;
import com.biao.reactive.data.mongo.domain.kline.KlineLog;
import com.biao.reactive.data.mongo.domain.kline.KlineStatLog;
import com.biao.reactive.data.mongo.domain.kline.TimingWheel;
import com.biao.reactive.data.mongo.service.KlineLogService;
import com.biao.reactive.data.mongo.service.KlineStatLogService;
import com.biao.service.KlineLogDataService;
import com.biao.util.DateUtils;
import com.biao.vo.KlineVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 *  ""dministrator
 */
@Service("klineLogDataService")
@SuppressWarnings("all")
public class KlineLogDataServiceImpl implements KlineLogDataService {

    private static Logger logger = LoggerFactory.getLogger(KlineLogDataServiceImpl.class);

    @Resource(name = "stringRedisTemplate")
    private ValueOperations<String, String> valOpsStr;

    private final ExPairDao exPairDao;
    private final KlineLogService klineLogService;
    private final RedisTemplate<String, Object> redisTemplate;
    private final KlineStatLogService klineStatLogService;
    
    private final KafkaTemplate kafkaTemplate;

    @Autowired
    public KlineLogDataServiceImpl(ExPairDao exPairDao, KlineLogService klineLogService,
                                   RedisTemplate<String, Object> redisTemplate,
                                   KlineStatLogService klineStatLogService,KafkaTemplate kafkaTemplate) {
        this.exPairDao = exPairDao;
        this.klineLogService = klineLogService;
        this.redisTemplate = redisTemplate;
        this.klineStatLogService = klineStatLogService;
        this.kafkaTemplate = kafkaTemplate;
    }

    public static String buildKlineCacheKey(final String coinMain, final String coinOther,
            final String klineTimeMsg) {
		return "kline:" + coinMain + "_" + coinOther + ":"
		+ klineTimeMsg;
	}
    
    private String buildKey(String coinMain, String coinOther) {
        return "kline:" + coinMain + "_" + coinOther + ":1M";
    }
    
    @Override
    public void initKlineData(KlineLogDataConfig config) {
    	LocalDateTime tradeTime = config.getTradeTime() ;
    	String redisKey = buildKey(config.getCoinMain(), config.getCoinOther()) ;
    	String member = DateUtils.formaterLocalDateTime(LocalDateTime.of(LocalDate.of(tradeTime.getYear(), tradeTime.getMonth(), 1), LocalTime.of(0, 0, 0))) ;
    	final KlineVO klineVO = (KlineVO) redisTemplate.opsForHash().get(redisKey,member );
    	logger.info("===========query kline data,redisKey:{}, member:{} , tradeTime:{},klineVO:{}",redisKey,member,tradeTime,klineVO);
    	if(klineVO!=null) {
    		String tradeTimeFormat = DateUtils.formaterLocalDateTime(tradeTime) ;
    		MatchStreamDto msd = new MatchStreamDto();
    		msd.setCoinMain(config.getCoinMain());
    		msd.setCoinOther(config.getCoinOther());
    		msd.setMinuteTime(tradeTimeFormat);
    		msd.setTradeTime(tradeTimeFormat);
    		msd.setPrice(new BigDecimal(klineVO.getC()));
    		msd.setVolume(new BigDecimal("0"));
    		kafkaTemplate.send(TradeConstant.KLINE_MIN_TRANSFER, buildKlineCacheKey(config.getCoinMain(), config.getCoinOther(), config.getDayType().getMsg()), 
    				SampleMessage.build(msd));
    		logger.info("===========send kline data , MainCoin:{},otherCoin:{},tradeTime:{}",config.getCoinMain(), config.getCoinOther(),tradeTimeFormat);
    	}
    }
    
    @Override
    public void synRedis(KlineLogDataConfig config) {
    	 final String pairVOKey = RedisKeyConstant.buildTaskKlineStatKey(config.getCoinMain(), config.getCoinOther(), config.getDayType().getMsg());
    	 List<Object> klineVOObjects = redisTemplate.boundListOps(pairVOKey).range(0, -1);
    	 SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	 logger.info("--------同步kline数据 开始,coinMain:{},coinOther:{},klineEnum:{}",config.getCoinMain(),config.getCoinOther(),config.getDayType().getMsg());
    	 if(!CollectionUtils.isEmpty(klineVOObjects)) {
    		 for(Object klineVOObject :klineVOObjects) {
    			 KlineVO klineVO = (KlineVO)klineVOObject ;
    			 String klineKeyStr = buildKlineCacheKey(config.getCoinMain(), config.getCoinOther(), config.getDayType().getMsg());
    			 String key = sdf.format(new Date(Long.parseLong(klineVO.getT())));;
    			 redisTemplate.boundHashOps(klineKeyStr).put(key, klineVO);
    			 logger.info("--------同步kline数据,coinMain:{},coinOther:{},klineEnum:{},key:{}",config.getCoinMain(),config.getCoinOther(),config.getDayType().getMsg(),key);
    		 }
    		 logger.info("--------同步kline数据 完成,coinMain:{},coinOther:{},klineEnum:{},同步数据大小size:{}",config.getCoinMain(),config.getCoinOther(),config.getDayType().getMsg(),klineVOObjects.size());
    	 }
    }
    
    @Override
    public void executePullKlineLogData(KlineTimeEnum dayType) {
        List<ExPair> expairs = exPairDao.findByList();
        if (!CollectionUtils.isEmpty(expairs)) {
            expairs.stream().map(expair -> {
                KlineLogDataConfig config = new KlineLogDataConfig();
                config.setCoinMain(expair.getPairOne());
                config.setCoinOther(expair.getPairOther());
                config.setDayType(dayType);
                return config;
            }).collect(Collectors.toList()).forEach(config -> KlineLogDataHandler.submit(config));
        }
    }
    
    @Override
    public void executePullKlineLogData(KlineTimeEnum dayType, boolean synRedis) {
    	List<ExPair> expairs = exPairDao.findByList();
        if (!CollectionUtils.isEmpty(expairs)) {
            expairs.stream().map(expair -> {
                KlineLogDataConfig config = new KlineLogDataConfig();
                config.setCoinMain(expair.getPairOne());
                config.setCoinOther(expair.getPairOther());
                config.setDayType(dayType);
                config.setSynRedis(synRedis);
                return config;
            }).collect(Collectors.toList()).forEach(config -> KlineLogDataHandler.submit(config));
        }
    	
    }
    
    @Override
    public void executePullKlineLogData(KlineTimeEnum dayType, boolean synRedis, boolean klineInitTag) {
    	List<ExPair> expairs = exPairDao.findByList();
        if (!CollectionUtils.isEmpty(expairs)) {
            expairs.stream().map(expair -> {
                KlineLogDataConfig config = new KlineLogDataConfig();
                config.setCoinMain(expair.getPairOne());
                config.setCoinOther(expair.getPairOther());
                config.setDayType(dayType);
                config.setSynRedis(synRedis);
                config.setTradeTime(LocalDateTime.now());
                config.setKlineInitTag(klineInitTag);
                return config;
            }).collect(Collectors.toList()).forEach(config -> KlineLogDataHandler.submit(config));
        }
    	
    }

    @Override
    public void klineLogData(KlineLogDataConfig config, boolean cache) {
        List<KlineLog> klinePairLogs = null;
        String exeTimeKey = "statistics_klineLog:" + config.getCoinMain() + "_" + config.getCoinOther() + ":" + config.getDayType().getMsg();
        // 上次执行的时间，如果为空，则查询所有数据，并且覆盖redis中的数据 key不存在 1:第一次执行 2 redis被清理
        LocalDateTime lastExeTime = null;
        String lastExeTimeStr = valOpsStr.get(exeTimeKey);
        if (StringUtils.isNotEmpty(lastExeTimeStr)) {
            DateTimeFormatter ofPattern = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
            lastExeTime = LocalDateTime.parse(lastExeTimeStr, ofPattern);
        }
        LocalDateTime currentTime = LocalDateTime.now();
        final LocalDateTime startTime = lastExeTime;
        if (cache) {
            klinePairLogs = klineLogService.findStatisticsTradeByCoinMain(config.getCoinMain(), config.getCoinOther(), KlineLog.class, startTime, currentTime);
        } else {
            DateTimeFormatter ofPattern = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
            klinePairLogs = klineLogService.findStatisticsTradeByCoinMain(config.getCoinMain(), config.getCoinOther(), KlineLog.class, null, LocalDateTime.parse("20181106210359", ofPattern));
            logger.info("初始化特殊交易对,mainCoin:{},coinOther:{},交易数据大小size:{}", config.getCoinMain(), config.getCoinOther(), klinePairLogs.size());
        }
        if (!CollectionUtils.isEmpty(klinePairLogs)) {
            final KlineLog end = klinePairLogs
                    .stream().max(Comparator.comparing(KlineLog::getTradeTime)).get();
            final KlineLog start = klinePairLogs
                    .stream().min(Comparator.comparing(KlineLog::getTradeTime)).get();

            List<RedisMatchStream> matchStreams = klinePairLogs.stream().map(klinePairLog -> {
                RedisMatchStream matchStream = new RedisMatchStream();
                matchStream.setCoinMain(klinePairLog.getCoinMain());
                matchStream.setCoinOther(klinePairLog.getCoinOther());
                matchStream.setMinuteTime(klinePairLog.getMinuteTime());
                matchStream.setPrice(klinePairLog.getPrice());
                matchStream.setTotalVolume(null);
                matchStream.setTradeTime(klinePairLog.getTradeTime());
                matchStream.setType(null);
                matchStream.setVolume(klinePairLog.getVolume());
                return matchStream;
            }).collect(Collectors.toList());

            TimingWheel timingWheel = null;
            if (config.getDayType().getMsg().contains("h")) {
                LocalTime endTimeKline = LocalTime.of(end.getMinuteTime().getHour(), 0, 0);
                final LocalDateTime endDate = LocalDateTime.of(end.getMinuteTime().toLocalDate(), endTimeKline);
                LocalTime startTimeKline = LocalTime.of(start.getMinuteTime().getHour(), 0, 0);
                final LocalDateTime startDate = LocalDateTime.of(start.getMinuteTime().toLocalDate(), startTimeKline);
                timingWheel = new TimingWheel(startDate, endDate,
                        config.getDayType().getTime(), ChronoUnit.MINUTES);
                timingWheel.addWheels(matchStreams);
            } else if (config.getDayType().getMsg().contains("d")) {
                LocalTime endTime = LocalTime.of(0, 0, 0);
                final LocalDateTime endDate = LocalDateTime.of(end.getMinuteTime().toLocalDate(), endTime);
                LocalTime startTimeKline = LocalTime.of(0, 0, 0);
                final LocalDateTime startDate = LocalDateTime.of(start.getMinuteTime().toLocalDate(), startTimeKline);
                timingWheel = new TimingWheel(startDate, endDate, 1, ChronoUnit.DAYS);
                timingWheel.addWheels(matchStreams);
            } else if (config.getDayType().getMsg().contains("w")) {
                LocalTime endTime = LocalTime.of(0, 0, 0);
                final LocalDateTime endDate = LocalDateTime.of(end.getMinuteTime().toLocalDate(), endTime);
                LocalTime startTimeKline = LocalTime.of(0, 0, 0);
                final LocalDateTime startDate = LocalDateTime.of(start.getMinuteTime().toLocalDate(), startTimeKline);
                timingWheel = new TimingWheel(startDate, endDate, 7, ChronoUnit.DAYS);
                timingWheel.addWheels(matchStreams);
            } else if (config.getDayType().getMsg().contains("m")) {
                timingWheel = new TimingWheel(start.getMinuteTime(), end.getMinuteTime(),
                        config.getDayType().getTime(), ChronoUnit.MINUTES);
                timingWheel.addWheels(matchStreams);
            } else {
                LocalTime endTime = LocalTime.of(0, 0, 0);
                final LocalDateTime endDate = LocalDateTime.of(end.getMinuteTime().toLocalDate(), endTime);
                LocalTime startTimeKline = LocalTime.of(0, 0, 0);
                final LocalDateTime startDate = LocalDateTime.of(start.getMinuteTime().toLocalDate(), startTimeKline);
                timingWheel = new TimingWheel(startDate, endDate, 30, ChronoUnit.DAYS);
                timingWheel.addWheels(matchStreams);
            }

            final List<TimingWheel.BucketValue<KlineVO>> compute = timingWheel.compute(rms -> {
                KlineVO klineVO = new KlineVO();
                final RedisMatchStream maxPrice = rms.stream().max(Comparator.comparing(RedisMatchStream::getPrice)).get();
                klineVO.setH(maxPrice.getPrice().toPlainString());

                final RedisMatchStream minPrice = rms.stream().min(Comparator.comparing(RedisMatchStream::getPrice)).get();
                klineVO.setL(minPrice.getPrice().toPlainString());

                final double totalVolume = rms.stream().mapToDouble(r -> r.getVolume().doubleValue()).sum();
                klineVO.setV(new BigDecimal(totalVolume).setScale(8, RoundingMode.HALF_DOWN).toPlainString());
                final RedisMatchStream openPrice = rms
                        .stream().min(Comparator.comparing(RedisMatchStream::getTradeTime)).get();
                final RedisMatchStream closePrice = rms
                        .stream().max(Comparator.comparing(RedisMatchStream::getTradeTime)).get();
                klineVO.setO(openPrice.getPrice().toPlainString());
                klineVO.setC(closePrice.getPrice().toPlainString());
                return klineVO;
            });
            List<KlineVO> vos = compute.stream().map(e -> {
                final KlineVO value = e.getValue();
                value.setT(String.valueOf(e.getTime().toInstant(ZoneOffset.of("+8")).toEpochMilli()));
                value.setS("ok");
                return value;
            }).collect(Collectors.toList());
            timingWheel.clear();
            final String pairVOKey = RedisKeyConstant.buildTaskKlineStatKey(config.getCoinMain(), config.getCoinOther(), config.getDayType().getMsg());
            if (!CollectionUtils.isEmpty(vos)) {
                vos.forEach(vo -> {
                    redisTemplate.opsForList().leftPushAll(pairVOKey, vo);
                });
                klineStatLogService.batchInsert(vos.stream().map(vo -> {
                    KlineStatLog klineStatLog = new KlineStatLog();
                    klineStatLog.setC(vo.getC());
                    klineStatLog.setCoinMain(config.getCoinMain());
                    klineStatLog.setCoinOther(config.getCoinOther());
                    klineStatLog.setH(vo.getH());
                    klineStatLog.setKlineTimeUnit(config.getDayType().getMsg());
                    klineStatLog.setL(vo.getL());
                    klineStatLog.setO(vo.getO());
                    klineStatLog.setT(vo.getT());
                    final LocalDateTime tradeTime = LocalDateTime
                            .ofEpochSecond(Long.parseLong(vo.getT()) / 1000, 0, ZoneOffset.ofHours(8));
                    klineStatLog.setTradeTime(tradeTime);
                    klineStatLog.setV(vo.getV());
                    return klineStatLog;
                }).collect(Collectors.toList()));
            }
            long increSize = redisTemplate.opsForList().size(pairVOKey);
            if (increSize > 100000) {
                // 保存0-100000之间的数据
                redisTemplate.opsForList().trim(pairVOKey, 0, 100000);
            }
            if (cache) {
                valOpsStr.set(exeTimeKey, DateUtils.formaterLocalDateTime(currentTime, "yyyyMMddHHmmss"));
            }
        }

    }
}
