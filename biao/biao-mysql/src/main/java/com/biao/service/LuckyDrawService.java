package com.biao.service;

import com.biao.entity.PlatUser;
import com.biao.entity.lucky.MkLuckyDrawPlayer;
import com.biao.pojo.RequestQuery;
import com.biao.pojo.ResponsePage;

import java.util.Map;

public interface LuckyDrawService {

    Map<String, Object> getData();

    void in(PlatUser platUser);

    ResponsePage<MkLuckyDrawPlayer> findPage(RequestQuery requestQuery);

    ResponsePage<MkLuckyDrawPlayer> findMyPage(RequestQuery requestQuery, PlatUser platUser);

}
