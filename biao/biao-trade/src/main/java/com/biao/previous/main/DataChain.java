package com.biao.previous.main;

import com.biao.pojo.TradeDto;
import com.biao.previous.domain.ProcessData;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.List;

/**
 *
 * @date 2018/4/7
 * 处理一个数据链
 */
public class DataChain {

    /**
     * node 头信息；
     */
    private Node<Filter> head;

    /**
     * node
     */
    private Node<Filter> last;

    /**
     * 初始化
     */
    public DataChain() {
        last = head = new Node<>(null);
    }

    /**
     * 增加一个过滤器批装
     *
     * @param filters filters;
     */
    public void addFilters(List<Filter> filters) {
        filters.forEach(this::addFilter);
    }

    /**
     * 构建一个Filter处理;
     *
     * @param filter filter;
     */
    public DataChain addFilter(Filter filter) {
        Node<Filter> node = new Node<>(filter);
        last = last.next = node;
        return this;
    }

    /**
     * 设置一下Redis;
     *
     * @param template template;
     * @return this;
     */
    public DataChain setRedis(RedisTemplate template) {
        Node<Filter> h = head;
        if (h != null) {
            do {
                Filter f = h.item;
                if (f != null) {
                    f.setRedis(template);
                }
                h = h.next;
            } while (h != null);
        }
        return this;
    }

    /**
     * 设置一下Kafka;
     *
     * @param kafka
     * @return this;
     */
    public DataChain setKafka(KafkaTemplate kafka) {
        Node<Filter> h = head;
        if (h != null) {
            do {
                Filter f = h.item;
                if (f != null) {
                    f.setKafka(kafka);
                }
                h = h.next;
            } while (h != null);
        }
        return this;
    }

    /**
     * 操作；
     *
     * @param client 数据；
     * @return 返回；
     */
    public DataChain setRedisson(RedissonClient client) {
        Node<Filter> h = head;
        if (h != null) {
            do {
                Filter f = h.item;
                if (f != null) {
                    f.setRedisson(client);
                }
                h = h.next;
            } while (h != null);
        }
        return this;
    }


    /**
     * 开始一个数据流程的处理；
     *
     * @param data        数据对象；
     * @param processData 过程数据;
     * @param chain       数据处理链
     */
    public void doFilter(TradeDto data, ProcessData processData, DataChain chain) {
        if (head != null) {
            Node<Filter> h = head;
            Node<Filter> first = h.next;
            if (first != null) {
                head = first;
                Filter filter = first.item;
                first.item = null;
                filter.doFilter(data, processData, chain);
            }
        }
    }

    /**
     * 是不是为最后一个Filter;
     *
     * @return true or false;
     */
    boolean isLast() {
        return head.next == null;
    }

    /**
     * 处理一个数据Node批装
     *
     * @param <E> 可以的数据类型;
     */

    public class Node<E> {
        /**
         * 当前的item;
         */
        private E item;
        /**
         * 下一个node;
         */
        private Node<E> next;

        Node(E item) {
            this.item = item;
        }
    }
}
