/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.otc.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.thinkgem.jeesite.modules.otc.entity.OtcAgentApplyUser;
import com.thinkgem.jeesite.modules.otc.entity.OtcAgentInfo;
import com.thinkgem.jeesite.modules.otc.entity.OtcAgentUser;
import com.thinkgem.jeesite.modules.otc.service.OtcAgentApplyUserService;
import com.thinkgem.jeesite.modules.otc.service.OtcAgentInfoService;
import com.thinkgem.jeesite.modules.otc.service.OtcAgentUserService;
import com.thinkgem.jeesite.modules.plat.entity.Coin;
import com.thinkgem.jeesite.modules.plat.entity.OfflineCoin;
import com.thinkgem.jeesite.modules.plat.service.CoinService;
import com.thinkgem.jeesite.modules.plat.service.OfflineCoinService;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.otc.entity.OtcAgentRemitApply;
import com.thinkgem.jeesite.modules.otc.service.OtcAgentRemitApplyService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 银商拨币申请列表Controller
 * @author zzj
 * @version 2018-09-18
 */
@Controller
@RequestMapping(value = "${adminPath}/otc/otcAgentRemitApply")
public class OtcAgentRemitApplyController extends BaseController {

	@Autowired
	private OtcAgentRemitApplyService otcAgentRemitApplyService;

	@Autowired
	private OtcAgentInfoService otcAgentInfoService;

	@Autowired
	private OtcAgentUserService otcAgentUserService;

	@Autowired
	private OtcAgentApplyUserService otcAgentApplyUserService;

	@Autowired
	private OfflineCoinService offlineCoinService;

	@Autowired
	private CoinService coinService;
	
	@ModelAttribute
	public OtcAgentRemitApply get(@RequestParam(required=false) String id) {
		OtcAgentRemitApply entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = otcAgentRemitApplyService.get(id);
		}
		if (entity == null){
			entity = new OtcAgentRemitApply();
		}
		return entity;
	}
	
	@RequiresPermissions("otc:otcAgentRemitApply:view")
	@RequestMapping(value = {"list", ""})
	public String list(OtcAgentRemitApply otcAgentRemitApply, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		otcAgentRemitApply.setCreateBy(user);
		Page<OtcAgentRemitApply> page = otcAgentRemitApplyService.findPage(new Page<OtcAgentRemitApply>(request, response), otcAgentRemitApply); 
		model.addAttribute("page", page);

		List<OfflineCoin> offlineCoinList = offlineCoinService.findList(new OfflineCoin());
		model.addAttribute(offlineCoinList);
		return "modules/otc/otcAgentRemitApplyList";
	}

	@RequiresPermissions("otc:otcAgentRemitApply:view")
	@RequestMapping(value = "form")
	public String form(OtcAgentRemitApply otcAgentRemitApply, Model model) {

		List<OtcAgentApplyUser> otcAgentApplyUserList = null;
		if(otcAgentRemitApply.getIsNewRecord()){
			OtcAgentInfo otcAgentInfo = otcAgentInfoService.getBySysUserName(UserUtils.getUser().getLoginName());
			if(ObjectUtils.isEmpty(otcAgentInfo)){
				List<String> list = new ArrayList<>();
				list.add(0, "数据验证提示：");
				list.add(1,String.format("您不是银商，不能新增拨币申请！"));
				addMessage(model, list.toArray(new String[]{}));

				if (CollectionUtils.isEmpty(otcAgentApplyUserList)){
					otcAgentApplyUserList = new ArrayList<>();
				}

				otcAgentRemitApply.setDiscount(Double.valueOf("1"));
				model.addAttribute(otcAgentApplyUserList);
				model.addAttribute("otcAgentRemitApply", otcAgentRemitApply);
				return "modules/otc/otcAgentRemitApplyForm";
			}
			OfflineCoin offlineCoin = offlineCoinService.getByCoinId(otcAgentInfo.getCoinId());
			otcAgentRemitApply.setAgentId(otcAgentInfo.getId());
			otcAgentRemitApply.setAgentName(otcAgentInfo.getName());
			otcAgentRemitApply.setCoinId(otcAgentInfo.getCoinId());
			otcAgentRemitApply.setCoinSymbol(otcAgentInfo.getCoinSymbol());
			otcAgentRemitApply.setDiscount(otcAgentInfo.getDiscount());
			otcAgentRemitApply.setTradeCoinRate(offlineCoin.getMaxPrice().doubleValue());
			OtcAgentUser otcAgentUserTmp = new OtcAgentUser();
			otcAgentUserTmp.setAgentId(otcAgentInfo.getId());

			if(CollectionUtils.isEmpty(otcAgentApplyUserList)){
				otcAgentApplyUserList = new ArrayList<>();
			}
			List<OtcAgentUser> otcAgentUserList = otcAgentUserService.findList(otcAgentUserTmp);
			for (OtcAgentUser otcAgentUser : otcAgentUserList) {
				OtcAgentApplyUser otcAgentApplyUser = new OtcAgentApplyUser();
				otcAgentApplyUser.setUserId(otcAgentUser.getUserId());
				otcAgentApplyUser.setMail(otcAgentUser.getMail());
				otcAgentApplyUser.setMobile(otcAgentUser.getMobile());
				otcAgentApplyUserList.add(otcAgentApplyUser);
			}
		}else{
			OtcAgentApplyUser otcAgentApplyUser = new OtcAgentApplyUser();
			otcAgentApplyUser.setApplyId(otcAgentRemitApply.getId());
			otcAgentApplyUserList = otcAgentApplyUserService.findList(otcAgentApplyUser);

			for (OtcAgentApplyUser applyUser: otcAgentApplyUserList) {
				Double volume = (otcAgentRemitApply.getApplyVolume() * Double.valueOf(applyUser.getPercentage())) / (Double.valueOf("100"));
				BigDecimal bg = new BigDecimal(volume);
				volume = bg.setScale(6, BigDecimal.ROUND_HALF_DOWN).doubleValue();
				applyUser.setVolume(volume);
			}
		}

		if (CollectionUtils.isEmpty(otcAgentApplyUserList)){
			otcAgentApplyUserList = new ArrayList<>();
		}

		model.addAttribute(otcAgentApplyUserList);
		model.addAttribute("otcAgentRemitApply", otcAgentRemitApply);
		return "modules/otc/otcAgentRemitApplyForm";
	}

	@RequiresPermissions("otc:otcAgentRemitApply:edit")
	@RequestMapping(value = "save")
	public String save(OtcAgentRemitApply otcAgentRemitApply, Model model, RedirectAttributes redirectAttributes) {

		if (!beanValidator(model, otcAgentRemitApply)){
			return form(otcAgentRemitApply, model);
		}

		OtcAgentRemitApply curApply = otcAgentRemitApplyService.get(otcAgentRemitApply.getId());
		if(!ObjectUtils.isEmpty(curApply) && !"0".equals(curApply.getStatus())){
			List<String> list = new ArrayList<>();
			list.add(0, "数据验证失败：");
			list.add(1,String.format("拨币申请已不在审核中，不能修改！"));
			addMessage(model, list.toArray(new String[]{}));
			return form(otcAgentRemitApply, model);
		}

		User user = UserUtils.getUser();
		if(otcAgentRemitApply.getIsNewRecord()){
			OtcAgentInfo otcAgentInfo = otcAgentInfoService.getBySysUserName(UserUtils.getUser().getLoginName());
			otcAgentRemitApply.setDiscount(otcAgentInfo.getDiscount());

			OfflineCoin offlineCoin = offlineCoinService.getByCoinId(otcAgentInfo.getCoinId());
			otcAgentRemitApply.setTradeCoinRate(offlineCoin.getMaxPrice().doubleValue());
			otcAgentRemitApply.setCreateBy(user);
			otcAgentRemitApply.setCreateByName(user.getLoginName());
		}else{
			otcAgentRemitApply.setDiscount(curApply.getDiscount());
			otcAgentRemitApply.setTradeCoinRate(curApply.getTradeCoinRate());
		}

		Double applyVolume ;
		if("0".equals(otcAgentRemitApply.getPayCoinType())){
			applyVolume = otcAgentRemitApply.getVolume() / (otcAgentRemitApply.getTradeCoinRate() * otcAgentRemitApply.getDiscount());
		}else{
			applyVolume = (otcAgentRemitApply.getVolume() * otcAgentRemitApply.getUsdtRate()) / (otcAgentRemitApply.getTradeCoinRate() * otcAgentRemitApply.getDiscount());
		}

		BigDecimal bg = new BigDecimal(applyVolume);
		applyVolume = bg.setScale(6, BigDecimal.ROUND_HALF_DOWN).doubleValue();

		otcAgentRemitApply.setApplyVolume(applyVolume);
		otcAgentRemitApply.setUpdateBy(user);
		otcAgentRemitApply.setUpdateByName(user.getLoginName());
		otcAgentRemitApply.setStatus("0");
		otcAgentRemitApplyService.save(otcAgentRemitApply);
		addMessage(redirectAttributes, "保存银商拨币申请成功");
		return "redirect:"+Global.getAdminPath()+"/otc/otcAgentRemitApply/?repage";
	}
	
	@RequiresPermissions("otc:otcAgentRemitApply:edit")
	@RequestMapping(value = "delete")
	public String delete(OtcAgentRemitApply otcAgentRemitApply, RedirectAttributes redirectAttributes) {

		OtcAgentRemitApply curApply = otcAgentRemitApplyService.get(otcAgentRemitApply.getId());
		if(!ObjectUtils.isEmpty(curApply) && !"0".equals(curApply.getStatus())){
			addMessage(redirectAttributes, "拨币申请已不在审核中，不能删除！");
			return "redirect:"+Global.getAdminPath()+"/otc/otcAgentRemitApply/?repage";
		}

		otcAgentRemitApplyService.delete(otcAgentRemitApply);
		addMessage(redirectAttributes, "删除银商拨币申请成功");
		return "redirect:"+Global.getAdminPath()+"/otc/otcAgentRemitApply/?repage";
	}


}