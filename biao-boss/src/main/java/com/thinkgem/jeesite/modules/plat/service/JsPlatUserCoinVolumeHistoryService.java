/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.plat.constant.HashSelect;
import com.thinkgem.jeesite.modules.plat.constant.UserCoinVolumeBillStatusEnum;
import com.thinkgem.jeesite.modules.plat.constant.UserCoinVolumeEventEnum;
import com.thinkgem.jeesite.modules.plat.dao.JsPlatUserCoinVolumeHistoryDao;
import com.thinkgem.jeesite.modules.plat.entity.JsPlatUserCoinVolumeBill;
import com.thinkgem.jeesite.modules.plat.entity.JsPlatUserCoinVolumeHistory;

/**
 * 手动转账Service
 * @author ruoyu
 * @version 2018-08-09
 */
@Service
@Transactional(readOnly = true)
public class JsPlatUserCoinVolumeHistoryService extends CrudService<JsPlatUserCoinVolumeHistoryDao, JsPlatUserCoinVolumeHistory> {

	private static Logger logger = LoggerFactory.getLogger(JsPlatUserCoinVolumeHistoryService.class);
	
	@Autowired
    private	UserCoinVolumeService userCoinVolumeService ;
	@Autowired
	private JsPlatUserCoinVolumeBillService jsPlatUserCoinVolumeBillService ;
	
	private HashSelect hashSelect = HashSelect.create(Integer.parseInt(Global.getConfig("plat.deploy.size")));
	
	public JsPlatUserCoinVolumeHistory get(String id) {
		return super.get(id);
	}
	
	public List<JsPlatUserCoinVolumeHistory> findList(JsPlatUserCoinVolumeHistory jsPlatUserCoinVolumeHistory) {
		return super.findList(jsPlatUserCoinVolumeHistory);
	}
	
	public Page<JsPlatUserCoinVolumeHistory> findPage(Page<JsPlatUserCoinVolumeHistory> page, JsPlatUserCoinVolumeHistory jsPlatUserCoinVolumeHistory) {
		return super.findPage(page, jsPlatUserCoinVolumeHistory);
	}
	
	@Transactional(readOnly = false)
	public void save(JsPlatUserCoinVolumeHistory jsPlatUserCoinVolumeHistory) {
		if(jsPlatUserCoinVolumeHistory.getChangeAsset()!=null && "true".equalsIgnoreCase(jsPlatUserCoinVolumeHistory.getChangeAsset())) {
			logger.warn("增加转账记录,不改变用户资产；account:{}",jsPlatUserCoinVolumeHistory.getAccount());
			super.save(jsPlatUserCoinVolumeHistory);
			return ;
		}
		//记录外键id
		super.save(jsPlatUserCoinVolumeHistory);
		//改成数据库表
		BigDecimal bigDecimal = new BigDecimal(jsPlatUserCoinVolumeHistory.getVolume());
		JsPlatUserCoinVolumeBill jsPlatUserCoinVolumeBill = new JsPlatUserCoinVolumeBill();
		jsPlatUserCoinVolumeBill.setCoinSymbol(jsPlatUserCoinVolumeHistory.getCoinSymbol());
		jsPlatUserCoinVolumeBill.setCreateDate(new Date());
		jsPlatUserCoinVolumeBill.setMark("手工转账");
		if(bigDecimal.compareTo(new BigDecimal("0"))>0) {
			jsPlatUserCoinVolumeBill.setOpSign(UserCoinVolumeEventEnum.ADD_VOLUME.getEvent());
		}else {
			jsPlatUserCoinVolumeBill.setOpSign(UserCoinVolumeEventEnum.SUB_VOLUME.getEvent());
		}
		jsPlatUserCoinVolumeBill.setOpVolume(bigDecimal.abs());
		jsPlatUserCoinVolumeBill.setPriority("100");
		jsPlatUserCoinVolumeBill.setRefKey(jsPlatUserCoinVolumeHistory.getId());
		jsPlatUserCoinVolumeBill.setSource("boss jsPlatUserCoinVolumeHistory");
		jsPlatUserCoinVolumeBill.setForceLock(1);
		jsPlatUserCoinVolumeBill.setStatus(UserCoinVolumeBillStatusEnum.UNPROCESSED.getStatus());
		jsPlatUserCoinVolumeBill.setHash(hashSelect.select(HashSelect.createKey(jsPlatUserCoinVolumeHistory.getUser().getId(), jsPlatUserCoinVolumeHistory.getCoinSymbol())));
		jsPlatUserCoinVolumeBill.setUser(jsPlatUserCoinVolumeHistory.getUser());
		jsPlatUserCoinVolumeBill.setUpdateDate(new Date());
		jsPlatUserCoinVolumeBillService.save(jsPlatUserCoinVolumeBill);
		//增加用户资产
		/*UserCoinVolume userCoinVolume = userCoinVolumeService.getByUserIdAndCoinId(jsPlatUserCoinVolumeHistory.getUser().getId(), jsPlatUserCoinVolumeHistory.getCoinId());
		if(userCoinVolume==null) {
			userCoinVolume = new UserCoinVolume();
			userCoinVolume.setVolume(new BigDecimal("0"));
			userCoinVolume.setLockVolume(new BigDecimal("0"));
			userCoinVolume.setCoinId(jsPlatUserCoinVolumeHistory.getCoinId());
			userCoinVolume.setUserId(jsPlatUserCoinVolumeHistory.getUser().getId());
			userCoinVolume.setCoinSymbol(jsPlatUserCoinVolumeHistory.getCoinSymbol());
			userCoinVolume.setCreateDate(new Date());
		}
		BigDecimal updateVolume = userCoinVolume.getVolume().add(new BigDecimal(jsPlatUserCoinVolumeHistory.getVolume()));
		userCoinVolume.setVolume(updateVolume);
		userCoinVolumeService.save(userCoinVolume);
		//清楚缓存
		String haskKey = "user:coin:volume:"+jsPlatUserCoinVolumeHistory.getUser().getId() ;
		JedisUtils.hdel(haskKey, jsPlatUserCoinVolumeHistory.getCoinSymbol());*/
		
	}
	
	@Transactional(readOnly = false)
	public void delete(JsPlatUserCoinVolumeHistory jsPlatUserCoinVolumeHistory) {
		super.delete(jsPlatUserCoinVolumeHistory);
	}
	
}