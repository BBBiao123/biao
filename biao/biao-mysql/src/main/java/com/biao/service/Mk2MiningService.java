package com.biao.service;

import com.biao.entity.SuperBook;
import com.biao.entity.SuperBookConf;
import com.biao.entity.mk2.Mk2PopularizeMiningGiveCoinLog;
import com.biao.entity.mk2.Mk2PopularizeMiningTeamConf;
import com.biao.entity.mk2.Mk2PopularizeMiningTeamSort;
import com.biao.pojo.RequestQuery;
import com.biao.pojo.ResponsePage;
import com.biao.vo.SuperBookVO;

public interface Mk2MiningService {

    Mk2PopularizeMiningTeamConf findConf();

    ResponsePage<Mk2PopularizeMiningTeamSort> findAll(RequestQuery requestQuery);

    Mk2PopularizeMiningTeamSort findByUserId(String userId);

    ResponsePage<Mk2PopularizeMiningGiveCoinLog> findSuperBookList(SuperBookVO superBookVO);

    SuperBookConf findSuperBookBySymbol(String symbol);

    SuperBook getSuperBook(String userId, String coinSymbol);
}
