/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.market.entity.MkDistributeMining;
import com.thinkgem.jeesite.modules.market.dao.MkDistributeMiningDao;
import org.springframework.util.ObjectUtils;

/**
 * 挖矿规则Service
 * @author zhangzijun
 * @version 2018-07-05
 */
@Service
@Transactional(readOnly = true)
public class MkDistributeMiningService extends CrudService<MkDistributeMiningDao, MkDistributeMining> {

	public MkDistributeMining get(String id) {
		return super.get(id);
	}
	
	public List<MkDistributeMining> findList(MkDistributeMining mkDistributeMining) {
		return super.findList(mkDistributeMining);
	}
	
	public Page<MkDistributeMining> findPage(Page<MkDistributeMining> page, MkDistributeMining mkDistributeMining) {
		return super.findPage(page, mkDistributeMining);
	}
	
	@Transactional(readOnly = false)
	public void save(MkDistributeMining mkDistributeMining) {
		if(mkDistributeMining.getIsNewRecord()){
			mkDistributeMining.setCreateDate(new Date());
			mkDistributeMining.setStatus("0");
		}
		super.save(mkDistributeMining);
	}
	
	@Transactional(readOnly = false)
	public void delete(MkDistributeMining mkDistributeMining) {
		super.delete(mkDistributeMining);
	}


	public boolean isRepeatEnable(MkDistributeMining mkDistributeMining){
		if(mkDistributeMining.getIsNewRecord() || StringUtils.isEmpty(mkDistributeMining.getStatus())
				|| "0".equals(mkDistributeMining.getStatus()) || "2".equals(mkDistributeMining.getStatus())){
			return false;
		}else{
			MkDistributeMining mining = dao.findByStatus("1");
			if(ObjectUtils.isEmpty(mining) || mining.getId().equals(mkDistributeMining.getId())){
				return false;
			}else{
				return true;
			}
		}
	}
	
}