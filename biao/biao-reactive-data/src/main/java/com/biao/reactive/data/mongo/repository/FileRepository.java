package com.biao.reactive.data.mongo.repository;

import com.biao.reactive.data.mongo.domain.File;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface FileRepository extends MongoRepository<File, String> {

}
