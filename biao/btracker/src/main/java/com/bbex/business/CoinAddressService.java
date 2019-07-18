package com.bbex.business;

import com.azazar.bitcoin.jsonrpcclient.BitcoinJSONRPCClient;
import com.biao.entity.AddressConfig;
import com.biao.entity.Coin;
import com.biao.entity.CoinAddress;
import com.biao.mapper.AddressConfigDao;
import com.biao.mapper.CoinAddressDao;
import com.biao.mapper.CoinDao;
import com.bbex.util.RpcClient;
import com.bbex.util.SnowFlake;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Service
public class CoinAddressService {

    private static Logger logger = LoggerFactory.getLogger(CoinAddressService.class);
    BitcoinJSONRPCClient client = null;


    @Autowired
    private CoinDao coinDao;

    @Autowired
    private CoinAddressDao coinAddressDao;


    @Autowired
    private AddressConfigDao addressConfigDao;

    @Value("${symbolUrl}")
    private String symbolUrl;

    @Transactional
    public void executeAddress(String symbol) throws Exception {
        try {
            AddressConfig addressConfig = addressConfigDao.findByName(symbol);
            if(Objects.isNull(addressConfig)) return;
            if(addressConfig.getStatus()==0) return;
            List<CoinAddress> addressesList = new ArrayList<>(100);
            Coin coin = coinDao.findByName(symbol);
            if (Objects.isNull(coin)) return;
            client = RpcClient.getClient(symbolUrl);
            for (int i = 0; i < 10; i++) {
                CoinAddress coinAddress = getByCoin(coin);
                String address = client.getNewAddress();
                if (StringUtils.isEmpty(address)) continue;
                coinAddress.setAddress(address);
                coinAddress.setId(SnowFlake.createSnowFlake().nextIdString());
                addressesList.add(coinAddress);
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
        coinAddress.setType(1);//0:eth 1:btc 2:ltc 3:usdt
        coinAddress.setCoinId(coin.getId());
        coinAddress.setSymbol(coin.getName().toUpperCase());
        return coinAddress;
    }
}
