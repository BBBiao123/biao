package com.biao.config;

import org.neo4j.ogm.session.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
//import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableNeo4jRepositories(basePackages = "com.biao.neo4j.repository")
//@EnableTransactionManagement
public class Neo4jConfiguration {

    private Logger logger = LoggerFactory.getLogger(Neo4jConfiguration.class);

    @Value("${neo4j.uri}")
    private String uri;

    @Value("${neo4j.username}")
    private String username;

    @Value("${neo4j.password}")
    private String password;

    @Value("${neo4j.connection.pool.size:10}")
    private Integer connectionPoolSize;

    @Bean("sessionFactory")
    public SessionFactory getSessionFactory() {
        logger.info("========================getSessionFactorygetSessionFactorygetSessionFactorygetSessionFactorygetSessionFactory");
        return new SessionFactory(configuration(), "com.biao.neo4j.entity");
    }

//    @Bean
//    public Neo4jTransactionManager transactionManager(){
//        logger.info("==========================transactionManagertransactionManagertransactionManagertransactionManagertransactionManager");
//        return new Neo4jTransactionManager(getSessionFactory());
//    }

    @Bean
    public org.neo4j.ogm.config.Configuration configuration() {
        logger.info("=============================configurationconfigurationconfigurationconfigurationconfigurationconfigurationconfiguration");

        return new org.neo4j.ogm.config.Configuration.Builder()
//                .uri("bolt://localhost:7687")
//                .credentials("neo4j","123456")       //用户密码
//                .connectionPoolSize(10)  //配置连接池大小
//                .uri("bolt://192.168.1.92:7687")
//                .credentials("neo4j","zhoury")       //用户密码
//                .autoIndex("update")                 //索引策略
                .uri(uri)
                .credentials(username, password)       //用户密码
                .build();
    }
}
