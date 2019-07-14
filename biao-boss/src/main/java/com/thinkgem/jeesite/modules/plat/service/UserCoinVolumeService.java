/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.thinkgem.jeesite.modules.plat.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.IdGen;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.plat.constant.HashSelect;
import com.thinkgem.jeesite.modules.plat.constant.UserCoinVolumeBillStatusEnum;
import com.thinkgem.jeesite.modules.plat.constant.UserCoinVolumeEventEnum;
import com.thinkgem.jeesite.modules.plat.dao.UserCoinVolumeDao;
import com.thinkgem.jeesite.modules.sys.entity.User;

/**
 * 币币资产Service
 * @author dazi
 * @version 2018-04-27
 */
@Service
@Transactional(readOnly = true)
public class UserCoinVolumeService extends CrudService<UserCoinVolumeDao, UserCoinVolume> {

	private HashSelect hashSelect = HashSelect.create(Integer.parseInt(Global.getConfig("plat.deploy.size")));
	
	@Autowired
	private JsPlatUserCoinVolumeBillService jsPlatUserCoinVolumeBillService ;

	@Autowired
	private JsPlatUserCoinVolumeBillHistoryService jsPlatUserCoinVolumeBillHistoryService ;

	public UserCoinVolume get(String id) {
		return super.get(id);
	}
	
	public List<UserCoinVolume> findList(UserCoinVolume userCoinVolume) {
		return super.findList(userCoinVolume);
	}
	
	public List<UserCoinVolume> findUserVolumeList(UserCoinVolume userCoinVolume) {
		return this.dao.findUserVolumeList(userCoinVolume);
	}
	
	public Page<UserCoinVolume> findPage(Page<UserCoinVolume> page, UserCoinVolume userCoinVolume) {
		return super.findPage(page, userCoinVolume);
	}
	
	@Transactional(readOnly = false)
	public void updateOutLockVolumeById(UserCoinVolume userCoinVolume,UserWithdrawLog userWithdrawLog) {
		this.dao.updateOutLockVolumeById(userCoinVolume);
		JsPlatUserCoinVolumeBill jsPlatUserCoinVolumeBill = new JsPlatUserCoinVolumeBill();
		jsPlatUserCoinVolumeBill.setCoinSymbol(userCoinVolume.getCoinSymbol());
		jsPlatUserCoinVolumeBill.setCreateDate(new Date());
		jsPlatUserCoinVolumeBill.setMark("提现审核");
		jsPlatUserCoinVolumeBill.setOpSign(UserCoinVolumeEventEnum.ADD_VOLUME.getEvent());
		jsPlatUserCoinVolumeBill.setOpVolume(userWithdrawLog.getVolume());
		jsPlatUserCoinVolumeBill.setPriority("5");
		jsPlatUserCoinVolumeBill.setRefKey(userWithdrawLog.getId());
		jsPlatUserCoinVolumeBill.setSource("boss userWithdrawLog");
		jsPlatUserCoinVolumeBill.setStatus(UserCoinVolumeBillStatusEnum.UNPROCESSED.getStatus());
		jsPlatUserCoinVolumeBill.setHash(hashSelect.select(HashSelect.createKey(userCoinVolume.getUserId(), userCoinVolume.getCoinSymbol())));
		User user = new User();
		user.setId(userCoinVolume.getUserId());
		jsPlatUserCoinVolumeBill.setUser(user);
		jsPlatUserCoinVolumeBill.setUpdateDate(new Date());
		jsPlatUserCoinVolumeBill.setForceLock(1);
		jsPlatUserCoinVolumeBillService.save(jsPlatUserCoinVolumeBill);
	}
	
	@Transactional(readOnly = false)
	public void save(UserCoinVolume userCoinVolume) {
		super.save(userCoinVolume);
	}
	
	@Transactional(readOnly = false)
	public void insert(UserCoinVolume userCoinVolume) {
		//增加用户资产
		//改成数据库表
		BigDecimal bigDecimal = userCoinVolume.getVolume();
		JsPlatUserCoinVolumeBill jsPlatUserCoinVolumeBill = new JsPlatUserCoinVolumeBill();
		jsPlatUserCoinVolumeBill.setCoinSymbol(userCoinVolume.getCoinSymbol());
		jsPlatUserCoinVolumeBill.setCreateDate(new Date());
		if(bigDecimal.compareTo(new BigDecimal("0"))>0) {
			jsPlatUserCoinVolumeBill.setOpSign(UserCoinVolumeEventEnum.ADD_VOLUME.getEvent());
		}else {
			jsPlatUserCoinVolumeBill.setOpSign(UserCoinVolumeEventEnum.SUB_VOLUME.getEvent());
		}
		jsPlatUserCoinVolumeBill.setOpVolume(bigDecimal.abs());
		jsPlatUserCoinVolumeBill.setPriority("5");
		if(StringUtils.isNotBlank(userCoinVolume.getAirdropId())) {
			jsPlatUserCoinVolumeBill.setRefKey(userCoinVolume.getAirdropId());
			jsPlatUserCoinVolumeBill.setSource("boss airdrop");
			jsPlatUserCoinVolumeBill.setMark("空头");
		}else {
			jsPlatUserCoinVolumeBill.setRefKey(IdGen.uuid());
			jsPlatUserCoinVolumeBill.setSource("boss userCoinVolume");
			jsPlatUserCoinVolumeBill.setMark("修改用户资产");
		}
		jsPlatUserCoinVolumeBill.setStatus(UserCoinVolumeBillStatusEnum.UNPROCESSED.getStatus());
		jsPlatUserCoinVolumeBill.setHash(hashSelect.select(HashSelect.createKey(userCoinVolume.getUserId(), userCoinVolume.getCoinSymbol())));
		User user = new User();
		user.setId(userCoinVolume.getUserId());
		jsPlatUserCoinVolumeBill.setUser(user);
		jsPlatUserCoinVolumeBill.setUpdateDate(new Date());
		jsPlatUserCoinVolumeBillService.save(jsPlatUserCoinVolumeBill);

		/*UserCoinVolume queryUserCoinVolume = this.getByUserIdAndCoinId(userCoinVolume.getUserId(), userCoinVolume.getCoinId());
		if(queryUserCoinVolume==null) {
			queryUserCoinVolume = new UserCoinVolume();
			queryUserCoinVolume.setVolume(new BigDecimal("0"));
			queryUserCoinVolume.setLockVolume(new BigDecimal("0"));
			queryUserCoinVolume.setOutLockVolume(new BigDecimal("0"));
			queryUserCoinVolume.setCoinId(userCoinVolume.getCoinId());
			queryUserCoinVolume.setUserId(userCoinVolume.getUserId());
			queryUserCoinVolume.setCoinSymbol(userCoinVolume.getCoinSymbol());
			queryUserCoinVolume.setCreateDate(new Date());
		}
		if(queryUserCoinVolume.getOutLockVolume()==null) {
			queryUserCoinVolume.setOutLockVolume(new BigDecimal("0"));
		}
		BigDecimal updateVolume = queryUserCoinVolume.getVolume().add(new BigDecimal(userCoinVolume.getVolume()+""));
		queryUserCoinVolume.setVolume(updateVolume);
		this.save(queryUserCoinVolume);
		//清楚缓存
		String haskKey = "user:coin:volume:"+queryUserCoinVolume.getUserId() ;
		JedisUtils.hdel(haskKey, queryUserCoinVolume.getCoinSymbol());*/
	}


	@Transactional(readOnly = false)
	public void insertBill(UserCoinVolume userCoinVolume) {
		//增加用户资产
		BigDecimal bigDecimal = userCoinVolume.getVolume();
		JsPlatUserCoinVolumeBill jsPlatUserCoinVolumeBill = new JsPlatUserCoinVolumeBill();
		jsPlatUserCoinVolumeBill.setCoinSymbol(userCoinVolume.getCoinSymbol());
		jsPlatUserCoinVolumeBill.setCreateDate(new Date());
		if(bigDecimal.compareTo(new BigDecimal("0"))>0) {
			jsPlatUserCoinVolumeBill.setOpSign(UserCoinVolumeEventEnum.ADD_VOLUME.getEvent());
		}else {
			jsPlatUserCoinVolumeBill.setOpSign(UserCoinVolumeEventEnum.SUB_VOLUME.getEvent());
		}
		jsPlatUserCoinVolumeBill.setOpVolume(bigDecimal.abs());
		jsPlatUserCoinVolumeBill.setPriority("5");
		if(StringUtils.isNotBlank(userCoinVolume.getAirdropId())) {
			jsPlatUserCoinVolumeBill.setRefKey(userCoinVolume.getAirdropId());
			jsPlatUserCoinVolumeBill.setSource("boss airdrop");
			jsPlatUserCoinVolumeBill.setMark("空头");
		}else {
			jsPlatUserCoinVolumeBill.setRefKey(IdGen.uuid());
			jsPlatUserCoinVolumeBill.setSource("boss userCoinVolume");
			jsPlatUserCoinVolumeBill.setMark("修改用户资产");
		}
		jsPlatUserCoinVolumeBill.setStatus(UserCoinVolumeBillStatusEnum.UNPROCESSED.getStatus());
		jsPlatUserCoinVolumeBill.setHash(hashSelect.select(HashSelect.createKey(userCoinVolume.getUserId(), userCoinVolume.getCoinSymbol())));
		User user = new User();
		user.setId(userCoinVolume.getUserId());
		jsPlatUserCoinVolumeBill.setUser(user);
		jsPlatUserCoinVolumeBill.setUpdateDate(new Date());
		jsPlatUserCoinVolumeBillService.save(jsPlatUserCoinVolumeBill);

	}

	@Transactional(readOnly = false)
	public void insertBillHistory(UserCoinVolume userCoinVolume) {
		//增加用户资产 操作记录
		BigDecimal bigDecimal = userCoinVolume.getVolume();
		JsPlatUserCoinVolumeBillHistory jsPlatUserCoinVolumeBillHistory = new JsPlatUserCoinVolumeBillHistory();
		jsPlatUserCoinVolumeBillHistory.setCoinSymbol(userCoinVolume.getCoinSymbol());
		jsPlatUserCoinVolumeBillHistory.setCreateDate(new Date());
		if(bigDecimal.compareTo(new BigDecimal("0"))>0) {
			jsPlatUserCoinVolumeBillHistory.setOpSign(UserCoinVolumeEventEnum.ADD_VOLUME.getEvent());
		}else {
			jsPlatUserCoinVolumeBillHistory.setOpSign(UserCoinVolumeEventEnum.SUB_VOLUME.getEvent());
		}
		jsPlatUserCoinVolumeBillHistory.setOpVolume(bigDecimal.abs());
		jsPlatUserCoinVolumeBillHistory.setPriority("5");
		if(StringUtils.isNotBlank(userCoinVolume.getAirdropId())) {
			jsPlatUserCoinVolumeBillHistory.setRefKey(userCoinVolume.getAirdropId());
			jsPlatUserCoinVolumeBillHistory.setSource("boss airdrop");
			jsPlatUserCoinVolumeBillHistory.setMark("空头");
		}else {
			jsPlatUserCoinVolumeBillHistory.setRefKey(IdGen.uuid());
			jsPlatUserCoinVolumeBillHistory.setSource("boss userCoinVolume");
			jsPlatUserCoinVolumeBillHistory.setMark("实名认证通过送币");
		}
		jsPlatUserCoinVolumeBillHistory.setStatus(UserCoinVolumeBillStatusEnum.SUCCESS.getStatus());
		jsPlatUserCoinVolumeBillHistory.setHash(hashSelect.select(HashSelect.createKey(userCoinVolume.getUserId(), userCoinVolume.getCoinSymbol())));
		jsPlatUserCoinVolumeBillHistory.setUserId(userCoinVolume.getUserId());
		jsPlatUserCoinVolumeBillHistory.setUpdateDate(new Date());
		jsPlatUserCoinVolumeBillHistoryService.save(jsPlatUserCoinVolumeBillHistory);

	}
	
	@Transactional(readOnly = false)
	public void delete(UserCoinVolume userCoinVolume) {
		super.delete(userCoinVolume);
	}

    public UserCoinVolume getByUserIdAndCoinId(String userId, String coinId) {
		UserCoinVolume userCoinVolume = new UserCoinVolume();
		userCoinVolume.setUserId(userId);
		userCoinVolume.setCoinId(coinId);
		return dao.findByUserIdAndCoinId(userCoinVolume);
    }

	public List<CoinVolumeStat> findCoinVolumeStat() {
		return dao.findCoinVolumeStat();
	}

	@Transactional(readOnly = false)
	public void updateCoinVolume(UserCoinVolume userCoinVolume) {
		dao.update(userCoinVolume);
	}
}