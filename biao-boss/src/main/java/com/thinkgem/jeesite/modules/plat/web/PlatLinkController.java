/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
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
import com.thinkgem.jeesite.modules.plat.entity.PlatLink;
import com.thinkgem.jeesite.modules.plat.service.PlatLinkService;

import java.util.Optional;

/**
 * 平台链接Controller
 * @author dongfeng
 * @version 2018-07-03
 */
@Controller
@RequestMapping(value = "${adminPath}/plat/platLink")
public class PlatLinkController extends BaseController {

	@Autowired
	private PlatLinkService platLinkService;
	
	@ModelAttribute
	public PlatLink get(@RequestParam(required=false) String id) {
		PlatLink entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = platLinkService.get(id);
		}
		if (entity == null){
			entity = new PlatLink();
		}
		return entity;
	}
	
	@RequiresPermissions("plat:platLink:view")
	@RequestMapping(value = {"list", ""})
	public String list(PlatLink platLink, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<PlatLink> page = platLinkService.findPage(new Page<PlatLink>(request, response), platLink); 
		model.addAttribute("page", page);
		return "modules/plat/platLinkList";
	}

	@RequiresPermissions("plat:platLink:view")
	@RequestMapping(value = "form")
	public String form(PlatLink platLink, Model model) {
		model.addAttribute("platLink", platLink);
		return "modules/plat/platLinkForm";
	}

	@RequiresPermissions("plat:platLink:edit")
	@RequestMapping(value = "save")
	public String save(PlatLink platLink, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, platLink)){
			return form(platLink, model);
		}
		String userId = UserUtils.getUser().getLoginName();
		platLink.setCreateby(userId);
		String image = platLink.getLinkimage();
		if (image != null && image.startsWith("|")) {
			image = image.substring(1);
		}
		platLink.setLinkimage(image);
		platLinkService.save(platLink);
		addMessage(redirectAttributes, "保存平台链接成功");
		return "redirect:"+Global.getAdminPath()+"/plat/platLink/?repage";
	}
	
	@RequiresPermissions("plat:platLink:edit")
	@RequestMapping(value = "delete")
	public String delete(PlatLink platLink, RedirectAttributes redirectAttributes) {
		platLinkService.delete(platLink);
		addMessage(redirectAttributes, "删除平台链接成功");
		return "redirect:"+Global.getAdminPath()+"/plat/platLink/?repage";
	}

}