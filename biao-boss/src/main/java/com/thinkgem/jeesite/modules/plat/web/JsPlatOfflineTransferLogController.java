/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.thinkgem.jeesite.modules.plat.entity.Coin;
import com.thinkgem.jeesite.modules.plat.entity.JsPlatOfflineTransferLog;
import com.thinkgem.jeesite.modules.plat.entity.PlatUser;
import com.thinkgem.jeesite.modules.plat.service.CoinService;
import com.thinkgem.jeesite.modules.plat.service.JsPlatOfflineTransferLogService;
import com.thinkgem.jeesite.modules.plat.service.PlatUserService;

/**
 * bb to c2c转账日志Controller
 * @author ruoyu
 * @version 2018-08-28
 */
@Controller
@RequestMapping(value = "${adminPath}/plat/jsPlatOfflineTransferLog")
public class JsPlatOfflineTransferLogController extends BaseController {

	@Autowired
	private JsPlatOfflineTransferLogService jsPlatOfflineTransferLogService;
	
	@Autowired
	private CoinService coinService;
	@Autowired
	private PlatUserService platUserService;
	
	@ModelAttribute
	public JsPlatOfflineTransferLog get(@RequestParam(required=false) String id) {
		JsPlatOfflineTransferLog entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = jsPlatOfflineTransferLogService.get(id);
		}
		if (entity == null){
			entity = new JsPlatOfflineTransferLog();
		}
		return entity;
	}
	
	@RequiresPermissions("plat:jsPlatOfflineTransferLog:view")
	@RequestMapping(value = {"list", ""})
	public String list(JsPlatOfflineTransferLog jsPlatOfflineTransferLog, HttpServletRequest request, HttpServletResponse response, Model model) {
		if(jsPlatOfflineTransferLog.getUser()!=null&&org.apache.commons.lang3.StringUtils.isNotBlank(jsPlatOfflineTransferLog.getUser().getName())) {
			PlatUser platUser = new PlatUser();
			platUser.setMobile(jsPlatOfflineTransferLog.getUser().getName());
			List<PlatUser> platUsers = platUserService.findList(platUser);
			if(CollectionUtils.isEmpty(platUsers)) {
				platUser = new PlatUser();
				platUser.setMail(jsPlatOfflineTransferLog.getUser().getName());
				platUsers = platUserService.findList(platUser);
			}
			if(!CollectionUtils.isEmpty(platUsers)&&platUsers.size()==1) {
				jsPlatOfflineTransferLog.getUser().setId(platUsers.get(0).getId());
			}
		}
		Page<JsPlatOfflineTransferLog> page = jsPlatOfflineTransferLogService.findPage(new Page<JsPlatOfflineTransferLog>(request, response), jsPlatOfflineTransferLog); 
		model.addAttribute("page", page);
		List<Coin> coinList = coinService.findList(new Coin());
		model.addAttribute(coinList);
		return "modules/plat/jsPlatOfflineTransferLogList";
	}

	@RequiresPermissions("plat:jsPlatOfflineTransferLog:view")
	@RequestMapping(value = "form")
	public String form(JsPlatOfflineTransferLog jsPlatOfflineTransferLog, Model model) {
		model.addAttribute("jsPlatOfflineTransferLog", jsPlatOfflineTransferLog);
		return "modules/plat/jsPlatOfflineTransferLogForm";
	}

	@RequiresPermissions("plat:jsPlatOfflineTransferLog:edit")
	@RequestMapping(value = "save")
	public String save(JsPlatOfflineTransferLog jsPlatOfflineTransferLog, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, jsPlatOfflineTransferLog)){
			return form(jsPlatOfflineTransferLog, model);
		}
		jsPlatOfflineTransferLogService.save(jsPlatOfflineTransferLog);
		addMessage(redirectAttributes, "保存bb to c2c转账日志成功");
		return "redirect:"+Global.getAdminPath()+"/plat/jsPlatOfflineTransferLog/?repage";
	}
	
	@RequiresPermissions("plat:jsPlatOfflineTransferLog:edit")
	@RequestMapping(value = "delete")
	public String delete(JsPlatOfflineTransferLog jsPlatOfflineTransferLog, RedirectAttributes redirectAttributes) {
		jsPlatOfflineTransferLogService.delete(jsPlatOfflineTransferLog);
		addMessage(redirectAttributes, "删除bb to c2c转账日志成功");
		return "redirect:"+Global.getAdminPath()+"/plat/jsPlatOfflineTransferLog/?repage";
	}

}