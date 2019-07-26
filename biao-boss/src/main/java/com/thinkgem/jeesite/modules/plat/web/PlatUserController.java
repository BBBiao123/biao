/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.web;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.*;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.plat.dao.CoinDao;
import com.thinkgem.jeesite.modules.plat.dao.Mk2PopularizeRegisterCoinDao;
import com.thinkgem.jeesite.modules.plat.dao.Mk2PopularizeRegisterConfDao;
import com.thinkgem.jeesite.modules.plat.dao.UserCoinVolumeDao;
import com.thinkgem.jeesite.modules.plat.entity.*;
import com.thinkgem.jeesite.modules.plat.enums.MessageTemplateCode;
import com.thinkgem.jeesite.modules.plat.enums.PlatUserOplogTypeEnum;
import com.thinkgem.jeesite.modules.plat.service.*;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 前台用户Controller
 *
 * @author dazi
 * @version 2018-04-26
 */
@Controller
@RequestMapping(value = "${adminPath}/plat/platUser")
public class PlatUserController extends BaseController {

    @Autowired
    private PlatUserService platUserService;

    @Autowired
	private UserCoinVolumeService userCoinVolumeService;

    @Autowired
    private OfflineCancelLogService offlineCancelLogService;

    @Autowired
    private JsPlatUserOplogService jsPlatUserOplogService;

    @Autowired
    private SmsMessageService smsMessageService;

    @Autowired
    private CoinDao coinDao;
    @Autowired
    private Mk2PopularizeRegisterCoinService registerCoinService;

    @Autowired
    private Mk2PopularizeRegisterConfService registerConfService;

    @Value("${image.url}")
    private String imageUrl;

    @ModelAttribute
    public PlatUser get(@RequestParam(required = false) String id) {
        PlatUser entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = platUserService.get(id);
        }
        if (entity == null) {
            entity = new PlatUser();
        }
        return entity;
    }

    @RequiresPermissions("plat:platUser:view")
    @RequestMapping(value = {"list", ""})
    public String list(PlatUser platUser, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<PlatUser> page = platUserService.findPage(new Page<PlatUser>(request, response), platUser);
        model.addAttribute("page", page);
        //设置查询参数
        model.addAttribute("platUser", platUser);
        request.getSession().setAttribute("platUserSearch", platUser);
        return "modules/plat/platUserList";
    }

    @RequiresPermissions("plat:platUser:manager")
    @RequestMapping(value = "manList")
    public String manList(PlatUser platUser, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<PlatUser> page = platUserService.findPage(new Page<PlatUser>(request, response), platUser);
        model.addAttribute("page", page);

        page.getList().forEach(user->{
            if(Objects.nonNull(user.getLockDate()) && this.toLocalDateTime(user.getLockDate()).isAfter(LocalDateTime.now())){
                user.setIsLockTrade("1");
            }else{
                user.setIsLockTrade("0");
            }
        });
        //设置查询参数
        model.addAttribute("platUser", platUser);
        request.getSession().setAttribute("platUserSearch", platUser);
        return "modules/plat/platUserManList";
    }

    @RequiresPermissions("plat:platUser:view")
    @RequestMapping(value = "dialog")
    public String listDialog(PlatUser platUser, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<PlatUser> page = platUserService.findPage(new Page<PlatUser>(request, response), platUser);
        model.addAttribute("page", page);
        //设置查询参数
        model.addAttribute("platUser", platUser);
        return "modules/plat/platUserDialog";
    }

    @RequiresPermissions("plat:platUser:view")
    @RequestMapping(value = "userDialog")
    public String userDialog(PlatUser platUser, HttpServletRequest request, HttpServletResponse response, Model model) {
        PlatUser user = platUserService.get(request.getParameter("userId"));
        model.addAttribute("platUser", user);
        return "modules/plat/userDialog";
    }

    @RequiresPermissions("plat:platUser:view")
    @RequestMapping(value = "form")
    public String form(PlatUser platUser, Model model) {
        model.addAttribute("platUser", platUser);
        model.addAttribute("imageUrl", imageUrl);
        return "modules/plat/platUserForm";
    }

    @RequiresPermissions("plat:platUser:view")
    @RequestMapping(value = "mobileForm")
    public String mobileForm(PlatUser platUser, Model model) {
        model.addAttribute("platUser", platUser);
        model.addAttribute("imageUrl", imageUrl);

        JsPlatUserOplog jsPlatUserOplog = new JsPlatUserOplog();
        jsPlatUserOplog.setType(PlatUserOplogTypeEnum.CHANGE_MOBILE.getCode());
        jsPlatUserOplog.setUserId(platUser.getId());
        List<JsPlatUserOplog> jsPlatUserOplogList = jsPlatUserOplogService.findList(jsPlatUserOplog);
        model.addAttribute("jsPlatUserOplogList", jsPlatUserOplogList);
        return "modules/plat/platUserChangePhoneForm";
    }

    @RequiresPermissions("plat:platUser:view")
    @RequestMapping(value = "tagForm")
    public String tagForm(PlatUser platUser, Model model) {
        model.addAttribute("platUser", platUser);
        model.addAttribute("imageUrl", imageUrl);
        JsPlatUserOplog jsPlatUserOplog = new JsPlatUserOplog();
        jsPlatUserOplog.setType(PlatUserOplogTypeEnum.CHANGE_TAG.getCode());
        jsPlatUserOplog.setUserId(platUser.getId());
        List<JsPlatUserOplog> jsPlatUserOplogList = jsPlatUserOplogService.findList(jsPlatUserOplog);
        model.addAttribute("jsPlatUserOplogList", jsPlatUserOplogList);
        return "modules/plat/platUserChangeTagForm";
    }

    @RequiresPermissions("plat:platUser:view")
    @RequestMapping(value = "lockForm/{type}")
    public String lockForm(PlatUser platUser,@PathVariable("type") String type, Model model) {
        model.addAttribute("platUser", platUser);
        model.addAttribute("imageUrl", imageUrl);

        JsPlatUserOplog jsPlatUserOplog = new JsPlatUserOplog();
        jsPlatUserOplog.setType(type);
        jsPlatUserOplog.setUserId(platUser.getId());
        List<JsPlatUserOplog> jsPlatUserOplogList = jsPlatUserOplogService.findList(jsPlatUserOplog);
        model.addAttribute("jsPlatUserOplogList", jsPlatUserOplogList);
        return "modules/plat/platUserLockForm";
    }

    @RequiresPermissions("plat:platUser:view")
    @RequestMapping(value = "lockDateForm/{type}")
    public String lockDateForm(PlatUser platUser,@PathVariable("type") String type, Model model) {

        if(Objects.nonNull(platUser.getLockDate()) && this.toLocalDateTime(platUser.getLockDate()).isAfter(LocalDateTime.now())){
            platUser.setIsLockTrade("1");
        }else{
            platUser.setIsLockTrade("0");
        }
        model.addAttribute("platUser", platUser);

        JsPlatUserOplog jsPlatUserOplog = new JsPlatUserOplog();
        jsPlatUserOplog.setType(type);
        jsPlatUserOplog.setUserId(platUser.getId());
        List<JsPlatUserOplog> jsPlatUserOplogList = jsPlatUserOplogService.findList(jsPlatUserOplog);
        model.addAttribute("jsPlatUserOplogList", jsPlatUserOplogList);
        return "modules/plat/platUserLockDateForm";
    }

    @RequiresPermissions("plat:platUser:edit")
    @RequestMapping(value = "save")
    public String save(PlatUser platUser, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        if (!beanValidator(model, platUser)) {
            return form(platUser, model);
        }
        PlatUser dbPlatUser = platUserService.get(platUser.getId());
        dbPlatUser.setCardStatus(platUser.getCardStatus());
        dbPlatUser.setCardStatusReason(platUser.getCardStatusReason());
        String content = "审核不通过";
        if ("11".equals(platUser.getCardStatus())) {// 审核通过记录审核时间，用于分销统计用
            dbPlatUser.setAuditDate(TimeUtils.curTimeLocal());
            dbPlatUser.setCardLevel(2);
            content = "审核通过";
            platUserService.giveCoin(dbPlatUser);
        }
        if("19".equals(platUser.getCardStatus()) && dbPlatUser.getCardLevel()!=null && dbPlatUser.getCardLevel() == 2) {
        	//重置身份审核
        	dbPlatUser.setCardLevel(0);
        	dbPlatUser.setCardStatus("0");
        	dbPlatUser.setCardStatusCheckTime(0);
        }
        dbPlatUser.setSource(platUser.getSource());
        platUserService.save(dbPlatUser);

        this.createOplog(dbPlatUser, PlatUserOplogTypeEnum.AUDIT.getCode(), content, platUser.getCardStatusReason());
        addMessage(redirectAttributes, "保存前台用户成功");
        redirectAttributes.addFlashAttribute("platUser", request.getSession().getAttribute("platUserSearch"));
        return "redirect:" + Global.getAdminPath() + "/plat/platUser/?repage";
    }

    @RequiresPermissions("plat:platUser:edit")
    @RequestMapping(value = "lock/{value}")
    public String lock(PlatUser platUser, @PathVariable("value") Integer value, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        PlatUser dbPlatUser = platUserService.get(platUser.getId());
        dbPlatUser.setStatus(value + "");
        platUserService.save(dbPlatUser);
        if(value==9) {
        	//踢出前台登录用户
            this.kickOutPlatUser(platUser);
        }
        if(value==0) {

            this.createOplog(dbPlatUser, PlatUserOplogTypeEnum.LOCK.getCode(), "解锁会员", "");
            //解除踢掉用户
            this.kickOutPlatUser(platUser);
        }
        addMessage(redirectAttributes, "锁定前台用户成功");
        redirectAttributes.addFlashAttribute("platUser", request.getSession().getAttribute("platUserSearch"));
        return "redirect:" + Global.getAdminPath() + "/plat/platUser/?repage";
    }

    @RequiresPermissions("plat:platUser:edit")
    @RequestMapping(value = "update/lock/{value}")
    public String lockPlatUser(PlatUser platUser, @PathVariable("value") Integer value, Model model, RedirectAttributes redirectAttributes, HttpServletResponse response) {

        if(StringUtils.isEmpty(platUser.getReason())){
            renderString(response, "{\"success\": \"0\", \"msg\" : \"原因不能为空\"}");
            return null;
        }

        PlatUser dbPlatUser = platUserService.get(platUser.getId());
        dbPlatUser.setStatus(value + "");
        platUserService.save(dbPlatUser);
        if(value==9) {

            //踢出前台登录用户
            this.kickOutPlatUser(platUser);
            addMessage(redirectAttributes, "锁定会员成功");
            renderString(response, "{\"success\": \"1\", \"msg\" : \"锁定会员成功\"}");
            boolean isSend = false;
            String templateCode = MessageTemplateCode.LOCK_PLAT_USER.getCode();
            try{
                isSend = smsMessageService.sendSms(templateCode, platUser.getMobile());
            }catch (Exception e){
                logger.error("发送短信失败：" + e.getMessage());
            }

//            this.createOplog(dbPlatUser, PlatUserOplogTypeEnum.LOCK.getCode(), "锁定会员", platUser.getReason());
            this.createOplogWithMessage(dbPlatUser, PlatUserOplogTypeEnum.LOCK.getCode(), "锁定会员", platUser.getReason(), isSend ? "1" : "2",templateCode ,"");

        }

        if(value==0) {

            //解除踢掉用户
            this.kickOutPlatUser(platUser);

            addMessage(redirectAttributes, "解锁会员成功");
            renderString(response, "{\"success\": \"1\", \"msg\" : \"解锁会员成功\"}");

            boolean isSend = false;
            String templateCode = MessageTemplateCode.UN_LOCK_PLAT_USER.getCode();
            try{
                isSend = smsMessageService.sendSms(templateCode, platUser.getMobile());
            }catch (Exception e){
                logger.error("发送短信失败：" + e.getMessage());
            }

//            this.createOplog(dbPlatUser, PlatUserOplogTypeEnum.LOCK.getCode(), "锁定会员", platUser.getReason());
            this.createOplogWithMessage(dbPlatUser, PlatUserOplogTypeEnum.LOCK.getCode(), "解锁会员", platUser.getReason(), isSend ? "1" : "2",templateCode ,"");

        }

        return null;
    }

    @RequiresPermissions("plat:platUser:operate")
    @RequestMapping(value = "update/google")
    public String update(String id, RedirectAttributes redirectAttributes, HttpServletRequest request) {
    	platUserService.updateGoogleNull(id);
        PlatUser user = platUserService.get(id);
    	this.createOplog(user, PlatUserOplogTypeEnum.CLEAR_GOOGLE.getCode(), PlatUserOplogTypeEnum.CLEAR_GOOGLE.getMessage(), "");
        this.kickOutPlatUser(user);
        return "redirect:" + Global.getAdminPath() + "/plat/platUser/manList?repage";
    }
    
    @RequiresPermissions("plat:platUser:operate")
    @RequestMapping(value = "update/clearPass")
    public String clearPass(String id, RedirectAttributes redirectAttributes, HttpServletRequest request) {
    	PlatUser user = platUserService.get(id);
    	if(user!=null) {
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    		String dateStr = sdf.format(new Date()) ;
        	if(org.apache.commons.lang3.StringUtils.isNotBlank(user.getMail())) {
        		String validTimeKey  = new StringBuilder("validTime:")
                        .append(dateStr).append(":").append(user.getMail()).toString();
            	JedisUtils.del(validTimeKey);
        	}
        	if(org.apache.commons.lang3.StringUtils.isNotBlank(user.getMobile())) {
        		String validTimeKey  = new StringBuilder("validTime:")
                        .append(dateStr).append(":").append(user.getMobile()).toString();
            	JedisUtils.del(validTimeKey);
        	}
    	}
    	this.createOplog(user, PlatUserOplogTypeEnum.CLEAR_PASS.getCode(), PlatUserOplogTypeEnum.CLEAR_PASS.getMessage(), "");
        return "redirect:" + Global.getAdminPath() + "/plat/platUser/manList?repage";
    }
    
    @RequiresPermissions("plat:platUser:operate")
    @RequestMapping(value = "update/clearCardCheckTime")
    public String clearCardCheckTime(String id, RedirectAttributes redirectAttributes, HttpServletRequest request) {
    	PlatUser user = platUserService.get(id);
    	if(user!=null) {
    		user.setCardStatusCheckTime(0);
    		platUserService.save(user);
    	}
    	this.createOplog(user, PlatUserOplogTypeEnum.CLEAR_PASS.getCode(), PlatUserOplogTypeEnum.CLEAR_PASS.getMessage(), "");
        return "redirect:" + Global.getAdminPath() + "/plat/platUser/manList?repage";
    }
    
    @RequiresPermissions("plat:platUser:operate")
    @RequestMapping(value = "update/clearExPass")
    public String clearExPass(String id, RedirectAttributes redirectAttributes, HttpServletRequest request) {
    	PlatUser user = platUserService.get(id);
    	if(user!=null) {
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    		String dateStr = sdf.format(new Date()) ;
    		String validTimeKey  = new StringBuilder("c2s:trade:sell:")
                    .append(dateStr).append(":").append(user.getId()).toString();
        	JedisUtils.del(validTimeKey);
    	}
        this.createOplog(user, PlatUserOplogTypeEnum.CLEAR_EX_PASS.getCode(), PlatUserOplogTypeEnum.CLEAR_EX_PASS.getMessage(), "");
        return "redirect:" + Global.getAdminPath() + "/plat/platUser/manList?repage";
    }
    
    @RequiresPermissions("plat:platUser:c2cIn")
    @RequestMapping(value = "c2cIn/{value}")
    public String c2cIn(PlatUser platUser, @PathVariable("value") Integer value, RedirectAttributes redirectAttributes, HttpServletRequest request, HttpServletResponse response) {

        if(StringUtils.isEmpty(platUser.getReason())){
            renderString(response, "{\"success\": \"0\", \"msg\" : \"原因不能为空\"}");
            return null;
        }

        PlatUser user = platUserService.get(platUser.getId());
        String type = PlatUserOplogTypeEnum.C2C_IN.getCode();
        String content = "冻结转入C2C";

        user.setC2cIn(String.valueOf(value));
        platUserService.save(user);

        if("0".equals(String.valueOf(value))){
            content = "解冻转入C2C";
        }

        this.createOplog(platUser, type, content, platUser.getReason());
        this.kickOutPlatUser(platUser);
        renderString(response, "{\"success\": \"1\", \"msg\" : \"" +content.concat("成功") + "\"}");
        addMessage(redirectAttributes,  content + "成功");
        return null;
    }


    @RequiresPermissions("plat:platUser:c2cOut")
    @RequestMapping(value = "c2cOut/{value}")
    public String c2cOut(PlatUser platUser, @PathVariable("value") Integer value, RedirectAttributes redirectAttributes, HttpServletRequest request, HttpServletResponse response) {

        if(StringUtils.isEmpty(platUser.getReason())){
            renderString(response, "{\"success\": \"0\", \"msg\" : \"原因不能为空\"}");
            return null;
        }

        PlatUser user = platUserService.get(platUser.getId());

        String type = PlatUserOplogTypeEnum.C2C_OUT.getCode();
        String content = "冻结转出C2C";

        user.setC2cOut(String.valueOf(value));
        platUserService.save(user);
        if("0".equals(String.valueOf(value))){
            content = "解冻转出C2C";
        }

        this.createOplog(platUser, type, content, platUser.getReason());
        this.kickOutPlatUser(platUser);
        renderString(response, "{\"success\": \"1\", \"msg\" : \"" +content.concat("成功") + "\"}");
        addMessage(redirectAttributes,  content + "成功");
        return null;
    }

    @RequestMapping(value = "c2cPublish/{value}")
    public String c2cPublish(PlatUser platUser, @PathVariable("value") Integer value, RedirectAttributes redirectAttributes, HttpServletRequest request, HttpServletResponse response) {

        if(StringUtils.isEmpty(platUser.getReason())){
            renderString(response, "{\"success\": \"0\", \"msg\" : \"原因不能为空\"}");
            return null;
        }

        PlatUser user = platUserService.get(platUser.getId());
        String type = PlatUserOplogTypeEnum.PUBLISH.getCode();
        String content = "冻结发广告";

        user.setC2cPublish(String.valueOf(value));
        platUserService.save(user);
        if("0".equals(String.valueOf(value))){
            content = "解冻发广告";
        }

        this.createOplog(platUser, type, content, platUser.getReason());
        this.kickOutPlatUser(platUser);
        renderString(response, "{\"success\": \"1\", \"msg\" : \"" +content.concat("成功") + "\"}");
        addMessage(redirectAttributes,  content + "成功");
        return null;
    }


    @RequiresPermissions("plat:platUser:coinOut")
    @RequestMapping(value = "coinOut/{value}")
    public String coinOut(PlatUser platUser, @PathVariable("value") Integer value, RedirectAttributes redirectAttributes, HttpServletResponse response) {

        if(StringUtils.isEmpty(platUser.getReason())){
            renderString(response, "{\"success\": \"0\", \"msg\" : \"原因不能为空\"}");
            return null;
        }

        PlatUser dbPlatUser = platUserService.get(platUser.getId());

        String type = PlatUserOplogTypeEnum.COIN_OUT.getCode();
        String content = "禁止提现";

        dbPlatUser.setCoinOut(String.valueOf(value));
        platUserService.save(dbPlatUser);

        if("0".equals(String.valueOf(value))){
            content = "解禁提现";
        }

        this.createOplog(platUser, type, content, platUser.getReason());
        this.kickOutPlatUser(platUser);
        renderString(response, "{\"success\": \"1\", \"msg\" : \"" +content.concat("成功") + "\"}");
        addMessage(redirectAttributes,  content + "成功");
        return null;
    }

    @RequiresPermissions("plat:platUser:c2cChange")
    @RequestMapping(value = "c2cChange/{value}")
    public String c2cChange(PlatUser platUser, @PathVariable("value") Integer value, RedirectAttributes redirectAttributes, HttpServletResponse response) {

        if(StringUtils.isEmpty(platUser.getReason())){
            renderString(response, "{\"success\": \"0\", \"msg\" : \"原因不能为空\"}");
            return null;
        }

        PlatUser dbPlatUser = platUserService.get(platUser.getId());

        String type = PlatUserOplogTypeEnum.CHANGE.getCode();
        String content = "禁用转账";

        dbPlatUser.setC2cChange(String.valueOf(value));
        platUserService.save(dbPlatUser);

        if("0".equals(String.valueOf(value))){
            content = "恢复转账";
        }

        this.createOplog(platUser, type, content, platUser.getReason());
        this.kickOutPlatUser(platUser);
        renderString(response, "{\"success\": \"1\", \"msg\" : \"" +content.concat("成功") + "\"}");
        addMessage(redirectAttributes,  content + "成功");
        return null;
    }

    @RequiresPermissions("plat:platUser:operate")
    @RequestMapping(value = "update/cleanMail")
    public String cleanMail(PlatUser platUser, RedirectAttributes redirectAttributes) {
        PlatUser dbPlatUser = platUserService.get(platUser.getId());
        dbPlatUser.setMail(null); //清空邮箱
        platUserService.save(dbPlatUser);
        this.createOplog(dbPlatUser, PlatUserOplogTypeEnum.CLEAR_MAIL.getCode(), "清空邮箱", "");
        this.kickOutPlatUser(platUser);
        addMessage(redirectAttributes, "清空邮箱成功");
        return "redirect:" + Global.getAdminPath() + "/plat/platUser/manList?repage";
    }

    @RequiresPermissions("plat:platUser:operate")
    @RequestMapping(value = "update/cleanOfflineCancel")
    public String cleanOfflineCancel(PlatUser platUser, RedirectAttributes redirectAttributes) {
        offlineCancelLogService.deleteAdCancelLogByUserId(platUser.getId());
        this.createOplog(platUser, PlatUserOplogTypeEnum.CLEAR_AD_CANCEL_LOG.getCode(), PlatUserOplogTypeEnum.CLEAR_AD_CANCEL_LOG.getMessage(), "");
        addMessage(redirectAttributes, "清空发广告次数成功");
        return "redirect:" + Global.getAdminPath() + "/plat/platUser/manList?repage";
    }

    @RequiresPermissions("plat:platUser:keyinfo")
    @RequestMapping(value = "update/changeMobile")
    public String changeMobile(PlatUser platUser, Model model, RedirectAttributes redirectAttributes, HttpServletResponse response) {
        PlatUser dbPlatUser = platUserService.get(platUser.getId());
        String oldMobile = dbPlatUser.getMobile();
        dbPlatUser.setMobile(platUser.getMobile());

        if(StringUtils.isEmpty(platUser.getMobile())){
            renderString(response, "{\"success\": \"0\", \"msg\" : \"会员手机不能为空\"}");
            return null;
        }

        if(StringUtils.isNotEmpty(oldMobile) && oldMobile.equals(dbPlatUser.getMobile())){
            renderString(response, "{\"success\": \"0\", \"msg\" : \"会员手机不能和修改前的一样\"}");
            return null;
        }

        PlatUser platUserTmp = platUserService.findByMobile(dbPlatUser.getMobile());
        if(!ObjectUtils.isEmpty(platUserTmp)){
            renderString(response, "{\"success\": \"0\", \"msg\" : \"会员手机已存在\"}");
            return null;
        }

        if(StringUtils.isEmpty(platUser.getReason())){
            renderString(response, "{\"success\": \"0\", \"msg\" : \"原因不能为空\"}");
            return null;
        }

        try {
            platUserService.save(dbPlatUser);
            JsPlatUserOplog jsPlatUserOplog = new JsPlatUserOplog();
            jsPlatUserOplog.setUserId(dbPlatUser.getId());
            jsPlatUserOplog.setMail(dbPlatUser.getMail());
            jsPlatUserOplog.setMobile(dbPlatUser.getMobile());
            jsPlatUserOplog.setRealName(dbPlatUser.getRealName());
            jsPlatUserOplog.setType(PlatUserOplogTypeEnum.CHANGE_MOBILE.getCode());
            jsPlatUserOplog.setContent(String.format("手机由%s修改为%s", oldMobile, dbPlatUser.getMobile()));
            jsPlatUserOplog.setReason(platUser.getReason());
            jsPlatUserOplogService.save(jsPlatUserOplog);
            renderString(response, "{\"success\": \"1\", \"msg\" : \"会员手机修改成功！\"}");

            //踢出前台登录用户
            this.kickOutPlatUser(platUser);
        } catch (Exception e) {
            logger.error(e.getMessage());
            renderString(response, "{\"success\": \"0\", \"msg\" : \"会员手机修改失败！\"}");
        }
        return null;

    }

    @RequiresPermissions("plat:platUser:keyinfo")
    @RequestMapping(value = "update/changeTag")
    public String changeTag(PlatUser platUser, Model model, RedirectAttributes redirectAttributes, HttpServletResponse response) {
        PlatUser dbPlatUser = platUserService.get(platUser.getId());
        String oldTag = dbPlatUser.getTag();
        dbPlatUser.setTag(platUser.getTag());

        if(StringUtils.isEmpty(platUser.getReason())){
            renderString(response, "{\"success\": \"0\", \"msg\" : \"原因不能为空\"}");
            return null;
        }

        try {
            platUserService.save(dbPlatUser);
            JsPlatUserOplog jsPlatUserOplog = new JsPlatUserOplog();
            jsPlatUserOplog.setUserId(dbPlatUser.getId());
            jsPlatUserOplog.setMail(dbPlatUser.getMail());
            jsPlatUserOplog.setMobile(dbPlatUser.getMobile());
            jsPlatUserOplog.setRealName(dbPlatUser.getRealName());
            jsPlatUserOplog.setType(PlatUserOplogTypeEnum.CHANGE_TAG.getCode());
            jsPlatUserOplog.setContent(String.format("会员标识由%s修改为%s", oldTag, dbPlatUser.getTag()));
            jsPlatUserOplog.setReason(platUser.getReason());
            jsPlatUserOplogService.save(jsPlatUserOplog);
            this.kickOutPlatUser(platUser);
            renderString(response, "{\"success\": \"1\", \"msg\" : \"会员标识修改成功！\"}");
        } catch (Exception e) {
            logger.error(e.getMessage());
            renderString(response, "{\"success\": \"0\", \"msg\" : \"会员标识修改失败功！\"}");
        }
        return null;
    }

    @RequiresPermissions("plat:platUser:c2cSwitch")
    @RequestMapping(value = "c2cSwitch/{value}")
    public String c2cSwitch(PlatUser platUser, @PathVariable("value") Integer value, RedirectAttributes redirectAttributes, HttpServletResponse response) {
        PlatUser dbPlatUser = platUserService.get(platUser.getId());

        if(StringUtils.isEmpty(platUser.getReason())){
            renderString(response, "{\"success\": \"0\", \"msg\" : \"原因不能为空\"}");
            return null;
        }

        String type = PlatUserOplogTypeEnum.C2C_SWITCH.getCode();
        String content = "冻结C2C交易";

        dbPlatUser.setC2cSwitch(String.valueOf(value));
        platUserService.save(dbPlatUser);

        if("0".equals(String.valueOf(value))){
            content = "解冻C2C交易";
        }

        this.createOplog(platUser, type, content, platUser.getReason());
        this.kickOutPlatUser(platUser);
        renderString(response, "{\"success\": \"1\", \"msg\" : \"" +content.concat("成功") + "\"}");
        addMessage(redirectAttributes,  content + "成功");
        return null;
    }

    @RequiresPermissions("plat:platUser:tradeSwitch")
    @RequestMapping(value = "tradeSwitch/{value}")
    public String tradeSwitch(PlatUser platUser, @PathVariable("value") Integer value, RedirectAttributes redirectAttributes, HttpServletResponse response) {
        PlatUser dbPlatUser = platUserService.get(platUser.getId());

        if(StringUtils.isEmpty(platUser.getReason())){
            renderString(response, "{\"success\": \"0\", \"msg\" : \"原因不能为空\"}");
            return null;
        }

        String type = PlatUserOplogTypeEnum.TRADE_SWITCH.getCode();
        String content = "冻结币币交易";

        dbPlatUser.setTradeSwitch(String.valueOf(value));
        platUserService.save(dbPlatUser);

        if("0".equals(String.valueOf(value))){
            content = "解冻币币交易";
        }

        this.createOplog(platUser, type, content, platUser.getReason());
        this.kickOutPlatUser(platUser);
        renderString(response, "{\"success\": \"1\", \"msg\" : \"" +content.concat("成功") + "\"}");
        addMessage(redirectAttributes,  content + "成功");
        return null;
    }

    @RequiresPermissions("plat:platUser:lockDate")
    @RequestMapping(value = "lockDate/{value}")
    public String lockDate(PlatUser platUser, @PathVariable("value") Integer value, RedirectAttributes redirectAttributes, HttpServletResponse response) {

        PlatUser dbPlatUser = platUserService.get(platUser.getId());
        if(StringUtils.isEmpty(platUser.getReason())){
            renderString(response, "{\"success\": \"0\", \"msg\" : \"原因不能为空\"}");
            return null;
        }

        String content = "清空资产锁定时间";
        dbPlatUser.setLockDate(platUser.getLockDate());
        if(Objects.nonNull(platUser.getLockDate())){
            if(this.toLocalDateTime(platUser.getLockDate()).isBefore(LocalDateTime.now())){
                renderString(response, "{\"success\": \"0\", \"msg\" : \"锁定时间无效\"}");
                return null;
            }
            content = "锁定资产时间:" + DateUtils.formatDateTime(platUser.getLockDate());
        }

        platUserService.save(dbPlatUser);
        this.createOplog(platUser, PlatUserOplogTypeEnum.LOCK_DATE.getCode(), content, platUser.getReason());
        this.kickOutPlatUser(platUser);

        renderString(response, "{\"success\": \"1\", \"msg\" : \"" +content.concat("成功") + "\"}");
        addMessage(redirectAttributes,  content + "成功");
        return null;
    }

    @RequiresPermissions("plat:platUser:view")
    @RequestMapping(value = "userVolumeList")
    public String userVolumeList(PlatUser platUser, Model model) {
        model.addAttribute("platUser", platUser);
        model.addAttribute("imageUrl", imageUrl);
        UserCoinVolume userCoinVolume=new UserCoinVolume();
        userCoinVolume.setUserId(platUser.getId());
        List<UserCoinVolume> userCoinVolumeList = userCoinVolumeService.findList(userCoinVolume);
        model.addAttribute("userCoinVolumeList", userCoinVolumeList);
        return "modules/plat/platUserCoinVolumeList";
    }

    private void createOplog(PlatUser platUser, String type, String content, String reason){
        this.createOplogWithMessage(platUser, type, content, reason, "0", null, null);
    }

    private void createOplogWithMessage(PlatUser platUser, String type, String content, String reason, String sendStatus, String templateCode, String remark){
        JsPlatUserOplog jsPlatUserOplog = new JsPlatUserOplog();
        jsPlatUserOplog.setUserId(platUser.getId());
        jsPlatUserOplog.setMail(platUser.getMail());
        jsPlatUserOplog.setMobile(platUser.getMobile());
        jsPlatUserOplog.setRealName(platUser.getRealName());
        jsPlatUserOplog.setType(type);
        jsPlatUserOplog.setContent(content);
        jsPlatUserOplog.setReason(reason);
        jsPlatUserOplog.setSendStatus(sendStatus);
        jsPlatUserOplog.setTemplateCode(templateCode);
        jsPlatUserOplog.setRealName(remark);
        jsPlatUserOplogService.save(jsPlatUserOplog);
    }

    private void kickOutPlatUser(PlatUser platUser){
        String collectKey = String.format("user-session-collect:%s", platUser.getId());
        List<String> tokens = JedisUtils.getList(collectKey);
        if(!CollectionUtils.isEmpty(tokens)) {
            tokens.stream().distinct().forEach(token->{
                String ticketoutTag = String.format("user-session-ticketout:%s", token);
                JedisUtils.set(ticketoutTag, "true",3600);
            });
        }
    }

    private LocalDateTime toLocalDateTime(Date lockDate){
        Instant instant = lockDate.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(instant, zone);
    }


    /*private void giveCoin(PlatUser platUser){
        Mk2PopularizeRegisterConf conf = null;
        //第一步
        //查询注册送币规则
        List<Mk2PopularizeRegisterConf> confList = registerConfService.findEffective();
        if (!CollectionUtils.isEmpty(confList)) {
            conf = confList.get(0);
        }else{
            return;
        }
        Coin coin = coinDao.findByName(conf.getCoinSymbol());
        conf.setCoinId(coin.getId());
        //第二步  给实名认证通过用户送币
        UserCoinVolume coinVolume=userCoinVolumeService.getByUserIdAndCoinId(platUser.getId(),coin.getId());
        BigDecimal vol = new BigDecimal(conf.getRegisterVolume().toString());
        if(coinVolume==null){
            UserCoinVolume coinVolumeNew=new UserCoinVolume();
            coinVolumeNew.setVolume(vol);
            coinVolumeNew.setTotalVolume(vol);
            coinVolumeNew.setCoinSymbol(coin.getName());
            coinVolumeNew.setUserId(platUser.getId());
            coinVolumeNew.setMail(platUser.getMail());
            coinVolumeNew.setMobile(platUser.getMobile());
            coinVolumeNew.setCoinId(coin.getId());
            coinVolumeNew.setLockVolume(new BigDecimal("0"));
            coinVolumeNew.setOutLockVolume(new BigDecimal("0"));
            userCoinVolumeService.save(coinVolumeNew);
            //插入交易记录表
//            userCoinVolumeService.insertBill(coinVolumeNew);
            //插入交易记录历史 日志
            userCoinVolumeService.insertBillHistory(coinVolumeNew);
        }else{
            coinVolume.setVolume(coinVolume.getVolume().add(vol));
            coinVolume.setTotalVolume(coinVolume.getTotalVolume().add(vol));
            userCoinVolumeService.save(coinVolume);
            //插入交易记录表
//            userCoinVolumeService.insertBill(coinVolume);
            //插入交易记录历史 日志
            userCoinVolumeService.insertBillHistory(coinVolume);

        }
        //第三步
        //给送币记录表中插入数据
        Mk2PopularizeRegisterCoin registerCoin = new Mk2PopularizeRegisterCoin();
        registerCoin.setMail(platUser.getMail());
        registerCoin.setMobile(platUser.getMobile());
        registerCoin.setRegisterConfId(conf.getId());
        registerCoin.setConfName(conf.getName());
        registerCoin.setUserId(platUser.getId());
        registerCoin.setUserName(platUser.getRealName());
        registerCoin.setVolume(conf.getRegisterVolume());
        registerCoin.setCoinId(conf.getCoinId());
        registerCoin.setCoinSymbol(conf.getCoinSymbol());
        registerCoin.setStatus("2");
        registerCoinService.save(registerCoin);
        //第四步
        //更新规则表，已送出币数量
        conf.setGiveVolume(conf.getGiveVolume()+conf.getRegisterVolume());
        registerConfService.save(conf);
    }*/

    @RequiresPermissions("user")
    @ResponseBody
    @RequestMapping(value = "treeData")
    public List<Map<String, Object>> treeData(@RequestParam(required=false) String extId, @RequestParam(required=false) String type,
                                              @RequestParam(required=false) Long grade, @RequestParam(required=false) Boolean isAll, HttpServletResponse response) {
        List<Map<String, Object>> mapList = Lists.newArrayList();
        List<PlatUser> list = platUserService.findAllList(null);
        for (int i=0; i<list.size(); i++){
            PlatUser e = list.get(i);

                Map<String, Object> map = Maps.newHashMap();
                map.put("id", e.getId());
                map.put("pId", e.getReferId());
                map.put("pIds", e.getReferId());
                if(e.getMobile() == null){
                    map.put("name", e.getMail());
                }else{
                    map.put("name", e.getMobile());
                }

                if (type != null && "3".equals(type)){
                    map.put("isParent", true);
                }
                mapList.add(map);
        }
        return mapList;
    }
    @RequestMapping(value = {"index"})
    public String index(PlatUser platUser, Model model) {
//        model.addAttribute("list", officeService.findAll());
        return "modules/sys/platUserIndex";
    }

    @RequestMapping(value = {"deailsList"})
    public String deailsList(PlatUser platUser, Model model) {
//        model.addAttribute("list", officeService.findAll());
        return "modules/sys/platUserList";
    }
}