/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.plat.entity.JsPlatUserCoinVolumeBillHistory;

/**
 * 币币交易历史记录dao
 * @author dazi
 * @version 2018-04-26
 */
@MyBatisDao
public interface JsPlatUserCoinVolumeBillHistoryDao extends CrudDao<JsPlatUserCoinVolumeBillHistory> {

    public static final String columns = " `id`,`user_id`,`coin_symbol`,`priority`,`ref_key`,`op_sign`',`op_lock_volume`,`retry_count`,`op_volume`,`source`,`mark`,`status`,`force_lock`,`hash`,`create_date`,`update_date`,`create_by`,`update_by` " ;


}