/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.plat.entity.JsPlatOfflineTransferLog;
import org.apache.ibatis.annotations.Insert;

/**
 * bb to c2c转账日志DAO接口
 * @author ruoyu
 * @version 2018-08-28
 */
@MyBatisDao
public interface JsPlatOfflineTransferLogDao extends CrudDao<JsPlatOfflineTransferLog> {

    long insertByLog(JsPlatOfflineTransferLog transferLog);
}