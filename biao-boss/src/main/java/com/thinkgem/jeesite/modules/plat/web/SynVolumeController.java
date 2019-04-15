package com.thinkgem.jeesite.modules.plat.web;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.utils.IdGen;
import com.thinkgem.jeesite.common.utils.JedisUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.plat.constant.HashSelect;
import com.thinkgem.jeesite.modules.plat.constant.UserCoinVolumeBillStatusEnum;
import com.thinkgem.jeesite.modules.plat.constant.UserCoinVolumeEventEnum;
import com.thinkgem.jeesite.modules.plat.entity.Coin;
import com.thinkgem.jeesite.modules.plat.entity.JsPlatUserCoinVolumeBill;
import com.thinkgem.jeesite.modules.plat.entity.PlatUser;
import com.thinkgem.jeesite.modules.plat.entity.SynVolume;
import com.thinkgem.jeesite.modules.plat.entity.UserCoinVolume;
import com.thinkgem.jeesite.modules.plat.service.CoinService;
import com.thinkgem.jeesite.modules.plat.service.JsPlatUserCoinVolumeBillService;
import com.thinkgem.jeesite.modules.plat.service.PlatUserService;
import com.thinkgem.jeesite.modules.plat.service.UserCoinVolumeService;
import com.thinkgem.jeesite.modules.sys.entity.User;

@Controller
@RequestMapping(value = "${adminPath}/plat/synVolume")
public class SynVolumeController extends BaseController{

	private static Logger logger = LoggerFactory.getLogger(SynVolumeController.class);
	
	@Autowired
	private CoinService coinService;
	
	@Autowired
    private	UserCoinVolumeService userCoinVolumeService ;
	
	@Autowired
	private JsPlatUserCoinVolumeBillService jsPlatUserCoinVolumeBillService ;
	
	private HashSelect hashSelect = HashSelect.create(Integer.parseInt(Global.getConfig("plat.deploy.size")));

	@Autowired
    private PlatUserService platUserService;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequiresPermissions("plat:synVolume:view")
	@RequestMapping(value = {"list", ""})
	public String list(SynVolume synVolume, HttpServletRequest request, HttpServletResponse response, Model model) {
		PlatUser platUser = null ;
		if(StringUtils.isNotBlank(synVolume.getUserId())) {
			platUser = new PlatUser();
			platUser.setId(synVolume.getUserId());
		}
		if(StringUtils.isNotBlank(synVolume.getMobile())) {
			platUser = new PlatUser();
			platUser.setMobile(synVolume.getMobile());
		}
		if(StringUtils.isNotBlank(synVolume.getEmail())) {
			platUser = new PlatUser();
			platUser.setMail(synVolume.getEmail());
		}
		List<Coin> coinList = coinService.findList(new Coin());
		PlatUser queryPlat = null ;
		List<UserCoinVolume> userCoinVolumes = new ArrayList<>()  ;
		if(platUser!=null) {
			List<PlatUser> platUsers = platUserService.findOne(platUser);
			if(!CollectionUtils.isEmpty(platUsers)) {
				queryPlat = platUsers.get(0) ;
				final PlatUser selectPlatUser = queryPlat ;
				if(StringUtils.isNotBlank(synVolume.getCoinId())) {
					userCoinVolumes = coinList.stream().filter(coin->coin.getId().equals(synVolume.getCoinId())).map(coin->{
						UserCoinVolume ucv = new UserCoinVolume();
						ucv.setCoinId(coin.getId());
						ucv.setCoinSymbol(coin.getName());
						ucv.setUserId(selectPlatUser.getId());
						UserCoinVolume userCoinVolume = userCoinVolumeService.getByUserIdAndCoinId(selectPlatUser.getId(), coin.getId());
						if(userCoinVolume!=null) {
							ucv.setVolume(userCoinVolume.getVolume().setScale(8,BigDecimal.ROUND_HALF_UP));
							ucv.setLockVolume(userCoinVolume.getLockVolume().setScale(8,BigDecimal.ROUND_HALF_UP));
						}
						String haskKey = "user:coin:volume:"+ucv.getUserId() ;
						Object object = JedisUtils.getUserCoinVolume(haskKey, ucv.getCoinSymbol());
						if(object!=null) {
							Map<String,Object> mapData = (Map<String,Object>)object ;
							if(mapData.get("volume")!=null) {
								Object redisVolume = mapData.get("volume") ;
							    BigDecimal bi = new BigDecimal(String.valueOf(((List)redisVolume).get(1))).setScale(8,BigDecimal.ROUND_HALF_UP);
								ucv.setRedisVolume(String.valueOf(bi));
							}
							if(mapData.get("lockVolume")!=null) {
								Object redisLockVolume = mapData.get("lockVolume") ;
								BigDecimal bi = new BigDecimal(String.valueOf(((List)redisLockVolume).get(1))).setScale(8,BigDecimal.ROUND_HALF_UP);
								ucv.setRedisLockVolume(String.valueOf(bi));
							}
						}
						return ucv ;
					}).collect(Collectors.toList());
				}else {
					userCoinVolumes = coinList.stream().map(coin->{
						UserCoinVolume ucv = new UserCoinVolume();
						ucv.setCoinId(coin.getId());
						ucv.setCoinSymbol(coin.getName());
						ucv.setUserId(selectPlatUser.getId());
						UserCoinVolume userCoinVolume = userCoinVolumeService.getByUserIdAndCoinId(selectPlatUser.getId(), coin.getId());
						if(userCoinVolume!=null) {
							ucv.setVolume(userCoinVolume.getVolume().setScale(8,BigDecimal.ROUND_HALF_UP));
							ucv.setLockVolume(userCoinVolume.getLockVolume().setScale(8,BigDecimal.ROUND_HALF_UP));
						}
						String haskKey = "user:coin:volume:"+ucv.getUserId() ;
						Object object = JedisUtils.getUserCoinVolume(haskKey, ucv.getCoinSymbol());
						if(object!=null) {
							Map<String,Object> mapData = (Map<String,Object>)object ;
							if(mapData.get("volume")!=null) {
								Object redisVolume = mapData.get("volume") ;
							    BigDecimal bi = new BigDecimal(String.valueOf(((List)redisVolume).get(1))).setScale(8,BigDecimal.ROUND_HALF_UP);
								ucv.setRedisVolume(String.valueOf(bi));
							}
							if(mapData.get("lockVolume")!=null) {
								Object redisLockVolume = mapData.get("lockVolume") ;
								BigDecimal bi = new BigDecimal(String.valueOf(((List)redisLockVolume).get(1))).setScale(8,BigDecimal.ROUND_HALF_UP);
								ucv.setRedisLockVolume(String.valueOf(bi));
							}
						}
						return ucv ;
					}).collect(Collectors.toList());
				}
			}
		}
		model.addAttribute("queryPlat", queryPlat);
		model.addAttribute("userCoinVolumes", userCoinVolumes);
		model.addAttribute("synVolume", synVolume);
		model.addAttribute(coinList);
		return "modules/plat/synVolumeList";
	}
	
	
	@RequiresPermissions("plat:synVolume:edit")
	@RequestMapping(value = "save")
	public String save(SynVolume synVolume, RedirectAttributes redirectAttributes,HttpServletResponse response) {
		String haskKey = "user:coin:volume:"+synVolume.getUserId() ;
		if(StringUtils.isNotBlank(synVolume.getLockVolume())&&StringUtils.isNotBlank(synVolume.getVolume())) {
			String redisUserVolumeValue = JedisUtils.getRedisUserVolume(haskKey, synVolume.getSymbol(),"volume");
			String redisUserLockVolumeValue = JedisUtils.getRedisUserVolume(haskKey, synVolume.getSymbol(),"lockVolume");
			BigDecimal bigDecimal = new BigDecimal(synVolume.getVolume());
			BigDecimal lockBigDecimal = new BigDecimal(synVolume.getLockVolume());
			JsPlatUserCoinVolumeBill jsPlatUserCoinVolumeBill = new JsPlatUserCoinVolumeBill();
			jsPlatUserCoinVolumeBill.setCoinSymbol(synVolume.getSymbol());
			jsPlatUserCoinVolumeBill.setCreateDate(new Date());
			jsPlatUserCoinVolumeBill.setMark("redis资产和锁定同步");
			BigDecimal synUserVolume = new BigDecimal(synVolume.getVolume());
			if(StringUtils.isNotEmpty(redisUserVolumeValue)) {
				synUserVolume = bigDecimal.subtract(new BigDecimal(redisUserVolumeValue));
			}
			UserCoinVolumeEventEnum opSign =  null  ;
			if(synUserVolume.compareTo(new BigDecimal("0"))>=0) {
				opSign = UserCoinVolumeEventEnum.ADD_VOLUME ;
			}else {
				opSign = UserCoinVolumeEventEnum.SUB_VOLUME ;
			}
			BigDecimal synUserLockVolume = new BigDecimal(synVolume.getLockVolume());
			if(StringUtils.isNotEmpty(redisUserLockVolumeValue)) {
				synUserLockVolume = lockBigDecimal.subtract(new BigDecimal(redisUserLockVolumeValue));
			}
			UserCoinVolumeEventEnum opSignLock =  null  ;
			if(synUserLockVolume.compareTo(new BigDecimal("0"))>=0) {
				opSignLock = UserCoinVolumeEventEnum.ADD_LOCKVOLUME;
			}else {
				opSignLock = UserCoinVolumeEventEnum.SUB_LOCKVOLUME ;
			}
			jsPlatUserCoinVolumeBill.setOpSign(UserCoinVolumeEventEnum.parseEvent(opSign,opSignLock));
			jsPlatUserCoinVolumeBill.setOpVolume(synUserVolume.abs());
			jsPlatUserCoinVolumeBill.setOpLockVolume(synUserLockVolume.abs());
			jsPlatUserCoinVolumeBill.setPriority("100");
			jsPlatUserCoinVolumeBill.setRefKey(IdGen.uuid());
			jsPlatUserCoinVolumeBill.setSource("boss synVolume");
			jsPlatUserCoinVolumeBill.setStatus(UserCoinVolumeBillStatusEnum.UNPROCESSED.getStatus());
			jsPlatUserCoinVolumeBill.setHash(hashSelect.select(HashSelect.createKey(synVolume.getUserId(), synVolume.getSymbol())));
			User user = new User();
			user.setId(synVolume.getUserId());
			jsPlatUserCoinVolumeBill.setUser(user);
			jsPlatUserCoinVolumeBill.setForceLock(1);
			jsPlatUserCoinVolumeBill.setUpdateDate(new Date());
			jsPlatUserCoinVolumeBillService.save(jsPlatUserCoinVolumeBill);
			logger.info("==========synVolume redis同步用户userId:{},symbol:{},volumn:{},lockVolumn:{},计算后的volume:{},lockVolumn:{},资产 billId:{}",synVolume.getUserId(),synVolume.getSymbol(),
					synVolume.getVolume(),synVolume.getLockVolume(),synUserVolume,synUserLockVolume,jsPlatUserCoinVolumeBill.getId());
		}else if(StringUtils.isNotBlank(synVolume.getLockVolume())) {
			String redisUserLockVolumeValue = JedisUtils.getRedisUserVolume(haskKey, synVolume.getSymbol(),"lockVolume");
			BigDecimal bigDecimal = new BigDecimal(synVolume.getLockVolume());
			JsPlatUserCoinVolumeBill jsPlatUserCoinVolumeBill = new JsPlatUserCoinVolumeBill();
			jsPlatUserCoinVolumeBill.setCoinSymbol(synVolume.getSymbol());
			jsPlatUserCoinVolumeBill.setCreateDate(new Date());
			jsPlatUserCoinVolumeBill.setMark("redis锁定资产同步");
			BigDecimal synUserLockVolume = new BigDecimal(synVolume.getLockVolume());
			if(StringUtils.isNotEmpty(redisUserLockVolumeValue)) {
				synUserLockVolume = bigDecimal.subtract(new BigDecimal(redisUserLockVolumeValue));
			}
			if(synUserLockVolume.compareTo(new BigDecimal("0"))>=0) {
				jsPlatUserCoinVolumeBill.setOpSign(UserCoinVolumeEventEnum.ADD_LOCKVOLUME.getEvent());
			}else {
				jsPlatUserCoinVolumeBill.setOpSign(UserCoinVolumeEventEnum.SUB_LOCKVOLUME.getEvent());
			}
			jsPlatUserCoinVolumeBill.setOpLockVolume(synUserLockVolume.abs());
			jsPlatUserCoinVolumeBill.setOpVolume(BigDecimal.ZERO);
			jsPlatUserCoinVolumeBill.setPriority("100");
			jsPlatUserCoinVolumeBill.setRefKey(IdGen.uuid());
			jsPlatUserCoinVolumeBill.setSource("boss synVolume");
			jsPlatUserCoinVolumeBill.setStatus(UserCoinVolumeBillStatusEnum.UNPROCESSED.getStatus());
			jsPlatUserCoinVolumeBill.setHash(hashSelect.select(HashSelect.createKey(synVolume.getUserId(), synVolume.getSymbol())));
			User user = new User();
			user.setId(synVolume.getUserId());
			jsPlatUserCoinVolumeBill.setUser(user);
			jsPlatUserCoinVolumeBill.setUpdateDate(new Date());
			jsPlatUserCoinVolumeBill.setForceLock(1);
			jsPlatUserCoinVolumeBillService.save(jsPlatUserCoinVolumeBill);
			logger.info("==========synVolume redis同步用户userId:{},symbol:{},lockVolumn:{},计算后的lockVolumn:{},资产 billId:{}",synVolume.getUserId(),synVolume.getSymbol(),
					synVolume.getLockVolume(),synUserLockVolume,jsPlatUserCoinVolumeBill.getId());
		}else if(StringUtils.isNotBlank(synVolume.getVolume())) {
			String redisUserVolumeValue = JedisUtils.getRedisUserVolume(haskKey, synVolume.getSymbol(),"volume");
			BigDecimal bigDecimal = new BigDecimal(synVolume.getVolume());
			JsPlatUserCoinVolumeBill jsPlatUserCoinVolumeBill = new JsPlatUserCoinVolumeBill();
			jsPlatUserCoinVolumeBill.setCoinSymbol(synVolume.getSymbol());
			jsPlatUserCoinVolumeBill.setCreateDate(new Date());
			jsPlatUserCoinVolumeBill.setMark("redis资产同步");
			BigDecimal synUserVolume = new BigDecimal(synVolume.getVolume());
			if(StringUtils.isNotEmpty(redisUserVolumeValue)) {
				synUserVolume = bigDecimal.subtract(new BigDecimal(redisUserVolumeValue));
			}
			if(synUserVolume.compareTo(new BigDecimal("0"))>=0) {
				jsPlatUserCoinVolumeBill.setOpSign(UserCoinVolumeEventEnum.ADD_VOLUME.getEvent());
			}else {
				jsPlatUserCoinVolumeBill.setOpSign(UserCoinVolumeEventEnum.SUB_VOLUME.getEvent());
			}
			jsPlatUserCoinVolumeBill.setOpVolume(synUserVolume.abs());
			jsPlatUserCoinVolumeBill.setPriority("100");
			jsPlatUserCoinVolumeBill.setRefKey(IdGen.uuid());
			jsPlatUserCoinVolumeBill.setSource("boss synVolume");
			jsPlatUserCoinVolumeBill.setStatus(UserCoinVolumeBillStatusEnum.UNPROCESSED.getStatus());
			jsPlatUserCoinVolumeBill.setHash(hashSelect.select(HashSelect.createKey(synVolume.getUserId(), synVolume.getSymbol())));
			User user = new User();
			user.setId(synVolume.getUserId());
			jsPlatUserCoinVolumeBill.setUser(user);
			jsPlatUserCoinVolumeBill.setForceLock(1);
			jsPlatUserCoinVolumeBill.setUpdateDate(new Date());
			jsPlatUserCoinVolumeBillService.save(jsPlatUserCoinVolumeBill);
			logger.info("==========synVolume redis同步用户userId:{},symbol:{},volumn:{},计算后的volume:{},资产 billId:{}",synVolume.getUserId(),synVolume.getSymbol(),
					synVolume.getVolume(),synUserVolume,jsPlatUserCoinVolumeBill.getId());
		}
		renderString(response, "{\"success\": \"1\", \"msg\" : \"成功\"}");
		addMessage(redirectAttributes, "处理成功");
		return null;
	}
}
