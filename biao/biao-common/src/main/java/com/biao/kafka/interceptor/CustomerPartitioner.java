package com.biao.kafka.interceptor;

import com.biao.util.StringHelp;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.clients.producer.internals.DefaultPartitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;


public class CustomerPartitioner implements Partitioner {

    private DefaultPartitioner defaultPartitioner = new DefaultPartitioner();

    private Map<String, Integer> expairPartition = new HashMap<>();

    private final static String EXPAIR_PARTITION_ATTR = "kafka.message.expair.part.attr";

    private final static String EXPAIR_PART_ATTR = "expair.part.attr";

    @Override
    public void configure(Map<String, ?> configs) {
        //初始化交易对和分区的关系
        String expairPartConfigs = (String) configs.get(EXPAIR_PART_ATTR);
        if (expairPartConfigs == null) {
            expairPartConfigs = System.getProperty(EXPAIR_PARTITION_ATTR);
        }
        if (StringUtils.isNotBlank(expairPartConfigs)) {
            String[] expairPartArrays = expairPartConfigs.trim().split(",");
            if (expairPartArrays.length == 0 && expairPartConfigs.indexOf(":") != -1 && expairPartConfigs.split(":").length == 2) {
                String partStr = expairPartConfigs.split(":")[1].trim();
                if (StringHelp.regexMatcher("\\d+", partStr)) {
                    expairPartition.put(expairPartConfigs.split(":")[0].trim(), Integer.parseInt(partStr));
                }
            }
            if (expairPartArrays.length > 0) {
                Stream.of(expairPartArrays).distinct().forEach(expairPartArray -> {
                    expairPartArray = expairPartArray.trim();
                    if (expairPartArray.indexOf(":") != -1 && expairPartArray.split(":").length == 2) {
                        String partStr = expairPartArray.split(":")[1].trim();
                        if (StringHelp.regexMatcher("\\d+", partStr)) {
                            expairPartition.put(expairPartArray.split(":")[0].trim(), Integer.parseInt(partStr));
                        }
                    }
                });
            }
        }
    }

    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        if (key != null && (key instanceof String) && expairPartition.get((String) key) != null) {
            Integer partition = expairPartition.get((String) key);
            List<PartitionInfo> partitions = cluster.partitionsForTopic(topic);
            //分区总数
            int numPartitions = partitions.size();
            //校正分区数
            if (partition > numPartitions) {
                return partition % numPartitions;
            }
            return partition;
        } else {
            return defaultPartitioner.partition(topic, key, keyBytes, value, valueBytes, cluster);
        }
    }

    @Override
    public void close() {

    }

}
