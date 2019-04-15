package com.biao.reactive.data.redis.repository;

import com.biao.reactive.data.redis.domain.Task;
import org.springframework.data.repository.CrudRepository;

/**
 * project :biao
 *
 *  ""
 * @version 1.0
 * @date 2018/4/6 下午12:50
 * @since JDK 1.8
 */
public interface TaskRepository extends CrudRepository<Task, String> {
}
