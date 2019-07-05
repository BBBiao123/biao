package com.biao.task.execute;

import com.biao.business.BlockNumberService;
import com.biao.business.CoinAddressService;
import com.biao.business.DepositService;
import com.biao.business.WithdrawService;
import com.biao.entity.DepositLog;
import com.biao.entity.WithdrawLog;
import com.biao.util.CheckUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Component
public class ScheduledTasks {

    private static Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);
    @Autowired
    private CoinAddressService coinAddressService;

    @Autowired
    private BlockNumberService blockNumberService;
    @Autowired
    private WithdrawService withdrawService;
    @Autowired
    private DepositService depositService;
    @Value("${generateAddress}")
    private String generateAddress;


    /**
     * 生产eth 地址
     */
    @Scheduled(fixedRate = 1000 * 60 * 5)
    public void generateEthAddress() {
        logger.info("generateEthAddress  start ....");
        try {
            if (!generateAddress.equals("1")) return;
            coinAddressService.executeAddress("ETH");
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("generateEthAddress   end   ....");
    }

    /**
     * 确认区块高度 并且更新用户资产
     */
    @Scheduled(fixedRate = 1000 * 60 * 5)
    public void confirmEthBlockNumber() {
        logger.info("confirm eth blockNumber  start ....");
        try {
            blockNumberService.confirmEthBlockNumber();
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("confirm eth blockNumber end  ....");
    }





    /**
     * 确认提现是否成功
     */
    @Scheduled(fixedRate = 1000 * 60 * 12)
    public void confirmWithdrawTxStatus() {
        logger.info("confirm erc20 withdraw status  start ....");
        try {
            List<WithdrawLog> list = withdrawService.findWithdrawLogByConfirmStatus(0, "1");
            if (CollectionUtils.isEmpty(list)) return;
            list.forEach(withdrawLog -> {
                try {
                    Thread.sleep(3000);
                    withdrawService.executeConfirmStatus(withdrawLog);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("confirm erc20 withdraw status end  ....");
    }

    @Scheduled(fixedRate = 1000 * 60 * 2)
    public void resendERCToken() {
        logger.info("resend err20  start ....");
        try {
            List<WithdrawLog> list = withdrawService.findWithdrawLogByConfirmStatus(3, "1");//btc 0 ltc 0 eth 0  erc20 1 usdt 5
            if (CollectionUtils.isEmpty(list)) return;
            list.forEach(withdrawLog -> {
                try {
                    withdrawService.resendERCToken(withdrawLog);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("resend err20  end ....");
    }


    @Scheduled(fixedRate = 1000 * 60 * 2)
    public void confirmWithdrawERC20() {
        logger.info("confirm eth erc20  withdraw  start ....");
        try {
            List<WithdrawLog> list = withdrawService.findErc20WithdrawLog();
            logger.info("提現記錄條數 ： "+String.valueOf(list.size()));
            if (CollectionUtils.isEmpty(list)) return;
            list.forEach(withdrawLog -> {
                try {
                    logger.info("提現start：-- "+ withdrawLog.getAddress());
                    if (!CheckUtil.checkEthAddress(withdrawLog.getAddress())) return;
                    logger.info("-- ");
                    withdrawService.executeWithdrawERC20(withdrawLog);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("confirm eth erc20  withdraw end  ....");
    }

    @Scheduled(fixedRate = 1000 * 60 * 5)
    public void confirmWithdrawETH() {
        logger.info("confirm eth withdraw  start ....");
        try {
            List<WithdrawLog> list = withdrawService.findETHWithdrawLog();
            if (CollectionUtils.isEmpty(list)) return;
            list.forEach(withdrawLog -> {
                try {
                    if (!CheckUtil.checkEthAddress(withdrawLog.getAddress().trim())) return;
                    Thread.sleep(3000L);
                    withdrawService.executeWithdrawETH(withdrawLog);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("confirm eth withdraw end  ....");
    }




    /**
     * ETH 归集 1是归集中 2归集完成
     */
    @Scheduled(cron = "0 0/5 * * * ?")
    public void raiseDepositLog() {
        logger.info(" eth token  collect start ....");
        try {
            List<DepositLog> ethDepositLogList = depositService.findETHDepositLog();
            if (CollectionUtils.isEmpty(ethDepositLogList)) {
                return;
            }
            ethDepositLogList.forEach(depositLog -> {
                try {
                    depositService.executeETHRaise(depositLog);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info(" eth token collect end  ....");
    }



    /**
     * ETH erc20 归集 1是归集中 2归集完成 9 归集失败
     */
    @Scheduled(cron = "0 0/5 * * * ?")
    public void raiseErc20DepositLog() {
        logger.info(" eth erc20 token  collect start ....");
        try {
            List<DepositLog> erc20DepositLogList = depositService.findErc20DepositLog();
            logger.info("查找充值記錄條數： " + erc20DepositLogList.size());
            if (CollectionUtils.isEmpty(erc20DepositLogList)) {
                return;
            }
            erc20DepositLogList.forEach(depositLog -> {
                try {
                    depositService.executeErc20Raise(depositLog);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info(" eth erc20 token collect end  ....");
    }

}
