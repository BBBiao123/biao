package com.biao.mysql;

import com.biao.BaseTest;
import com.biao.entity.UserCoinVolume;
import com.biao.mapper.UserCoinVolumeDao;
import com.biao.util.SnowFlake;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 *  ""(""611 @ qq.com)
 */
public class MysqlTransactionTest extends BaseTest {

    @Autowired
    private UserCoinVolumeDao userCoinVolumeDao;

    @Test
    public void test() {

        UserCoinVolume userCoinVolume = new UserCoinVolume();
        userCoinVolume.setLockVolume(BigDecimal.ZERO);
        userCoinVolume.setCoinSymbol("USDT");
        userCoinVolume.setVolume(new BigDecimal(1.0));
        userCoinVolume.setCreateDate(LocalDateTime.now());
        userCoinVolume.setUpdateDate(LocalDateTime.now());
        userCoinVolume.setUserId("1991951");
        userCoinVolume.setCoinId("1");
        userCoinVolume.setId(SnowFlake.createSnowFlake().nextIdString());
        userCoinVolumeDao.insert(userCoinVolume);

    }

}
