/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.web;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.plat.constant.UserCoinVolumeEventEnum;
import com.thinkgem.jeesite.modules.plat.entity.Coin;
import com.thinkgem.jeesite.modules.plat.entity.JsPlatUserCoinVolumeBill;
import com.thinkgem.jeesite.modules.plat.service.CoinService;
import com.thinkgem.jeesite.modules.plat.service.JsPlatUserCoinVolumeBillService;

/**
 * 用户资产账单Controller
 * @author ruoyu
 * @version 2019-01-10
 */
@Controller
@RequestMapping(value = "${adminPath}/plat/jsPlatUserCoinVolumeBill")
public class JsPlatUserCoinVolumeBillController extends BaseController {

	@Autowired
	private JsPlatUserCoinVolumeBillService jsPlatUserCoinVolumeBillService;
	
	@Autowired
	private CoinService coinService;
	
	@ModelAttribute
	public JsPlatUserCoinVolumeBill get(@RequestParam(required=false) String id) {
		JsPlatUserCoinVolumeBill entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = jsPlatUserCoinVolumeBillService.get(id);
		}
		if (entity == null){
			entity = new JsPlatUserCoinVolumeBill();
		}
		return entity;
	}
	
	@RequiresPermissions("plat:jsPlatUserCoinVolumeBill:view")
	@RequestMapping(value = {"list", ""})
	public String list(JsPlatUserCoinVolumeBill jsPlatUserCoinVolumeBill, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<JsPlatUserCoinVolumeBill> page = jsPlatUserCoinVolumeBillService.findPage(new Page<JsPlatUserCoinVolumeBill>(request, response), jsPlatUserCoinVolumeBill); 
		if(page!=null&&page.getList()!=null) {
			page.getList().stream().forEach(ucvb->{
				String opSignText = UserCoinVolumeEventEnum.getEventText(ucvb.getOpSign()) ;
				ucvb.setOpSignText(opSignText);
				if(ucvb.getOpLockVolume()!=null) {
					ucvb.setOpLockVolume(ucvb.getOpLockVolume().setScale(8,BigDecimal.ROUND_HALF_UP));
				}
				if(ucvb.getOpVolume()!=null) {
					ucvb.setOpVolume(ucvb.getOpVolume().setScale(8,BigDecimal.ROUND_HALF_UP));
				}
			});
		}
		model.addAttribute("page", page);
		List<Coin> coinList = coinService.findList(new Coin());
		model.addAttribute(coinList);
		List<Coin> staticList = new ArrayList<>();
		staticList.add(new Coin("0", "未处理"));
		staticList.add(new Coin("1", "处理中"));
		staticList.add(new Coin("2", "成功处理"));
		staticList.add(new Coin("3", "处理失败"));
		staticList.add(new Coin("4", "需要重试"));
		model.addAttribute("staticList",staticList);
		return "modules/plat/jsPlatUserCoinVolumeBillList";
	}

	@RequiresPermissions("plat:jsPlatUserCoinVolumeBill:view")
	@RequestMapping(value = "form")
	public String form(JsPlatUserCoinVolumeBill jsPlatUserCoinVolumeBill, Model model) {
		model.addAttribute("jsPlatUserCoinVolumeBill", jsPlatUserCoinVolumeBill);
		return "modules/plat/jsPlatUserCoinVolumeBillForm";
	}

	@RequiresPermissions("plat:jsPlatUserCoinVolumeBill:edit")
	@RequestMapping(value = "save")
	public String save(JsPlatUserCoinVolumeBill jsPlatUserCoinVolumeBill, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, jsPlatUserCoinVolumeBill)){
			return form(jsPlatUserCoinVolumeBill, model);
		}
		jsPlatUserCoinVolumeBillService.save(jsPlatUserCoinVolumeBill);
		addMessage(redirectAttributes, "保存用户资产账单成功");
		return "redirect:"+Global.getAdminPath()+"/plat/jsPlatUserCoinVolumeBill/?repage";
	}
	
	@RequiresPermissions("plat:jsPlatUserCoinVolumeBill:edit")
	@RequestMapping(value = "delete")
	public String delete(JsPlatUserCoinVolumeBill jsPlatUserCoinVolumeBill, RedirectAttributes redirectAttributes) {
		jsPlatUserCoinVolumeBillService.delete(jsPlatUserCoinVolumeBill);
		addMessage(redirectAttributes, "删除用户资产账单成功");
		return "redirect:"+Global.getAdminPath()+"/plat/jsPlatUserCoinVolumeBill/?repage";
	}

}