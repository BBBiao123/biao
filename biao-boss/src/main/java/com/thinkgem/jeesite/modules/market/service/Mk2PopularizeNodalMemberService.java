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
import com.thinkgem.jeesite.modules.market.dao.Mk2PopularizeAreaMemberDao;
import com.thinkgem.jeesite.modules.market.dao.Mk2PopularizeMemberRuleDao;
import com.thinkgem.jeesite.modules.market.entity.Mk2PopularizeMemberRule;
import com.thinkgem.jeesite.modules.plat.constant.HashSelect;
import com.thinkgem.jeesite.modules.plat.constant.RedisKeyConstant;
import com.thinkgem.jeesite.modules.plat.constant.UserCoinVolumeBillStatusEnum;
import com.thinkgem.jeesite.modules.plat.constant.UserCoinVolumeEventEnum;
import com.thinkgem.jeesite.modules.plat.dao.UserCoinVolumeDao;
import com.thinkgem.jeesite.modules.plat.entity.JsPlatUserCoinVolumeBill;
import com.thinkgem.jeesite.modules.plat.entity.UserCoinVolume;
import com.thinkgem.jeesite.modules.plat.service.JsPlatUserCoinVolumeBillService;
import com.thinkgem.jeesite.modules.sys.entity.User;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.market.entity.Mk2PopularizeNodalMember;
import com.thinkgem.jeesite.modules.market.dao.Mk2PopularizeNodalMemberDao;

/**
 * 节点人Service
 * @author dongfeng
 * @version 2018-07-24
 */
@Service
@Transactional(readOnly = true)
public class Mk2PopularizeNodalMemberService extends CrudService<Mk2PopularizeNodalMemberDao, Mk2PopularizeNodalMember> {

	@Autowired
	private UserCoinVolumeDao userCoinVolumeDao;

	@Autowired
	private Mk2PopularizeAreaMemberDao mk2PopularizeAreaMemberDao;

	@Autowired
	private Mk2PopularizeMemberRuleDao mk2PopularizeMemberRuleDao;

	@Autowired
	private JsPlatUserCoinVolumeBillService jsPlatUserCoinVolumeBillService ;

	private HashSelect hashSelect = HashSelect.create(Integer.parseInt(Global.getConfig("plat.deploy.size")));

	public Mk2PopularizeNodalMember get(String id) {
		return super.get(id);
	}
	
	public List<Mk2PopularizeNodalMember> findList(Mk2PopularizeNodalMember mk2PopularizeNodalMember) {
		return super.findList(mk2PopularizeNodalMember);
	}
	
	public Page<Mk2PopularizeNodalMember> findPage(Page<Mk2PopularizeNodalMember> page, Mk2PopularizeNodalMember mk2PopularizeNodalMember) {
		return super.findPage(page, mk2PopularizeNodalMember);
	}

	public List<Mk2PopularizeNodalMember> findLockReleaseInfo(String id) {
		return dao.findLockReleaseInfo(id);
	}

	/**
	 * 保存节点人用户信息
	 * @param mk2PopularizeNodalMember
	 */
	@Transactional(readOnly = false)
	public void save(Mk2PopularizeNodalMember mk2PopularizeNodalMember) {
		checkValid(mk2PopularizeNodalMember);// 参数校验
		checkUserExists(mk2PopularizeNodalMember.getUserId());// 判断该节点人是否已经存在
		mk2PopularizeNodalMember.setType("1"); // 节点人
		mk2PopularizeNodalMember.setLockStatus("0");// 未锁定
		soldNodalMember();// 节点人数量加1
		super.save(mk2PopularizeNodalMember);
	}

	private void checkValid(Mk2PopularizeNodalMember mk2PopularizeNodalMember) {
		if (StringUtils.isBlank(mk2PopularizeNodalMember.getUserId())) {
			throw new ServiceException("节点人ID参数有误!");
		}
		if (StringUtils.isBlank(mk2PopularizeNodalMember.getCoinId())) {
			throw new ServiceException("冻结币种参数有误!");
		}
//		if (mk2PopularizeNodalMember.getReleaseBeginDate() == null) {
//			throw new ServiceException("释放开始时间参数有误!");
//		}
//		if (mk2PopularizeNodalMember.getReleaseCycleRatio() == null || mk2PopularizeNodalMember.getReleaseCycleRatio().compareTo(BigDecimal.ZERO) != 1) {
//			throw new ServiceException("周期释放数量参数有误!");
//		}
//		if (mk2PopularizeNodalMember.getReleaseCycleRatio() == null || mk2PopularizeNodalMember.getReleaseCycleRatio().compareTo(new BigDecimal(100)) == 1) {
//			throw new ServiceException("释放比例设置错误!");
//		}
	}
	private void checkUserExists(String userId) {
		List<Mk2PopularizeNodalMember> nodalMembers = dao.findByUserId(userId);
		if (CollectionUtils.isNotEmpty(nodalMembers)) {
			throw new ServiceException("该节点人已经存在!");
		}
	}

	private void soldNodalMember() {
//		Mk2PopularizeMemberRule rule = mk2PopularizeMemberRuleDao.findByType("2");
//		if (rule == null) {
//			throw new ServiceException("节点人总量规则未设置!");
//		}
//		Integer soldMember = rule.getSoldMember() == null ? 0 : rule.getSoldMember();
//		if (rule.getTotalMember() > soldMember) {
//			rule.setSoldMember(soldMember + 1);
//			mk2PopularizeMemberRuleDao.updateRuleMemberSoldNumber(rule);
//		} else {
//			throw new ServiceException("节点人数量已满!");
//		}
	}

	/**
	 * 保存节点人冻结释放信息
	 * @param mk2PopularizeNodalMember
	 */
	@Transactional(readOnly = false)
	public void saveLockReleaseInfo(Mk2PopularizeNodalMember mk2PopularizeNodalMember) {
		mk2PopularizeNodalMember.setType("2");
		if (StringUtils.isBlank(mk2PopularizeNodalMember.getParentId())) {
			throw new ServiceException("该节点人冻结和释放规则的用户信息有误!");
		}
		Mk2PopularizeNodalMember parentDbMember = dao.get(mk2PopularizeNodalMember.getParentId());
		if (parentDbMember == null) {
			throw new ServiceException("该节点人冻结和释放规则的用户信息有误!");
		}
		// 确保和父nodal一致性属性
		mk2PopularizeNodalMember.setCoinId(parentDbMember.getCoinId());
		mk2PopularizeNodalMember.setCoinSymbol(parentDbMember.getCoinSymbol());
		mk2PopularizeNodalMember.setUserId(parentDbMember.getUserId());
		mk2PopularizeNodalMember.setMail(parentDbMember.getMail());
		mk2PopularizeNodalMember.setMobile(parentDbMember.getMobile());

		if (StringUtils.isBlank(mk2PopularizeNodalMember.getId())) { // 保存节点人冻结和释放信息
			super.save(mk2PopularizeNodalMember);
			if ("1".equals(mk2PopularizeNodalMember.getLockStatus())) {// 冻结节点人资金 1锁定0未锁定
				lockNodalMemberVolume(mk2PopularizeNodalMember);
			}
		} else { // 更新节点人冻结和释放信息
			long count = 0;
			Mk2PopularizeNodalMember dbLockReLeaseInfo = dao.get(mk2PopularizeNodalMember.getId());
			if ("1".equals(dbLockReLeaseInfo.getLockStatus())) { // 之前已锁定更新，只能更新释放开始时间和释放比例
				count = dao.updateHadLockReleaseInfo(mk2PopularizeNodalMember);
			} else {// 之前未锁定更新
				if ("1".equals(mk2PopularizeNodalMember.getLockStatus())) { // 冻结节点人资金 1锁定0未锁定
					lockNodalMemberVolume(mk2PopularizeNodalMember);
				}
				count = dao.updateNoLockReleaseInfo(mk2PopularizeNodalMember); // 之前未锁定更新
			}
			if (count != 1) {
				throw new ServiceException("该节点人冻结和释放规则更新失败!");
			}
		}
	}

	/**
	 * 执行冻结节点人资金数量
	 * @param mk2PopularizeNodalMember
	 */
	public void lockNodalMemberVolume(Mk2PopularizeNodalMember mk2PopularizeNodalMember) {
		if (mk2PopularizeNodalMember.getLockVolume() == null || mk2PopularizeNodalMember.getLockVolume().compareTo(BigDecimal.ZERO) != 1) {
			throw new ServiceException("冻结资金数量设置有误！");
		}
		UserCoinVolume userParan = new UserCoinVolume();
		userParan.setUserId(mk2PopularizeNodalMember.getUserId());
		userParan.setCoinId(mk2PopularizeNodalMember.getCoinId());
		UserCoinVolume userCoinVolume = userCoinVolumeDao.findByUserIdAndCoinId(userParan);
		if (userCoinVolume == null || userCoinVolume.getVolume().compareTo(mk2PopularizeNodalMember.getLockVolume()) == -1) {
			throw new ServiceException("该节点人可用资金数量不足！");
		}

        mk2PopularizeNodalMember.setRelationId(UUID.randomUUID().toString());
        mk2PopularizeNodalMember.setRemarks("异步减常规资产");

		JsPlatUserCoinVolumeBill jsPlatUserCoinVolumeBill = new JsPlatUserCoinVolumeBill();
		jsPlatUserCoinVolumeBill.setCoinSymbol(userCoinVolume.getCoinSymbol());
		jsPlatUserCoinVolumeBill.setCreateDate(new Date());
		jsPlatUserCoinVolumeBill.setMark("冻结资产");
		jsPlatUserCoinVolumeBill.setOpSign(UserCoinVolumeEventEnum.SUB_VOLUME.getEvent());
		jsPlatUserCoinVolumeBill.setOpVolume(mk2PopularizeNodalMember.getLockVolume().abs());
		jsPlatUserCoinVolumeBill.setPriority("50");
        jsPlatUserCoinVolumeBill.setForceLock(1);// 强制拿锁
		jsPlatUserCoinVolumeBill.setRefKey(mk2PopularizeNodalMember.getRelationId());
		jsPlatUserCoinVolumeBill.setSource("boss 冻结");
		jsPlatUserCoinVolumeBill.setStatus(UserCoinVolumeBillStatusEnum.UNPROCESSED.getStatus());
		jsPlatUserCoinVolumeBill.setHash(hashSelect.select(HashSelect.createKey(mk2PopularizeNodalMember.getUserId(), mk2PopularizeNodalMember.getCoinSymbol())));
		User user = new User();
		user.setId(mk2PopularizeNodalMember.getUserId());
		jsPlatUserCoinVolumeBill.setUser(user);
		jsPlatUserCoinVolumeBill.setUpdateDate(new Date());
		jsPlatUserCoinVolumeBillService.save(jsPlatUserCoinVolumeBill);

//		long count = mk2PopularizeAreaMemberDao.updateUserCoinVolume(mk2PopularizeNodalMember.getLockVolume(), userCoinVolume.getCoinId(), userCoinVolume.getUserId(), userCoinVolume.getUpdateDate());
//		if (count != 1) {
//			throw new ServiceException("该节点人个人资产数据更新失败!");
//		}
//		// 删除redis里用户币资产
//		updateRedisUserCoin(userCoinVolume.getUserId(), userCoinVolume.getCoinSymbol());
	}

//	private void updateRedisUserCoin(String userId, String coinSymbol) {
//		JedisCacheManager cacheManager = new JedisCacheManager();
//		cacheManager.setCacheKeyPrefix(RedisKeyConstant.USER_COIN_VOLUME);
//		cacheManager.getCache(userId).remove(coinSymbol);
//	}

	@Transactional(readOnly = false)
	public void deleteLockRelease(Mk2PopularizeNodalMember mk2PopularizeNodalMember) {
		if (StringUtils.isBlank(mk2PopularizeNodalMember.getId())) {
			throw new ServiceException("该节点人存在冻结释放规则不存在，不能删除!");
		}
		Mk2PopularizeNodalMember dbLockRule = dao.get(mk2PopularizeNodalMember.getId());
		if (dbLockRule == null) {
			throw new ServiceException("该节点人存在冻结释放规则不存在，不能删除!");
		}
		if ("1".equals(dbLockRule.getLockStatus())) {
			throw new ServiceException("该节点人本条冻结释已存在冻结资金，不能删除!");
		}
		super.delete(mk2PopularizeNodalMember);
	}

	@Transactional(readOnly = false)
	public void delete(Mk2PopularizeNodalMember mk2PopularizeNodalMember) {

		long countLockRule = dao.countLockReleaseRule(mk2PopularizeNodalMember.getId());
		if (countLockRule > 0) {
			throw new ServiceException("该节点人存在冻结释放资金规则，不能删除!");
		}
		super.delete(mk2PopularizeNodalMember);
	}
	
}