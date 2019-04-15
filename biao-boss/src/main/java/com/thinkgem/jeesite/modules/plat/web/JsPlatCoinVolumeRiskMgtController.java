/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.thinkgem.jeesite.modules.plat.entity.Coin;
import com.thinkgem.jeesite.modules.plat.service.CoinService;
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
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.plat.entity.JsPlatCoinVolumeRiskMgt;
import com.thinkgem.jeesite.modules.plat.service.JsPlatCoinVolumeRiskMgtService;

import java.util.List;

/**
 * 币种资产风控管理Controller
 * @author zzj
 * @version 2019-01-18
 */
@Controller
@RequestMapping(value = "${adminPath}/plat/jsPlatCoinVolumeRiskMgt")
public class JsPlatCoinVolumeRiskMgtController extends BaseController {

	@Autowired
	private JsPlatCoinVolumeRiskMgtService jsPlatCoinVolumeRiskMgtService;

	@Autowired
	private CoinService coinService;
	
	@ModelAttribute
	public JsPlatCoinVolumeRiskMgt get(@RequestParam(required=false) String id) {
		JsPlatCoinVolumeRiskMgt entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = jsPlatCoinVolumeRiskMgtService.get(id);
		}
		if (entity == null){
			entity = new JsPlatCoinVolumeRiskMgt();
		}
		return entity;
	}
	
	@RequiresPermissions("plat:jsPlatCoinVolumeRiskMgt:view")
	@RequestMapping(value = {"list", ""})
	public String list(JsPlatCoinVolumeRiskMgt jsPlatCoinVolumeRiskMgt, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<JsPlatCoinVolumeRiskMgt> page = jsPlatCoinVolumeRiskMgtService.findPage(new Page<JsPlatCoinVolumeRiskMgt>(request, response), jsPlatCoinVolumeRiskMgt); 
		model.addAttribute("page", page);

		List<Coin> coinList = coinService.findList(new Coin());
		model.addAttribute(coinList);
		return "modules/plat/jsPlatCoinVolumeRiskMgtList";
	}

	@RequiresPermissions("plat:jsPlatCoinVolumeRiskMgt:view")
	@RequestMapping(value = "form")
	public String form(JsPlatCoinVolumeRiskMgt jsPlatCoinVolumeRiskMgt, Model model) {
		model.addAttribute("jsPlatCoinVolumeRiskMgt", jsPlatCoinVolumeRiskMgt);

		List<Coin> coinList = coinService.findList(new Coin());
		model.addAttribute(coinList);
		return "modules/plat/jsPlatCoinVolumeRiskMgtForm";
	}

	@RequiresPermissions("plat:jsPlatCoinVolumeRiskMgt:edit")
	@RequestMapping(value = "save")
	public String save(JsPlatCoinVolumeRiskMgt jsPlatCoinVolumeRiskMgt, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, jsPlatCoinVolumeRiskMgt)){
			return form(jsPlatCoinVolumeRiskMgt, model);
		}
		jsPlatCoinVolumeRiskMgtService.save(jsPlatCoinVolumeRiskMgt);
		addMessage(redirectAttributes, "保存币种资产风控管理成功");
		return "redirect:"+Global.getAdminPath()+"/plat/jsPlatCoinVolumeRiskMgt/?repage";
	}
	
	@RequiresPermissions("plat:jsPlatCoinVolumeRiskMgt:edit")
	@RequestMapping(value = "delete")
	public String delete(JsPlatCoinVolumeRiskMgt jsPlatCoinVolumeRiskMgt, RedirectAttributes redirectAttributes) {
		jsPlatCoinVolumeRiskMgtService.delete(jsPlatCoinVolumeRiskMgt);
		addMessage(redirectAttributes, "删除币种资产风控管理成功");
		return "redirect:"+Global.getAdminPath()+"/plat/jsPlatCoinVolumeRiskMgt/?repage";
	}

}