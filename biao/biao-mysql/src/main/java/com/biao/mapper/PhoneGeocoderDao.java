package com.biao.mapper;

import com.biao.entity.PhoneGeocoder;
import com.biao.sql.build.PhoneGeocoderBuild;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface PhoneGeocoderDao {

    @Select("select " + PhoneGeocoder.columns + " from js_plat_phone_geocoder where user_id = #{user_id}")
    PhoneGeocoder findByUserId(String userId);

    @InsertProvider(type = PhoneGeocoderBuild.class, method = "insert")
    void insert(PhoneGeocoder phoneGeocoder);
}
