/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.plat.entity.RobotConfigUnsafe;
import com.thinkgem.jeesite.modules.plat.dao.RobotConfigUnsafeDao;

/**
 * 币安自动化管理Service
 * @author xiaoyu
 * @version 2018-12-25
 */
@Service
@Transactional(readOnly = true)
public class RobotConfigUnsafeService extends CrudService<RobotConfigUnsafeDao, RobotConfigUnsafe> {

	public RobotConfigUnsafe get(String id) {
		return super.get(id);
	}
	
	public List<RobotConfigUnsafe> findList(RobotConfigUnsafe robotConfigUnsafe) {
		return super.findList(robotConfigUnsafe);
	}
	
	public Page<RobotConfigUnsafe> findPage(Page<RobotConfigUnsafe> page, RobotConfigUnsafe robotConfigUnsafe) {
		return super.findPage(page, robotConfigUnsafe);
	}
	
	@Transactional(readOnly = false)
	public void save(RobotConfigUnsafe robotConfigUnsafe) {
		super.save(robotConfigUnsafe);
	}
	
	@Transactional(readOnly = false)
	public void delete(RobotConfigUnsafe robotConfigUnsafe) {
		super.delete(robotConfigUnsafe);
	}
	
}