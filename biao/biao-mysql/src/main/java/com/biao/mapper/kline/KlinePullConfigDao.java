package com.biao.mapper.kline;

import com.biao.entity.kline.KlinePullConfig;
import com.biao.sql.build.kline.KlinePullConfigSqlBuild;
import org.apache.ibatis.annotations.InsertProvider;
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


    /**
     * Find all list.
     *
     * @return the list
     */
    @Select("select  " + KlinePullConfigSqlBuild.columns +
            "  from kline_pull_config where status = 1")
    List<KlinePullConfig> findAll();

    /**
     * Insert int.
     *
     * @param klinePullConfig the kline pull config
     * @return the int
     */
    @InsertProvider(type = KlinePullConfigSqlBuild.class, method = "insert")
    int insert(KlinePullConfig klinePullConfig);
}
