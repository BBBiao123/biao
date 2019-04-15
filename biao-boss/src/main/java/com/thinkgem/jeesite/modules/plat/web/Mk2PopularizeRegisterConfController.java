/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.thinkgem.jeesite.modules.plat.entity.Coin;
import com.thinkgem.jeesite.modules.plat.service.CoinService;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.plat.entity.Mk2PopularizeRegisterConf;
import com.thinkgem.jeesite.modules.plat.service.Mk2PopularizeRegisterConfService;

import java.util.List;

/**
 * 注册送币规则Controller
 * @author dongfeng
 * @version 2018-07-20
 */
@Controller
@RequestMapping(value = "${adminPath}/plat/mk2PopularizeRegisterConf")
public class Mk2PopularizeRegisterConfController extends BaseController {

	@Autowired
	private Mk2PopularizeRegisterConfService mk2PopularizeRegisterConfService;

	@Autowired
	private CoinService coinService;
	
	@ModelAttribute
	public Mk2PopularizeRegisterConf get(@RequestParam(required=false) String id) {
		Mk2PopularizeRegisterConf entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = mk2PopularizeRegisterConfService.get(id);
		}
		if (entity == null){
			entity = new Mk2PopularizeRegisterConf();
		}
		return entity;
	}
	
	@RequiresPermissions("plat:mk2PopularizeRegisterConf:view")
	@RequestMapping(value = {"list", ""})
	public String list(Mk2PopularizeRegisterConf mk2PopularizeRegisterConf, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Mk2PopularizeRegisterConf> page = mk2PopularizeRegisterConfService.findPage(new Page<Mk2PopularizeRegisterConf>(request, response), mk2PopularizeRegisterConf); 
		model.addAttribute("page", page);
		return "modules/plat/mk2PopularizeRegisterConfList";
	}

	@RequiresPermissions("plat:mk2PopularizeRegisterConf:view")
	@RequestMapping(value = "form")
	public String form(Mk2PopularizeRegisterConf mk2PopularizeRegisterConf, Model model) {
		List<Coin> coinList = coinService.findList(new Coin());
		model.addAttribute(coinList);
		model.addAttribute("mk2PopularizeRegisterConf", mk2PopularizeRegisterConf);
		return "modules/plat/mk2PopularizeRegisterConfForm";
	}

	@RequiresPermissions("plat:mk2PopularizeRegisterConf:edit")
	@RequestMapping(value = "save")
	public String save(Mk2PopularizeRegisterConf mk2PopularizeRegisterConf, Model model, RedirectAttributes redirectAttributes) {

		mk2PopularizeRegisterConf.setUpdateBy(UserUtils.getUser());
		if (!beanValidator(model, mk2PopularizeRegisterConf)){
			return form(mk2PopularizeRegisterConf, model);
		}
		List<Mk2PopularizeRegisterConf> confs =  mk2PopularizeRegisterConfService.findEffective();
		if (CollectionUtils.isEmpty(confs)
				|| (confs.size() == 1 && confs.get(0).getId().equals(mk2PopularizeRegisterConf.getId()))
				|| !"3".equals(mk2PopularizeRegisterConf.getStatus())) {
			mk2PopularizeRegisterConfService.save(mk2PopularizeRegisterConf);
			addMessage(redirectAttributes, "保存注册送币规则成功");
		} else {
			addMessage(redirectAttributes, "目前有正在使用中的注册送币规则，若重新定义规则，请先弃用使用中的送币规则！");
		}
		return "redirect:"+Global.getAdminPath()+"/plat/mk2PopularizeRegisterConf/?repage";
	}
	
	@RequiresPermissions("plat:mk2PopularizeRegisterConf:edit")
	@RequestMapping(value = "delete")
	public String delete(Mk2PopularizeRegisterConf mk2PopularizeRegisterConf, RedirectAttributes redirectAttributes) {
		mk2PopularizeRegisterConfService.delete(mk2PopularizeRegisterConf);
		addMessage(redirectAttributes, "删除注册送币规则成功");
		return "redirect:"+Global.getAdminPath()+"/plat/mk2PopularizeRegisterConf/?repage";
	}

}