package com.biao.service;

import com.biao.entity.MkRedEnvelopeConf;
import com.biao.pojo.ResponsePage;
import com.biao.vo.*;

public interface OfflineRedEnvelopeService {

    RedEnvelopeViewVO send(RedEnvelopeSendVO redEnvelopeSendVO);

    RedEnvelopeViewVO open(RedEnvelopeViewVO redEnvelopeViewVO);

    RedEnvelopeViewVO view(RedEnvelopeViewVO redEnvelopeViewVO);

    RedEnvelopeDetailVO detail(String id, String userId);

    RedEnvelopeMySendVO findMySendInfo(RedEnvelopeListVO redEnvelopeListVO);

    ResponsePage<RedEnvelopeViewVO> findMySendList(RedEnvelopeListVO redEnvelopeListVO);

    RedEnvelopeMyReceiveVO findMyReceiveInfo(RedEnvelopeListVO redEnvelopeListVO);

    ResponsePage<RedEnvelopeSubViewVO> findMyReceiveList(RedEnvelopeListVO redEnvelopeListVO);

    MkRedEnvelopeConf getRedEnvelopeConf();

    void handleExpired();

}
