/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.web;

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
import com.thinkgem.jeesite.modules.market.entity.MkRelayPrizeConfig;
import com.thinkgem.jeesite.modules.market.service.MkRelayPrizeConfigService;

import java.util.List;

/**
 * 接力撞奖配置Controller
 * @author zzj
 * @version 2018-08-31
 */
@Controller
@RequestMapping(value = "${adminPath}/market/mkRelayPrizeConfig")
public class MkRelayPrizeConfigController extends BaseController {

	@Autowired
	private MkRelayPrizeConfigService mkRelayPrizeConfigService;

	@Autowired
	private CoinService coinService;
	
	@ModelAttribute
	public MkRelayPrizeConfig get(@RequestParam(required=false) String id) {
		MkRelayPrizeConfig entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = mkRelayPrizeConfigService.get(id);
		}
		if (entity == null){
			entity = new MkRelayPrizeConfig();
		}
		return entity;
	}
	
	@RequiresPermissions("market:mkRelayPrizeConfig:view")
	@RequestMapping(value = {"list", ""})
	public String list(MkRelayPrizeConfig mkRelayPrizeConfig, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<MkRelayPrizeConfig> page = mkRelayPrizeConfigService.findPage(new Page<MkRelayPrizeConfig>(request, response), mkRelayPrizeConfig); 
		model.addAttribute("page", page);
		return "modules/market/mkRelayPrizeConfigList";
	}

	@RequiresPermissions("market:mkRelayPrizeConfig:view")
	@RequestMapping(value = "form")
	public String form(MkRelayPrizeConfig mkRelayPrizeConfig, Model model) {
		model.addAttribute("mkRelayPrizeConfig", mkRelayPrizeConfig);
		List<Coin> coinList = coinService.findList(new Coin());
		model.addAttribute(coinList);
		return "modules/market/mkRelayPrizeConfigForm";
	}

	@RequiresPermissions("market:mkRelayPrizeConfig:edit")
	@RequestMapping(value = "save")
	public String save(MkRelayPrizeConfig mkRelayPrizeConfig, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, mkRelayPrizeConfig)){
			return form(mkRelayPrizeConfig, model);
		}
		mkRelayPrizeConfigService.save(mkRelayPrizeConfig);
		addMessage(redirectAttributes, "保存接力撞奖配置成功");
		return "redirect:"+Global.getAdminPath()+"/market/mkRelayPrizeConfig/?repage";
	}
	
	@RequiresPermissions("market:mkRelayPrizeConfig:edit")
	@RequestMapping(value = "delete")
	public String delete(MkRelayPrizeConfig mkRelayPrizeConfig, RedirectAttributes redirectAttributes) {
		mkRelayPrizeConfigService.delete(mkRelayPrizeConfig);
		addMessage(redirectAttributes, "删除接力撞奖配置成功");
		return "redirect:"+Global.getAdminPath()+"/market/mkRelayPrizeConfig/?repage";
	}

	@RequiresPermissions("market:mkRelayPrizeConfig:edit")
	@RequestMapping(value = "stop")
	public String stop(MkRelayPrizeConfig mkRelayPrizeConfig, RedirectAttributes redirectAttributes) {
		mkRelayPrizeConfigService.stop(mkRelayPrizeConfig);
		addMessage(redirectAttributes, "终止接力撞奖配置成功");
		return "redirect:"+Global.getAdminPath()+"/market/mkRelayPrizeConfig/?repage";
	}

}