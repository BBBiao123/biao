package com.biao.neo4j.entity;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;

import java.io.Serializable;

/**
 * neo4j 用户关系实体
 */

@NodeEntity(label = "Parent")
public class Neo4jUserRelationship implements Serializable {

    private static final long serialVersionUID = 1L;
    @GraphId
    private Long id;

    private String fromToKey;

    private Neo4jPlatUser fromUser;

    private Neo4jPlatUser toUser;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFromToKey() {
        return fromToKey;
    }

    public void setFromToKey(String fromToKey) {
        this.fromToKey = fromToKey;
    }

    public Neo4jPlatUser getFromUser() {
        return fromUser;
    }

    public void setFromUser(Neo4jPlatUser fromUser) {
        this.fromUser = fromUser;
    }

    public Neo4jPlatUser getToUser() {
        return toUser;
    }

    public void setToUser(Neo4jPlatUser toUser) {
        this.toUser = toUser;
    }
}
