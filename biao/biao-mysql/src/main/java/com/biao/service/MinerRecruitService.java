package com.biao.service;

import com.biao.entity.MkMinerRecruit;
import com.biao.entity.PlatUser;
import com.biao.entity.UserCoinVolume;
import com.biao.pojo.RequestQuery;
import com.biao.pojo.ResponsePage;

public interface MinerRecruitService {

    long recruitAdd(PlatUser platUser, UserCoinVolume volume);

    MkMinerRecruit findUserId(String userId);

    ResponsePage<MkMinerRecruit> findPage(RequestQuery requestQuery);

}
