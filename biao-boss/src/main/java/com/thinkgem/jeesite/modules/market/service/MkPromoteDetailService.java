/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.thinkgem.jeesite.modules.market.entity.MkDistributeDividendDetail;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.market.entity.MkPromoteDetail;
import com.thinkgem.jeesite.modules.market.dao.MkPromoteDetailDao;

/**
 * 会员推广明细Service
 * @author zhangzijun
 * @version 2018-07-20
 */
@Service
@Transactional(readOnly = true)
public class MkPromoteDetailService extends CrudService<MkPromoteDetailDao, MkPromoteDetail> {

	public MkPromoteDetail get(String id) {
		return super.get(id);
	}
	
	public List<MkPromoteDetail> findList(MkPromoteDetail mkPromoteDetail) {
		return super.findList(mkPromoteDetail);
	}
	
	public Page<MkPromoteDetail> findPage(Page<MkPromoteDetail> page, MkPromoteDetail mkPromoteDetail) {
		return super.findPage(page, mkPromoteDetail);
	}
	
	@Transactional(readOnly = false)
	public void save(MkPromoteDetail mkPromoteDetail) {
		super.save(mkPromoteDetail);
	}
	
	@Transactional(readOnly = false)
	public void delete(MkPromoteDetail mkPromoteDetail) {
		super.delete(mkPromoteDetail);
	}

	@Transactional(readOnly = false)
	public void deleteById(String id) {
		dao.deleteById(id);
	}


	@Transactional(readOnly = false)
	public void saveDetails(List<MkPromoteDetail> mkPromoteDetails){
		for (MkPromoteDetail mkPromoteDetail : mkPromoteDetails){
			super.save(mkPromoteDetail);
		}
	}

	public String checkDetails(List<MkPromoteDetail> mkPromoteDetails){
		String result = "";
		Map<String,Object> levelMap = new HashMap<String, Object>();
		for (MkPromoteDetail mkPromoteDetail : mkPromoteDetails){
			if(levelMap.containsKey(mkPromoteDetail.getLevel())){
				result = String.format("层级[%s]不能重复！", mkPromoteDetail.getLevel());
				return result;
			}else{
				levelMap.put(mkPromoteDetail.getLevel(),"");
			}
		}
		return result;
	}


}