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
import com.thinkgem.jeesite.modules.plat.entity.mongo.SecurityLog;

@Component
public class UserSecurityLogService {

	@Autowired
    private MongoTemplate mongoTemplate;
	
	public Page<SecurityLog> findPage(Page<SecurityLog> page,SecurityLog uSecurityLog){
		Query query = new Query();
		if(StringUtils.isNotBlank(uSecurityLog.getType())) {
			query.addCriteria(Criteria.where("type").is(uSecurityLog.getType()));
		}
		if(StringUtils.isNotBlank(uSecurityLog.getUserId())) {
			query.addCriteria(Criteria.where("userId").is(uSecurityLog.getUserId()));
		}
		if(StringUtils.isNotBlank(uSecurityLog.getMobile())) {
			query.addCriteria(Criteria.where("mobile").is(uSecurityLog.getMobile()));
		}
		if(uSecurityLog.getStatus()!=null) {
			//0:成功 1:失败
			query.addCriteria(Criteria.where("status").is(uSecurityLog.getStatus()));
		}
		Long totalCount = mongoTemplate.count(query, SecurityLog.class);
		if (Objects.isNull(totalCount) || totalCount <= 0) {
			page.setCount(0);
			return page ;
		}
		Sort sort = new Sort(Sort.Direction.DESC, "updateTime");
	    Pageable pageable = PageRequest.of(page.getPageNo() - 1, page.getPageSize(), sort);
	    query.with(pageable);
	    List<SecurityLog> userSecurityLogs = mongoTemplate.find(query, SecurityLog.class);
	    if(!CollectionUtils.isEmpty(userSecurityLogs)) {
	    	userSecurityLogs.stream().filter(userSecurity->userSecurity.getUpdateTime()!=null).forEach(userSecurity->{
	    		userSecurity.setUpdateTimeStr(userSecurity.getUpdateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
	    	});
	    }
	    page.setCount(totalCount);
	    page.setList(userSecurityLogs);
		return page ;
	}
}
