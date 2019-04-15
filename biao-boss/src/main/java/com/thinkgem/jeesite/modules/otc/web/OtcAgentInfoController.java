/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.otc.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.thinkgem.jeesite.modules.otc.entity.OtcAgentUser;
import com.thinkgem.jeesite.modules.otc.service.OtcAgentUserService;
import com.thinkgem.jeesite.modules.plat.entity.Coin;
import com.thinkgem.jeesite.modules.plat.entity.OfflineCoin;
import com.thinkgem.jeesite.modules.plat.entity.PlatUser;
import com.thinkgem.jeesite.modules.plat.service.CoinService;
import com.thinkgem.jeesite.modules.plat.service.OfflineCoinService;
import com.thinkgem.jeesite.modules.sys.dao.UserDao;
import com.thinkgem.jeesite.modules.sys.entity.User;
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
import com.thinkgem.jeesite.modules.otc.entity.OtcAgentInfo;
import com.thinkgem.jeesite.modules.otc.service.OtcAgentInfoService;

import java.util.ArrayList;
import java.util.List;

/**
 * 银商列表Controller
 * @author zzj
 * @version 2018-09-17
 */
@Controller
@RequestMapping(value = "${adminPath}/otc/otcAgentInfo")
public class OtcAgentInfoController extends BaseController {

	@Autowired
	private OtcAgentInfoService otcAgentInfoService;

	@Autowired
	private CoinService coinService;

	@Autowired
	private OfflineCoinService offlineCoinService;

	@Autowired
	private OtcAgentUserService otcAgentUserService;

	@Autowired
	private UserDao userDao;

	@ModelAttribute
	public OtcAgentInfo get(@RequestParam(required=false) String id) {
		OtcAgentInfo entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = otcAgentInfoService.get(id);
		}
		if (entity == null){
			entity = new OtcAgentInfo();
		}
		return entity;
	}
	
	@RequiresPermissions("otc:otcAgentInfo:view")
	@RequestMapping(value = {"list", ""})
	public String list(OtcAgentInfo otcAgentInfo, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<OtcAgentInfo> page = otcAgentInfoService.findPage(new Page<OtcAgentInfo>(request, response), otcAgentInfo);
		model.addAttribute("page", page);
		return "modules/otc/otcAgentInfoList";
	}

	@RequiresPermissions("otc:otcAgentInfo:view")
	@RequestMapping(value = "form")
	public String form(OtcAgentInfo otcAgentInfo, Model model) {

		List<OfflineCoin> offlineCoinList = offlineCoinService.findList(new OfflineCoin());
		model.addAttribute(offlineCoinList);

		List<OtcAgentUser> otcAgentUserList = new ArrayList<>();
		if(!otcAgentInfo.getIsNewRecord()){
			OtcAgentUser otcAgentUser = new OtcAgentUser();
			otcAgentUser.setAgentId(otcAgentInfo.getId());
			otcAgentUserList = otcAgentUserService.findList(otcAgentUser);
		}
		model.addAttribute(otcAgentUserList);
		model.addAttribute("otcAgentInfo", otcAgentInfo);
		return "modules/otc/otcAgentInfoForm";
	}

	@RequiresPermissions("otc:otcAgentInfo:edit")
	@RequestMapping(value = "save")
	public String save(OtcAgentInfo otcAgentInfo, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, otcAgentInfo)){
			return form(otcAgentInfo, model);
		}

		if(otcAgentInfo.getUserIds() != null && otcAgentInfo.getUserIds().length > 0){
			for (int i = 0; i < otcAgentInfo.getUserIds().length; i++){
				OtcAgentUser otcAgentUser = otcAgentUserService.getOneByUserId(otcAgentInfo.getUserIds()[i]);
				if(!ObjectUtils.isEmpty(otcAgentUser)){
					if(otcAgentInfo.getIsNewRecord() || !otcAgentInfo.getId().equals(otcAgentUser.getAgentId())){
						List<String> list = new ArrayList<>();
						list.add(0, "数据验证失败：");
						list.add(1,String.format("会员[%s]在银商[%s]记录已添加", otcAgentUser.getUserId(), otcAgentUser.getAgentId()));
						addMessage(model, list.toArray(new String[]{}));
						return form(otcAgentInfo, model);
					}
				}
			}
		}

		OtcAgentInfo otcAgentInfoTmp = otcAgentInfoService.getBySysUserName(otcAgentInfo.getSysUserName());
		if(!ObjectUtils.isEmpty(otcAgentInfoTmp)){
			if(otcAgentInfo.getIsNewRecord() || !otcAgentInfo.getId().equals(otcAgentInfoTmp.getId())){
				List<String> list = new ArrayList<>();
				list.add(0, "数据验证失败：");
				list.add(1,String.format("银商登录名不能重复！"));
				addMessage(model, list.toArray(new String[]{}));
				return form(otcAgentInfo, model);
			}
		}

		OtcAgentInfo otcAgentInfoTmp2 = otcAgentInfoService.getByName(otcAgentInfo.getName());
		if(!ObjectUtils.isEmpty(otcAgentInfoTmp2)){
			if(otcAgentInfo.getIsNewRecord() || !otcAgentInfo.getId().equals(otcAgentInfoTmp2.getId())){
				List<String> list = new ArrayList<>();
				list.add(0, "数据验证失败：");
				list.add(1,String.format("银商名称不能重复！"));
				addMessage(model, list.toArray(new String[]{}));
				return form(otcAgentInfo, model);
			}
		}

		if(otcAgentInfo.getIsNewRecord()){
			User user = new User();
			user.setLoginName(otcAgentInfo.getSysUserName());
			user = userDao.getByLoginName(user);

			if(!ObjectUtils.isEmpty(user)){
				List<String> list = new ArrayList<>();
				list.add(0, "数据验证失败：");
				list.add(1,String.format("银商登录名在系统中已存在！"));
				addMessage(model, list.toArray(new String[]{}));
				return form(otcAgentInfo, model);
			}
		}

		otcAgentInfoService.save(otcAgentInfo);
		addMessage(redirectAttributes, "保存银商列表成功");
		return "redirect:"+Global.getAdminPath()+"/otc/otcAgentInfo/?repage";
	}
	
	@RequiresPermissions("otc:otcAgentInfo:edit")
	@RequestMapping(value = "delete")
	public String delete(OtcAgentInfo otcAgentInfo, RedirectAttributes redirectAttributes) {
		otcAgentInfoService.delete(otcAgentInfo);
		addMessage(redirectAttributes, "删除银商列表成功");
		return "redirect:"+Global.getAdminPath()+"/otc/otcAgentInfo/?repage";
	}

}