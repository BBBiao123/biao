package com.biao.service;

import com.biao.BaseTest;
import com.biao.neo4j.repository.Neo4jPlatUserRepository;
import com.biao.neo4j.service.Neo4jPlatUserService;
import com.biao.util.DateUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;


public class MkNeo4jUserTest extends BaseTest {

    @Autowired
    private MkNeo4jUserService mkNeo4jUserService;

    @Autowired
    private Neo4jPlatUserService neo4jPlatUserService;

    @Autowired
    private Neo4jPlatUserRepository neo4jPlatUserRepository;

    @Autowired
    private Mk2MiningTeamService mk2MiningTeamService;

    @Autowired
    private OfflineOrderDetailCancelTaskService offlineOrderDetailCancelTaskService;

    @Test
    public void testInitNeo4jUser() {
        mkNeo4jUserService.initNeo4jUser();
    }

    @Test
    public void testNeo4jTeamMining() {
        mk2MiningTeamService.doTeamCoinMining();
    }

    @Test
    public void testCancel() {
        offlineOrderDetailCancelTaskService.doCancelOrderDetail();
    }

    @Test
    public void testMiss() {
        try {
            LocalDateTime beginTime = DateUtils.parseLocalDateTime("2018-07-18 00:00:00");
            mkNeo4jUserService.repairMissNeo4jUser(beginTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
//        MyConfiguration myConfiguration = new MyConfiguration();
//        SessionFactory factory = myConfiguration.getSessionFactory();
//        org.neo4j.ogm.session.Session session = factory.openSession();
//        org.neo4j.ogm.model.Result result = session.query("MATCH (a:Person) WHERE a.name = 'aa' RETURN a.name AS name, a.title AS title", new HashMap<>());
////        Neo4jPlatUser user = new Neo4jPlatUser();
////        user.setUserId("111111");
////        user.setMail("11@sina.com");
////        user.setMobile("131111111");
////        session.save(user);
//        result.forEach(e -> {
//            System.out.println("结果：" + e.get( "title" ) + " " + e.get( "name" ) );
//        });
//
//        factory.close();

        System.out.println("aa-" + String.valueOf(null) + "-bb");

    }

    public static void doAction() {
//        Driver driver = GraphDatabase.driver("bolt://localhost:7687", AuthTokens.basic("neo4j", "123456"));
//        Session session = driver.session();
//        session.run("CREATE (a:Person {name: 'aa5555555', title: 'bb2'})");
//
//        StatementResult result = session.run("MATCH (a:Person) WHERE a.name = 'aa' RETURN a.name AS name, a.title AS title");
//        while (result.hasNext()) {
//            Record record = result.next();
//            System.out.println(record.get("title").asString() + " " + record.get("name").asString());
//        }
//        session.close();
//        driver.close();
    }
}
