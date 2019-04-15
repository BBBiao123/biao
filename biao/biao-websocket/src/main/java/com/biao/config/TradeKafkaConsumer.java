package com.biao.config;

/**
 *  ""
 */
public class TradeKafkaConsumer {

    private String groupId;

    private String topic;

    private String[] partitions;

    public TradeKafkaConsumer(String groupId, String topic, String[] partitions) {
        super();
        this.groupId = groupId;
        this.topic = topic;
        this.partitions = partitions;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String[] getPartitions() {
        return partitions;
    }

    public void setPartitions(String[] partitions) {
        this.partitions = partitions;
    }

}
