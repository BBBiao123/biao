package com.biao.wallet.ethereum;

import com.biao.init.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;

import java.io.IOException;
import java.math.BigDecimal;

public class TransferClient {


    private static Web3j web3j;
    private static Admin admin;
    private Credentials credentials;
    private static final Logger LOGGER = LoggerFactory.getLogger(TransferClient.class);

    static {
        web3j = Web3j.build(new HttpService(Environment.RPC_URL));
        admin = Admin.build(new HttpService(Environment.RPC_URL));
        Credentials credentials = null;
        try {
            credentials = WalletUtils.loadCredentials(Environment.password, Environment.WALLET_PATH);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        } catch (CipherException e) {
            LOGGER.error(e.getMessage());
        }
    }

    public TransactionReceipt sendEther(Credentials credentials, String toAddress, BigDecimal volume) {
        TransactionReceipt transactionReceipt = new TransactionReceipt();
        try {
            transactionReceipt = Transfer.sendFunds(web3j, credentials, toAddress, volume, Convert.Unit.ETHER)
                    .send();
            return transactionReceipt;
        } catch (Exception e) {
            return transactionReceipt;
        }
    }


}
