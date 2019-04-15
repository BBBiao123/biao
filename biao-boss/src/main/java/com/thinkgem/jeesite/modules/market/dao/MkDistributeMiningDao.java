/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.market.entity.MkDistributeMining;
import com.thinkgem.jeesite.modules.plat.entity.OfflineCoinVolume;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 挖矿规则DAO接口
 * @author zhangzijun
 * @version 2018-07-05
 */
@MyBatisDao
public interface MkDistributeMiningDao extends CrudDao<MkDistributeMining> {

    public static final String columns = " id,coin_id,coin_symbol,volume,grant_volume,create_date,update_date " ;

    @Select("select "+ columns +" from mk_distribute_mining where status = #{status} limit 1")
    MkDistributeMining findByStatus(@Param("status") String status);
	
}