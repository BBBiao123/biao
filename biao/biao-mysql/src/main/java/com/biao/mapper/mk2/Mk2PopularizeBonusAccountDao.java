package com.biao.mapper.mk2;

import com.biao.entity.mk2.Mk2PopularizeBonusAccount;
import com.biao.sql.build.mk2.Mk2PopularizeBonusAccountBuild;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface Mk2PopularizeBonusAccountDao {

    @Select("SELECT " + Mk2PopularizeBonusAccountBuild.columns + " FROM mk2_popularize_bonus_account t WHERE t.type = #{type}")
    Mk2PopularizeBonusAccount findByTpye(String type);
}
