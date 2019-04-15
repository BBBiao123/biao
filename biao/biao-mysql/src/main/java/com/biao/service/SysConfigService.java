package com.biao.service;

import com.biao.entity.SysConfig;

import java.util.List;

public interface SysConfigService {
    List<SysConfig> findAll();

    SysConfig getOne();
}
