package com.biao.service.impl;

import com.biao.entity.SuperBook;
import com.biao.entity.SuperBookConf;
import com.biao.entity.mk2.Mk2PopularizeMiningGiveCoinLog;
import com.biao.entity.mk2.Mk2PopularizeMiningTeamConf;
import com.biao.entity.mk2.Mk2PopularizeMiningTeamSort;
import com.biao.mapper.SuperBookConfDao;
import com.biao.mapper.SuperBookDao;
import com.biao.mapper.mk2.Mk2PopularizeMiningGiveCoinLogDao;
import com.biao.mapper.mk2.Mk2PopularizeMiningTeamSortDao;
import com.biao.pojo.RequestQuery;
import com.biao.pojo.ResponsePage;
import com.biao.service.Mk2MiningService;
import com.biao.vo.SuperBookVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
public class Mk2MiningServiceImpl implements Mk2MiningService {

    @Autowired
    private Mk2PopularizeMiningTeamSortDao mk2PopularizeMiningTeamSortDao;

    @Autowired
    private Mk2PopularizeMiningGiveCoinLogDao mk2PopularizeMiningGiveCoinLogDao;

    @Autowired
    private SuperBookConfDao superBookConfDao;

    @Autowired
    private SuperBookDao superBookDao;

    public Mk2PopularizeMiningTeamConf findConf() {
        Mk2PopularizeMiningTeamConf conf = mk2PopularizeMiningTeamSortDao.findConf();
        if (Objects.isNull(conf)) {
            conf = new Mk2PopularizeMiningTeamConf();
            conf.setShow("0");
        }
        conf.setSortBeginDate(null);
        conf.setSortEndDate(null);
        return conf;
    }

    @Override
    public ResponsePage<Mk2PopularizeMiningTeamSort> findAll(RequestQuery requestQuery) {
        ResponsePage<Mk2PopularizeMiningTeamSort> responsePage = new ResponsePage<>();
        Page<Mk2PopularizeMiningTeamSort> page = PageHelper.startPage(requestQuery.getCurrentPage(), requestQuery.getShowCount());
        List<Mk2PopularizeMiningTeamSort> teamSorts = mk2PopularizeMiningTeamSortDao.findAll();
        teamSorts.forEach(e -> {
            e.setMobile(maskOff(e.getMobile()));
            e.setMail(maskOff(e.getMail()));
            e.setRealName(maskOff(e.getRealName()));
            e.setUserId(null);
            e.setVolumeStr(maskOffVolume(e.getVolume()));
            e.setVolume(null);
        });
        responsePage.setList(teamSorts);
        responsePage.setCount(page.getTotal());
        return responsePage;
    }

    private String maskOffVolume(BigDecimal volume) {
        if (Objects.isNull(volume)) {
            return null;
        }
        String vol = String.valueOf(volume.longValue());
        if (vol.length() > 2) {
            return vol.substring(0, 2) + "******";
        } else {
            return vol + "******";
        }
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
        } else if (usernameTmp.length() > 4) {
            startStr = usernameTmp.substring(0, 1);
            endStr = usernameTmp.substring(usernameTmp.length() - 2, usernameTmp.length());
        } else if (usernameTmp.length() > 0) {
            startStr = usernameTmp.substring(0, 1);
            return startStr.concat("**").concat(endStr).concat(tail);
        }
        return startStr.concat("****").concat(endStr).concat(tail);
    }

    public Mk2PopularizeMiningTeamSort findByUserId(String userId) {
        return mk2PopularizeMiningTeamSortDao.findByUserId(userId);
    }

    /**
     * 查询超级账本
     * @param superBookVO
     * @return
     */
    public ResponsePage<Mk2PopularizeMiningGiveCoinLog> findSuperBookList(SuperBookVO superBookVO) {
        ResponsePage<Mk2PopularizeMiningGiveCoinLog> responsePage = new ResponsePage<>();
        Page<Mk2PopularizeMiningGiveCoinLog> page = PageHelper.startPage(superBookVO.getCurrentPage(), superBookVO.getShowCount());
        List<Mk2PopularizeMiningGiveCoinLog> giveCoinLogs = mk2PopularizeMiningGiveCoinLogDao.findByAddress(superBookVO.getAddress());
        responsePage.setList(giveCoinLogs);
        responsePage.setCount(page.getTotal());
        return responsePage;
    }

    public SuperBookConf findSuperBookBySymbol(String symbol) {
       return superBookConfDao.findBySymbol(symbol); // 查询超级账本属性
    }

    public SuperBook getSuperBook(String userId, String coinSymbol) {
        return superBookDao.findByUserIdAndSymbol(userId, coinSymbol);
    }
}
