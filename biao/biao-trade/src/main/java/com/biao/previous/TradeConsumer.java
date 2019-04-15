package com.biao.previous;

import com.biao.constant.TradeConstant;
import com.biao.current.threadpool.BbexThreadFactory;
import com.biao.entity.UserCoinFee;
import com.biao.kafka.interceptor.SampleMessage;
import com.biao.mapper.UserCoinFeeDao;
import com.biao.pojo.TradeDto;
import com.biao.pojo.TradeUserFeeNotify;
import com.biao.previous.cache.UserFeeCache;
import com.biao.previous.domain.ProcessData;
import com.biao.previous.main.*;
import com.biao.previous.message.*;
import com.biao.previous.queue.QueueDistribute;
import com.biao.previous.queue.QueueProviderManage;
import com.biao.previous.queue.QueueType;
import com.biao.previous.thread.ConsistentHashSelector;
import com.biao.previous.thread.SingletonExecutor;
import com.biao.redis.RedisCacheManager;
import com.biao.service.OrderService;
import com.biao.service.UserCoinVolumeExService;
import com.biao.spring.SpringBeanFactoryContext;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 *
 * @date 2018/4/7
 * 监听一个客户端；
 * 接收trade信息。
 * 接收到信息的统一入口。所有信息从这里开始处理
 */
@Component
public final class TradeConsumer {

    /**
     * 操作mongo
     */
    @Autowired
    private ReactiveMongoTemplate mongoTemplate;

    /**
     * 操作Reids;
     */
    @Resource(name = "redisTemplate")
    private RedisTemplate redisTemplate;

    @Autowired
    private RedissonClient redissonClient;
    /**
     * kafka处理;
     */
    @Autowired(required = false)
    private KafkaTemplate kafkaTemplate;
    /**
     * 获取一些redis处理；
     */
    @Autowired
    private RedisCacheManager redisCacheManager;


    @Autowired
    SpringBeanFactoryContext context;

    @Autowired
    UserCoinFeeDao userCoinFeeDao;
    /**
     * 交易消息的流水处理；
     */
    private QueueDistribute distribute = QueueDistribute.get();
    /**
     * 初始化一个选择器;
     */
    private ConsistentHashSelector selector;

    private OrderService orderService;

    private Logger logger = LoggerFactory.getLogger(TradeConsumer.class);

    @PostConstruct
    public void init() {
        //拿出orderDao数据；
        orderService = SpringBeanFactoryContext.findBean(OrderService.class);
        UserCoinVolumeExService userCoinVolumeService = SpringBeanFactoryContext.findBean(UserCoinVolumeExService.class);
        //初始化一个业务执行的enw线程池；
        QueueProviderManage manage = new QueueProviderManage(
                new QueueTradeDetailExecutor.QueueTradeDetailExecutorFactory()
                        .addSubscribers(new TradeDetailConsumerByDetail(mongoTemplate))
                        .addSubscribers(new TradeDetailConsumerByMatch(mongoTemplate, kafkaTemplate)));
        manage.start();
        distribute.provider(QueueType.TR_DE_MESSAGE, manage::getProvider);
        //启动一个订单结果的队列处理器；
        QueueProviderManage manage2 = new QueueProviderManage(
                new QueueTradeResultExecutor.QueueTradeResultExecutorFactory()
                        .addSubscribers(new OrderConsumer(orderService, kafkaTemplate))
                        .addSubscribers(new CancelOrderConsumer(orderService, kafkaTemplate))
                        .addSubscribers(new CancelUserCoinVolumeConsumer(userCoinVolumeService))
                        .addSubscribers(new UserCoinVolumeConsumer(userCoinVolumeService)));
        manage2.start();
        distribute.provider(QueueType.TR_RE_MESSAGE, manage2::getProvider);
        int threads = Runtime.getRuntime().availableProcessors() << 1;
        List<SingletonExecutor> selects = new ArrayList<>();
        for (int i = 0; i < threads; i++) {
            selects.add(new SingletonExecutor("trade_consumer_exe" + i));
        }
        selector = new ConsistentHashSelector(selects);
        //调度线程
        loadUserFee();
        ScheduledExecutorService service =
                new ScheduledThreadPoolExecutor(1, BbexThreadFactory.create("trade-user-fee-task", false));
        service.scheduleWithFixedDelay(this::loadUserFee,
                300, 300, TimeUnit.SECONDS);
    }
    private void loadUserFee() {
        //初始化用户的缓存信息
        List<UserCoinFee> list = userCoinFeeDao.list();
        list.stream().map(e -> {
            TradeUserFeeNotify notify = new TradeUserFeeNotify();
            notify.setUserId(e.getUserId());
            notify.setPairOther(e.getPairOther());
            notify.setFee(e.getFee());
            notify.setPairOne(e.getPairOne());
            notify.setStatus(e.getStatus());
            return notify;
        }).forEach(UserFeeCache::op);
    }
    /**
     * @param message 接收到的message消息。
     */
    @KafkaListener(topics = TradeConstant.TRADE_KAFKA_TOPIC)
    public void message(SampleMessage message) {
        TradeDto dto = message.getMessage(TradeDto.class);
        if (dto == null) {
            return;
        }
        next(dto);
    }

    /**
     * 下一个服务处理;
     *
     * @param dto dto;
     */
    private void next(TradeDto dto) {
        DataChain chain = null;
        if(dto.getType() == null) {
            chain = cancelChain();
        }else {
            switch (dto.getType()) {
                case BUY:
                    chain = buyChain();
                    break;
                case SELL:
                    chain = sellChain();
                    break;
                default:
                    break;
            }
        }
        selector.select(dto.ackKey()).execute(new NextTask(chain, dto));
    }

    /**
     * 关于买入的处理链实现；
     *
     * @return 处理链信息；
     */
    private DataChain buyChain() {
        return new DataChain()
                .addFilter(new PrepositionFilter())
                .addFilter(new BuyInFilter(distribute,
                        redisCacheManager))
                .addFilter(new FallbackFilter())
                .addFilter(new EndFilter())
                .setRedis(redisTemplate)
                .setRedisson(redissonClient)
                .setKafka(kafkaTemplate);
    }

    /**
     * 取消资产的相关处理.
     * @return 取消.
     */
    private DataChain cancelChain(){
        return new DataChain()
                .addFilter(new PrepositionFilter())
                .addFilter(new CancelFilter(orderService,
                        distribute,
                        redisCacheManager))
                .addFilter(new FallbackFilter())
                .addFilter(new EndFilter())
                .setRedis(redisTemplate)
                .setRedisson(redissonClient)
                .setKafka(kafkaTemplate);
    }

    /**
     * 关于买出的处理链实现；
     *
     * @return 处理链信息；
     */
    private DataChain sellChain() {
        return new DataChain()
                .addFilter(new PrepositionFilter())
                .addFilter(new SellOutFilter(distribute,
                        redisCacheManager))
                .addFilter(new FallbackFilter())
                .addFilter(new EndFilter())
                .setRedis(redisTemplate)
                .setRedisson(redissonClient)
                .setKafka(kafkaTemplate);
    }

    /**
     * 任务的真实处理；
     */
    class NextTask implements Runnable {
        private DataChain chain;

        private TradeDto dto;

        private ProcessData processData;

        NextTask(DataChain chain, TradeDto dto) {
            this.chain = chain;
            this.dto = dto;
            processData = new ProcessData();
        }

        @Override
        public void run() {
            if (chain != null) {
                try {
                    chain.doFilter(dto, processData, chain);
                } catch (Exception ex) {
                    logger.error("", ex);
                }
            }
        }
    }
}
