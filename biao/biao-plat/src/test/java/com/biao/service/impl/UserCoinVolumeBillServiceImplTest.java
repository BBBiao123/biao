package com.biao.service.impl;

import com.biao.BaseTest;
import com.biao.enums.UserCoinVolumeEventEnum;
import com.biao.pojo.UserCoinVolumeBillDTO;
import com.biao.service.UserCoinVolumeBillService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

/**
 * UserCoinVolumeBillServiceImplTest.
 * <p>
 * <p>
 * 19-1-3下午2:29
 *
 *  "" sixh
 */
public class UserCoinVolumeBillServiceImplTest extends BaseTest {

    @Autowired
    UserCoinVolumeBillService userCoinVolumeBillService;

    @Test
   public void testInsert() {
        UserCoinVolumeBillDTO dto = new UserCoinVolumeBillDTO();
        dto.setCoinSymbol("BTC");
        dto.setUserId("1001");
        dto.setForceLock(false);
        dto.setMark("测试");
        dto.setOpLockVolume(BigDecimal.valueOf(20));
        dto.setOpVolume(BigDecimal.ZERO);
        UserCoinVolumeEventEnum[] emst = {
                UserCoinVolumeEventEnum.ADD_VOLUME,
                UserCoinVolumeEventEnum.SUB_LOCKVOLUME
        };
        dto.setOpSign(emst);
        dto.setSource("测试");
        dto.setRefKey("123a7489");
        userCoinVolumeBillService.insert(dto);
    }
}
