package com.biao.service;

import com.biao.entity.relay.MkRelayPrizeCandidate;
import com.biao.entity.relay.MkRelayPrizeConfig;
import com.biao.entity.relay.MkRelayRemitLog;
import com.biao.pojo.RequestQuery;
import com.biao.pojo.ResponsePage;

import java.util.List;
import java.util.Map;

public interface RelayPrizeService {

    Map<String, Object> getData();

    ResponsePage<MkRelayPrizeCandidate> findPage(RequestQuery requestQuery);

    ResponsePage<MkRelayRemitLog> findWinnersPage(RequestQuery requestQuery);

    List<MkRelayPrizeCandidate> findCandidateList();

    MkRelayPrizeConfig findRelayPrize();

}
