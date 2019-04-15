/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.web;

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
import com.thinkgem.jeesite.modules.plat.entity.JsPlatUserOplog;
import com.thinkgem.jeesite.modules.plat.service.JsPlatUserOplogService;

/**
 * 会员操作日志Controller
 * @author zzj
 * @version 2018-11-07
 */
@Controller
@RequestMapping(value = "${adminPath}/plat/jsPlatUserOplog")
public class JsPlatUserOplogController extends BaseController {

	@Autowired
	private JsPlatUserOplogService jsPlatUserOplogService;
	
	@ModelAttribute
	public JsPlatUserOplog get(@RequestParam(required=false) String id) {
		JsPlatUserOplog entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = jsPlatUserOplogService.get(id);
		}
		if (entity == null){
			entity = new JsPlatUserOplog();
		}
		return entity;
	}
	
	@RequiresPermissions("plat:jsPlatUserOplog:view")
	@RequestMapping(value = {"list", ""})
	public String list(JsPlatUserOplog jsPlatUserOplog, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<JsPlatUserOplog> page = jsPlatUserOplogService.findPage(new Page<JsPlatUserOplog>(request, response), jsPlatUserOplog); 
		model.addAttribute("page", page);
		return "modules/plat/jsPlatUserOplogList";
	}

	@RequiresPermissions("plat:jsPlatUserOplog:view")
	@RequestMapping(value = "form")
	public String form(JsPlatUserOplog jsPlatUserOplog, Model model) {
		model.addAttribute("jsPlatUserOplog", jsPlatUserOplog);
		return "modules/plat/jsPlatUserOplogForm";
	}

	@RequiresPermissions("plat:jsPlatUserOplog:edit")
	@RequestMapping(value = "save")
	public String save(JsPlatUserOplog jsPlatUserOplog, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, jsPlatUserOplog)){
			return form(jsPlatUserOplog, model);
		}
		jsPlatUserOplogService.save(jsPlatUserOplog);
		addMessage(redirectAttributes, "保存会员操作日志成功");
		return "redirect:"+Global.getAdminPath()+"/plat/jsPlatUserOplog/?repage";
	}
	
	@RequiresPermissions("plat:jsPlatUserOplog:edit")
	@RequestMapping(value = "delete")
	public String delete(JsPlatUserOplog jsPlatUserOplog, RedirectAttributes redirectAttributes) {
		jsPlatUserOplogService.delete(jsPlatUserOplog);
		addMessage(redirectAttributes, "删除会员操作日志成功");
		return "redirect:"+Global.getAdminPath()+"/plat/jsPlatUserOplog/?repage";
	}

}