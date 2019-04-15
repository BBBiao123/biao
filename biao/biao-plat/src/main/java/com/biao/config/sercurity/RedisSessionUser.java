package com.biao.config.sercurity;

import com.biao.config.UserConfig;
import com.biao.entity.PlatUser;
import com.biao.enums.UserStatusEnum;
import com.biao.redis.RedisCacheManager;
import com.biao.spring.SpringBeanFactoryContext;
import com.biao.util.DateUtils;
import com.biao.util.StringHelp;
import com.biao.vo.redis.RedisMkAutoTradeUserVO;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RedisSessionUser implements UserDetails {

    private static Set<String> IGNORE_USERIDS = new HashSet<>();

//    static {
//        IGNORE_USERIDS.add("1");
//        IGNORE_USERIDS.add("2");
//    }

    private static final long serialVersionUID = 1L;
    private String id;
    private String token;
    private Integer userType;
    private String googleAuth;
    private String realName;
    private Integer cardStatus;
    private String password;
    private String mobile;//手机号
    private String username;//用户名
    private String mail;//邮箱
    private String alipayNo;
    private String alipayQrcodeId;
    private String wechatQrcodeId;
    private String wechatNo;
    private String loginIp;
    private String inviteCode;
    private String referInviteCode;
    private String createTime;
    private String referId;
    private String exPassword;
    //1：谷歌验证  2：交易密码验证 3:短信 4:邮箱
    private Integer exValidType;
    private String tag;

    private Integer lockLength;
    private String nickName ;

    //spring security need
    private boolean enabled = true;//是否启用 ，默认true
    private Collection<? extends GrantedAuthority> authorities;
    private boolean accountNonLocked;

    private String loginSource; // 登录来源
    
    private Integer cardStatusCheckTime ;
    private Integer cardLevel ;
    private String countryCode ;

    //客服系统
    private String csUsername;
    private String csPass;
    private String isRegisteredCs;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime lockDate;

    public static RedisSessionUser converRedisSessionUser(PlatUser user, UserConfig UserConfig) {
        RedisSessionUser redisSessionUser = new RedisSessionUser();
        redisSessionUser.setId(user.getId());
        redisSessionUser.setMobile(user.getMobile());
        redisSessionUser.setExPassword(user.getExPassword());
        redisSessionUser.setUserType(user.getUserType());
        redisSessionUser.setGoogleAuth(user.getGoogleAuth());
        redisSessionUser.setRealName(user.getRealName());
        redisSessionUser.setCardStatus(user.getCardStatus());
        redisSessionUser.setUsername(user.getUsername());
        redisSessionUser.setMail(user.getMail());
        redisSessionUser.setPassword(user.getPassword());
        redisSessionUser.setAlipayNo(user.getAlipayNo());
        redisSessionUser.setAlipayQrcodeId(user.getAlipayQrcodeId());
        redisSessionUser.setWechatNo(user.getWechatNo());
        redisSessionUser.setWechatQrcodeId(user.getWechatQrcodeId());
        redisSessionUser.setInviteCode(user.getInviteCode());
        redisSessionUser.setReferId(user.getReferId());
        redisSessionUser.setExValidType(user.getExValidType());
        redisSessionUser.setTag(user.getTag());
        redisSessionUser.setCardStatusCheckTime(user.getCardStatusCheckTime());
        redisSessionUser.setCardLevel(user.getCardLevel());
        redisSessionUser.setCountryCode(user.getCountryCode());
        //spring security need
        redisSessionUser.setEnabled(true);
        redisSessionUser.setAccountNonLocked(true);
        redisSessionUser.setLockLength(UserConfig.getLockLength() == null ? 24 : UserConfig.getLockLength());
        redisSessionUser.setNickName(user.getNickName());
        if (user.getLockDate() != null && user.getLockDate().isAfter(LocalDateTime.now())) {
            redisSessionUser.setLockDate(user.getLockDate());
        }
        if (user.getCreateDate() != null) {
            redisSessionUser.setCreateTime(DateUtils.formaterLocalDateTime(user.getCreateDate()));
        }
        redisSessionUser.setReferInviteCode(user.getReferInviteCode());
        if (user.getStatus() != null) {
            if (UserStatusEnum.USER_LOCK.getCode().equals(user.getStatus().toString())) {
                //账号锁定
                redisSessionUser.setAccountNonLocked(false);
            }
            if (UserStatusEnum.USER_DISABLE.getCode().equals(user.getStatus().toString())) {
                //账号禁用
                redisSessionUser.setEnabled(false);
            }
        }

        redisSessionUser.setIsRegisteredCs(user.getIsRegisteredCs());
        redisSessionUser.setCsUsername(buildCsUsername(user.getId(), user.getMobile(), user.getMail()));
        redisSessionUser.setCsPass("5hMTZI9i");

        return redisSessionUser;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getGoogleAuth() {
        return googleAuth;
    }

    public void setGoogleAuth(String googleAuth) {
        this.googleAuth = googleAuth;
    }

    public String getRealName() {
        return realName;
    }

    public String getAlipayNo() {
        return alipayNo;
    }

    public void setAlipayNo(String alipayNo) {
        this.alipayNo = alipayNo;
    }

    public String getAlipayQrcodeId() {
        return alipayQrcodeId;
    }

    public void setAlipayQrcodeId(String alipayQrcodeId) {
        this.alipayQrcodeId = alipayQrcodeId;
    }

    public String getWechatQrcodeId() {
        return wechatQrcodeId;
    }

    public void setWechatQrcodeId(String wechatQrcodeId) {
        this.wechatQrcodeId = wechatQrcodeId;
    }

    public String getWechatNo() {
        return wechatNo;
    }

    public void setWechatNo(String wechatNo) {
        this.wechatNo = wechatNo;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public Integer getCardStatus() {
        return cardStatus;
    }

    public void setCardStatus(Integer cardStatus) {
        this.cardStatus = cardStatus;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isAccountNonExpired() {
        return true;
    }

    public boolean isCredentialsNonExpired() {
        return true;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public static Set<String> getIGNORE_USERIDS() {

        //清空
        IGNORE_USERIDS.clear();

        //通过Redis获取最新数据
        RedisCacheManager redisCacheManager = null;
        try {
            redisCacheManager = (RedisCacheManager) SpringBeanFactoryContext.findBean(RedisCacheManager.class);
        } catch (Exception e) {
            System.out.println("获取RedisCacheManager出现异常," + e.getMessage());
            return IGNORE_USERIDS;
        }

        if (null == redisCacheManager) {
            return IGNORE_USERIDS;
        }

        //解析数据
        List<RedisMkAutoTradeUserVO> redisMkAutoTradeUserVOList = redisCacheManager.acquireAllMkAutoTradeUser();
        if (!CollectionUtils.isEmpty(redisMkAutoTradeUserVOList)) {
            redisMkAutoTradeUserVOList.forEach(redisMkAutoTradeUserVO -> {
                IGNORE_USERIDS.add(redisMkAutoTradeUserVO.getUserId());
            });
        }

        return IGNORE_USERIDS;
    }

    public static boolean isUserFilter(String userId) {
        if (IGNORE_USERIDS.contains(userId)) {
            return true;
        }
        if (StringHelp.regexMatcher("\\d{1,3}", userId)) {
            return true;
        }
        return false;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public String getReferId() {
        return referId;
    }

    public void setReferId(String referId) {
        this.referId = referId;
    }

    public String getExPassword() {
        return exPassword;
    }

    public void setExPassword(String exPassword) {
        this.exPassword = exPassword;
    }

    public Integer getExValidType() {
        return exValidType;
    }

    public void setExValidType(Integer exValidType) {
        this.exValidType = exValidType;
    }

    public String getLoginSource() {
        return loginSource;
    }

    public void setLoginSource(String loginSource) {
        this.loginSource = loginSource;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getReferInviteCode() {
        return referInviteCode;
    }

    public void setReferInviteCode(String referInviteCode) {
        this.referInviteCode = referInviteCode;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getLockDate() {
        return lockDate;
    }

    public void setLockDate(LocalDateTime lockDate) {
        this.lockDate = lockDate;
    }

    public Integer getLockLength() {
        return lockLength;
    }

    public void setLockLength(Integer lockLength) {
        this.lockLength = lockLength;
    }

    public Integer getCardStatusCheckTime() {
		return cardStatusCheckTime;
	}

	public void setCardStatusCheckTime(Integer cardStatusCheckTime) {
		this.cardStatusCheckTime = cardStatusCheckTime;
	}

	public Integer getCardLevel() {
		return cardLevel;
	}

	public void setCardLevel(Integer cardLevel) {
		this.cardLevel = cardLevel;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	/**
     * 判断是否可以交易
     *
     * @return true:不能交易   false:可以交易
     */
    public boolean isLockTrade() {
        if (this.lockDate == null) {
            return false;
        }
        return this.lockDate.isAfter(LocalDateTime.now());
    }

    public String getCsUsername() {
        return csUsername;
    }

    public void setCsUsername(String csUsername) {
        this.csUsername = csUsername;
    }

    public String getCsPass() {
        return csPass;
    }

    public void setCsPass(String csPass) {
        this.csPass = csPass;
    }

    public String getIsRegisteredCs() {
        return isRegisteredCs;
    }

    public void setIsRegisteredCs(String isRegisteredCs) {
        this.isRegisteredCs = isRegisteredCs;
    }

    public static String buildCsUsername(String userId, String mobile, String mail){
        String csUsername = "";
        if(userId.length() > 8){
            csUsername = "-".concat(userId.substring(0,8));
        }else{
            csUsername = "-".concat(userId);
        }

        if(StringUtils.isNotEmpty(mobile)){
            csUsername = mobile.substring(7,11).concat(csUsername);
        }else{
            csUsername = mail.substring(0,4).concat(csUsername);
        }
        return csUsername;
    }
}


