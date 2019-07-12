
package com.thinkgem.jeesite.modules.plat.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.plat.entity.Coin;
import org.apache.ibatis.annotations.Select;

/**
 * 币种资料DAO接口
 * @author dazi
 * @version 2018-04-253
 */
@MyBatisDao
public interface CoinDao extends CrudDao<Coin> {
    @Select("select id,parent_id,name,full_name,domain,whitepaper_url,status,token_status,token_volume,withdraw_max_volume,withdraw_min_volume,withdraw_day_max_volume,withdraw_fee_type,withdraw_fee,ico_price,remarks,coin_type from js_plat_coin where name =#{name} limit 1")
    Coin findByName(String name);
}