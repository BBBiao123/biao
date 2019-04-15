/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.otc.web;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.otc.entity.OtcAgentApplyUser;
import com.thinkgem.jeesite.modules.otc.entity.OtcAgentInfo;
import com.thinkgem.jeesite.modules.otc.entity.OtcAgentRemitApply;
import com.thinkgem.jeesite.modules.otc.service.OtcAgentApplyUserService;
import com.thinkgem.jeesite.modules.otc.service.OtcAgentInfoService;
import com.thinkgem.jeesite.modules.otc.service.OtcAgentRemitApplyService;
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
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 银商拨币申请列表Controller
 * @author zzj
 * @version 2018-09-18
 */
@Controller
@RequestMapping(value = "${adminPath}/otc/otcAgentRemitAudit")
public class OtcAgentRemitAuditController extends BaseController {

	@Autowired
	private OtcAgentRemitApplyService otcAgentRemitApplyService;

	@Autowired
	private OtcAgentInfoService otcAgentInfoService;

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
	
	@RequiresPermissions("otc:otcAgentRemitAudit:view")
	@RequestMapping(value = {"list", ""})
	public String list(OtcAgentRemitApply otcAgentRemitApply, HttpServletRequest request, HttpServletResponse response, Model model) {

		Page<OtcAgentRemitApply> page = otcAgentRemitApplyService.findPage(new Page<OtcAgentRemitApply>(request, response), otcAgentRemitApply);
        model.addAttribute("page", page);

		List<OfflineCoin> offlineCoinList = offlineCoinService.findList(new OfflineCoin());
		model.addAttribute(offlineCoinList);
        return "modules/otc/otcAgentRemitAuditList";
	}

	@RequiresPermissions("otc:otcAgentRemitAudit:view")
	@RequestMapping(value = "form")
	public String form(OtcAgentRemitApply otcAgentRemitApply, Model model) {

		if("0".equals(otcAgentRemitApply.getStatus())){
			otcAgentRemitApply.setRemitVolume(otcAgentRemitApply.getApplyVolume());
		}

		OtcAgentApplyUser otcAgentApplyUser = new OtcAgentApplyUser();
		otcAgentApplyUser.setApplyId(otcAgentRemitApply.getId());
		List<OtcAgentApplyUser> otcAgentApplyUserList = otcAgentApplyUserService.findList(otcAgentApplyUser);
		if(!"3".equals(otcAgentRemitApply.getStatus())){
			for (OtcAgentApplyUser applyUser: otcAgentApplyUserList) {
				Double volume = (otcAgentRemitApply.getRemitVolume() * Double.valueOf(applyUser.getPercentage()) ) / (Double.valueOf("100"));
				BigDecimal bg = new BigDecimal(volume);
				volume = bg.setScale(6, BigDecimal.ROUND_HALF_DOWN).doubleValue();
				applyUser.setVolume(volume);
			}
		}


		model.addAttribute(otcAgentApplyUserList);
		model.addAttribute("otcAgentRemitApply", otcAgentRemitApply);
		return "modules/otc/otcAgentRemitAuditForm";
	}

	@RequiresPermissions("otc:otcAgentRemitAudit:edit")
	@RequestMapping(value = "save")
	public String save(OtcAgentRemitApply otcAgentRemitApply, Model model, RedirectAttributes redirectAttributes) {

		if (!beanValidator(model, otcAgentRemitApply)){
			return form(otcAgentRemitApply, model);
		}

		User user = UserUtils.getUser();
		otcAgentRemitApply.setUpdateBy(user);
		otcAgentRemitApply.setUpdateByName(user.getLoginName());
		otcAgentRemitApply.setStatus("0");
		otcAgentRemitApplyService.save(otcAgentRemitApply);
		addMessage(redirectAttributes, "保存银商拨币申请成功");
		return "redirect:"+Global.getAdminPath()+"/otc/otcAgentRemitApply/?repage";
	}

	@RequiresPermissions("otc:otcAgentRemitAudit:edit")
	@RequestMapping(value = "cancel")
	public String cancel(OtcAgentRemitApply otcAgentRemitApply,  Model model,RedirectAttributes redirectAttributes) {

		OtcAgentRemitApply curApply = otcAgentRemitApplyService.get(otcAgentRemitApply.getId());
		if(!ObjectUtils.isEmpty(curApply) && !"0".equals(curApply.getStatus())){
			List<String> list = new ArrayList<>();
			list.add(0, "数据验证失败：");
			list.add(1,String.format("拨币申请已不在审核中，不能终止！"));
			addMessage(model, list.toArray(new String[]{}));
			return form(otcAgentRemitApply, model);
		}

		curApply.setStatus("3"); //取消
		curApply.setFinanceAuditComment(otcAgentRemitApply.getFinanceAuditComment());
		otcAgentRemitApplyService.cancel(curApply);
		addMessage(redirectAttributes, "取消银商拨币申请成功");
		return "redirect:"+Global.getAdminPath()+"/otc/otcAgentRemitAudit/?repage";
	}

	@RequiresPermissions("otc:otcAgentRemitAudit:edit")
	@RequestMapping(value = "audit")
	public String audit(OtcAgentRemitApply otcAgentRemitApply,  Model model, RedirectAttributes redirectAttributes) {

		OtcAgentRemitApply curApply = otcAgentRemitApplyService.get(otcAgentRemitApply.getId());
		if(!ObjectUtils.isEmpty(curApply) && !"0".equals(curApply.getStatus())){
			List<String> list = new ArrayList<>();
			list.add(0, "数据验证失败：");
			list.add(1,String.format("拨币申请已不在审核中，不能财务审核！"));
			addMessage(model, list.toArray(new String[]{}));
			return form(otcAgentRemitApply, model);
		}

		curApply.setStatus("1"); //已到账
		curApply.setUsdtRate(otcAgentRemitApply.getUsdtRate());

		Double remitVolume ;
		if("0".equals(curApply.getPayCoinType())){
			remitVolume = curApply.getVolume() / (curApply.getTradeCoinRate() * curApply.getDiscount());
		}else{
			remitVolume = (curApply.getVolume() * curApply.getUsdtRate()) / (curApply.getTradeCoinRate() * curApply.getDiscount());
		}

		BigDecimal bg = new BigDecimal(remitVolume);
		remitVolume = bg.setScale(6, BigDecimal.ROUND_HALF_DOWN).doubleValue();

		curApply.setRemitVolume(remitVolume);
		curApply.setFinanceAuditComment(otcAgentRemitApply.getFinanceAuditComment());
		otcAgentRemitApplyService.audit(curApply);
		addMessage(redirectAttributes, "财务审核银商拨币申请成功");
		return "redirect:"+Global.getAdminPath()+"/otc/otcAgentRemitAudit/?repage";
	}

	@RequiresPermissions("otc:otcAgentRemitCoin:edit")
	@RequestMapping(value = "remit")
	public String remit(OtcAgentRemitApply otcAgentRemitApply,  Model model, RedirectAttributes redirectAttributes) {

		OtcAgentRemitApply curApply = otcAgentRemitApplyService.get(otcAgentRemitApply.getId());
		if(!ObjectUtils.isEmpty(curApply) && !"1".equals(curApply.getStatus())){
			List<String> list = new ArrayList<>();
			list.add(0, "数据验证失败：");
			list.add(1,String.format("拨币申请已不在已到账状态，不能拨币！"));
			addMessage(model, list.toArray(new String[]{}));
			return form(otcAgentRemitApply, model);
		}

		curApply.setStatus("2"); //已拨币
		curApply.setMarketAuditComment(otcAgentRemitApply.getMarketAuditComment());
		otcAgentRemitApplyService.remit(curApply);
		addMessage(redirectAttributes, "运营审核银商拨币成功");
		return "redirect:"+Global.getAdminPath()+"/otc/otcAgentRemitAudit/?repage";
	}

}