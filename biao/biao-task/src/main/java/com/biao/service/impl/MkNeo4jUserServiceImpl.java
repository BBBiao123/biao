package com.biao.service.impl;

import com.biao.entity.PlatUserNeo4j;
import com.biao.mapper.MkNeo4jUserDao;
import com.biao.neo4j.entity.Neo4jPlatUser;
import com.biao.neo4j.service.Neo4jPlatUserService;
import com.biao.service.MkNeo4jUserService;
import com.biao.util.DateUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
public class MkNeo4jUserServiceImpl implements MkNeo4jUserService {

    private Logger logger = LoggerFactory.getLogger(MkNeo4jUserServiceImpl.class);

    @Autowired
    private Neo4jPlatUserService neo4jPlatUserService;

    @Autowired
    private MkNeo4jUserDao mkNeo4jUserDao;

    private static int pageCount = 500; // 批量提交每页数量

    //    @Transactional
    public void initNeo4jUser() {
        logger.info("initNeo4jUser初始化用户开始。。。。");
        List<PlatUserNeo4j> platUsers = mkNeo4jUserDao.findAllPlatUser();

        List<Neo4jPlatUser> users = new ArrayList<>();
        platUsers.forEach(u -> {
            Neo4jPlatUser neo4jUser = new Neo4jPlatUser();
            BeanUtils.copyProperties(u, neo4jUser);
            users.add(neo4jUser);
        });

        logger.info("initNeo4jUser用户总量:" + users.size());
        Map<String, Neo4jPlatUser> depthMap = new HashMap<>(); // 用户设置depth, topParentId后入此map
        Map<String, Neo4jPlatUser> userMap = new HashMap<>(); // 用户未设置depth, topParentId前入此map
        users.forEach(e -> {
            if (StringUtils.isBlank(e.getParentUserId())) { // 根节点
                e.setDepth(1L); // 深度从1开始
                e.setTopParentUserId(e.getUserId()); // 根节点的toparentId是自己
                depthMap.put(e.getUserId(), e);
            } else {
                userMap.put(e.getUserId(), e);
            }
        });
        // 设置用户的depth,topParentId
        logger.info("initNeo4jUser设置用户的深度和top父ID");
        scalcDepth(userMap, depthMap);
        // 清空neo4j库中的所有节点和关系
        logger.info("initNeo4jUser清空neo4j库中的所有节点和关系");
        neo4jPlatUserService.deleteAllRelationAndNodes();
        // 存储用户数据入neo4j库
        logger.info("initNeo4jUser存储用户数据入neo4j库");
        saveNeo4jUserBatch(users);
        // 存储用户关系入neo4j库
        logger.info("initNeo4jUser存储用户关系入neo4j库");
        saveNeo4jRelationBatch(users);
        logger.info("initNeo4jUser初始化用户结束。。。。");
    }

    private void scalcDepth(Map<String, Neo4jPlatUser> userMap, Map<String, Neo4jPlatUser> depthMap) {
        if (userMap.isEmpty()) {
            return;
        }
        Map.Entry<String, Neo4jPlatUser> entry = null;
        Neo4jPlatUser parentUser = null;
        Neo4jPlatUser user = null;
        for (Iterator<Map.Entry<String, Neo4jPlatUser>> it = userMap.entrySet().iterator(); it.hasNext(); ) {
            user = it.next().getValue();
            parentUser = depthMap.get(user.getParentUserId());
            if (Objects.nonNull(parentUser)) {
                user.setTopParentUserId(parentUser.getTopParentUserId());
                user.setDepth(parentUser.getDepth() + 1);
                it.remove();
                depthMap.put(user.getUserId(), user);
            }
        }
        scalcDepth(userMap, depthMap);
    }

    public void saveNeo4jUserBatch(List<Neo4jPlatUser> users) {
        int page = users.size() % pageCount != 0 ? (users.size() / pageCount + 1) : (users.size() / pageCount);
        List<Neo4jPlatUser> pageTmp = null;
        int count = 0; // 统计入库数量，打印日志
        for (int index = 1; index <= page; index++) {
            if (index == page) {
                pageTmp = users.subList((index - 1) * pageCount, users.size());
            } else {
                pageTmp = users.subList((index - 1) * pageCount, index * pageCount);
            }
            neo4jPlatUserService.batchSave(pageTmp);
            count = count + pageTmp.size();
            logger.info("saveNeo4jUserBatch批量保存节点数:" + count);
        }
    }

    public void saveNeo4jRelationBatch(List<Neo4jPlatUser> users) {
        int page = users.size() % pageCount != 0 ? (users.size() / pageCount + 1) : (users.size() / pageCount);
        List<Neo4jPlatUser> pageTmp = null;
        int count = 0; // 统计入库数量，打印日志
        for (int index = 1; index <= page; index++) {
            if (index == page) {
                pageTmp = users.subList((index - 1) * pageCount, users.size());
            } else {
                pageTmp = users.subList((index - 1) * pageCount, index * pageCount);
            }
            neo4jPlatUserService.createBatchParentRelation(pageTmp);
            count = count + pageTmp.size();
            logger.info("saveNeo4jRelationBatch批量保存关系数:" + count);
        }
    }

    /**
     * 按时间修复Neo4j用户，起始时间为2018-11-22 00:00:00
     */
    public void repairMissNeo4jOnce() {
        try {
            LocalDateTime beginTime = DateUtils.parseLocalDateTime("2018-11-22 00:00:00");
            repairMissNeo4jUser(beginTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 按时间修复Neo4j用户，起始时间为昨天0点0分0秒
     */
    public void repairMissNeo4jEveryDay() {
        LocalDateTime beginDate = LocalDateTime.of(LocalDateTime.now().minusDays(1).toLocalDate(), LocalTime.MIN);// 昨天0点0分0秒
        repairMissNeo4jUser(beginDate);
    }
    /**
     * 按时间修复
     * @param beginTime  修复开始时间
     */
    public void repairMissNeo4jUser(LocalDateTime beginTime) {
        logger.info("修复用户开始。。。");
        List<PlatUserNeo4j> userNeo4js = mkNeo4jUserDao.findPlatUserByDate(beginTime);
        logger.info("修复用户数量。。。" + (Objects.nonNull(userNeo4js) ? userNeo4js.size() : "0"));
        doRepailMissNeo4jUser(userNeo4js);
    }

    public void doRepailMissNeo4jUser(List<PlatUserNeo4j> userNeo4js) {
        if (CollectionUtils.isEmpty(userNeo4js)) {
            return;
        }

        List<Neo4jPlatUser> neo4jPlatUsers = new ArrayList<>(); // 排序结果，用来入Neo4j库
        Map<String, Neo4jPlatUser> sortMap = new HashMap<>(); //
        Map<String, Neo4jPlatUser> userMap = new HashMap<>(); //

        userNeo4js.forEach(user -> {
            Neo4jPlatUser neo4jUser = new Neo4jPlatUser();
            BeanUtils.copyProperties(user, neo4jUser);
            userMap.put(user.getUserId(), neo4jUser);
        });

        // 找出第一层用户放入sortMap、neo4jPlatUsers
        Neo4jPlatUser user = null;
        for (Iterator<Map.Entry<String, Neo4jPlatUser>> it = userMap.entrySet().iterator(); it.hasNext(); ) {
            user = it.next().getValue();
            if (!userMap.containsKey(user.getParentUserId())) {
                sortMap.put(user.getUserId(), user);
                neo4jPlatUsers.add(user);
                it.remove();
            }
        }
        sortNeo4jUsers(neo4jPlatUsers, userMap, sortMap); // 把剩下N层用户排序入neo4jPlatUsers

        logger.info("修复用户入Neo4j。。。");
        // 入Neo4j库，保存节点用户和创建上级关系
        neo4jPlatUsers.forEach(neo4jPlatUser -> {
            neo4jPlatUserService.saveAndRelation(neo4jPlatUser);
        });
        logger.info("修复用户结束。。。" + LocalDateTime.now());
    }

    private void sortNeo4jUsers(List<Neo4jPlatUser> neo4jPlatUsers, Map<String, Neo4jPlatUser> userMap, Map<String, Neo4jPlatUser> sortMap) {
        if (userMap.isEmpty()) {
            return;
        }
        Neo4jPlatUser user = null;
        for (Iterator<Map.Entry<String, Neo4jPlatUser>> it = userMap.entrySet().iterator(); it.hasNext(); ) {
            user = it.next().getValue();
            if (sortMap.containsKey(user.getParentUserId())) {
                sortMap.put(user.getUserId(), user);
                neo4jPlatUsers.add(user);
                it.remove();
            }
        }
        sortNeo4jUsers(neo4jPlatUsers, userMap, sortMap);
    }


}
