package com.biao.reactive.data.mongo.config;

import com.biao.reactive.data.mongo.convert.BigDecimalToDecimal128Converter;
import com.biao.reactive.data.mongo.convert.Decimal128ToBigDecimalConverter;
import com.mongodb.MongoClientOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.ReactiveMongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

import java.util.ArrayList;
import java.util.List;

/**
 * project :biao
 *
 *  ""
 * @version 1.0
 * @date 2018/4/22 下午5:40
 * @since JDK 1.8
 */
@Configuration
public class MongoReactiveConfig {


    @Autowired(required = false)
    private ReactiveMongoDatabaseFactory mongoDatabaseFactory;


    @Autowired(required = false)
    private MongoDbFactory mongoDbFactory;


    @Bean
    public ReactiveMongoTemplate reactiveMongoTemplate() {
        return new ReactiveMongoTemplate(mongoDatabaseFactory, mappingMongoConverter());

    }

    @Bean
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongoDbFactory, mappingMongoConverter());
    }

    @Bean
    public MongoClientOptions mongoOptions() {
        return MongoClientOptions.builder().maxConnectionIdleTime(6000)
                .socketTimeout(6000).build();
    }


    @Bean
    public MongoMappingContext mongoMappingContext() {
        return new MongoMappingContext();
    }

    @Bean
    public MappingMongoConverter mappingMongoConverter() {
        DefaultDbRefResolver dbRefResolver = new DefaultDbRefResolver(mongoDbFactory);
        MappingMongoConverter converter = new MappingMongoConverter(dbRefResolver, this.mongoMappingContext());
        List<Object> list = new ArrayList<>(2);
        list.add(new BigDecimalToDecimal128Converter());
        list.add(new Decimal128ToBigDecimalConverter());
        converter.setCustomConversions(new MongoCustomConversions(list));
        return converter;
    }
}
