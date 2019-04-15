/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.web;

import java.util.Date;
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
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.plat.entity.Coin;
import com.thinkgem.jeesite.modules.plat.entity.JsPlatUserAirdrop;
import com.thinkgem.jeesite.modules.plat.service.CoinService;
import com.thinkgem.jeesite.modules.plat.service.JsPlatUserAirdropService;

/**
 * 用户空头币种Controller
 * @author ruoyu
 * @version 2018-11-15
 */
@Controller
@RequestMapping(value = "${adminPath}/plat/jsPlatUserAirdrop")
public class JsPlatUserAirdropController extends BaseController {

	@Autowired
	private JsPlatUserAirdropService jsPlatUserAirdropService;
	
	@Autowired
	private CoinService coinService;
	
	@ModelAttribute
	public JsPlatUserAirdrop get(@RequestParam(required=false) String id) {
		JsPlatUserAirdrop entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = jsPlatUserAirdropService.get(id);
		}
		if (entity == null){
			entity = new JsPlatUserAirdrop();
		}
		return entity;
	}
	
	@RequiresPermissions("plat:jsPlatUserAirdrop:view")
	@RequestMapping(value = {"list", ""})
	public String list(JsPlatUserAirdrop jsPlatUserAirdrop, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<JsPlatUserAirdrop> page = jsPlatUserAirdropService.findPage(new Page<JsPlatUserAirdrop>(request, response), jsPlatUserAirdrop); 
		model.addAttribute("page", page);
		List<Coin> coinList = coinService.findList(new Coin());
		model.addAttribute(coinList);
		return "modules/plat/jsPlatUserAirdropList";
	}

	@RequiresPermissions("plat:jsPlatUserAirdrop:view")
	@RequestMapping(value = "form")
	public String form(JsPlatUserAirdrop jsPlatUserAirdrop, Model model) {
		model.addAttribute("jsPlatUserAirdrop", jsPlatUserAirdrop);
		List<Coin> coinList = coinService.findList(new Coin());
		model.addAttribute(coinList);
		return "modules/plat/jsPlatUserAirdropForm";
	}

	@RequiresPermissions("plat:jsPlatUserAirdrop:edit")
	@RequestMapping(value = "save")
	public String save(JsPlatUserAirdrop jsPlatUserAirdrop, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, jsPlatUserAirdrop)){
			return form(jsPlatUserAirdrop, model);
		}
		Coin coin = coinService.get(jsPlatUserAirdrop.getCoinId());
		jsPlatUserAirdrop.setCoinSymbol(coin.getName());
		jsPlatUserAirdrop.setStatus("0");
		jsPlatUserAirdrop.setCreateTime(new Date());
		jsPlatUserAirdropService.save(jsPlatUserAirdrop);
		addMessage(redirectAttributes, "保存用户空头币种成功");
		return "redirect:"+Global.getAdminPath()+"/plat/jsPlatUserAirdrop/?repage";
	}
	
	@RequiresPermissions("plat:jsPlatUserAirdrop:edit")
	@RequestMapping(value = "delete")
	public String delete(JsPlatUserAirdrop jsPlatUserAirdrop, RedirectAttributes redirectAttributes) {
		jsPlatUserAirdropService.delete(jsPlatUserAirdrop);
		addMessage(redirectAttributes, "删除用户空头币种成功");
		return "redirect:"+Global.getAdminPath()+"/plat/jsPlatUserAirdrop/?repage";
	}

}