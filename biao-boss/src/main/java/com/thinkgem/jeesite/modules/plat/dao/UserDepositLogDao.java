/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.plat.entity.UserDepositLog;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 用户充值管理DAO接口
 *
 * @author dazi
 * @version 2018-05-04
 */
@MyBatisDao
public interface UserDepositLogDao extends CrudDao<UserDepositLog> {

    public List<UserDepositLog> findListCount(UserDepositLog userDepositLog);


    List<UserDepositLog> findListByUserIdAndSymbol(UserDepositLog userDepositLog);

    @Update("update js_plat_user_deposit_log set raise_status = #{raiseStatus},update_date = #{updateDate},update_by = #{updateBy.id} WHERE id = #{id}")
    Integer updateRaiseStatus(UserDepositLog userDepositLog);

}