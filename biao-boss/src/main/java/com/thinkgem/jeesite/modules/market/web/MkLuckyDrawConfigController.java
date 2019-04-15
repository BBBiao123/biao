/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.thinkgem.jeesite.modules.plat.entity.Coin;
import com.thinkgem.jeesite.modules.plat.service.CoinService;
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

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.market.entity.MkLuckyDrawConfig;
import com.thinkgem.jeesite.modules.market.service.MkLuckyDrawConfigService;

import java.util.List;

/**
 * 抽奖活动规则Controller
 * @author zzj
 * @version 2018-11-01
 */
@Controller
@RequestMapping(value = "${adminPath}/market/mkLuckyDrawConfig")
public class MkLuckyDrawConfigController extends BaseController {

	@Autowired
	private MkLuckyDrawConfigService mkLuckyDrawConfigService;

	@Autowired
	private CoinService coinService;
	
	@ModelAttribute
	public MkLuckyDrawConfig get(@RequestParam(required=false) String id) {
		MkLuckyDrawConfig entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = mkLuckyDrawConfigService.get(id);
		}
		if (entity == null){
			entity = new MkLuckyDrawConfig();
		}
		return entity;
	}
	
	@RequiresPermissions("market:mkLuckyDrawConfig:view")
	@RequestMapping(value = {"list", ""})
	public String list(MkLuckyDrawConfig mkLuckyDrawConfig, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<MkLuckyDrawConfig> page = mkLuckyDrawConfigService.findPage(new Page<MkLuckyDrawConfig>(request, response), mkLuckyDrawConfig); 
		model.addAttribute("page", page);
		return "modules/market/mkLuckyDrawConfigList";
	}

	@RequiresPermissions("market:mkLuckyDrawConfig:view")
	@RequestMapping(value = "form")
	public String form(MkLuckyDrawConfig mkLuckyDrawConfig, Model model) {
		model.addAttribute("mkLuckyDrawConfig", mkLuckyDrawConfig);
		List<Coin> coinList = coinService.findList(new Coin());
		model.addAttribute(coinList);
		return "modules/market/mkLuckyDrawConfigForm";
	}

	@RequiresPermissions("market:mkLuckyDrawConfig:edit")
	@RequestMapping(value = "save")
	public String save(MkLuckyDrawConfig mkLuckyDrawConfig, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, mkLuckyDrawConfig)){
			return form(mkLuckyDrawConfig, model);
		}
		if(mkLuckyDrawConfig.getIsNewRecord()){
			mkLuckyDrawConfig.setStatus("0");
			mkLuckyDrawConfig.setPeriods(0);
			mkLuckyDrawConfig.setCreateBy(UserUtils.getUser());
		}
		mkLuckyDrawConfig.setUpdateBy(UserUtils.getUser());
		mkLuckyDrawConfigService.save(mkLuckyDrawConfig);
		addMessage(redirectAttributes, "保存抽奖活动规则成功");
		return "redirect:"+Global.getAdminPath()+"/market/mkLuckyDrawConfig/?repage";
	}
	
	@RequiresPermissions("market:mkLuckyDrawConfig:edit")
	@RequestMapping(value = "delete")
	public String delete(MkLuckyDrawConfig mkLuckyDrawConfig, RedirectAttributes redirectAttributes) {
		mkLuckyDrawConfigService.delete(mkLuckyDrawConfig);
		addMessage(redirectAttributes, "删除抽奖活动规则成功");
		return "redirect:"+Global.getAdminPath()+"/market/mkLuckyDrawConfig/?repage";
	}

	@RequiresPermissions("market:mkLuckyDrawConfig:edit")
	@RequestMapping(value = "start")
	public String start(MkLuckyDrawConfig mkLuckyDrawConfig, RedirectAttributes redirectAttributes) {
		mkLuckyDrawConfig.setPeriods(mkLuckyDrawConfig.getPeriods() + 1);
		if(ObjectUtils.isEmpty(mkLuckyDrawConfig.getGrantVolume())){
			mkLuckyDrawConfig.setGrantVolume(Double.valueOf("0"));
		}
		mkLuckyDrawConfig.setPoolVolume(mkLuckyDrawConfig.getStartVolume());
		Double remainVolume = mkLuckyDrawConfig.getVolume() - mkLuckyDrawConfig.getGrantVolume();
		if(mkLuckyDrawConfig.getPoolVolume() > remainVolume){
			mkLuckyDrawConfig.setPoolVolume(remainVolume);
		}
		mkLuckyDrawConfig.setFee(Double.valueOf("0.00"));
		mkLuckyDrawConfig.setPlayerNumber(0);
		mkLuckyDrawConfig.setStatus("1");
		mkLuckyDrawConfig.setUpdateBy(UserUtils.getUser());
		mkLuckyDrawConfigService.save(mkLuckyDrawConfig);
		addMessage(redirectAttributes, "开启抽奖活动规则成功");
		return "redirect:"+Global.getAdminPath()+"/market/mkLuckyDrawConfig/?repage";
	}

	@RequiresPermissions("market:mkLuckyDrawConfig:edit")
	@RequestMapping(value = "end")
	public String end(MkLuckyDrawConfig mkLuckyDrawConfig, RedirectAttributes redirectAttributes) {
		mkLuckyDrawConfig.setStatus("2");
		mkLuckyDrawConfig.setUpdateBy(UserUtils.getUser());
		mkLuckyDrawConfigService.save(mkLuckyDrawConfig);
		addMessage(redirectAttributes, "结束抽奖活动规则成功");
		return "redirect:"+Global.getAdminPath()+"/market/mkLuckyDrawConfig/?repage";
	}

	@RequiresPermissions("market:mkLuckyDrawConfig:edit")
	@RequestMapping(value = "draw")
	public String draw(MkLuckyDrawConfig mkLuckyDrawConfig, RedirectAttributes redirectAttributes) {
		mkLuckyDrawConfig.setStatus("3");
		mkLuckyDrawConfig.setUpdateBy(UserUtils.getUser());
		mkLuckyDrawConfigService.draw(mkLuckyDrawConfig);
		addMessage(redirectAttributes, "开奖成功！");
		return "redirect:"+Global.getAdminPath()+"/market/mkLuckyDrawConfig/?repage";
	}

}