package com.biao.init;

import com.biao.util.StringUtil;
import com.biao.wallet.contract.LoomContractEvent;
import com.biao.wallet.ethereum.Filter;
import com.biao.wallet.ethereum.TokenClient;
import com.biao.wallet.ethereum.TransactionClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.thymeleaf.util.StringUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import static com.biao.constant.Constant.TOKEN_ADDRESS_MAP;
import static com.biao.constant.Constant.TOKEN_MAP;

/**
 * ""(""611 @ qq.com)
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
        logger.info("初始化-------------- ");
        Web3j web3j = Web3j.build(new HttpService(Environment.RPC_URL));
        filter.newTransactionFilter(web3j);
        String symbol = "MG";
        loomContractEvent.ContractFilter(web3j, symbol, TOKEN_ADDRESS_MAP.get(symbol), TOKEN_MAP.get(symbol).getDecimals());
        String symbol_bat = "BAT";
        loomContractEvent.ContractFilter(web3j, symbol_bat, TOKEN_ADDRESS_MAP.get(symbol_bat), TOKEN_MAP.get(symbol_bat).getDecimals());
        String symbol_zrx = "ZRX";
        loomContractEvent.ContractFilter(web3j, symbol_zrx, TOKEN_ADDRESS_MAP.get(symbol_zrx), TOKEN_MAP.get(symbol_zrx).getDecimals());
        String symbol_MTL = "MTL";
        loomContractEvent.ContractFilter(web3j, symbol_MTL, TOKEN_ADDRESS_MAP.get(symbol_MTL), TOKEN_MAP.get(symbol_MTL).getDecimals());

        //处理客户的转币需求
//        logger.info("——————————————————————————————————————开始处理 转账  ——————————————————————————————————————");
//        tranfer("0x63b8165760e3207782cd8fc77a3f67d45c28625c", "0x344787790b64dd5508e33991e3deff2c98320e7c",new BigDecimal("0.0011"));
//        try {
//            logger.info("——————————————————————————————————————等一分半分钟  ——————————————————————————————————————");
//            Thread.sleep(90000L);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        logger.info("——————————————————————————————————————开始处理 转账代币  ——————————————————————————————————————");
//        tranfer("0x344787790b64dd5508e33991e3deff2c98320e7c", "0x766244681cf86727707889db514af0a1131794db", "0x30dacf4500df587853661c232ff2cadbcbec6837",
//                "MG", new BigDecimal("3029943.4765"), 18);
    }

    public void tranfer(String fromAddress, String toAddress, String contractAddress, String symbol, BigDecimal count, Integer decimals) {
        String password = Environment.password;
        Web3j web3j = Web3j.build(new HttpService(Environment.RPC_URL));
        Admin admin = Admin.build(new HttpService(Environment.RPC_URL));

        logger.info("symbol:{},contractAddress:{}", symbol, contractAddress);
        //获取钱包代币余额
        BigInteger addrAmount = TokenClient.getTokenBalance(web3j, fromAddress, contractAddress);
        BigDecimal bdAmount = BigDecimal.ZERO;
        BigInteger amount = BigInteger.ZERO;
        if (decimals == 18) {
             bdAmount = Convert.fromWei(new BigDecimal(addrAmount), Convert.Unit.ETHER);
            amount = Convert.toWei(count, Convert.Unit.ETHER).toBigInteger();
        }
        logger.info("bdAmount{},Volume{}", bdAmount, amount);

        BigInteger addressAmount = TransactionClient.getBalance(fromAddress);
        logger.info("地址所有資產： " + addressAmount);
        if (addressAmount.compareTo(BigInteger.ZERO) < 0) return;

            String txId = TokenClient.sendTokenTransaction(admin, web3j, fromAddress, password, toAddress, contractAddress, amount);
        logger.info("{} transfer{} txid{}:", symbol, count, txId);
    }

    public void tranfer(String fromAddress, String toAddress, BigDecimal count) {
        String password = Environment.password;
        Web3j web3j = Web3j.build(new HttpService(Environment.RPC_URL));
        Admin admin = Admin.build(new HttpService(Environment.RPC_URL));
        String txId = TransactionClient.sendETH(fromAddress.trim(), toAddress, count);
        logger.info("{} transfer{} txid{}:", "eth", count, txId);
    }
}
