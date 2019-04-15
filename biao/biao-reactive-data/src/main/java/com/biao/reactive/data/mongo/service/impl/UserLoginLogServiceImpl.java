package com.biao.reactive.data.mongo.service.impl;

import com.biao.pojo.ResponsePage;
import com.biao.query.UserLoginLogQuery;
import com.biao.reactive.data.mongo.domain.UserLoginLog;
import com.biao.reactive.data.mongo.repository.UserLoginLogRepository;
import com.biao.reactive.data.mongo.service.UserLoginLogService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("userLoginLogService")
public class UserLoginLogServiceImpl implements UserLoginLogService {

    @Autowired
    private UserLoginLogRepository loginLogRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void insert(UserLoginLog userLoginLog) {
        loginLogRepository.save(userLoginLog);
    }

    @Override
    public ResponsePage<UserLoginLog> findPage(UserLoginLogQuery userLoginLogQuery) {
        ResponsePage<UserLoginLog> responsePage = new ResponsePage<>();
        Sort sort = new Sort(Sort.Direction.DESC, "loginTime");
        Query query = new Query();
        if (StringUtils.isNotBlank(userLoginLogQuery.getUserId())) {
            query.addCriteria(new Criteria("userId").is(userLoginLogQuery.getUserId()));
        }
        if (StringUtils.isNotBlank(userLoginLogQuery.getSource())) {
            query.addCriteria(new Criteria("source").is(userLoginLogQuery.getSource()));
        }
        final long totalCount = mongoTemplate.count(query, UserLoginLog.class);
        if (totalCount <= 0) {
            return responsePage;
        }
        responsePage.setCount(totalCount);
        Pageable pageable = PageRequest.of(userLoginLogQuery.getCurrentPage() - 1, userLoginLogQuery.getShowCount(), sort);
        query.with(pageable);
        List<UserLoginLog> userLoginLogFlux = mongoTemplate.find(query, UserLoginLog.class);
        responsePage.setList(userLoginLogFlux);
        return responsePage;
    }
}
