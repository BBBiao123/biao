package com.biao.reactive.data.mongo.repository;

import com.biao.reactive.data.mongo.domain.UserTest;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface UserRepository extends ReactiveMongoRepository<UserTest, String> {

    Flux<UserTest> findByName(String name);


}
