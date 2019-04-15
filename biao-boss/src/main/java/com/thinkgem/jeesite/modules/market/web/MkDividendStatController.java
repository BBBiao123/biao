/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.web;

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
import com.thinkgem.jeesite.modules.market.entity.MkDividendStat;
import com.thinkgem.jeesite.modules.market.service.MkDividendStatService;

/**
 * 分红统计Controller
 * @author zhangzijun
 * @version 2018-08-02
 */
@Controller
@RequestMapping(value = "${adminPath}/market/mkDividendStat")
public class MkDividendStatController extends BaseController {

	@Autowired
	private MkDividendStatService mkDividendStatService;
	
	@ModelAttribute
	public MkDividendStat get(@RequestParam(required=false) String id) {
		MkDividendStat entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = mkDividendStatService.get(id);
		}
		if (entity == null){
			entity = new MkDividendStat();
		}
		return entity;
	}
	
	@RequiresPermissions("market:mkDividendStat:view")
	@RequestMapping(value = {"list", ""})
	public String list(MkDividendStat mkDividendStat, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<MkDividendStat> page = mkDividendStatService.findPage(new Page<MkDividendStat>(request, response), mkDividendStat); 
		model.addAttribute("page", page);
		return "modules/market/mkDividendStatList";
	}

	@RequiresPermissions("market:mkDividendStat:view")
	@RequestMapping(value = "form")
	public String form(MkDividendStat mkDividendStat, Model model) {
		model.addAttribute("mkDividendStat", mkDividendStat);
		return "modules/market/mkDividendStatForm";
	}

	@RequiresPermissions("market:mkDividendStat:edit")
	@RequestMapping(value = "save")
	public String save(MkDividendStat mkDividendStat, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, mkDividendStat)){
			return form(mkDividendStat, model);
		}
		mkDividendStatService.save(mkDividendStat);
		addMessage(redirectAttributes, "保存分红统计成功");
		return "redirect:"+Global.getAdminPath()+"/market/mkDividendStat/?repage";
	}
	
	@RequiresPermissions("market:mkDividendStat:edit")
	@RequestMapping(value = "delete")
	public String delete(MkDividendStat mkDividendStat, RedirectAttributes redirectAttributes) {
		mkDividendStatService.delete(mkDividendStat);
		addMessage(redirectAttributes, "删除分红统计成功");
		return "redirect:"+Global.getAdminPath()+"/market/mkDividendStat/?repage";
	}

}