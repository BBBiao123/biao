/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.plat.entity.CoinVolumeStat;
import com.thinkgem.jeesite.modules.plat.entity.UserChangeRecordVolume;
import com.thinkgem.jeesite.modules.plat.entity.UserCoinVolume;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 币币资产DAO接口
 * @author dazi
 * @version 2018-04-27
 */
@MyBatisDao
public interface UserChangeRecordDao extends CrudDao<UserChangeRecordVolume> {

    List<UserCoinVolume> findUserChangeList(UserChangeRecordVolume userCoinVolume);
    public List<UserChangeRecordVolume> findIncomeList(UserChangeRecordVolume entity);
    public List<UserChangeRecordVolume> findBalanceList(UserChangeRecordVolume entity);
}