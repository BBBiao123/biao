package com.biao.wallet.ethereum;

import com.biao.business.DepositService;
import com.biao.init.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;
import rx.Subscription;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

/**
 * filter相关
 * 监听区块、交易
 * 所有监听都在Web3jRx中
 */
@Component
public class Filter {
    private static Logger logger = LoggerFactory.getLogger(Filter.class);
    private static Web3j web3j;
    @Autowired
    private DepositService depositService;

    public static void main(String[] args) {
        web3j = Web3j.build(new HttpService(Environment.RPC_URL));
        /**
         * 新区块监听
         */
        //newBlockFilter(web3j);
        /**
         * 新交易监听
         */
        new Filter().newTransactionFilter(web3j);
        /**
         * 遍历旧区块、交易
         */
        //replayFilter(web3j);
        /**
         * 从某一区块开始直到最新区块、交易
         */
        //catchUpFilter(web3j);

        /**
         * 取消监听
         */
        //subscription.unsubscribe();
    }

    private static void newBlockFilter(Web3j web3j) {
        Subscription subscription = web3j.
                blockObservable(false).
                subscribe(block -> {
                    System.out.println("new block come in");
                    System.out.println("block number" + block.getBlock().getNumber());
                });
    }

    public void newTransactionFilter(Web3j web3j) {
        Subscription subscription = web3j.
                transactionObservable().
                subscribe(transaction -> {
//                    logger.info("transaction come in");
                    BigDecimal volume = Convert.fromWei(new BigDecimal(transaction.getValue()), Convert.Unit.ETHER);
//                    logger.info("hash:" + transaction.getHash() + "from:" + transaction.getFrom() + "to:" + transaction.getTo() + "volume:" + volume);

                    //if(TOKEN_ADDRESS_MAP.get())
                    if (Objects.isNull(transaction.getTo())) return;
                    //充值
                    try {
                        if (transaction.getFrom().equals(Environment.fromAddress)) {
                            if (volume.compareTo(BigDecimal.valueOf(0.005)) <= 0) return;
                        }
                        depositService.executeDepositETH(transaction);
                    } catch (Exception e) {
                        logger.error("ETH充值保存失败:" + "hash:" + transaction.getHash() + "from:" + transaction.getFrom() + "to:" + transaction.getTo() + "volume:" + volume);
                        logger.error("e{}", e.getMessage());
                        e.printStackTrace();
                    }
                });
    }


    private static void replayFilter(Web3j web3j) {
        BigInteger startBlock = BigInteger.valueOf(2000000);
        BigInteger endBlock = BigInteger.valueOf(2010000);
        /**
         * 遍历旧区块
         */
        Subscription subscription = web3j.
                replayBlocksObservable(
                        DefaultBlockParameter.valueOf(startBlock),
                        DefaultBlockParameter.valueOf(endBlock),
                        false).
                subscribe(ethBlock -> {
                    System.out.println("replay block");
                    System.out.println(ethBlock.getBlock().getNumber());
                });

        /**
         * 遍历旧交易
         */
        Subscription subscription1 = web3j.
                replayTransactionsObservable(
                        DefaultBlockParameter.valueOf(startBlock),
                        DefaultBlockParameter.valueOf(endBlock)).
                subscribe(transaction -> {
                    System.out.println("replay transaction");
                    System.out.println("txHash " + transaction.getHash());
                });
    }

    private static void catchUpFilter(Web3j web3j) {
        BigInteger startBlock = BigInteger.valueOf(2000000);

        /**
         * 遍历旧区块，监听新区块
         */
        Subscription subscription = web3j.catchUpToLatestAndSubscribeToNewBlocksObservable(
                DefaultBlockParameter.valueOf(startBlock), false)
                .subscribe(block -> {
                    System.out.println("block");
                    System.out.println(block.getBlock().getNumber());
                });

        /**
         * 遍历旧交易，监听新交易
         */
        Subscription subscription2 = web3j.catchUpToLatestAndSubscribeToNewTransactionsObservable(
                DefaultBlockParameter.valueOf(startBlock))
                .subscribe(tx -> {
                    System.out.println("transaction");
                    System.out.println(tx.getHash());
                });
    }
}
