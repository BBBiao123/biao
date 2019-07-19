package com.biao.wallet.ethereum;

import com.biao.constant.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.*;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.admin.methods.response.PersonalUnlockAccount;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.core.methods.response.EthGasPrice;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.http.HttpService;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * 基于ERC20的代币
 */
public class TokenClient {


    private static Logger logger = LoggerFactory.getLogger(TokenClient.class);

    private static Web3j web3j;

    private static Admin admin;

    private static String fromAddress = "0x63b8165760e3207782cd8fc77a3f67d45c28625c";

    private static String contractAddress = "0x204fd80f6ad13e709dcbbf6f5e1f89b19f4b016d";

    private static String emptyAddress = "0x0000000000000000000000000000000000000000";
    public static String RPC_URL = "http://47.91.153.236:5555/";

    public static void main(String[] args) {

        web3j = Web3j.build(new HttpService(RPC_URL));
        admin = Admin.build(new HttpService(RPC_URL));
        //getTokenBalance(web3j, fromAddress, contractAddress);
        // System.out.println(getTokenName(web3j, contractAddress));
//        System.out.println(getTokenDecimals(web3j, contractAddress));
//        System.out.println(getTokenSymbol(web3j, contractAddress));
//        System.out.println(getTokenTotalSupply(web3j, contractAddress));
        // String   hashT = TokenClient.sendTokenTransaction(admin,web3j,"0x7d6c4ef9c621067fcae1f61dd082c56633943bda","123456789","0xab94f47aa58c948ee99dd2a338f30c27493b2db4",dvcAddr,amount);

        //GBI 归集
//        BigInteger amount = Convert.toWei(BigDecimal.valueOf(1000000), Convert.Unit.ETHER).toBigInteger();
//        String hs = sendTokenTransaction(admin, web3j,
//                "0x1ef1bf3cc394107be49a5ffe41cd29e3b7c7eb62", "123456789",
//                "0xab94f47aa58c948ee99dd2a338f30c27493b2db4", "0xceb8d6d39633fca9cdb37f7654f0c47294b97743",
//                amount);
//        System.out.println(hs);
//        System.out.println("end");

        BigInteger addrAmount = getTokenBalance(web3j, fromAddress, contractAddress);
        logger.info(addrAmount.toString());
    }

    /**
     * 查询代币余额
     */
    public static BigInteger getTokenBalance(Web3j web3j, String fromAddress, String contractAddress) {

        String methodName = "balanceOf";
        List<Type> inputParameters = new ArrayList<>();
        List<TypeReference<?>> outputParameters = new ArrayList<>();
        Address address = new Address(fromAddress);
        inputParameters.add(address);

        TypeReference<Uint256> typeReference = new TypeReference<Uint256>() {
        };
        outputParameters.add(typeReference);
        Function function = new Function(methodName, inputParameters, outputParameters);
        String data = FunctionEncoder.encode(function);
        Transaction transaction = Transaction.createEthCallTransaction(fromAddress, contractAddress, data);

        EthCall ethCall;
        BigInteger balanceValue = BigInteger.ZERO;
        try {
            ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).send();
            List<Type> results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
            results.stream().forEach(result -> logger.info("查询余额结果 ： "+  result.getTypeAsString() +"      " + result.getValue().toString()));
            balanceValue = (BigInteger) results.get(0).getValue();
        } catch (IOException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }

        return balanceValue;
    }

    /**
     * 查询代币名称
     *
     * @param web3j
     * @param contractAddress
     * @return
     */
    public static String getTokenName(Web3j web3j, String contractAddress) {
        String methodName = "name";
        String name = null;
        String fromAddr = emptyAddress;
        List<Type> inputParameters = new ArrayList<>();
        List<TypeReference<?>> outputParameters = new ArrayList<>();

        TypeReference<Utf8String> typeReference = new TypeReference<Utf8String>() {
        };
        outputParameters.add(typeReference);

        Function function = new Function(methodName, inputParameters, outputParameters);

        String data = FunctionEncoder.encode(function);
        Transaction transaction = Transaction.createEthCallTransaction(fromAddr, contractAddress, data);

        EthCall ethCall;
        try {
            ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get();
            List<Type> results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
            name = results.get(0).getValue().toString();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return name;
    }

    /**
     * 查询代币符号
     *
     * @param web3j
     * @param contractAddress
     * @return
     */
    public static String getTokenSymbol(Web3j web3j, String contractAddress) {
        String methodName = "symbol";
        String symbol = null;
        String fromAddr = emptyAddress;
        List<Type> inputParameters = new ArrayList<>();
        List<TypeReference<?>> outputParameters = new ArrayList<>();

        TypeReference<Utf8String> typeReference = new TypeReference<Utf8String>() {
        };
        outputParameters.add(typeReference);

        Function function = new Function(methodName, inputParameters, outputParameters);

        String data = FunctionEncoder.encode(function);
        Transaction transaction = Transaction.createEthCallTransaction(fromAddr, contractAddress, data);

        EthCall ethCall;
        try {
            ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get();
            List<Type> results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
            symbol = results.get(0).getValue().toString();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return symbol;
    }

    /**
     * 查询代币精度
     *
     * @param web3j
     * @param contractAddress
     * @return
     */
    public static int getTokenDecimals(Web3j web3j, String contractAddress) {
        String methodName = "decimals";
        String fromAddr = emptyAddress;
        int decimal = 0;
        List<Type> inputParameters = new ArrayList<>();
        List<TypeReference<?>> outputParameters = new ArrayList<>();

        TypeReference<Uint8> typeReference = new TypeReference<Uint8>() {
        };
        outputParameters.add(typeReference);

        Function function = new Function(methodName, inputParameters, outputParameters);

        String data = FunctionEncoder.encode(function);
        Transaction transaction = Transaction.createEthCallTransaction(fromAddr, contractAddress, data);

        EthCall ethCall;
        try {
            ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get();
            List<Type> results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
            decimal = Integer.parseInt(results.get(0).getValue().toString());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return decimal;
    }

    /**
     * 查询代币发行总量
     *
     * @param web3j
     * @param contractAddress
     * @return
     */
    public static BigInteger getTokenTotalSupply(Web3j web3j, String contractAddress) {
        String methodName = "totalSupply";
        String fromAddr = emptyAddress;
        BigInteger totalSupply = BigInteger.ZERO;
        List<Type> inputParameters = new ArrayList<>();
        List<TypeReference<?>> outputParameters = new ArrayList<>();

        TypeReference<Uint256> typeReference = new TypeReference<Uint256>() {
        };
        outputParameters.add(typeReference);

        Function function = new Function(methodName, inputParameters, outputParameters);

        String data = FunctionEncoder.encode(function);
        Transaction transaction = Transaction.createEthCallTransaction(fromAddr, contractAddress, data);

        EthCall ethCall;
        try {
            ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get();
            List<Type> results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
            totalSupply = (BigInteger) results.get(0).getValue();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return totalSupply;
    }

    /**
     * 代币转账
     */
    public static String sendTokenTransaction(Admin admin, Web3j web3j, String fromAddress, String password, String toAddress, String contractAddress, BigInteger amount) {
        String txHash = null;
        logger.info("代幣轉賬開始-----  " + fromAddress
                + " to  " + toAddress + " 數量： " + amount);
        try {
            BigInteger unlockDuration = BigInteger.valueOf(60L);
            PersonalUnlockAccount personalUnlockAccount = admin.personalUnlockAccount(
                    fromAddress, password, unlockDuration).send();
            if (personalUnlockAccount.accountUnlocked()) {
                logger.info(" 解鎖成功 --");
                String methodName = "transfer";
                List<Type> inputParameters = new ArrayList<>();
                List<TypeReference<?>> outputParameters = new ArrayList<>();
                Address tAddress = new Address(toAddress);
                Uint256 value = new Uint256(amount);
                inputParameters.add(tAddress);
                inputParameters.add(value);
                TypeReference<Bool> typeReference = new TypeReference<Bool>() {
                };
                outputParameters.add(typeReference);
                Function function = new Function(methodName, inputParameters, outputParameters);
                String data = FunctionEncoder.encode(function);
                EthGetTransactionCount ethGetTransactionCount = web3j
                        .ethGetTransactionCount(fromAddress, DefaultBlockParameterName.PENDING).sendAsync().get();
                BigInteger nonce = ethGetTransactionCount.getTransactionCount();
                //BigInteger gasPrice = Convert.toWei(BigDecimal.valueOf(6), Convert.Unit.GWEI).toBigInteger();
                EthGasPrice ethGasPrice = (EthGasPrice) web3j.ethGasPrice().send();
                Transaction transaction = Transaction.createFunctionCallTransaction(fromAddress, nonce, ethGasPrice.getGasPrice().multiply(BigInteger.valueOf(2)),
                        Constant.GAS_LIMIT, contractAddress, data);
                EthSendTransaction ethSendTransaction = web3j.ethSendTransaction(transaction).sendAsync().get();
                txHash = ethSendTransaction.getTransactionHash();
            } else {
                logger.info(" 解鎖失敗 --");
            }
        } catch (Exception e) {
            logger.info(e.getMessage());
            e.printStackTrace();
        }
        return txHash;
    }
}
