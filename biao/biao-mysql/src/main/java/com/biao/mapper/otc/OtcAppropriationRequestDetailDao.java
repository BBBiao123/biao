package com.biao.mapper.otc;

import com.biao.entity.otc.OtcAppropriationRequestDetail;
import com.biao.sql.build.otc.OtcAppropriationRequestDetailBuild;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OtcAppropriationRequestDetailDao {

    @InsertProvider(type = OtcAppropriationRequestDetailBuild.class, method = "insert")
    int insert(OtcAppropriationRequestDetail otcAppropriationRequestDetail);

}
