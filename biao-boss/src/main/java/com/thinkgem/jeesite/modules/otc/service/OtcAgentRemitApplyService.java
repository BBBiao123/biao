/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.otc.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.thinkgem.jeesite.common.service.ServiceException;
import com.thinkgem.jeesite.common.utils.SnowFlake;
import com.thinkgem.jeesite.modules.otc.entity.OtcAgentApplyUser;
import com.thinkgem.jeesite.modules.otc.entity.OtcAgentInfo;
import com.thinkgem.jeesite.modules.otc.entity.OtcAgentUser;
import com.thinkgem.jeesite.modules.plat.dao.OfflineCoinVolumeDao;
import com.thinkgem.jeesite.modules.plat.dao.PlatUserDao;
import com.thinkgem.jeesite.modules.plat.entity.OfflineCoinVolume;
import com.thinkgem.jeesite.modules.plat.entity.PlatUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.otc.entity.OtcAgentRemitApply;
import com.thinkgem.jeesite.modules.otc.dao.OtcAgentRemitApplyDao;
import org.springframework.util.ObjectUtils;

/**
 * 银商拨币申请列表Service
 * @author zzj
 * @version 2018-09-18
 */
@Service
@Transactional(readOnly = true)
public class OtcAgentRemitApplyService extends CrudService<OtcAgentRemitApplyDao, OtcAgentRemitApply> {

	@Autowired
	private PlatUserDao platUserDao;

	@Autowired
	private OtcAgentApplyUserService otcAgentApplyUserService;

	@Autowired
	private OtcAgentInfoService otcAgentInfoService;

	@Autowired
	private OfflineCoinVolumeDao offlineCoinVolumeDao;

	public OtcAgentRemitApply get(String id) {
		return super.get(id);
	}
	
	public List<OtcAgentRemitApply> findList(OtcAgentRemitApply otcAgentRemitApply) {
		return super.findList(otcAgentRemitApply);
	}
	
	public Page<OtcAgentRemitApply> findPage(Page<OtcAgentRemitApply> page, OtcAgentRemitApply otcAgentRemitApply) {
		return super.findPage(page, otcAgentRemitApply);
	}
	
	@Transactional(readOnly = false)
	public void save(OtcAgentRemitApply otcAgentRemitApply) {
		super.save(otcAgentRemitApply);

		if(otcAgentRemitApply.getUserIds() != null && otcAgentRemitApply.getUserIds().length > 0){
			for (int i = 0; i < otcAgentRemitApply.getUserIds().length; i++){
				OtcAgentApplyUser otcAgentApplyUser = new OtcAgentApplyUser();
				otcAgentApplyUser.setApplyId(otcAgentRemitApply.getId());
				otcAgentApplyUser.setUserId(otcAgentRemitApply.getUserIds()[i]);
				OtcAgentApplyUser otcAgentApplyUserTmp = otcAgentApplyUserService.getByApplyIdAndUserId(otcAgentApplyUser);
				if(ObjectUtils.isEmpty(otcAgentApplyUserTmp)){
					PlatUser platUser = platUserDao.get(otcAgentRemitApply.getUserIds()[i]);
					otcAgentApplyUser.setAgentId(otcAgentRemitApply.getAgentId());
					otcAgentApplyUser.setAgentName(otcAgentRemitApply.getAgentName());
					otcAgentApplyUser.setStatus("0");
					otcAgentApplyUser.setPercentage(otcAgentRemitApply.getPercentages()[i]);
					otcAgentApplyUser.setApplyId(otcAgentRemitApply.getId());
					otcAgentApplyUser.setCoinId(otcAgentRemitApply.getCoinId());
					otcAgentApplyUser.setCoinSymbol(otcAgentRemitApply.getCoinSymbol());
					otcAgentApplyUser.setVolume(Double.valueOf("0"));
					otcAgentApplyUser.setUserId(platUser.getId());
					otcAgentApplyUser.setMail(platUser.getMail());
					otcAgentApplyUser.setMobile(platUser.getMobile());
					otcAgentApplyUser.setRealName(platUser.getRealName());
				}else{
					otcAgentApplyUser = otcAgentApplyUserTmp;
					otcAgentApplyUser.setPercentage(otcAgentRemitApply.getPercentages()[i]);
				}
				otcAgentApplyUserService.save(otcAgentApplyUser);
			}
		}
	}

	@Transactional(readOnly = false)
	public void audit(OtcAgentRemitApply otcAgentRemitApply) {
		super.save(otcAgentRemitApply);

		OtcAgentApplyUser otcAgentApplyUser = new OtcAgentApplyUser();
		otcAgentApplyUser.setApplyId(otcAgentRemitApply.getId());
		List<OtcAgentApplyUser> otcAgentApplyUserList =  otcAgentApplyUserService.findList(otcAgentApplyUser);
		for (OtcAgentApplyUser agentApplyUser : otcAgentApplyUserList) {
			Double volume = (otcAgentRemitApply.getRemitVolume() * Double.valueOf(agentApplyUser.getPercentage()) / (Double.valueOf("100")));
			BigDecimal bg = new BigDecimal(volume);
			volume = bg.setScale(6, BigDecimal.ROUND_HALF_DOWN).doubleValue();
			agentApplyUser.setVolume(volume);
			otcAgentApplyUserService.save(agentApplyUser);
		}
	}

	@Transactional(readOnly = false)
	public void remit(OtcAgentRemitApply otcAgentRemitApply) {

		super.save(otcAgentRemitApply);

		OtcAgentInfo otcAgentInfo = otcAgentInfoService.get(otcAgentRemitApply.getAgentId());
		OtcAgentApplyUser otcAgentApplyUser = new OtcAgentApplyUser();
		otcAgentApplyUser.setApplyId(otcAgentRemitApply.getId());
		List<OtcAgentApplyUser> otcAgentApplyUserList =  otcAgentApplyUserService.findList(otcAgentApplyUser);

		for (OtcAgentApplyUser agentApplyUser : otcAgentApplyUserList) {

			PlatUser platUser = platUserDao.get(agentApplyUser.getUserId());
			if(StringUtils.isEmpty(platUser.getTag())){
				platUser.setTag(this.getAgentNumber(otcAgentInfo));
				platUserDao.update(platUser); //更新 js_plat_user  tag
			}

			//拨币
			OfflineCoinVolume offlineCoinVolume = offlineCoinVolumeDao.findByUserIdAndCoinId(agentApplyUser.getUserId(), agentApplyUser.getCoinId()); // C2C会员账户资金
			if(null == offlineCoinVolume){
				offlineCoinVolume = new OfflineCoinVolume();
				String id = SnowFlake.createSnowFlake().nextIdString();
				offlineCoinVolume.setId(id);
				offlineCoinVolume.setCoinId(agentApplyUser.getCoinId());
				offlineCoinVolume.setCoinSymbol(agentApplyUser.getCoinSymbol());
				offlineCoinVolume.setUserId(agentApplyUser.getUserId());
				offlineCoinVolume.setVolume(agentApplyUser.getVolume().toString());
				offlineCoinVolume.setAdvertVolume(BigDecimal.ZERO.toString());
				offlineCoinVolume.setLockVolume(BigDecimal.ZERO.toString());
				offlineCoinVolume.setCreateDate(new Date());
				offlineCoinVolume.setUpdateDate(new Date());
				offlineCoinVolumeDao.insert(offlineCoinVolume);// 添加C2C会员账户资金
			}else{
				BigDecimal volume = new BigDecimal(offlineCoinVolume.getVolume()).add(BigDecimal.valueOf(agentApplyUser.getVolume()));
				if( volume.compareTo(BigDecimal.ZERO ) == -1){
					throw new ServiceException("操作非法");
				}
				long count = offlineCoinVolumeDao.updateVolume(offlineCoinVolume.getUserId(), offlineCoinVolume.getCoinId(), volume, new Timestamp(offlineCoinVolume.getUpdateDate().getTime()), offlineCoinVolume.getVersion());// 更新买家C2C账户可用资金
				if (count != 1) {
					throw new ServiceException("更新失败！");
				}
			}

			agentApplyUser.setStatus("1"); //已拨币，//拨币申请会员表
			agentApplyUser.setUpdateDate(new Date());
			otcAgentApplyUserService.save(agentApplyUser);
		}
	}

	@Transactional(readOnly = false)
	public void cancel(OtcAgentRemitApply otcAgentRemitApply) {
		super.save(otcAgentRemitApply);
	}

	private String getAgentNumber(OtcAgentInfo otcAgentInfo){
		if(otcAgentInfo.getNumber() >= 100){
			return "YS".concat(String.valueOf(otcAgentInfo.getNumber()));
		}else if(otcAgentInfo.getNumber() > 10){
			return "YS0".concat(String.valueOf(otcAgentInfo.getNumber()));
		}else{
			return "YS00".concat(String.valueOf(otcAgentInfo.getNumber()));
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(OtcAgentRemitApply otcAgentRemitApply) {
		super.delete(otcAgentRemitApply);
	}
	
}