/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.thinkgem.jeesite.modules.plat.entity.ExPair;
import com.thinkgem.jeesite.modules.plat.service.ExPairService;
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
import com.thinkgem.jeesite.modules.market.entity.MkSysUserExPair;
import com.thinkgem.jeesite.modules.market.service.MkSysUserExPairService;

import java.util.ArrayList;
import java.util.List;

/**
 * 营销用户币币对Controller
 * @author zzj
 * @version 2018-08-23
 */
@Controller
@RequestMapping(value = "${adminPath}/market/mkSysUserExPair")
public class MkSysUserExPairController extends BaseController {

	@Autowired
	private MkSysUserExPairService mkSysUserExPairService;

	@Autowired
	private ExPairService exPairService;
	
	@ModelAttribute
	public MkSysUserExPair get(@RequestParam(required=false) String id) {
		MkSysUserExPair entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = mkSysUserExPairService.get(id);
		}
		if (entity == null){
			entity = new MkSysUserExPair();
		}
		return entity;
	}
	
	@RequiresPermissions("market:mkSysUserExPair:view")
	@RequestMapping(value = {"list", ""})
	public String list(MkSysUserExPair mkSysUserExPair, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<MkSysUserExPair> page = mkSysUserExPairService.findPage(new Page<MkSysUserExPair>(request, response), mkSysUserExPair); 
		model.addAttribute("page", page);
		return "modules/market/mkSysUserExPairList";
	}

	@RequiresPermissions("market:mkSysUserExPair:view")
	@RequestMapping(value = "form")
	public String form(MkSysUserExPair mkSysUserExPair, Model model) {
		model.addAttribute("mkSysUserExPair", mkSysUserExPair);
		ExPair exPair = new ExPair();
		exPair.setStatus("1");
		List<ExPair> exPairList = exPairService.findList(exPair);
		model.addAttribute(exPairList);
		return "modules/market/mkSysUserExPairForm";
	}

	@RequiresPermissions("market:mkSysUserExPair:edit")
	@RequestMapping(value = "save")
	public String save(MkSysUserExPair mkSysUserExPair, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, mkSysUserExPair)){
			return form(mkSysUserExPair, model);
		}

		MkSysUserExPair userExPair = mkSysUserExPairService.getByUserAndExPair(mkSysUserExPair);
		if(!ObjectUtils.isEmpty(userExPair)){
			if(mkSysUserExPair.getIsNewRecord() || !mkSysUserExPair.getId().equals(userExPair.getId())){
				List<String> list = new ArrayList<>();
				list.add(0, "数据验证失败：");
				list.add(1,"运营用户重复配置前端账户与币币对！");
				addMessage(model, list.toArray(new String[]{}));
				return form(mkSysUserExPair, model);
			}
		}

		mkSysUserExPairService.save(mkSysUserExPair);
		addMessage(redirectAttributes, "保存营销用户币币对成功");
		return "redirect:"+Global.getAdminPath()+"/market/mkSysUserExPair/?repage";
	}
	
	@RequiresPermissions("market:mkSysUserExPair:edit")
	@RequestMapping(value = "delete")
	public String delete(MkSysUserExPair mkSysUserExPair, RedirectAttributes redirectAttributes) {
		mkSysUserExPairService.delete(mkSysUserExPair);
		addMessage(redirectAttributes, "删除营销用户币币对成功");
		return "redirect:"+Global.getAdminPath()+"/market/mkSysUserExPair/?repage";
	}

}