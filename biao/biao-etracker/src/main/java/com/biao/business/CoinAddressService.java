package com.biao.business;

import com.biao.entity.AddressConfig;
import com.biao.entity.Coin;
import com.biao.entity.CoinAddress;
import com.biao.mapper.AddressConfigDao;
import com.biao.mapper.CoinAddressDao;
import com.biao.mapper.CoinDao;
import com.biao.mapper.PlatUserDao;
import com.biao.util.SnowFlake;
import com.biao.wallet.ethereum.TransactionClient;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class CoinAddressService {

    private static Logger logger = LoggerFactory.getLogger(CoinAddressService.class);
    @Autowired
    CoinDao coinDao;

    @Autowired
    CoinAddressDao coinAddressDao;

    @Autowired
    PlatUserDao platUserDao;

    @Autowired
    private AddressConfigDao addressConfigDao;

    //1:基于以太  2:基于量子 3：基于小蚂 4：基于EOS
    public void executeAddress(String symbol) throws Exception {
        try {
            AddressConfig addressConfig = addressConfigDao.findByName("ETH");
            if (Objects.isNull(addressConfig)) return;
            if (addressConfig.getStatus() == 0) return;
            List<CoinAddress> addressesList = new ArrayList<>(10);
            Coin coin = coinDao.findByName(symbol);
            if (!Objects.isNull(coin)) {
                if (symbol.equals("ETH")) {
                    for (int i = 0; i < 10; i++) {
                        CoinAddress coinAddress = getByCoin(coin);
                        String address = TransactionClient.createNewAccount();
                        logger.info("new eth address{}",address);
                        if (StringUtils.isEmpty(address)) continue;
                        coinAddress.setAddress(address);
                        addressesList.add(coinAddress);
                    }
                } else {
                    logger.info("没有该种币的符号");
                }
            }
            //此处写持久层逻辑
            coinAddressDao.batchInsert(addressesList);
        } catch (Exception e) {
            logger.error("出现异常，事务回滚", e);
            throw new Exception("生成地址失败");
        }
    }

    private CoinAddress getByCoin(Coin coin) {
        CoinAddress coinAddress = new CoinAddress();
        coinAddress.setStatus("0");
        coinAddress.setUserId("0");
        coinAddress.setCreateDate(LocalDateTime.now());
        coinAddress.setUpdateDate(LocalDateTime.now());
        coinAddress.setType(0);//如果是eth 则是0 否则就是1
        coinAddress.setCoinId(coin.getId());
        coinAddress.setSymbol("ETH");
        coinAddress.setId(SnowFlake.createSnowFlake().nextIdString());
        return coinAddress;
    }
}
