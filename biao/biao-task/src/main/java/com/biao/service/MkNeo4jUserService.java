package com.biao.service;

import java.time.LocalDateTime;

public interface MkNeo4jUserService {
    void initNeo4jUser();
    void repairMissNeo4jUser(LocalDateTime beginTime);
    void repairMissNeo4jEveryDay();
    void repairMissNeo4jOnce();
}
