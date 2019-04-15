/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.service;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.service.ServiceException;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.modules.market.dao.Mk2PopularizeCommonMemberCopyDao;
import com.thinkgem.jeesite.modules.market.dao.Mk2PopularizeCommonMemberDao;
import com.thinkgem.jeesite.modules.market.dao.Mk2PopularizeReleaseLogDao;
import com.thinkgem.jeesite.modules.market.entity.Mk2PopularizeCommonMember;
import com.thinkgem.jeesite.modules.market.entity.Mk2PopularizeCommonMemberCopy;
import com.thinkgem.jeesite.modules.market.entity.Mk2PopularizeReleaseLog;
import com.thinkgem.jeesite.modules.plat.constant.HashSelect;
import com.thinkgem.jeesite.modules.plat.constant.UserCoinVolumeBillStatusEnum;
import com.thinkgem.jeesite.modules.plat.constant.UserCoinVolumeEventEnum;
import com.thinkgem.jeesite.modules.plat.dao.JsPlatUserCoinVolumeHistoryDao;
import com.thinkgem.jeesite.modules.plat.dao.UserCoinVolumeDao;
import com.thinkgem.jeesite.modules.plat.entity.JsPlatUserCoinVolumeBill;
import com.thinkgem.jeesite.modules.plat.entity.JsPlatUserCoinVolumeHistory;
import com.thinkgem.jeesite.modules.plat.entity.UserCoinVolume;
import com.thinkgem.jeesite.modules.plat.service.JsPlatUserCoinVolumeBillService;
import com.thinkgem.jeesite.modules.plat.service.JsPlatUserCoinVolumeHistoryService;
import com.thinkgem.jeesite.modules.sys.entity.User;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * 普通用户Service
 * @author dongfeng
 * @version 2018-08-17
 */
@Service
@Transactional(readOnly = true)
public class Mk2PopularizeCommonMemberService extends CrudService<Mk2PopularizeCommonMemberDao, Mk2PopularizeCommonMember> {

	@Autowired
	private UserCoinVolumeDao userCoinVolumeDao;

	@Autowired
	private JsPlatUserCoinVolumeBillService jsPlatUserCoinVolumeBillService ;

	@Autowired
	private Mk2PopularizeCommonMemberCopyDao mk2PopularizeCommonMemberCopyDao;

	@Autowired
	private Mk2PopularizeReleaseLogDao mk2PopularizeReleaseLogDao;

	@Autowired
	private JsPlatUserCoinVolumeHistoryDao jsPlatUserCoinVolumeHistoryDao;

	private HashSelect hashSelect = HashSelect.create(Integer.parseInt(Global.getConfig("plat.deploy.size")));

	public Mk2PopularizeCommonMember get(String id) {
		return super.get(id);
	}
	
	public List<Mk2PopularizeCommonMember> findList(Mk2PopularizeCommonMember mk2PopularizeCommonMember) {
		return super.findList(mk2PopularizeCommonMember);
	}
	
	public Page<Mk2PopularizeCommonMember> findPage(Page<Mk2PopularizeCommonMember> page, Mk2PopularizeCommonMember mk2PopularizeCommonMember) {
		return super.findPage(page, mk2PopularizeCommonMember);
	}
	
	@Transactional(readOnly = false)
	public void save(Mk2PopularizeCommonMember mk2PopularizeCommonMember) {
//		mk2PopularizeCommonMember.setType("1");
		checkValid(mk2PopularizeCommonMember);
		if (StringUtils.isBlank(mk2PopularizeCommonMember.getId())) {
			lockCommonMemberVolume(mk2PopularizeCommonMember);
			mk2PopularizeCommonMember.setLockStatus("1");
			super.save(mk2PopularizeCommonMember);
		} else {
			dao.updateReleaseInfo(mk2PopularizeCommonMember);
		}
	}

	public void checkValid(Mk2PopularizeCommonMember mk2PopularizeCommonMember) {
		if (StringUtils.isBlank(mk2PopularizeCommonMember.getUserId())) {
			throw new ServiceException("会员用户ID参数有误!");
		}
		if (mk2PopularizeCommonMember.getReleaseBeginDate() == null) {
			throw new ServiceException("释放开始时间参数有误!");
		}
		if (mk2PopularizeCommonMember.getReleaseCycleRatio() == null || mk2PopularizeCommonMember.getReleaseCycleRatio().compareTo(BigDecimal.ZERO) < 0) {
			throw new ServiceException("周期释放数量参数有误!");
		}
	}

	/**
	 * 执行冻结节点人资金数量
	 * @param mk2PopularizeCommonMember
	 */
	public void lockCommonMemberVolume(Mk2PopularizeCommonMember mk2PopularizeCommonMember) {
		if (mk2PopularizeCommonMember.getLockVolume() == null || mk2PopularizeCommonMember.getLockVolume().compareTo(BigDecimal.ZERO) != 1) {
			throw new ServiceException("冻结资金数量设置有误！");
		}

		UserCoinVolume userParan = new UserCoinVolume();
		userParan.setUserId(mk2PopularizeCommonMember.getUserId());
		userParan.setCoinId(mk2PopularizeCommonMember.getCoinId());
		UserCoinVolume userCoinVolume = userCoinVolumeDao.findByUserIdAndCoinId(userParan);
		if (userCoinVolume == null || userCoinVolume.getVolume().compareTo(mk2PopularizeCommonMember.getLockVolume()) == -1) {
			throw new ServiceException("该普通冻结会员可用资金数量不足！");
		}

        mk2PopularizeCommonMember.setRelationId(UUID.randomUUID().toString());
        mk2PopularizeCommonMember.setRemarks("异步减常规资产");

		JsPlatUserCoinVolumeBill jsPlatUserCoinVolumeBill = new JsPlatUserCoinVolumeBill();
		jsPlatUserCoinVolumeBill.setCoinSymbol(userCoinVolume.getCoinSymbol());
		jsPlatUserCoinVolumeBill.setCreateDate(new Date());
		jsPlatUserCoinVolumeBill.setMark("会员冻结资产");
        jsPlatUserCoinVolumeBill.setForceLock(1);// 强制拿锁
		jsPlatUserCoinVolumeBill.setOpSign(UserCoinVolumeEventEnum.SUB_VOLUME.getEvent());
		jsPlatUserCoinVolumeBill.setOpVolume(mk2PopularizeCommonMember.getLockVolume().abs());
		jsPlatUserCoinVolumeBill.setPriority("50");
		jsPlatUserCoinVolumeBill.setRefKey(mk2PopularizeCommonMember.getRelationId());
		jsPlatUserCoinVolumeBill.setSource("boss 冻结");
		jsPlatUserCoinVolumeBill.setStatus(UserCoinVolumeBillStatusEnum.UNPROCESSED.getStatus());
		jsPlatUserCoinVolumeBill.setHash(hashSelect.select(HashSelect.createKey(mk2PopularizeCommonMember.getUserId(), mk2PopularizeCommonMember.getCoinSymbol())));
		User user = new User();
		user.setId(mk2PopularizeCommonMember.getUserId());
		jsPlatUserCoinVolumeBill.setUser(user);
		jsPlatUserCoinVolumeBill.setUpdateDate(new Date());
		jsPlatUserCoinVolumeBillService.save(jsPlatUserCoinVolumeBill);
	}

	@Transactional(readOnly = false)
	public void release(BigDecimal manualReleaseVolume, String commonMemberId) {
		if (manualReleaseVolume.compareTo(BigDecimal.ZERO) < 1) {
			throw new ServiceException("普通用户释放数量必须大于0!" + manualReleaseVolume);
		}
		Mk2PopularizeCommonMember commonMember = get(commonMemberId);
		if (Objects.isNull(commonMember)) {
			throw new ServiceException("普通用户冻结信息不存在!" + commonMemberId);
		}
		BigDecimal lastVolume = commonMember.getLockVolume().subtract(commonMember.getReleaseVolume());
		if (lastVolume.compareTo(manualReleaseVolume) == -1) {
			throw new ServiceException("手动释放数量" + manualReleaseVolume + "超出剩余数量" + lastVolume);
		}

		// 1、更新会员的释放数量
		if (lastVolume.compareTo(manualReleaseVolume) == 0) {
			commonMember.setReleaseOver("1");
		}
		commonMember.setReleaseVolume(commonMember.getReleaseVolume().add(manualReleaseVolume));
		dao.updateReleaseVolume(commonMember);

		// 2、记录释放日志
		Mk2PopularizeReleaseLog log = new Mk2PopularizeReleaseLog();
		log.setId(UUID.randomUUID().toString());
		log.setRelationId(commonMember.getId());
		log.setType("1");// 1普通会员2接点人3合伙人
		log.setUserId(commonMember.getUserId());
		log.setMail(commonMember.getMail());
		log.setMobile(commonMember.getMobile());
		log.setCoinId(commonMember.getCoinId());
		log.setCoinSymbol(commonMember.getCoinSymbol());
		log.setReleaseVolume(manualReleaseVolume.doubleValue());
		log.setReleaseCycleDate(new Date());
		log.setReleaseSource("1");
		log.setReleaseStatus("1");
		log.setRemark("普通会员手动释放");
		mk2PopularizeReleaseLogDao.insert(log);

		// 3、释放数量加到用户BB账户
		JsPlatUserCoinVolumeBill jsPlatUserCoinVolumeBill = new JsPlatUserCoinVolumeBill();
		jsPlatUserCoinVolumeBill.setCoinSymbol(commonMember.getCoinSymbol());
		jsPlatUserCoinVolumeBill.setCreateDate(new Date());
		jsPlatUserCoinVolumeBill.setMark("会员释放资产");
		jsPlatUserCoinVolumeBill.setForceLock(0);// 强制拿锁
		jsPlatUserCoinVolumeBill.setOpSign(UserCoinVolumeEventEnum.ADD_VOLUME.getEvent());
		jsPlatUserCoinVolumeBill.setOpVolume(manualReleaseVolume);
		jsPlatUserCoinVolumeBill.setPriority("1");
		jsPlatUserCoinVolumeBill.setRefKey(log.getId());
		jsPlatUserCoinVolumeBill.setSource("boss 手动释放会员资产");
		jsPlatUserCoinVolumeBill.setStatus(UserCoinVolumeBillStatusEnum.UNPROCESSED.getStatus());
		jsPlatUserCoinVolumeBill.setHash(hashSelect.select(HashSelect.createKey(commonMember.getUserId(), commonMember.getCoinSymbol())));
		User user = new User();
		user.setId(commonMember.getUserId());
		jsPlatUserCoinVolumeBill.setUser(user);
		jsPlatUserCoinVolumeBill.setUpdateDate(new Date());
		jsPlatUserCoinVolumeBillService.save(jsPlatUserCoinVolumeBill);
	}

	@Transactional(readOnly = false)
	public void delete(Mk2PopularizeCommonMember mk2PopularizeCommonMember){

		Mk2PopularizeCommonMemberCopy mk2PopularizeCommonMemberCopy = null;
		try{
			mk2PopularizeCommonMemberCopy = new Mk2PopularizeCommonMemberCopy();
			BeanUtils.copyProperties( mk2PopularizeCommonMemberCopy, mk2PopularizeCommonMember);
			mk2PopularizeCommonMemberCopy.setIsNewRecord(true);

			super.delete(mk2PopularizeCommonMember);
			mk2PopularizeCommonMemberCopyDao.insert(mk2PopularizeCommonMemberCopy);
		}catch (Exception e){
			logger.error("删除普通用户出现异常，{}", e);
			throw new ServiceException("删除普通用户出现异常!");
		}
	}

	@Transactional(readOnly = false)
	public void saveImport(Mk2PopularizeCommonMember mk2PopularizeCommonMember) {
		this.makeParam(mk2PopularizeCommonMember);
		this.checkValid(mk2PopularizeCommonMember);
		super.save(mk2PopularizeCommonMember);
		this.saveHistory(mk2PopularizeCommonMember);
	}

	private void makeParam(Mk2PopularizeCommonMember mk2PopularizeCommonMember){
		mk2PopularizeCommonMember.setId(UUID.randomUUID().toString().replace("-",""));
		mk2PopularizeCommonMember.setIsNewRecord(true);
		mk2PopularizeCommonMember.setLockVolume(BigDecimal.valueOf(mk2PopularizeCommonMember.getLockVolumeDouble()));
		mk2PopularizeCommonMember.setReleaseVolume(BigDecimal.valueOf(mk2PopularizeCommonMember.getReleaseVolumeDouble()));
		mk2PopularizeCommonMember.setReleaseCycleRatio(BigDecimal.valueOf(mk2PopularizeCommonMember.getReleaseCycleRatioDouble()));
		mk2PopularizeCommonMember.setReleaseBeginDate(DateUtils.parseDate(mk2PopularizeCommonMember.getReleaseBeginDateString().concat(" 00:00:00")));
	}

	private void saveHistory(Mk2PopularizeCommonMember mk2PopularizeCommonMember){
		JsPlatUserCoinVolumeHistory jsPlatUserCoinVolumeHistory = new JsPlatUserCoinVolumeHistory();
		jsPlatUserCoinVolumeHistory.setId(UUID.randomUUID().toString().replace("-",""));
		jsPlatUserCoinVolumeHistory.setIsNewRecord(true);
		User user = new User();
		user.setId(mk2PopularizeCommonMember.getUserId());
		jsPlatUserCoinVolumeHistory.setUser(user);
		jsPlatUserCoinVolumeHistory.setAccount(StringUtils.isNotEmpty(mk2PopularizeCommonMember.getMobile()) ? mk2PopularizeCommonMember.getMobile() : mk2PopularizeCommonMember.getMail());
		jsPlatUserCoinVolumeHistory.setCoinId(mk2PopularizeCommonMember.getCoinId());
		jsPlatUserCoinVolumeHistory.setCoinSymbol(mk2PopularizeCommonMember.getCoinSymbol());
		jsPlatUserCoinVolumeHistory.setType("other_scene");
		jsPlatUserCoinVolumeHistory.setVolume(String.valueOf(mk2PopularizeCommonMember.getLockVolume()));
		jsPlatUserCoinVolumeHistory.setRemark("会员币冻结记录ID:".concat(mk2PopularizeCommonMember.getId()));
		jsPlatUserCoinVolumeHistoryDao.insert(jsPlatUserCoinVolumeHistory);
	}

	
}