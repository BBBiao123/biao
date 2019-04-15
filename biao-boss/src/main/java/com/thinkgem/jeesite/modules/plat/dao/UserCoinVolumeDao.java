/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.plat.entity.CoinVolumeStat;
import com.thinkgem.jeesite.modules.plat.entity.UserCoinVolume;

import java.util.List;

import org.apache.ibatis.annotations.Update;

/**
 * 币币资产DAO接口
 * @author dazi
 * @version 2018-04-27
 */
@MyBatisDao
public interface UserCoinVolumeDao extends CrudDao<UserCoinVolume> {
    UserCoinVolume findByUserIdAndCoinId(UserCoinVolume userCoinVolume);

    List<CoinVolumeStat> findCoinVolumeStat();
    
    List<UserCoinVolume> findUserVolumeList(UserCoinVolume userCoinVolume);
    
    @Update("update js_plat_user_coin_volume set out_lock_volume = #{outLockVolume},update_date = now() where id = #{id} and update_date = #{updateDate}")
    void updateOutLockVolumeById(UserCoinVolume userCoinVolume);
}