package com.biao.service.impl;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.biao.aliyun.check.SyncCardCheck;
import com.biao.constant.Constants;
import com.biao.constant.RedisConstants;
import com.biao.constant.SercurityConstant;
import com.biao.disruptor.DisruptorData;
import com.biao.disruptor.DisruptorManager;
import com.biao.entity.PlatUser;
import com.biao.entity.PlatUserOplog;
import com.biao.entity.PlatUserSyna;
import com.biao.entity.UserRelation;
import com.biao.enums.CardStatusEnum;
import com.biao.enums.PlatUserOplogTypeEnum;
import com.biao.enums.UserCardStatusEnum;
import com.biao.enums.UserStatusEnum;
import com.biao.exception.PlatException;
import com.biao.mapper.PlatUserDao;
import com.biao.mapper.PlatUserOplogDao;
import com.biao.mapper.mk2.Mk2PopularizeMiningGiveCoinLogDao;
import com.biao.pojo.CardStatuScanCheckDTO;
import com.biao.pojo.ResponsePage;
import com.biao.query.UserLoginLogQuery;
import com.biao.service.MessageSendService;
import com.biao.service.PlatUserService;
import com.biao.service.SerialCodeService;
import com.biao.service.SuperBookService;
import com.biao.util.DateUtil;
import com.biao.util.NumberUtils;
import com.biao.util.SnowFlake;
import com.biao.util.StringHelp;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.base.Joiner;

@Service
public class PlatUserServiceImpl implements PlatUserService {

    private static org.slf4j.Logger logger = LoggerFactory.getLogger(PlatUserServiceImpl.class);

    @Autowired
    private PlatUserDao platUserDao;
    @Autowired(required = false)
    private PasswordEncoder passwordEncoder;
    @Autowired
    private SerialCodeService serialCodeService;
    @Autowired
    private MessageSendService messageSendService;
    @Resource(name = "stringRedisTemplate")
    ValueOperations<String, String> valOpsStr;

    @Value("${inviteCodes}")
    private String inviteCodes = "";

    @Autowired
    private Mk2PopularizeMiningGiveCoinLogDao mk2PopularizeMiningGiveCoinLogDao;

    @Autowired
    private SuperBookService superBookService;
    @Autowired
    private PlatUserOplogDao platUserOplogDao;

    @Transactional
    @Override
    public String registerPlatUser(PlatUser platUser, boolean isValidInvoteCode) {
        PlatUser existUser = platUserDao.findExistByMobieOrMail(platUser.getUsername());
        if (existUser != null) {
            throw new PlatException(Constants.USERNAME_EXIST_ERROR, "该用户已经注册");
        }
        if (valOpsStr.setIfAbsent(RedisConstants.USER_INVOTE_KEY, "10000")) {
            String maxInvote = platUserDao.findMaxInviteCode();
            if (StringUtils.isNotBlank(maxInvote)) {
                valOpsStr.set(RedisConstants.USER_INVOTE_KEY, maxInvote);
            }
        }
//        String invoteCodeInc = String.valueOf(valOpsStr.increment(RedisConstants.USER_INVOTE_KEY, 1L));
        String invoteCodeInc =NumberUtils.getRandomNumber(8);
        if (StringUtils.isNotBlank(platUserDao.findIdByInviteCode(invoteCodeInc))) {
            throw new PlatException(Constants.INVOTE_CODE_SYNC_ERROR, "邀请码冲突,请重新注册");
        }
        if (StringUtils.isNotBlank(platUser.getInviteCode()) && !"null".equals(platUser.getInviteCode())) {
            Optional<String> optional = Optional.ofNullable(platUserDao.findIdByInviteCode(platUser.getInviteCode()));
            optional.ifPresent(userId -> {
                platUser.setReferId(userId);
                platUser.setReferInviteCode(platUser.getInviteCode());
            });
            optional.orElseThrow(() -> new PlatException(Constants.INVOTE_CODE_SYNC_ERROR, "邀请码不正确！"));
        }
//        else {
//            throw new PlatException(Constants.INVOTE_CODE_SYNC_ERROR, "邀请码不能为空！");
//        }
        // ==邀请码校验
//        if (isValidInvoteCode) {
//            checkInviteCode(platUser.getInviteCode());
//        }

        platUser.setInviteCode(invoteCodeInc);
        String id = SnowFlake.createSnowFlake().nextIdString();
        platUser.setPassword(passwordEncoder.encode(platUser.getPassword()));
        platUser.setStatus(Integer.parseInt(UserStatusEnum.USER_NORMAl.getCode()));
        platUser.setUserType(1);
        platUser.setCreateDate(LocalDateTime.now());
        platUser.setId(id);
        String prefix = new StringBuilder(platUser.getUserType()).toString();
        platUser.setUsername(serialCodeService.generateSerialCode(prefix));
        platUser.setCardStatus(Integer.parseInt(UserCardStatusEnum.NO_APPLY.getCode()));
        platUser.setCardLevel(CardStatusEnum.CARD_STATUS_ZERO.getCode());
        platUser.setC2cIn("0");
        platUser.setCoinOut("0");
        platUser.setC2cPublish("0");
        platUser.setC2cChange("0");
        platUserDao.insert(platUser);

        superBookService.initSuperBook(platUser.getId()); // 初始化用户超级账本地址信息

        //手机号注册
        if (StringUtils.isNotBlank(platUser.getMobile())) {
            DisruptorManager.instance().runConfig();
            DisruptorData data = new DisruptorData();
            data.setType(3);
            data.setPlatUser(platUser);
            DisruptorManager.instance().publishData(data);
        }
        //发送消息
        return id;
    }

    @Override
    public PlatUser synUser(PlatUser platUser, PlatUserSyna platUserSyna) {
        String password = NumberUtils.getRandomNumber(8);
        platUserSyna.setPass(password);
        platUser.setPassword(password);
        platUser.setUsername(platUser.getMobile());
        platUser.setIsAward("1"); //设置为已奖励送币
        this.registerPlatUser(platUser, false);
        //发送用户密码短信
//		smsMessageService.sendSms(platUser.getMobile(), MessageTemplateCode.MOBILE_SYN_TEMPLATE.getCode(), password);

        return platUser;
    }

    /**
     * 校验邀请码，必须输入，而且必须是固定的配置文件里的几个邀请码，专给UES用
     *
     * @param inviteCode
     */
    private void checkInviteCode(String inviteCode) {
        if (StringUtils.isBlank(inviteCodes)) {
            return;
        }
        String[] inviteCodeArr = inviteCodes.split(";");
        Map<String, String> inviteCodeMap = new HashMap<>();
        List<String> codesList = Arrays.asList(inviteCodeArr);
        codesList.forEach(code -> inviteCodeMap.put(code, code));
        if (StringUtils.isBlank(inviteCode)) {
            throw new PlatException(Constants.COMMON_ERROR_CODE, "邀请码不正确，请重新输入");
        }
        if (inviteCodeMap.containsKey(inviteCode)) {
            return;
        } else {
            String userId = platUserDao.findIdByInviteCode(inviteCode);
            if (StringUtils.isBlank(userId)) {
                throw new PlatException(Constants.COMMON_ERROR_CODE, "邀请码不正确，请重新输入");
            }
        }

    }

    @Override
    public PlatUser findById(String id) {
        return platUserDao.findById(id);
    }

    @Override
    public PlatUser findByUsername(String username) {
        return platUserDao.findByUsername(username);
    }

    @Override
    public PlatUser findByLoginName(String mailOrMobile) {
        //验证邮箱
        if (StringHelp.regexMatcher("^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*\\.[a-zA-Z0-9]{2,6}$", mailOrMobile)) {
            return platUserDao.findExistByMmail(mailOrMobile);
        }
        //验证手机
        if (StringHelp.regexMatcher("^1[2|3|4|5|6|8][0-9]\\d{4,9}$", mailOrMobile)) {
            return platUserDao.findExistByMobie(mailOrMobile);
        }
        return platUserDao.findExistByMobieOrMail(mailOrMobile);
    }

    @Override
    public ResponsePage<PlatUser> findInvitesById(UserLoginLogQuery query) {
        ResponsePage<PlatUser> responsePage = new ResponsePage<>();
        Page<PlatUser> page = PageHelper.startPage(query.getCurrentPage(), query.getShowCount());
        List<PlatUser> users = platUserDao.findInvitesById(query.getUserId());
        List<PlatUser> simpleUsers = users.stream().map(simpleUser -> {
            PlatUser plat = new PlatUser();
            if (StringUtils.isNotBlank(simpleUser.getMobile())) {
                //139****7637
                //plat.setMobile(simpleUser.getMobile().substring(0, 3)+"****"+simpleUser.getMobile().substring(7, 11));
                plat.setMobile(simpleUser.getMobile());
            }
            if (StringUtils.isNotBlank(simpleUser.getMail())) {
                //139****7637
                //int index = simpleUser.getMail().indexOf("@");
				/*if(index!=-1&&index>4) {
					plat.setMail(simpleUser.getMail().substring(0, 4)+"****"+simpleUser.getMail().substring(index, simpleUser.getMail().length()));
				}else {
					plat.setMail(simpleUser.getMail());
				}*/
                plat.setMail(simpleUser.getMail());
            }
            plat.setRealName(simpleUser.getRealName());
            //持币挖矿
            String moneyHoldingTagMkId = mk2PopularizeMiningGiveCoinLogDao.findIdByUserId(simpleUser.getId(), "1", LocalDate.now().plusDays(-1));
            //多元挖矿
            String multielementTagMkId = mk2PopularizeMiningGiveCoinLogDao.findIdByUserId(simpleUser.getId(), "2", LocalDate.now().plusDays(-1));
            plat.setMoneyHoldingTag(moneyHoldingTagMkId);
            plat.setMultielementTag(multielementTagMkId);
            return plat;
        }).collect(Collectors.toList());
        responsePage.setCount(page.getTotal());
        responsePage.setList(simpleUsers);
        return responsePage;
    }

    @Override
    public void updateCardById(PlatUser platUser) {
        PlatUser eixst = this.findById(platUser.getId());
        if (eixst == null) {
            throw new PlatException(Constants.USER_IS_NULL_ERROR, "用户不存在");
        }
        if (eixst.getCardStatus() != null && (eixst.getCardStatus().equals(Integer.parseInt(UserCardStatusEnum.APPLY.getCode())) || eixst.getCardStatus().equals(Integer.parseInt(UserCardStatusEnum.TWO_APPLY.getCode())))) {
            throw new PlatException(Constants.USER_CARD_PASS_ERROR, "用户已经认证,不能再次认证");
        }
        //验证身份证是否绑定了
        Long count = platUserDao.findExistByIdcard(platUser.getIdCard(), platUser.getId());
        if (count != null && count > 0) {
            throw new PlatException(Constants.USER_CARD_PASS_ERROR, "用户身份证已经绑定,请联系客服");
        }
        this.updateById(platUser);
    }

    @Transactional
    @Override
    public void updateById(PlatUser platUser) {
        platUser.setUpdateDate(LocalDateTime.now());
        long count = platUserDao.updateById(platUser);
        if (count == 0) {
            throw new PlatException(Constants.UPDATE_ERROR, "更新失败");
        }
    }

    @Transactional
    @Override
    public void updateNickNameById(PlatUser platUser) {
        PlatUser updateUser = new PlatUser();
        updateUser.setNickName(platUser.getNickName());
        ;
        updateUser.setId(platUser.getId());
        this.updateById(updateUser);
    }

    @Transactional
    @Override
    public void updatePassword(PlatUser platUser) {
        PlatUser user = this.findById(platUser.getId());
        if (user == null) {
            throw new PlatException(Constants.USER_IS_NULL_ERROR, "用户不存在");
        }
        boolean match = passwordEncoder.matches(platUser.getOldPassword(), user.getPassword());
        if (!match) {
            throw new PlatException(Constants.USER_VALID_PASSWORD_ERROR, "请输入正确的密码");
        }
        //验证新密码是否和原密码一样
        match = passwordEncoder.matches(platUser.getPassword(), user.getPassword());
        if (match) {
            throw new PlatException(Constants.USER_VALID_REPASSWORD_ERROR, "新密码和原密码不能一样");
        }
        PlatUser updateUser = new PlatUser();
        updateUser.setPassword(passwordEncoder.encode(platUser.getPassword()));
        updateUser.setId(platUser.getId());
        this.updateById(updateUser);
    }

    @Transactional
    @Override
    public void updateExpasswordById(PlatUser platUser) {
        PlatUser user = this.findById(platUser.getId());
        if (user == null) {
            throw new PlatException(Constants.USER_IS_NULL_ERROR, "用户不存在");
        }
//        boolean match = passwordEncoder.matches(platUser.getPassword(), user.getPassword());
//        if (!match) {
//            throw new PlatException(Constants.USER_VALID_PASSWORD_ERROR, "请输入正确的密码");
//        }
        PlatUser updateUser = new PlatUser();
        updateUser.setId(platUser.getId());
        updateUser.setExPassword(platUser.getExPassword());
        updateUser.setLockDate(platUser.getLockDate());
        this.updateById(updateUser);
    }

    @Transactional
    @Override
    public void updateGoogleAuthById(PlatUser platUser) {
        platUser.setUpdateDate(LocalDateTime.now());
        long count = platUserDao.updateGoogleAuthById(platUser);
        if (count == 0) {
            throw new PlatException(Constants.UPDATE_ERROR, "更新失败");
        }
    }

    @Override
    public PlatUser resetPassword(PlatUser platUser) {
        String mail = messageSendService.findMailByPtoken(platUser.getPtoken());
        PlatUser existUser = platUserDao.findExistByMobieOrMail(mail);
        if (existUser == null) {
            throw new PlatException(Constants.USER_IS_NULL_ERROR, "该用户不存在");
        }
        PlatUser updateUser = new PlatUser();
        updateUser.setPassword(passwordEncoder.encode(platUser.getPassword()));
        updateUser.setId(existUser.getId());
        this.updateById(updateUser);
        messageSendService.expirePtoken(platUser.getPtoken());
        //清除当天错误次数的key
        String validTimeKey = new StringBuilder(SercurityConstant.VALID_TIME_USERNAME_REDIS_NAMESAPCE)
                .append(DateUtil.formatDate()).append(":").append(existUser.getMail()).toString();
        valOpsStr.getOperations().delete(validTimeKey);
        if (StringUtils.isNotBlank(existUser.getMobile())) {
            validTimeKey = new StringBuilder(SercurityConstant.VALID_TIME_USERNAME_REDIS_NAMESAPCE)
                    .append(DateUtil.formatDate()).append(":").append(existUser.getMobile()).toString();
            valOpsStr.getOperations().delete(validTimeKey);
        }
        return updateUser;
    }

    @Override
    public PlatUser resetPasswordByMobile(PlatUser platUser) {
        PlatUser existUser = platUserDao.findExistByMobie(platUser.getMobile());
        if (existUser == null) {
            throw new PlatException(Constants.USER_IS_NULL_ERROR, "该用户不存在");
        }
        PlatUser updateUser = new PlatUser();
        updateUser.setPassword(passwordEncoder.encode(platUser.getPassword()));
        updateUser.setId(existUser.getId());
        this.updateById(updateUser);
        //清除当天错误次数的key
        String validTimeKey = new StringBuilder(SercurityConstant.VALID_TIME_USERNAME_REDIS_NAMESAPCE)
                .append(DateUtil.formatDate()).append(":").append(existUser.getMobile()).toString();
        valOpsStr.getOperations().delete(validTimeKey);
        if (StringUtils.isNotBlank(existUser.getMail())) {
            validTimeKey = new StringBuilder(SercurityConstant.VALID_TIME_USERNAME_REDIS_NAMESAPCE)
                    .append(DateUtil.formatDate()).append(":").append(existUser.getMail()).toString();
            valOpsStr.getOperations().delete(validTimeKey);
        }
        return updateUser;
    }

    @Override
    @Transactional
    public void updateAlipay(String userId, String username, String qrcodeId) {
        long count = platUserDao.updateAlipay(userId, username, qrcodeId);
        if (count == 0) {
            throw new PlatException(Constants.UPDATE_ERROR, "更新失败");
        }
    }

    @Override
    @Transactional
    public void updateWechat(String userId, String username, String qrcodeId) {
        long count = platUserDao.updateWechatPay(userId, username, qrcodeId);
        if (count == 0) {
            throw new PlatException(Constants.UPDATE_ERROR, "更新失败");
        }
    }

    @Override
    public List<PlatUser> findByIdcardStatus(List<Integer> status) {
        if (status == null || status.size() == 0) {
            return null;
        }
        return platUserDao.findByIdcardStatus(Joiner.on(",").join(status));
    }

    @Override
    public Long findByImages(String imageName) {
        return platUserDao.findExistByImages(imageName);
    }

    @Transactional
    @Override
    public void userCardStatuScanCheck(CardStatuScanCheckDTO cardStatuScanCheckDTO) {
        List<PlatUser> platUsers = platUserDao.findByNeedCardStatusChech(cardStatuScanCheckDTO.getContryCode(), cardStatuScanCheckDTO.getCardLevel(),
                cardStatuScanCheckDTO.getCardStatus(), cardStatuScanCheckDTO.getCardStatusCheckTime());
        if (CollectionUtils.isNotEmpty(platUsers)) {
            platUsers.stream().filter(platUser -> StringUtils.isNotBlank(platUser.getIdCard()) && StringUtils.isNotBlank(platUser.getRealName())).forEach(platUser -> {
                //调用阿里接口认证
                PlatUser updatePlatUser = new PlatUser();
                updatePlatUser.setId(platUser.getId());
                try {
                    SyncCardCheck cardCheck = new SyncCardCheck(cardStatuScanCheckDTO.getAppKey(), cardStatuScanCheckDTO.getAppSecret());
                    boolean body = cardCheck.safrv_cert_checkBool(cardStatuScanCheckDTO.getUserId(), cardStatuScanCheckDTO.getVerifyKey(),
                            platUser.getRealName(), platUser.getIdCard(),
                            platUser.getId());
                    if (body) {
                        //阿里云审核通过
                        updatePlatUser.setCardLevel(CardStatusEnum.CARD_STATUS_ONE.getCode());
                        updatePlatUser.setCardStatus(Integer.parseInt(UserCardStatusEnum.PASS.getCode()));
                        //如果用户照片上传
                        if (StringUtils.isNotBlank(platUser.getCardDownId()) && StringUtils.isNotBlank(platUser.getCardUpId())) {
                            //v2审核中
                            updatePlatUser.setCardStatus(Integer.parseInt(UserCardStatusEnum.TWO_APPLY.getCode()));
                        }
                    } else {
                        updatePlatUser.setCardStatusCheckTime(platUser.getCardStatusCheckTime() == null ? 1 : (platUser.getCardStatusCheckTime() + 1));
                        updatePlatUser.setCardStatus(Integer.parseInt(UserCardStatusEnum.NO_PASS.getCode()));
                        updatePlatUser.setCardStatusReason("身份审核不通过");
                        PlatUserOplog platUserOplog = new PlatUserOplog();
                        platUserOplog.setType(PlatUserOplogTypeEnum.IDCARD_CHECH_AUTH.getCode());
                        platUserOplog.setContent("身份证号:" + platUser.getIdCard() + ",真实姓名:" + platUser.getRealName() + ",阿里云身份审核不通过.");
                        platUserOplog.setCreateBy(platUser.getId());
                        platUserOplog.setCreateByName("plat");
                        platUserOplog.setCreateDate(LocalDateTime.now());
                        platUserOplog.setMail(platUser.getMail());
                        platUserOplog.setMobile(platUser.getMobile());
                        platUserOplog.setRealName(platUser.getRealName());
                        platUserOplog.setReason("阿里云身份审核不通过.");
                        platUserOplog.setUserId(platUser.getId());
                        platUserOplog.setId(SnowFlake.createSnowFlake().nextIdString());
                        platUserOplog.setUpdateDate(LocalDateTime.now());
                        platUserOplog.setUpdateByName("plat");
                        platUserOplog.setUpdateBy(platUser.getId());
                        platUserOplogDao.insert(platUserOplog);
                    }
                    logger.info("用户id:{},阿里云身份认证结果result:{}", platUser.getId(), body);

                } catch (UnsupportedEncodingException e) {
                    updatePlatUser.setCardStatusCheckTime(platUser.getCardStatusCheckTime() == null ? 1 : (platUser.getCardStatusCheckTime() + 1));
                    PlatUserOplog platUserOplog = new PlatUserOplog();
                    platUserOplog.setType(PlatUserOplogTypeEnum.IDCARD_CHECH_AUTH.getCode());
                    platUserOplog.setContent("身份证号:" + platUser.getIdCard() + ",真实姓名:" + platUser.getRealName() + ",阿里云身份证审核api调用失败");
                    platUserOplog.setCreateBy(platUser.getId());
                    platUserOplog.setCreateByName("plat");
                    platUserOplog.setCreateDate(LocalDateTime.now());
                    platUserOplog.setMail(platUser.getMail());
                    platUserOplog.setMobile(platUser.getMobile());
                    platUserOplog.setRealName(platUser.getRealName());
                    platUserOplog.setReason("阿里云api调用失败");
                    platUserOplog.setUserId(platUser.getId());
                    platUserOplog.setId(SnowFlake.createSnowFlake().nextIdString());
                    platUserOplog.setUpdateDate(LocalDateTime.now());
                    platUserOplog.setUpdateByName("plat");
                    platUserOplog.setUpdateBy(platUser.getId());
                    platUserOplogDao.insert(platUserOplog);
                    logger.error("阿里云身份证审核api,error:{}", e);
                }
                updatePlatUser.setUpdateDate(LocalDateTime.now());
                platUserDao.updateById(updatePlatUser);

                //清空用户的缓存


            });
        }
    }

    public String getRandomInvoteCode(int length)
    {
        String val = "";

        Random random = new Random();
        for(int i = 0; i < length; i++)
        {
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num"; // 输出字母还是数字

            if("char".equalsIgnoreCase(charOrNum)) // 字符串
            {
                int choice = random.nextInt(2) % 2 == 0 ? 65 : 97; //取得大写字母还是小写字母
                val += (char) (choice + random.nextInt(26));
            }
            else if("num".equalsIgnoreCase(charOrNum)) // 数字
            {
                val += String.valueOf(random.nextInt(10));
            }
        }

        return val;
    }

    @Override
    public PlatUser findByInviteCode(String inviteCode){
        return platUserDao.findByInviteCode(inviteCode);
    }

}
