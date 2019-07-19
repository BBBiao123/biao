package com.bbex.listener.btc;

import com.azazar.bitcoin.jsonrpcclient.*;
import com.bbex.business.CoinAddressService;
import com.bbex.business.DepositService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
                if (!processed.add(transaction.txId()))
                    return; // already processed
                if(transaction.amount() <=0) return;
                if(transaction.amount() <=0.0002) return;
                depositService.executeDeposit(transaction);
                System.out.println("Payment received, amount: " + transaction.amount() + "; account: " + transaction.account());

            }
        });
        acceptor.run();

    }



}