package com.biao.neo4j.entity;

import lombok.Data;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.io.Serializable;

/**
 * neo4j 用户实体
 */

@Data
@NodeEntity(label = "Neo4jPlatUser")
public class Neo4jPlatUser implements Serializable {

    private static final long serialVersionUID = 1L;
    @GraphId
    private Long id;

    private String userId;

    private String realName;

    private String mobile;

    private String mail;

    private Double lastBbVolume = 0D; // 持币量 = bb可用量 + 超级钱包量

    private Double joinMiningVol = 0D; // 参与团队挖矿计算量 = bb可用量 + 超级钱包量 * 配置倍数

    private Double feeVolume = 0D;

    private Long depth;

    @Relationship(type = "Parent", direction = Relationship.OUTGOING)
    private Neo4jPlatUser parentUser;// 关系，上一节点对象

    private String topParentUserId; // 关系的根节点
    private String parentUserId; // 关系上一节点ID
    private String fromToKey; // 关系方向KEY

//    @Relationship(type = "TOP_REFER", direction = Relationship.OUTGOING)
//    private Neo4jPlatUser topReferUser;
}
