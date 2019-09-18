package com.biao.service;

import com.biao.entity.PlatUser;
import com.biao.entity.PlatUserSyna;
import com.biao.entity.Sysdict;
import com.biao.pojo.CardStatuScanCheckDTO;
import com.biao.pojo.ResponsePage;
import com.biao.query.UserLoginLogQuery;

import java.util.List;

public interface PlatUserService {

    String registerPlatUser(PlatUser platUser, boolean isValidInvoteCode);

    PlatUser synUser(PlatUser platUser, PlatUserSyna platUserSyna);

    PlatUser findById(String id);

    void updateCardById(PlatUser platUser);

    /**
     * 通过邮箱或者手机号获取用户
     *
     * @param mailOrMobile
     * @return
     */
    PlatUser findByLoginName(String mailOrMobile);

    /**
     * 查询用户邀请的用户
     *
     * @param query
     * @return
     */
    ResponsePage<PlatUser> findInvitesById(UserLoginLogQuery query);

    void updateExpasswordById(PlatUser platUser);

    /**
     * 通过用户名获取用户
     *
     * @param username
     * @return
     */
    PlatUser findByUsername(String username);

    void updateById(PlatUser platUser);

    void updatePassword(PlatUser platUser);
    
    void updateNickNameById(PlatUser platUser);

    void updateGoogleAuthById(PlatUser platUser);

    PlatUser resetPassword(PlatUser platUser);

    PlatUser resetPasswordByMobile(PlatUser platUser);

    void updateAlipay(String userId, String username, String qrcodeId);

    void updateWechat(String userId, String username, String qrcodeId);

    List<PlatUser> findByIdcardStatus(List<Integer> status);

    Long findByImages(String imageName);
    
    void userCardStatuScanCheck(CardStatuScanCheckDTO cardStatuScanCheckDTO);

    PlatUser findByInviteCode(String inviteCode);

    List<Sysdict> findBySysdictType();
}
