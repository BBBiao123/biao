package com.biao.mapper.mk2;

import com.biao.entity.mk2.Mk2PopularizeMiningUserTmpBak;
import com.biao.sql.build.mk2.Mk2PopularizeMiningUserTmpBakBuild;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface Mk2PopularizeMiningUserTmpBakDao {

    @InsertProvider(type = Mk2PopularizeMiningUserTmpBakBuild.class, method = "insert")
    void insert(Mk2PopularizeMiningUserTmpBak miningUserTmpBak);

    @InsertProvider(type = Mk2PopularizeMiningUserTmpBakBuild.class, method = "batchInsert")
    void insertBatch(@Param("listValues") List<Mk2PopularizeMiningUserTmpBak> miningUserTmpBaks);

}
