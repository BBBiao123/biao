package com.biao.service.impl;

import com.biao.constant.Constants;
import com.biao.constant.TradeConstant;
import com.biao.current.threadpool.BbexThreadFactory;
import com.biao.current.threadpool.BbexThreadPool;
import com.biao.entity.Order;
import com.biao.entity.UserCoinVolume;
import com.biao.enums.OrderEnum;
import com.biao.enums.TradeEnum;
import com.biao.exception.PlatException;
import com.biao.kafka.interceptor.SampleMessage;
import com.biao.mapper.OrderDao;
import com.biao.pojo.BatchCancelTradeDTO;
import com.biao.pojo.GlobalMessageResponseVo;
import com.biao.pojo.TradeDto;
import com.biao.pojo.TradeVo;
import com.biao.query.UserTradeQuery;
import com.biao.redis.RedisCacheManager;
import com.biao.service.TradeService;
import com.biao.service.UserCoinVolumeExService;
import com.biao.util.SnowFlake;
import com.biao.util.TradeCompute;
import com.biao.vo.redis.RedisExPairVO;
import com.google.common.collect.Range;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.concurrent.ListenableFutureCallback;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 *
 * @date 2018/4/6
 * 交易实现操作；
 */
@SuppressWarnings("all")
@Component
public class TradeServiceImpl implements TradeService {
    private final Logger logger = LoggerFactory.getLogger(TradeServiceImpl.class);
    @Autowired(required = false)
    private UserCoinVolumeExService userCoinVolumeService;
    /**
     * kafka消息发送模板
     */
    private final KafkaTemplate<Object, SampleMessage> kafkaTemplate;
    @Resource(name = "redisTemplate")
    private RedisTemplate redisTemplate;

    TradeServiceImpl(KafkaTemplate<Object, SampleMessage> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * order处理;
     */
    @Autowired(required = false)
    private OrderDao orderDao;

    @Autowired
    private RedissonClient rsclient;

    @Autowired
    private RedisCacheManager redisCacheManager;

    @Autowired
    private PlatformTransactionManager transactionManager;

    /**
     * 一个异步取消的队列线程信息.
     */
    private AsyncCancelTrade asyncCancelTrade;


    @PostConstruct
    public void initAsyncCancelTrade() {
        asyncCancelTrade = new AsyncCancelTrade();
    }

    /**
     * 获取一个订单号处理；
     *
     * @param <T>
     * @return 返回数据；
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public <T> Mono<T> getOrderNo() {
        String orderNo = SnowFlake.createSnowFlake().nextIdString();
        TradeVo vo = new TradeVo();
        vo.setOrderNo(orderNo);
        return Mono.just((T) GlobalMessageResponseVo.newSuccessInstance(vo));
    }

    /**
     * 买入数据的处理；
     *
     * @param tradeVo 数据对象；
     * @param <T>
     * @return 操作结果；
     */
    @Override
    public <T> Mono<T> buyIn(TradeVo tradeVo) {
        DefaultTransactionDefinition defaultTransactionDefinition = new DefaultTransactionDefinition();
        defaultTransactionDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus transaction = transactionManager.getTransaction(defaultTransactionDefinition);
        GlobalMessageResponseVo gms;
        try {
            gms = useTrade(tradeVo, TradeEnum.BUY);
            transactionManager.commit(transaction);
        } catch (RuntimeException ex) {
            transactionManager.rollback(transaction);
            gms = GlobalMessageResponseVo.newErrorInstance(ex.getMessage());
        }
        if (Objects.equals(Constants.SUCCESS_CODE, gms.getCode())) {
            return sendToExternal((TradeDto) gms.getData());
        } else {
            return Mono.just((T) gms);
        }

    }

    /**
     * 买入与卖出的业务处理；
     *
     * @param tradeVo   业务处理；
     * @param tradeEnum 操作的类型
     * @param <T>
     * @return 操作结果；
     * @see TradeEnum
     */
    @SuppressWarnings("all")
    private GlobalMessageResponseVo useTrade(TradeVo tradeVo, TradeEnum tradeEnum) {
        /*
         * 1.参数校验
         *  所有参数不允许为null
         *  2.数量>0
         *  3.单价格大于>0
         */
        if (logger.isDebugEnabled()) {
            logger.debug("{}", tradeVo);
        }
        try {
            check(tradeVo);
        } catch (RuntimeException ex) {
            logger.error("{}:{}", ex.getMessage(), tradeVo);
            return GlobalMessageResponseVo.newInstance(TradeConstant.TRAND_FAILURE, ex.getMessage());
        }
        TradeDto tradeDto = TradeDto
                .transform(tradeVo, tradeEnum);
        //发送到mq前进入交易队列
        /*
         * 1. 检查用户资才，锁定用户资产（根据产易对的数据）
         * 2. 加入redis买队列
         * 3. 落库一个订单
         * 4. 发送一个MQ消息。
         */
        UserCoinVolume volume = userCoinVolumeService.findByUserIdAndCoinSymbol(tradeDto.getUserId(), tradeDto.getTradeCoin());
        if (volume == null) {
            logger.warn("{}用户没有查询{}相应的资产信息！", tradeDto.getUserId(), tradeDto.getTradeCoin());
            return GlobalMessageResponseVo.newInstance(TradeConstant.TRAND_FAILURE, "没有资产信息！");
        }
        if (volume.getVolume() == null || volume.getVolume().doubleValue() <= 0) {
            logger.warn("{}用户查询到资产{}相应的资产为0！", tradeDto.getUserId(), tradeDto.getTradeCoin());
            return GlobalMessageResponseVo.newInstance(TradeConstant.TRAND_FAILURE, "资产不足！");
        }
        //判断当前的资产信息是否可以下单.
        boolean check = volume.getFlag() != null && volume.getFlag() == 1;
        if (check) {
            logger.warn("{}用户查询到资产{}相应的资产已锁定！", tradeDto.getUserId(), tradeDto.getTradeCoin());
            return GlobalMessageResponseVo.newInstance(TradeConstant.TRAND_FAILURE, volume.getFlagMark());
        }
        //比较资产是不是可以购买当前的币数量。
        //需要东结的资产估计值
        BigDecimal blockVolume = tradeDto.getBlockVolume();
        BigDecimal userVolume = volume.getVolume();
        //得到资产交易可能使用的最大所需资产数
        boolean expect = blockVolume.compareTo(userVolume) == 1;
        if (expect) {
            logger.warn("{}用户查询到资产{}相应的资产为{}超出范围！", tradeDto.getUserId(), tradeDto.getTradeCoin(), volume.getVolume());
            return GlobalMessageResponseVo.newInstance(TradeConstant.TRAND_FAILURE, "资产不足！");
        }
        //冻结资产（需考虑并发问题）
        //得到相应的资产信息；
        long count = userCoinVolumeService.addLockVolume(
                volume.getUserId(),
                volume.getCoinSymbol(),
                blockVolume, true);
        if (count <= 0) {
            return GlobalMessageResponseVo.newInstance(Constants.COMMON_ERROR_CODE, "修改资产失败！(有可能是资产不足)");
        }
        /*
         * 以下是真实的开始处理；
         */
        long su = insertOrder(tradeEnum, tradeDto, volume.getUserId());
        if (su <= 0) {
            logger.error("用户挂单失败!");
            throw new RuntimeException("用户挂单失败！");
        }
        //以下为操作Redis;与处理；
        redisTemplate.opsForHash()
                .put(TradeConstant.TRADE_PREPOSITION_KEY, tradeDto.getOrderNo(), tradeDto);
        return GlobalMessageResponseVo.newSuccessInstance(tradeDto);
    }

    private <T> Mono<T> sendToExternal(TradeDto tradeDto) {
        SampleMessage sampleMessage = SampleMessage.build(tradeDto);
        //发送
        kafkaTemplate.send(TradeConstant.TRADE_KAFKA_TOPIC, tradeDto.ackKey(), sampleMessage)
                .addCallback(new ListenableFutureCallback() {
                    @Override
                    public void onSuccess(Object result) {
                        logger.info("发送订单数据到撮合引擎:{}", tradeDto.getOrderNo());
                    }

                    @Override
                    public void onFailure(Throwable ex) {
                        logger.error("发送订单数据到撮合引擎失败{}", tradeDto, ex);
                    }
                });
        //发送挂单消息通知。
        kafkaTemplate.send(TradeConstant.TRADE_KAFKA_ORDER_BUYANDSELL_TOPIC, tradeDto.ackKey(),
                SampleMessage.build(tradeDto));
        return Mono.just((T) GlobalMessageResponseVo.newInstance(Constants.SUCCESS_CODE, "挂单成功!"));
    }

    /**
     * 插入一个订单信息；
     *
     * @param tradeEnum 操作类型；
     * @param tradeDto  操作对象；
     * @param volume    用户资产信息；
     */
    private long insertOrder(TradeEnum tradeEnum, TradeDto tradeDto, String userId) {
        try {
            Order order = new Order();
            order.setPrice(tradeDto.getPrice())
                    .setAskVolume(tradeDto.getVolume())
                    .setSuccessVolume(BigDecimal.ZERO)
                    .setToCoinVolume(BigDecimal.ZERO)
                    .setSpentVolume(BigDecimal.ZERO)
                    .setExFee(BigDecimal.ZERO)
                    .setCoinId(tradeDto.getCoinManId())
                    .setCoinSymbol(tradeDto.getTradeCoin())
                    .setUserId(userId)
                    .setFlag(OrderEnum.OrderFlag.YES.ordinal())
                    .setStatus(OrderEnum.OrderStatus.NOT_SUCCESS.getCode())
                    .setAskCoinId(tradeDto.getCoinOtherId())
                    .setToCoinSymbol(tradeDto.getToTradeCoin())
                    .setCoinMain(tradeDto.getCoinMain())
                    .setCoinOther(tradeDto.getCoinOther())
                    .setExType(tradeEnum.ordinal());
            order.setId(tradeDto.getOrderNo());
            order.setCreateDate(LocalDateTime.now());
            order.setUpdateDate(LocalDateTime.now());
            return orderDao.insert(order);
        } catch (Exception ex) {
            logger.error("创建订单失败:", ex);
            throw new RuntimeException("创建订单失败");
        }
    }

    /**
     * 卖出的业务处理；
     *
     * @param tradeVo 数据对象；
     * @param <T>     类型
     * @return 结果；
     */
    @Override
    public <T> Mono<T> sellOut(TradeVo tradeVo) {
        DefaultTransactionDefinition defaultTransactionDefinition = new DefaultTransactionDefinition();
        defaultTransactionDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus transaction = transactionManager.getTransaction(defaultTransactionDefinition);
        GlobalMessageResponseVo gms;
        try {
            gms = useTrade(tradeVo, TradeEnum.SELL);
            transactionManager.commit(transaction);
        } catch (RuntimeException ex) {
            transactionManager.rollback(transaction);
            gms = GlobalMessageResponseVo.newErrorInstance(ex.getMessage());
        }
        if (Objects.equals(Constants.SUCCESS_CODE, gms.getCode())) {
            return sendToExternal((TradeDto) gms.getData());
        } else {
            return Mono.just((T) gms);
        }
    }


    /**
     * 取消订单的业务处理；；
     *
     * @param tradeVo 数据对象；
     * @param <T>     类型
     * @return 结果；
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public <T> Mono<T> cancelTrade(String userId, String orderNo) {
        checkNotNull(orderNo);
        TradeDto tradeDto = new TradeDto();
        tradeDto.setOrderNo(orderNo);
        tradeDto.setUserId(userId);
        SampleMessage sampleMessage = SampleMessage.build(tradeDto);
        //发送
        kafkaTemplate.send(TradeConstant.TRADE_KAFKA_TOPIC, tradeDto.ackKey(), sampleMessage)
                .addCallback(new ListenableFutureCallback() {
                    @Override
                    public void onSuccess(Object result) {
                        logger.info("发送取取消订单数据到撮合引擎成功:{}", tradeDto.getOrderNo());
                    }

                    @Override
                    public void onFailure(Throwable ex) {
                        logger.error("发送取取消订单数据到撮合引擎失败{}", tradeDto, ex);
                    }
                });
        return Mono.just((T) GlobalMessageResponseVo.newInstance(Constants.SUCCESS_CODE, "发送取消订单消息成功!"));

    }

    @Override
    public <T> Mono<T> batchCancelTrade(BatchCancelTradeDTO dto) {
        //1.根据订单号和交易对，查询未交易或部分交易的订单信息
        //2.提交订单号到异步取消队列.
        UserTradeQuery query = new UserTradeQuery();
        query.setUserId(dto.getUserId());
        query.setCoinMain(dto.getCoinMain());
        query.setCoinOther(dto.getCoinOther());
        query.setStatus(0);
        List<Order> listByQuery = orderDao.findListByQuery(query);
        if (listByQuery == null || listByQuery.isEmpty()) {
            return Mono.just((T) GlobalMessageResponseVo.newInstance(Constants.SUCCESS_CODE, "没有可以取消的订单!"));
        }
        Optional.ofNullable(listByQuery).ifPresent(e -> {
            e.forEach(ddo -> {
                String id = ddo.getId();
                asyncCancelTrade.put(dto.getUserId(), id);
            });
        });
        return Mono.just((T) GlobalMessageResponseVo.newInstance(Constants.SUCCESS_CODE, "批量取消提交成功!共提交" + listByQuery.size() + "笔订单."));
    }

    /**
     * 参数校验
     *
     * @param vo 对象;
     * @return 数据对象；
     */
    private void check(TradeVo vo) {
        checkNotNull(vo);
        checkArgument(StringUtils.isNoneBlank(vo.getOrderNo()));
        checkArgument(vo.getVolume().doubleValue() > 0, Constants.COMMON_ERROR_CODE);
        checkArgument(vo.getPrice().doubleValue() > 0, Constants.COMMON_ERROR_CODE);
        //需要校验最大数量；s
        RedisExPairVO redisExPairVO = redisCacheManager.acquireExPair(vo.getCoinMain(), vo.getCoinOther());
        if (redisExPairVO == null) {
            throw new PlatException(TradeConstant.TRAND_FAILURE, "没有可以使用的交易对信息！");
        }
        //如果有一个为空，则表示所有范围
        Range<BigDecimal> range;
        if (StringUtils.isBlank(redisExPairVO.getMinVolume()) || StringUtils.isBlank(redisExPairVO.getMaxVolume())) {
            range = Range.all();
        } else {
            range = Range.closed(new BigDecimal(redisExPairVO.getMinVolume()), new BigDecimal(redisExPairVO.getMaxVolume()));
        }
        int scale = vo.getVolume().scale();
        if (scale > redisExPairVO.getVolumePrecision().intValue()) {
            throw new PlatException(TradeConstant.TRAND_FAILURE, "数量小数位数错误,小数位小于" + scale + "!");
        }
        scale = vo.getPrice().scale();
        if (scale > redisExPairVO.getPricePrecision().intValue()) {
            throw new PlatException(TradeConstant.TRAND_FAILURE, "价格小数位数错误,小数位小于" + scale + "!");
        }
        //判断是不是在这个范围里。
        boolean contains = range.contains(vo.getVolume());
        if (!contains) {
            throw new PlatException(TradeConstant.TRAND_FAILURE, "超出可挂单的范围," + range.toString() + "!");
        }
    }

    /**
     * 异步取消交易的处理.
     */
    private class AsyncCancelTrade {

        private final Logger logger = LoggerFactory.getLogger(AsyncCancelTrade.class);
        /**
         * 保存异步队列的信息.
         * left userId  right:orderNo
         */
        private LinkedBlockingQueue<Pair<String, String>> ctQueue = new LinkedBlockingQueue<>(100000);

        AsyncCancelTrade() {
            start();
        }

        /**
         *
         */
        public void start() {
            logger.info("启动交易批量取消线程.");
            ExecutorService threads = BbexThreadPool.newSingleThreadExecutor(BbexThreadFactory.create("trade_batch_cancel_trade_thread", false));
            threads.execute(() -> take());
        }

        public void put(String userId, String orderNo) {
            if (StringUtils.isNotBlank(orderNo)) {
                try {
                    ctQueue.put(Pair.of(userId, orderNo));
                    logger.info("批量取消->发送一个订单号到异步队列:{}", orderNo);
                } catch (InterruptedException e) {
                    logger.error("put ctQueue error:{}", e);
                }
            }
        }

        private void take() {
            Pair<String, String> pair = null;
            while (true) {
                try {
                    pair = ctQueue.take();
                    if (pair != null) {
                        DefaultTransactionDefinition defaultTransactionDefinition = new DefaultTransactionDefinition();
                        defaultTransactionDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
                        TransactionStatus transaction = transactionManager.getTransaction(defaultTransactionDefinition);
                        try {
                            String userId = pair.getLeft();
                            String orderNo = pair.getRight();
                            TradeServiceImpl.this.cancelTrade(userId, orderNo).subscribe(e -> {
                                GlobalMessageResponseVo msg = (GlobalMessageResponseVo) e;
                                logger.info("ctQueue：{}笔正在等待取消,取消操作结果响应：{}", ctQueue.size(), msg);
                            });
                            transactionManager.commit(transaction);
                        } catch (RuntimeException ex) {
                            logger.error("  Cancel Trade  commit transaction error: order no is {} ", pair, ex);
                            transactionManager.rollback(transaction);
                        }
                    }
                } catch (Exception ex) {
                    logger.error(" Cancel Trade error: order no is {}", pair, ex);
                }
            }
        }
    }
}
