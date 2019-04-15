package com.biao.dao;

import com.biao.binance.config.KlineData;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface KlineDataDao {

	@Insert("insert into kline_data(id,klime_enum,klime_time,kline_data,coin_main_id,coin_main,coin_other_id,coin_other,create_time) values(#{id},#{klimeEnum},#{klimeTime},#{klineData},#{coinMainId},#{coinMain},#{coinOtherId},#{coinOther},#{createTime})")
	public long insert(KlineData klineData);
	
}
