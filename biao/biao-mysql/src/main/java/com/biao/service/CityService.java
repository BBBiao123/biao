package com.biao.service;


import com.biao.entity.City;

import java.util.List;

/**
 * 城市业务逻辑接口类
 */
public interface CityService {

    /**
     * 获取城市信息列表
     *
     * @return
     */
    List<City> findAllCity();

    /**
     * 根据城市 ID,查询城市信息
     *
     * @param id
     * @return
     */
    City findCityById(String id);

    /**
     * 新增城市信息
     *
     * @param city
     * @return
     */
    void saveCity(City city);

    /**
     * 更新城市信息
     *
     * @param city
     * @return
     */
    void updateCity(City city);

    /**
     * 根据城市 ID,删除城市信息
     *
     * @param id
     * @return
     */
    void deleteCity(String id);
}
