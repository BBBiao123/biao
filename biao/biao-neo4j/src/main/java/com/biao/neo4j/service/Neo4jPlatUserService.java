package com.biao.neo4j.service;

import com.biao.entity.PlatUser;
import com.biao.neo4j.entity.Neo4jPlatUser;

import java.util.List;

public interface Neo4jPlatUserService {

//    void save(Neo4jPlatUser user);

    void save(PlatUser platUser);

    void saveAndRelation(Neo4jPlatUser user);

    void batchSave(List<Neo4jPlatUser> neo4jPlatUsers);

    /**
     * 删除neo4j库里的所有节点和关系
     */
    void deleteAllRelationAndNodes();

    /**
     * 初始化所有用户的LastBbVolume=0
     */
    long cleanAllUserLastBbVolume();

    /**
     * 查询用户
     *
     * @param userId
     * @return
     */
    Neo4jPlatUser findByUserId(String userId);

    /**
     * 查询用户下一层节点
     *
     * @param userId
     * @return
     */
    List<Neo4jPlatUser> findDownFirstByUserId(String userId);

    /**
     * 查询用户下所有推荐节点（自己除外）
     *
     * @param userId
     * @return
     */
    List<Neo4jPlatUser> findTreeByUserId(String userId);

    /**
     * 统计用户下所有推荐树人员的Volume之和
     *
     * @param userId
     * @return
     */
    Double sumTreeLastBbVolume(String userId);

    /**
     * 统计用户下所有推荐树人员的Volume之和(自己除外)
     *
     * @param userId
     * @return
     */
    Double sumTreeLastBbVolumeExSelf(String userId);

    /**
     * 统计用户下所有推荐树人员的joinMiningVol之和
     * @param userId
     * @return
     */
    Double sumJoinMiningVol(String userId);

    /**
     * 统计用户下所有推荐树人员的joinMiningVol之和(自己除外)
     * @param userId
     * @return
     */
    Double sumJoinMiningVolExSelf(String userId);

    /**
     * 更新用户的LastBbVolume
     *
     * @param userId
     * @param volume
     */
    void updateUserLastBbVolume(String userId, Double volume);

    /**
     * 更新创建用户的上级关系
     *
     * @param userId
     * @param parentUserId
     */
//    void createParentRelation(String userId, String parentUserId);

    /**
     * 批量更新节点的LastBbVolume
     *
     * @param users
     */
    void updateBatchUserLastBbVolume(List<Neo4jPlatUser> users);

    /**
     * 批量设置节点的父级节点
     *
     * @param users
     */
    void createBatchParentRelation(List<Neo4jPlatUser> users);

    /**
     * 查询两个节点之前的关系条数
     *
     * @param fromUserId
     * @param toUserId
     * @return
     */
    long countRelationship(String fromUserId, String toUserId);

}
