package com.biao.previous;

import com.biao.previous.domain.CancelResult;
import com.biao.previous.domain.TradeResult;
import com.biao.previous.message.CancelOrderConsumer;
import com.biao.previous.message.CancelUserCoinVolumeConsumer;
import com.biao.previous.message.OrderConsumer;
import com.biao.previous.message.QueueTradeResultExecutor;
import com.biao.previous.queue.QueueDistribute;
import com.biao.previous.queue.QueueProviderManage;
import com.biao.previous.queue.QueueType;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * OrderConsumerTest.
 * <p>
 * <p>
 * 18-12-29下午5:08
 *
 *  "" sixh
 */
public class OrderConsumerTest {
    @Test
    public void test() throws InterruptedException {
        /**
         * 交易消息的流水处理；
         */
        QueueDistribute distribute = QueueDistribute.get();
        //启动一个订单结果的队列处理器；
        QueueProviderManage manage2 = new QueueProviderManage(
                new QueueTradeResultExecutor.QueueTradeResultExecutorFactory()
                        .addSubscribers(new OrderConsumer(null, null))
                        .addSubscribers(new CancelUserCoinVolumeConsumer(null))
                        .addSubscribers(new CancelOrderConsumer(null, null)));
        manage2.start();
        distribute.provider(QueueType.TR_RE_MESSAGE, manage2::getProvider);
        TradeResult result = new TradeResult();
        List list = new ArrayList();
//        list.add(result);
//        distribute.send(QueueType.TR_RE_MESSAGE,list);
        CancelResult result2 = new CancelResult();
        list = new ArrayList();
        list.add(result2);
        distribute.send(QueueType.TR_RE_MESSAGE,list);
        Thread.sleep(Integer.MAX_VALUE);
    }
}
