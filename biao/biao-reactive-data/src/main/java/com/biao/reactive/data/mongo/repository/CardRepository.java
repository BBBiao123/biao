package com.biao.reactive.data.mongo.repository;

import com.biao.reactive.data.mongo.domain.Card;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface CardRepository extends MongoRepository<Card, String> {
}
