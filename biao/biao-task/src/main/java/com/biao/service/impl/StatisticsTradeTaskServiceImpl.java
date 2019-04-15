package com.biao.service.impl;

import com.biao.config.AliYunOOSClientConfig;
import com.biao.constant.RedisKeyConstant;
import com.biao.entity.PlatUser;
import com.biao.entity.PlatUserSyna;
import com.biao.entity.ReportTradeDay;
import com.biao.entity.ReportTradeFree;
import com.biao.enums.TradePairEnum;
import com.biao.kafka.Producer;
import com.biao.reactive.data.mongo.domain.MatchStream;
import com.biao.reactive.data.mongo.domain.RedisMatchStream;
import com.biao.reactive.data.mongo.domain.StatTradePair;
import com.biao.reactive.data.mongo.domain.kline.KlineLog;
import com.biao.reactive.data.mongo.repository.CardRepository;
import com.biao.reactive.data.mongo.service.GridFsTemplateService;
import com.biao.reactive.data.mongo.service.KlineLogService;
import com.biao.reactive.data.mongo.service.MatchStreamService;
import com.biao.reactive.data.mongo.service.TradeDetailService;
import com.biao.service.*;
import com.biao.util.DateUtils;
import com.biao.util.SnowFlake;
import com.biao.utils.AliyunOOSUtils;
import com.biao.vo.ReportTradeFreeVO;
import com.biao.vo.TradePairVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class StatisticsTradeTaskServiceImpl implements StatisticsTradeTaskService {

    private static Logger logger = LoggerFactory.getLogger(StatisticsTradeTaskServiceImpl.class);

    private ExecutorService executorService = Executors.newFixedThreadPool(2);

    @Autowired
    private MatchStreamService matchStreamService;
    @Autowired
    private TradeDetailService tradeDetailService;
    @Autowired
    private ReportTradeFreeService reportTradeFreeService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Resource(name = "stringRedisTemplate")
    private ValueOperations<String, String> valOpsStr;
    @Autowired
    private MongoTemplate template;
    @Autowired
    private Producer producer;
    @Autowired
    private ReportTradeDayService reportTradeDayService;
    @Autowired
    private UserPhoneGeocoderService userPhoneGeocoderService;
    @Autowired
    private PlatUserSynaService platUserSynaService;
    @Autowired
    private PlatUserService platUserService;
    @Autowired
    private GridFsTemplateService gridFsTemplateService;
    @Autowired
    public CardRepository cardRepository;
    @Autowired
    private AliYunOOSClientConfig clientConfig;
    @Autowired
    private KlineLogService klineLogService;

    /**
     * 主区交易对
     */
    @Value("${maincoin:ETH,USDT,BTC,CNB,USDB}")
    private String coinMains;

    /**
     * 用户同步文件目录
     */
    @Value("${userSynDir}")
    private String userSynDir;

    @Override
    public void statisticsTradeTask(String coinMain) {
        List<TradePairVO> pairVOs = matchStreamService.findStatisticsTradeByCoinMain(coinMain);
        if (!CollectionUtils.isEmpty(pairVOs)) {
            // 把主区存放redis中
            pairVOs.stream().forEach(pairVO -> {
                BigDecimal rise = new BigDecimal(0);
                if (pairVO.getFirstPrice().compareTo(new BigDecimal(0)) != 0) {
                    rise = pairVO.getLatestPrice().subtract(pairVO.getFirstPrice())
                            .divide(pairVO.getFirstPrice(), 8, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
                }
                pairVO.setRise(rise.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "%");
                pairVO.setFirstPrice(pairVO.getFirstPrice().setScale(8, BigDecimal.ROUND_HALF_UP));
                pairVO.setHighestPrice(pairVO.getHighestPrice().setScale(8, BigDecimal.ROUND_HALF_UP));
                pairVO.setLatestPrice(pairVO.getLatestPrice().setScale(8, BigDecimal.ROUND_HALF_UP));
                pairVO.setLowerPrice(pairVO.getLowerPrice().setScale(8, BigDecimal.ROUND_HALF_UP));
                redisTemplate.opsForHash().put(TradePairEnum.buildTradeKey(coinMain), pairVO.getCoinOther(), pairVO);
            });
            // 发送消息通知推送系统
            logger.info("coinMain = {},发送统计完成消息。。。", coinMain);
            producer.send(RedisKeyConstant.TRADE_STATISTIC_TOPIC, coinMain, coinMain);
        }
    }

    @Override
    public void everyMinForDayTrade(String coinMain) {
        LocalDate date = LocalDate.now();
        String redisFeild = DateUtils.formaterDate(date);
        logger.info("everyMinForDayTrade redisFeild = {}", redisFeild);
        List<TradePairVO> tradePairVOs = matchStreamService.statEveryMinForDayTrade(coinMain);
        Map<String, List<TradePairVO>> collect = tradePairVOs.stream().collect(Collectors
                .groupingBy(v -> RedisKeyConstant.buildTaskStatTradeToDay(v.getCoinMain(), v.getCoinOther())));
        collect.forEach((key, value) -> redisTemplate.opsForHash().put(key, redisFeild, value));
        List<StatTradePair> datas = tradePairVOs.stream()
                .map(tradePairVO -> StatTradePair.convertFromVo(tradePairVO, date)).collect(Collectors.toList());
        Optional.ofNullable(datas).ifPresent(dataPresent -> template.insertAll(dataPresent));
    }

    @Override
    public void statisticsTradeDay() {
        List<String> coinMainsList = new ArrayList<>();
        if (StringUtils.isNotBlank(coinMains)) {
            Stream.of(coinMains.split(",")).distinct().forEach(coin -> coinMainsList.add(coin.trim().toUpperCase()));
            ;
        }
        if (coinMainsList.size() < 1) {
            coinMainsList.add("ETH");
            coinMainsList.add("USDT");
            coinMainsList.add("BTC");
        }
        // 获取昨天的时间
        // LocalDateTime curentDate = LocalDateTime.now();
        LocalDateTime curentDate = DateUtils.addDay(LocalDateTime.now(), -1);
        // 开始时间
        LocalDateTime sLocalDateTime = LocalDateTime.of(curentDate.toLocalDate(), LocalTime.of(0, 0, 0));
        // 结束时间
        LocalDateTime eLocalDateTime = LocalDateTime.of(curentDate.toLocalDate(), LocalTime.of(23, 59, 59));
        coinMainsList.forEach(coinMain -> {
            logger.info("start  每天定时统计交易量  主区 coinMain = {} , 统计时间 curentDate = {}", coinMain,
                    DateUtils.formaterLocalDateTime(curentDate));
            List<TradePairVO> tradePairVOs = matchStreamService.findStatisticsTradeByCoinMain(coinMain,
                    MatchStream.class, sLocalDateTime, eLocalDateTime);
            processTradePairVOs(tradePairVOs, curentDate);
            logger.info("end 每天定时统计交易量  主区 coinMain = {} , 统计时间 curentDate = {} , 数据量大小 size = {}", coinMain,
                    DateUtils.formaterLocalDateTime(curentDate), tradePairVOs == null ? 0 : tradePairVOs.size());
        });
    }

    /**
     * 插入数据库
     *
     * @param tradePairVOs 查询结果
     * @param curentDate   统计时间
     */
    private void processTradePairVOs(List<TradePairVO> tradePairVOs, LocalDateTime curentDate) {
        if (tradePairVOs != null && tradePairVOs.size() > 0) {
            LocalDate localDate = curentDate.toLocalDate();
            List<ReportTradeDay> reportTradeDays = tradePairVOs.stream().map(tradePairVO -> {
                ReportTradeDay reportTradeDay = new ReportTradeDay();
                String id = SnowFlake.createSnowFlake().nextIdString();
                BeanUtils.copyProperties(tradePairVO, reportTradeDay);
                reportTradeDay.setCountTime(localDate);
                reportTradeDay.setCreateDate(LocalDateTime.now());
                reportTradeDay.setId(id);
                return reportTradeDay;
            }).collect(Collectors.toList());
            reportTradeDayService.batchInsert(reportTradeDays);
        }
    }

    @Override
    public void incrementStatisticsTradeTask(String coinMain) {
        // 获取上次执行时间
        String exeTimeKey = "increment_statistics_trade_" + coinMain;
        /*
         * redisTemplate.setKeySerializer(new StringRedisSerializer());
         * redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
         * redisTemplate.setHashKeySerializer(new StringRedisSerializer());
         * redisTemplate.setHashValueSerializer(new JdkSerializationRedisSerializer());
         */
        // 上次执行的时间，如果为空，则查询所有数据，并且覆盖redis中的数据 key不存在 1:第一次执行 2 redis被清理
        String lastExeTimeStr = valOpsStr.get(exeTimeKey);
        LocalDateTime lastExeTime = null;
        if (StringUtils.isNotEmpty(lastExeTimeStr)) {
            DateTimeFormatter ofPattern = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
            lastExeTime = LocalDateTime.parse(lastExeTimeStr, ofPattern);
        }
        LocalDateTime currentTime = LocalDateTime.now();
        // 查询主区交易对数据
        List<TradePairVO> pairVOs = matchStreamService.findIncrementStatisticsTradeByCoinMain(coinMain,
                MatchStream.class, lastExeTime, currentTime);
        if (!CollectionUtils.isEmpty(pairVOs)) {
            // 查询所有主区和被交易区的所有数据
            final LocalDateTime startTime = lastExeTime;
            pairVOs.stream().forEach(pairVO -> {
                //String pairVOKey = TradePairEnum.buildIncrementTradeKey(coinMain, pairVO.getCoinOther());
                List<RedisMatchStream> matchStreams = matchStreamService.findIncrementTrades(coinMain,
                        pairVO.getCoinOther(), MatchStream.class, startTime, currentTime);
                //数据转移都mongo
                if (!CollectionUtils.isEmpty(matchStreams)) {
                    List<KlineLog> klineLogs = matchStreams.stream().map(matchStream -> {
                        KlineLog klineLog = new KlineLog();
                        klineLog.setCoinMain(coinMain);
                        klineLog.setCoinOther(matchStream.getCoinOther());
                        klineLog.setCreateTime(LocalDateTime.now());
                        klineLog.setMinuteTime(matchStream.getMinuteTime());
                        klineLog.setPrice(matchStream.getPrice());
                        klineLog.setTradeTime(matchStream.getTradeTime());
                        klineLog.setVolume(matchStream.getVolume());
                        return klineLog;
                    }).collect(Collectors.toList());
                    klineLogService.batchInsert(klineLogs);
                }
            });
        }
        valOpsStr.set(exeTimeKey, DateUtils.formaterLocalDateTime(currentTime, "yyyyMMddHHmmss"));
    }

    @Override
    public void statisticsTradeDetailFree() {
        // 统计交易手续费 0:买入 1:卖出 TradeDetail:交易流水
        LocalDateTime curentDate = DateUtils.addDay(LocalDateTime.now(), -1);
        // 开始时间
        LocalDateTime sLocalDateTime = LocalDateTime.of(curentDate.toLocalDate(), LocalTime.of(0, 0, 0));
        // 结束时间
        LocalDateTime eLocalDateTime = LocalDateTime.of(curentDate.toLocalDate(), LocalTime.of(23, 59, 59));

        // 统计时间
        LocalDate countDate = curentDate.toLocalDate();

        // 买入 手续费收入的是coinOther
        int type = 0;
        List<ReportTradeFreeVO> buyReportTradeFreeVOs = tradeDetailService.countTradeFree(type, sLocalDateTime,
                eLocalDateTime);
        if (!CollectionUtils.isEmpty(buyReportTradeFreeVOs)) {
            List<ReportTradeFree> tradeFrees = convertReportTradeFree(buyReportTradeFreeVOs, 0, countDate);
            reportTradeFreeService.batchInsert(tradeFrees);
        }
        // 卖出 手续费收入的是coinMain
        type = 1;
        List<ReportTradeFreeVO> sellReportTradeFreeVOs = tradeDetailService.countTradeFree(type, sLocalDateTime,
                eLocalDateTime);
        if (!CollectionUtils.isEmpty(sellReportTradeFreeVOs)) {
            List<ReportTradeFree> tradeFrees = convertReportTradeFree(sellReportTradeFreeVOs, 1, countDate);
            if (tradeFrees != null) {
                reportTradeFreeService.batchInsert(tradeFrees);
            }
        }
        // 执行币种的手续费统计
        executorService.execute(() -> {
            try {
                reportTradeFreeService.exeFreeCoins(countDate);
            } catch (Exception e) {
                logger.error("批量统计币种的手续费异常，e:{}", e);
            }
        });

        // 执行币种记录的手续费统计
        executorService.execute(() -> {
            try {
                reportTradeFreeService.exeFreeRecords(countDate);
            } catch (Exception e) {
                logger.error("执行币种记录的手续费统计，e:{}", e);
            }
        });
    }

    @Override
    public void statisticsUserTradeFreesTask() {

    }

    @Override
    public void userGeoOneTask() {
        userPhoneGeocoderService.onceTaskCompletionInsert();
    }

    private List<ReportTradeFree> convertReportTradeFree(List<ReportTradeFreeVO> reportTradeFreeVOs, int type,
                                                         final LocalDate countTime) {
        if (!CollectionUtils.isEmpty(reportTradeFreeVOs)) {
            return reportTradeFreeVOs.stream().map(reportTradeFreeVO -> {
                ReportTradeFree reportTradeFree = new ReportTradeFree();
                reportTradeFree.setCoinOther(reportTradeFreeVO.getCoinOther());
                reportTradeFree.setId(SnowFlake.createSnowFlake().nextIdString());
                reportTradeFree.setCoinMain(reportTradeFreeVO.getCoinMain());
                reportTradeFree.setSumFee(reportTradeFreeVO.getSumFee());
                reportTradeFree.setCountTime(countTime);
                reportTradeFree.setCreateTime(LocalDateTime.now());
                if (type == 0) {
                    // 买入 手续费收入的是coinOther
                    reportTradeFree.setCoin(reportTradeFreeVO.getCoinOther());
                } else {
                    // 卖出 手续费收入的是coinMain
                    reportTradeFree.setCoin(reportTradeFreeVO.getCoinMain());
                }
                return reportTradeFree;
            }).collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public void userSynTask() {
        // 用户同步任务
        // 解析文件、插入数据库
//		String dataDir = DateUtils.formaterDate(LocalDate.now());
//		String parentDir = userSynDir + File.separator + dataDir;
//		File fileDir = new File(parentDir);
//		if (fileDir.exists() && fileDir.isDirectory()) {
//			File[] files = fileDir.listFiles();
//			for (File file : files) {
//				if (file.isFile()) {
//					List<Map<String, Object>> synFiles = ExcelHelper.strartParseFile(parentDir, file.getName());
//					pageBatchSynUser(synFiles);
//				}
//			}
//		} else {
//			logger.info("parentDir = {},同步用户的文件夹不存在", parentDir);
//		}
        // 开启同步数据
        platUserSynaService.synUserToPlatUser();
    }

    @Override
    public void userPhoteTransferTask() {
        List<Integer> status = Arrays.asList(2);
        List<PlatUser> platUsers = platUserService.findByIdcardStatus(status);
        if (!CollectionUtils.isEmpty(platUsers)) {
            platUsers.stream().distinct().forEach(platUser -> {
                PlatUser updatePlat = new PlatUser();
                if (StringUtils.isNotBlank(platUser.getCardUpId())) {
                    GridFsResource file = gridFsTemplateService.imageView(platUser.getCardUpId());
                    if (file != null) {
                        String imageName = uploadImage(file);
                        logger.info("同步用户id:{},原地址:{},正面图片地址:{}", platUser.getId(), platUser.getCardUpId(), imageName);
                        if (imageName != null) {
                            updatePlat.setCardUpId(imageName);
                        }
                    }
                }
                if (StringUtils.isNotBlank(platUser.getCardDownId())) {
                    GridFsResource file = gridFsTemplateService.imageView(platUser.getCardDownId());
                    if (file != null) {
                        String imageName = uploadImage(file);
                        logger.info("同步用户id:{},原地址:{},反面图片地址:{}", platUser.getId(), platUser.getCardDownId(), imageName);
                        if (imageName != null) {
                            updatePlat.setCardDownId(imageName);
                        }
                    }
                }

                if (StringUtils.isNotBlank(platUser.getCardFaceId())) {
                    GridFsResource file = gridFsTemplateService.imageView(platUser.getCardFaceId());
                    if (file != null) {
                        String imageName = uploadImage(file);
                        logger.info("同步用户id:{},原地址:{},手持图片地址:{}", platUser.getId(), platUser.getCardFaceId(), imageName);
                        if (imageName != null) {
                            updatePlat.setCardDownId(imageName);
                        }
                    }
                }
                updatePlat.setId(platUser.getId());
                platUserService.updateById(updatePlat);
            });
        }
    }

    private String uploadImage(GridFsResource file) {
        InputStream inputStream;
        try {
            inputStream = file.getInputStream();
            String fileName = file.getFilename();
            String contentType = "png";
            int index = Objects.requireNonNull(fileName).lastIndexOf(".");
            if (index != -1) {
                contentType = fileName.substring(index + 1, fileName.length());
            }
            String imageName = UUID.randomUUID().toString().replaceAll("-", "") + "." + contentType;
            String idcardBucketName = AliyunOOSUtils.getIdcardBucketName(imageName);
            Map<String, String> userMetadata = new HashMap<>();
            userMetadata.put("simpleImageName", imageName);
            AliyunOOSUtils.uploadToAliyun(clientConfig, inputStream, idcardBucketName, AliyunOOSUtils.createObjectMetadata("application/" + contentType, file.contentLength(), "utf-8", userMetadata));
            return idcardBucketName + "?x-oss-process=style/uesstyle";
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void pageBatchSynUser(List<Map<String, Object>> synFiles) {
        int batchSize = 100;
        int dataSize = synFiles.size();
        if (dataSize > batchSize) {
            int remainder = synFiles.size() % batchSize;
            int pageSize = (remainder == 0) ? (synFiles.size() / batchSize) : ((synFiles.size() / batchSize) + 1);
            logger.info("size:{},page:{}", synFiles.size(), pageSize);
            for (int i = 0; i < pageSize; i++) {
                int realSize = (synFiles.size() > batchSize) ? batchSize : synFiles.size();
                List<Map<String, Object>> subPlatUserSynas = synFiles.subList(0, realSize);
                List<PlatUserSyna> platUserSynas = converPlatUserSyna(subPlatUserSynas);
                handerData(platUserSynas);
                // 剔除
                synFiles.subList(0, realSize).clear();
                logger.info("batch handler size:{}", synFiles.size());
            }
        } else {
            List<PlatUserSyna> platUserSynas = converPlatUserSyna(synFiles);
            handerData(platUserSynas);
        }

    }

    private List<PlatUserSyna> converPlatUserSyna(List<Map<String, Object>> synFiles) {
        if (synFiles != null && synFiles.size() > 0) {
            return synFiles.stream().map(synFile -> {
                PlatUserSyna platUserSyna = new PlatUserSyna();
                String id = SnowFlake.createSnowFlake().nextIdString();
                platUserSyna.setId(id);
                platUserSyna.setMobile(synFile.get("mobile").toString());
                if (synFile.get("source") != null) {
                    platUserSyna.setSource(synFile.get("source").toString());
                }
                platUserSyna.setSourceId(synFile.get("id").toString());
                if (synFile.get("parent_id") != null && StringUtils.isNotBlank(synFile.get("parent_id").toString())) {
                    platUserSyna.setSourceParentId(synFile.get("parent_id").toString());
                }
                platUserSyna.setStatus(0);
                platUserSyna.setSynDate(LocalDateTime.now());
                String createDate = synFile.get("create_date").toString();
                try {
                    int index = createDate.indexOf(":");
                    if (index != -1) {
                        platUserSyna.setCreateDate(DateUtils.parseLocalDateTime(createDate));
                    } else {
                        platUserSyna.setCreateDate(DateUtils.parseLocalDateTime10(createDate));
                    }
                } catch (ParseException e) {
                    logger.error("同步用户创建时间格式不正确,data = {}", synFile.get("create_date").toString());
                }
                return platUserSyna;
            }).collect(Collectors.toList());
        }
        return null;
    }

    private void handerData(List<PlatUserSyna> platUserSynas) {
        platUserSynaService.batchHandler(platUserSynas);
    }

    @Override
    public void userSynSendMessageTask() {
        platUserSynaService.sendMessageToSynUser();
    }

    @Override
    public void clearValidPhotoTask() {
        String imageNameRegx = ".png$";
        List<String> imageNames = gridFsTemplateService.selectPhotos(imageNameRegx);
        if (!CollectionUtils.isEmpty(imageNames)) {
            logger.info("select images size :" + imageNames.size());
            for (String imageName : imageNames) {
                //判断是否存在，存在为身份证图片
                Long count = platUserService.findByImages(imageName);
                if (count == null || count == 0) {
                    gridFsTemplateService.clearPhotosByIds(imageNames);
                }
            }
        }

    }
}
