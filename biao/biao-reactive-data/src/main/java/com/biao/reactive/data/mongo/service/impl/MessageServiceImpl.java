package com.biao.reactive.data.mongo.service.impl;

import com.biao.kafka.interceptor.MessageDTO;
import com.biao.pojo.ResponsePage;
import com.biao.query.MessageQuery;
import com.biao.reactive.data.mongo.domain.Message;
import com.biao.reactive.data.mongo.service.MessageService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * MessageServiceImpl.
 *
 *  ""(Myth)
 */
@Service("messageService")
public class MessageServiceImpl implements MessageService {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public MessageServiceImpl(final MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void insert(final Message message) {
        mongoTemplate.insert(message);
    }

    @Override
    public ResponsePage<MessageDTO> findByPage(final MessageQuery messageQuery) {
        ResponsePage<MessageDTO> responsePage = new ResponsePage<>();

        Query query = new Query();

        if (StringUtils.isNoneBlank(messageQuery.getUserId())) {
            query.addCriteria(new Criteria("toUserId").is(messageQuery.getUserId()));
        }

        final Long totalCount = mongoTemplate.count(query, Message.class);
        if (totalCount <= 0) {
            return responsePage;
        }
        responsePage.setCount(totalCount);

        Sort sort = new Sort(Sort.Direction.DESC, "createTime");

        Pageable pageable = PageRequest.of(messageQuery.getCurrentPage() - 1, messageQuery.getShowCount(), sort);

        query.with(pageable);
        final List<Message> messagesList = mongoTemplate.find(query, Message.class);
        responsePage.setList(convertDTO(messagesList));
        return responsePage;
    }


    private List<MessageDTO> convertDTO(final List<Message> messagesList) {
        if (CollectionUtils.isEmpty(messagesList)) {
            return new ArrayList<>();
        }
        return messagesList.stream().map(message -> {
            MessageDTO dto = new MessageDTO();
            dto.setId(message.getId());
            dto.setUserId(message.getFromUserId());
            dto.setType(message.getType());
            dto.setOrderId(message.getOrderId());
            dto.setStatus(message.getOrderStatus());
            dto.setPrice(message.getPrice());
            dto.setVolume(message.getVolume());
            dto.setCoinMain(message.getCoinMain());
            dto.setCoinOther(message.getCoinOther());
            dto.setCreateTime(message.getCreateTime().toInstant(ZoneOffset.of("+8")).toEpochMilli());
            return dto;
        }).collect(Collectors.toList());
    }


    @Override
    public void removeById(final String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));
        mongoTemplate.remove(query, Message.class);

    }

    @Override
    public void removeByUserId(final String userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("toUserId").is(userId));
        mongoTemplate.remove(query, Message.class);
    }
}
