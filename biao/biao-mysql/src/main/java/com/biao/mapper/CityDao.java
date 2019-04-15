package com.biao.mapper;

import com.biao.entity.City;
import com.biao.sql.build.CitySqlBuild;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CityDao {

    @InsertProvider(type = CitySqlBuild.class, method = "insert")
    void insert(City city);

    @SelectProvider(type = CitySqlBuild.class, method = "findById")
    City findById(String id);

    @UpdateProvider(type = CitySqlBuild.class, method = "updateById")
    long updateById(City city);

    @Select("select " + CitySqlBuild.columns + " from city")
    List<City> findAllCity();

    @Delete("delete from city where id = #{id}")
    Void deleteById(String id);
}
