package com.biao.dao;

import com.biao.binance.config.KlinePullConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * The interface User register lottery log dao.
 *
 *  ""
 */
@Mapper
public interface KlinePullConfigDao {

    String COLUMNS = " id,coin_main,coin_other,exchange_name,pull_url,proxyed,status";


    /**
     * Find all list.
     *
     * @return the list
     */
    @Select("select  " + COLUMNS + "   from kline_pull_config where status = 1")
    List<KlinePullConfig> findAll();

}
