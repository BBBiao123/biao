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
import com.thinkgem.jeesite.modules.plat.entity.JsPlatMainCnb;
import com.thinkgem.jeesite.modules.plat.service.JsPlatMainCnbService;

import java.util.List;

/**
 * 主区币兑人民币汇率Controller
 * @author dongfeng
 * @version 2018-08-21
 */
@Controller
@RequestMapping(value = "${adminPath}/plat/jsPlatMainCnb")
public class JsPlatMainCnbController extends BaseController {

	@Autowired
	private CoinService coinService;

	@Autowired
	private JsPlatMainCnbService jsPlatMainCnbService;
	
	@ModelAttribute
	public JsPlatMainCnb get(@RequestParam(required=false) String id) {
		JsPlatMainCnb entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = jsPlatMainCnbService.get(id);
		}
		if (entity == null){
			entity = new JsPlatMainCnb();
		}
		return entity;
	}
	
	@RequiresPermissions("plat:jsPlatMainCnb:view")
	@RequestMapping(value = {"list", ""})
	public String list(JsPlatMainCnb jsPlatMainCnb, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<JsPlatMainCnb> page = jsPlatMainCnbService.findPage(new Page<JsPlatMainCnb>(request, response), jsPlatMainCnb); 
		model.addAttribute("page", page);
		List<Coin> coinList = coinService.findList(new Coin());
		model.addAttribute(coinList);
		return "modules/plat/jsPlatMainCnbList";
	}

	@RequiresPermissions("plat:jsPlatMainCnb:view")
	@RequestMapping(value = "form")
	public String form(JsPlatMainCnb jsPlatMainCnb, Model model) {
		model.addAttribute("jsPlatMainCnb", jsPlatMainCnb);
		List<Coin> coinList = coinService.findList(new Coin());
		model.addAttribute(coinList);
		return "modules/plat/jsPlatMainCnbForm";
	}

	@RequiresPermissions("plat:jsPlatMainCnb:edit")
	@RequestMapping(value = "save")
	public String save(JsPlatMainCnb jsPlatMainCnb, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, jsPlatMainCnb)){
			return form(jsPlatMainCnb, model);
		}
		jsPlatMainCnbService.save(jsPlatMainCnb);
		addMessage(redirectAttributes, "保存主区币兑人民币汇率成功");
		return "redirect:"+Global.getAdminPath()+"/plat/jsPlatMainCnb/?repage";
	}
	
	@RequiresPermissions("plat:jsPlatMainCnb:edit")
	@RequestMapping(value = "delete")
	public String delete(JsPlatMainCnb jsPlatMainCnb, RedirectAttributes redirectAttributes) {
		jsPlatMainCnbService.delete(jsPlatMainCnb);
		addMessage(redirectAttributes, "删除主区币兑人民币汇率成功");
		return "redirect:"+Global.getAdminPath()+"/plat/jsPlatMainCnb/?repage";
	}

}