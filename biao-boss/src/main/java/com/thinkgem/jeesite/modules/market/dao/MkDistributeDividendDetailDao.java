/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.market.entity.MkDistributeDividendDetail;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 分红规则明细DAO接口
 * @author zhangzijun
 * @version 2018-07-16
 */
@MyBatisDao
public interface MkDistributeDividendDetailDao extends CrudDao<MkDistributeDividendDetail> {

    @Delete("delete from mk_distribute_dividend_detail where dividend_id = #{dividendId}")
    long deleteByDividendId(@Param("dividendId") String dividendId);

    @Delete("delete from mk_distribute_dividend_detail where id = #{id}")
    long deleteById(@Param("id") String id);
	
}