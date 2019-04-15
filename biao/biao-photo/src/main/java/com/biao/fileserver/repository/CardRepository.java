package com.biao.fileserver.repository;

import com.biao.fileserver.domain.Card;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface CardRepository extends MongoRepository<Card, String> {
}
