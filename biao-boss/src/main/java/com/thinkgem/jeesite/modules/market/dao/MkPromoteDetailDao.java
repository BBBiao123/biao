/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.market.entity.MkPromoteDetail;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;

/**
 * 会员推广明细DAO接口
 * @author zhangzijun
 * @version 2018-07-20
 */
@MyBatisDao
public interface MkPromoteDetailDao extends CrudDao<MkPromoteDetail> {
    @Delete("delete from mk_distribute_promote_detail where id = #{id}")
    long deleteById(@Param("id") String id);
}