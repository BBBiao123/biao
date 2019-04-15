/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.thinkgem.jeesite.common.service.ServiceException;
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
import com.thinkgem.jeesite.modules.plat.entity.OfflineOrder;
import com.thinkgem.jeesite.modules.plat.service.OfflineOrderService;

/**
 * c2c广告Controller
 * @author dazi
 * @version 2018-04-27
 */
@Controller
@RequestMapping(value = "${adminPath}/plat/offlineOrder")
public class OfflineOrderController extends BaseController {

	@Autowired
	private OfflineOrderService offlineOrderService;
	
	@ModelAttribute
	public OfflineOrder get(@RequestParam(required=false) String id) {
		OfflineOrder entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = offlineOrderService.get(id);
		}
		if (entity == null){
			entity = new OfflineOrder();
		}
		return entity;
	}
	
	@RequiresPermissions("plat:offlineOrder:view")
	@RequestMapping(value = {"list", ""})
	public String list(OfflineOrder offlineOrder, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<OfflineOrder> page = offlineOrderService.findPage(new Page<OfflineOrder>(request, response), offlineOrder); 
		model.addAttribute("page", page);
		return "modules/plat/offlineOrderList";
	}

	@RequiresPermissions("plat:offlineOrder:view")
	@RequestMapping(value = "form")
	public String form(OfflineOrder offlineOrder, Model model) {
		model.addAttribute("offlineOrder", offlineOrder);
		return "modules/plat/offlineOrderForm";
	}

	@RequiresPermissions("plat:offlineOrder:edit")
	@RequestMapping(value = "save")
	public String save(OfflineOrder offlineOrder, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, offlineOrder)){
			return form(offlineOrder, model);
		}
		offlineOrderService.save(offlineOrder);
		addMessage(redirectAttributes, "保存c2c广告成功");
		return "redirect:"+Global.getAdminPath()+"/plat/offlineOrder/?repage";
	}

	@RequiresPermissions("plat:offlineOrder:edit")
	@RequestMapping(value = "batchAction")
	public String batchAction(Model model) {
		return "modules/plat/offlineOrderAction";
	}

	@RequiresPermissions("plat:offlineOrder:edit")
	@RequestMapping(value = "doBatchCancel")
	public String doBatchCancel(String coinSymbol, String exType, Model model, RedirectAttributes redirectAttributes) {
		String result = offlineOrderService.doBatchCancel(coinSymbol, exType);
		addMessage(model, result);
		return "modules/plat/offlineOrderAction";
	}

	@RequiresPermissions("plat:offlineOrder:edit")
	@RequestMapping(value = "doCancel")
	public String doCancel(String id, Model model, RedirectAttributes redirectAttributes) {

		try {
			offlineOrderService.doCancel(id);
			addMessage(redirectAttributes, "撤销广告成功,广告ID：" + id);
		} catch (ServiceException e) {
			logger.error("撤销广告失败,广告ID" + id, e);
			addMessage(redirectAttributes, "撤销广告失败,广告ID" + id + "=>" + e.getMessage());
		} catch (Exception e) {
            logger.error("撤销广告失败,广告ID" + id, e);
			addMessage(redirectAttributes, "参数错误，撤销广告失败！广告ID" + id);
		}
		return "redirect:"+Global.getAdminPath()+"/plat/offlineOrder/?repage";
	}

	@RequiresPermissions("plat:offlineOrder:edit")
	@RequestMapping(value = "delete")
	public String delete(OfflineOrder offlineOrder, RedirectAttributes redirectAttributes) {
		offlineOrderService.delete(offlineOrder);
		addMessage(redirectAttributes, "删除c2c广告成功");
		return "redirect:"+Global.getAdminPath()+"/plat/offlineOrder/?repage";
	}

}