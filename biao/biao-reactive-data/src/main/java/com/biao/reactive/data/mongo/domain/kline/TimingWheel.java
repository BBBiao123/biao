package com.biao.reactive.data.mongo.domain.kline;


import com.biao.reactive.data.mongo.domain.RedisMatchStream;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;


/**
 * TimingWheel.
 *
 *  ""1
 */
@SuppressWarnings("all")
public class TimingWheel {

    /**
     * 开始时间;
     */
    private LocalDateTime start;
    /**
     * 结束时间;
     */
    private LocalDateTime end;
    /**
     * 桶大小;
     */
    private HashedWheelBucket[] wheelBuckets;
    /**
     * 标记;
     */
    private final int mask;
    /**
     * 跨度;
     */
    private final int span;

    private final ChronoUnit chronoUnit;

    /**
     * 时间序列初始化;
     *
     * @param start start;
     * @param end   end;
     * @param span  时间的跨度
     */
    public TimingWheel(LocalDateTime start, LocalDateTime end, int span, ChronoUnit chronoUnit) {
        this.start = start;
        this.end = end;
        this.span = span;
        this.chronoUnit = chronoUnit;
        /*
         * 创建时间分片区；
         */
        this.wheelBuckets = createWheel(span);
        mask = this.wheelBuckets.length - 1;

    }

    private HashedWheelBucket[] createWheel(int span) {
        if (span <= 0) {
            throw new IllegalArgumentException(
                    "ticksPerWheel must be greater than 0: " + span);
        }
        int ticksPerWheel = (int) start.until(end, chronoUnit) / span;
        ticksPerWheel = normalizeTicksPerWheel(ticksPerWheel);
        HashedWheelBucket[] wheel = new HashedWheelBucket[ticksPerWheel];
        for (int i = 0; i < wheel.length; i++) {
            int s = i * span;
            wheel[i] = new HashedWheelBucket(start.plus(s, chronoUnit));
        }
        return wheel;
    }


    private int normalizeTicksPerWheel(int ticksPerWheel) {
        int normalizedTicksPerWheel = 1;
        while (normalizedTicksPerWheel <= ticksPerWheel) {
            normalizedTicksPerWheel <<= 1;
        }
        return normalizedTicksPerWheel;
    }

    /**
     * 增加一个数据计算桶中;
     */
    private TimingWheel addWheel(RedisMatchStream value) {
        int calculated = (int) start.until(value.getMinuteTime(), chronoUnit) / span;
        int index = (calculated & mask);
        wheelBuckets[index].addWheel(value);
        return this;
    }

    /**
     * 增加集合计算;
     *
     * @param values values;
     * @return TimingWheel
     */
    public <V extends RedisMatchStream> TimingWheel addWheels(Collection<V> values) {
        values.forEach(this::addWheel);
        return this;
    }

    /**
     * 计算结果;
     */
    public <V> List<BucketValue<V>> compute(Function<List<RedisMatchStream>, V> fn) {
        List<BucketValue<V>> result = new ArrayList<>();
        for (HashedWheelBucket bucket : wheelBuckets) {
            if (bucket.isHealth()) {
                V v = bucket.compute(fn).get();
                BucketValue<V> bucketValue = new BucketValue<>(bucket.time, v);
                result.add(bucketValue);
            }
        }
        return result;
    }

    /**
     * 提供快速清除算法;
     */
    public void clear() {
        wheelBuckets = null;
    }

    /**
     * 数据；
     *
     * @param <V>
     */
    public class BucketValue<V> {
        /**
         * Values值对象;
         */
        private V value;
        /**
         * 时间
         */
        private LocalDateTime time;

        /**
         * 初始化数据；
         *
         * @param time  时间；
         * @param value value;
         */
        BucketValue(LocalDateTime time, V value) {
            this.value = value;
            this.time = time;
        }

        public LocalDateTime getTime() {
            return time;
        }

        public V getValue() {
            return value;
        }
    }

    /**
     * 每个分片槽封装对象，
     * 这里使用的是双向队列；
     */
    private static final class HashedWheelBucket {
        /**
         * 所属时间;
         */
        private LocalDateTime time;
        /**
         * 初始化一个值对象;
         */
        List<RedisMatchStream> values;

        /**
         * 看一下这个桶是不是健康的;
         *
         * @return true or false;
         */
        private boolean isHealth() {
            return values != null && !values.isEmpty();
        }

        /**
         * 时间版的时间计算出来;
         *
         * @param time 时间；
         */
        HashedWheelBucket(LocalDateTime time) {
            this.time = time;
        }

        void addWheel(RedisMatchStream value) {
            if (values == null) {
                values = new ArrayList<>();
            }
            values.add(value);
        }

        /**
         * 计算一下结果了;
         *
         * @param function 执行计算;
         * @return 结果;
         */
        <V> Supplier<V> compute(Function<List<RedisMatchStream>, V> function) {
            return () -> function.apply(values);
        }
    }

}
