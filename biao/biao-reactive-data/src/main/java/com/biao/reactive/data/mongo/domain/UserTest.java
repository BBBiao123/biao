package com.biao.reactive.data.mongo.domain;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *  ""1
 */

@Data
@RequiredArgsConstructor
@Document
public class UserTest {

    private @Id
    String id;

    private final String name;


}
