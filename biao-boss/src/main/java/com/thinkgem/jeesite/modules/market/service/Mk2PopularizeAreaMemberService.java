/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.security.shiro.cache.JedisCacheManager;
import com.thinkgem.jeesite.common.service.ServiceException;
import com.thinkgem.jeesite.modules.market.dao.Mk2PopularizeMemberRuleDao;
import com.thinkgem.jeesite.modules.market.entity.Mk2PopularizeMemberRule;
import com.thinkgem.jeesite.modules.plat.constant.HashSelect;
import com.thinkgem.jeesite.modules.plat.constant.RedisKeyConstant;
import com.thinkgem.jeesite.modules.plat.constant.UserCoinVolumeBillStatusEnum;
import com.thinkgem.jeesite.modules.plat.constant.UserCoinVolumeEventEnum;
import com.thinkgem.jeesite.modules.plat.dao.PlatUserDao;
import com.thinkgem.jeesite.modules.plat.dao.UserCoinVolumeDao;
import com.thinkgem.jeesite.modules.plat.entity.JsPlatUserCoinVolumeBill;
import com.thinkgem.jeesite.modules.plat.entity.PlatUser;
import com.thinkgem.jeesite.modules.plat.entity.UserCoinVolume;
import com.thinkgem.jeesite.modules.plat.service.JsPlatUserCoinVolumeBillService;
import com.thinkgem.jeesite.modules.sys.entity.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.market.entity.Mk2PopularizeAreaMember;
import com.thinkgem.jeesite.modules.market.dao.Mk2PopularizeAreaMemberDao;

/**
 * 区域合伙人售卖规则Service
 * @author dongfeng
 * @version 2018-07-24
 */
@Service
@Transactional(readOnly = true)
public class Mk2PopularizeAreaMemberService extends CrudService<Mk2PopularizeAreaMemberDao, Mk2PopularizeAreaMember> {

//	private static final String USER_COIN_VOLUME = "user:coin:volume:";

	@Autowired
	private UserCoinVolumeDao userCoinVolumeDao;

	@Autowired
	private PlatUserDao platUserDao;

	@Autowired
	private Mk2PopularizeMemberRuleDao mk2PopularizeMemberRuleDao;

	@Autowired
	private JsPlatUserCoinVolumeBillService jsPlatUserCoinVolumeBillService ;

	private HashSelect hashSelect = HashSelect.create(Integer.parseInt(Global.getConfig("plat.deploy.size")));

	public Mk2PopularizeAreaMember get(String id) {
		return super.get(id);
	}
	
	public List<Mk2PopularizeAreaMember> findList(Mk2PopularizeAreaMember mk2PopularizeAreaMember) {
		return super.findList(mk2PopularizeAreaMember);
	}

	public Page<Mk2PopularizeAreaMember> findPage(Page<Mk2PopularizeAreaMember> page, Mk2PopularizeAreaMember mk2PopularizeAreaMember) {
		return super.findPage(page, mk2PopularizeAreaMember);
	}

	public List<Mk2PopularizeAreaMember> findShareholder(Mk2PopularizeAreaMember mk2PopularizeAreaMember) {
		return dao.findShareholder(mk2PopularizeAreaMember.getId());
	}

	@Transactional(readOnly = false)
	public void batchSetAreaMember(Mk2PopularizeAreaMember mk2PopularizeAreaMember) {
		dao.updateBatchAreaMember(mk2PopularizeAreaMember);
	}

	@Transactional(readOnly = false)
	public void save(Mk2PopularizeAreaMember mk2PopularizeAreaMember) {

		Mk2PopularizeAreaMember dbAreaMember = dao.get(mk2PopularizeAreaMember.getId());

		if ("1".equals(mk2PopularizeAreaMember.getStatus()) && "1".equals(dbAreaMember.getStatus()) && !dbAreaMember.getUserId().equals(mk2PopularizeAreaMember.getUserId())) {
			throw new ServiceException("该区域已经售出，不能转售给他人！");
		}
		if ("1".equals(mk2PopularizeAreaMember.getStatus())) {
			if (mk2PopularizeAreaMember.getReleaseCycleRatio().compareTo(new BigDecimal(100)) == 1) {
				throw new ServiceException("释放比例设置错误！");
			}
			if ( StringUtils.isBlank(mk2PopularizeAreaMember.getUserId())) {
				throw new ServiceException("请选择区域合伙人用户ID！");
			}
			if (mk2PopularizeAreaMember.getReleaseBeginDate() == null) {
				throw new ServiceException("请选择释放开始时间！");
			}
		}

		saveShareholders(dbAreaMember, mk2PopularizeAreaMember);// 保存股东
		// 保存区域合伙人信息
		if ("1".equals(mk2PopularizeAreaMember.getStatus()) && !"1".equals(dbAreaMember.getStatus())) {// 卖出
			super.save(mk2PopularizeAreaMember);// 保存区域合伙人
			//saveShareholders(dbAreaMember, mk2PopularizeAreaMember);// 保存股东
			soldAreaMember();// 售出区域合伙人总量加1
			sellAreaMember(mk2PopularizeAreaMember, dbAreaMember);
		} else if ("0".equals(mk2PopularizeAreaMember.getStatus()) && "0".equals(dbAreaMember.getStatus())) {// 未售
			super.save(mk2PopularizeAreaMember);
			//saveShareholders(dbAreaMember, mk2PopularizeAreaMember);// 保存股东
		} else if ("1".equals(dbAreaMember.getStatus())) { // 已售出，只允许修改释放数量
			dao.updateAreaMemberReleaseVolume(mk2PopularizeAreaMember.getId(), mk2PopularizeAreaMember.getReleaseCycleRatio(), mk2PopularizeAreaMember.getReleaseBeginDate());
		}

	}

	private void sellAreaMember(Mk2PopularizeAreaMember mk2PopularizeAreaMember, Mk2PopularizeAreaMember dbAreaMember) {
		if ("1".equals(mk2PopularizeAreaMember.getStatus()) && !"1".equals(dbAreaMember.getStatus())) {// 卖出
			if (StringUtils.isBlank(mk2PopularizeAreaMember.getUserId())) {
				throw new ServiceException("卖出该区域必须选择合伙人用户！");
			}

			UserCoinVolume userParan = new UserCoinVolume();
			userParan.setUserId(mk2PopularizeAreaMember.getUserId());
			userParan.setCoinId(mk2PopularizeAreaMember.getCoinId());
			UserCoinVolume userCoinVolume = userCoinVolumeDao.findByUserIdAndCoinId(userParan);
			if (userCoinVolume == null || userCoinVolume.getVolume().compareTo(mk2PopularizeAreaMember.getLockVolume()) == -1) {
				throw new ServiceException("该区域合伙人可用资金数量不足！");
			}

            mk2PopularizeAreaMember.setRelationId(UUID.randomUUID().toString());
			mk2PopularizeAreaMember.setRemarks("异步减常规资产");

			JsPlatUserCoinVolumeBill jsPlatUserCoinVolumeBill = new JsPlatUserCoinVolumeBill();
			jsPlatUserCoinVolumeBill.setCoinSymbol(userCoinVolume.getCoinSymbol());
			jsPlatUserCoinVolumeBill.setCreateDate(new Date());
			jsPlatUserCoinVolumeBill.setMark("冻结资产");
			jsPlatUserCoinVolumeBill.setOpSign(UserCoinVolumeEventEnum.SUB_VOLUME.getEvent());
			jsPlatUserCoinVolumeBill.setOpVolume(mk2PopularizeAreaMember.getLockVolume().abs());
			jsPlatUserCoinVolumeBill.setPriority("50");
            jsPlatUserCoinVolumeBill.setForceLock(1);// 强制拿锁
			jsPlatUserCoinVolumeBill.setRefKey(mk2PopularizeAreaMember.getRelationId());
			jsPlatUserCoinVolumeBill.setSource("boss 冻结");
			jsPlatUserCoinVolumeBill.setStatus(UserCoinVolumeBillStatusEnum.UNPROCESSED.getStatus());
			jsPlatUserCoinVolumeBill.setHash(hashSelect.select(HashSelect.createKey(mk2PopularizeAreaMember.getUserId(), mk2PopularizeAreaMember.getCoinSymbol())));
			User user = new User();
			user.setId(mk2PopularizeAreaMember.getUserId());
			jsPlatUserCoinVolumeBill.setUser(user);
			jsPlatUserCoinVolumeBill.setUpdateDate(new Date());
			jsPlatUserCoinVolumeBillService.save(jsPlatUserCoinVolumeBill);

//			long count = dao.updateUserCoinVolume(mk2PopularizeAreaMember.getLockVolume(), userCoinVolume.getCoinId(), userCoinVolume.getUserId(), userCoinVolume.getUpdateDate());
//			if (count != 1) {
//				throw new ServiceException("该区域合伙人资产数据有误!");
//			}
//			// 删除redis里用户币资产
//			updateRedisUserCoin(userCoinVolume.getUserId(), userCoinVolume.getCoinSymbol());

//			// 更新区域合伙人推荐树top_parent_id
//			updateUserRelationTopParent(mk2PopularizeAreaMember.getUserId());
//
//			// 更新股东推荐树top_parent_id
//			if (mk2PopularizeAreaMember.getShareholderUserId() != null && mk2PopularizeAreaMember.getShareholderUserId().length > 0) {
//				for (String uesrId : mk2PopularizeAreaMember.getShareholderUserId()) {
//					updateUserRelationTopParent(uesrId);
//				}
//			}

		}
	}

	private void soldAreaMember() {
//		Mk2PopularizeMemberRule rule = mk2PopularizeMemberRuleDao.findByType("3");
//		if (rule == null) {
//			throw new ServiceException("区域合伙人总量规则未设置!");
//		}
//		Integer soldMember = rule.getSoldMember() == null ? 0 : rule.getSoldMember();
//		if (rule.getTotalMember() > soldMember) {
//			rule.setSoldMember(soldMember + 1);
//			mk2PopularizeMemberRuleDao.updateRuleMemberSoldNumber(rule);
//		} else {
//			throw new ServiceException("区域合伙人数量已满!");
//		}
	}

	/**
	 * 根据用户ID查询出当前推荐树用户以下的所有区域合伙人/股东
	 * @param topUserId
	 */
//	private void updateUserRelationTopParent(String topUserId) {
//		List<String> userIds = dao.findUserTree(topUserId);
//		for (String userId : userIds) {
//			dao.updateUserTopParentId(userId);
//		}
//	}

	/**
	 * 保存股东
	 * @param dbAreaMember
	 * @param mk2PopularizeAreaMember
	 */
	private void saveShareholders(Mk2PopularizeAreaMember dbAreaMember, Mk2PopularizeAreaMember mk2PopularizeAreaMember) {
		dao.deleteShareholder(dbAreaMember.getId());// 删除股东
		if (mk2PopularizeAreaMember.getShareholderUserId() != null && mk2PopularizeAreaMember.getShareholderRatio() != null) {
			if (mk2PopularizeAreaMember.getShareholderUserId().length != mk2PopularizeAreaMember.getShareholderRatio().length) {
				throw new ServiceException("股东数据有误，股东用户ID和股份比例没有一一对应！");
			}
			BigDecimal totalRatio = BigDecimal.ZERO;
			BigDecimal ratio = BigDecimal.ZERO;
			String userId = null;
			PlatUser platUser = null;
			for (int index = 0; index < mk2PopularizeAreaMember.getShareholderUserId().length; index ++) {
				ratio = (mk2PopularizeAreaMember.getShareholderRatio())[index];
				userId = (mk2PopularizeAreaMember.getShareholderUserId())[index];
				platUser = platUserDao.get(userId);
				if (ratio.compareTo(BigDecimal.ZERO) == 1) {
					totalRatio = totalRatio.add(ratio);
				} else {
					throw new ServiceException("股份比例必须大于0！");
				}
				if (totalRatio.compareTo(new BigDecimal(100)) == 1) {
					throw new ServiceException("股份比例总量不能大于100%！");
				}
				Mk2PopularizeAreaMember shareholder = new Mk2PopularizeAreaMember();
				shareholder.setType("2");
				shareholder.setParentId(dbAreaMember.getId());
				shareholder.setRatio(ratio);
				shareholder.setCoinId(dbAreaMember.getCoinId());
				shareholder.setCoinSymbol(dbAreaMember.getCoinSymbol());
				shareholder.setAreaId(dbAreaMember.getAreaId());
				shareholder.setAreaName(dbAreaMember.getAreaName());
				shareholder.setAreaParaentId(dbAreaMember.getAreaParaentId());
				shareholder.setAreaParaentName(dbAreaMember.getAreaParaentName());
				shareholder.setStatus(mk2PopularizeAreaMember.getStatus());
				shareholder.setUserId(platUser.getId());
				shareholder.setMail(platUser.getMail());
				shareholder.setMobile(platUser.getMobile());
				shareholder.setIdCard(platUser.getIdCard());
				shareholder.setRealName(platUser.getRealName());
				shareholder.setLockVolume(BigDecimal.ZERO);
				super.save(shareholder);
			}
		}
	}

//	private void updateRedisUserCoin(String userId, String coinSymbol) {
//		JedisCacheManager cacheManager = new JedisCacheManager();
//		cacheManager.setCacheKeyPrefix(RedisKeyConstant.USER_COIN_VOLUME);
//		cacheManager.getCache(userId).remove(coinSymbol);
//	}

	@Transactional(readOnly = false)
	public void delete(Mk2PopularizeAreaMember mk2PopularizeAreaMember) {
		super.delete(mk2PopularizeAreaMember);
	}
	
}