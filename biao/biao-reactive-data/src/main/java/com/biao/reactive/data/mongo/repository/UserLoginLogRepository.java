package com.biao.reactive.data.mongo.repository;

import com.biao.reactive.data.mongo.domain.UserLoginLog;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserLoginLogRepository extends MongoRepository<UserLoginLog, String> {

}
