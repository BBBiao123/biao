package com.biao.init;

import com.biao.wallet.contract.LoomContractEvent;
import com.biao.wallet.ethereum.Filter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import java.io.IOException;

import static com.biao.constant.Constant.TOKEN_ADDRESS_MAP;
import static com.biao.constant.Constant.TOKEN_MAP;

/**
 *  ""(""611 @ qq.com)
 */
@Component
@SuppressWarnings("unchecked")
public class InitService implements CommandLineRunner {

    private static Logger logger = LoggerFactory.getLogger(InitService.class);
    private final LoomContractEvent loomContractEvent;

    @Autowired
    private Filter filter;


    @Autowired
    public InitService(LoomContractEvent loomContractEvent) {
        this.loomContractEvent = loomContractEvent;
    }

    @Override
    public void run(String... args) throws IOException {

        Web3j web3j = Web3j.build(new HttpService(Environment.RPC_URL));
        filter.newTransactionFilter(web3j);
        String symbol = "SIXEXTOKEN";
        loomContractEvent.ContractFilter(web3j,symbol,TOKEN_ADDRESS_MAP.get(symbol), TOKEN_MAP.get(symbol).getDecimals());
    }
}
