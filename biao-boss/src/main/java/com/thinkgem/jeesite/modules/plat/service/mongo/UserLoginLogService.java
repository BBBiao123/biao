package com.thinkgem.jeesite.modules.plat.service.mongo;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.modules.plat.entity.mongo.UserLoginLog;

@Component
public class UserLoginLogService {

	@Autowired
    private MongoTemplate mongoTemplate;
	
	public Page<UserLoginLog> findPage(Page<UserLoginLog> page,UserLoginLog userLoginLog){
		Query query = new Query();
		if(userLoginLog.getStatus()!=null) {
			query.addCriteria(Criteria.where("status").is(userLoginLog.getStatus()));
		}
		if(StringUtils.isNotBlank(userLoginLog.getLoginName())) {
			query.addCriteria(Criteria.where("loginName").is(userLoginLog.getLoginName()));
		}
		Long totalCount = mongoTemplate.count(query, UserLoginLog.class);
		if (Objects.isNull(totalCount) || totalCount <= 0) {
			page.setCount(0);
			return page ;
		}
		Sort sort = new Sort(Sort.Direction.DESC, "loginTime");
	    Pageable pageable = PageRequest.of(page.getPageNo() - 1, page.getPageSize(), sort);
	    query.with(pageable);
	    List<UserLoginLog> userLoginLogs = mongoTemplate.find(query, UserLoginLog.class);
	    if(!CollectionUtils.isEmpty(userLoginLogs)) {
	    	userLoginLogs.stream().filter(userLogin->userLogin.getLoginTime()!=null).forEach(userLogin->{
	    		userLogin.setLoginDate(userLogin.getLoginTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
	    	});
	    }
	    page.setCount(totalCount);
	    page.setList(userLoginLogs);
		return page ;
	}
}
