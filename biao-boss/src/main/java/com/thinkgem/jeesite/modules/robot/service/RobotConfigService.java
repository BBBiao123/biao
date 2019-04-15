/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.robot.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.robot.entity.RobotConfig;
import com.thinkgem.jeesite.modules.robot.dao.RobotConfigDao;

/**
 * 价格机器人Service
 * @author dazi
 * @version 2018-06-06
 */
@Service
@Transactional(readOnly = true)
public class RobotConfigService extends CrudService<RobotConfigDao, RobotConfig> {

	public RobotConfig get(String id) {
		return super.get(id);
	}
	
	public List<RobotConfig> findList(RobotConfig robotConfig) {
		return super.findList(robotConfig);
	}
	
	public Page<RobotConfig> findPage(Page<RobotConfig> page, RobotConfig robotConfig) {
		return super.findPage(page, robotConfig);
	}
	
	@Transactional(readOnly = false)
	public void save(RobotConfig robotConfig) {
		super.save(robotConfig);
	}
	
	@Transactional(readOnly = false)
	public void delete(RobotConfig robotConfig) {
		super.delete(robotConfig);
	}
	
}