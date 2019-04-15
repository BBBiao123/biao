package com.biao.service;

import com.biao.entity.AppVersion;

import java.util.List;

public interface AppVersionService {
    List<AppVersion> findAll();

    AppVersion getLastestByType(String type);
}
