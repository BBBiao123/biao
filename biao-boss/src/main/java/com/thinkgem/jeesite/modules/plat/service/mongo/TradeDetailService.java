package com.thinkgem.jeesite.modules.plat.service.mongo;

import com.thinkgem.jeesite.modules.plat.entity.mongo.TradeDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>Description: .</p>
 *
 * @author xiaoyu(Myth)
 * @version 1.0
 * @date 2018/4/27 17:15
 * @since JDK 1.8
 */
@Component
public class TradeDetailService {

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<TradeDetail> findByOrderNo(String orderNo) {
        Query query = new Query();
        query.addCriteria(Criteria.where("orderNo").is(orderNo));

        return mongoTemplate.find(query, TradeDetail.class);
    }


}
