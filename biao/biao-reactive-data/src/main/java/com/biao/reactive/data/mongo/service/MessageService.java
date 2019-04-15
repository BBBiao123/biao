package com.biao.reactive.data.mongo.service;

import com.biao.kafka.interceptor.MessageDTO;
import com.biao.pojo.ResponsePage;
import com.biao.query.MessageQuery;
import com.biao.reactive.data.mongo.domain.Message;

/**
 * The interface Message service.
 *
 *  ""(Myth)
 */
public interface MessageService {

    /**
     * Insert.
     *
     * @param message the message
     */
    void insert(Message message);


    /**
     * Find by page response page.
     *
     * @param messageQuery the message query
     * @return the response page
     */
    ResponsePage<MessageDTO> findByPage(MessageQuery messageQuery);


    /**
     * Remove by id.
     *
     * @param id the id
     */
    void removeById(String id);


    /**
     * Remove by user id.
     *
     * @param userId the user id
     */
    void removeByUserId(String userId);


}
