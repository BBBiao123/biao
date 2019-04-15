package com.biao.service.impl;

import com.biao.entity.MkMinerRecruit;
import com.biao.entity.PlatUser;
import com.biao.entity.UserCoinVolume;
import com.biao.mapper.MkMinerRecruitDao;
import com.biao.pojo.RequestQuery;
import com.biao.pojo.ResponsePage;
import com.biao.service.MinerRecruitService;
import com.biao.service.UserCoinVolumeExService;
import com.biao.util.SnowFlake;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class MinerRecruitServiceImpl implements MinerRecruitService {

    @Autowired
    private MkMinerRecruitDao mkMinerRecruitDao;

    @Autowired(required = false)
    private UserCoinVolumeExService userCoinVolumeService;

    @Override
    public long recruitAdd(PlatUser platUser, UserCoinVolume volume) {

        MkMinerRecruit mkMinerRecruit = new MkMinerRecruit();
        mkMinerRecruit.setId(SnowFlake.createSnowFlake().nextIdString());
        mkMinerRecruit.setUserId(platUser.getId());
        mkMinerRecruit.setMail(platUser.getMail());
        mkMinerRecruit.setMobile(platUser.getMobile());
        mkMinerRecruit.setRealName(platUser.getRealName());
        mkMinerRecruit.setVolume(volume.getVolume());
        mkMinerRecruit.setIsStandard("0");
        mkMinerRecruit.setReachNumber(0);
        mkMinerRecruit.setInviteNumber(0);
        mkMinerRecruit.setRemark(String.format("矿工招募，UES持币[%s]", String.valueOf(volume.getVolume())));
        return mkMinerRecruitDao.insert(mkMinerRecruit);
    }

    @Override
    public MkMinerRecruit findUserId(String userId) {
        return mkMinerRecruitDao.findByUserId(userId);
    }

    @Override
    public ResponsePage<MkMinerRecruit> findPage(RequestQuery requestQuery) {
        ResponsePage<MkMinerRecruit> responsePage = new ResponsePage<>();
        Page<MkMinerRecruit> page = PageHelper.startPage(requestQuery.getCurrentPage(), requestQuery.getShowCount());
        List<MkMinerRecruit> data = mkMinerRecruitDao.findList();

        data.forEach(mkMinerRecruit -> {

            if (!StringUtils.isEmpty(mkMinerRecruit.getMail())) {
                mkMinerRecruit.setMail(this.maskOff(mkMinerRecruit.getMail()));
            }

            if (!StringUtils.isEmpty(mkMinerRecruit.getMobile())) {
                mkMinerRecruit.setMobile(this.maskOff(mkMinerRecruit.getMobile()));
            }

            if (StringUtils.isEmpty(mkMinerRecruit.getMail())) {
                mkMinerRecruit.setMail(mkMinerRecruit.getMobile());
            }

            if (!StringUtils.isEmpty(mkMinerRecruit.getRealName())) {
                String realName = mkMinerRecruit.getRealName();
                String firstName = realName.substring(0, 1);
                String endName = realName.substring(realName.length() - 1, realName.length());
                mkMinerRecruit.setRealName(firstName.concat("*").concat(endName));
            }

            if (!ObjectUtils.isEmpty(mkMinerRecruit.getInviteNumber()) && mkMinerRecruit.getInviteNumber() >= 30) {
                mkMinerRecruit.setIsInviteNumber("1");
            } else {
                mkMinerRecruit.setIsInviteNumber("0");
            }

            if (!ObjectUtils.isEmpty(mkMinerRecruit.getReachNumber()) && mkMinerRecruit.getReachNumber() >= 30) {
                mkMinerRecruit.setIsReachNumber("1");
            } else {
                mkMinerRecruit.setIsReachNumber("0");
            }
        });
        responsePage.setCount(page.getTotal());
        responsePage.setList(data);
        return responsePage;
    }

    private String maskOff(String username) {
        if (StringUtils.isEmpty(username)) return "";

        String usernameTmp = username;
        String tail = "";
        if (username.indexOf("@") > 0) {
            usernameTmp = username.substring(0, username.indexOf("@"));
            tail = username.substring(username.indexOf("@"), username.length());
        }

        String startStr = "";
        String endStr = "";
        if (usernameTmp.length() > 7) {
            startStr = usernameTmp.substring(0, 3);
            endStr = usernameTmp.substring(usernameTmp.length() - 4, usernameTmp.length());
        } else {
            startStr = usernameTmp.substring(0, 1);
            endStr = usernameTmp.substring(usernameTmp.length() - 2, usernameTmp.length());
        }

        return startStr.concat("****").concat(endStr).concat(tail);
    }
}
