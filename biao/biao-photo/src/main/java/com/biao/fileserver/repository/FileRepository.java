package com.biao.fileserver.repository;

import com.biao.fileserver.domain.File;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface FileRepository extends MongoRepository<File, String> {
}
