package com.bbex.task.execute;

import com.azazar.bitcoin.jsonrpcclient.BitcoinJSONRPCClient;
import com.bbex.business.CoinAddressService;
import com.bbex.business.WithdrawService;
import com.bbex.entity.WithdrawLog;
import com.bbex.listener.btc.BitcoinEventProcessor;
import com.bbex.util.CheckUtil;
import com.bbex.util.RpcClient;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ScheduledTasks {

    private static Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);
    @Autowired
    private CoinAddressService coinAddressService;
    @Autowired
    private WithdrawService withdrawService;

    BitcoinJSONRPCClient client = null;
    @Value("${symbol}")
    private String symbol;
    @Value("${generateAddress}")
    private String generateAddress;
    @Value("${symbolUrl}")
    private String symbolUrl;
    private boolean flag = false;

    /**
     * 生产btc 地址10分钟1次
     */
    @Scheduled(fixedRate = 1000*60*10)
    public void generateBtcAddress() {
        logger.info("generate {} address  start ....",symbol);
        try {
            if (!generateAddress.equals("1")) return;
            coinAddressService.executeAddress(symbol);
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("generate {} address  end   ....",symbol);
    }


    /**
     * 1分钟执行1次
     */
    @Scheduled(fixedRate = 1000*60*1)
    public void confirmWithdraw() {
        logger.info("confirm btc Withdraw  start ....");
        try {
            List<WithdrawLog> list = withdrawService.findWithdrawLogBySymbol(symbol);
            if (CollectionUtils.isEmpty(list)) return;
            list.forEach(withdrawLog -> {
                try {
                    client = RpcClient.getClient(symbolUrl);
                    boolean flag = client.validateAddress(withdrawLog.getAddress().trim()).isValid();
                    if(!flag) {
                        logger.error("提币地址非法{}",withdrawLog.getAddress());
                        return;
                    }
                    //if (!CheckUtil.checkBtcAddress(withdrawLog.getAddress())) return;
                    withdrawService.executeWithdraw(withdrawLog);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("confirm btc Withdraw end  ....");
    }

    public static void main(String[] args) {
        System.out.println(CheckUtil.checkBtcAddress("3A8utVy134xyJpV5GW2owjTYrDVHDhUBgY"));
    }

}
