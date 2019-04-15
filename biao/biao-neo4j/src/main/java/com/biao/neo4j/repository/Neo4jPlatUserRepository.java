package com.biao.neo4j.repository;

import com.biao.neo4j.entity.Neo4jPlatUser;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Neo4jPlatUserRepository extends Neo4jRepository<Neo4jPlatUser, Long> {

    @Query("MATCH (a:Neo4jPlatUser)<-[:Parent*]-(b:Neo4jPlatUser) WHERE a.userId = {0} RETURN b ")
    List<Neo4jPlatUser> findTreeByUserId(String userId);

    @Query("MATCH (a:Neo4jPlatUser)<-[:Parent]-(b:Neo4jPlatUser) WHERE a.userId = {0} RETURN b ")
    List<Neo4jPlatUser> findDownFirstByUserId(String userId);

    @Query("MATCH (a:Neo4jPlatUser) WHERE a.userId = {0} RETURN a ")
    Neo4jPlatUser findByUserId(String userId);

//    @Query("MATCH (n:Neo4jPlatUser)-[:Parent*]->(m:Neo4jPlatUser) where m.userId = {0} RETURN sum(n.lastBbVolume) + m.lastBbVolume ")
//    Double sumTreeLastBbVolume(String userId);

    @Query("MATCH (n:Neo4jPlatUser)-[:Parent*]->(m:Neo4jPlatUser) where m.userId = {0} RETURN sum(n.lastBbVolume) ")
    Double sumTreeLastBbVolumeExSelf(String userId);

//    @Query("MATCH (n:Neo4jPlatUser)-[:Parent*]->(m:Neo4jPlatUser) where m.userId = {0} RETURN sum(n.joinMiningVol) + m.joinMiningVol ")
//    Double sumTreeJoinMiningVol(String userId);

    @Query("MATCH (n:Neo4jPlatUser)-[:Parent*]->(m:Neo4jPlatUser) where m.userId = {0} RETURN sum(n.joinMiningVol) ")
    Double sumTreeJoinMiningVolExSelf(String userId);

    @Query("MATCH (n) OPTIONAL MATCH (n)-[r]-() DELETE n,r ")
    void deleteAllRelationAndNodes();

    @Query("MATCH (a:Neo4jPlatUser) SET a.lastBbVolume = 0, a.joinMiningVol = 0  RETURN count(*)")
    long cleanAllUserLastBbVolume();

    @Query("MATCH (a:Neo4jPlatUser) WHERE a.userId = {0} SET a.lastBbVolume = {1} RETURN count(a) ")
    long updateUserLastBbVolume(String userId, Double volume);

    @Query("UNWIND {0} as row MATCH (n:Neo4jPlatUser) WHERE n.userId = row.userId SET n.lastBbVolume = row.lastBbVolume, n.joinMiningVol = row.joinMiningVol ")
    void updateBatchUserLastBbVolume(List<Neo4jPlatUser> users);

    @Query("MATCH (a:Neo4jPlatUser), (b:Neo4jPlatUser) WHERE a.userId = {0} AND b.userId = {1} CREATE (a)-[r:Parent{fromToKey:{2}}]->(b) RETURN a ")
    Neo4jPlatUser createParentRelation(String userId, String parentUserId, String fromToKey);

    @Query("UNWIND {0} as row MATCH (n:Neo4jPlatUser{userId:row.userId}), (m:Neo4jPlatUser{userId:row.parentUserId}) CREATE (n)-[r:Parent{fromToKey:row.fromToKey}]->(m) ")
    void createBatchParentRelation(List<Neo4jPlatUser> users);

    @Query("MATCH (a:Neo4jPlatUser)-[r:Parent]->(b:Neo4jPlatUser) WHERE a.userId = {0} AND b.userId = {1} RETURN count(r) ")
    long countRelationship(String fromUserId, String toUserId);
}
