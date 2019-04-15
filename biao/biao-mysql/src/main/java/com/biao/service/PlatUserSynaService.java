package com.biao.service;

import com.biao.entity.PlatUserSyna;

import java.util.List;

public interface PlatUserSynaService {

    void batchHandler(List<PlatUserSyna> platUserSynas);

    void synUserToPlatUser();

    void synEgoToPlatUser();

    void sendMessageToSynUser();

    void batchCreateSynUser();
}
