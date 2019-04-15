/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.market.entity.MkAutoTradeMonitor;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 自动交易监控DAO接口
 * @author zhangzijun
 * @version 2018-08-13
 */
@MyBatisDao
public interface MkAutoTradeMonitorDao extends CrudDao<MkAutoTradeMonitor> {

    @Select("select `id`, `setting_id`, `type`, `status`, `user_id`, `mail`, `mobile`, `coin_main_symbol`, `coin_other_symbol`, `begin_date`, `end_date`, `min_volume`, `max_volume`, `min_price`, `max_price`, `frequency`, `time_unit`, `order_number`, `order_volume`, `remark` from mk_auto_trade_monitor where setting_id = #{settingId} and status in ('0','1')")
    MkAutoTradeMonitor findAcitveBySetting(@Param("settingId") String settingId);
	
}