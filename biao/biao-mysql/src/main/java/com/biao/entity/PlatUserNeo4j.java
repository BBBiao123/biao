package com.biao.entity;


import lombok.Data;

import java.io.Serializable;

@Data
public class PlatUserNeo4j implements Serializable {
    private static final long serialVersionUID = 1L;

    private String userId;

    private String realName;

    private String mobile;

    private String mail;

    private Double lastBbVolume = 0D; // 持币量 = bb可用量 + 超级钱包量

    private Double joinMiningVol = 0D; // 参与团队挖矿计算量 = bb可用量 + 超级钱包量 * 配置倍数

    private Double feeVolume = 0D;

    private Long depth;

    private String topParentUserId; // 关系的根节点
    private String parentUserId; // 关系上一节点ID
    private String fromToKey; // 关系方向KEY
}
