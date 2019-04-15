package com.biao.reactive.data.redis.domain;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

/**
 * project :biao
 *
 *  ""
 * @version 1.0
 * @date 2018/4/6 下午12:48
 * @since JDK 1.8
 */


@Data
@RedisHash
@Value
@RequiredArgsConstructor
public class Task implements Serializable {

    private static final long serialVersionUID = 9199848247320081613L;

    @Id
    String id;
    String body;
}
