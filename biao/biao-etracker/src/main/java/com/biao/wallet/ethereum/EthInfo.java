package com.biao.wallet.ethereum;

import com.biao.constant.Constant;
import com.biao.init.Environment;
import com.biao.util.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.util.encoders.Hex;
import org.web3j.crypto.*;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.admin.methods.response.PersonalUnlockAccount;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.ClientTransactionManager;
import org.web3j.tx.Transfer;
import org.web3j.tx.response.NoOpProcessor;
import org.web3j.utils.Convert;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;

public class EthInfo {
    private static Web3j web3j;
    private static Admin admin;
    public static String WALLET_PATH = "D:\\UES\\UTC--2018-08-15T11-10-16.951935474Z--30188628a9f8345844adf08569863318bc0e6745";

    public static void main(String[] args) throws ParseException {
        LocalDateTime date=LocalDateTime.of(2019,12,30,00,00,00,0);
        System.out.println(DateUtils.parseLocalDateTime("2019-12-30 00:00:01"));
        System.out.println(date);
        System.out.println(LocalDateTime.now());
        System.out.println(StringUtils.isEmpty(""));
        BigInteger result1 = Convert.toWei(BigDecimal.ONE, Convert.Unit.MWEI).toBigInteger().multiply(BigInteger.valueOf(100L));
        System.out.println(result1);
        BigInteger result2 = Convert.toWei(BigDecimal.ONE, Convert.Unit.WEI).toBigInteger();
        System.out.println(result2);
    /*    String address = "0X28b061dba412da2cdb162f9ab56b8aea5613596f";
        BigInteger amout = TransactionClient.getBalance(address);
        String toAddress = "0x5db185359dcff8df8f6d19106b6237a30f913a20";

        String txId = TransactionClient.sendTransaction(address, Environment.password, toAddress, BigDecimal.valueOf(2.38));
        System.out.println(txId);
        System.out.println(amout);*/

        String url = "http://47.88.175.183:5555";
        web3j = Web3j.build(new HttpService(url));


        EthGetTransactionReceipt receipt = null;
        try {
            receipt = web3j.ethGetTransactionReceipt("0x1e1348d0b6dee6e6b5f75197ba9c3ef1c056cb5d6fd3c702daaa1ea0ddca4838").send();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String result = receipt.getResult().getStatus();

        if ("0x1".equals(result)) {
            System.out.println(result);
        } else {
            System.out.println(result);

        }


        Integer decimals = TokenClient.getTokenDecimals(web3j, "0x331a244a58cfc1356d94c5fdf09f349cc87de195");
        System.out.println(decimals);
        String rpcUrl = "http://47.88.175.183:5555";
        web3j = Web3j.build(new HttpService(rpcUrl));
        admin = Admin.build(new HttpService(rpcUrl));
        String fromAddress = "0x30188628a9f8345844adf08569863318bc0e6745";
        String toAddress = "0xacf9DD9A0E677184c547fDf22C35358abb7C72ba";
        String uesAddr = "0x479Bb409743D07cF47e2cf692BfF62d174e72AF1";
        String dvcAddr = "0x331a244a58cfc1356d94c5fdf09f349cc87de195";
        String powrAddress = "0x595832f8fc6bf59c85c527fec3740a1b7a361269";

        BigInteger addrAmount = TokenClient.getTokenBalance(web3j, "0xab94f47aa58c948ee99dd2a338f30c27493b2db4", dvcAddr);
        BigInteger amount = Convert.toWei(BigDecimal.valueOf(70), Convert.Unit.WEI).toBigInteger();
//        BigInteger amount = Convert.toWei(BigDecimal.valueOf(5), Convert.Unit.ETHER).toBigInteger();
        //String   hashT = TokenClient.sendTokenTransaction(admin,web3j,"0xab94f47aa58c948ee99dd2a338f30c27493b2db4","123456789","0x5d027a385f9f4c04f78b96e389336b1a7dca6843",dvcAddr,amount);
        // Integer decimals = TokenClient.getTokenDecimals(web3j,"0xceb8d6d39633fca9cdb37f7654f0c47294b97743");
        // System.out.println(decimals);
        //getEthInfo();
        String txHash = sendETH(rpcUrl, fromAddress, toAddress, BigDecimal.valueOf(0.01));
        System.out.println(txHash);
        try {
            amount = Convert.toWei(BigDecimal.valueOf(0.0001), Convert.Unit.ETHER).toBigInteger();
            String wallet_path = "D:/UES/UTC--2018-08-15T11-10-16.951935474Z--30188628a9f8345844adf08569863318bc0e6745";
            // Credentials credentials = WalletUtils.loadCredentials(Environment.ETH_ACCOUNT_PWD, wallet_path);
            send(rpcUrl, wallet_path, fromAddress, toAddress, amount);
            toAddress = "0x68D0D42eFf127A6ee3f6b8783dd535f5b5CB0161";
            // use(rpcUrl,credentials,uesAddr,toAddress,amount);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CipherException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private static String use(String RPC_URL, Credentials credentials, String contractAddress, String toAddress, BigInteger amount) {
        Web3j web3j = Web3j.build(new HttpService(RPC_URL));
        TokenERC20 contract = TokenERC20.load(contractAddress, web3j, credentials,
                Convert.toWei("10", Convert.Unit.GWEI).toBigInteger(),
                BigInteger.valueOf(100000));
        String hash = "";
        try {
            // BigInteger balance = contract.balanceOf(myAddress).send();
            TransactionReceipt receipt = contract.transfer(toAddress, amount).send();
            hash = receipt.getBlockHash();
            System.out.println(hash);
            TransactionReceipt transactionReceipt = Transfer.sendFunds(
                    web3j, credentials, toAddress,
                    BigDecimal.valueOf(1.0), Convert.Unit.ETHER)
                    .send();
            //etc..
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hash;
    }


    private static String send(String rpcUrl, String wallet_path, String fromAddress, String toAddress, BigInteger value) throws IOException, CipherException, ExecutionException, InterruptedException {

        Web3j web3j = Web3j.build(new HttpService(rpcUrl));  // defaults to http://localhost:8545/
        Credentials credentials = WalletUtils.loadCredentials(Environment.ETH_ACCOUNT_PWD, wallet_path);

        // get the next available nonce
        EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(
                fromAddress, DefaultBlockParameterName.LATEST).sendAsync().get();
        BigInteger nonce = ethGetTransactionCount.getTransactionCount();

        // create our transaction
        RawTransaction rawTransaction = RawTransaction.createEtherTransaction(
                nonce, Constant.GAS_PRICE, Constant.GAS_LIMIT, toAddress, value);

        // sign & send our transaction
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        String hexValue = Hex.toHexString(signedMessage);
        EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).send();
        return ethSendTransaction.getTransactionHash();
    }


    private static String sendETH(String rpcUrl, String fromAddress, String toAddress, BigDecimal amount) {
        Web3j web3j = Web3j.build(new HttpService(rpcUrl));
        String txhash = "";
        try {
            PersonalUnlockAccount personalUnlockAccount = admin.personalUnlockAccount(fromAddress, Environment.password).send();
            if (!personalUnlockAccount.accountUnlocked()) {
                System.out.println("unlock fail");
            }
            Transfer transfer = new Transfer(web3j, new ClientTransactionManager(web3j, fromAddress, new NoOpProcessor(web3j)));
            TransactionReceipt receipt = transfer.sendFunds(toAddress, amount, Convert.Unit.ETHER).send();
            txhash = receipt.getTransactionHash();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return txhash;
    }

    /**
     * 请求区块链的信息
     */
    private static void getEthInfo() {

        Web3ClientVersion web3ClientVersion = null;
        try {
            //客户端版本
            web3ClientVersion = web3j.web3ClientVersion().send();
            String clientVersion = web3ClientVersion.getWeb3ClientVersion();
            System.out.println("clientVersion " + clientVersion);
            //区块数量
            EthBlockNumber ethBlockNumber = web3j.ethBlockNumber().send();
            BigInteger blockNumber = ethBlockNumber.getBlockNumber();// 5671814
            System.out.println(blockNumber);
            //挖矿奖励账户
            EthCoinbase ethCoinbase = web3j.ethCoinbase().send();
            String coinbaseAddress = ethCoinbase.getAddress();
            System.out.println(coinbaseAddress);
            //是否在同步区块
            EthSyncing ethSyncing = web3j.ethSyncing().send();
            boolean isSyncing = ethSyncing.isSyncing();
            System.out.println(isSyncing);
            //是否在挖矿
            EthMining ethMining = web3j.ethMining().send();
            boolean isMining = ethMining.isMining();
            System.out.println(isMining);
            //当前gas price
            EthGasPrice ethGasPrice = web3j.ethGasPrice().send();
            BigInteger gasPrice = ethGasPrice.getGasPrice();
            System.out.println(gasPrice);
            //挖矿速度
            EthHashrate ethHashrate = web3j.ethHashrate().send();
            BigInteger hashRate = ethHashrate.getHashrate();
            System.out.println(hashRate);
            //协议版本
            EthProtocolVersion ethProtocolVersion = web3j.ethProtocolVersion().send();
            String protocolVersion = ethProtocolVersion.getProtocolVersion();
            System.out.println(protocolVersion);
            //连接的节点数
            NetPeerCount netPeerCount = web3j.netPeerCount().send();
            BigInteger peerCount = netPeerCount.getQuantity();
            System.out.println(peerCount);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
