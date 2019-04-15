/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.service.ServiceException;
import com.thinkgem.jeesite.common.utils.SnowFlake;
import com.thinkgem.jeesite.modules.plat.constant.HashSelect;
import com.thinkgem.jeesite.modules.plat.constant.UserCoinVolumeBillStatusEnum;
import com.thinkgem.jeesite.modules.plat.constant.UserCoinVolumeEventEnum;
import com.thinkgem.jeesite.modules.plat.entity.JsPlatOfflineTransferLog;
import com.thinkgem.jeesite.modules.plat.entity.JsPlatUserCoinVolumeBill;
import com.thinkgem.jeesite.modules.sys.entity.User;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.plat.entity.OfflineCoinVolume;
import com.thinkgem.jeesite.modules.plat.dao.OfflineCoinVolumeDao;

/**
 * c2c资产Service
 * @author dazi
 * @version 2018-04-27
 */
@Service
@Transactional(readOnly = true)
public class OfflineCoinVolumeService extends CrudService<OfflineCoinVolumeDao, OfflineCoinVolume> {

	@Autowired
	private JsPlatOfflineTransferLogService jsPlatOfflineTransferLogService;

	@Autowired
	private OfflineCoinVolumeDao offlineCoinVolumeDao;

	@Autowired
	private JsPlatUserCoinVolumeBillService jsPlatUserCoinVolumeBillService;

	private HashSelect hashSelect = HashSelect.create(Integer.parseInt(Global.getConfig("plat.deploy.size")));

	public OfflineCoinVolume get(String id) {
		return super.get(id);
	}
	
	public List<OfflineCoinVolume> findList(OfflineCoinVolume offlineCoinVolume) {
		return super.findList(offlineCoinVolume);
	}
	
	public Page<OfflineCoinVolume> findPage(Page<OfflineCoinVolume> page, OfflineCoinVolume offlineCoinVolume) {
		return super.findPage(page, offlineCoinVolume);
	}
	
	@Transactional(readOnly = false)
	public void save(OfflineCoinVolume offlineCoinVolume) {
		super.save(offlineCoinVolume);
	}
	
	@Transactional(readOnly = false)
	public void delete(OfflineCoinVolume offlineCoinVolume) {
		super.delete(offlineCoinVolume);
	}

	@Transactional(readOnly = false)
	public void transfer(OfflineCoinVolume offlineCoinVolume) {

		if(new BigDecimal(offlineCoinVolume.getVolume()).compareTo(BigDecimal.ZERO) <= 0){
			throw new ServiceException("资产不足，划转失败！");
		}

		long count = offlineCoinVolumeDao.updateVolume(offlineCoinVolume.getUserId(), offlineCoinVolume.getCoinId(), BigDecimal.ZERO, new Timestamp(offlineCoinVolume.getUpdateDate().getTime()), offlineCoinVolume.getVersion());
		if(count != 1){
			throw new ServiceException("更新失败！");
		}

		User user = new User();
		user.setId(offlineCoinVolume.getUserId());

		JsPlatOfflineTransferLog offlineTransferLog = new JsPlatOfflineTransferLog();
		offlineTransferLog.setId(SnowFlake.createSnowFlake().nextIdString());
		offlineTransferLog.setIsNewRecord(true);
		offlineTransferLog.setCreateDate(new Date());
		offlineTransferLog.setUpdateDate(new Date());
		offlineTransferLog.setUser(user);
		offlineTransferLog.setUserId(offlineCoinVolume.getUserId());
		offlineTransferLog.setCoinSymbol(offlineCoinVolume.getCoinSymbol());
		offlineTransferLog.setVolume(offlineCoinVolume.getVolume());
		offlineTransferLog.setType("1");
		offlineTransferLog.setCoinId(offlineCoinVolume.getCoinId());
		offlineTransferLog.setSourceVolume(offlineCoinVolume.getVolume());
		offlineTransferLog.setMark("Boss手工划转,C2C转币币");
		jsPlatOfflineTransferLogService.save(offlineTransferLog);

		JsPlatUserCoinVolumeBill jsPlatUserCoinVolumeBill = new JsPlatUserCoinVolumeBill();
		jsPlatUserCoinVolumeBill.setId(SnowFlake.createSnowFlake().nextIdString());
		jsPlatUserCoinVolumeBill.setIsNewRecord(true);
		jsPlatUserCoinVolumeBill.setCoinSymbol(offlineCoinVolume.getCoinSymbol());
		jsPlatUserCoinVolumeBill.setCreateDate(new Date());
		jsPlatUserCoinVolumeBill.setMark("Boss手工划转,C2C转币币");
		jsPlatUserCoinVolumeBill.setOpSign(UserCoinVolumeEventEnum.ADD_VOLUME.getEvent());
		jsPlatUserCoinVolumeBill.setOpVolume(new BigDecimal(offlineCoinVolume.getVolume()));
		jsPlatUserCoinVolumeBill.setPriority("100");
		jsPlatUserCoinVolumeBill.setRefKey(offlineTransferLog.getId());
		jsPlatUserCoinVolumeBill.setSource("boss transfer");
		jsPlatUserCoinVolumeBill.setForceLock(1);
		jsPlatUserCoinVolumeBill.setStatus(UserCoinVolumeBillStatusEnum.UNPROCESSED.getStatus());

		jsPlatUserCoinVolumeBill.setHash(hashSelect.select(HashSelect.createKey(user.getId(), offlineCoinVolume.getCoinSymbol())));
		jsPlatUserCoinVolumeBill.setUser(user);
		jsPlatUserCoinVolumeBill.setUpdateDate(new Date());
		jsPlatUserCoinVolumeBillService.save(jsPlatUserCoinVolumeBill);

	}


}