/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.market.entity.MkRelayPrizeCandidate;
import com.thinkgem.jeesite.modules.plat.entity.OfflineOrder;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 接力撞奖名单DAO接口
 * @author zzj
 * @version 2018-09-05
 */
@MyBatisDao
public interface MkRelayPrizeCandidateDao extends CrudDao<MkRelayPrizeCandidate> {

    long lose();

    @Delete("delete from mk_relay_prize_candidate where status = '2' and mobile = #{mobile}")
    long delActiveByMobile(@Param("mobile") String mobile);

    @Delete("delete from mk_relay_prize_elector where mobile = #{mobile}")
    long delElectorByMobile(@Param("mobile") String mobile);
	
}