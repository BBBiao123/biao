package com.biao.sql.build;

import com.biao.entity.City;
import com.biao.sql.BaseSqlBuild;

public class CitySqlBuild extends BaseSqlBuild<City, Integer> {

    public static final String columns = "id,city_name,province_id,description";

}
