package com.biao.mapper;

import com.biao.entity.AreaSell;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;


@Mapper
public interface AreaSellDao {

    @Select("select id, area_id AS areaId, area_name AS areaName, area_paraent_id AS areaParaentId, area_paraent_name AS areaParaentName, lock_volume AS sellPrice, status AS sold, " +
            " (SELECT t.center FROM sys_area t WHERE t.id = a.area_id LIMIT 1) AS center from mk2_popularize_area_member a where a.type = '1' ")
    List<AreaSell> findAll();

    @Select("select id, area_id AS areaId, area_name AS areaName, area_paraent_id AS areaParaentId, area_paraent_name AS areaParaentName, lock_volume AS sellPrice, status AS sold, " +
            " (SELECT t.center FROM sys_area t WHERE t.id = a.area_id LIMIT 1) AS center from mk2_popularize_area_member a where a.status = '0' AND a.type = '1' ")
    List<AreaSell> findSale();

    @Select("select id, area_id AS areaId, area_name AS areaName, area_paraent_id AS areaParaentId, area_paraent_name AS areaParaentName, lock_volume AS sellPrice, status AS sold, " +
            " (SELECT t.center FROM sys_area t WHERE t.id = a.area_id LIMIT 1) AS center from mk2_popularize_area_member a where a.status = '1' AND a.type = '1' ")
    List<AreaSell> findSold();

}
