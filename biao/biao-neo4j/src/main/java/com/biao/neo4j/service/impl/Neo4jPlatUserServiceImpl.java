package com.biao.neo4j.service.impl;

import com.biao.constant.Constants;
import com.biao.entity.PlatNeoUserError;
import com.biao.entity.PlatUser;
import com.biao.exception.PlatException;
import com.biao.mapper.PlatNeoUserErrorDao;
import com.biao.neo4j.entity.Neo4jPlatUser;
import com.biao.neo4j.repository.Neo4jPlatUserRepository;
import com.biao.neo4j.service.Neo4jPlatUserService;
import com.biao.util.SnowFlake;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class Neo4jPlatUserServiceImpl implements Neo4jPlatUserService {

    private Logger logger = LoggerFactory.getLogger(Neo4jPlatUserServiceImpl.class);

    @Autowired
    private Neo4jPlatUserRepository neo4jPlatUserRepository;

    @Autowired
    private PlatNeoUserErrorDao platNeoUserErrorDao;

    private static final String RELATIONSHIP_CONNECT_CHAR = "-";

//    public void save(Neo4jPlatUser user) {
//        // 判断如果存在则不用保存
//        Neo4jPlatUser oldUser = neo4jPlatUserRepository.findByUserId(user.getUserId());
//        if (Objects.nonNull(oldUser) && StringUtils.isNotBlank(oldUser.getUserId())) {
//            return;
//        }
//        // 设置Neo4jUser的深度和TopParentUserId
//        Neo4jPlatUser parentUser = findByUserId(user.getParentUserId());
//        if (Objects.isNull(parentUser)) {
//            logger.error("用户ID：{}的推荐人用户ID: {}在NEO4J库中不存在,无法创建节点和关系", user.getId(), user.getParentUserId());
//            return;
//        }
//        user.setTopParentUserId(parentUser.getTopParentUserId());
//        user.setDepth(parentUser.getDepth() + 1);
//        // 保存
//        neo4jPlatUserRepository.save(user);
//    }

    public void saveAndRelation(Neo4jPlatUser user) {
        // 判断如果存在则不用保存
        Neo4jPlatUser oldUser = neo4jPlatUserRepository.findByUserId(user.getUserId());
        if (Objects.nonNull(oldUser) && StringUtils.isNotBlank(oldUser.getUserId())) {
            return;
        }
        if (StringUtils.isNotBlank(user.getParentUserId())) { // 如果ParentUserId不为空，则保存节点，还需保存上级关系
            Neo4jPlatUser parentUser = findByUserId(user.getParentUserId());
            if (Objects.isNull(parentUser)) {
                logger.error("saveAndRelation用户ID：{}的推荐人用户ID: {}在NEO4J库中不存在,无法创建节点和关系", user.getUserId(), user.getParentUserId());
                saveErrorNeo4jUser(user); // 上级节点不存在，创建Neo4j节点失败，记录错误节点信息
                return;
            }
            user.setTopParentUserId(parentUser.getTopParentUserId());
            user.setDepth(parentUser.getDepth() + 1);
            // 保存节点
            neo4jPlatUserRepository.save(user);
            // 保存节点上级关系
            String relaKey = getRelationshipKey(user.getUserId(), user.getParentUserId());
            user = neo4jPlatUserRepository.createParentRelation(user.getUserId(), user.getParentUserId(), relaKey);
            if (Objects.isNull(user)) {
                logger.error("saveAndRelationNEO4J用户Parent关系失败,用户ID：{}, 上级ID：{}", user.getUserId(), user.getParentUserId());
            }
        } else { // 如果ParentUserId为空，则表明为根节点
            user.setDepth(1L); // 深度从1开始
            user.setTopParentUserId(user.getUserId()); // 根节点的toparentId是自己
            // 保存节点
            neo4jPlatUserRepository.save(user);
        }
    }

    private void saveErrorNeo4jUser(Neo4jPlatUser user) {
        PlatNeoUserError error = new PlatNeoUserError();
        error.setId(SnowFlake.createSnowFlake().nextIdString());
        error.setUserId(user.getUserId());
        error.setReferId(user.getParentUserId());
        error.setStatus("0");
        error.setCreateDate(LocalDateTime.now());
        error.setErrorMsg("上级在neo4j中不存在");
        platNeoUserErrorDao.insert(error);
    }

    public void save(PlatUser platUser) {
        Neo4jPlatUser neo4jPlatUser = new Neo4jPlatUser();
        neo4jPlatUser.setUserId(platUser.getId());
        neo4jPlatUser.setMail(platUser.getMail());
        neo4jPlatUser.setMobile(platUser.getMobile());
        neo4jPlatUser.setRealName(platUser.getRealName());
        neo4jPlatUser.setParentUserId(platUser.getReferId());
//        Neo4jPlatUser parentUser = findByUserId(platUser.getReferId());
//        if (Objects.isNull(parentUser)) {
//            logger.error("用户ID：{}的推荐人用户ID: {}在NEO4J库中不存在,无法创建节点和关系", platUser.getId(), platUser.getReferId());
//            return;
//        }
//        neo4jPlatUser.setTopParentUserId(parentUser.getTopParentUserId());
//        neo4jPlatUser.setDepth(parentUser.getDepth() + 1);
        saveAndRelation(neo4jPlatUser);
//        createParentRelation(platUser.getId(), platUser.getReferId());
    }

    @Override
    public void batchSave(List<Neo4jPlatUser> neo4jPlatUsers) {
        neo4jPlatUserRepository.saveAll(neo4jPlatUsers);
    }

    /**
     * 删除neo4j库里的所有节点和关系
     */
    public void deleteAllRelationAndNodes() {
        neo4jPlatUserRepository.deleteAllRelationAndNodes();
    }

    /**
     * 初始化所有用户的LastBbVolume=0
     */
    public long cleanAllUserLastBbVolume() {
        return neo4jPlatUserRepository.cleanAllUserLastBbVolume();
    }

    /**
     * 查询用户下所有推荐节点（自己除外）
     *
     * @param userId
     * @return
     */
    public List<Neo4jPlatUser> findTreeByUserId(String userId) {
        return neo4jPlatUserRepository.findTreeByUserId(userId);
    }

    /**
     * 查询用户下一层节点
     *
     * @param userId
     * @return
     */
    public List<Neo4jPlatUser> findDownFirstByUserId(String userId) {
        return neo4jPlatUserRepository.findDownFirstByUserId(userId);
    }

    /**
     * 查询用户
     *
     * @param userId
     * @return
     */
    public Neo4jPlatUser findByUserId(String userId) {
        return neo4jPlatUserRepository.findByUserId(userId);
    }

    /**
     * 统计用户下所有推荐树人员的lastBBVolume之和
     *
     * @param userId
     * @return
     */
    public Double sumTreeLastBbVolume(String userId) {
        Neo4jPlatUser userSelf = findByUserId(userId);
        Double self = 0D; // 自己持币量
        if (Objects.nonNull(userSelf) && Objects.nonNull(userSelf.getLastBbVolume())) {
            self = userSelf.getLastBbVolume();
        }
        return self + sumTreeLastBbVolumeExSelf(userId);  // 自己持币量 + 统计用户下所有推荐树人员的Volume之和(自己除外)
    }

    /**
     * 统计用户下所有推荐树人员的lastBBVolume之和(自己除外)
     *
     * @param userId
     * @return
     */
    public Double sumTreeLastBbVolumeExSelf(String userId) {
        Double treeTotalExSelf = neo4jPlatUserRepository.sumTreeLastBbVolumeExSelf(userId);
        return Objects.isNull(treeTotalExSelf) ? 0D : treeTotalExSelf;
    }

    /**
     * 统计用户下所有推荐树人员的joinMiningVol之和
     *
     * @param userId
     * @return
     */
    public Double sumJoinMiningVol(String userId) {
        Neo4jPlatUser userSelf = findByUserId(userId);
        Double self = 0D; // 自己持币量
        if (Objects.nonNull(userSelf) && Objects.nonNull(userSelf.getJoinMiningVol())) {
            self = userSelf.getJoinMiningVol();
        }
        return self + sumJoinMiningVolExSelf(userId);  // 自己持币量 + 统计用户下所有推荐树人员的Volume之和(自己除外)
    }

    /**
     * 统计用户下所有推荐树人员的joinMiningVol之和(自己除外)
     *
     * @param userId
     * @return
     */
    public Double sumJoinMiningVolExSelf(String userId) {
        Double treeTotalExSelf = neo4jPlatUserRepository.sumTreeJoinMiningVolExSelf(userId);
        return Objects.isNull(treeTotalExSelf) ? 0D : treeTotalExSelf;
    }

    /**
     * 更新用户的LastBbVolume
     *
     * @param userId
     * @param volume
     */
    public void updateUserLastBbVolume(String userId, Double volume) {
        long count = neo4jPlatUserRepository.updateUserLastBbVolume(userId, volume);
        if (count != 1) {
            logger.error("NEO4J用户VOLUME更新失败,用户ID：{}", userId);
            throw new PlatException(Constants.UPDATE_ERROR, "NEO4J用户VOLUME更新失败");
        }
    }

    /**
     * 更新创建用户的上级关系
     *
     * @param userId
     * @param parentUserId
     */
//    public void createParentRelation(String userId, String parentUserId) {
//
//        Neo4jPlatUser user = neo4jPlatUserRepository.findByUserId(userId);
//        if (Objects.isNull(user)) {
//            logger.error("NEO4J用户不存在,用户ID：{}", userId);
//            throw new PlatException(Constants.UPDATE_ERROR, "NEO4J用户不存在");
//        }
//        if (Objects.nonNull(user.getParentUser())) {
//            logger.error("NEO4J用户上级关系已存在,用户ID：{}, 上级ID：{}", userId, user.getParentUser().getUserId());
//            throw new PlatException(Constants.UPDATE_ERROR, "NEO4J用户上级关系已存在");
//        }
//        String relaKey = getRelationshipKey(userId, parentUserId);
//        user = neo4jPlatUserRepository.createParentRelation(userId, parentUserId, relaKey);
//        if (Objects.isNull(user)) {
//            logger.error("NEO4J用户Parent关系失败,用户ID：{}, 上级ID：{}", userId, parentUserId);
//            //throw new PlatException(Constants.UPDATE_ERROR, "NEO4J用户Parent关系失败");
//        }
//    }

    /**
     * 批量更新节点的LastBbVolume
     *
     * @param users
     */
    public void updateBatchUserLastBbVolume(List<Neo4jPlatUser> users) {
        neo4jPlatUserRepository.updateBatchUserLastBbVolume(users);
    }

    /**
     * 批量设置节点的父级节点
     *
     * @param users
     */
    public void createBatchParentRelation(List<Neo4jPlatUser> users) {
        neo4jPlatUserRepository.createBatchParentRelation(users);
    }

    /**
     * 查询两个节点之前的关系条数
     *
     * @param fromUserId
     * @param toUserId
     * @return
     */
    public long countRelationship(String fromUserId, String toUserId) {
        long count = neo4jPlatUserRepository.countRelationship(fromUserId, toUserId);
        return count;
    }

    private String getRelationshipKey(String from, String to) {
        StringBuilder sb = new StringBuilder();
        sb.append(from).append(RELATIONSHIP_CONNECT_CHAR).append(to);
        return sb.toString();
    }

}
