package com.biao.mapper;

import com.biao.entity.EthTokenVolume;
import com.biao.sql.build.EthTokenVolumeSqlBuild;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface EthTokenVolumeDao {


    @SelectProvider(type = EthTokenVolumeSqlBuild.class, method = "findById")
    EthTokenVolume findById(String id);

    @Select("select " + EthTokenVolumeSqlBuild.columns + " from eth_token_volume where name =#{name} limit 1")
    EthTokenVolume findByName(String name);

    @Select("select " + EthTokenVolumeSqlBuild.columns + " from eth_token_volume")
    List<EthTokenVolume> findAll();

    @InsertProvider(type = EthTokenVolumeSqlBuild.class, method = "batchInsert")
    void batchInsert(@Param("listValues") List<EthTokenVolume> list);

    @Delete("delete from eth_token_volume")
    long deleteAll();

    @InsertProvider(type = EthTokenVolumeSqlBuild.class, method = "insert")
    void insert(EthTokenVolume ethTokenVolume);


}
