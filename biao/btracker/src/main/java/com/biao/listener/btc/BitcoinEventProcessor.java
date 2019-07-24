package com.biao.listener.btc;

import com.azazar.bitcoin.jsonrpcclient.*;
import com.biao.business.CoinAddressService;
import com.biao.business.DepositService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import sun.rmi.runtime.Log;

import java.net.MalformedURLException;
import java.util.HashSet;
@Component
public class BitcoinEventProcessor {
    private static Logger logger = LoggerFactory.getLogger(BitcoinEventProcessor.class);
    @Autowired
    DepositService depositService;
    @Autowired
    CoinAddressService coinAddressService;

    @Value("${symbolUrl}")
    private String symbolUrl;


    public void receiveCoins() throws BitcoinException, MalformedURLException {
        final Bitcoin bitcoin = new BitcoinJSONRPCClient(symbolUrl);
        final BitcoinAcceptor acceptor = new BitcoinAcceptor(bitcoin);

        //System.out.println("Send bitcoins to " + bitcoin.getNewAddress("NewAccount"));

        acceptor.addListener(new ConfirmedPaymentListener(2) {
            HashSet processed = new HashSet();

            @Override
            public void confirmed(Bitcoin.Transaction transaction) {
                logger.info("监听交易： " +transaction.txId() );
                if (!processed.add(transaction.txId()))
                    return; // already processed
                logger.info("--------------------------------------过滤后的交易  txid: " + transaction.txId() +" amount : " + transaction.amount());
                if(transaction.amount() <=0) return;
                if(transaction.amount() <=0.0002) return;
                logger.info("--------------------------------------进入充值服务");
                depositService.executeDeposit(transaction);
                logger.info("Payment received, amount: " + transaction.amount() + "; account: " + transaction.account());

            }
        });
        acceptor.run();

    }



}