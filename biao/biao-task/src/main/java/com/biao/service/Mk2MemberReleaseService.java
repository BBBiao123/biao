package com.biao.service;

import com.biao.entity.mk2.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface Mk2MemberReleaseService {
    void releaseLockVolume();

    void releaseCommonMemberLock(Mk2PopularizeCommonMember commonMember, Mk2PopularizeMemberRule rule);

    void commonMemberUpdateBBvolume(Mk2PopularizeMemberRule rule);

    void saveTaskLog(LocalDateTime taskDate, BigDecimal releaseVolume);
}
