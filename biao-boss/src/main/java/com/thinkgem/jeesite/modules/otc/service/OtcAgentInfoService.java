/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.otc.service;

import java.util.List;

import com.thinkgem.jeesite.modules.otc.entity.OtcAgentUser;
import com.thinkgem.jeesite.modules.plat.dao.PlatUserDao;
import com.thinkgem.jeesite.modules.plat.entity.PlatUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.otc.entity.OtcAgentInfo;
import com.thinkgem.jeesite.modules.otc.dao.OtcAgentInfoDao;

/**
 * 银商列表Service
 * @author zzj
 * @version 2018-09-17
 */
@Service
@Transactional(readOnly = true)
public class OtcAgentInfoService extends CrudService<OtcAgentInfoDao, OtcAgentInfo> {

	@Autowired
	private OtcAgentUserService otcAgentUserService;

	@Autowired
	private PlatUserDao platUserDao;

	public OtcAgentInfo get(String id) {
		return super.get(id);
	}
	
	public List<OtcAgentInfo> findList(OtcAgentInfo otcAgentInfo) {
		return super.findList(otcAgentInfo);
	}
	
	public Page<OtcAgentInfo> findPage(Page<OtcAgentInfo> page, OtcAgentInfo otcAgentInfo) {
		return super.findPage(page, otcAgentInfo);
	}
	
	@Transactional(readOnly = false)
	public void save(OtcAgentInfo otcAgentInfo) {

		super.save(otcAgentInfo);

		if(!otcAgentInfo.getIsNewRecord()){
			OtcAgentUser otcAgentUser = new OtcAgentUser();
			otcAgentUser.setAgentId(otcAgentInfo.getId());
			List<OtcAgentUser> otcAgentUserList = otcAgentUserService.findList(otcAgentUser);
			for (OtcAgentUser agentUser : otcAgentUserList){
				PlatUser platUser = platUserDao.get(agentUser.getUserId());
				platUser.setC2cOut("0"); //0:可以转出1：不可以转出
			}
			otcAgentUserService.deleteByAgentId(otcAgentInfo.getId());
		}

		if(otcAgentInfo.getUserIds() != null && otcAgentInfo.getUserIds().length > 0){
			for (int i = 0; i < otcAgentInfo.getUserIds().length; i++){
				OtcAgentUser otcAgentUser = new OtcAgentUser();
				PlatUser platUser = platUserDao.get(otcAgentInfo.getUserIds()[i]);
				otcAgentUser.setAgentId(otcAgentInfo.getId());
				otcAgentUser.setUserId(platUser.getId());
				otcAgentUser.setMail(platUser.getMail());
				otcAgentUser.setMobile(platUser.getMobile());
				otcAgentUser.setRealName(platUser.getRealName());
				otcAgentUserService.save(otcAgentUser);
				platUser.setC2cOut("1"); //0:可以转出1：不可以转出
				platUserDao.update(platUser);
			}
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(OtcAgentInfo otcAgentInfo) {
		super.delete(otcAgentInfo);
	}

	public OtcAgentInfo getBySysUserId(String sysUserId){
		return dao.getBySysUserId(sysUserId);
	}

	public OtcAgentInfo getBySysUserName(String sysUserName){
		return dao.getBySysUserName(sysUserName);
	}

	public OtcAgentInfo getByName(String name){
		return dao.getByName(name);
	}
	
}